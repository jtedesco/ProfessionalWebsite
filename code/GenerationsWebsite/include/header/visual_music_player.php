<?php
/**
 * Created By: Tedesco
 * Date: Dec 15, 2010
 */
?>

<!-- soundManager.useFlashBlock: related CSS -->
<link rel="stylesheet" type="text/css" href="scripts/visual_music_player/flashblock.css" />

<!-- required -->
<link rel="stylesheet" type="text/css" href="scripts/visual_music_player/360player.css" />
<link rel="stylesheet" type="text/css" href="scripts/visual_music_player/360player-visualization.css" />

<!-- special IE-only canvas fix -->
<!--[if IE]>
    <script type="text/javascript" src="scripts/visual_music_player/script/excanvas.js"></script><![endif]-->

<!-- Apache-licensed animation library -->
<script type="text/javascript" src="scripts/visual_music_player/script/berniecode-animator.js"></script>

<!-- the core stuff -->
<script type="text/javascript" src="scripts/visual_music_player/script/soundmanager2.js"></script>
<script type="text/javascript" src="scripts/visual_music_player/script/360player.js"></script>

<script type="text/javascript">

soundManager.url = 'scripts/visual_music_player/swf/'; // path to directory containing SM2 SWF

soundManager.useFastPolling = true; // increased JS callback frequency, combined with useHighPerformance = true

threeSixtyPlayer.config.scaleFont = (navigator.userAgent.match(/msie/i)?false:true);
threeSixtyPlayer.config.showHMSTime = true;

// enable some spectrum stuffs

threeSixtyPlayer.config.useWaveformData = true;
threeSixtyPlayer.config.useEQData = true;

// enable this in SM2 as well, as needed

if (threeSixtyPlayer.config.useWaveformData) {
  soundManager.flash9Options.useWaveformData = true;
}
if (threeSixtyPlayer.config.useEQData) {
  soundManager.flash9Options.useEQData = true;
}
if (threeSixtyPlayer.config.usePeakData) {
  soundManager.flash9Options.usePeakData = true;
}

// favicon is expensive CPU-wise, but can be used.
if (window.location.href.match(/hifi/i)) {
  threeSixtyPlayer.config.useFavIcon = true;
}



</script>

<script type="text/javascript" src="scripts/simple_audio_player/audio-player.js"></script>
<script type="text/javascript">
    AudioPlayer.setup("scripts/simple_audio_player/player.swf", {
        width: 230,
        animation: 'no',
        transparentpagebg: 'yes',
        loader: '0099ff'
    });
</script>


