<?php
require_once ("../databaseutils/MySqlRootDriver.php");

$database_driver = new MySqlRootDriver();
$query = "SELECT * FROM team";
$result = $database_driver->query_database($query);
$teamnames = array();

while($row = mysql_fetch_array($result)){
	if($row['team_name'] != "admin"){
		array_push($teamnames, $row['team_name']);/*
		$p1 = $row['username'];
		$qqq = "INSERT INTO team VALUES ('$p1', 0,0,0,'$p1',1)";
		$result5 = $database_driver->query_database($qqq);*/
	}
}

shuffle($teamnames);

foreach ($teamnames as $player) {
    //echo "$player";
	//echo "<br/>";
}
$delete = "DELETE FROM matchups";
$database_driver->query_database($delete);

$size = count($teamnames);
for($i = 0; $i < $size-1; $i+=2){
	$j = $i + 1;
	if($teamnames[$i] == "" || $teamnames[$j] == '')
		continue;
    $teamname1 = $teamnames[$i];
    $teamname2 = $teamnames[$j];
	$insert = "INSERT INTO matchups (player1, player2) VALUES (\"$teamname1\", \"$teamname2\");";
	$database_driver->query_database($insert);
	echo "$insert";
	echo "<br/>";
}


?>