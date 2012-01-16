<?php session_start();

require_once ("src/databaseutils/MySqlRootDriver.php");?>

<form action="trade_user.php" method="post" >
    <select name="player_team">
    <?php
       $database_driver = new mySqlRootDriver();
        $result = $database_driver->query_database("SELECT team_name FROM team;");
        if(!$result){
            echo "error";
        }
        while($row = mysql_fetch_assoc($result)){
            $user = $row["team_name"];
            if(isset($_POST['take'])){
                $compare = $_SESSION['USER_TO_TAKE'];
            }
            else{
                $compare = $_POST["player_team"];
            }
            if($user == $compare){
                echo "<option selected=\"true\">$user</option>";
            }
            else{
                echo "<option value=\"$user\">$user</option>";
            }

        }
    ?>
    <input type="submit" value="View Team" />
    </select>
    
    <?php 
    ?>
</form>