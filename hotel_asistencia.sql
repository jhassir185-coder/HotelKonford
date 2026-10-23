CREATE DATABASE  IF NOT EXISTS `hotelkonford` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `hotelkonford`;
-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: hotelkonford
-- ------------------------------------------------------
-- Server version	8.3.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `area`
--

DROP TABLE IF EXISTS `area`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `area` (
  `id_area` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_area`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `area`
--

LOCK TABLES `area` WRITE;
/*!40000 ALTER TABLE `area` DISABLE KEYS */;
INSERT INTO `area` VALUES (1,'Administración'),(2,'Recepción'),(3,'Limpieza y Housekeeping'),(4,'Mantenimiento'),(5,'Cocina y Restaurante'),(6,'Seguridad');
/*!40000 ALTER TABLE `area` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `asistencia`
--

DROP TABLE IF EXISTS `asistencia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `asistencia` (
  `id_asistencia` int NOT NULL AUTO_INCREMENT,
  `id_empleado` int DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  `hora_ingreso` time DEFAULT NULL,
  `hora_salida` time DEFAULT NULL,
  `estado` varchar(50) DEFAULT NULL,
  `observaciones` text,
  PRIMARY KEY (`id_asistencia`),
  KEY `FK_ASISTENCIA_EMPLEADO` (`id_empleado`),
  CONSTRAINT `FK_ASISTENCIA_EMPLEADO` FOREIGN KEY (`id_empleado`) REFERENCES `empleado` (`id_empleado`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `asistencia`
--

LOCK TABLES `asistencia` WRITE;
/*!40000 ALTER TABLE `asistencia` DISABLE KEYS */;
INSERT INTO `asistencia` VALUES (1,3,'2026-09-30','00:49:31',NULL,'PUNTUAL',NULL),(2,3,'2026-10-01','19:53:28',NULL,'FUERA_DE_TURNO',NULL);
/*!40000 ALTER TABLE `asistencia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cargo`
--

DROP TABLE IF EXISTS `cargo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cargo` (
  `id_cargo` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_cargo`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cargo`
--

LOCK TABLES `cargo` WRITE;
/*!40000 ALTER TABLE `cargo` DISABLE KEYS */;
INSERT INTO `cargo` VALUES (1,'Jefa de Recursos Humanos'),(2,'Recepcionista'),(3,'Administrador General'),(4,'Cocinero / Chef'),(5,'Técnico de Mantenimiento'),(6,'Personal de Housekeeping'),(7,'Agente de Seguridad'),(8,'Mozo / Mesero');
/*!40000 ALTER TABLE `cargo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `empleado`
--

DROP TABLE IF EXISTS `empleado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `empleado` (
  `id_empleado` int NOT NULL AUTO_INCREMENT,
  `id_cargo` int DEFAULT NULL,
  `id_rol` int DEFAULT NULL,
  `id_area` int DEFAULT NULL,
  `id_horario` int DEFAULT NULL,
  `id_tipo_documento` int DEFAULT NULL,
  `numero_documento` varchar(20) DEFAULT NULL,
  `nombres` varchar(255) DEFAULT NULL,
  `apellidos` varchar(255) DEFAULT NULL,
  `email` varchar(150) DEFAULT NULL,
  `telefono` varchar(50) DEFAULT NULL,
  `direccion` varchar(200) DEFAULT NULL,
  `fecha_ingreso` date DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `activo` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id_empleado`),
  KEY `FK_EMPLEADO_CARGO` (`id_cargo`),
  KEY `FK_EMPLEADO_ROL` (`id_rol`),
  KEY `FK_EMPLEADO_AREA` (`id_area`),
  KEY `FK_EMPLEADO_HORARIO` (`id_horario`),
  KEY `FK_EMPLEADO_TIPO_DOCUMENTO` (`id_tipo_documento`),
  CONSTRAINT `FK_EMPLEADO_AREA` FOREIGN KEY (`id_area`) REFERENCES `area` (`id_area`),
  CONSTRAINT `FK_EMPLEADO_CARGO` FOREIGN KEY (`id_cargo`) REFERENCES `cargo` (`id_cargo`),
  CONSTRAINT `FK_EMPLEADO_HORARIO` FOREIGN KEY (`id_horario`) REFERENCES `horario` (`id_horario`),
  CONSTRAINT `FK_EMPLEADO_ROL` FOREIGN KEY (`id_rol`) REFERENCES `rol` (`id_rol`),
  CONSTRAINT `FK_EMPLEADO_TIPO_DOCUMENTO` FOREIGN KEY (`id_tipo_documento`) REFERENCES `tipo_documento` (`id_tipo_documento`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `empleado`
--

LOCK TABLES `empleado` WRITE;
/*!40000 ALTER TABLE `empleado` DISABLE KEYS */;
INSERT INTO `empleado` VALUES (1,3,1,1,3,1,'74581236','Maria','Administradora','admin@hotelkonford.com',NULL,NULL,'2023-01-15','$2a$10$F.64XIUOSTlkf/8FYG5omuAMFVNflmYhjSmRlBtEG4THrlveaUjnS',1),(2,2,2,2,3,1,'12345678','Juan','Pérez','juan.perez@hotelkonford.com','987654321',NULL,'2024-02-10','$2a$10$.cEjhVdOghK65Hg/MPd5cevFIyobI6dUgfRxwYBLbDABvwBQtJzyC',1),(3,4,2,5,2,1,'77777788','Luca','Ziegler',NULL,NULL,NULL,'2026-09-30','$2a$10$kcICVkFVKvt0BuV1FrsxZeVgXZQwlXPWoLiNN645PI5SHVAje3Jr6',1),(4,5,2,4,2,1,'87654321','Carlos Alberto','Gómez Ruiz','carlos.gomez@hotelkonford.com',NULL,NULL,'2026-01-15','$2a$10$e8T33e3J8W8qE.oWJ0j2f.5zU1.u8M1g6cR7E9J3lK8mN2oP1qR2',1),(5,6,2,3,3,2,'00000004','Diego','Diaz',NULL,NULL,NULL,'2026-10-23','$2a$10$RgmOxhepEPLQJlAxjheGg.kn0uROIt8xjBPvrNcrkjUrr94F1Wt4S',1),(6,4,2,5,4,1,'77777799','Nicolas','Ramirez',NULL,NULL,NULL,'2026-10-23','$2a$10$ar2s66oPRGQNrz6N/yfRbuoFS.tLIY7nUSsX4ljTwDXJBwvgyPHE6',1),(7,7,2,6,2,1,'77777711','Alex','Bellido',NULL,NULL,NULL,'2026-10-23','$2a$10$.NFHLD/adqA3AxzrorHVxOjC/acbKKDElZF6MNaz/XrSV.aEcGmE2',1),(8,8,2,5,3,1,'77777722','Maximiliano','Julca',NULL,NULL,NULL,'2026-10-23','$2a$10$zk5uznKFsKxIDLBVPUVXT.kZth7blNO9JbECmp1JDUCgBdk5xrbSu',1);
/*!40000 ALTER TABLE `empleado` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `horario`
--

DROP TABLE IF EXISTS `horario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `horario` (
  `id_horario` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) DEFAULT NULL,
  `hora_entrada` time DEFAULT NULL,
  `hora_salida` time DEFAULT NULL,
  `tolerancia` int DEFAULT NULL,
  PRIMARY KEY (`id_horario`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `horario`
--

LOCK TABLES `horario` WRITE;
/*!40000 ALTER TABLE `horario` DISABLE KEYS */;
INSERT INTO `horario` VALUES (2,'Turno Noche (11:00 pm - 07:00 am)','23:00:00','07:00:00',15),(3,'Turno Mañana (07:00 am - 03:00 pm)','07:00:00','15:00:00',15),(4,'Turno Tarde (03:00 pm - 11:00 pm)','15:00:00','23:00:00',15);
/*!40000 ALTER TABLE `horario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permiso`
--

DROP TABLE IF EXISTS `permiso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `permiso` (
  `id_permiso` int NOT NULL AUTO_INCREMENT,
  `id_empleado` int DEFAULT NULL,
  `id_tipo_permiso` int DEFAULT NULL,
  `motivo` text,
  `estado` varchar(20) DEFAULT NULL,
  `fecha_solicitud` datetime DEFAULT NULL,
  `comentario_jefe` text,
  `comprobante_ruta` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id_permiso`),
  KEY `FK_PERMISO_EMPLEADO` (`id_empleado`),
  KEY `FK_PERMISO_TIPO_PERMISO` (`id_tipo_permiso`),
  CONSTRAINT `FK_PERMISO_EMPLEADO` FOREIGN KEY (`id_empleado`) REFERENCES `empleado` (`id_empleado`),
  CONSTRAINT `FK_PERMISO_TIPO_PERMISO` FOREIGN KEY (`id_tipo_permiso`) REFERENCES `tipo_permiso` (`id_tipo_permiso`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permiso`
--

LOCK TABLES `permiso` WRITE;
/*!40000 ALTER TABLE `permiso` DISABLE KEYS */;
INSERT INTO `permiso` VALUES (1,2,1,'FUTBOL','PENDIENTE','2026-09-30 05:47:10',NULL,NULL),(2,3,1,'Mundial','RECHAZADO','2026-09-30 05:49:04','No',NULL);
/*!40000 ALTER TABLE `permiso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rol`
--

DROP TABLE IF EXISTS `rol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rol` (
  `id_rol` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_rol`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rol`
--

LOCK TABLES `rol` WRITE;
/*!40000 ALTER TABLE `rol` DISABLE KEYS */;
INSERT INTO `rol` VALUES (1,'ADMIN'),(2,'TRABAJADOR');
/*!40000 ALTER TABLE `rol` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipo_documento`
--

DROP TABLE IF EXISTS `tipo_documento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tipo_documento` (
  `id_tipo_documento` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_tipo_documento`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipo_documento`
--

LOCK TABLES `tipo_documento` WRITE;
/*!40000 ALTER TABLE `tipo_documento` DISABLE KEYS */;
INSERT INTO `tipo_documento` VALUES (1,'DNI'),(2,'CARNE_EXTRANJERIA'),(3,'PASAPORTE');
/*!40000 ALTER TABLE `tipo_documento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipo_permiso`
--

DROP TABLE IF EXISTS `tipo_permiso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tipo_permiso` (
  `id_tipo_permiso` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_tipo_permiso`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipo_permiso`
--

LOCK TABLES `tipo_permiso` WRITE;
/*!40000 ALTER TABLE `tipo_permiso` DISABLE KEYS */;
INSERT INTO `tipo_permiso` VALUES (1,'PERSONAL'),(2,'SALUD'),(3,'MATERNIDAD');
/*!40000 ALTER TABLE `tipo_permiso` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-23 18:53:35
