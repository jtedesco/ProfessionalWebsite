<?php
/**
 * User: Jon
 * Date: Jan 15, 2011
 */
?>

<script type="text/javascript" src="http://mediaplayer.yahoo.com/latest"></script>

<script type="text/javascript">
   // Disable Yahoo! Media Player search links
   function apiReadyHandler(){
      YAHOO.MediaPlayer.onMediaUpdate.subscribe(DisableYahooLinks);
      YAHOO.MediaPlayer.onTrackStart.subscribe(DisableYahooLinks);
      setTimeout(DisableYahooLinks, 2000);
       document.getElementById('ymp-btn-tray').style.display = 'none';
   }

   function DisableYahooLinks(){
      DisableLink(document.getElementById('ymp-meta-track-title'));
      DisableLink(document.getElementById('ymp-meta-artist-title'));
      DisableLink(document.getElementById('ymp-meta-image'));
      DisableLink(document.getElementById('ymp-meta-album-title'));
   }

   function DisableLink(el){
      if(el){
         el.href = '';
         el.onclick = function(){ return false; };
      }
   }

   YAHOO.MediaPlayer.onAPIReady.subscribe(apiReadyHandler);
</script>

<link href='scripts/yahoo_media_player/yahoo_media_player.css' rel='stylesheet' type='text/css' media='all' />
