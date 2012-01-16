<?php
/**
 * Created By: Tedesco
 * Date: Dec 15, 2010
 */

require_once("src/utils/mysql_root_driver.php");


function list_upcoming_gigs($number_to_list){

    # Get a connection to the database
    $database_connection = new MySqlRootDriver();

    # Build a query to get the top 5 most recent events
    $query = "SELECT date_and_time, Event.name, Location.name, Location.id FROM Event, Location
                WHERE Event.location_id=Location.id AND date_and_time > date(now()) ORDER BY date_and_time ASC LIMIT ".$number_to_list.";";

    # Execute this query and get the results
    $results = $database_connection->query_database($query);

    # Find out the number of events
    $number_of_events = mysql_numrows($results);

    # Create an array storing the month abbreviations
    $months = array("Jan", "Feb", "Mar", "Apr", "May", "Jun", "July", "Aug", "Sep", "Oct", "Nov", "Dec");

    # Keep a count of how many we've output
    $upcoming_gigs = 0;

    # For each event from the database...
    for($row = 0; $row < $number_of_events; $row++){

        # Get the current date & time
        $current_date_and_time = getdate(time());
        $skip = false;

        # Parse the data from the database
        $name = mysql_result($results, $row, 'Event.name');
        $date_and_time = (string) mysql_result($results, $row, 'date_and_time');
        $location_name = (string) mysql_result($results, $row, 'Location.name');
        $location_id = (string) mysql_result($results, $row, 'Location.id');

        # Parse the date
        $parsed_date_and_time = date_parse($date_and_time);
        $month = $months[$parsed_date_and_time["month"]-1];
        $day = $parsed_date_and_time["day"];

        # Check if any of these gigs are future
        if($parsed_date_and_time["year"]<$current_date_and_time["year"]){
            $skip = true;
        } else if ($parsed_date_and_time["year"]==$current_date_and_time["year"] && $parsed_date_and_time["month"]<$current_date_and_time["mon"]){
            $skip = true;
        } else if ($parsed_date_and_time["year"]==$current_date_and_time["year"] && $parsed_date_and_time["month"]==$current_date_and_time["mon"]
                && $parsed_date_and_time["day"]<$current_date_and_time["mday"]){
            $skip = true;
        }

        # Shorten the name if it's too long
        if(strlen($name)>30){
            $name = substr($name, 0, 30);
            $name = $name."...";
        }

        if(!$skip){
            $upcoming_gigs = $upcoming_gigs+1;

            # Display the opening tags if this is the first item
            if($row==0){
                echo "
                    <br/><h2>Upcoming Gigs</h2>
                        <ul class='list1'>
                    ";
            }

            echo "<li style='margin-left:-10px;'><strong>$month $day</strong> <span><a href='locations.php#$location_id'>$location_name</a> <small>$name</small></span></li>";
        }
    }

    # Output a default message if there aren't any gigs to output
    if($upcoming_gigs==0){
        echo "<br/><br/>";
    }
    
    # Output the closing tags
    echo "</ul>";
}