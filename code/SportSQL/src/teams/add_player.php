<?php require_once ("src/databaseutils/MySqlRootDriver.php");?>

<form action="src/teams/insert_player.php" method="post" >
    <select name="player">
    <?php
        $position = $_POST["position"];

        $database_driver = new mySqlRootDriver();
        $result = $database_driver->query_database("SELECT * FROM player WHERE position='$position' AND player_id NOT IN(SELECT player_id FROM consists_of) ORDER BY name");
        if(!$result){
            echo "error";
        }
        while($row = mysql_fetch_assoc($result)){
            $player_name = $row["name"];
            $player_id = $row["player_id"];
            echo "<option value=$player_id>$player_name</option>";
        }
    ?>
    <input type="submit" value="Add" />
    </select>
</form>

 
