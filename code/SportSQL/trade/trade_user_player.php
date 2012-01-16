<?php session_start();
require_once ("src/databaseutils/MySqlRootDriver.php");?>

<form action="trade_player.php" method="post" >
    <select name="take">
    <?php
        if(isset($_POST["player_team"])){
        $_SESSION['USER_TO_TAKE'] = $_POST["player_team"];
        }
        $team = $_SESSION['USER_TO_TAKE'];
        $result = $database_driver->query_database("SELECT * FROM consists_of,player WHERE consists_of.team_name=\"$team\" AND player.player_id=consists_of.player_id");
        if(!$result){
            echo "error";
        }
        while($row = mysql_fetch_assoc($result)){
            $player = $row["name"];
            $id = $row["player_id"];
            if($id == $_POST['take']){
                echo "<option selected=\"true\" value=$id>$player</option>";
            }
            else{
                echo "<option value=$id>$player</option>";
            }
        }
    ?>
    <input type="submit" value="Trade For" />
    </select>
    
    <?php
    $_SESSION['PLAYER_TO_GET'] = $_POST['take']; ?>
</form>