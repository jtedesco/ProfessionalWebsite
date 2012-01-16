<?php
@session_start();
require_once ("../databaseutils/MySqlRootDriver.php");

$username = $_POST["username"];
$password = $_POST["password"];

$database_driver = new MySqlRootDriver();

$username = mysql_real_escape_string($username);
$password = mysql_real_escape_string($password);

$result = $database_driver->query_database("SELECT * FROM user WHERE username='$username' and password='$password'");

if (mysql_num_rows($result) == 1) {

    $_SESSION['user'] = $username;
	header("Location: ../../index.php");

} else {

    $_SESSION['new_account_message'] = "<h1>Sorry, no account with those credentials exist.</h1><br/>"; 
	header('Location: ../../create_new_account.php');
}
?>