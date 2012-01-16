<div style="width:100%; margin: 20px 20px 20px 40px;">

    <?
        if(isset($_SESSION['email_list_thanks_message'])){

             echo $_SESSION['email_list_thanks_message'];
             unset($_SESSION['email_list_thanks_message']);

             echo "<form style=\"width: 100%;\"></form>";

        } else if(isset($_SESSION['email_list_error_message'])){
    ?>
            <h2 style="margin-bottom: 3px;">Subscribe to Our E-mail List</h2>
            <h4 style="margin-top: 3px;">Get regular updates of our schedule. <br/> We respect your privacy, and you can unsubscribe at any time.</h4>
            
    <?
            echo $_SESSION['email_list_error_message'];
            unset($_SESSION['email_list_error_message']);
    ?>

            <form style="width: 500px;" action="src/contact/subscribe.php" method="post">
                 <div>
                     First Name:
                 </div><div>
                    <input type="text" name="first_name" size="15" value='<? echo $_SESSION["first_name"]; ?>'/>
                 </div><div>
                   Last Name:
                 </div><div>
                    <input type="text" name="last_name" size="15" value='<? echo $_SESSION["last_name"]; ?>'/>
                 </div><div>
                    E-Mail: <b>*</b>
                 </div><div>
                    <input type="text" name="email" size="15" value='<? echo $_SESSION["email"]; ?>'/>
                 </div>
                <center>
                   <input class="submit" type="submit" value="Submit" />
                </center>
            </form>
    <?
        } else {
    ?>

        <h2 style="margin-bottom: 3px;">Subscribe to Our E-mail List</h2>
        <h4 style="margin-top: 3px;">Get regular updates of our schedule. <br/> We respect your privacy, and you can unsubscribe at any time.</h4>

        <form style="width: 500px;" action="src/contact/subscribe.php" method="post">
            <div>
                First Name:
            </div><div>
               <input type="text" name="first_name" size="15"/>
            </div><div>
              Last Name:
            </div><div>
               <input type="text" name="last_name" size="15"/>
            </div><div>
               E-Mail: <b>*</b>
            </div><div>
               <input type="text" name="email" size="15"/>
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