-- phpMyAdmin SQL Dump
-- version 4.1.7
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Aug 11, 2019 at 04:36 PM
-- Server version: 5.5.37
-- PHP Version: 5.4.33

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `5911011802057_mydb`
--

-- --------------------------------------------------------

--
-- Table structure for table `bagculate_object`
--

CREATE TABLE IF NOT EXISTS `bagculate_object` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) CHARACTER SET tis620 NOT NULL,
  `type` enum('shirt','pants','shoes','thing','etc') NOT NULL,
  `weight` float NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=20 ;

--
-- Dumping data for table `bagculate_object`
--

INSERT INTO `bagculate_object` (`id`, `name`, `type`, `weight`) VALUES
(1, 'เสื้อเชิ้ต', 'shirt', 200),
(2, 'เสื้อยืดคอกลม', 'shirt', 200),
(3, 'กางเกงยีนส์', 'pants', 600),
(4, 'กางเกงสแลค', 'pants', 350),
(5, 'กางเกงขาสั้น', 'pants', 200),
(6, 'ผ้าเช็ดหน้า', 'thing', 20),
(7, 'ผ้าขนหนูเช็ดตัว', 'thing', 500),
(8, 'ผ้าขนหนูเช็ดหน้า', 'thing', 200),
(9, 'โรลออน', 'thing', 78),
(10, 'ลิปสติกแท่งเล็ก', 'thing', 4),
(11, 'ยาสีฟันแบบหลอด', 'thing', 40),
(12, 'ยาสีฟันแบบตลับ', 'thing', 200),
(13, 'เสื้อแขนยาว', 'shirt', 260),
(14, 'เสื้อสูท', 'shirt', 540),
(15, 'กางเกงวอร์ม', 'pants', 370),
(16, 'รองเท้าผ้าใบ', 'shoes', 760),
(17, 'รองเท้าคัทชู', 'shoes', 580),
(18, 'รองเท้าแตะ', 'shoes', 260),
(19, 'กล้องถ่ายรูป', 'etc', 390);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
