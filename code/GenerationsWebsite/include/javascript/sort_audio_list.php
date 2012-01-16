<script language="JavaScript" type="text/javascript">
    function init() {
        select_audio_list_sorting("date");
    }
    window.onload = init;

    function select_audio_list_sorting(list_name) {
        hide_lists();
        showdiv(list_name+"_list");
        strip_classes();
        document.getElementById(list_name+"_link").className = "faux_link";
    }

    function hide_lists(){
        hidediv("date_list");
        hidediv("artist_list");
        hidediv("name_list");
    }

    function strip_classes(){
        document.getElementById("date_link").className = "";
        document.getElementById("artist_link").className = "";
        document.getElementById("name_link").className = "";
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