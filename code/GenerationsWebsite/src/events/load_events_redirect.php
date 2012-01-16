<?php
    session_start();

    //Grab the name of the column to sort by
    $event_to_load = $_POST['event'];

    //Set a flag so that the page loading it knows to pull this data from the session
    $_SESSION['load_event_from_session'] = 1;
    $_SESSION['event_to_load'] = $event_to_load;

    //Redirect to the video page
    header("Location: ../../events.php");
?>
 
