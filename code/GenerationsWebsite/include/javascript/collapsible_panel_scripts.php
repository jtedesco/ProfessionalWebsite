<?php
/**
 * User: Jon
 * Date: Dec 27, 2010
 * Time: 1:18:53 PM
 */

function build_collapsible_panel_script($number) {

    # Opening tag for script
    echo "<script type='text/javascript'>";

    # Render each portion of the script
    for($panel = 0; $panel < $number; $panel++){
        echo "var CollapsiblePanel$panel = new Spry.Widget.CollapsiblePanel('CollapsiblePanel$panel', {contentIsOpen:false, enableAnimation:true});";
    }

    # Closing tag for script
    echo "</script>";
}