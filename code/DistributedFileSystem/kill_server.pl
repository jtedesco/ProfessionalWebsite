#!/usr/local/bin/perl

$numArgs = $#ARGV + 1;

if ($numArgs != 1) {
    print "usage: ./kill_server.pl <server name>\n" ;
    exit(1) ;
}

$server_id = $ARGV[0] ;


open(FILE, 'process_ids') or die("Could not open process_ids file.");
foreach $line (<FILE>) {
    chomp($line);

	@arr_words = split(/\s+/,$line) ;

	if( $server_id eq $arr_words[0]) {
    	system("kill -9 $arr_words[1]") ;
    	system("rm -f  server_$arr_words[0]/*") ;
    }
}
