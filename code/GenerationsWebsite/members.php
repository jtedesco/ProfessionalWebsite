<?php @session_start();

    # Include PHP scripts
    require_once("include/header/meta_header.php");
    require_once("src/members/member_biographies.php");
    require_once("include/javascript/collapsible_panel_scripts.php");
    require_once("include/menu.php");
?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>

<?
    /**
     * Created By: Jon Tedesco
     * Date: Dec 10, 2010
     */

    # Meta data, including the title
    meta_header('Members');

    # CSS and javascript
    include('include/header/common_css.php');
    include('include/header/common_javascript.php');
    include("include/header/collapsible_panel.php");
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
        build_menu('members');
    ?>

    <div id="page" class="container">
        <div id="content">
            <div class="box3">

<?
                # Build the member bios for the page
                $number = build_member_biographies();

?>

    	    </div>
        </div>
<?
        # Include the Facebook 'like' button
        echo "<div style='margin-bottom: 20px;'>";
        include('include/widgets/facebook_like_button.php');
        
?>
    </div>

    <?
        # The footer
    include('include/footer.php');
    ?>

</div>

<?
    # Render collapsible panel scripts
    build_collapsible_panel_script($number);
?>

</body>
</html>



