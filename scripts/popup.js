/**
 * The status of the popup
 */
var popupStatus = 0;

/**
 * Centers the popup
 */
function centerPopup(){

    //request data for centering
    var popupHeight = $("#popup").height();
    var popupWidth = $("#popup").width();

    //centering
    $("#popup").css({
        "position": "absolute",
        "top": window.innerHeight/2-popupHeight/2,
        "left": window.innerWidth/2-popupWidth/2
    });
    //only need force for IE6

    $("#backgroundPopup").css({
        "width": window.innerWidth,
        "height": window.innerHeight
    });
}

/**
 * Show search underway with a given message displaying.
 *
 *  @param  message The message to display
 */
function showMessage(message) {

    // Set the popup's text
    $("#popupText").text(message);
    centerPopup();

    // If the popup's not displayed, fade it in
    if(popupStatus == 0) {
        $("#backgroundPopup").fadeIn("slow");
        $("#popup").fadeIn("slow");
        popupStatus = 1;
    }
}

/**
 * Hide the progess display
 */
function hideProgress() {

    // If the popup's displayed, fade it out
    if(popupStatus == 1) {
        $("#backgroundPopup").fadeOut("slow");
        $("#popup").fadeOut("slow");
        popupStatus = 0;
    }
}