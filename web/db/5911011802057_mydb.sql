-- phpMyAdmin SQL Dump
-- version 4.1.7
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Aug 03, 2019 at 05:56 PM
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
-- Table structure for table `bagculate_bag`
--

CREATE TABLE IF NOT EXISTS `bagculate_bag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `weight` float DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=7 ;

--
-- Dumping data for table `bagculate_bag`
--

INSERT INTO `bagculate_bag` (`id`, `name`, `type`, `weight`) VALUES
(1, 'Classy 20 นิ้ว 40.5 x 24.5 x 56 cm', 1, 3.24),
(2, 'Classy 24 นิ้ว 47 x 27 x 65 cm', 1, 3.97),
(3, 'Classy 26 นิ้ว 49 x 27.5 x 70 cm', 1, 4.36),
(4, 'A602 Sport 26 นิ้ว 39 x 33 x 68 cm', 0, 4.95),
(5, 'A602 Sport 29 นิ้ว 40 x 36 x 74 cm', 0, 5.7),
(6, 'A602 Sport 32 นิ้ว 41 x 36 x 78 cm', 0, 6.2);

-- --------------------------------------------------------

--
-- Table structure for table `bagculate_history`
--

CREATE TABLE IF NOT EXISTS `bagculate_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `bag_id` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=7 ;

--
-- Dumping data for table `bagculate_history`
--

INSERT INTO `bagculate_history` (`id`, `user_id`, `bag_id`, `created_at`) VALUES
(1, 1, 4, '2019-08-03 09:03:43'),
(2, 1, 4, '2019-08-03 09:03:56'),
(3, 1, 4, '2019-08-03 09:04:53'),
(4, 1, 4, '2019-08-03 10:09:24'),
(5, 1, 5, '2019-08-03 10:27:34'),
(6, 1, 4, '2019-08-03 10:43:07');

-- --------------------------------------------------------

--
-- Table structure for table `bagculate_history_details`
--

CREATE TABLE IF NOT EXISTS `bagculate_history_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `history_id` int(11) NOT NULL,
  `object_id` int(11) NOT NULL,
  `count` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=13 ;

--
-- Dumping data for table `bagculate_history_details`
--

INSERT INTO `bagculate_history_details` (`id`, `history_id`, `object_id`, `count`) VALUES
(1, 1, 13, 3),
(2, 1, 15, 2),
(3, 1, 17, 1),
(4, 2, 13, 3),
(5, 2, 15, 2),
(6, 2, 17, 1),
(7, 3, 13, 3),
(8, 3, 15, 2),
(9, 3, 17, 1),
(10, 4, 2, 2),
(11, 5, 14, 1),
(12, 6, 12, 1);

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

-- --------------------------------------------------------

--
-- Table structure for table `bagculate_user`
--

CREATE TABLE IF NOT EXISTS `bagculate_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=7 ;

--
-- Dumping data for table `bagculate_user`
--

INSERT INTO `bagculate_user` (`id`, `username`, `password`, `name`) VALUES
(1, 'admin', '1234', 'ผู้ดูแลระบบ'),
(2, 'test', 'test', 'test'),
(3, 'test2', 'test2', 'test2'),
(4, 'test3', 'test3', 'test3'),
(5, 'banks', '250540', 'bank'),
(6, 'bankss', 'bankss', 'bankss');

-- --------------------------------------------------------

--
-- Table structure for table `category`
--

CREATE TABLE IF NOT EXISTS `category` (
  `catid` int(10) NOT NULL AUTO_INCREMENT,
  `catname` varchar(60) NOT NULL,
  `catactive` tinyint(4) NOT NULL,
  `catimage` varchar(60) NOT NULL,
  PRIMARY KEY (`catid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=8 ;

--
-- Dumping data for table `category`
--

INSERT INTO `category` (`catid`, `catname`, `catactive`, `catimage`) VALUES
(1, 'mouse', 1, '1.png'),
(2, 'computer', 1, '2.png'),
(3, 'sofar', 1, '3.png'),
(4, 'locker', 1, '4.png'),
(5, 'office', 1, '5.png'),
(6, 'computer', 1, '6.png'),
(7, 'sofar', 1, '7.png');

-- --------------------------------------------------------

--
-- Table structure for table `orderdetail`
--

CREATE TABLE IF NOT EXISTS `orderdetail` (
  `ode_id` int(11) NOT NULL AUTO_INCREMENT,
  `orderid` int(11) NOT NULL,
  `pid` int(11) NOT NULL,
  `ordernum` int(11) NOT NULL,
  `orderprice` int(11) NOT NULL,
  PRIMARY KEY (`ode_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=7 ;

--
-- Dumping data for table `orderdetail`
--

INSERT INTO `orderdetail` (`ode_id`, `orderid`, `pid`, `ordernum`, `orderprice`) VALUES
(1, 19, 7, 2, 14000),
(2, 19, 8, 1, 123),
(3, 22, 7, 1, 7000),
(4, 23, 1, 1, 7990),
(5, 24, 1, 2, 7800),
(6, 25, 1, 1, 3900);

-- --------------------------------------------------------

--
-- Table structure for table `orderss`
--

CREATE TABLE IF NOT EXISTS `orderss` (
  `orderid` int(11) NOT NULL AUTO_INCREMENT,
  `ordername` varchar(60) CHARACTER SET utf8 NOT NULL,
  `orderadd` varchar(60) CHARACTER SET utf8 NOT NULL,
  `orderemail` varchar(60) CHARACTER SET utf8 NOT NULL,
  `orderphone` varchar(10) CHARACTER SET utf8 NOT NULL,
  `ordertotal` double NOT NULL,
  `orderdate` date NOT NULL,
  PRIMARY KEY (`orderid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=26 ;

--
-- Dumping data for table `orderss`
--

INSERT INTO `orderss` (`orderid`, `ordername`, `orderadd`, `orderemail`, `orderphone`, `ordertotal`, `orderdate`) VALUES
(4, 'non', 'กรุงเทพฯ', 'nivea-250540@hotmail.com', '0957980461', 30000, '2019-04-02'),
(5, 'hh', 'jj green', 'nivea-250540@hotmail.com', '0871033295', 7000, '2019-04-02'),
(6, 'dd', 'dd', 'nivea-250540@hotmail.com', '0895342271', 7012, '2019-04-02'),
(7, 'fd', 'df', 'jjssdd@gmail.com', '0247854612', 3000, '2019-04-02'),
(8, '', '', '', '', 0, '2019-04-02'),
(9, '', '', '', '', 0, '2019-04-02'),
(10, '', '', '', '', 0, '2019-04-02'),
(11, '', '', '', '', 0, '2019-04-02'),
(12, 'ffd', '45/887', 'kayeetao77@gmail.com', '0245645148', 3012, '2019-04-02'),
(13, 'ddcdc', 'wdcc', 'efhcc@gmail.com', '0212457896', 7000, '2019-04-02'),
(14, '', '', '', '', 0, '2019-04-02'),
(15, '', '', '', '', 0, '2019-04-02'),
(16, 'srd', 'see', 'hhgg@gmail.com', '0889456214', 7123, '2019-04-02'),
(17, '', '', '', '', 0, '2019-04-02'),
(18, '', '', '', '', 0, '2019-04-02'),
(19, 'efrg', 'efd', 'dad@gmail.com', '', 14123, '2019-04-02'),
(20, '', '', '', '', 0, '2019-04-09'),
(21, '', '', '', '', 0, '2019-04-09'),
(22, 'dfd', '548', 'sdf@gmail.com', '095484456', 7000, '2019-05-05'),
(23, 'fdf', '5455', 'gh@gmail.com', '0957542645', 7990, '2019-05-05'),
(24, 'bank', '20/5', 'kayeetao@gmail.com', '0957980461', 7800, '2019-05-07'),
(25, 'banks', '20/5', 'banks@gmail.com', '0957980461', 3900, '2019-05-07');

-- --------------------------------------------------------

--
-- Table structure for table `product`
--

CREATE TABLE IF NOT EXISTS `product` (
  `pid` int(10) NOT NULL AUTO_INCREMENT,
  `pname` varchar(100) NOT NULL,
  `pprice` double NOT NULL,
  `pdescription` text NOT NULL,
  `pimage` varchar(60) NOT NULL,
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=9 ;

--
-- Dumping data for table `product`
--

INSERT INTO `product` (`pid`, `pname`, `pprice`, `pdescription`, `pimage`) VALUES
(1, 'adidas superstar white black', 3900, 'รองเท้าที่ครองตำแหน่งไอคอนแห่งท้องถนนด้วยดีไซน์หัวรองเท้ารูปเปลือกหอยที่ไม่มีใครเหมือน รองเท้า adidas Superstar รุ่นนี้มาพร้อมความโดดเด่นจากแถบ 3-Stripes ขอบหยักที่มีขนาดใหญ่กว่าเดิมสองเท่า โลโก้ที่ได้แรงบันดาลใจจากอดีตช่วยเพิ่มสไตล์เรโทรแบบดั้งเดิมให้กับรองเท้า อัปเปอร์ทำจากหนังฟูลเกรน แต่งเทกเจอร์อ่อน ๆ', 'adidas superstar white black.jpg'),
(2, 'adidas yeezy boost 350', 3900, 'วันนี้เราจึงหยิบหนึ่งในรองเท้าที่ Kanye West ออกแบบนั่นคือ Yeezy Boost 350 มารีวิวให้ทุกท่านได้ชมรองเท้าคู่นี้วางจำหน่ายในประเทศไทยเมื่อวันที่  28 ธันว่าคม 2015 ที่ผ่านมา ที่ร้าน Adidas Original สยามเซนเตอร์  โอกาสนี้จึงขอรีวิว และบอกเล่าความรู้สึกหลังจากได้สัมผัสรองเท้าคู่นี้กัน', 'adidas yeezy boost 350.jpg'),
(3, 'Adidas-NMD-R1-Blizzard', 3900, 'รองเท้าจาก Adidas ในรุ่น NMD ที่มีคำย่อมาจาก “NOMAD” หรือผู้ที่ชอบการเดินทาง รองเท้าที่ถูกออกแบบมาจากต้นฉบับรองเท้า 3 รุ่นในยุคกลางของปี 80’s อย่างรุ่น Micropacer, Rising Star และ Boston Super แต่นำกลับมา redesign ใหม่และใส่เทคโนโลยีของ Primeknit กับ Boost เข้าไป\r\nรองเท้ารุ่นนี้ถูกจัดเป็นรองเท้าที่อยู่ในหมวดที่เหมาะสำหรับคนที่ชอบรองเท้า (sneakerheads) รวมไปถึงนักกีฬาหรือผู้ที่ชื่นชอบการออกกำลังกาย (athleisure lover) ด้วย เพราะมันถูกออกแบบมาให้ตอบโจทย์ของการสวมใส่ได้อย่างดีเยี่ยม', 'Adidas-NMD-R1-Blizzard.jpg'),
(4, 'Adidas-NMD-XR1-Triple-Grey', 3900, 'สามารถใส่ได้ทุกเพศทุกวัย Unisex​- ใส่สบาย เนื้อผ้ามีความยืดหยุ่น', 'Adidas-NMD-XR1-Triple-Grey.jpg'),
(5, 'nike air jordan x offwihte', 3900, 'ไม่มีอะไรมาบดบังความโดดเด่นของรองเท้าคู่นี้ได้ แม้กฎของลีกก็ตาม ช่วงเวลาแห่งความยิ่งใหญ่บนพื้นสนามนำไปสู่ยุคใหม่ของสไตล์นอกสนาม เพราะรองเท้าคู่นี้ยกระดับความมีสไตล์ไม่ว่าจะสวมใส่กับอะไร', 'nike air jordan x offwihte.png'),
(6, 'nike air max 90', 3900, 'รองเท้าที่ออกแบบดีไซน์ให้มีความคลาสสิคเหมือนกับรองเท้ารุ่นยุค 90 นั้นถือว่ายังคงได้รับความนิยมอยู่จนถึงทุกวันนี้โดยเฉพาะอย่างยิ่งรองเท้าผู้ชายที่ออกแบบดีไซน์มีเอกลักษณ์ในเรื่องของความคลาสสิคด้วยและเราก็สามารถที่จะขายได้ง่ายอย่างแน่นอนซึ่งรองเท้ารุ่นหนึ่งที่ถือว่าตอบโจทย์เกี่ยวกับเรื่องนี้ด้วยนั่นก็คือ Nike Air Max 90 และยังมีความโดดเด่นอื่นๆที่ถือว่าเป็นรองเท้ารุ่นหนึ่งที่ขายดีเป็นอย่างมาก', 'nike air max 90.png'),
(7, 'nike air max advantage running', 3900, 'รองเท้า Nike Air Max Advantage นิยามใหม่ของความนุ่ม เบา สบาย ถือว่าเป็นรองเท้าวิ่งรุ่นขายดี ของ Nike ด้วยจุดเด่นที่ไม่ใช่เป็นเพียงรองเท้าวิ่งเท่านั้น', 'nike air max advantage running.jpg'),
(8, 'nike air max97white', 3900, 'สาวก AIR MAX 97 ไม่ควรพลาดคู่สุดสวยคู่นี้เลยจร้า บอกเลยว่า มันมาแบบ FRESH FRESH CLEAN CLEAN ไปเลย ขาวสวย มาก', 'nike air max97white.jpg');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
