<script language="JavaScript" type="text/javascript">

    function submit_hidden_form (sortcolumn) {
      document.hiddenform.sortcolumn.value = sortcolumn;
      document.hiddenform.submit() ;
    }

    function display_events (event) {
      document.hiddenform.event.value = event;
      document.hiddenform.submit() ;
    }

    function display_video (video_path) {
      document.hiddenform.video_path.value = video_path;
      document.hiddenform.submit() ;
    }

    function display_photos (event_id) {
      document.hiddenform.event_id.value = event_id;
      document.hiddenform.submit() ;
    }

</script>