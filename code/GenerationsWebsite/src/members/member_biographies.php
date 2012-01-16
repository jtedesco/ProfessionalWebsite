<?php
/**
 * Created By: Tedesco
 * Date: Dec 21, 2010
 */
require_once("src/utils/mysql_root_driver.php");

/**
 * Builds the biographies of all band members
 */
function build_member_biographies(){

    # Get a connection to the database
    $database_connection = new MySqlRootDriver();

    # Build the query we're going to run
    $query = "SELECT * FROM Member ORDER BY name DESC;";

    # Execute the query and get the results
    $results = $database_connection->query_database($query);

    # For each member from the database, render the photo and biography
    $number_of_members = mysql_num_rows($results);
    echo "<div style='width:48%; float:left;'>";
    for($row = 0; $row < $number_of_members; $row=$row+2){

        # Get the member name & instruments
        $name = (string) mysql_result($results, $row, 'name');
        $instruments = (string) mysql_result($results, $row, 'instruments');

        # Show the member biography
        $biography = mysql_result($results, $row, 'bio');

        # Get the member photo
        $photo_id = mysql_result($results, $row, 'photo_id');
        $next_query = "SELECT image_path FROM Photo, Member WHERE Photo.id=$photo_id;";
        $photo_data = $database_connection->query_database($next_query);
        $photo_path = (string) mysql_result($photo_data, 0, 'image_path');

        # Render member data (float left for even entries, float right for others
        echo "<div id=CollapsiblePanel$row class='CollapsiblePanel'>
                <div class='CollapsiblePanelTab'>
                    <a><h1 style='margin:0px;'>$name</h1></a>
                    <h2>$instruments</h2>
                </div>
                <div class='CollapsiblePanelContent'>
                    <img width='120' class='bordered' src='$photo_path'/>
                    <p>$biography</p>
                </div>
               </div>
               <br/><br/>";
    }

    echo "</div>
          <div style='width:48%; float:right;'>";

    for($row = 1; $row < $number_of_members; $row=$row+2){

        # Get the member name & instruments
        $name = (string) mysql_result($results, $row, 'name');
        $instruments = (string) mysql_result($results, $row, 'instruments');

        # Show the member biography
        $biography = mysql_result($results, $row, 'bio');

        # Get the member photo
        $photo_id = mysql_result($results, $row, 'photo_id');
        $next_query = "SELECT image_path FROM Photo, Member WHERE Photo.id=$photo_id;";
        $photo_data = $database_connection->query_database($next_query);
        $photo_path = (string) mysql_result($photo_data, 0, 'image_path');

        # Render member data (float left for even entries, float right for others
        echo "<div style='width: 100%; font-size:110%;'
                id=CollapsiblePanel$row class='CollapsiblePanel'>
                <div class='CollapsiblePanelTab'>
                    <a><h1 style='margin:0px;'>$name</h1></a>
                    <h2>$instruments</h2>
                </div>
                <div class='CollapsiblePanelContent'>
                    <img width='120' class='bordered' src='$photo_path'/>
                    <p>$biography</p>
                </div>
               </div>
               <br/><br/>";
    }
    echo "</div>";

    # Return the number of panels to render
    return $number_of_members;
}
?>