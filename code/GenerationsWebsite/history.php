<?php @session_start();
    /**
     * Created By: Jon Tedesco
     * Date: Dec 10, 2010
     */

    # Include PHP scripts
    require_once("include/header/meta_header.php");
    require_once("include/widgets/slidertron.php");
    require_once("include/menu.php");
?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<?
    # Meta data, including the title
    meta_header('History');

    # CSS and javascript
    include('include/header/common_css.php');
    include('include/header/common_javascript.php');
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
        build_menu('history');
    ?>

    <div id="page" class="container">
        <div id="content">
            <div class="box3">

                <?
                    # Slideshow, giving the script a list of strings, or paths to the pictures to include
                    $pictures = array('style/images/history/pic1.jpg', 'style/images/history/pic2.jpg', 'style/images/history/pic3.jpg');
                    build_styled_slidertron($pictures, "float: left;", "375px", "300px");
                ?>
                    Maybe it’s the rock ‘n’ roll that keeps them young.
                <br /><br />
                    But a Wheaton band is bridging the generation gap and giving a few middle-aged musicians another shot
                        at the easy-going feel of the garage band.
                <br /><br />
                    The band, Generations, got its start about a year ago, as residents of a neighborhood in east
                        Wheaton were gearing up for their annual block party. Idle conversation among the neighbors turned
                        into a discussion about getting some musical entertainment from the few grown-up, amateur musicians.
                <br /><br />
                    “We had low expectations of banging out like ‘Louie Louie’ and ‘Mony Mony,’” said guitarist Rob Noonan, 40.
                <br /><br />
                    But when a neighborhood teenager brought his saxophone to one of the initial practices, it set things in
                        a new direction.
                <br /><br />
                    “So he started playing. It was just like, ‘Wow, this was pretty cool,’” Noonan said. “It opened up a few
                        different songs for us.”
                <br /><br />
                    After the block party, the band settled into a seven-member group: four adults and three then-high
                        school seniors making up a brass section. All but one of the band members live within a few blocks
                        of each other in the neighborhood, just west of President Street.
                <br /><br />
                    A winter of practicing and a few gigs later, the band had its biggest show last month at Daley Plaza
                        in Chicago.
                <br /><br />
                    “To play [there] was amazing — there had to be 2,000 people,” Noonan said. “Took us a long way from
                        the block party.”
                <br /><br />
                    Now Generations is getting in its flow, practicing at least weekly in a garage near Wheaton’s
                        Lincoln Elementary School.
                <br /><br />
                    For the older members, all of whom had their own bands in their teen years, it’s a chance to re-experience
                        the joy of music.
                <br /><br />
                        A musician never knows when the last show will be, Noonan said, so most of the older members appreciate
                        performing more now than they did in their younger days.
                <br /><br />
                        “You don’t have that drive or that pressure of trying to make it or trying to make money. We’re
                        doing this for fun,” he said. “It’s not the same as when you had visions of being the next Guns N’
                        Roses — that’s when I was playing the first time around, if it gives it away.”
                <br /><br />
                        Tony Restaino, the band’s 47-year-old drummer and host of the practices, remembers trying to earn
                        money with his music as a teenager. He and his brothers were in a wedding band to earn money for college.
                <br /><br />
                        “We were booked pretty heavily until the disco age, and then the DJs kind of took over the scene,”
                        he said.
                <br /><br />
                        Now, Generations can choose when and where it plays — though it’s not as if members are turning
                        down requests, Restaino added.
                <br /><br />
                        The band has about 40 songs in its growing repertoire, all of them covers. Members haven’t started
                        writing their own music — or at least they haven’t brought out the songs they might have written
                        long ago.
                <br /><br />
                        “We all have them, but we’ve kept them in the closet,” Noonan said with a laugh. “There are so many
                        other good things that people have written.”
                <br/><br>
                    <div style="text-align:right; padding-right:100px;";>
                        &mdash; Brian Hudson, <b>Wheaton Leader</b>, 2008
                    </div>
            <br/><br>


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



