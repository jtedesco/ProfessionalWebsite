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
?>

    <h2><a href="manage_my_team.php">Manage My Team</a></h2><br />
    <h2><a href="trade.php">Trade Players</a></h2><br />

<?php
       }
?>