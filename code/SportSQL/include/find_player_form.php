<h2>Find Players</h2>
<br />
<?php
if(isset($_POST["position"])) $position = $_POST["position"];
else $position = "QB";
?>
<form action="manage_my_team_add_player.php" method="post">
	<div>
		Position:
	<div></div>
		<select name="position">
			<option <?php if($position == "QB")echo "selected=\"true\"";?>>QB</option>
			<option <?php if($position == "WR")echo "selected=\"true\"";?>>WR</option>
			<option <?php if($position == "RB")echo "selected=\"true\"";?>>RB</option>
			<option <?php if($position == "TE")echo "selected=\"true\"";?>>TE</option>
			<option <?php if($position == "K")echo "selected=\"true\"";?>>K</option>
			<option <?php if($position == "D")echo "selected=\"true\"";?>>D</option>
		</select>
	<input type="submit" value="Find Players" />
</form>