CREATE TABLE user (
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
	user_name 	VARCHAR(256)    	NOT NULL,
	league_id	INT	NOT NULL
);

CREATE TABLE player (
	player_id	INT	PRIMARY KEY	    NOT NULL AUTO_INCREMENT,
	position	VARCHAR(256)	    NOT NULL,
	real_team	VARCHAR(256)	    NOT NULL,
	name		VARCHAR(256)	    NOT NULL,
	url         VARCHAR(256)        
);

CREATE TABLE consists_of (
	player_id	INT	            NOT NULL,
	team_name	VARCHAR(256)	NOT NULL,
	active	    INT	            NOT NULL DEFAULT 0,  /* Will act as a boolean -- Ideas?*/
	PRIMARY KEY (player_id, team_name)
);

CREATE TABLE banned(
  email VARCHAR(256)  NOT NULL  PRIMARY KEY
);

CREATE TABLE matchups(
	player1 VARCHAR(256) NOT NULL,
	player2 VARCHAR(256) NOT NULL,
	winner VARCHAR(256),
	PRIMARY KEY(player1, player2)
);