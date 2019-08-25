<?php
require_once 'api/global.php';

error_reporting(E_ERROR | E_PARSE);
header('Content-type: text/html; charset=utf-8');
header('Expires: Sun, 01 Jan 2014 00:00:00 GMT');
header('Cache-Control: no-store, no-cache, must-revalidate');
header('Cache-Control: post-check=0, pre-check=0', FALSE);
header('Pragma: no-cache');

session_start();
if (!isset($_SESSION[KEY_SESSION_USER_ID])) {
    header('Location: login.php');
    exit();
}

require_once 'api/db_config.php';
$db = new mysqli(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);

if ($db->connect_errno) {
    echo 'การเชื่อมต่อฐานข้อมูลล้มเหลว!';
    exit();
}
$db->set_charset("utf8");

$sql = "SELECT * FROM `bagculate_bag`";
if ($result = $db->query($sql)) {
    $bagList = array();
    while ($row = $result->fetch_assoc()) {
        $bag = array();
        $bag['id'] = (int)$row['id'];
        $bag['name'] = $row['name'];
        $bag['weight'] = $row['weight'];
        $bag['type'] = (int)$row['type'];
        array_push($bagList, $bag);
    }
    $result->close();
} else {
    echo 'เกิดข้อผิดพลาดในการเชื่อมต่อฐานข้อมูล';
    exit();
}

?>
    <!DOCTYPE html>
    <html lang="th">
    <head>
        <?php require_once('include/head.inc'); ?>
        <!-- DataTables -->
        <link rel="stylesheet" href="bower_components/datatables.net-bs/css/dataTables.bootstrap.min.css">
        <style>

        </style>
    </head>
    <body class="hold-transition skin-blue sidebar-mini">

    <!-- Add Bag Modal -->
    <div class="modal fade" id="addBagModal" role="dialog">
        <div class="modal-dialog modal-md">
            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;
                    </button>
                    <h4 class="modal-title">เพิ่มกระเป๋าเดินทาง</h4>
                </div>
                <div class="modal-body">
                    <form id="formAddBag" role="form"
                          style="margin-top: 0; margin-bottom: 0">
                        <div class="box-body">

                            <!--ชื่อกระเป๋า-->
                            <div class="form-group">
                                <label for="inputBagName">ชื่อกระเป๋า:</label>
                                <div class="input-group">
                                    <span class="input-group-addon">
                                        <i class="fa fa-font"></i>
                                    </span>
                                    <input type="text" class="form-control"
                                           id="inputBagName"
                                           placeholder="กรอกชื่อกระเป๋า" required
                                           oninvalid="this.setCustomValidity('กรอกชื่อกระเป๋า')"
                                           oninput="this.setCustomValidity('')">
                                </div>
                            </div>
                            <!--น้ำหนัก-->
                            <div class="form-group">
                                <label for="inputBagWeight">น้ำหนัก (กิโลกรัม):</label>
                                <div class="input-group">
                                    <span class="input-group-addon">
                                        <i class="fa fa-hashtag"></i>
                                    </span>
                                    <input type="number" class="form-control"
                                           id="inputBagWeight" step="0.01"
                                           placeholder="กรอกน้ำหนักกระเป๋าในหน่วยกิโลกรัม" required
                                           oninvalid="this.setCustomValidity('กรอกน้ำหนักกระเป๋าในหน่วยกิโลกรัม')"
                                           oninput="this.setCustomValidity('')">
                                </div>
                            </div>
                            <!--ประเภท-->
                            <div class="form-group">
                                <label for="selectBagType">ประเภท:</label>
                                <div class="input-group">
                                    <span class="input-group-addon">
                                        <i class="fa fa-tag"></i>
                                    </span>
                                    <select class="form-control"
                                            id="selectBagType" required
                                            oninvalid="this.setCustomValidity('เลือกประเภทกระเป๋า')"
                                            oninput="this.setCustomValidity('')">
                                        <option value="" disabled selected>เลือกประเภทกระเป๋า</option>
                                        <option value="0">สะพาย</option>
                                        <option value="1">ล้อลาก</option>
                                    </select>
                                </div>
                            </div>

                            <div id="addBagResult"
                                 style="text-align: center; color: red; margin-top: 25px; margin-bottom: 20px;">
                            </div>
                        </div>
                        <!-- /.box-body -->
                        <div class="box-footer">
                            <button id="buttonSave" type="submit"
                                    class="btn btn-info pull-right">
                                <span class="fa fa-save"></span>&nbsp;
                                บันทึก
                            </button>
                        </div>
                        <!-- /.box-footer -->
                    </form>
                </div>
                <!--<div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                </div>-->
            </div>
        </div>
    </div>

    <!-- Edit Bag Modal -->
    <div class="modal fade" id="editBagModal" role="dialog">
        <div class="modal-dialog modal-md">
            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;
                    </button>
                    <h4 class="modal-title">แก้ไขกระเป๋าเดินทาง</h4>
                </div>
                <div class="modal-body">
                    <form id="formEditBag" role="form"
                          style="margin-top: 0; margin-bottom: 0">
                        <div class="box-body">

                            <input type="hidden" id="inputBagId">

                            <!--ชื่อกระเป๋า-->
                            <div class="form-group">
                                <label for="inputBagName">ชื่อกระเป๋า:</label>
                                <div class="input-group">
                                    <span class="input-group-addon">
                                        <i class="fa fa-font"></i>
                                    </span>
                                    <input type="text" class="form-control"
                                           id="inputBagName"
                                           placeholder="กรอกชื่อกระเป๋า" required
                                           oninvalid="this.setCustomValidity('กรอกชื่อกระเป๋า')"
                                           oninput="this.setCustomValidity('')">
                                </div>
                            </div>
                            <!--น้ำหนัก-->
                            <div class="form-group">
                                <label for="inputBagWeight">น้ำหนัก (กิโลกรัม):</label>
                                <div class="input-group">
                                    <span class="input-group-addon">
                                        <i class="fa fa-hashtag"></i>
                                    </span>
                                    <input type="number" class="form-control"
                                           id="inputBagWeight" step="0.01"
                                           placeholder="กรอกน้ำหนักกระเป๋าในหน่วยกิโลกรัม" required
                                           oninvalid="this.setCustomValidity('กรอกน้ำหนักกระเป๋าในหน่วยกิโลกรัม')"
                                           oninput="this.setCustomValidity('')">
                                </div>
                            </div>
                            <!--ประเภท-->
                            <div class="form-group">
                                <label for="selectBagType">ประเภท:</label>
                                <div class="input-group">
                                    <span class="input-group-addon">
                                        <i class="fa fa-tag"></i>
                                    </span>
                                    <select class="form-control"
                                            id="selectBagType" required
                                            oninvalid="this.setCustomValidity('เลือกประเภทกระเป๋า')"
                                            oninput="this.setCustomValidity('')">
                                        <option value="" disabled selected>เลือกประเภทกระเป๋า</option>
                                        <option value="0">สะพาย</option>
                                        <option value="1">ล้อลาก</option>
                                    </select>
                                </div>
                            </div>

                            <div id="editBagResult"
                                 style="text-align: center; color: red; margin-top: 25px; margin-bottom: 20px;">
                            </div>
                        </div>
                        <!-- /.box-body -->
                        <div class="box-footer">
                            <button id="buttonEditSave" type="submit"
                                    class="btn btn-info pull-right">
                                <span class="fa fa-save"></span>&nbsp;
                                บันทึก
                            </button>
                        </div>
                        <!-- /.box-footer -->
                    </form>
                </div>
                <!--<div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                </div>-->
            </div>
        </div>
    </div>

    <div class="wrapper">
        <?php require_once('include/header.inc'); ?>
        <?php require_once('include/sidebar.inc'); ?>

        <!-- Content Wrapper. Contains page content -->
        <div class="content-wrapper">
            <!-- Content Header (Page header) -->
            <section class="content-header">
                <h1>
                    กระเป๋าเดินทาง
                </h1>
            </section>

            <!-- Main content: ข้อมูลกระเป๋าเดินทาง -->
            <section class="content">
                <div class="row">
                    <div class="col-xs-12">
                        <div class="box box-info">
                            <div class="box-header with-border">
                                <h3 class="box-title">&nbsp;</h3>
                                <button type="button" class="btn btn-success pull-right"
                                        data-toggle="modal" data-target="#addBagModal">
                                    <span class="fa fa-plus"></span>&nbsp;
                                    เพิ่มกระเป๋าเดินทาง
                                </button>
                            </div>
                            <div class="box-body">
                                <table id="tableBag" class="table table-bordered table-striped">
                                    <thead>
                                    <tr>
                                        <th style="width: 50%; text-align: center">ชื่อกระเป๋า</th>
                                        <th style="width: 30%; text-align: center">น้ำหนัก (กิโลกรัม)</th>
                                        <th style="width: 20%; text-align: center">ประเภท</th>
                                        <th style="text-align: center" nowrap>จัดการ</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <?php
                                    if (sizeof($bagList) == 0) {
                                        ?>
                                        <tr valign="middle">
                                            <td colspan="10" align="center">ไม่มีข้อมูลกระเป๋าเดินทาง</td>
                                        </tr>
                                        <?php
                                    } else {
                                        foreach ($bagList as $bag) {
                                            $bagId = $bag['id'];
                                            $bagName = $bag['name'];
                                            $bagWeight = $bag['weight'];
                                            $bagType = $bag['type'];
                                            ?>
                                            <tr style="">
                                                <td style="vertical-align: top; text-align: center"><?= $bagName; ?></td>
                                                <td style="vertical-align: top; text-align: center"><?= $bagWeight; ?></td>
                                                <td style="vertical-align: top; text-align: center"><?= $bagType === 0 ? 'สะพาย' : 'ล้อลาก'; ?></td>
                                                <td style="text-align: center" nowrap>
                                                    <button type="button" class="btn btn-warning"
                                                            style="margin-left: 6px; margin-right: 3px;"
                                                            onclick="onClickEdit(this, <?= $bagId; ?>, '<?= $bagName; ?>', '<?= $bagWeight; ?>', <?= $bagType; ?>)">
                                                        <span class="fa fa-edit"></span>&nbsp;
                                                        แก้ไข
                                                    </button>
                                                    <button type="button" class="btn btn-danger"
                                                            style="margin-left: 3px; margin-right: 6px;"
                                                            onclick="onClickDelete(this, <?= $bagId; ?>, '<?= $bagName; ?>', '<?= $bagWeight; ?>')">
                                                        <span class="fa fa-remove"></span>&nbsp;
                                                        ลบ
                                                    </button>
                                                </td>
                                            </tr>
                                            <?php
                                        }
                                    }
                                    ?>
                                    </tbody>
                                </table>
                            </div>
                            <!-- /.box-body -->
                        </div>
                        <!-- /.box -->
                    </div>
                    <!-- /.col -->
                </div>
                <!-- /.row -->
            </section>
            <!-- /.content -->
        </div>
        <!-- /.content-wrapper -->

        <?php require_once('include/footer.inc'); ?>
    </div>
    <!-- ./wrapper -->

    <script>
        $(document).ready(function () {
            $('#tableBag').DataTable({
                stateSave: true,
                stateDuration: -1, // sessionStorage
                order: [[2, 'desc']],
                language: {
                    lengthMenu: "แสดงหน้าละ _MENU_ แถวข้อมูล",
                    zeroRecords: "ไม่มีข้อมูล",
                    emptyTable: "ไม่มีข้อมูล",
                    info: "หน้าที่ _PAGE_ จากทั้งหมด _PAGES_ หน้า",
                    infoEmpty: "แสดง 0 แถวข้อมูล",
                    infoFiltered: "(กรองจากทั้งหมด _MAX_ แถวข้อมูล)",
                    search: "ค้นหา:",
                    thousands: ",",
                    loadingRecords: "รอสักครู่...",
                    processing: "กำลังประมวลผล...",
                    paginate: {
                        first: "หน้าแรก",
                        last: "หน้าสุดท้าย",
                        next: "ถัดไป",
                        previous: "ก่อนหน้า"
                    },
                }
            });

            $('#formAddBag').submit(event => {
                event.preventDefault();
                doAddBag();
            });
            $('#formEditBag').submit(event => {
                event.preventDefault();
                doUpdateBag();
            });
        });

        function onClickEdit(element, bagId, bagName, bagWeight, bagType) {
            $('#formEditBag #inputBagId').val(bagId);
            $('#formEditBag #inputBagName').val(bagName);
            $('#formEditBag #inputBagWeight').val(bagWeight);
            $('#formEditBag #selectBagType').val(bagType);
            $('#formEditBag #editBagResult').text('');
            $('#editBagModal').modal('show');
        }

        function onClickDelete(element, bagId, bagName, bagWeight) {
            BootstrapDialog.show({
                title: 'Confirm Delete Bag',
                message: 'ยืนยันลบกระเป๋า ' + bagName + ' (น้ำหนัก ' + bagWeight + ' กิโลกรัม)?',
                buttons: [{
                    label: 'ลบ',
                    action: function (self) {
                        doDeleteBag(bagId);
                        self.close();
                    },
                    cssClass: 'btn-primary'
                }, {
                    label: 'ยกเลิก',
                    action: function (self) {
                        self.close();
                    }
                }]
            });
        }

        function doDeleteBag(bagId) {
            $.post(
                'api/api.php/delete_bag',
                {
                    bagId: bagId,
                }
            ).done(function (data) {
                if (data.error_code === 0) {
                    location.reload(true);
                } else {
                    BootstrapDialog.show({
                        title: 'Delete Bag',
                        message: data.error_message,
                        buttons: [{
                            label: 'ปิด',
                            action: function (self) {
                                self.close();
                            }
                        }]
                    });
                }
            }).fail(function () {
                BootstrapDialog.show({
                    title: 'Delete Bag',
                    message: 'เกิดข้อผิดพลาดในการเชื่อมต่อ Server',
                    buttons: [{
                        label: 'ปิด',
                        action: function (self) {
                            self.close();
                        }
                    }]
                });
            });
        }

        function doAddBag() {
            $.post(
                'api/api.php/add_bag',
                {
                    bagName: $('#formAddBag #inputBagName').val(),
                    bagWeight: $('#formAddBag #inputBagWeight').val(),
                    bagType: $('#formAddBag #selectBagType').val(),
                }
            ).done(function (data) {
                if (data.error_code === 0) {
                    location.reload(true);
                } else {
                    $('#formAddBag #addBagResult').text(data.error_message);
                }
            }).fail(function () {
                $('#formAddBag #addBagResult').text('เกิดข้อผิดพลาดในการเชื่อมต่อ Server');
            });
        }

        function doUpdateBag() {
            $.post(
                'api/api.php/update_bag',
                {
                    bagId: $('#formEditBag #inputBagId').val(),
                    bagName: $('#formEditBag #inputBagName').val(),
                    bagWeight: $('#formEditBag #inputBagWeight').val(),
                    bagType: $('#formEditBag #selectBagType').val(),
                }
            ).done(function (data) {
                if (data.error_code === 0) {
                    location.reload(true);
                } else {
                    $('#formEditBag #editBagResult').text(data.error_message);
                }
            }).fail(function () {
                $('#formEditBag #editBagResult').text('เกิดข้อผิดพลาดในการเชื่อมต่อ Server');
            });
        }
    </script>

    <?php require_once('include/foot.inc'); ?>
    
    <!-- DataTables -->
    <script src="bower_components/datatables.net/js/jquery.dataTables.min.js"></script>
    <script src="bower_components/datatables.net-bs/js/dataTables.bootstrap.min.js"></script>
    </body>
    </html>

<?php
$db->close();

function valueOrEmptyString($value)
{
    return isset($value) ? $value : '';
}

?>