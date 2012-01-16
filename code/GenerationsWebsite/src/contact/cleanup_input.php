<?php
/**
 * Created by PhpStorm.
 * User: Jon
 * Date: Jan 12, 2011
 * Time: 7:51:57 PM
 * To change this template use File | Settings | File Templates.
 */

function cleanup_input($input) {

    // Remove slashes
    $input = stripcslashes($input);
    $input = stripslashes($input);

    // Remove semicolons & other coding escape characters
    $escape_characters = array("\"", "'", ";");
    $input = str_replace($escape_characters, "", $input);


    // Return the cleaned up input data
    return $input;
}
