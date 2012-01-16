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

<div id="wrapper">

    <?
        # Build the menu, giving it the current page to highlight
        build_menu('admin');
    ?>

	<div id="page" class="container">
		<div id="content">

    <?
    if(isset($_POST['username']) && $_POST['username']=='USERNAME'
            && isset($_POST['password']) && $_POST['password']=='PASSWORD'){
            $_SESSION['username'] = 'USERNAME';
            $_SESSION['password'] = 'PASSWORD';

    ?>

            <div class="box3">
                     <h1>Send Email to Mailing List</h1>
                     <form action="send_mailing_list.php" method="POST" style="margin-right: 20px; width:95%;">

                         <h3>Send To</h3>
                         <div style="width: 5%; float:left;">
                             <input type="radio" name="to" value="jon.c.tedesco@gmail.com" checked>
                             <input type="radio" name="to" value="all">
                         </div>
                         <div style="font-size:13px; padding-top:5px; width=90%;">
                            &nbsp;&nbsp;Jon<br/><br/>
                            &nbsp;&nbsp;Entire Generations Mailing List<br/><br/>
                         </div>


                         <h3>Reply-To</h3>
                         <input type="text" name="reply-to" value="<?echo $_SESSION['reply-to'];?>"/><br/><br/>

                         <h3>Email Subject</h3>
                         <input type="text" name="subject" value="<?echo $_SESSION['subject'];?>"/><br/><br/>

                         <h3>Name of File to Email:</h3>
                         <input name="file_name" id="file_name" value="<?echo strip_tags($_SESSION['file_name']);?>"/><br/><br/>
                         <input type="submit" name="submit" value="Send Email">
                     </form>
             </div>
    <?
        } else {
            echo "<center><br/><br/><h1 style='color: #ff3333;'>You must login to access this page.</h1></center>";
        }
    ?>

		</div>
	</div>

    <?
        # The footer
    include('include/footer.php');
    ?>

</div>
</body>
</html>