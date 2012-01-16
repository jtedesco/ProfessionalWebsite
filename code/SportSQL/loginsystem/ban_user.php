<?php
    session_start();
    require_once ("../databaseutils/MySqlRootDriver.php");

   
    $email = $_POST["email"];

    //Get a connection to the database and check if this user already exists
    $database_driver = new MySqlRootDriver();
    
    // SQL Injection Checks
    // $email = mysql_real_escape_string($email);
    
    $query = "SELECT * FROM banned WHERE email='$email'";
    $result = $database_driver->query_database($query);

    // If the email is already in the database display an error message
    if (mysql_num_rows($result) == 1) {

        $_SESSION['create_account_error_message'] = "<h1><br/>The specified email is already banned.</h1>";
        header("Location: ../../admin_panel.php");

    } else {

        //Create this new account
        $query = "INSERT INTO banned VALUES ('$email');";
        $database_driver->query_database($query);

        header("Location: ../../admin_panel.php");
    }

?>