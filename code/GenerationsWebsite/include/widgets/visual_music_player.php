<?php
/**
 * Created By: Tedesco
 * Date: Dec 15, 2010
 */

function build_visual_music_player($mp3_file, $artist, $title){

    // Get the browser information
    $browser_information = $_SERVER['HTTP_USER_AGENT'] . "\n\n";

    // Determine whether this is one of the supported browsers, and show the advanced media player if it is
    if(strstr($browser_information, "Firefox")!=False || strstr($browser_information, "Chrome")!=False || strstr($browser_information, "Safari")!=False) {
        echo "<div class='ui360' style='margin-bottom:20px;'><a href=$mp3_file></a></div>";
    } else {

        // Otherwise, use a simple media player alternative
        echo"
            <center>
            <br/>
            <p id='audioplayer_1'>Sorry, it looks like your browser doesn't support flash!</p>  
            <script type='text/javascript'>
                 AudioPlayer.embed('audioplayer_1', {
                 soundFile: '$mp3_file',
                 titles: '$title',  
                 artists: '$artist',
            });
            </script>
            <br/>
            <br/>
            </center>";
        
    }
}
?>
