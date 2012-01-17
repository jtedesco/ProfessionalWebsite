<?php session_start(); ?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

    <!-- Title-->
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />

        <title>SportSQL | By Jon Tedesco, Joe Gonzalez, Aditya Pandyaram, and Kiran Ryali</title>

        <link href="style/default.css" rel="stylesheet" type="text/css" media="screen" />
        <link href="style/header.css" rel="stylesheet" type="text/css" media="screen" />
        <link href="style/footer.css" rel="stylesheet" type="text/css" media="screen" />
        <link href="style/tables.css" rel="stylesheet" type="text/css" media="screen" />

        <?php require_once ("src/databaseutils/MySqlRootDriver.php"); ?>
        <?php require_once ("src/databaseutils/StaticDataRenderer.php"); ?>

    </head>
    <!-- End Title -->

    <body class="main-body">

        <div id="menu-header">
            <!-- Main Menu -->
                <?php include("include/menu.php"); ?>
            <!-- End Main Menu-->
        </div>

        <div id="wrapper">

            <!-- start page -->
            <div id="page">
                <!-- Start Main Content-->
                <div style="width: 960px;" id="content">
                    <?php
                        //Grab an instance of the database
                        $database_driver = new MySqlRootDriver();

                        //Form the query and query the database
                        $query = "SELECT * FROM matchups;";
                        $result = $database_driver->query_database($query);

                        //Initialize the renderer
                        $renderer = new StaticDataRenderer();

                        //Create the array of column names, column labels, and style class of the table
                        $column_names = array("player1","player2", "winner");
                        $column_labels = array("Team 1","Team 2", "Winner");
                        $table_class = "stats";

                        //Render the table!
                        $renderer->renderResultsAsStyledTable($result, $column_names, $column_labels, $table_class);

                    ?>
                </div>
                <!-- End Main Content-->

             <div style="clear: both;">&nbsp;</div>
            </div>
            <!-- end page -->
        </div>
            <?php include("include/footer.php"); ?>
    </body>
</html>