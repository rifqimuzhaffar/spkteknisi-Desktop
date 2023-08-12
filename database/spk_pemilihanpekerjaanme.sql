-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 04, 2023 at 11:35 AM
-- Server version: 10.4.24-MariaDB
-- PHP Version: 7.4.29

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `spk_pemilihanpekerjaanme`
--

-- --------------------------------------------------------

--
-- Table structure for table `tabelalternatif1`
--

CREATE TABLE `tabelalternatif1` (
  `id` int(11) NOT NULL,
  `tanggalmasuk` date NOT NULL,
  `namapekerjaan` varchar(50) NOT NULL,
  `c1` varchar(50) NOT NULL,
  `c2` varchar(50) NOT NULL,
  `c3` varchar(50) NOT NULL,
  `c4` varchar(50) NOT NULL,
  `c5` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tabelalternatif1`
--

INSERT INTO `tabelalternatif1` (`id`, `tanggalmasuk`, `namapekerjaan`, `c1`, `c2`, `c3`, `c4`, `c5`) VALUES
(1, '2023-08-11', 'lampu tl18 prodi bk', 'Listrik & Penerangan', 'R.DOSEN', 'Aman', 'Cukup', '15000'),
(2, '2023-08-23', 'CCTV pak dekan', 'Sistem Keamanan', 'R.PIMPINAN', 'Aman', 'Sangat Penting', '150000'),
(3, '2023-08-11', 'AC wadek2', 'Sistem HVAC', 'R.PIMPINAN', 'Berbahaya', 'Penting', '1000000'),
(4, '2023-08-20', 'wc kasubag', 'Sistem Plumbing', 'R.KARYAWAN', 'Sangat Aman', 'Penting', '500000'),
(5, '2023-08-17', 'handle pintu lab fisika', 'Struktural Gedung', 'LABORATORIUM', 'Sangat Aman', 'Cukup', '10000');

-- --------------------------------------------------------

--
-- Table structure for table `tabelalternatif2`
--

CREATE TABLE `tabelalternatif2` (
  `id` int(11) NOT NULL,
  `tanggalmasuk` date NOT NULL,
  `namapekerjaan` varchar(50) NOT NULL,
  `c1` double NOT NULL,
  `c2` double NOT NULL,
  `c3` double NOT NULL,
  `c4` double NOT NULL,
  `c5` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tabelalternatif2`
--

INSERT INTO `tabelalternatif2` (`id`, `tanggalmasuk`, `namapekerjaan`, `c1`, `c2`, `c3`, `c4`, `c5`) VALUES
(1, '2023-08-11', 'lampu tl18 prodi bk', 5, 3, 4, 3, 5),
(2, '2023-08-23', 'CCTV pak dekan', 3, 5, 4, 5, 5),
(3, '2023-08-11', 'AC wadek2', 1, 5, 2, 4, 1),
(4, '2023-08-20', 'wc kasubag', 4, 4, 5, 4, 3),
(5, '2023-08-17', 'handle pintu lab fisika', 2, 2, 5, 3, 5);

-- --------------------------------------------------------

--
-- Table structure for table `tabelkriteria`
--

CREATE TABLE `tabelkriteria` (
  `id` varchar(50) NOT NULL,
  `kriteria` varchar(50) NOT NULL,
  `keterangan` varchar(50) NOT NULL,
  `nilai` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tabelkriteria`
--

INSERT INTO `tabelkriteria` (`id`, `kriteria`, `keterangan`, `nilai`) VALUES
('01', 'fasilitas', 'Listrik & Penerangan', '5'),
('02', 'fasilitas', 'Sistem Plumbing', '4'),
('03', 'fasilitas', 'Sistem Keamanan', '3'),
('04', 'fasilitas', 'Struktural Gedung', '2'),
('05', 'fasilitas', 'Sistem HVAC', '1'),
('06', 'ruangan', 'R.PIMPINAN', '5'),
('07', 'ruangan', 'R.KARYAWAN', '4'),
('08', 'ruangan', 'R.DOSEN', '3'),
('09', 'ruangan', 'LABORATORIUM', '2'),
('10', 'ruangan', 'R.KELAS', '1');

-- --------------------------------------------------------

--
-- Table structure for table `tb_saw`
--

CREATE TABLE `tb_saw` (
  `id` int(11) NOT NULL,
  `tanggalmasuk` varchar(50) NOT NULL,
  `namapekerjaan` varchar(50) NOT NULL,
  `c1` double NOT NULL,
  `c2` double NOT NULL,
  `c3` double NOT NULL,
  `c4` double NOT NULL,
  `c5` double NOT NULL,
  `sum` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tb_saw`
--

INSERT INTO `tb_saw` (`id`, `tanggalmasuk`, `namapekerjaan`, `c1`, `c2`, `c3`, `c4`, `c5`, `sum`) VALUES
(1, '2023-08-11', 'lampu tl18 prodi bk', 0.15, 0.09, 0.24, 0.18, 0.1, 0.76),
(2, '2023-08-23', 'CCTV pak dekan', 0.09, 0.15, 0.24, 0.3, 0.1, 0.88),
(3, '2023-08-11', 'AC wadek2', 0.03, 0.15, 0.12, 0.24, 0.02, 0.56),
(4, '2023-08-20', 'wc kasubag', 0.12, 0.12, 0.3, 0.24, 0.06, 0.84),
(5, '2023-08-17', 'handle pintu lab fisika', 0.06, 0.06, 0.3, 0.18, 0.1, 0.7);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`username`, `password`) VALUES
('admin', 'admin123');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tabelalternatif1`
--
ALTER TABLE `tabelalternatif1`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tabelalternatif2`
--
ALTER TABLE `tabelalternatif2`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tb_saw`
--
ALTER TABLE `tb_saw`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tabelalternatif1`
--
ALTER TABLE `tabelalternatif1`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `tabelalternatif2`
--
ALTER TABLE `tabelalternatif2`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `tb_saw`
--
ALTER TABLE `tb_saw`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
