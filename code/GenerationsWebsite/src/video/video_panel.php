<?php

    //Build a hidden form to submit when we click one of the
    echo "<form name=\"hiddenform\" action=\"src/video/load_video_redirect.php\" method=\"post\" style=\"display: none;\"><input name=\"video_path\" type=\"text\" id=\"video_path\" style=\"display: none;\"></form>";


    if(isset($_SESSION['load_video_from_session'])){
        unset($_SESSION['load_video_from_session']);

        // Grab the path (without the file extension) of the video to play
        $video_path = $_SESSION['video_to_load'];

        // Get a connection to the database
        $database_connection = new MySqlRootDriver();

        // Lookup the song data from the video path
        $raw_video_path = str_replace(" ", "%20", $video_path);
        $query = "SELECT artist, name, description, Recording.resource_path FROM Recording, RecordingOf, Song WHERE Song.id=RecordingOf.song_id
                    AND Recording.id=RecordingOf.recording_id AND Recording.resource_path='$raw_video_path';";
        $video_data = $database_connection->query_database($query);
        $artist = (string) mysql_result($video_data, 0, 'artist');
        $song_name = (string) mysql_result($video_data, 0, 'name');
        $description = (string) mysql_result($video_data, 0, 'description');

        // Render the song data
?>
        <h3><?echo $song_name;?> by <?echo $artist;?></h3>
        <p><?echo $description;?></p>


        <div class="video-js-box">
          <center>
              <!-- this A tag is where your Flowplayer will be placed. it can be anywhere -->
              <a
                   href="<?echo $video_path;?>.flv"  
                   style="display:block;width:450px;height:338px; border: 1px #040726 solid;"
                   id="player">
              </a>

              <!-- this will install flowplayer inside previous A- tag. -->
              <script>
                  flowplayer("player", "scripts/flowplayer/flowplayer-3.2.5.swf", {
                  clip: {
                    // stop at the first frame and start buffering
                    autoPlay: false,
                    autoBuffering: true
                  }});
              </script>

          <!-- Download links provided for devices that can't play video in the browser. -->
          <p syle='margin-top:400px;'>
            <br/>
            <strong>Download Video:</strong>
            <a href="<?echo $video_path;?>.mp4">mp4</a>,
            <a href="<?echo $video_path;?>.avi">avi</a>,
            <a href="<?echo $video_path;?>.ogv">ogg</a><br>
          </p>
          </center>
        </div>

<?
    } else {
        echo "<h2>video recordings</h2>
              <p>Please select a song from the sidebar</p>";
    }
?>
