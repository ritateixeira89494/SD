create database if not exists `sd_db`;

use `sd_db`;

create table if not exists `sd_db`.`Utilizador` (
		`idUtilizador` int unsigned unique not null auto_increment,
        `Email` varchar(45) not null unique,
        `Nome` varchar(20) not null,
        `Password` varchar(30) not null,
        `Tipo` int,
        Primary Key (`idUtilizador`)
)
Engine = InnoDB;


create table if not exists `sd_db`.`Administrador` (
	`idAdministrador` int unsigned unique not null,
    Primary Key (`idAdministrador`)
)
Engine = InnoDB;

create table if not exists `sd_db`.`UtilizadorNormal` (
	`idUtilizadorNormal` int unsigned unique not null,
    Primary Key (`idUtilizadorNormal`)
)
Engine = InnoDB;


create table if not exists `sd_db`.`Voo` (
	`idVoo` int unsigned unique not null auto_increment,
    `Partida` varchar(100) not null,
    `Destino` varchar(100) not null,
    `Capacidade` int not null,
    `Ocupacao` int,
    `Duracao` int not null,
    Primary Key (`idVoo`)
)
Engine = InnoDB;

CREATE TABLE IF NOT EXISTS `sd_db`.`Reserva` (
  `idReserva` INT UNSIGNED UNIQUE NOT NULL AUTO_INCREMENT,
  `idUtilizador` INT UNSIGNED NOT NULL,
  `idVoo` INT UNSIGNED NOT NULL,
  `Data_Reserva` DATETIME NOT NULL,
  `Data_Voo` DATETIME NOT NULL,
  PRIMARY KEY (`idReserva`),
  INDEX `fk_Registo_Utilizador_idx` (`idUtilizador` ASC) VISIBLE,
  INDEX `fk_Reserva_Voo1_idx` (`idVoo` ASC) VISIBLE,
  CONSTRAINT `fk_Registo_Utilizador`
    FOREIGN KEY (`idUtilizador`)
    REFERENCES `sd_db`.`Utilizador` (`idUtilizador`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Reserva_Voo1`
    FOREIGN KEY (`idVoo`)
    REFERENCES `sd_db`.`Voo` (`idVoo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;