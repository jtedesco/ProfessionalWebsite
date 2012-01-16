<?php

    // Render the descriptions of all recorded songs in the database, and identify them by their recording id's in div's
    echo "<div id='default_header'> <h2>audio recordings</h2>
          <p>Please select a song from the sidebar</p></div>";

    // Grab the path (without the file extension) of the video to play
    $event_id = $_SESSION['event_id'];

    // Get a connection to the database
    $database_connection = new MySqlRootDriver();

    // Form the query to find out everything about the photos and event
    $query = "SELECT name, date_and_time, image_path, facebook_url, caption FROM Event, PhotosOf, Photo
                WHERE Photo.id=photo_id AND Event.id=event_id ORDER BY rand();";
    $photos_data = $database_connection->query_database($query);

    // Extract all data about the event
    $event_name = (string) mysql_result($photos_data, 0, 'name');
    $facebook_url = (string) mysql_result($photos_data, 0, 'facebook_url');
    $date_and_time = (string) mysql_result($photos_data, 0, 'date_and_time');

    // Parse the date into readable date format
    $months = array("January", "February", "March", "April", "May", "June", "July", "August", "September", "October",
                "November", "December");
    $parsed_date_and_time = date_parse($date_and_time);
    $month = $months[$parsed_date_and_time["month"]-1];
    $day = $parsed_date_and_time["day"];
    $year = $parsed_date_and_time["year"];
    $day_of_the_week = date("l", mktime(0, 0, 0, $parsed_date_and_time["month"], $day, $year));

    // Prepare data
    $query = "SELECT artist, name, description, song_id FROM Recording, RecordingOf, Song WHERE Song.id=RecordingOf.song_id
                AND Recording.id=RecordingOf.recording_id AND is_video=false ORDER BY rand();";
    $audio_data = $database_connection->query_database($query);
    $number_of_songs = mysql_num_rows($audio_data);

    // Open div tag to identify song data
    echo "<div id='song_data'>";

    // Render all song data inside of spans, where each span's id is the song id
    for($row = 0; $row < $number_of_songs; $row++){

        // Display this song's data
        $artist = (string) mysql_result($audio_data, $row, 'artist');
        $song_name = (string) mysql_result($audio_data, $row, 'name');
        $description = (string) mysql_result($audio_data, $row, 'description');
        $song_id = (int) mysql_result($audio_data, $row, 'song_id');

        // Render the song data
?>
        <div style='display: none;' id='<? echo $song_id; ?>' name='<? echo $song_id; ?>'>
            <h3><?echo $song_name;?> by <?echo $artist;?></h3>
            <p><?echo $description;?></p>
        </div>
<?

    }

    echo "</div>";
?>