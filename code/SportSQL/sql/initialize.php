<?php

echo "<html>hi</html>";

require_once("../databaseutils/MySqlRootDriver.php");

$query = "CREATE TABLE user (
	username	VARCHAR(256)        PRIMARY KEY 	NOT NULL,
	password	VARCHAR(256)	    NOT NULL,
	email	    VARCHAR(256),
	firstname	VARCHAR(256),
	lastname	VARCHAR(256),
	league_id	INT
);

CREATE TABLE team (
	team_name	VARCHAR(256)	    PRIMARY KEY	NOT NULL,
	wins		INT	DEFAULT 0,
	losses	    INT	DEFAULT 0,
	ties		INT	DEFAULT 0,
	user_id 	INT	NOT NULL,
	league_id	INT	NOT NULL
);

CREATE TABLE player (
	player_id	INT	PRIMARY KEY	    NOT NULL AUTO_INCREMENT,
	position	VARCHAR(256)	    NOT NULL,
	real_team	VARCHAR(256)	    NOT NULL,
	name		VARCHAR(256)	    NOT NULL,
	stats	    INT  /*Please edit this, I don't know what's included in stats*/
);

CREATE TABLE has (
	league_id	INT	    NOT NULL,
	player_id	INT	    NOT NULL,
	PRIMARY KEY (league_id, player_id)
);

CREATE TABLE consists_of (
	player_id	INT	            NOT NULL,
	team_name	VARCHAR(256)	NOT NULL,
	active	    INT	            NOT NULL	    DEFAULT 0,  /* Will act as a boolean -- Ideas?*/
	PRIMARY KEY (player_id, team_name)
);

CREATE TABLE matchups(
	player1 VARCHAR(256) NOT NULL,
	player2 VARCHAR(256),
	winner INT 
);";

//Grab an instance of the database
$database_driver = new MySqlRootDriver();
$result = $database_driver->query_database($query);
?>