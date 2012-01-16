<?php @session_start();
    /**
     * Created By: Jon Tedesco
     * Date: Dec 10, 2010
     */

    # Include PHP scripts
    require_once("include/header/meta_header.php");
    require_once("include/menu.php");
    require_once("include/widgets/upcoming_gigs.php");

    # Include scripts
    require_once("src/utils/mysql_root_driver.php");


?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<?
    # Meta data, including the title
    meta_header('Unsubscribe');

    # CSS and javascript
    include('include/header/common_css.php');
    include('include/header/common_javascript.php');
?>
</head>


<body class="home">

<?
    # Google analytics
    include('include/header/google_analytics.php');
?>

<div id="wrapper" style="min-height:300px;">

    <?
        # Build the menu, giving it the current page to highlight
        build_menu('');
    ?>

	<div id="page" class="container" style="min-height:300px;">
		<div id="content">


            <div id="summary" class="box2" style='text-align:left;'>
            <?
                # Include the panel that holds the current loaded song's data
                include('src/contact/unsubscribe.php');
            ?>
            </div>
            

		</div>
		<div id="sidebar" >

           <?
               # Build the list of 'upcoming gigs' from the database
                $number_of_gigs_to_list = 4;
                list_upcoming_gigs($number_of_gigs_to_list);
            ?>

    	</div>
	</div>

    <?
        # The footer
    include('include/footer.php');
    ?>

</div>
</body>
</html>