<?php
    if(!isset($_SESSION['user'])){
?>
                <div>
                    <h2>Login</h2><br>
                        <form name="loginform" method="post" action="src/loginsystem/login.php">
                            <div>
                                User Name:
                            <div></div>
                                <input type="text" name="username" style="width: 100%;" id="username" size="15" value="" /><br />
                            <div></div>
                                Password:
                            </div><div>
                                <input type="password" name="password"  style="width: 100%;" id="password" size="15" value="" />
                            </div>
                        </form><br/>
                        <a href="javascript:submit_login_form()">Login</a> | <a href="create_new_account.php">Create a new account</a>
                </div><br/>
<?php
    } else {

        //Grab an instance of the database
        $database_driver = new MySqlRootDriver();

        //Form the query and query the database
        $param = 'user';
        $query = "SELECT * FROM user WHERE username='$_SESSION[$param]';";
        $result = $database_driver->query_database($query);

        //Grab the actual user data from the database results
        $user_name = mysql_result($result, 0, 'username');
        $password = mysql_result($result, 0, 'password');
        $email = mysql_result($result, 0, 'email');
        $first_name = mysql_result($result, 0, 'firstname');
        $last_name = mysql_result($result, 0, 'lastname');
?>
        <h2>My Account</h2><br />
<?php
        if(isset($_SESSION['error_message'])){
            echo $_SESSION['error_message'];
            unset($_SESSION['error_message']);
        }
?>
        <div style="text-align:left; width: 100%;">
            <div>
                <form name="updateform" method="post" action="src/userhome/update_user_data.php">
                    User Name:
                    <input type="text" readonly="true" name="username" style="width: 100%;" id="username" size="15" value="<?php echo $user_name;?>" /><br /><br />
                    First Name:
                    <input type="text" name="firstname"  style="width: 100%;" id="firstname" size="15" value="<?php echo $first_name;?>" /> <br /><br />
                    Last Name:
                    <input type="text" name="lastname"  style="width: 100%;" id="lastname" size="15" value="<?php echo $last_name;?>" /><br /><br />
                    Email:
                    <input type="text" name="email"  style="width: 100%;" id="email" size="15" value="<?php echo $email;?>" /><br /><br />
                    Old Password:
                    <input type="password" name="old_password"  style="width: 100%;" id="old_password" size="15" value="" /><br /><br />
                    New Password:
                    <input type="password" name="password"  style="width: 100%;" id="password" size="15" value="" /><br /><br />
                    Confirm New Password:
                    <input type="password" name="password_check"  style="width: 100%;" id="password_check" size="15" value="" /><br /><br />
                </form>
            </div><br/>
        <p style="text-align:center">
            <a href="src/loginsystem/logout.php">Logout</a> &nbsp; |
                &nbsp; <a href="javascript:submit_user_data_form()">Save Changes</a>  &nbsp; |
                &nbsp; <a href="src/loginsystem/cancel_account.php">Cancel Account</a>
        </p>
        </div>
<?php

    }
    
?>