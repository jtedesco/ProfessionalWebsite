<?php
require_once("src/utils/mysql_root_driver.php");

/**
 * 
 *
 * @return void
 */
function build_gallery() {

    # Build generic container for gallery
    echo "<div id='gallery' class='content'>
                <div id='controls' class='controls'></div>
                <div class='slideshow-container'>
                    <div id='loading' class='loader'></div>
                    <div id='slideshow' class='slideshow'></div>
                </div>
                <div id='caption' class='caption-container'></div>
            </div>
            <div id='thumbs' class='navigation'>
         ";

    # Start the list of actual pictures
    echo "<ul class='thumbs noscript'>";

    # Grab the photo and related info from the database
    $query = "SELECT image_path, caption, date_and_time, facebook_url, Event.name, homepage, Location.name FROM Event, Location,
                Photo, PhotosOf WHERE Event.id=PhotosOf.event_id AND PhotosOf.photo_id=Photo.id AND Event.location_id=Location.id;";
    $database_connection = new MySqlRootDriver();
    $results = $database_connection->query_database($query);

    # Get the number of pictures we are going to put in here
    $number_of_pictures = mysql_numrows($results);

    # Static info for parsing data
    $months = array("January", "February", "March", "April", "May", "June", "July", "August", "September", "October",
                  "November", "December");

    # Render the list of pictures, including image paths and photo descriptions
    #  (Iterate through all images passed in)
    for($row = 0; $row < $number_of_pictures; $row++){

        # Pull info from the database results
        $event_name = mysql_result($results, $row, 'Event.name');
        $location_name = mysql_result($results, $row, 'Location.name');
        $image_path = mysql_result($results, $row, 'image_path');
        $caption = mysql_result($results, $row, 'caption');
        $facebook_url = mysql_result($results, $row, 'facebook_url');
        $homepage = mysql_result($results, $row, 'homepage');
        $date_and_time = (string) mysql_result($results, $row, 'date_and_time');

        # Parse the date
        $parsed_date_and_time = date_parse($date_and_time);
        $month = $months[$parsed_date_and_time["month"]-1];
        $day = $parsed_date_and_time["day"];

        # Find the thumbnail of this image
        $thumbnail_image_path = $image_path;

        # Render this entry for the gallery
        echo "
                <li>
                    <a class='thumb' href='$image_path' title='$event_name'>
                        <img width=150 src='$thumbnail_image_path' alt='$event_name' />
                    </a>
                    <div class='caption'>
                        <p>$caption</p>
                        <div class='image-title'>$event_name</div>
                        <div class='image-desc'>
                            $month $day, at <a href='$homepage'>$location_name</a>
                            <a href='$facebook_url'>Facebook Event</a>
                        </div>
                        <div class='download'>
                            <a href='$image_path'>Download Original</a>
                        </div>
                    </div>
                </li>
        ";

    }
    
    # End the list of actual pictures
    echo "</ul></div>";
}
?>