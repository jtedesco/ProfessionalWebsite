<?php
/**
 * User: Jon Tedesco
 * Date: Dec 10, 2010
 */




/**
 * Google analytics header and title for the page
 *
 *  @param  $title  String  The title for the header to render
 */
function meta_header($title){
       
    # First, display the title, css, and script links
    echo "
        <title>Generations | $title </title>
                
        <meta name='google-site-verification' content='oCfu-ROJQzECW7iBTolCG7A_XOT4L_TiS1THVN5B1lA' />
        <meta name='keywords' content='Generations, live, music, live music, blues, Chicago, Blues Brothers, Al Green, Ides of March, rock, classic rock, local, entertainment, Chicagoland, Generation, Generationz, generations, generationz, generation, generationschicago, generations of chicago' />
        <meta name='description' content='The homepage of Generations, an active Chicagoland blues cover band, that performs all things classic rock from Al Green and Queen to the Blues Brothers and Sam and Dave.  Homepage includes information about upcoming live performances, recordings, history, band members, and regular venues.' />
        <meta name='Author' content='Jon Tedesco' />
        <meta name='Copyright' content='Jon Tedesco' />
        <meta name='Publisher' content='Jon Tedesco' />
        <meta name='Page-Topic' content='Generations of Chicago' />
        <meta name='Page-Type' content='Live Music' />
        <meta name='Audience' content='Blues and Live Music Enthusiasts' />

		<link rel='icon' href='style/images/shared/favicon.ico' type='image/vnd.microsoft.icon'>

    ";
}