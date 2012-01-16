<?php @session_start();
    /**
     * Created By: Jon Tedesco
     * Date: Dec 27 2010
     */

    # Include PHP scripts
    require_once("include/header/meta_header.php");
    require_once("include/menu.php");
    require_once("src/songs/song_list.php");
?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<?
    # Meta data, including the title
    meta_header('Songs');

    # CSS and javascript
    include('include/header/common_css.php');
    include('include/header/common_javascript.php');
    include("include/javascript/submit_form.php");
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
        build_menu('songs');
    ?>

    <div id="page" class="container">
        <div id="content">
            <div class="box3">

            <?

                # Build the Generations song list
                build_song_list();
            ?>

                <br/><br/>

            <?
                # Include the Facebook 'like' button
                include('include/widgets/facebook_like_button.php');
            ?>

    	    </div>
    	</div>
	</div>
    <?
        # The footer
    include('include/footer.php');
    ?>

</div>
</body>
</html>