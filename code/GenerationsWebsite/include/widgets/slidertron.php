<?php
/**
 * Created By: Tedesco
 * Date: Dec 15, 2010
 */

    
function build_slidertron($pictures){
    build_styled_slidertron($pictures, "", "550px", "240px");
}

function build_styled_slidertron($pictures, $style_tags, $width, $height){

    # Display opening tags for the slideshow
    echo "
        <div id='slideshow' style='$style_tags; width: $width; height: $height;'>
            <div class='viewer' style='width: $width; height: $height;'>
                <div class='reel' style='width: $width; height: $height;'>
        ";

    # Loop through list of pictures and add each picture to the slideshow
    foreach ($pictures as $picture){
        echo "
                <div class='slide' ><img src= $picture alt='' width='$width' height='$height' /> </div>
        ";

    }

    # Display closing tags for the slideshow
    echo "
            </div>
        </div>
        <div class='navigation' ><a href='#' class='previous'>[ &lt; ]</a> <a href='#' class='next'>[ &gt; ]</a></div>
    </div>
    ";
}

