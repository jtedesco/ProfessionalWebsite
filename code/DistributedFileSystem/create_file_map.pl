#!/usr/local/bin/perl

$numArgs = $#ARGV + 1;

if ($numArgs != 2) {
	print "usage: ./create.pl <num. of servers> <num. of files>\n" ;
    exit(1) ;
}

$num_servers = $ARGV[0] ;
$num_files = $ARGV[1] ;

if($num_servers < 4) {
    print "make servers more than 3\n" ;
	exit(1) ;
}

if($num_files < 16) {
    print "make files more than 15 \n" ;
	exit(1) ;
}

# create configuration file
open CONF_FILE, "+>", "file_map.conf" or die $! ;
print CONF_FILE "$num_servers\n" ;

# max size of a file 50KB
# min size of a file 1KB
$max_size_file = 50000 ;
$min_size_file = 1000 ;

# create random files
for ($file_id = 0; $file_id < $num_files; $file_id++) {
    my $file_size = int(rand($max_size_file - $min_size_file)) + $min_size_file ;

	my @chars=('a'..'z','A'..'Z','0'..'9','_');
	my $random_string;

    open FILE, "+>", "F".$file_id or die $! ;

	foreach (1..$file_size) 
	{
		# rand @chars will generate a random 
		# number between 0 and scalar @chars
		#$random_string.=$chars[rand @chars];
		print FILE $chars[rand @chars];
	}
    close(FILE) ;
}

for ($server_id = 0; $server_id < $num_servers; $server_id++) {
   $direc_name = "server_".$server_id ;

   if (-d "./".$direc_name) {
      #  there is a directory
      print " there is directory $direc_name\n" ;
      system("rm -rf $direc_name/*") ;
   }
   else {
      #  no directory
	  mkdir $direc_name, 0777 ;
   }
}

for ($file_id = 0; $file_id < $num_files; $file_id++) {
   print CONF_FILE "F$file_id " ;
   $first_server_id = int(rand($num_servers)) ;

   print CONF_FILE "$first_server_id " ;
   system("cp F$file_id server_$first_server_id") ;
   
   $flag = 1 ; 
   while($flag == 1) {
       $second_server_id = int(rand($num_servers-1)) ;
       if("$first_server_id" ne "$second_server_id") {
          print CONF_FILE "$second_server_id\n" ;
          system("cp F$file_id server_$second_server_id") ;
          $flag = 0 ;
       }
   }
}

mkdir client, 0777 unless -d "Client";

for ($file_id = 0; $file_id < $num_files; $file_id++) {

	system("rm -f F$file_id") ;
}

close(CONF_FILE) ;
