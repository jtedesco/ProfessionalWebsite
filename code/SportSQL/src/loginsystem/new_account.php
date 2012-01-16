<?php
    session_start();
    require_once ("../databaseutils/MySqlRootDriver.php");

    $username = $_POST["username"];
    $password = $_POST["password"];
    $password_check = $_POST["password_check"];
    $first_name = $_POST["first_name"];
    $last_name = $_POST["last_name"];
    $email = $_POST["email"];

    //Display an error message if the passwords don't match
    if ($password != $password_check){

        $_SESSION['create_account_error_message'] = "<h1><br/>Password and Verify Password do not match.</h1>";
        header("Location: ../../create_new_account.php");

    //Display an error message if the password is too short
    } else if (strlen($password) < 4) {

        $_SESSION['create_account_error_message'] = "<h1><br/>Password must be at least 4 characters.</h1>";
        header("Location: ../../create_new_account.php");
    }

    //Get a connection to the database and check if this user already exists
    $database_driver = new MySqlRootDriver();
    
    // SQL Injection Checks
    $username = mysql_real_escape_string($username);
    $password = mysql_real_escape_string($password);
    $password_check = mysql_real_escape_string($password_check);
    $first_name = mysql_real_escape_string($first_name);
    $last_name = mysql_real_escape_string($last_name);
    $email = mysql_real_escape_string($email);
    
    $query = "SELECT * FROM user WHERE username='$username'";
    $result = $database_driver->query_database($query);
    
    $ban_query = "SELECT * FROM banned WHERE email='$email'";
    $ban_result = $database_driver->query_database($ban_query);

    // If the username is already in the database display an error message
    if (mysql_num_rows($ban_result) == 1) {
    
    	$_SESSION['create_account_error_message'] = "<h1><br/>You have been banned by the adminitrator</h1>";
        header("Location: ../../create_new_account.php");
        
    } elseif (mysql_num_rows($result) == 1) {

        $_SESSION['create_account_error_message'] = "<h1><br/>Sorry, that username already exists.</h1>";
        header("Location: ../../create_new_account.php");

    } else {

        //Create this new account
        $query = "INSERT INTO user VALUES ('$username', '$password', '$email', '$first_name', '$last_name', '1');";
        $database_driver->query_database($query);
        
        // Create Team Name
        if($username != "admin"){
	        $team_name = $username."\'s Team";
            $query = "INSERT INTO team (team_name, user_name, league_id) VALUES ('$team_name','$username','1');";
            $database_driver->query_database($query);
        }
        
        // Create View
        $view_name = $username . "players";
        $view_query = "CREATE VIEW $view_name AS SELECT consists_of.player_id, position, name, real_team FROM consists_of, player WHERE consists_of.player_id = player.player_id
        AND team_name='$team_name'";
        $database_driver->query_database($view_query);
        
        //Log the user in
        $_SESSION['user'] = $username;
        header("Location: ../../index.php");
    }

?>