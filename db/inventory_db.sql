-- MySQL Script generated by MySQL Workbench
-- Sat Oct 19 21:09:13 2019
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema media
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema media
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `media` DEFAULT CHARACTER SET utf8mb4 ;
USE `media` ;

-- -----------------------------------------------------
-- Table `media`.`book`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `media`.`book` (
  `BookID` INT(11) NOT NULL,
  `Title` VARCHAR(250) NULL DEFAULT '',
  `Description` LONGTEXT NULL DEFAULT NULL,
  `Genre` VARCHAR(250) NULL DEFAULT '',
  `Author` VARCHAR(250) NULL DEFAULT '',
  `ISBN` VARCHAR(250) NULL DEFAULT '',
  PRIMARY KEY (`BookID`),
  UNIQUE INDEX `BookID_UNIQUE` (`BookID` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `media`.`cd`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `media`.`cd` (
  `CDID` INT(11) NOT NULL,
  `Title` VARCHAR(250) NULL DEFAULT '',
  `Description` LONGTEXT NULL DEFAULT NULL,
  `Genre` VARCHAR(250) NULL DEFAULT '',
  `Artist` VARCHAR(250) NULL DEFAULT '',
  PRIMARY KEY (`CDID`),
  UNIQUE INDEX `ID_UNIQUE` (`CDID` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `media`.`dvd`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `media`.`dvd` (
  `DVDID` INT(11) NOT NULL,
  `Title` VARCHAR(250) NULL DEFAULT '',
  `Description` LONGTEXT NULL DEFAULT NULL,
  `Genre` VARCHAR(250) NULL DEFAULT '',
  `Cast` VARCHAR(250) NULL DEFAULT '',
  PRIMARY KEY (`DVDID`),
  UNIQUE INDEX `DVDID_UNIQUE` (`DVDID` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `media`.`inventory`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `media`.`inventory` (
  `MediaID` INT(11) NOT NULL,
  `Quantity` INT(11) NULL DEFAULT '1',
  PRIMARY KEY (`MediaID`),
  UNIQUE INDEX `MediaID_UNIQUE` (`MediaID` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `media`.`mediaID`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `media`.`mediaID` (
  `MediaID` INT(11) NOT NULL AUTO_INCREMENT,
  `IDCounter` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`MediaID`),
  UNIQUE INDEX `MediaID_UNIQUE` (`MediaID` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = latin1;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
