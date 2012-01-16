<?php
/**
 * Created By: Tedesco
 * Date: jan 16, 2011
 */

function list_locations(){

    //If we're supposed to grab the data from the session, grab it, otherwise, build it
    if(isset($_SESSION['render_data_from_session'])){
        unset($_SESSION['render_data_from_session']);

        //Grab the session object holding the render data
        $sort_column = $_SESSION['render_data_sort_column'];

        //Erase the session data
        unset($_SESSION['render_data_sort_column']);

        //Render the table!
        render_locations($sort_column);

    } else {

        //Build the data rendering data
        $sort_column = "Location.name ASC";

        //Render the data
        render_locations($sort_column);
    }
}

function render_locations($sort_column){

     //Build a hidden form to submit when we click one of the
     echo "<form name=\"hiddenform\" action=\"src/utils/sortable_table_redirect.php\" method=\"post\" style=\"display: none;\">
            <input name=\"sortcolumn\" type=\"text\" id=\"sortcolumn\" style=\"display: none;\"></form>";

     //Store the render data in the session
     $_SESSION['target_page'] = "locations.php";

     //Form the query to pull all location data and the table data
     $query = "SELECT date_and_time, Location.id, Location.name, address, google_maps_url, homepage FROM Location, Event
                WHERE Location.id = Event.location_id GROUP BY Location.id ORDER BY ".$sort_column;
     $column_names = array("date_and_time", "Location.name");
     $column_titles = array("Date", "Location");

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
     $location_data = $database_connection->query_database($query);

     //Get the number of rows in the data
     $rows = mysql_numrows($location_data);

     //Loop through all the results
     for($row = 0; $row < $rows; $row++){

         // Display the name of the location
         $location_name = mysql_result($location_data, $row, 'Location.name');
         $location_id = mysql_result($location_data, $row, 'Location.id');
         echo "<div style='width:100%;'><h1 id=$location_id>$location_name</h1>";

         // For this location, get the dates most recently played
         $query = "SELECT date_and_time FROM Location, Event WHERE Location.id = Event.location_id
                    AND Location.name='$location_name' ORDER BY date_and_time DESC;";
         $dates = $database_connection->query_database($query);

         //Prepare parsing information
         $months = array("January", "February", "March", "April", "May", "June", "July", "August", "September",
             "October", "November", "December");
         $number_of_dates = mysql_num_rows($dates);

         // List the dates information
         echo "<div style='width:55%; float:left;'><div style='padding-left:20px; padding-bottom:40px; font-size:12px;'>
                   <h3 style='margin-bottom: 5px;'>Dates Played</h3>";
         for($date = 0; $date < $number_of_dates; $date++){

             // Parse the date
             $date_and_time = (string) mysql_result($dates, $date, 'date_and_time');
             $parsed_date_and_time = date_parse($date_and_time);
             $month = $months[$parsed_date_and_time["month"]-1];
             $day = $parsed_date_and_time["day"];
             $year = $parsed_date_and_time["year"];
             $day_of_the_week = date("l", mktime(0, 0, 0, $parsed_date_and_time["month"], $day, $year));

             // Display it
             echo "<a style='padding-left: 20px;' href='events.php'>$day_of_the_week, $month $day, $year</a><br/>";

         }

         //Display the homepage
         $homepage = mysql_result($location_data, $row, 'homepage');
         echo "<h3 style='margin-bottom: 5px; margin-top: 15px;'>Homepage</h3>
                 <a style='padding-left: 20px;' href='$homepage'>$location_name Homepage</a>";
         echo "</div></div>";

         // Display the address
         echo "<div style='padding-left:20px; width:40%; height: 70px; float:right; padding-bottom:40px; font-size:12px;'>
                   <h3 style='margin-bottom: 5px;'>Street Address</h3>";
         $street_address = mysql_result($location_data, $row, 'address');
         echo "<div style='padding-left: 20px;'>$street_address</div>";
         echo "</div>";

         // Include the google map
         $google_maps_url = mysql_result($location_data, $row, 'google_maps_url');
         echo "<div id=CollapsiblePanel$row class='CollapsiblePanel'>
                    <div class='CollapsiblePanelTab' style='width: 600px;'><h3 style='width:600px; text-align:right; cursor: pointer'>Show Map</h3></div>
                    <div class='CollapsiblePanelContent' style='width: 800px; padding: 0px; border: none; padding-left:25px;'>
                        <center><iframe allowtransparency='true' scrolling='no' height=350 width=800 src='$google_maps_url'></iframe></center>
                    </div>
               </div>";

         // End this Location
         echo "</div><br/><br/>";

     }
}

?>