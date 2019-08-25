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
    case 'login_admin':
        doLoginAdmin();
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
    case 'get_history':
        doGetHistory();
        break;
    case 'add_history':
        doAddHistory();
        break;
    case 'add_bag':
        doAddBag();
        break;
    case 'update_bag':
        doUpdateBag();
        break;
    case 'delete_bag':
        doDeleteBag();
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
            $response[KEY_ERROR_CODE] = ERROR_CODE_ERROR;
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

function doLoginAdmin()
{
    global $db, $response;

    $username = $db->real_escape_string($_POST['username']);
    $password = $db->real_escape_string($_POST['password']);

    $selectUserSql = "SELECT * FROM `bagculate_user` WHERE `username` = '$username' AND `password` = '$password' AND `is_admin` = 1";

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
            $response[KEY_ERROR_CODE] = ERROR_CODE_ERROR;
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

function doGetHistory()
{
    global $db, $response;

    $userId = $db->real_escape_string($_POST['user_id']);

    $sql = "SELECT h.id, h.created_at, b.name, b.type, b.weight FROM bagculate_history h INNER JOIN bagculate_bag b ON h.bag_id = b.id WHERE user_id = $userId ORDER BY created_at DESC";
    if ($result = $db->query($sql)) {
        $historyList = array();
        while ($row = $result->fetch_assoc()) {
            $history = array();
            $historyId = (int)$row['id'];
            $history['id'] = $historyId;
            $history['created_at'] = $row['created_at'];
            $history['bag'] = array(
                'name' => $row['name'],
                'type' => (int)$row['type'],
                'weight' => floatval($row['weight'])
            );
            $history['object_list'] = array();

            $sql = "SELECT o.name, o.type, o.weight, hd.count FROM bagculate_history_details hd INNER JOIN bagculate_object o ON hd.object_id = o.id WHERE history_id = $historyId";
            if ($objectResult = $db->query($sql)) {
                $response[KEY_ERROR_CODE] = ERROR_CODE_SUCCESS;
                $response[KEY_ERROR_MESSAGE] = 'อ่านข้อมูลสำเร็จ';
                $response[KEY_ERROR_MESSAGE_MORE] = '';

                while ($objectRow = $objectResult->fetch_assoc()) {
                    $object = array();
                    $object['name'] = $objectRow['name'];
                    $object['type'] = $objectRow['type'];
                    $object['weight'] = floatval($objectRow['weight']);
                    $object['count'] = (int)$objectRow['count'];

                    array_push($history['object_list'], $object);
                }
                $objectResult->close();
            } else {
                $response[KEY_ERROR_CODE] = ERROR_CODE_ERROR;
                $response[KEY_ERROR_MESSAGE] = 'เกิดข้อผิดพลาดในการอ่านข้อมูล (2)';
                $errMessage = $db->error;
                $response[KEY_ERROR_MESSAGE_MORE] = "$errMessage\nSQL: $sql";
                return;
            }

            array_push($historyList, $history);
        }
        $result->close();
        $response[KEY_DATA_LIST] = $historyList;
    } else {
        $response[KEY_ERROR_CODE] = ERROR_CODE_ERROR;
        $response[KEY_ERROR_MESSAGE] = 'เกิดข้อผิดพลาดในการอ่านข้อมูล (1)';
        $errMessage = $db->error;
        $response[KEY_ERROR_MESSAGE_MORE] = "$errMessage\nSQL: $sql";
    }
}

function doAddHistory()
{
    global $db, $response;

    $userId = $db->real_escape_string($_POST['user_id']);
    $bagId = $db->real_escape_string($_POST['bag_id']);
    $objectList = $db->real_escape_string($_POST['object_list']);

    $sql = "INSERT INTO bagculate_history (user_id, bag_id) VALUES ($userId, $bagId)";
    if ($result = $db->query($sql)) {
        $insertId = $db->insert_id;

        $objectList = explode(',', $objectList);
        $valueList = "";
        foreach ($objectList as $object) {
            $objectPart = explode('-', $object);
            $objectId = $objectPart[0];
            $count = $objectPart[1];

            $valueList .= "($insertId, $objectId, $count),";
        }
        $valueList = substr($valueList, 0, -1);

        $insertObjectListSql = "INSERT INTO bagculate_history_details (history_id, object_id, count) 
                                VALUES $valueList";
        if ($db->query($insertObjectListSql)) {
            $response[KEY_ERROR_CODE] = ERROR_CODE_SUCCESS;
            $response[KEY_ERROR_MESSAGE] = 'บันทึกข้อมูลสำเร็จ';
            $response[KEY_ERROR_MESSAGE_MORE] = '';
        } else {
            $response[KEY_ERROR_CODE] = ERROR_CODE_ERROR;
            $response[KEY_ERROR_MESSAGE] = 'เกิดข้อผิดพลาดในการบันทึกข้อมูล (2)';
            $errMessage = $db->error;
            $response[KEY_ERROR_MESSAGE_MORE] = "$errMessage\nSQL: $insertObjectListSql";
        }
    } else {
        $response[KEY_ERROR_CODE] = ERROR_CODE_ERROR;
        $response[KEY_ERROR_MESSAGE] = 'เกิดข้อผิดพลาดในการบันทึกข้อมูล (1)';
        $errMessage = $db->error;
        $response[KEY_ERROR_MESSAGE_MORE] = "$errMessage\nSQL: $sql";
    }
}

function doAddBag()
{
    global $db, $response;

    $bagName = $db->real_escape_string($_POST['bagName']);
    $bagWeight = $db->real_escape_string($_POST['bagWeight']);
    $bagType = $db->real_escape_string($_POST['bagType']);

    $sql = "INSERT INTO `bagculate_bag` (name, weight, type) VALUES ('$bagName', $bagWeight, $bagType)";
    if ($db->query($sql)) {
        $response[KEY_ERROR_CODE] = ERROR_CODE_SUCCESS;
        $response[KEY_ERROR_MESSAGE] = 'เพิ่มข้อมูลสำเร็จ';
        $response[KEY_ERROR_MESSAGE_MORE] = '';
    } else {
        $response[KEY_ERROR_CODE] = ERROR_CODE_ERROR;
        $response[KEY_ERROR_MESSAGE] = 'เกิดข้อผิดพลาดในการบันทึกข้อมูล';
        $errMessage = $db->error;
        $response[KEY_ERROR_MESSAGE_MORE] = "$errMessage\nSQL: $sql";
    }
}

function doUpdateBag()
{
    global $db, $response;

    $bagId = $db->real_escape_string($_POST['bagId']);
    $bagName = $db->real_escape_string($_POST['bagName']);
    $bagWeight = $db->real_escape_string($_POST['bagWeight']);
    $bagType = $db->real_escape_string($_POST['bagType']);

    $sql = "UPDATE `bagculate_bag` SET name = '$bagName', weight = $bagWeight, type = $bagType WHERE id = $bagId";
    if ($db->query($sql)) {
        $response[KEY_ERROR_CODE] = ERROR_CODE_SUCCESS;
        $response[KEY_ERROR_MESSAGE] = 'แก้ไขข้อมูลสำเร็จ';
        $response[KEY_ERROR_MESSAGE_MORE] = '';
    } else {
        $response[KEY_ERROR_CODE] = ERROR_CODE_ERROR;
        $response[KEY_ERROR_MESSAGE] = 'เกิดข้อผิดพลาดในการบันทึกข้อมูล';
        $errMessage = $db->error;
        $response[KEY_ERROR_MESSAGE_MORE] = "$errMessage\nSQL: $sql";
    }
}

function doDeleteBag()
{
    global $db, $response;

    $bagId = $db->real_escape_string($_POST['bagId']);

    $sql = "DELETE FROM `bagculate_bag` WHERE id = $bagId";
    if ($db->query($sql)) {
        $response[KEY_ERROR_CODE] = ERROR_CODE_SUCCESS;
        $response[KEY_ERROR_MESSAGE] = 'ลบข้อมูลสำเร็จ';
        $response[KEY_ERROR_MESSAGE_MORE] = '';
    } else {
        $response[KEY_ERROR_CODE] = ERROR_CODE_ERROR;
        $response[KEY_ERROR_MESSAGE] = 'เกิดข้อผิดพลาดในการลบข้อมูล';
        $errMessage = $db->error;
        $response[KEY_ERROR_MESSAGE_MORE] = "$errMessage\nSQL: $sql";
    }
}

?>
