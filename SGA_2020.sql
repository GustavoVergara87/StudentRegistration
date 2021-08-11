/*
SQLyog Ultimate v9.02 
MySQL - 5.6.17 : Database - SGA_2020
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`SGA_2020` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `SGA_2020`;

/*Table structure for table `alumno` */

DROP TABLE IF EXISTS `alumno`;

CREATE TABLE `alumno` (
  `alu_dni` bigint(10) unsigned NOT NULL,
  `alu_nombre` varchar(30) NOT NULL,
  `alu_apellido` varchar(50) NOT NULL,
  `alu_fec_nac` date DEFAULT NULL,
  `alu_domicilio` varchar(100) DEFAULT NULL,
  `alu_telefono` varchar(50) DEFAULT NULL,
  `alu_insc_cod` bigint(10) DEFAULT NULL,
  PRIMARY KEY (`alu_dni`),
  KEY `FK_alumno_inscripcion` (`alu_insc_cod`),
  CONSTRAINT `FK_alumno_inscripcion` FOREIGN KEY (`alu_insc_cod`) REFERENCES `inscripcion` (`insc_cod`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `alumno` */

insert  into `alumno`(`alu_dni`,`alu_nombre`,`alu_apellido`,`alu_fec_nac`,`alu_domicilio`,`alu_telefono`,`alu_insc_cod`) values (21654789,'Nicolasa','Ciciliani','2021-01-23','Uspallata 40','+654123897',14),(22654789,'Maria','Antonieta','1985-09-05','Salamanca 689','+2245678912',15),(25698854,'Molina','Blotta','2009-07-15','25 mayo 55','+5546879412',18),(32956956,'Bart','Simpson','1985-08-12','SiempreViva 1223','+0125641873',18),(32956957,'Gustavo','Vergara','1987-06-14','Beltran 600','+654789132',16);

/*Table structure for table `carrera` */

DROP TABLE IF EXISTS `carrera`;

CREATE TABLE `carrera` (
  `car_cod` bigint(10) unsigned NOT NULL AUTO_INCREMENT,
  `car_nombre` varchar(30) NOT NULL,
  `car_duracion` int(2) unsigned NOT NULL,
  PRIMARY KEY (`car_cod`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;

/*Data for the table `carrera` */

insert  into `carrera`(`car_cod`,`car_nombre`,`car_duracion`) values (8,'Ingenieria en sistemas',5),(9,'Programacion',2),(10,'Medicina',5);

/*Table structure for table `cursado` */

DROP TABLE IF EXISTS `cursado`;

CREATE TABLE `cursado` (
  `cur_alu_dni` bigint(10) unsigned NOT NULL,
  `cur_mat_cod` bigint(10) unsigned NOT NULL,
  `cur_nota` float unsigned DEFAULT NULL,
  PRIMARY KEY (`cur_alu_dni`,`cur_mat_cod`),
  KEY `FK_cursado_materia` (`cur_mat_cod`),
  CONSTRAINT `FK_cursado_alumno` FOREIGN KEY (`cur_alu_dni`) REFERENCES `alumno` (`alu_dni`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_cursado_materia` FOREIGN KEY (`cur_mat_cod`) REFERENCES `materia` (`mat_cod`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `cursado` */

insert  into `cursado`(`cur_alu_dni`,`cur_mat_cod`,`cur_nota`) values (22654789,24,8),(32956957,22,9),(32956957,23,4);

/*Table structure for table `inscripcion` */

DROP TABLE IF EXISTS `inscripcion`;

CREATE TABLE `inscripcion` (
  `insc_cod` bigint(10) NOT NULL AUTO_INCREMENT,
  `insc_nombre` varchar(30) NOT NULL,
  `insc_fecha` date NOT NULL,
  `insc_car_cod` bigint(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`insc_cod`),
  KEY `FK_inscripcion` (`insc_car_cod`),
  CONSTRAINT `FK_inscripcion` FOREIGN KEY (`insc_car_cod`) REFERENCES `carrera` (`car_cod`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=latin1;

/*Data for the table `inscripcion` */

insert  into `inscripcion`(`insc_cod`,`insc_nombre`,`insc_fecha`,`insc_car_cod`) values (14,'Ingenieria TM','2021-02-02',8),(15,'Ingenieria TT','2021-02-02',8),(16,'Programacion TT','2021-02-03',9),(17,'Programacion TM','2021-02-03',9),(18,'Medicina TT','2021-02-02',10);

/*Table structure for table `materia` */

DROP TABLE IF EXISTS `materia`;

CREATE TABLE `materia` (
  `mat_cod` bigint(10) unsigned NOT NULL AUTO_INCREMENT,
  `mat_nombre` varchar(30) NOT NULL,
  `mat_prof_dni` bigint(10) unsigned NOT NULL,
  PRIMARY KEY (`mat_cod`),
  KEY `FK_materia_profesor` (`mat_prof_dni`),
  CONSTRAINT `FK_materia_profesor` FOREIGN KEY (`mat_prof_dni`) REFERENCES `profesor` (`prof_dni`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=latin1;

/*Data for the table `materia` */

insert  into `materia`(`mat_cod`,`mat_nombre`,`mat_prof_dni`) values (22,'Anatomia y Disección',25654789),(23,'Algoritmos',22987456),(24,'Biologia',21456987);

/*Table structure for table `profesor` */

DROP TABLE IF EXISTS `profesor`;

CREATE TABLE `profesor` (
  `prof_dni` bigint(10) unsigned NOT NULL,
  `prof_nombre` varchar(30) NOT NULL,
  `prof_apellido` varchar(30) NOT NULL,
  `prof_fec_nac` date DEFAULT NULL,
  `prof_domicilio` varchar(100) NOT NULL,
  `prof_telefono` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`prof_dni`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `profesor` */

insert  into `profesor`(`prof_dni`,`prof_nombre`,`prof_apellido`,`prof_fec_nac`,`prof_domicilio`,`prof_telefono`) values (21456987,'Walter','Lapadula','1986-10-06','Beltran 1600','+54987321465'),(22987456,'Matias','Fornes','1986-03-03','Necochea 1700','+54654789321'),(25654789,'Felipe','Calderon','1986-12-15','Benavente 36','+54123654789');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
