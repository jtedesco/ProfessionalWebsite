<?php

    //Build a hidden form to submit when we click one of the
    echo "<form name=\"hiddenform\" action=\"src/photos/load_photos_redirect.php\" method=\"post\" style=\"display: none;\"><input name=\"event_id\" type=\"text\" id=\"event_id\" style=\"display: none;\"></form>";

    if(isset($_SESSION['load_photos_from_session'])){
        unset($_SESSION['load_photos_from_session']);


        // Grab the path (without the file extension) of the video to play
        $event_id = $_SESSION['event_id'];

        // Get a connection to the database
        $database_connection = new MySqlRootDriver();

        // Form the query to find out everything about the photos and event
        $query = "SELECT name, date_and_time, image_path, facebook_url, caption FROM Event, PhotosOf, Photo
                    WHERE Photo.id=photo_id AND Event.id=$event_id AND event_id=Event.id ORDER BY rand();";
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

        echo "<h1 style='margin-bottom: 5px;'>$event_name</h1>
              <h3>$day_of_the_week, $month $day, $year</h3>
              <br/>";

        // Begin rendering the photo gallery
        echo "<div id=\"galleria\">";
        $number_of_photos = mysql_num_rows($photos_data);
        for($photo = 0; $photo < $number_of_photos; $photo++){

            // Grab all data about this particular photo out of the database results
            $caption = (string) mysql_result($photos_data, $photo, 'caption');
            $image_path = (string) mysql_result($photos_data, $photo, 'image_path');

            // Find the path to the corresponding thumbnails
            $last_slash_index = strrpos($image_path, "/");
            $thumbnail_image_path = substr_replace($image_path, "/thumbnails/", $last_slash_index, 1);

            // Render this portion of the photo gallery
            if(strlen($caption)>0){
                echo "<a href=\"$image_path\">
                        <img alt=\"$caption\" src=\"$thumbnail_image_path\">
                      </a>";
            } else {
                echo "<a href=\"$image_path\">
                        <img src=\"$thumbnail_image_path\">
                      </a>";
            }
        }
        echo "</div>";

    } else {
        echo "<h2>photo albums</h2>
              <p>Please select an event from the sidebar</p>";

        // Grab the path (without the file extension) of the video to play
        $event_id = $_SESSION['event_id'];

        // Get a connection to the database
        $database_connection = new MySqlRootDriver();
 
        // Form the query to find out everything about the photos and event
        $query = "SELECT name, date_and_time, image_path, facebook_url, caption FROM Event, PhotosOf, Photo
                    WHERE Photo.id=photo_id AND event_id=Event.id ORDER BY rand();";
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

        // Begin rendering the photo gallery
        echo "<div id=\"galleria\">";
        $number_of_photos = mysql_num_rows($photos_data);
        for($photo = 0; $photo < $number_of_photos; $photo++){

            // Grab all data about this particular photo out of the database results
            $caption = (string) mysql_result($photos_data, $photo, 'caption');
            $image_path = (string) mysql_result($photos_data, $photo, 'image_path');

            // Rewrite to accomodate spaces
            $image_path = str_replace(" ", "%20", $image_path);

            // Find the path to the corresponding thumbnails
            $last_slash_index = strrpos($image_path, "/");
            $thumbnail_image_path = substr_replace($image_path, "/thumbnails/", $last_slash_index, 1);

            // Render this portion of the photo gallery
            if(strlen($caption)>0){
                echo "<a href=\"$image_path\">
                        <img alt=\"$caption\" src=\"$thumbnail_image_path\">
                      </a>";
            } else {
                echo "<a href=\"$image_path\">
                        <img alt=\"\" src=\"$thumbnail_image_path\">
                      </a>";
            }
         }
        echo "</div>";
    }
?>
