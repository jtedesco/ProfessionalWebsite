<div id="menu">
    <ul id="main">
        <li><a href="index.php">Home</a></li>
        <li><a href="players.php">Players</a></li>
        <li><a href="matchups.php">Matchups</a></li>
        <li><a href="league.php">Standings</a></li>
        <?php
        if(isset($_SESSION['user']) && $_SESSION['user']=="admin"){
        	echo('
        	<li><a href="admin_panel.php">Admin</a></li>
        	');
        }else{
        	echo('
        	<li><a href="my_team.php">My Team</a></li>
        	');
        }
        ?>             
        <li><a href="my_account.php">My Account</a></li>
        <li><a href="contact_us.php">Contact Us</a></li>        
           </ul>
</div>
<div id="logo"></div>