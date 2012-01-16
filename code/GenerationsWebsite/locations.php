<?php @session_start();
    /**
     * Created By: Jon Tedesco
     * Date: Dec 10, 2010
     */

    # Include PHP scripts
    require_once("include/header/meta_header.php");
    require_once("include/menu.php");
    require_once("include/javascript/collapsible_panel_scripts.php");
    require_once("src/utils/mysql_root_driver.php");
    require_once("src/locations/list_locations.php");


?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<?
    # Meta data, including the title
    meta_header('Locations');

    # CSS and javascript
    include('include/header/common_css.php');
    include('include/header/common_javascript.php');
    include('include/header/calendar_css.php');
    include("include/javascript/submit_form.php");
    include("include/header/collapsible_panel.php");
?>
</head>


<body class="single">

<?
    # Google analytics
    include('include/header/google_analytics.php');
?>

<div id="wrapper">

    <?
        # Build the menu, giving it the current page to highlight
        build_menu('locations');
    ?>

	<div id="page" class="container">
		<div id="content">


            <div class="box3">
            <?
                # List the locations (sortable list)
                list_locations();
            ?>

            </div>
            
		</div>
	</div>

    <?
        # The footer
    include('include/footer.php');
    ?>

</div>

<?
    // Find the number of locations
    $query = "SELECT * FROM Location GROUP BY name;";
    $database_connection = new MySqlRootDriver();
    $results = $database_connection->query_database($query);
    $number_of_locations = mysql_num_rows($results);

    # Render collapsible panel scripts
    build_collapsible_panel_script($number_of_locations);
?>

</body>
</html>