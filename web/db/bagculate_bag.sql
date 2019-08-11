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
-- Table structure for table `bagculate_bag`
--

CREATE TABLE IF NOT EXISTS `bagculate_bag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) DEFAULT NULL,
  `type` int(11) DEFAULT NULL COMMENT '0 = สะพาย, 1 = ล้อลาก',
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

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
