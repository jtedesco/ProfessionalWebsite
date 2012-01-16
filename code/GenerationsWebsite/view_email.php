<?php

    # Get the data from the email for this email
    $email = $_GET['email'];
    $file_name = $_GET['file_name'];
    @$first_name = $_GET['first_name'];

    # Fill in the email template
    $message = file_get_contents('eblasts/'.$file_name);
    $message = str_replace('@@@@@@', $file_name, $message);

    # If we have a first name for them
    if($first_name!=null){
        $message = str_replace("######", $first_name, $message); # Replace this special sequence with their first name if we have it
        $message = str_replace("!!!!!!", $email, $message);
    } else {
        $message = str_replace(" ######", "", $message); # Replace this special sequence with their first name if we have it
        $message = str_replace("&first_name=######", "", $message); # Disable sending name via GET
        $message = str_replace("!!!!!!", $email, $message);
    }

    # Display the email
    echo $message;
?>