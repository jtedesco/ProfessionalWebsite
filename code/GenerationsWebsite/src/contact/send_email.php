<?php
    // Start the session
    session_start();

    # Include input script
    require_once("cleanup_input.php");

    //Grab the data from the form
    $email = $_POST['email'];
    $name = $_POST['name'];
    $message = $_POST['message'];

    // Put the form data back into the session
    $_SESSION['email'] = $email;
    $_SESSION['name'] = $name;
    $_SESSION['message'] = $message;

    // Check for spammers
    if (eregi('http:', $message)) {

        $_SESSION['contact_us_error_message'] = "<div class='error_message'>Sorry, your message cannot contain any links.</div>";

    // Check that everything has data
    } elseif (empty($email) || empty($name) || empty($message)){

        $_SESSION['contact_us_error_message'] = "<div class='error_message'>Oops! It looks like you left one of the fields empty. Please fill in all fields, and try again.</div>";

    // Check for a valid email address
    } elseif (!strstr($email, "@") || !strstr($email, ".")) {

        $_SESSION['contact_us_error_message'] = "<div class='error_message'>Oops! It looks like that email address was invalid. Please check your email address try again.</div>";

    } else {

        $todayis = date("l, F j, Y, g:i a") ;

        $message = cleanup_input($message);

        $message = " $todayis [EST] \n
            Message: $message \n
            From: $name ($email)\n
        ";

        $from = "From: $email\r\n";

        @mail("tedesco1@illinois.edu", "Site Visitor", $message, $from, "-f ".$from);

        $_SESSION['contact_us_thanks_message'] = "<div class='error_message'>Thank you for contacting Generations!</div>";

    }

    header('Location: ../../contact.php');
?>
