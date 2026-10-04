-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versione server:              11.8.2-MariaDB - mariadb.org binary distribution
-- S.O. server:                  Win64
-- HeidiSQL Versione:            12.10.0.7000
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dump della struttura del database monitoraggio_db
CREATE DATABASE IF NOT EXISTS `monitoraggio_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci */;
USE `monitoraggio_db`;

-- Dump della struttura di tabella monitoraggio_db.stato_macchine
CREATE TABLE IF NOT EXISTS `stato_macchine` (
  `codice_visivo` varchar(50) NOT NULL,
  `stato` varchar(20) NOT NULL,
  `ultimo_heartbeat` bigint(20) DEFAULT NULL,
  `latitudine` double DEFAULT 0,
  `longitudine` double DEFAULT 0,
  `ultimo_aggiornamento` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`codice_visivo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Dump dei dati della tabella monitoraggio_db.stato_macchine: ~3 rows (circa)
INSERT INTO `stato_macchine` (`codice_visivo`, `stato`, `ultimo_heartbeat`, `latitudine`, `longitudine`, `ultimo_aggiornamento`) VALUES
	('MCH010', 'ATTIVO', 1768654710423, 41.907333, 12.463406, '2026-01-17 12:58:30'),
	('MCH018', 'ATTIVO', 1768654747424, 41.902303, 12.514801, '2026-01-17 12:59:07');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
