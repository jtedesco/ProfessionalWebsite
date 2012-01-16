<?php
    //Start the session
    session_start();

    # Include scripts
    require_once("cleanup_input.php");
    require_once("../utils/mysql_root_driver.php");

    //Grab the data from the form
    $email = $_POST['email'];
    if(isset($_POST['first_name'])){
        $first_name = $_POST['first_name'];
    } else {
        $first_name = null;
    }
    if(isset($_POST['last_name'])){
        $last_name = $_POST['last_name'];
    } else {
        $last_name = null;
    }

    // Put the form data back into the session
    $_SESSION['email'] = $email;
    $_SESSION['last_name'] = $last_name;
    $_SESSION['first_name'] = $first_name;

    //Check for valid email
    if (!strstr($email, "@") || !strstr($email, ".")) {

        $_SESSION['email_list_error_message'] = "<div class='error_message'>Please enter a valid email address.</div>";

    //Check that it's only one email
    } elseif (substr_count($email, "@")>1 && substr_count($email, ".")>1 ){

        $_SESSION['email_list_error_message'] = "<div class='error_message'>Please enter only one email address.</div>";

    } else {

        // Cleanup any dangerous input
        $email = cleanup_input($email);
        $email = trim($email);
        $_SESSION['email_list_thanks_message'] = "<div class='error_message'>Thank you for signing up for the Generations email list!</div>";

        // Form the query for inserting the new email into the database
        if($last_name != null && $first_name != null){
            $query = "INSERT INTO Fan VALUES ('$email', '$first_name', '$last_name');";
        } elseif($last_name == null && $first_name != null){
            $query = "INSERT INTO Fan (email, first_name) VALUES ('$email', '$first_name');";
        } elseif($last_name != null && $first_name == null){
            $query = "INSERT INTO Fan (email, last_name) VALUES ('$email', '$last_name');";
        } else {
            $query = "INSERT INTO Fan (email) VALUES ('$email');";
        }
        if($first_name==null) {
            $first_name = "";
        }


        // Insert this new email address into the database & ignore any errors that occur (e.g. email already exists)
        $database_connection = new MySqlRootDriver();
        @$database_connection->query_database($query);

        // The content of the welcome email
        $email_content = file_get_contents("../../eblasts/automated_welcome_email.html");
        $email_content = str_replace('######', $first_name, $email_content);
        $email_content = str_replace('!!!!!!', $email, $email_content);
        $email_content = str_replace('@@@@@@', 'automated_welcome_email.html', $email_content);

        // Send a welcome email
        @mail($email, "Welcome to the Generations Mailing List", $email_content, "From: jon@generationschicago.com\r\nContent-type: text/html\r\n", "-f jon@generationschicago.com");
    }
    header('Location: ../../contact.php');
?>
