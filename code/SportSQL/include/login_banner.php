<?php
    $param = 'user';
    if(isset($_SESSION[$param])){

        //Grab an instance of the database
        $database_driver = new MySqlRootDriver();

        //Form the query and query the database
        $query = "SELECT firstname FROM user WHERE username = '$_SESSION[$param]';";
        $result = $database_driver->query_database($query);
        $firstname = mysql_result($result, 0, 'firstname');

        echo "<h2>Weclome, $firstname!</h2></br>";
    }
?>