<?php
    session_start();

    //Grab the name of the column to sort by
    $video_to_load = $_POST['video_path'];

    //Set a flag so that the page loading it knows to pull this data from the session
    $_SESSION['load_video_from_session'] = 1;
    $_SESSION['video_to_load'] = $video_to_load;

    //Redirect to the video page
    header("Location: ../../video.php");
?>
 
