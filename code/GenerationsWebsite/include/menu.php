<?php
/**
 * Created By: Tedesco
 * Date: Dec 15, 2010
 */

function build_menu($current_page) {

    # Print out the opening tag for the menu
    echo "
        <div id='header' class='container'>
            <div id='menu'>
                <ul>
        ";

    
    if(strcasecmp('admin', $current_page)!=0){

        # Explicitly check each possibility of the current page
        if(strcasecmp('home', $current_page)==0){
            echo "<li class='active'><a href='home.php' accesskey='1' title='home'>home</a></li>";
        } else {
            echo "<li><a href='home.php' accesskey='1' title='home'>home</a></li>";
        }
        if(strcasecmp('history', $current_page)==0){
            echo "<li class='active'><a href='history.php' accesskey='1' title='history'>history</a></li>";
        } else {
            echo "<li><a href='history.php' accesskey='1' title='history'>history</a></li>";
        }
        if(strcasecmp('members', $current_page)==0){
            echo "<li class='active'><a href='members.php' accesskey='1' title='members'>members</a></li>";
        } else {
            echo "<li><a href='members.php' accesskey='1' title='members'>members</a></li>";
        }
        if(strcasecmp('songs', $current_page)==0){
            echo "<li class='active'><a href='songs.php' accesskey='1' title='songs'>songs</a></li>";
        } else {
            echo "<li><a href='songs.php' accesskey='1' title='songs'>songs</a></li>";
        }
        if(strcasecmp('events', $current_page)==0){
            echo "<li class='active'><a href='events.php' accesskey='1' title='events'>events</a></li>";
        } else {
            echo "<li><a href='events.php' accesskey='1' title='events'>events</a></li>";
        }
        if(strcasecmp('photos', $current_page)==0){
            echo "<li class='active'><a href='photos.php' accesskey='1' title='photos'>photos</a></li>";
        } else {
            echo "<li><a href='photos.php' accesskey='1' title='photos'>photos</a></li>";
        }
        if(strcasecmp('audio', $current_page)==0){
            echo "<li class='active'><a href='audio.php' accesskey='1' title='audio'>audio</a></li>";
        } else {
            echo "<li><a href='audio.php' accesskey='1' title='audio'>audio</a></li>";
        }
        if(strcasecmp('video', $current_page)==0){
            echo "<li class='active'><a href='video.php' accesskey='1' title='video'>video</a></li>";
        } else {
            echo "<li><a href='video.php' accesskey='1' title='video'>video</a></li>";
        }
        if(strcasecmp('locations', $current_page)==0){
            echo "<li class='active'><a href='locations.php' accesskey='1' title='locations'>locations</a></li>";
        } else {
            echo "<li><a href='locations.php' accesskey='1' title='locations'>locations</a></li>";
        }
        if(strcasecmp('contact', $current_page)==0){
            echo "<li class='active'><a href='contact.php' accesskey='1' title='contact'>contact</a></li>";
        } else {
            echo "<li><a href='contact.php' accesskey='1' title='contact'>contact</a></li>";
        }
    }
    echo "
               </ul>
            </div>
        </div>
        ";
}


