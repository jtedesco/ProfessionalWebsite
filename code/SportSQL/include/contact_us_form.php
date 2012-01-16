 <h2>Contact Us</h2>
 <br />

<?
    if(isset($_SESSION['contact_us_thanks_message'])){

         echo $_SESSION['contact_us_thanks_message'];
         unset($_SESSION['contact_us_thanks_message']);

         echo "<form style=\"width: 480px;\"></form>";

    } else if(isset($_SESSION['contact_us_error_message'])){

        echo $_SESSION['contact_us_error_message'];
        unset($_SESSION['contact_us_error_message']);
?>
        <form style="width: 480px;" action="src/contactus/send_email.php" method="post">
             <div>
                 Name:
             <div></div>
                 <input type="text" name="name" id="name" size="15" value="" /><br />
             <div></div>
                 E-Mail:
             </div><div>
                 <input type="text" name="email" id="email" size="15" value=""/>
             <div></div>
                 Message:
             </div><div>
                 <textarea type="text" name="message" id="message" size="15" value=""></textarea>
             </div><br/><br/>
             <input type="submit" value="Submit" />
        </form>
<?
    } else {
?>

        <form style="width: 480px;" action="src/contactus/send_email.php" method="post">
              <div>
                  Name:
              <div></div>
                  <input type="text" name="name" id="name" size="15" value="" /><br />
              <div></div>
                  E-Mail:
              </div><div>
                  <input type="text" name="email" id="email" size="15" value=""/>
              <div></div>
                  Message:
              </div><div>
                  <textarea type="text" name="message" id="message" size="15" value=""></textarea>
              </div><br/><br/>
              <input type="submit" value="Submit" />
          </form>
<?
    }
?>