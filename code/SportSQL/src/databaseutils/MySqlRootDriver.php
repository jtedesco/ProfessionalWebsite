<?php

/**
 *  This class provides a neat point of access to the database. However, this is given root
 *      access, so the queries must be stripped before being executed.
 */
 class MySqlRootDriver {

    //The connection to the database
    private $link;

   /**
    *   Constructor for the driver. Usage:
    *
    *      $some_variable = new MySqlRootDriver();
    *
    *   Using this implementation, the database will automatically disconnect
    *      when the object goes out of scope.
    *
    *   WARNING: This will give a root access connection to the database
    */
    function __construct() {

        //Gives root access to the database! Check
        $host = 'localhost';
        $username = 'root';
        $password = 'root';
		$database = 'sportsql';
		
		//Connect to MySQL and connect to the SportSQL database
        $this->link = mysql_connect($host, $username, $password);
		mysql_select_db($database, $this->link);
    }

   /**
    *   Essentially, this just acts as a wrapper to perform queries
    *		on this database instance. Will take in some query, and
    *		return the result of that query
    */
    public function query_database($query){

		//Just perform the query and return the result
		$result = mysql_query($query, $this->link);
		return $result;
    }

   /**
	*	This function will automatically be called when the object goes
	*		out of scope, and disconnect gracefully as such
	*/
	function __destruct() {
		mysql_close($this->link);
	}
}
?>