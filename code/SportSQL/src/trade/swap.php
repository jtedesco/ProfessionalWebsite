<?php session_start();
require_once ("../databaseutils/MySqlRootDriver.php");

$get_id = $_SESSION['PLAYER_TO_GET'];
$send_id = $_POST['give'];
$temp = "-0";

$query_send = "UPDATE consists_of SET player_id='$temp' WHERE player_id='$get_id';";

$query_get = "UPDATE consists_of SET player_id='$get_id' WHERE player_id='$send_id';";

$query_send_finalize = "UPDATE consists_of SET player_id='$send_id' WHERE player_id='$temp';";


$database_driver = new MySqlRootDriver();

$database_driver->query_database($query_send);
$database_driver->query_database($query_get);
$database_driver->query_database($query_send_finalize);

header("Location: ../../my_team.php");
?>