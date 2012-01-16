<?php
require_once ("../databaseutils/MySqlRootDriver.php");

$database_driver = new MySqlRootDriver();
$query = "SELECT * FROM matchups";
$matchups_result = $database_driver->query_database($query);


while($matchup_row = mysql_fetch_array($matchups_result)){
    set_time_limit(300);

	$user1 = $matchup_row['player1'];
	$user2 = $matchup_row['player2'];

    $user1_query = "SELECT name, position, url FROM consists_of, player WHERE consists_of.player_id = player.player_id AND team_name = \"$user1\"";
    $user2_query = "SELECT name, position, url FROM consists_of, player WHERE consists_of.player_id = player.player_id AND team_name = \"$user2\"";

    $user1_result = $database_driver->query_database($user1_query);
    $user2_result = $database_driver->query_database($user2_query);

    $user1_points = 0;
    $user2_points = 0;

    //Get user1's points

    while($user1_row = mysql_fetch_assoc($user1_result)){
        set_time_limit(300);

        $url = $user1_row["url"];
        $position = $user1_row["position"];
        $player = $user1_row["name"];

        $points = 0;
        if($position != "D"){
            $raw = file_get_contents($url);

            $newlines = array("\t","\n","\r","\x20\x20","\0","x0B");

            $content = str_replace($newlines, "", html_entity_decode($raw));

            $start = strpos($content,'<table cellspacing="1" cellpadding="0" width="100%" class="datatablecell">');

            $end = strpos($content,'</table>');

            $table = substr($content,$start,$end-$start);

            preg_match_all("|<tr(.*)</tr>|U",$table,$rows);

            //Find most recent week
            $row_of_week = 0;
            while(strpos($rows[0][$row_of_week+1],'<tr><td colspan=')===false){
                $row_of_week++;
            }
            //Get points from touchdowns and yards for the recent week
            $row = $rows[0][$row_of_week];
            preg_match_all("|<td(.*)</td>|U",$row,$cells);

            if($position=="QB"){
                $recv_yds = strip_tags($cells[0][6]);
                $recv_tds = strip_tags($cells[0][8]);
                $rush_yds = strip_tags($cells[0][14]);
                $rush_tds = strip_tags($cells[0][16]);
            }
            if($position=="RB" || $position=="WR" || $position=="TE"){
                $recv_yds = strip_tags($cells[0][4]);
                $recv_tds = strip_tags($cells[0][7]);
                $rush_yds = strip_tags($cells[0][9]);
                $rush_tds = strip_tags($cells[0][12]);
            }
            if($position=="QB" || $position=="RB" || $position=="WR" || $position=="TE"){
                if($recv_yds == "--" || $recv_yds == ""){
                    $recv_yds = 0;
                }
                if($recv_tds == "--" || $recv_tds == ""){
                    $recv_tds = 0;
                }
                if($rush_yds == "--" || $rush_yds == ""){
                    $rush_yds = 0;
                }
                if($rush_tds == "--" || $rush_tds == ""){
                    $rush_tds = 0;
                }
                echo $player . " RECV_YDS: " . $recv_yds . "<br>";
                echo $player . " RECV_TDS: " . $recv_tds . "<br>";
                echo $player . " RUSH_YDS: " . $rush_yds . "<br>";
                echo $player . " RUSH_TDS: " . $rush_tds . "<br>";
                $points = $recv_yds/25 + $recv_tds*5 + $rush_yds/25 + $rush_tds*5;
                echo $player . " PTS: " . $points . "<br>";
            }
            if($position=="K"){
                $fg = strip_tags($cells[0][6]);
                echo $player . " FGS: " . $fg . "<br>";
                $points = $fg * 5;
                echo $player . " PTS: " . $points . "<br>";
            }

        }    
        $user1_points += $points;

    }

    echo $user1 . " GETS: " . $user1_points . " PTS <br>";

    //Get user2's points

    while($user2_row = mysql_fetch_assoc($user2_result)){
        set_time_limit(300);

        $url = $user2_row["url"];
        $position = $user2_row["position"];
        $player = $user2_row["name"];

        $points = 0;
        if($position != "D"){
            $raw = file_get_contents($url);

            $newlines = array("\t","\n","\r","\x20\x20","\0","x0B");

            $content = str_replace($newlines, "", html_entity_decode($raw));

            $start = strpos($content,'<table cellspacing="1" cellpadding="0" width="100%" class="datatablecell">');

            $end = strpos($content,'</table>');

            $table = substr($content,$start,$end-$start);

            preg_match_all("|<tr(.*)</tr>|U",$table,$rows);

            //Find most recent week
            $row_of_week = 0;
            while(strpos($rows[0][$row_of_week+1],'<tr><td colspan=')===false){
                $row_of_week++;
            }
            //Get points from touchdowns and yards for the recent week
            $row = $rows[0][$row_of_week];
            preg_match_all("|<td(.*)</td>|U",$row,$cells);

            if($position=="QB"){
                $recv_yds = strip_tags($cells[0][6]);
                $recv_tds = strip_tags($cells[0][8]);
                $rush_yds = strip_tags($cells[0][14]);
                $rush_tds = strip_tags($cells[0][16]);
            }
            if($position=="RB" || $position=="WR" || $position=="TE"){
                $recv_yds = strip_tags($cells[0][4]);
                $recv_tds = strip_tags($cells[0][7]);
                $rush_yds = strip_tags($cells[0][9]);
                $rush_tds = strip_tags($cells[0][12]);
            }
            if($position=="QB" || $position=="RB" || $position=="WR" || $position=="TE"){
                if($recv_yds == "--" || $recv_yds == ""){
                    $recv_yds = 0;
                }
                if($recv_tds == "--" || $recv_tds == ""){
                    $recv_tds = 0;
                }
                if($rush_yds == "--" || $rush_yds == ""){
                    $rush_yds = 0;
                }
                if($rush_tds == "--" || $rush_tds == ""){
                    $rush_tds = 0;
                }
                echo $player . " RECV_YDS: " . $recv_yds . "<br>";
                echo $player . " RECV_TDS: " . $recv_tds . "<br>";
                echo $player . " RUSH_YDS: " . $rush_yds . "<br>";
                echo $player . " RUSH_TDS: " . $rush_tds . "<br>";
                $points = $recv_yds/25 + $recv_tds*5 + $rush_yds/25 + $rush_tds*5;
                echo $player . " PTS: " . $points . "<br>";
            }
            if($position=="K"){
                $fg = strip_tags($cells[0][6]);
                echo $player . " FGS: " . $fg . "<br>";
                $points = $fg * 5;
                echo $player . " PTS: " . $points . "<br>";
            }
        }
        $user2_points += $points;

    }

    echo $user2 . " GETS: " . $user2_points . " PTS <br>"; 

	if( $user1_points > $user2_points ){
		$query = "UPDATE matchups SET winner=\"$user1\" WHERE player1=\"$user1\";";
		$database_driver->query_database($query);
		$wins = $database_driver->query_database("SELECT wins FROM team WHERE team_name=\"$user1\"");
		$wtf = mysql_fetch_array($wins);
		$wins = $wtf[0] + 1;
		$query = "UPDATE team SET wins='$wins' WHERE team_name=\"$user1\";";
		$database_driver->query_database($query);
		
		$losses = $database_driver->query_database("SELECT losses FROM team WHERE team_name=\"$user2\"");
		$wtf = mysql_fetch_array($losses);
		$losses = $wtf[0] + 1;
		$query ="UPDATE team SET losses='$losses' WHERE team_name=\"$user2\";";
		$database_driver->query_database($query);
		
	}
	else if( $user1_points < $user2_points){
		$query = "UPDATE matchups SET winner=\"$user2\" WHERE player2=\"$user2\";" ;
		$database_driver->query_database($query);
		
		$wins = $database_driver->query_database("SELECT wins FROM team WHERE team_name=\"$user2\"");
		$wtf = mysql_fetch_array($wins);
		$wins = $wtf[0] + 1;
		$query = "UPDATE team SET wins='$wins' WHERE team_name=\"$user2\";";
		$database_driver->query_database($query);
		
		$losses = $database_driver->query_database("SELECT losses FROM team WHERE team_name=\"$user1\"");
		$wtf = mysql_fetch_array($losses);
		$losses = $wtf[0] + 1;
		$query ="UPDATE team SET losses='$losses' WHERE team_name=\"$user1\";";
		$database_driver->query_database($query);
	
	}
	else{
		
		$ties = $database_driver->query_database("SELECT ties FROM team WHERE team_name=\"$user1\"");
		$wtf = mysql_fetch_array($ties);
		$ties = $wtf[0] + 1;
		$query ="UPDATE team SET ties='$ties' WHERE team_name=\"$user1\";";
		$database_driver->query_database($query);
		
		$ties = $database_driver->query_database("SELECT ties FROM team WHERE team_name=\"$user2\"");
		$wtf = mysql_fetch_array($ties);
		$ties = $wtf[0] + 1;
		$query = "UPDATE team SET ties='$ties' WHERE team_name=\"$user2\";";
		$database_driver->query_database($query);
	}
	if( $query != ""){
		$result2 = $database_driver->query_database($query);
	}
	$query = "";
}
?>