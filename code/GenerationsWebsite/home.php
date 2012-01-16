<?php @session_start();
    /**
     * Created By: Jon Tedesco
     * Date: Dec 10, 2010
     */

    # Include PHP scripts
    require_once("include/header/meta_header.php");
    require_once("include/widgets/slidertron.php");
    require_once("include/widgets/visual_music_player.php");
    require_once("include/widgets/upcoming_gigs.php");
    require_once("include/menu.php");
?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<?
    # Meta data, including the title
    meta_header('Home');

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
        build_menu('home');
    ?>

	<div id="page" class="container">
		<div id="content">

            <?
                # Slideshow, giving the script a list of strings, or paths to the pictures to include
                $pictures = array('style/images/home/pic3.jpg', 'style/images/home/pic1.jpg', 'style/images/home/pic2.jpg');
                build_slidertron($pictures);
            ?>

			<div id="summary" class="box2">
                <strong> Our Music</strong><br />Generations performs the classic &amp; soulful music of artists such
                as the Blues Brothers, Chicago, Sam &amp; Dave, Al Greene, B.B. King, the Doobie Brothers, and many more.
                &nbsp; To listen to our latest recordings, <a href="audio.php">click here</a>.<br /><br />

                <strong>Our Members<br /></strong>Generations boasts three vocalists, a solid rhythm section, and a full
                horn section, ten members in total, ranging from ages sixteen to sixty. &nbsp; For details about our members,
                <a href="members.php">click here</a>.<br />

                <strong><br />Our History<br /></strong>Generations began as a local block party band, and now nearly three
                years old, plays regularly in local restaurants, bars, and festivals. Navigate to our
                <a href="locations.php">locations</a> page to find find more information
                about past and current venues, or learn more about <a href="history.php">our history</a>.<br /><br />

                <strong>Our Future<br /></strong> Generations is always looking for new venues or opportunities.&nbsp;
                <a href="contact.php">Contact us</a> if you are interested in hiring Generations for your next event. <br/><br/>

			</div>
		</div>

		<div id="sidebar">

           <?
                # Build the list of 'upcoming gigs' from the database
                $number_of_gigs_to_list = 5;
                list_upcoming_gigs($number_of_gigs_to_list);

                # Render the visual music player, giving it the path to an mp3
                echo "<center><h3 style='margin-bottom:0px;'><strong>Domino by Van Morrison:</strong></h3></center>";
                $mp3_file = 'audio/Van%20Morrison%20-%20Domino%20(10.15.2008).mp3';
                build_visual_music_player($mp3_file, "Van Morrison", "Domino");

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



