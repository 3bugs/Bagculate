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

$sql = "SELECT * FROM `bagculate_object`";
if ($result = $db->query($sql)) {
    $objectList = array();
    while ($row = $result->fetch_assoc()) {
        $object = array();
        $object['id'] = (int)$row['id'];
        $object['name'] = $row['name'];
        $object['weight'] = $row['weight'];
        $object['type'] = $row['type'];
        array_push($objectList, $object);
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
            .object-image {
                border-radius: 50%;
                border: 1px solid black;
            }
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
                    <h4 class="modal-title">เพิ่มสิ่งของ</h4>
                </div>
                <div class="modal-body">
                    <form id="formAddObject" role="form"
                          style="margin-top: 0; margin-bottom: 0">
                        <div class="box-body">

                            <!--ชื่อสิ่งของ-->
                            <div class="form-group">
                                <label for="inputObjectName">ชื่อสิ่งของ:</label>
                                <div class="input-group">
                                    <span class="input-group-addon">
                                        <i class="fa fa-font"></i>
                                    </span>
                                    <input type="text" class="form-control"
                                           id="inputObjectName"
                                           placeholder="กรอกชื่อสิ่งของ" required
                                           oninvalid="this.setCustomValidity('กรอกชื่อสิ่งของ')"
                                           oninput="this.setCustomValidity('')">
                                </div>
                            </div>
                            <!--น้ำหนัก-->
                            <div class="form-group">
                                <label for="inputObjectWeight">น้ำหนัก (กรัม):</label>
                                <div class="input-group">
                                    <span class="input-group-addon">
                                        <i class="fa fa-hashtag"></i>
                                    </span>
                                    <input type="number" class="form-control"
                                           id="inputObjectWeight"
                                           placeholder="กรอกน้ำหนักสิ่งของในหน่วยกรัม" required
                                           oninvalid="this.setCustomValidity('กรอกน้ำหนักสิ่งของในหน่วยกรัม และห้ามมีจุดทศนิยม')"
                                           oninput="this.setCustomValidity('')">
                                </div>
                            </div>
                            <!--ประเภท-->
                            <div class="form-group">
                                <label for="selectObjectType">ประเภท:</label>
                                <div class="input-group">
                                    <span class="input-group-addon">
                                        <i class="fa fa-tag"></i>
                                    </span>
                                    <select class="form-control"
                                            id="selectObjectType" required
                                            oninvalid="this.setCustomValidity('เลือกประเภทสิ่งของ')"
                                            oninput="this.setCustomValidity('')">
                                        <option value="" disabled selected>เลือกประเภทสิ่งของ</option>
                                        <option value="shirt">เสื้อ</option>
                                        <option value="pants">กางเกง</option>
                                        <option value="shoes">รองเท้า</option>
                                        <option value="thing">ของใช้</option>
                                        <option value="etc">อื่นๆ</option>
                                    </select>
                                </div>
                            </div>

                            <div id="addObjectResult"
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
    <div class="modal fade" id="editObjectModal" role="dialog">
        <div class="modal-dialog modal-md">
            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;
                    </button>
                    <h4 class="modal-title">แก้ไขสิ่งของ</h4>
                </div>
                <div class="modal-body">
                    <form id="formEditObject" role="form"
                          style="margin-top: 0; margin-bottom: 0">
                        <div class="box-body">

                            <input type="hidden" id="inputObjectId">

                            <!--ชื่อสิ่งของ-->
                            <div class="form-group">
                                <label for="inputObjectName">ชื่อสิ่งของ:</label>
                                <div class="input-group">
                                    <span class="input-group-addon">
                                        <i class="fa fa-font"></i>
                                    </span>
                                    <input type="text" class="form-control"
                                           id="inputObjectName"
                                           placeholder="กรอกชื่อสิ่งของ" required
                                           oninvalid="this.setCustomValidity('กรอกชื่อสิ่งของ')"
                                           oninput="this.setCustomValidity('')">
                                </div>
                            </div>
                            <!--น้ำหนัก-->
                            <div class="form-group">
                                <label for="inputObjectWeight">น้ำหนัก (กรัม):</label>
                                <div class="input-group">
                                    <span class="input-group-addon">
                                        <i class="fa fa-hashtag"></i>
                                    </span>
                                    <input type="number" class="form-control"
                                           id="inputObjectWeight"
                                           placeholder="กรอกน้ำหนักสิ่งของในหน่วยกรัม" required
                                           oninvalid="this.setCustomValidity('กรอกน้ำหนักสิ่งของในหน่วยกรัม และห้ามมีจุดทศนิยม')"
                                           oninput="this.setCustomValidity('')">
                                </div>
                            </div>
                            <!--ประเภท-->
                            <div class="form-group">
                                <label for="selectObjectType">ประเภท:</label>
                                <div class="input-group">
                                    <span class="input-group-addon">
                                        <i class="fa fa-tag"></i>
                                    </span>
                                    <select class="form-control"
                                            id="selectObjectType" required
                                            oninvalid="this.setCustomValidity('เลือกประเภทสิ่งของ')"
                                            oninput="this.setCustomValidity('')">
                                        <option value="" disabled selected>เลือกประเภทสิ่งของ</option>
                                        <option value="shirt">เสื้อ</option>
                                        <option value="pants">กางเกง</option>
                                        <option value="shoes">รองเท้า</option>
                                        <option value="thing">ของใช้</option>
                                        <option value="etc">อื่นๆ</option>
                                    </select>
                                </div>
                            </div>

                            <div id="editObjectResult"
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
                    สิ่งของ
                </h1>
            </section>

            <!-- Main content: ข้อมูลสิ่งของ -->
            <section class="content">
                <div class="row">
                    <div class="col-xs-12">
                        <div class="box box-info">
                            <div class="box-header with-border">
                                <h3 class="box-title">&nbsp;</h3>
                                <button type="button" class="btn btn-success pull-right"
                                        data-toggle="modal" data-target="#addBagModal">
                                    <span class="fa fa-plus"></span>&nbsp;
                                    เพิ่มสิ่งของ
                                </button>
                            </div>
                            <div class="box-body">
                                <table id="tableBag" class="table table-bordered table-striped">
                                    <thead>
                                    <tr>
                                        <th style="width: 50%; text-align: center">ชื่อสิ่งของ</th>
                                        <th style="width: 30%; text-align: center">น้ำหนัก (กรัม)</th>
                                        <th style="text-align: center" nowrap>&nbsp;</th>
                                        <th style="width: 20%; text-align: center">ประเภท</th>
                                        <th style="text-align: center" nowrap>จัดการ</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <?php
                                    if (sizeof($objectList) == 0) {
                                        ?>
                                        <tr valign="middle">
                                            <td colspan="10" align="center">ไม่มีข้อมูลสิ่งของ</td>
                                        </tr>
                                        <?php
                                    } else {
                                        foreach ($objectList as $object) {
                                            $objectId = $object['id'];
                                            $objectName = $object['name'];
                                            $objectWeight = $object['weight'];
                                            $objectType = $object['type'];
                                            $objectTypeText = '';
                                            $objectTypeImageFileName = '';
                                            switch ($objectType) {
                                                case 'shirt':
                                                    $objectTypeText = 'เสื้อ';
                                                    $objectTypeImageFileName = 'ic_type_shirt.jpg';
                                                    break;
                                                case 'pants':
                                                    $objectTypeText = 'กางเกง';
                                                    $objectTypeImageFileName = 'ic_type_pants.jpg';
                                                    break;
                                                case 'shoes':
                                                    $objectTypeText = 'รองเท้า';
                                                    $objectTypeImageFileName = 'ic_type_shoes.jpg';
                                                    break;
                                                case 'thing':
                                                    $objectTypeText = 'ของใช้';
                                                    $objectTypeImageFileName = 'ic_type_thing.jpg';
                                                    break;
                                                case 'etc':
                                                    $objectTypeText = 'อื่นๆ';
                                                    $objectTypeImageFileName = 'ic_type_etc.jpg';
                                                    break;
                                            }
                                            ?>
                                            <tr style="">
                                                <td style="vertical-align: top; text-align: center"><?= $objectName; ?></td>
                                                <td style="vertical-align: top; text-align: center"><?= $objectWeight; ?></td>
                                                <td style="vertical-align: top; text-align: center">
                                                    <span style="display: none"><?= $objectTypeText; ?></span>
                                                    <img src="images/<?= $objectTypeImageFileName; ?>"
                                                         class="object-image"
                                                         title="<?= $objectTypeText; ?>"
                                                         alt="<?= $objectTypeText; ?>"
                                                         height="50px"
                                                         style="margin-left: 10px; margin-right: 10px"/>
                                                </td>
                                                <td style="vertical-align: top; text-align: center"><?= $objectTypeText; ?></td>
                                                <td style="text-align: center" nowrap>
                                                    <button type="button" class="btn btn-warning"
                                                            style="margin-left: 6px; margin-right: 3px;"
                                                            onclick="onClickEdit(this, <?= $objectId; ?>, '<?= $objectName; ?>', '<?= $objectWeight; ?>', '<?= $objectType; ?>')">
                                                        <span class="fa fa-edit"></span>&nbsp;
                                                        แก้ไข
                                                    </button>
                                                    <button type="button" class="btn btn-danger"
                                                            style="margin-left: 3px; margin-right: 6px;"
                                                            onclick="onClickDelete(this, <?= $objectId; ?>, '<?= $objectName; ?>', '<?= $objectWeight; ?>')">
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

            $('#formAddObject').submit(event => {
                event.preventDefault();
                doAddObject();
            });
            $('#formEditObject').submit(event => {
                event.preventDefault();
                doUpdateObject();
            });
        });

        function onClickEdit(element, objectId, objectName, objectWeight, objectType) {
            $('#formEditObject #inputObjectId').val(objectId);
            $('#formEditObject #inputObjectName').val(objectName);
            $('#formEditObject #inputObjectWeight').val(objectWeight);
            $('#formEditObject #selectObjectType').val(objectType);
            $('#formEditObject #editObjectResult').text('');
            $('#editObjectModal').modal('show');
        }

        function onClickDelete(element, objectid, objectName, objectWeight) {
            BootstrapDialog.show({
                title: 'Confirm Delete Object',
                message: 'ยืนยันลบสิ่งของ ' + objectName + ' (น้ำหนัก ' + objectWeight + ' กรัม)?',
                buttons: [{
                    label: 'ลบ',
                    action: function (self) {
                        doDeleteBag(objectid);
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

        function doDeleteBag(objectId) {
            $.post(
                'api/api.php/delete_object',
                {
                    objectId: objectId,
                }
            ).done(function (data) {
                if (data.error_code === 0) {
                    location.reload(true);
                } else {
                    BootstrapDialog.show({
                        title: 'Delete Object',
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
                    title: 'Delete Object',
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

        function doAddObject() {
            $.post(
                'api/api.php/add_object',
                {
                    objectName: $('#formAddObject #inputObjectName').val(),
                    objectWeight: $('#formAddObject #inputObjectWeight').val(),
                    objectType: $('#formAddObject #selectObjectType').val(),
                }
            ).done(function (data) {
                if (data.error_code === 0) {
                    location.reload(true);
                } else {
                    $('#formAddObject #addObjectResult').text(data.error_message);
                }
            }).fail(function () {
                $('#formAddObject #addObjectResult').text('เกิดข้อผิดพลาดในการเชื่อมต่อ Server');
            });
        }

        function doUpdateObject() {
            $.post(
                'api/api.php/update_object',
                {
                    objectId: $('#formEditObject #inputObjectId').val(),
                    objectName: $('#formEditObject #inputObjectName').val(),
                    objectWeight: $('#formEditObject #inputObjectWeight').val(),
                    objectType: $('#formEditObject #selectObjectType').val(),
                }
            ).done(function (data) {
                if (data.error_code === 0) {
                    location.reload(true);
                } else {
                    $('#formEditObject #editObjectResult').text(data.error_message);
                }
            }).fail(function () {
                $('#formEditObject #editObjectResult').text('เกิดข้อผิดพลาดในการเชื่อมต่อ Server');
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