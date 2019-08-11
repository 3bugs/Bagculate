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
-- Table structure for table `bagculate_history_details`
--

CREATE TABLE IF NOT EXISTS `bagculate_history_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `history_id` int(11) NOT NULL,
  `object_id` int(11) NOT NULL,
  `count` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=25 ;

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
(12, 6, 12, 1),
(13, 7, 13, 1),
(14, 7, 1, 1),
(15, 7, 14, 1),
(16, 7, 4, 1),
(17, 7, 3, 1),
(18, 7, 16, 1),
(19, 7, 17, 1),
(20, 8, 13, 2),
(21, 8, 5, 1),
(22, 9, 17, 1),
(23, 10, 13, 3),
(24, 10, 5, 2);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
