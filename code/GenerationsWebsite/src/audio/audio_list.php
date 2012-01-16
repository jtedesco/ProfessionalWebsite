<?php
/**
 * User: Jon
 * Date: Jan 14, 2011
 * Time: 2:07:43 PM
 */

/**
 * This function lists the video recordings from the database and renders them as a list of links sorted by name, artist,
 *  or recording date.
 *
 * Clicking on a particular song in the list will
 *
 * @return void
 */
function list_audio_recordings(){

    // Render links to sort by different columns
    echo "<br/><div style='float:left; width: 50%;'>Group By:</div>
          <div style='float:right; width: 50%;'>
                <a class='faux_link' id=date_link href=\"javascript:select_audio_list_sorting('date')\">date</a> |
                <a id=artist_link href=\"javascript:select_audio_list_sorting('artist')\">artist</a> |
                <a id=name_link href=\"javascript:select_audio_list_sorting('name')\">name</a>
          </div><br/><br/><br/>";

    // Render each list separately, and only show one (default is grouped by date)
    render_date_grouped_list();
    render_artist_grouped_list();
    render_name_grouped_list();

    echo "<br/>";
}

function render_date_grouped_list(){

    echo "<div id='date_list'>";

    // Create the query to the database
    $list_dates_query = "SELECT DISTINCT recording_date FROM Song, Recording, RecordingOf WHERE Song.id=song_id AND
                Recording.id=recording_id AND is_video=false ORDER BY recording_date DESC, artist, name;";

    // Get the results of the query
    $database_connection = new MySqlRootDriver();
    $dates = $database_connection->query_database($list_dates_query);


    // Loop through the dates
    $number_of_dates = mysql_numrows($dates);
    for($row = 0; $row < $number_of_dates; $row++){

        // Grab this date
        $date_and_time = (string) mysql_result($dates, $row, 'recording_date');

        // Parse the date into readable date format
        $months = array("Jan", "Feb", "March", "April", "May", "Jun", "July", "Aug", "Sep", "Oct", "Nov", "Dec");
        $parsed_date_and_time = date_parse($date_and_time);
        $month = $months[$parsed_date_and_time["month"]-1];
        $day = $parsed_date_and_time["day"];
        $year = $parsed_date_and_time["year"];
        $day_of_the_week = date("l", mktime(0, 0, 0, $parsed_date_and_time["month"], $day, $year));

        // Lookup the name of the corresponding event
        $find_matching_event_query = "SELECT name, Event.id FROM Event, Recording WHERE date_and_time='$date_and_time';";
        $event_result = $database_connection->query_database($find_matching_event_query);
        $event_id = (int) @mysql_result($event_result, 0, 'Event.id');
        if($event_id <= 0) {
            if($year = 2008){
                $event_name = "Basement Recording Session";
            }
        } else {
            $event_name = (string) mysql_result($event_result, 0, 'name');
        }

        // Render this date as a section header
        echo "<h3 style='margin-bottom: 1px;'>$day_of_the_week, $month $day, $year</h3>";
        echo "<h4 style='margin-top: 1px;'>$event_name</h4>";

        // Get all information about recording for this event
        $find_matching_recordings_query = "SELECT artist, name, resource_path, song_id FROM Song, RecordingOf, Recording
            WHERE Song.id=song_id AND Recording.id=recording_id AND is_video=false AND recording_date='$date_and_time';";
        $recordings = $database_connection->query_database($find_matching_recordings_query);
        $number_of_recordings = mysql_numrows($recordings);

        // List all video recordings for this event
        echo "<ul>";
        for($recording = 0; $recording < $number_of_recordings; $recording++){

            //Grab the artist, name, and resource path from the entry
            $artist_name = (string) mysql_result($recordings, $recording, 'artist');
            $song_name = (string) mysql_result($recordings, $recording, 'name');
            $resource_path = (string) mysql_result($recordings, $recording, 'resource_path');
            $song_id = (string) mysql_result($recordings, $recording, 'song_id');

            // Show this link
//            echo "<li><a clas='' href=\"javascript:load_audio('$resource_path')\">$song_name by $artist_name</a><li style='font-size: 8px;'>&nbsp;";
            echo "<li><a onclick=\"load_audio('$song_id')\" title='$song_name' artist='$artist_name' href=\"http://www.generationschicago.com/$resource_path\">$song_name by $artist_name</a><li style='font-size: 8px;'>&nbsp;";

        }
        echo "</ul>";
    }
    echo "</div>";
}

function render_artist_grouped_list(){

    echo "<div id='artist_list'>";

    // Create the query to the database
    $list_artists_query = "SELECT DISTINCT artist FROM Song, RecordingOf, Recording WHERE Song.id=song_id AND
                Recording.id=recording_id AND is_video=false ORDER BY artist, name, recording_date;";

    // Get the results of the query
    $database_connection = new MySqlRootDriver();
    $dates = $database_connection->query_database($list_artists_query);

    // Loop through the artists
    $number_of_artists = mysql_numrows($dates);
    for($row = 0; $row < $number_of_artists; $row++){

        // Grab this artist
        $artist = (string) mysql_result($dates, $row, 'artist');

        // Render this artist as a section header
        echo "<h3 style='margin-bottom: 1px;'>Songs by $artist</h3>";

        // Get all information about recordings for songs by this artist
        $find_matching_recordings_query = "SELECT artist, name, resource_path, song_id FROM Song, RecordingOf, Recording
            WHERE Song.id=song_id AND Recording.id=recording_id AND is_video=false AND artist='$artist';";
        $recordings = $database_connection->query_database($find_matching_recordings_query);
        $number_of_recordings = mysql_numrows($recordings);

        // List all video recordings for this artist
        echo "<ul>";
        for($recording = 0; $recording < $number_of_recordings; $recording++){

            //Grab the artist, name, and resource path from the entry
            $artist_name = (string) mysql_result($recordings, $recording, 'artist');
            $song_name = (string) mysql_result($recordings, $recording, 'name');
            $resource_path = (string) mysql_result($recordings, $recording, 'resource_path');
            $song_id = (string) mysql_result($recordings, $recording, 'song_id');

            // Show this link
            echo "<li><a onclick=\"load_audio('$song_id')\" title='$song_name' artist='$artist_name' href=\"http://www.generationschicago.com/$resource_path\">$song_name by $artist_name</a><li style='font-size: 8px;'>&nbsp;";

        }
        echo "</ul>";
    }
    echo "</div>";

}

function render_name_grouped_list(){

    echo "<div id='name_list'>";

    // Create the query to the database
    $list_songs_query = "SELECT DISTINCT name FROM Song, RecordingOf, Recording WHERE Song.id=song_id AND
                Recording.id=recording_id AND is_video=false ORDER BY name, artist, recording_date;";

    // Get the results of the query
    $database_connection = new MySqlRootDriver();
    $dates = $database_connection->query_database($list_songs_query);

    // Loop through the artists
    $number_of_songs = mysql_numrows($dates);
    for($row = 0; $row < $number_of_songs; $row++){

        // Grab this artist
        $song_name = (string) mysql_result($dates, $row, 'name');

        // Render this artist as a section header
        echo "<h3 style='margin-bottom: 1px;'>Recordings of $song_name</h3>";

        // Get all information about recordings for songs by this artist
        $find_matching_recordings_query = "SELECT artist, name, resource_path, song_id FROM Song, RecordingOf, Recording
            WHERE Song.id=song_id AND Recording.id=recording_id AND is_video=false AND name='$song_name';";
        $recordings = $database_connection->query_database($find_matching_recordings_query);
        $number_of_recordings = mysql_numrows($recordings);

        // List all video recordings for this artist
        echo "<ul>";
        for($recording = 0; $recording < $number_of_recordings; $recording++){

            //Grab the artist, name, and resource path from the entry
            $artist_name = (string) mysql_result($recordings, $recording, 'artist');
            $song_name = (string) mysql_result($recordings, $recording, 'name');
            $resource_path = (string) mysql_result($recordings, $recording, 'resource_path');
            $song_id = (string) mysql_result($recordings, $recording, 'song_id');

            // Show this link
            echo "<li><a onclick=\"load_audio('$song_id')\" title='$song_name' artist='$artist_name' href=\"http://www.generationschicago.com/$resource_path\">$song_name by $artist_name</a><li style='font-size: 8px;'>&nbsp;";

        }
        echo "</ul>";
    }
    echo "</div>";
}

