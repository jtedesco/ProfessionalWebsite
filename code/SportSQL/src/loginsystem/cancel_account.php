<?php
    //Start the session
    session_start();
    require_once("../databaseutils/MySqlRootDriver.php");

    if(isset($_SESSION['user'])){

        //Grab an instance of the database
        $database_driver = new MySqlRootDriver();

        //Form the query and query the database
        $username = $_SESSION['user'];
        $query = "DELETE FROM user WHERE username='$username';";
        $result = $database_driver->query_database($query);

        unset($_SESSION['user']);
    }
    header('Location: ../../index.php');
?>
