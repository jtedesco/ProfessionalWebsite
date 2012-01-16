<?php @session_start();
    /**
     * Created By: Jon Tedesco
     * Date: Dec 10, 2010
     */

    # Include PHP scripts
    require_once("include/header/meta_header.php");
    require_once("include/menu.php");
    require_once("src/utils/mysql_root_driver.php");
    require_once("src/photos/photo_list.php");
?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<?
    # Meta data, including the title
    meta_header('Photos');

    # CSS and javascript
    include('include/header/common_css.php');
    include('include/header/common_javascript.php');
    include("include/javascript/submit_form.php");
    include("include/header/galleria.php");
?>
</head>


<body class="home">

<?
    # Google analytics
    include('include/header/google_analytics.php');
?>

<div id="wrapper">

    <?
        # Build the menu, giving it the current page to highlight
        build_menu('photos');
    ?>

	<div id="page" class="container">
		<div id="content">


            <div id="summary" class="box2">
            <?
                # Include the panel that holds the currently loaded photo album
                include("src/photos/photo_panel.php");
            ?>
            </div>

		</div>
		<div id="sidebar" >

           <?
               # Render the list of photos (by event)
                display_photo_navigation();

                # Include the Facebook 'like' button
                include('include/widgets/facebook_like_button.php');
            ?>
            <br/><br/>

    	</div>
	</div>

    <?
        # The footer
        include('include/footer.php');
    ?>

</div>


<?
    # The gallery scripts
    include('include/javascript/initialize_galleria.php');
?>

</body>
</html>