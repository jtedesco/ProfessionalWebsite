<?php


$host = 'localhost';
$user = 'root';
$dbpass = '';

$link = mysql_connect($host, $user, $dbpass) or die ('Could not connect: ' . mysql_error());

mysql_select_db('sportsql') or die ('Could not select database');

//CHANGE THIS FOR EACH TEAM
//$url = "http://www.nfl.com/teams/carolinapanthers/roster?team=CAR";
//$real_team = "Panthers";
$url_array = array(
"http://www.nfl.com/teams/arizonacardinals/roster?team=ARI",
"http://www.nfl.com/teams/atlantafalcons/roster?team=ATL",
"http://www.nfl.com/teams/baltimoreravens/roster?team=BAL",
"http://www.nfl.com/teams/buffalobills/roster?team=BUF",
"http://www.nfl.com/teams/carolinapanthers/roster?team=CAR",
"http://www.nfl.com/teams/chicagobears/roster?team=CHI",
"http://www.nfl.com/teams/cincinnatibengals/roster?team=CIN",
"http://www.nfl.com/teams/clevelandbrowns/roster?team=CLE",
"http://www.nfl.com/teams/dallascowboys/roster?team=DAL",
"http://www.nfl.com/teams/denverbroncos/roster?team=DEN",
"http://www.nfl.com/teams/detroitlions/roster?team=DET",
"http://www.nfl.com/teams/greenbaypackers/roster?team=GB",
"http://www.nfl.com/teams/houstontexans/roster?team=HOU",
"http://www.nfl.com/teams/indianapoliscolts/roster?team=IND",
"http://www.nfl.com/teams/jacksonvillejaguars/roster?team=JAC",
"http://www.nfl.com/teams/kansascitychiefs/roster?team=KC",
"http://www.nfl.com/teams/miamidolphins/roster?team=MIA",
"http://www.nfl.com/teams/minnesotavikings/roster?team=MIN",
"http://www.nfl.com/teams/newenglandpatriots/roster?team=NE",
"http://www.nfl.com/teams/neworleanssaints/roster?team=NO",
"http://www.nfl.com/teams/newyorkgiants/roster?team=NYG",
"http://www.nfl.com/teams/newyorkjets/roster?team=NYJ",
"http://www.nfl.com/teams/oaklandraiders/roster?team=OAK",
"http://www.nfl.com/teams/philadelphiaeagles/roster?team=PHI",
"http://www.nfl.com/teams/pittsburghsteelers/roster?team=PIT",
"http://www.nfl.com/teams/sandiegochargers/roster?team=SD",
"http://www.nfl.com/teams/sanfrancisco49ers/roster?team=SF",
"http://www.nfl.com/teams/seattleseahawks/roster?team=SEA",
"http://www.nfl.com/teams/st.louisrams/roster?team=STL",
"http://www.nfl.com/teams/tampabaybuccaneers/roster?team=TB",
"http://www.nfl.com/teams/tennesseetitans/roster?team=TEN",
"http://www.nfl.com/teams/washingtonredskins/roster?team=WAS");

$real_team_array = array("Cardinals", "Falcons", "Ravens", "Bills", "Panthers", "Bears", "Bengals", "Browns", "Cowboys", "Broncos", "Lions", "Packers", "Texans", "Colts",
"Jaguars", "Chiefs", "Dolphins", "Vikings", "Patriots", "Saints", "Giants", "Jets",
"Raiders", "Eagles", "Steelers", "Chargers", "49ers", "Hawks", "Rams", "Buccaneers",
"Titans", "Redskins");

for($teamNo = 0; $teamNo < count($url_array); $teamNo++){

    set_time_limit(300); //So that the php doesn't time out

    $url = $url_array[$teamNo];
    $real_team = $real_team_array[$teamNo];

    $raw = file_get_contents($url);

    $newlines = array("\t","\n","\r","\x20\x20","\0","\x0B");

    $content = str_replace($newlines, "", html_entity_decode($raw));

    $start = strpos($content,'<table cellpadding="0" class="data-table1" style="width:100%" cellspacing="0" id="result">');
    
    $end = strpos($content,'</table>',$start) + 8;

    $table = substr($content,$start,$end-$start);

    preg_match_all("|<tr(.*)</tr>|U",$table,$rows);

    foreach ($rows[0] as $row){

        if ((strpos($row,'<th')===false)){

            preg_match_all("|<td(.*)</td>|U",$row,$cells);
            //echo $cells[0][1];
            $player_url_start = strpos($cells[0][1],'href="') + 6; //href="..." where ... is what we want
            $player_url_stop = strpos($cells[0][1],'"',$player_url_start);
            $player_url = "http://www.nfl.com".substr($cells[0][1],$player_url_start,$player_url_stop-$player_url_start);

            $name = strip_tags($cells[0][1]);

            $position = strip_tags($cells[0][2]);
        if($position=="WR" || $position=="RB" || $position=="QB" || $position=="TE" || $position=="K"){

            $query = "INSERT INTO player (position, real_team,name,url)
            VALUES ('$position','$real_team','$name','$player_url')";

            mysql_query($query);
        }

        }

    }
    $query = "INSERT INTO player (position, real_team, name) VALUES('D','$real_team','$real_team Defense')";
    mysql_query($query);

}
mysql_close();

echo "<h1>Success!</h1>";

?>

