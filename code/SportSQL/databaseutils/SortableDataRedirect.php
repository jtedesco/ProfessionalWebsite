<?php
    session_start();

    //Grab the name of the column to sort by
    $sort_column = $_POST['sortcolumn'];

    //Set a flag so that the page loading it knows to pull this data from the session
    $_SESSION['render_data_from_session'] = 1;
    $_SESSION['render_data_sort_column'] = $sort_column;

    //Redirect to the target url
    $target_page = $_SESSION['target_page'];
    unset($_SESSION['target_page']);
    $actual_target_page = "Location: ../../".$target_page;
    header($actual_target_page);
?>
 
