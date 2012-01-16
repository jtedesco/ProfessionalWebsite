<?php @session_start();
    /**
     * Created By: Jon Tedesco
     * Date: Dec 10, 2010
     */

    # Include PHP scripts
    require_once("include/header/meta_header.php");
    require_once("include/menu.php");
    require_once("include/widgets/visual_music_player.php");
    require_once("src/utils/mysql_root_driver.php");
    require_once("src/audio/audio_list.php");


?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<?
    # Meta data, including the title
    meta_header('Audio');

    # CSS and javascript
    include('include/header/common_css.php');
    include('include/header/common_javascript.php');
    include("include/javascript/sort_audio_list.php");
    include("include/javascript/load_audio_file.php");
    include("include/header/yahoo_media_player.php");
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
        build_menu('audio');
    ?>

	<div id="page" class="container">
		<div id="sidebar">

           <?
               # Render the list of video recordings
                list_audio_recordings();

                # Include the Facebook 'like' button
                include('include/widgets/facebook_like_button.php');
            ?>
            <br/><br/>

    	</div>
        <div id="content">


            <div id="summary" class="box2">
            <?
                # Include the panel that holds the current loaded song's data
                include('src/audio/audio_panel.php');

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