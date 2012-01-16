<?php
    //Initializations
    session_start();
    require_once("../databaseutils/MySqlRootDriver.php");

    //Grab the data from the form
    $user_name = $_POST['username'];
    $password = $_POST['password'];
    $password_check = $_POST['password_check'];
    $old_password = $_POST['old_password'];
    $email = $_POST['email'];
    $first_name = $_POST['firstname'];
    $last_name = $_POST['lastname'];

    //Grab an instance of the database
    $database_driver = new MySqlRootDriver();

    //Form the query and query the database
    $query = "SELECT * FROM user WHERE username='$user_name';";
    $result = $database_driver->query_database($query);
    $actual_old_password = mysql_result($result, 0, 'password');

    //Check that the old password is equal to the new one
    if($old_password != '' && $actual_old_password != $old_password){

        $_SESSION['error_message'] = "<h1><br/>Oops! It looks like your old password is incorrect!</h1>";
        header('Location: ../../my_account.php');

    //Check that the new passwords match
    } else if($password != $password_check){

        $_SESSION['error_message'] = "<h1><br/>Oops! It looks like your new password didn't match!</h1>";
        header('Location: ../../my_account.php');

    } else {

        //Grab the real user data from the database
        $query = "SELECT * FROM user WHERE username='$user_name';";
        $result = $database_driver->query_database($query);
        $old_email = mysql_result($result, 0, 'email');
        $old_last_name = mysql_result($result, 0, 'lastname');
        $old_first_name = mysql_result($result, 0, 'firstname');

        //Update the data that's changed
        if($old_email != $email && $email != ''){

            //For the query and update the email
            $query = "UPDATE user SET email='$email' WHERE username='$user_name';";
            $database_driver->query_database($query);
        }
        if($old_first_name != $first_name && $first_name != ''){

            //For the query and update the first name
            $query = "UPDATE user SET firstname='$first_name' WHERE username='$user_name';";
            $database_driver->query_database($query);
        }
        if($old_last_name != $last_name && $last_name != ''){

            //For the query and update the last name
            $query = "UPDATE user SET lastname='$last_name' WHERE username='$user_name';";
            $database_driver->query_database($query);
        }

        //Update the password if it's not blank
        if($password != ''){

            //For the query and update the password
             $query = "UPDATE user SET password='$password' WHERE username='$user_name';";
             $database_driver->query_database($query);
        }
        header('Location: ../../my_account.php');
    }
?>
