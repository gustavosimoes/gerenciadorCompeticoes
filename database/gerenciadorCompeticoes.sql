SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema gerenciadorCompeticoes
-- -----------------------------------------------------

DROP SCHEMA IF EXISTS `gerenciadorCompeticoes`;
CREATE SCHEMA IF NOT EXISTS `gerenciadorCompeticoes` DEFAULT CHARACTER SET utf8 ;
USE `gerenciadorCompeticoes` ;

-- -----------------------------------------------------
-- Table `gerenciadorCompeticoes`.`torneio`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gerenciadorCompeticoes`.`torneio` (
  `idtorneio` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(20) NOT NULL UNIQUE KEY,
  `localizacao` VARCHAR(20) NULL,
  PRIMARY KEY (`idtorneio`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gerenciadorCompeticoes`.`competidor`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gerenciadorCompeticoes`.`competidor` (
  `idcompetidor` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(20) NOT NULL,
  `idade` INT NULL,
  `sexo` VARCHAR(10) NULL,
  `equipe_idequipe` INT NOT NULL,
  PRIMARY KEY (`idcompetidor`),
  INDEX `fk_competidor_equipe1_idx` (`equipe_idequipe` ASC),
  CONSTRAINT `fk_competidor_equipe1`
    FOREIGN KEY (`equipe_idequipe`)
    REFERENCES `gerenciadorCompeticoes`.`equipe` (`idequipe`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gerenciadorCompeticoes`.`equipe`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gerenciadorCompeticoes`.`equipe` (
  `idequipe` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(30) UNIQUE KEY,
  `competidor_idcompetidor` INT NULL,
  PRIMARY KEY (`idequipe`),
  INDEX `fk_equipe_competidor1_idx` (`competidor_idcompetidor` ASC),
  CONSTRAINT `fk_equipe_competidor1`
    FOREIGN KEY (`competidor_idcompetidor`)
    REFERENCES `gerenciadorCompeticoes`.`competidor` (`idcompetidor`)
    ON DELETE SET NULL
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gerenciadorCompeticoes`.`torneio_has_equipe`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gerenciadorCompeticoes`.`torneio_has_equipe` (
  `torneio_idtorneio` INT NOT NULL,
  `equipe_idequipe` INT NOT NULL,
  PRIMARY KEY (`torneio_idtorneio`, `equipe_idequipe`),
  INDEX `fk_torneio_has_equipe_equipe1_idx` (`equipe_idequipe` ASC),
  INDEX `fk_torneio_has_equipe_torneio_idx` (`torneio_idtorneio` ASC),
  CONSTRAINT `fk_torneio_has_equipe_torneio`
    FOREIGN KEY (`torneio_idtorneio`)
    REFERENCES `gerenciadorCompeticoes`.`torneio` (`idtorneio`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_torneio_has_equipe_equipe1`
    FOREIGN KEY (`equipe_idequipe`)
    REFERENCES `gerenciadorCompeticoes`.`equipe` (`idequipe`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

