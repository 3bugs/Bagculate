<?php
require_once 'api/global.php';

error_reporting(E_ERROR | E_PARSE);
header('Content-type: text/html; charset=utf-8');
header('Expires: Sun, 01 Jan 2014 00:00:00 GMT');
header('Cache-Control: no-store, no-cache, must-revalidate');
header('Cache-Control: post-check=0, pre-check=0', FALSE);
header('Pragma: no-cache');

session_start();
if (isset($_SESSION[KEY_SESSION_USER_ID])) {
    header('Location: index.php');
    exit();
}

?>

<!DOCTYPE html>
<html lang="th">
<head>
    <?php require_once('include/head.inc'); ?>
    <style>
        body {
            background: url(images/bagculate_bg.jpg);
            background-repeat: no-repeat;
            background-size: cover;
        }
    </style>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div>
    <?php
    if (isset($_SESSION[KEY_SESSION_USER_ID])) {
        require_once('include/header.inc');
        require_once('include/sidebar.inc');
    }
    ?>

    <h1 style="margin-top: 100px; text-align: center; color: white">ระบบหลังบ้าน Bagculate</h1>

    <div id="loginbox" style="margin-top: 30px;" class="mainbox col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">
        <div class="panel panel-info">
            <div class="panel-heading">
                <div class="panel-title">ลงชื่อเข้าสู่ระบบ</div>
                <!--<div style="float:right; font-size: 80%; position: relative; top:-10px"><a href="#">Forgot password?</a></div>-->
            </div>

            <div style="padding-top:30px;" class="panel-body">
                <form id="loginForm" class="form-horizontal" role="form">
                    <div style="margin-bottom: 25px" class="input-group">
                        <span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
                        <input id="usernameInput" type="text" class="form-control" name="username" value="" placeholder="ชื่อผู้ใช้งาน">
                    </div>

                    <div style="margin-bottom: 25px" class="input-group">
                        <span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
                        <input id="passwordInput" type="password" class="form-control" name="password" placeholder="รหัสผ่าน">
                    </div>

                    <!--<div class="input-group">
                        <div class="checkbox">
                            <label>
                                <input id="login-remember" type="checkbox" name="remember" value="1"> Remember me
                            </label>
                        </div>
                    </div>-->

                    <div style="margin-top:10px" class="form-group">
                        <!-- Button -->
                        <div class="col-sm-12 controls">
                            <button id="loginButton" type="submit" class="btn btn-success">Login</button>
                            <!--<a id="btn-fblogin" href="#" class="btn btn-primary">Login with Facebook</a>-->
                        </div>
                    </div>

                    <!--<div class="form-group">
                        <div class="col-md-12 control">
                            <div style="border-top: 1px solid#888; padding-top:15px; font-size:85%">
                                Don't have an account!
                                <a href="#" onClick="$('#loginbox').hide(); $('#signupbox').show()">
                                    Sign Up Here
                                </a>
                            </div>
                        </div>
                    </div>-->
                </form>

                <div style="display:none" id="alertLogin" class="alert alert-danger col-sm-12"></div>
            </div>
        </div>
    </div>

    <?php //require_once('include/footer.inc'); ?>
</div>
<!-- ./wrapper -->

<script>
    $(document).ready(function () {
        $('#loginForm').submit(function (e) {
            const alertLogin = $('#alertLogin');
            alertLogin.text('');
            alertLogin.hide();

            e.preventDefault();
            $.post(
                'api/api.php/login_admin',
                {
                    username: $('#usernameInput').val(),
                    password: $('#passwordInput').val()
                }
            ).done(function (data) {
                if (data.error_code === 0) {
                    if (data.login_success) {
                        window.location.href = "index.php";
                    } else {
                        alertLogin.text(data.error_message);
                        alertLogin.show();

                        /*BootstrapDialog.show({
                            title: 'Login',
                            message: data.error_message,
                            buttons: [{
                                label: 'ปิด',
                                action: function (self) {
                                    self.close();
                                }
                            }]
                        });*/
                    }
                } else {
                    alertLogin.text(data.error_message);
                    alertLogin.show();

                    /*BootstrapDialog.show({
                        title: 'Login',
                        message: data.error_message,
                        buttons: [{
                            label: 'ปิด',
                            action: function (self) {
                                self.close();
                            }
                        }]
                    });*/
                }
            }).fail(function () {
                alertLogin.text('เกิดข้อผิดพลาดในการเชื่อมต่อ Server');
                alertLogin.show();

                /*BootstrapDialog.show({
                    title: 'Login',
                    message: 'เกิดข้อผิดพลาดในการเชื่อมต่อ Server',
                    buttons: [{
                        label: 'ปิด',
                        action: function (self) {
                            self.close();
                        }
                    }]
                });*/
            });
        });
    });
</script>

<?php require_once('include/foot.inc'); ?>
</body>
</html>
