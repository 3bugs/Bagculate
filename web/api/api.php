<?php
session_start();
require_once 'global.php';

error_reporting(E_ERROR | E_PARSE);
header('Content-type: application/json; charset=utf-8');

header('Expires: Sun, 01 Jan 2014 00:00:00 GMT');
header('Cache-Control: no-store, no-cache, must-revalidate');
header('Cache-Control: post-check=0, pre-check=0', FALSE);
header('Pragma: no-cache');

$response = array();

$request = explode('/', trim($_SERVER['PATH_INFO'], '/'));
$action = strtolower(array_shift($request));
$id = array_shift($request);

require_once 'db_config.php';
$db = new mysqli(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);

if ($db->connect_errno) {
    $response[KEY_ERROR_CODE] = ERROR_CODE_ERROR;
    $response[KEY_ERROR_MESSAGE] = 'เกิดข้อผิดพลาดในการเชื่อมต่อฐานข้อมูล';
    $response[KEY_ERROR_MESSAGE_MORE] = $db->connect_error;
    echo json_encode($response);
    exit();
}
$db->set_charset("utf8");

switch ($action) {
    case 'login':
        doLogin();
        break;
    case 'logout':
        doLogout();
        break;
    case 'register':
        doRegister();
        break;
    case 'get_bag':
        doGetBag();
        break;
    case 'get_object':
        doGetObject();
        break;
    default:
        $response[KEY_ERROR_CODE] = ERROR_CODE_ERROR;
        $response[KEY_ERROR_MESSAGE] = 'No action specified or invalid action.';
        $response[KEY_ERROR_MESSAGE_MORE] = '';
        break;
}

$db->close();
echo json_encode($response);
exit();

function doLogin()
{
    global $db, $response;

    $username = $db->real_escape_string($_POST['username']);
    $password = $db->real_escape_string($_POST['password']);

    $selectUserSql = "SELECT * FROM `bagculate_user` WHERE `username` = '$username' AND `password` = '$password'";

    $selectUserResult = $db->query($selectUserSql);
    if ($selectUserResult) {
        if ($selectUserResult->num_rows > 0) {
            $response[KEY_ERROR_CODE] = ERROR_CODE_SUCCESS;
            $response[KEY_ERROR_MESSAGE] = 'เข้าสู่ระบบสำเร็จ';
            $response[KEY_ERROR_MESSAGE_MORE] = '';
            $response[KEY_LOGIN_SUCCESS] = TRUE;
            $user = fetchUser($selectUserResult);
            $response['user'] = $user;

            $_SESSION[KEY_SESSION_USER_ID] = $user['id'];
            $_SESSION[KEY_SESSION_USER_USERNAME] = $user['username'];
            $_SESSION[KEY_SESSION_USER_EMAIL] = $user['email'];
        } else {
            $response[KEY_ERROR_CODE] = ERROR_CODE_SUCCESS;
            $response[KEY_ERROR_MESSAGE] = 'ชื่อผู้ใช้ หรือรหัสผ่าน ไม่ถูกต้อง';
            $response[KEY_ERROR_MESSAGE_MORE] = '';
            $response[KEY_LOGIN_SUCCESS] = FALSE;
        }
        $selectUserResult->close();
    } else {
        $response[KEY_ERROR_CODE] = ERROR_CODE_ERROR;
        $response[KEY_ERROR_MESSAGE] = 'เกิดข้อผิดพลาดในการอ่านข้อมูลบัญชีผู้ใช้';
        $errMessage = $db->error;
        $response[KEY_ERROR_MESSAGE_MORE] = "$errMessage\nSQL: $selectUserSql";
    }
}

function doLogout()
{
    global $response;

    session_destroy();
    $response[KEY_ERROR_CODE] = ERROR_CODE_SUCCESS;
    $response[KEY_ERROR_MESSAGE] = 'ออกจากระบบสำเร็จ';
    $response[KEY_ERROR_MESSAGE_MORE] = '';
}

function fetchUser($selectUserResult)
{
    $row = $selectUserResult->fetch_assoc();

    $user = array();
    $user['id'] = (int)$row['id'];
    $user['username'] = $row['username'];
    $user['name'] = $row['name'];

    return $user;
}

function doRegister()
{
    global $db, $response;

    $username = $db->real_escape_string($_POST['username']);
    $password = $db->real_escape_string($_POST['password']);
    $name = $db->real_escape_string($_POST['name']);

    $selectExistingUserSQL = "SELECT * FROM `bagculate_user` WHERE `username` = '$username'";
    $selectExistingUserResult = $db->query($selectExistingUserSQL);
    if ($selectExistingUserResult->num_rows > 0) {
        $response[KEY_ERROR_CODE] = ERROR_CODE_ERROR;
        $response[KEY_ERROR_MESSAGE] = "สมัครสมาชิกไม่ได้ เนื่องจากมีชื่อผู้ใช้ '$username' อยู่ในระบบแล้ว";
        $response[KEY_ERROR_MESSAGE_MORE] = '';
        $selectExistingUserResult->close();
        return;
    }
    $selectExistingUserResult->close();

    $insertUserSql = "INSERT INTO `bagculate_user` (`username`, `password`, `name`) "
        . " VALUES ('$username', '$password', '$name')";
    $insertUserResult = $db->query($insertUserSql);
    if ($insertUserResult) {
        $insertId = $db->insert_id;
        $selectUserSql = "SELECT * FROM `bagculate_user` WHERE `id` = $insertId";

        $selectUserResult = $db->query($selectUserSql);
        if ($selectUserResult) {
            $response[KEY_ERROR_CODE] = ERROR_CODE_SUCCESS;
            $response[KEY_ERROR_MESSAGE] = 'สมัครสมาชิกสำเร็จ';
            $response[KEY_ERROR_MESSAGE_MORE] = '';
            $response['user'] = fetchUser($selectUserResult);
        }
    } else {
        $response[KEY_ERROR_CODE] = ERROR_CODE_ERROR;
        $response[KEY_ERROR_MESSAGE] = 'เกิดข้อผิดพลาดในการบันทึกข้อมูล';
        $errMessage = $db->error;
        $response[KEY_ERROR_MESSAGE_MORE] = "$errMessage\nSQL: $insertUserSql";
    }
}

function doGetBag()
{
    global $db, $response;

    $sql = "SELECT * FROM `bagculate_bag`";
    if ($result = $db->query($sql)) {
        $response[KEY_ERROR_CODE] = ERROR_CODE_SUCCESS;
        $response[KEY_ERROR_MESSAGE] = 'อ่านข้อมูลสำเร็จ';
        $response[KEY_ERROR_MESSAGE_MORE] = '';

        $bagList = array();
        while ($row = $result->fetch_assoc()) {
            $bag = array();
            $bag['id'] = (int)$row['id'];
            $bag['name'] = $row['name'];
            $bag['type'] = (int)$row['type'];
            $bag['weight'] = floatval($row['weight']);
            array_push($bagList, $bag);
        }
        $result->close();
        $response[KEY_DATA_LIST] = $bagList;
    } else {
        $response[KEY_ERROR_CODE] = ERROR_CODE_ERROR;
        $response[KEY_ERROR_MESSAGE] = 'เกิดข้อผิดพลาดในการอ่านข้อมูล';
        $errMessage = $db->error;
        $response[KEY_ERROR_MESSAGE_MORE] = "$errMessage\nSQL: $sql";
    }
}

function doGetObject()
{
    global $db, $response;

    $sql = "SELECT * FROM `bagculate_object` ORDER BY type, name";
    if ($result = $db->query($sql)) {
        $response[KEY_ERROR_CODE] = ERROR_CODE_SUCCESS;
        $response[KEY_ERROR_MESSAGE] = 'อ่านข้อมูลสำเร็จ';
        $response[KEY_ERROR_MESSAGE_MORE] = '';

        $objectList = array();
        while ($row = $result->fetch_assoc()) {
            $object = array();
            $object['id'] = (int)$row['id'];
            $object['name'] = $row['name'];
            $object['type'] = $row['type'];
            $object['weight'] = floatval($row['weight']);
            array_push($objectList, $object);
        }
        $result->close();
        $response[KEY_DATA_LIST] = $objectList;
    } else {
        $response[KEY_ERROR_CODE] = ERROR_CODE_ERROR;
        $response[KEY_ERROR_MESSAGE] = 'เกิดข้อผิดพลาดในการอ่านข้อมูล';
        $errMessage = $db->error;
        $response[KEY_ERROR_MESSAGE_MORE] = "$errMessage\nSQL: $sql";
    }
}
?>
