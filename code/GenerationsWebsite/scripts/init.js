$(document).ready(function(){
	if ($('#slideshow').length > 0) {
		$('#slideshow').slidertron({
			viewerSelector:			'.viewer',
			reelSelector:			'.viewer .reel',
			slidesSelector:			'.viewer .reel .slide',
			navPreviousSelector:	'.previous',
			navNextSelector:		'.next'
		});
	}
});