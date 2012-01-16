<script language="JavaScript" type="text/javascript">

    function load_audio(recording_id) {
        var header = document.getElementById('default_header');
        header.style.display='none';
        hide_song_data();
        showdiv(recording_id);
    }

    function hide_song_data(){
        var song_data_wrapper = document.getElementById('song_data');

        // Iterate over divs inside of this
        var containedDivElements = song_data_wrapper.getElementsByTagName("div");
        for (var i = 0; i < containedDivElements.length; i++) {
            var this_div = containedDivElements[i];
            this_div.style.display = 'none';
        }
    }

    function hidediv(id) {
        //safe function to hide an element with a specified id
        if (document.getElementById) { // DOM3 = IE5, NS6
            document.getElementById(id).style.display = 'none';
        }
        else {
            if (document.layers) { // Netscape 4
                document.id.display = 'none';
            }
            else { // IE 4
                document.all.id.style.display = 'none';
            }
        }
    }

    function showdiv(id) {
        //safe function to show an element with a specified id

        if (document.getElementById) { // DOM3 = IE5, NS6
            document.getElementById(id).style.display = 'block';
        }
        else {
            if (document.layers) { // Netscape 4
                document.id.display = 'block';
            }
            else { // IE 4
                document.all.id.style.display = 'block';
            }
        }
    }
</script>