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
        <?php require_once ("src/databaseutils/DataRendererData.php"); ?>
        <?php require_once ("src/databaseutils/SortableDataRenderer.php"); ?>
        <?php include("include/submit_form_with_link.php")?>

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
                        $database_connection = new MySqlRootDriver();

                        //The page this page to reload to
                        $target_page = "admin_panel.php";

                        if($_SESSION['user']=="admin"){
				//Build the data rendering data
				$table_name = "user";
				$sort_column = "username";
				$column_names = array("username","firstname","lastname","email");
				$column_labels = array("Username", "First Name", "Last Name","E-mail");
				$table_class = "stats";
				$data_renderer_data = new DataRendererData($table_name, $sort_column, $column_names,
				$column_labels, $table_class, '', '', '', $database_connection, $target_page);
				
				//Create a dynamic data renderer
				$renderer = new SortableDataRenderer();
				
				//Render the table!
				$renderer->renderResultsAsStyledTable($data_renderer_data);
                        } else {

                            die("You do not have admin access");
                        }

                    ?>
                </div>
         
                	<center>
			<form style="width: 440px;" name="loginform" method="post" action="src/loginsystem/admin_delete.php">
			   
			        <br>
			        Username:
			        <input type="text" name="username" id="username" size="15" value="" /> <input type="submit" value="Delete User" />
			
			</form>
			<form style="width: 440px;" name="banform" method="post" action="src/loginsystem/ban_user.php">
			   
			        Email:
			        <input type="text" name="email" id="email" size="15" value="" /> <input type="submit" value="Ban User" />
			
			</form>
			</center>
			
        </div>		
                <!-- End Main Content-->

             <div style="clear: both;">&nbsp;</div>
            </div>
            <!-- end page -->
        </div>
            <?php include("include/footer.php"); ?>
    </body>
</html>