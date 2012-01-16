

<script type="text/javascript">
function adjustIFrameSize (iframeWindow) {
  if (iframeWindow.document.height) {
    var iframeElement = parent.document.getElementById(iframeWindow.name);
    iframeElement.style.height = iframeWindow.document.height + 'px';
    iframeElement.style.width = iframeWindow.document.width + 'px';
  }
  else if (document.all) {
    var iframeElement = parent.document.all[iframeWindow.name];
    if (iframeWindow.document.compatMode &&
        iframeWindow.document.compatMode != 'BackCompat')
    {
      iframeElement.style.height =
        iframeWindow.document.documentElement.scrollHeight + 5 + 'px';
      iframeElement.style.width =
        iframeWindow.document.documentElement.scrollWidth + 5 + 'px';
    }
    else {
      iframeElement.style.height = iframeWindow.document.body.scrollHeight + 5 + 'px';
      iframeElement.style.width = iframeWindow.document.body.scrollWidth + 5 + 'px';
    }
  }
}


function sleep(milliSeconds){
    // runs Java Applets sleep method
    document.devCheater.sleep(milliSeconds);
}

</script>