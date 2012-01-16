<?php session_start();
require_once ("../databaseutils/MySqlRootDriver.php");

$player_id = $_POST["player"];
$user = $_SESSION['user'];
$team_name = $user . "\'s Team";
$query = "INSERT INTO consists_of VALUES('$player_id','$team_name',1);";
$database_driver = new MySqlRootDriver();
$database_driver->query_database($query);

 header("Location: ../../manage_my_team.php");
?>
