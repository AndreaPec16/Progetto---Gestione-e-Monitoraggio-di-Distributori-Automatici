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


-- Dump della struttura del database coffee_app_main
CREATE DATABASE IF NOT EXISTS `coffee_app_main` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci */;
USE `coffee_app_main`;

-- Dump della struttura di tabella coffee_app_main.distributori
CREATE TABLE IF NOT EXISTS `distributori` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `codice_visivo` varchar(255) DEFAULT NULL,
  `indirizzo` varchar(255) DEFAULT NULL,
  `modello` varchar(255) DEFAULT NULL,
  `livello_zucchero` int(11) DEFAULT 100,
  `livello_caffe` int(11) DEFAULT 100,
  `livello_bicchieri` int(11) DEFAULT 100,
  `livello_acqua` int(11) DEFAULT 100,
  `livello_palette` int(11) DEFAULT 100,
  `livello_cioccolato` int(11) DEFAULT 100,
  `livello_latte` int(11) DEFAULT 100,
  `livello_te` int(11) DEFAULT 100,
  `latitudine` double DEFAULT 0,
  `longitudine` double DEFAULT 0,
  `data_ultima_manutenzione` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Dump dei dati della tabella coffee_app_main.distributori: ~3 rows (circa)
INSERT INTO `distributori` (`id`, `codice_visivo`, `indirizzo`, `modello`, `livello_zucchero`, `livello_caffe`, `livello_bicchieri`, `livello_acqua`, `livello_palette`, `livello_cioccolato`, `livello_latte`, `livello_te`, `latitudine`, `longitudine`, `data_ultima_manutenzione`) VALUES
	(9, 'MCH010', 'via catania', 'fr400', 99, 95, 99, 90, 100, 100, 100, 100, 41.907333, 12.463406, '2026-01-04 13:34:03.002669'),
	(17, 'MCH018', 'via notarbartolo', 'tr800', 97, 100, 98, 80, 100, 90, 100, 90, 41.902303, 12.514801, '2026-01-04 12:24:56.624944');

-- Dump della struttura di tabella coffee_app_main.prodotto
CREATE TABLE IF NOT EXISTS `prodotto` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) NOT NULL,
  `prezzo` decimal(10,2) DEFAULT NULL,
  `consumo_caffe` int(11) DEFAULT 0,
  `consumo_latte` int(11) DEFAULT 0,
  `consumo_cioccolato` int(11) DEFAULT 0,
  `consumo_te` int(11) DEFAULT 0,
  `consumo_acqua` int(11) DEFAULT 10,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nome` (`nome`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Dump dei dati della tabella coffee_app_main.prodotto: ~4 rows (circa)
INSERT INTO `prodotto` (`id`, `nome`, `prezzo`, `consumo_caffe`, `consumo_latte`, `consumo_cioccolato`, `consumo_te`, `consumo_acqua`) VALUES
	(1, 'Caffè', 0.50, 5, 0, 0, 0, 10),
	(2, 'Cappuccino', 0.80, 3, 7, 0, 0, 10),
	(3, 'Cioccolato', 0.70, 0, 0, 10, 0, 10),
	(4, 'Tè', 0.60, 0, 0, 0, 10, 10);

-- Dump della struttura di tabella coffee_app_main.utenti
CREATE TABLE IF NOT EXISTS `utenti` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `ruolo` varchar(255) DEFAULT NULL,
  `credito` double DEFAULT NULL,
  `nome` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `connected_machine_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Dump dei dati della tabella coffee_app_main.utenti: ~5 rows (circa)
INSERT INTO `utenti` (`id`, `username`, `password`, `ruolo`, `credito`, `nome`, `email`, `connected_machine_id`) VALUES
	(2, 'admin', '$2a$10$LMNR3uSaJjQ./Pp4RCojVefVVlsB5mcA5XEYO2PaHQGvRJYaJ5O7u', 'GESTORE', 0, 'Admin', 'admin@mycoffee.it', NULL),
	(18, 'cliente', '$2a$10$Xii51X3UALXDLXtyLmG7sOUI4wuzydSCq0DEzPAr50rEu1jr94m1u', 'CLIENTE', 0, 'cliente', 'cliente@prova.it', NULL),
	(19, 'user', '$2a$10$lNPcZhpgC138j32q1qHOKuq8QIdyqAcsRu.rx3ytZqMBJBegB64AO', 'CLIENTE', 0, 'user', 'user@email.com', NULL),
	(20, 'asd', '$2a$10$C8apU9TTpdQYThTMJZZvDO8fQc/2PGd2WibB6q8r/D6v9w1uBnx0W', 'CLIENTE', 3.8, 'asd', 'asd@email.com', NULL),
	(22, 'addetto', '$2a$10$XOwe9zEZ4lmkS0CEuv75PuJyzRWgIfZCwvPj1NysJn0K2m10nMsDK', 'MANUTENTORE', 0, 'addetto', 'addetto@email.com', NULL);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
