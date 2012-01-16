<?php
/**
 * Created By: Tedesco
 * Date: Jan 14, 2011
 */
?>

<!--
    include flowplayer JavaScript file that does
    Flash embedding and provides the Flowplayer API.
-->
<script type="text/javascript" src="scripts/flowplayer/example/flowplayer-3.2.4.min.js"></script>

<script type="text/javascript">
flowplayer("player","/flowplayer/flowplayer-3.1.3.swf", {
clip: {
// stop at the first frame and start buffering
autoPlay: false,
autoBuffering: true
}
});
</script>