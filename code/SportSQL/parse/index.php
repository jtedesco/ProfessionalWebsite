<?php

$file = "player.xml";

function contents($parser, $data){
	echo $data;
}
function startTag($parser, $data){
}
function endTag($parser, $data){
	echo '<br \>';
}

$xml_parser = xml_parser_create();
xml_set_character_data_handler( $xml_parser, "contents" );
xml_set_element_handler($xml_parser, "startTag", "endTag");
$fp = fopen($file, "r");
$data = fread($fp, 80000);
if( !(xml_parse($xml_parser, $data, feof($fp)))){
	die("Error on line " . xml_get_current_line_number($xml_parser));
}

xml_parser_free($xml_parser);
fclose($fp);

?>