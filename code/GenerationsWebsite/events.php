<?php @session_start();
    /**
     * Created By: Jon Tedesco
     * Date: Dec 10, 2010
     */

    # Include PHP scripts
    require_once("include/header/meta_header.php");
    require_once("include/menu.php");
    require_once("src/utils/mysql_root_driver.php");
    require_once("src/events/list_events.php");
?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<?
    # Meta data, including the title
    meta_header('Events');

    # CSS and javascript
    include('include/header/common_css.php');
    include('include/header/calendar_css.php');
    include('include/header/common_javascript.php');
    include("include/javascript/submit_form.php");
?>
</head>


<body class="single">

<?
    # Google analytics
    include('include/header/google_analytics.php');
?>

<center>
<div id="wrapper">

    <?
        # Build the menu, giving it the current page to highlight
        build_menu('events');
    ?>

    <div id="page" class="container" style="text-align:justify;">
        <div id="content">


            <div class="box3">
             <?
                 # Show the list of events
                 list_events();

                 # Include the Facebook 'like' button
                 echo "<span style='float:right;'";
                     include('include/widgets/facebook_like_button.php');
                 echo "</span>";
             ?>
              </div>

        </div>
    </div>

    <?
        # The footer
    include('include/footer.php');
    ?>

</div>
    </center>
</body>
</html>