<?php @session_start();
    /**
     * Created By: Jon Tedesco
     * Date: Dec 10, 2010
     */

    # Include PHP scripts
    require_once("include/header/meta_header.php");
    require_once("include/menu.php");
    require_once("include/widgets/slidertron.php");
    require_once("include/widgets/visual_music_player.php");
    require_once("include/widgets/upcoming_gigs.php");
?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<?
    # Meta data, including the title
    meta_header('Contact');

    # CSS and javascript
    include('include/header/common_css.php');
    include('include/header/common_javascript.php');
    include('include/header/visual_music_player.php');
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
        build_menu('contact');
    ?>

	<div id="page" class="container">
		<div id="content">

            <?
                # Build the form for signing up for the email list
                include("src/contact/email_list_form.php");
            ?>

            <?
                # Build the contact us form
                include("src/contact/contact_us_form.php");
            ?>


		</div>

		<div id="sidebar" >

           <?
                # Build the list of 'upcoming gigs' from the database
                $number_of_gigs_to_list = 4;
                list_upcoming_gigs($number_of_gigs_to_list);

                # Render the visual music player, giving it the path to an mp3
               echo "<center><h3 style='margin-bottom:0px;'><strong>Vehicle by Ides of March:</strong></h3></center>";
                $mp3_file = 'audio/Ides%20of%20March%20-%20Vehicle%20(10.15.2008).mp3';
                build_visual_music_player($mp3_file, "Ides of March", "Vehicle");

                # Include the Facebook 'like' button
                include('include/widgets/facebook_like_button.php');
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



