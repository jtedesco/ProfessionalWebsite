<?php
/**
 * Created By: Tedesco
 * Date: jan 16, 2011
 */

function list_events(){

    //If we're supposed to grab the data from the session, grab it, otherwise, build it
    if(isset($_SESSION['render_data_from_session'])){
        unset($_SESSION['render_data_from_session']);

        //Grab the session object holding the render data
        $sort_column = $_SESSION['render_data_sort_column'];

        //Erase the session data
        unset($_SESSION['render_data_sort_column']);

        //Render the table!
        render_events($sort_column);

    } else {

        //Build the data rendering data
        $sort_column = "date_and_time DESC";

        //Render the data
        render_events($sort_column);
    }
}

function render_events($sort_column){

     //Build a hidden form to submit when we click one of the
     echo "<form name=\"hiddenform\" action=\"src/utils/sortable_table_redirect.php\" method=\"post\" style=\"display: none;\">
            <input name=\"sortcolumn\" type=\"text\" id=\"sortcolumn\" style=\"display: none;\"></form>";

     //Store the render data in the session
     $_SESSION['target_page'] = "events.php";

     //Form the query to pull all location data and the table data
     $query = "SELECT Event.id, date_and_time, facebook_url, Event.name, Location.name, Location.id, address, homepage FROM Location, Event
                WHERE Location.id = Event.location_id ORDER BY ".$sort_column;
     $column_names = array("date_and_time", "Event.name");
     $column_titles = array("Date", "Name");

     //Build the 'order by' header
     echo "<br/><div style='width:100%; height:30px;'><div style='float:right; padding-bottom: 20px;'>Order By:
           <span style='padding-left: 20px; text-align: right; width: 50%;'>";
     for($column_num = 0; $column_num < count($column_titles); $column_num++){

         // Hide the vertical divider
         if($column_num==0){
             $rendered_divider = "";
         } else {
             $rendered_divider = "<span style='padding:5px;'>|</span>";
         }

         $name = $column_names[$column_num];
         $title = $column_titles[$column_num];

          // If the this column is not the column currently sorted by
         if(strstr($sort_column, $name)==false){

             echo $rendered_divider."<a href=\"javascript:submit_hidden_form('$name ASC')\"/>$title</a>";

          } else { // If this is the column currently sorted by

             if(strstr($sort_column, 'DESC')==false){

                  echo $rendered_divider."<a href=\"javascript:submit_hidden_form('$name DESC')\"/>$title</a><span
                                            style='font-size: 14px; padding-bottom: 5px;'>&nbsp;&#9652</span>";
              } else {

                  echo $rendered_divider."<a href=\"javascript:submit_hidden_form('$name ASC')\"/>$title</a><span
                                            style='font-size: 14px; padding-bottom: 5px;'>&nbsp;&#9662</span>";
              }
         }
     }
     echo "</span></div></div>";

     // Get a connection to the database and run the query
     $database_connection = new MySqlRootDriver();
     $event_data = $database_connection->query_database($query);

     //Get the number of rows in the data
     $rows = mysql_numrows($event_data);

    //Prepare parsing information
     $months = array("January", "February", "March", "April", "May", "June", "July", "August", "September",
         "October", "November", "December");

     // Find the number of upcoming gigs
     $upcoming_gigs = find_upcoming_gigs($event_data, $rows, $months);
     $display_upcoming_gigs = !strcmp("date_and_time DESC", $sort_column);

     //Loop through all the results
     for($row = 0; $row < $rows; $row++){

         // Display 'upcoming gigs' header if there are some
         if($display_upcoming_gigs && $upcoming_gigs>0 && $row==0) {
             echo "<h1 style='font-size: 23px; color: #76c6fe;'>Upcoming Gigs</h1>";
         } else if($display_upcoming_gigs && $upcoming_gigs>0 && $row==$upcoming_gigs) {
             echo "<h1 style='font-size: 23px; color: #76c6fe;'>Past Gigs</h1>";
         }

         // Display the name of the location
         $event_name = mysql_result($event_data, $row, 'Event.name');
         $date_and_time = (string) mysql_result($event_data, $row, 'date_and_time');
         $location_name = mysql_result($event_data, $row, 'Location.name');
         $event_id = mysql_result($event_data, $row, 'Event.id');
         $location_id = mysql_result($event_data, $row, 'Location.id');
         echo "<div style='width:1000px;'><h1 style='margin-left: 5px; font-size: 17px;' id=$event_id>$event_name</h1>";

             // List the event information
             echo "<div style='width:55%; float:left; height: 150px;'><div style='padding-left:20px; padding-bottom:40px; font-size:12px;'>";
    
                 // Parse the date
                 $parsed_date_and_time = date_parse($date_and_time);
                 $month = $months[$parsed_date_and_time["month"]-1];
                 $day = $parsed_date_and_time["day"];
                 $year = $parsed_date_and_time["year"];
                 $day_of_the_week = date("l", mktime(0, 0, 0, $parsed_date_and_time["month"], $day, $year));

                 // Display it
                 echo "<h3 style='padding-left: 10px;'>$day_of_the_week, $month $day, $year<br/>&nbsp;&nbsp;at $location_name</h3>";

                 //Display the facebook event
                 $facebook_url = mysql_result($event_data, $row, 'facebook_url');
                 if($facebook_url != null) {
                     echo "<a style='padding-left: 10px;' href='$facebook_url'>Facebook Event</a><br/>";
                 }

                 //Display the homepage
                 $homepage = mysql_result($event_data, $row, 'homepage');
                 echo "<a style='padding-left: 10px;' href='$homepage'>$location_name Homepage</a>";
                 echo "</div></div>";

             // Display the address
             echo "<div style='padding-left:5px; width:43%; height: 120px; float:right; padding-bottom:40px; font-size:12px;'>
                       <h3 style='margin-bottom: 5px;'>Street Address</h3>";
             $street_address = mysql_result($event_data, $row, 'address');
             echo "<div style='padding-left: 10px;'>$street_address
                    <br><br>
                    <a href='locations.php#$location_id'>Location Page</a>
             </div>";
             echo "</div><hr/>";

         // End this Location
         echo "</div><br/><br/>";

     }
}

function find_upcoming_gigs($event_data, $rows, $months){

    //Loop through all the results
    $upcoming_gigs = 0;
     for($row = 0; $row < $rows; $row++){

         // Display the name of the location
         $date_and_time = (string) mysql_result($event_data, $row, 'date_and_time');

         // Parse the date
         $parsed_date_and_time = date_parse($date_and_time);
         $month = $months[$parsed_date_and_time["month"]-1];
         $day = $parsed_date_and_time["day"];
         $year = $parsed_date_and_time["year"];
         $day_of_the_week = date("l", mktime(0, 0, 0, $parsed_date_and_time["month"], $day, $year));

        # Get the current date & time
        $current_date_and_time = getdate(time());
        $skip = false;

        # Check if any of these gigs are future
        if($parsed_date_and_time["year"]<$current_date_and_time["year"]){
            $skip = true;
        } else if ($parsed_date_and_time["year"]==$current_date_and_time["year"] && $parsed_date_and_time["month"]<$current_date_and_time["mon"]){
            $skip = true;
        } else if ($parsed_date_and_time["year"]==$current_date_and_time["year"] && $parsed_date_and_time["month"]==$current_date_and_time["mon"]
                && $parsed_date_and_time["day"]<$current_date_and_time["mday"]){
            $skip = true;
        }

        if(!$skip) {
            $upcoming_gigs++;
        }
     }

    return $upcoming_gigs;

}

?>