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

        <?php include("include/submit_form_with_link.php")?>
        <?php require_once ("src/databaseutils/MySqlRootDriver.php"); ?>

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

                <!-- Start Main (Center) Content-->
                <div class="full-content">
                     <?php include("src/userhome/user_home_content.php"); ?>
                 </div>

                <!-- End Main (Center) Content-->
                <div style="clear: both;">&nbsp;</div>
            </div>
            <!-- end page -->
        </div>
            <?php include("include/footer.php"); ?>
    </body>
</html>
