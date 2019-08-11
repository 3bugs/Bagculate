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
-- Table structure for table `bagculate_history`
--

CREATE TABLE IF NOT EXISTS `bagculate_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `bag_id` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=11 ;

--
-- Dumping data for table `bagculate_history`
--

INSERT INTO `bagculate_history` (`id`, `user_id`, `bag_id`, `created_at`) VALUES
(1, 1, 4, '2019-08-03 09:03:43'),
(2, 1, 4, '2019-08-03 09:03:56'),
(3, 1, 4, '2019-08-03 09:04:53'),
(4, 1, 4, '2019-08-03 10:09:24'),
(5, 1, 5, '2019-08-03 10:27:34'),
(6, 1, 4, '2019-08-03 10:43:07'),
(7, 6, 1, '2019-08-06 17:40:24'),
(8, 1, 4, '2019-08-11 05:07:32'),
(9, 1, 1, '2019-08-11 05:44:57'),
(10, 1, 5, '2019-08-11 06:30:57');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
