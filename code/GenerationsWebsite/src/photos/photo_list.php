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
function display_photo_navigation(){

    // Display a title for the navigation
    echo "<br/><h2>Events with Photos</h2>";

    // Render the only list (only list for now)
    render_event_grouped_list();

    echo "<br/>";
}

function render_event_grouped_list(){

    echo "<div>";

    // Create the query to the database
    $list_dates_query = "SELECT Event.id, date_and_time, name FROM Event, PhotosOf, Photo WHERE Photo.id=photo_id AND
                Event.id=event_id GROUP BY date_and_time ORDER BY date_and_time DESC;";

    // Get the results of the query
    $database_connection = new MySqlRootDriver();
    $dates = $database_connection->query_database($list_dates_query);

    // Loop through the dates
    $number_of_dates = mysql_numrows($dates);
    for($row = 0; $row < $number_of_dates; $row++){

        // Grab this date
        $date_and_time = (string) mysql_result($dates, $row, 'date_and_time');

        // Parse the date into readable date format
        $months = array("Jan", "Feb", "March", "April", "May", "Jun", "July", "Aug", "Sep", "Oct", "Nov", "Dec");
        $parsed_date_and_time = date_parse($date_and_time);
        $month = $months[$parsed_date_and_time["month"]-1];
        $day = $parsed_date_and_time["day"];
        $year = $parsed_date_and_time["year"];
        $day_of_the_week = date("l", mktime(0, 0, 0, $parsed_date_and_time["month"], $day, $year));

        // Lookup the name & id of the corresponding event
        $event_name = (string) mysql_result($dates, $row, 'name');
        $event_id = (string) mysql_result($dates, $row, 'Event.id');

        // Render this date as a section header
        echo "<a href=\"javascript:display_photos('$event_id')\">
                <h3 style='margin-bottom: 1px;'>
                    $day_of_the_week, $month $day, $year
                </h3>
                <h4 style='margin-top: 1px;'>
                    $event_name
                </h4>
              </a> <br/>";
      }
    echo "</div>";
}