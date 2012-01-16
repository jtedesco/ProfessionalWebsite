<?php
    if(!isset($_SESSION['user'])){
?>
        <div>
            <h2>Login</h2><br>

                <form name="loginform" method="post" action="src/loginsystem/login.php">
                    <div>
                        User Name:
                    <div></div>
                        <input type="text" name="username" id="username" size="15" value="" /><br />
                    <div></div>
                        Password:
                    </div><div>
                        <input type="password" name="password" id="password" size="15" value="" />
                    </div>
                </form>
                <a href="javascript:submit_login_form()">Login</a> | <a href="create_new_account.php">Create a new account</a>
        </div>
<?php
    } else {
        include("include/login_banner.php");
    }
?>