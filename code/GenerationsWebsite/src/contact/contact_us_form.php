<div style="width:100%; margin: 20px 20px 20px 40px;">

    <?
        if(isset($_SESSION['contact_us_thanks_message'])){

             echo $_SESSION['contact_us_thanks_message'];
             unset($_SESSION['contact_us_thanks_message']);

             echo "<form style=\"width: 100%;\"></form>";

        } else if(isset($_SESSION['contact_us_error_message'])){
    ?>
            <h2 style="margin-bottom: 3px;">Contact Us</h2>
            <h4 style="margin-top: 3px;">Looking to hire us? Song Suggestions? Fan photos? Contact us!</h4>
    <?
            echo $_SESSION['contact_us_error_message'];
            unset($_SESSION['contact_us_error_message']);

    ?>

            <form style="width: 500px;" action="src/contact/send_email.php" method="post">
                 <div>
                     Name: <b>*</b>
                 <div></div>
                     <input type="text" name="name" size="15" value='<? echo $_SESSION["name"];?>'/><br />
                 <div></div>
                     E-Mail: <b>*</b>
                 </div><div>
                     <input type="text" name="email" size="15" value='<? echo $_SESSION["email"]; ?>'/>
                 <div></div>
                     Message: <b>*</b>
                 </div><div>
                     <textarea type="text" name="message"><? echo $_SESSION["message"]; ?></textarea>
                 </div>
                 <center>
                    <input class="submit" type="submit" value="Submit" />
                 </center>
            </form>
    <?
        } else {
    ?>

            <h2 style="margin-bottom: 3px;">Contact Us</h2>
            <h4 style="margin-top: 3px;">Looking to hire us? Song Suggestions? Fan photos? Contact us!</h4>

            <form style="width: 500px;" action="src/contact/send_email.php" method="post">
                  <div>
                      Name: <b>*</b>
                  <div></div>
                      <input type="text" name="name" size="15"/><br />
                  <div></div>
                      E-Mail: <b>*</b>
                  </div><div>
                      <input type="text" name="email" size="15"/>
                  <div></div>
                      Message: <b>*</b>
                  </div><div>
                      <textarea name="message"></textarea>
                  </div>
                  <center>
                     <input class="submit" type="submit" value="Submit" />
                  </center>
              </form>
    <?
        }
    ?>
</div>
<br/>