<?php
/**
 * User: Jon
 * Date: Jan 12, 2011
 */

    // Grab the email address to delete
    $email = $_GET['email'];
    $email = stripcslashes($email);

    // Run the query to delete the email address
    $query = "DELETE FROM Fan WHERE email='$email';";
    $database_connection = new MySqlRootDriver();
    $database_connection->query_database($query);

    // Display the goodbye message
    echo "<h3>You have successfully unsubscribed <i>$email</i> from the Generations mailing list</h3>";
 
