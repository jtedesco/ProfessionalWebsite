<?php @session_start();
    /**
     * Created By: Jon Tedesco
     * Date: Feb 11, 2011
     */

    # Include PHP scripts
    require_once("include/header/meta_header.php");
    require_once("include/menu.php");
    require_once("src/utils/mysql_root_driver.php");
?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<?
    # Meta data, including the title
    meta_header('Sending Mail...');

    # CSS and javascript
    include('include/header/common_css.php');
    include('include/header/common_javascript.php');
?>
</head>


<body class="single">

<?
    # Google analytics
    include('include/header/google_analytics.php');
?>

<div id="wrapper" style="min-height: 300px;">

    <?
        # Build the menu, giving it the current page to highlight
        build_menu('admin');
    ?>

	<div id="page" class="container" style="min-height:300px;">
		<div id="content">


            <div class="box3">
                     <h1>Login</h1>
                     <form action="mailing_list.php" method="POST" style="margin-right: 20px; width:95%;">

                         <h3>User Name</h3>
                         <input type="text" name="username" id="username">


                         <h3>Password</h3>
                         <input type="password" name="password" id="password">
                         
                         <input type="submit" name="submit" value="Send Email">
                     </form>

             </div>

		</div>
	</div>

    <?
        # The footer
    include('include/footer.php');
    ?>

</div>
</body>
</html>