<?php session_start(); ?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

    <!-- Title-->
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />

        <title>SportSQL | By Jon Tedesco, Joe Gonzalez, Aditya Pandyaram, and Kiran Ryali</title>

        <link href="../../style/default.css" rel="stylesheet" type="text/css" media="screen" />
        <link href="../../style/header.css" rel="stylesheet" type="text/css" media="screen" />
        <link href="../../style/footer.css" rel="stylesheet" type="text/css" media="screen" />
        <link href="../../style/tables.css" rel="stylesheet" type="text/css" media="screen" />

        <?php require_once ("../../src/databaseutils/MySqlRootDriver.php"); ?>
        <?php require_once ("../../src/databaseutils/DataRendererData.php"); ?>
        <?php require_once ("../../src/databaseutils/SortableDataRenderer.php"); ?>
        <?php include("../../include/submit_form_with_link.php")?>

    </head>
    <!-- End Title -->

    <body class="main-body">

        <div id="menu-header">
            <!-- Main Menu -->
                <?php include("../../include/menu.php"); ?>
            <!-- End Main Menu-->
        </div>

        <div id="wrapper">

            <!-- start page -->
            <div id="page">
                <!-- Start Main Content-->
                <div style="width: 960px;" id="content">
				<h1>Matchups</h1>
                    <?php

                        //Grab an instance of the database
                        $database_connection = new MySqlRootDriver();

                        //The page this page to reload to
                        $target_page = "index.php";

                        //If we're supposed to grab the data from the session, grab it, otherwise, build it
                        if(isset($_SESSION['render_data_from_session'])){
                            unset($_SESSION['render_data_from_session']);


                            //Grab the session object holding the render data
                            $table_name = $_SESSION['render_data_table_name'];
                            $sort_column = $_SESSION['render_data_sort_column'];
                            $column_names = $_SESSION['render_data_column_names'];
                            $column_labels = $_SESSION['render_data_column_titles'];
                            $table_class = $_SESSION['render_data_table_class'];

                            //Erase the session data
                            unset($_SESSION['render_data_table_name']);
                            unset($_SESSION['render_data_sort_column']);
                            unset($_SESSION['render_data_column_names']);
                            unset($_SESSION['render_data_column_titles']);
                            unset($_SESSION['render_data_table_class']);
                            unset($_SESSION['render_data_highlighted_column_name']);
                            unset($_SESSION['render_data_highlighted_column_value']);
                            unset($_SESSION['render_data_highlighted_class']);

                            $data_renderer_data = new DataRendererData($table_name, $sort_column, $column_names,
                                $column_labels, $table_class, '', '', '', $database_connection, $target_page);

                            //Create a dynamic data renderer
                            $renderer = new SortableDataRenderer();

                            //Render the table!
                            $renderer->renderResultsAsStyledTable($data_renderer_data);

                        } else {

                            //Build the data rendering data
                            $table_name = "matchups";
                            $sort_column = "player1";
                            $column_names = array("player1","player2","winner");
                            $column_labels = array("Player", "Player", "Winner");
                            $table_class = "stats";
                            $data_renderer_data = new DataRendererData($table_name, $sort_column, $column_names,
                                $column_labels, $table_class, '', '', '', $database_connection, $target_page);

                            //Create a dynamic data renderer
                            $renderer = new SortableDataRenderer();

                            //Render the table!
                            $renderer->renderResultsAsStyledTable($data_renderer_data);
                        }

                    ?>
                </div>
                <!-- End Main Content-->

             <div style="clear: both;">&nbsp;</div>
            </div>
            <!-- end page -->
        </div>
            <?php include("../../include/footer.php"); ?>
    </body>
</html>