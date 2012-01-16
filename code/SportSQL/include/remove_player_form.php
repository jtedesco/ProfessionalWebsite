<h2>Remove Player</h2>
<br />
<?php
?>
<form action="src/teams/remove_player.php" method="post">
		<select name="player">
            <?php
                $team_name = $_SESSION['user'] . "\'s Team";

                $database_driver = new mySqlRootDriver();
                $result = $database_driver->query_database("SELECT consists_of.player_id, name FROM consists_of, player
                WHERE consists_of.player_id=player.player_id AND team_name='$team_name'");

                if(!$result){
                    echo "error";
                }

                while($row = mysql_fetch_assoc($result)){
                    $player_name = $row["name"];
                    $player_id = $row["player_id"];
                    echo "<option value=$player_id>$player_name</option>";
                }
                ?>
		</select>
	<input type="submit" value="Remove Player" />
</form>