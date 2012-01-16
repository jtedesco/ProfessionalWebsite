<?php
    //Start the session
    session_start();

    //Grab the data from the form
    $email = $_POST['email'];
    $name = $_POST['name'];
    $message = $_POST['message'];

    //Check for spammers
    if (eregi('http:', $message)) {

        $_SESSION['contact_us_error_message'] = "<h1><br/>Do NOT try that!!!</h1>";

    //Check that everything has data
    } elseif (empty($email) || empty($name) || empty($message)){

        $_SESSION['contact_us_error_message'] = "<h1><br/>Oops! It looks like you left one of the fields empty. <br/>Please fill in all fields, and try again.</h1>";

    //
    } elseif (!strstr($email, "@") || !strstr($email, ".")) {

        $_SESSION['contact_us_error_message'] = "<h1><br/>Oops! It looks like that email address was invalid.<br/> Please check your email address try again.</h1>";

    } else {

        $todayis = date("l, F j, Y, g:i a") ;

        $message = stripcslashes($message);

        $message = " $todayis [EST] \n
            Message: $message \n
            From: $name ($email)\n
        ";

        $from = "From: $email\r\n";

        @mail("tedesco1@illinois.edu", "Site Visitor", $message, $from);

        $_SESSION['contact_us_thanks_message'] = "<h2>Thank you for contacting SportSQL!</h2>";

    }

    header('Location: ../../contact_us.php');
?>
                    