<?php
    session_start();

    //Grab the name of the column to sort by
    $event_id = $_POST['event_id'];

    //Set a flag so that the page loading it knows to pull this data from the session
    $_SESSION['load_photos_from_session'] = 1;
    $_SESSION['event_id'] = $event_id;

    //Redirect to the video page
    header("Location: ../../photos.php");
?>
 
