<?php session_start();

require_once ("src/databaseutils/MySqlRootDriver.php");?>

<form action="src/trade/swap.php" method="post" >
    <select name="give">
    <?php
        $_SESSION['PLAYER_TO_GET'] = $_POST["take"];
    	$user = $_SESSION['user'];
    	
        $result = $database_driver->query_database("SELECT * FROM team,consists_of,player WHERE user_name = '$user' AND team.team_name=consists_of.team_name AND player.player_id=consists_of.player_id");
        if(!$result){
            echo "error";
        }
        while($row = mysql_fetch_assoc($result)){
            $user = $row["name"];
            $id = $row["player_id"];
            echo "<option value=$id>$user</option>";
        }
    ?>
    <input type="submit" value="Make Trade" />
    </select>
    
    <?php
    ?>
</form>