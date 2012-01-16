<?php
@session_start();
require_once ("../databaseutils/MySqlRootDriver.php");

$username = $_POST["username"];

$database_driver = new MySqlRootDriver();

$username = mysql_real_escape_string($username);

$result = $database_driver->query_database("SELECT * FROM user WHERE username='$username'");

if (mysql_num_rows($result) == 1) {

        //Remove user
    	$query = "DELETE FROM user WHERE username='$username';";
        $result = $database_driver->query_database($query);

        //Remove his team
        $query = "DELETE FROM team WHERE user_name='$username';";
        $result = $database_driver->query_database($query);

	header("Location: ../../admin_panel.php");

} else {

    $_SESSION['new_account_message'] = "<h1>Sorry, no account with those credentials exist.</h1><br/>"; 
	header('Location: ../../admin_panel.php');
}
?>