<head>
<?php require_once ("src/databaseutils/MySqlRootDriver.php"); ?>
        <?php require_once ("src/databaseutils/StaticDataRenderer.php"); ?>
</head>
<?php

$position = $_POST["position"];

$host = 'localhost';
$user = 'root';
$dbpass = '';

$link = mysql_connect($host, $user, $dbpass) or die ('Could not connect: ' . mysql_error());

mysql_select_db('sportsql') or die ('Could not select database');
echo $position;
$query = "SELECT * FROM player WHERE position='$position'";
echo $query;
            //Grab an instance of the database
            $database_driver = new MySqlRootDriver();             		//Form the query and query the database
		$result = $database_driver->query_database($query);
            //$rows = mysql_num_rows($result);
            //echo $result;
            //Initialize the renderer
            $renderer = new StaticDataRenderer();

            //Create the array of column names, column labels, and style class of the table
            $column_names = array("position","real_team", "name");
            $column_labels = array("Position", "Real Team", "Name");
            $table_class = "stats";

            //Render the table!
            $renderer->renderResultsAsStyledTable($result, $column_names, $column_labels, $table_class);

?>
