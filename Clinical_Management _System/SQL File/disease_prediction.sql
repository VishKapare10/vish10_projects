-- phpMyAdmin SQL Dump
-- version 4.2.7.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Feb 20, 2017 at 09:50 AM
-- Server version: 5.5.39
-- PHP Version: 5.4.31

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `disease_prediction`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE IF NOT EXISTS `admin` (
`id` int(30) NOT NULL,
  `name` varchar(30) NOT NULL,
  `password` varchar(30) NOT NULL,
  `hospital_name` varchar(30) NOT NULL,
  `address` text NOT NULL,
  `speciality` varchar(40) NOT NULL,
  `others` text NOT NULL,
  `mobile_no` int(30) NOT NULL,
  `subarea` varchar(30) NOT NULL,
  `area` varchar(30) NOT NULL,
  `rating` varchar(30) NOT NULL DEFAULT '4.5'
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=6 ;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`id`, `name`, `password`, `hospital_name`, `address`, `speciality`, `others`, `mobile_no`, `subarea`, `area`, `rating`) VALUES
(1, 'Amrut', '1234', 'Amrut Hospital', 'Near Pimpri', 'Brain', 'Flu,Cold,', 1234567890, 'PIMPRI', 'Pune', '4.21875'),
(2, 'Nilesh', '1234', 'Nilesh Hospital', 'Chinchawad', 'Dentist', ' Flu,', 1234567809, 'AKURDI', 'Pune', '4.25'),
(3, 'Shruti', '1234', 'Shruti Hospital', 'Dange Chowk', 'Hair', 'Cold,Cough,Stomach Pain,', 1234569870, 'Pune', 'CHINCHWAD', '5'),
(4, 'Pooja', '1234', 'Pooja Hospital', 'near shivajinagar', 'Hair', 'Flu,Cold,Cough,Head ache,Back Pain,', 2147483647, 'SHIVAJINAGAR', 'Pune', '4.5'),
(5, 'ankita', '1234', 'Ankita Hospital', 'chichwad', 'Heart', 'Flu,Cough,Stomach Pain,Head ache,', 2147483647, 'CHINCHWAD', 'Pune', '4.5');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
`id` int(30) NOT NULL,
  `name` varchar(40) NOT NULL,
  `password` varchar(30) NOT NULL,
  `mobile_no` int(30) NOT NULL,
  `email_id` varchar(30) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `name`, `password`, `mobile_no`, `email_id`) VALUES
(1, 'amrut', '1234', 1234567890, 'a@gmail.com'),
(2, 'ankita', '1234', 1234657890, 'amrutab99@gmail.com');

-- --------------------------------------------------------

--
-- Table structure for table `user_request`
--

CREATE TABLE IF NOT EXISTS `user_request` (
`id` int(30) NOT NULL,
  `username` varchar(30) NOT NULL,
  `user_mobile` varchar(30) NOT NULL,
  `admin_mobile` varchar(30) NOT NULL,
  `request_status` varchar(30) NOT NULL DEFAULT 'Pending',
  `date` date NOT NULL,
  `time` time NOT NULL,
  `symptoms` text NOT NULL,
  `rate` varchar(30) NOT NULL DEFAULT 'not rated'
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=11 ;

--
-- Dumping data for table `user_request`
--

INSERT INTO `user_request` (`id`, `username`, `user_mobile`, `admin_mobile`, `request_status`, `date`, `time`, `symptoms`, `rate`) VALUES
(5, 'amrut', '1234567890', '1234567890', 'Accepted', '2017-02-14', '04:40:00', 'Flu,Cough', 'rated'),
(6, 'amrut', '1234567890', '1234567890', 'Accepted', '2017-02-14', '12:11:00', 'Flu,Cold', 'rated'),
(7, 'amrut', '1234567890', '1234567809', 'Accepted', '2017-02-16', '17:52:00', 'Flu,Stomach Pain,Head ache,Back Pain,', 'rated'),
(8, 'amrut', '1234567890', '2147483647', 'Rejected', '2017-02-15', '12:40:00', 'Flu,Cold,Anxiety,sleeping sickness,', 'not rated'),
(9, 'amrut', '1234567890', '1234567809', 'Rejected', '2017-02-17', '09:51:00', 'Cold,sleeping sickness,', 'not rated'),
(10, 'ankita', '1234657890', '1234567890', 'Accepted', '2017-02-24', '01:45:00', 'Flu,Stomach Pain,', 'rated');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user_request`
--
ALTER TABLE `user_request`
 ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admin`
--
ALTER TABLE `admin`
MODIFY `id` int(30) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
MODIFY `id` int(30) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `user_request`
--
ALTER TABLE `user_request`
MODIFY `id` int(30) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=11;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
