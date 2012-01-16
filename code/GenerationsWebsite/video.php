<?php @session_start();
    /**
     * Created By: Jon Tedesco
     * Date: Dec 10, 2010
     */

    # Include PHP scripts
    require_once("include/header/meta_header.php");
    require_once("include/menu.php");
    require_once("include/header/video_player.php");
    require_once("src/utils/mysql_root_driver.php");
    require_once("src/video/video_list.php");


?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<?
    # Meta data, including the title
    meta_header('Video');

    # CSS and javascript
    include('include/header/common_css.php');
    include('include/header/common_javascript.php');
    include("include/javascript/submit_form.php");
    include("include/javascript/sort_video_list.php");
?>
</head>


<body class="home">

<?
    # Google analytics
    include('include/header/google_analytics.php');
?>

<center>
<div id="wrapper">

    <?
        # Build the menu, giving it the current page to highlight
        build_menu('video');
    ?>

	<div id="page" class="container">
		<div id="content">


            <div id="summary" class="box2" style="text-align: justify;'">
            <?
                # Include the panel that holds the current loaded video
                include('src/video/video_panel.php');               
            ?>
             </div>
            
 		</div>
		<div id="sidebar" style="float: left; width: 250px; padding: 0px 25px; text-align: justify; margin-left:25px; padding:0px;">

           <?
               # Render the list of video recordings
                list_video_recordings();

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
    </center>
</body>
</html>