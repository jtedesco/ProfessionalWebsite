<?php session_start();
require_once ("../databaseutils/MySqlRootDriver.php");

$player_id = $_POST["player"];
$user = $_SESSION['user'];
$team_name = $user . "\'s Team";
$query = "DELETE FROM consists_of WHERE team_name='$team_name' AND player_id='$player_id'";
$database_driver = new MySqlRootDriver();
$database_driver->query_database($query);

 header("Location: ../../manage_my_team.php");
?>
