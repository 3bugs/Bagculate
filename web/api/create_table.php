<?php

error_reporting(E_ERROR | E_PARSE);
header('Content-type: text/html; charset=utf-8');

header('Expires: Sun, 01 Jan 2014 00:00:00 GMT');
header('Cache-Control: no-store, no-cache, must-revalidate');
header('Cache-Control: post-check=0, pre-check=0', FALSE);
header('Pragma: no-cache');

require_once 'db_config.php';
$db = new mysqli(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);

if ($db->connect_errno) {
    echo 'เกิดข้อผิดพลาดในการเชื่อมต่อฐานข้อมูล';
    exit();
}
$db->set_charset("utf8");

$sql = "CREATE TABLE `object` (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(200), type INT, weight FLOAT)";
$result = $db->query($sql);
if ($result = $db->query($sql)) {
    echo 'สร้างเทเบิลสำเร็จ';
    $sql = "INSERT INTO `object` (name, type, weight) VALUES "
        . " ('เสื้อเชิ้ต', 0, 0.2), "
        . " ('เสื้อยืดคอกลม', 0, 0.2), "
        . " ('กางเกงยีนส์', 1, 0.6), "
        . " ('กางเกงสแลค', 1, 0.35) ";
    if ($result = $db->query($sql)) {
        echo 'เพิ่มข้อมูลสำเร็จ';
    } else {
        echo 'เกิดข้อผิดพลาดในการเพิ่มข้อมูล: ' . $db->error;
    }
} else {
    echo 'เกิดข้อผิดพลาดในการสร้างเทเบิล object: ' . $db->error;
}

$db->close();
echo json_encode($response);
exit();
