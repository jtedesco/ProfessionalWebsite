<?
    if(isset($_SESSION['new_account_message'])){

        echo $_SESSION['new_account_message'];
        unset($_SESSION['new_account_message']);
    }
?>

<h2>Create A New Account</h2>
<br />
<form style="width: 480px;" action="src/loginsystem/new_account.php" method="post">
    <div>
        User Name:
    <div></div>
        <input type="text" name="username" id="user_name" size="15" value="" /><br />
    <div></div>
        Password:
    </div><div>
        <input type="password" name="password" id="password" size="15" value="" />
    <div></div>
        Retype Password:
    </div><div>
        <input type="password" name="password_check" id="password_check" size="15" value="" />
    <div></div>
        First Name:
    </div><div>
        <input type="text" name="first_name" id="first_name" size="15" value="" />
    <div></div>
        Last Name:
    </div><div>
        <input type="text" name="last_name" id="last_name" size="15" value="" />
    <div></div>
        Email:
    </div><div>
        <input type="text" name="email" id="email" size="15" value="" />
    </div><br/><br/>
    <input type="submit" value="Submit" />
</form>
