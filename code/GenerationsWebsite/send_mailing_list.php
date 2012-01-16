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

<div id="wrapper" style="min-height:300px;">

    <?
        # Build the menu, giving it the current page to highlight
        build_menu('admin');
    ?>

	<div id="page" class="container" style="min-height:300px;">
		<div id="content">
        <?
            if(isset($_SESSION['username']) && $_SESSION['username']=='USERNAME'
                    && isset($_SESSION['password']) && $_SESSION['password']=='PASSWORD'){
        ?>
            <center><h1><br/>Sending email...</h1></center>
        <?

                # Pull email command data from session
                $sendTo = $_POST['to'];
                $replyTo = $_POST['reply-to'];
                $subject = $_POST['subject'];
                $file_name = $_POST['file_name'];
                $_SESSION['reply-to'] = $replyTo;
                $_SESSION['subject'] = $subject;
                $_SESSION['file_name'] = $file_name;

                # Display email data
                echo "<div style='float:left; margin-left: 50px; width: 20%;'>
                        <h3>Send To:</h3>
                        &nbsp;&nbsp;&nbsp;$sendTo<br/><br/>

                      </div><div style='float:left;  margin-left: 90px; width: 20%;'>
                        <h3>Reply-To:</h3>
                        &nbsp;&nbsp;&nbsp;$replyTo<br/><br/>

                      </div><div style='float:left;  margin-left: 90px; width: 30%;'>

                        <h3>Subject:</h3>
                        &nbsp;&nbsp;&nbsp;$subject<br/><br/>

                      </div><br/><br/><br/><br/><br/><br/> 

                      <div style='margin-left: 25px; width: 850px;'>
                        <h3>Email Message:</h3>
                        <div style='border: #8B8494 1px solid; background: #1C202F; padding:10px; margin: 20px; box-shadow: black 1px;'>
                            <iframe frameborder=0 style='width: 100%; height:600px; background: #FFFFFF; padding:0px;' src='eblasts/$file_name' ></iframe>
                        </div>
                      </div>";

                # Pull message contents from file
                $template_message = file_get_contents('eblasts/'.$file_name);
                $template_message = str_replace('@@@@@@', $file_name, $template_message);

                  # Decide whether to start emailing people, or to email for debugging ('jon')
                echo "<div style='width:450px; margin-left: 325px;'>";
                if(strcasecmp("all", $sendTo)==0){

                    # Get a connection to the database
                    $database_connection = new MySqlRootDriver();

                    # Form the query for all database information & pull fan data from site
                    $query = "SELECT * FROM Fan;";
                    $results = $database_connection->query_database($query);

                    # Find the number of results
                    $number_of_results = mysql_num_rows($results);

                    # Mail to each person in the list, printing out emails as we go
                    for($i=0; $i<$number_of_results; $i++){

                        # Get this entry from the database
                        $first_name = mysql_result($results, $i, 'first_name');
                        $email = mysql_result($results, $i, 'email');

                        # If we have a first name for them
                        if($first_name!=null){
                            echo "<div id='$email'  style='display:none;'>Emailing:&nbsp;&nbsp;&nbsp;".$first_name." - ".$email."</div>";
                        } else {
                            echo "<div id='$email' style='display:none;'>Emailing:&nbsp;&nbsp;&nbsp;".$email."</div>";
                        }
                    }

                } else {

                     # Send the first test email, with a first name
                    $message = str_replace("######", "Jon", $template_message);
                    $message = str_replace("!!!!!!", "jon.c.tedesco@gmail.com", $message);
                    mail("jon.c.tedesco@gmail.com", $subject, $message, "From: ".$replyTo."\r\nContent-type: text/html\r\n", "-f ".$replyTo);
                    echo "Emailing:&nbsp;&nbsp;&nbsp;"."Jon"." - "."jon.c.tedesco@gmail.com"."<br/>";

                    # Send the first test email, without a first name
                    $message = str_replace(" ######", "", $template_message);
                    $message = str_replace("&first_name=######", "", $message);
                    $message = str_replace("!!!!!!", "tedesco1@illinois.edu", $message);
                    mail("tedesco1@illinois.edu", $subject, $message, "From: ".$replyTo."\r\nContent-type: text/html\r\n", "-f ".$replyTo);
                    echo "Emailing:&nbsp;&nbsp;&nbsp;"."tedesco1@illinois.edu"."<br/>";
                }
                echo "</div><br/><br/>";

                # Show the status of emailing each person if we're emailing everyone
                if(strcasecmp("all", $sendTo)==0){

                     # Mail to each person in the list, printing out emails as we go
                    for($i=0; $i<$number_of_results; $i++){

                        # Get this entry from the database
                        $first_name = mysql_result($results, $i, 'first_name');
                        $email = mysql_result($results, $i, 'email');

                        # If we have a first name for them
                        if($first_name!=null){
                            echo "<script language=javascript>document.getElementById('$email').style.display = 'block';</script>";
                            $message = str_replace("######", $first_name, $template_message); # Replace this special sequence with their first name if we have it
                            $message = str_replace("!!!!!!", $email, $message);
                            mail($email, $subject, $message, "From: ".$replyTo."\r\nContent-type: text/html\r\n", "-f ".$replyTo);
                        } else {
                            echo "<script language=javascript>document.getElementById('$email').style.display = 'block';</script>";
                            $message = str_replace(" ######", "", $template_message); # Replace this special sequence with their first name if we have it
                            $message = str_replace("&first_name=######", "", $message);
                            $message = str_replace("!!!!!!", $email, $message);
                            mail($email, $subject, $message, "From: ".$replyTo."\r\nContent-type: text/html\r\n", "-f ".$replyTo);
                        }
                    }

                }
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