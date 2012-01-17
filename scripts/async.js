/**
 * Simply utilities to make entire site asynchronous
 */

// Keep track of the last HTML block loaded (avoid reloading)
var lastHTML = null;

/**
 * Submit an AJAX request for the given URL
 * @param url The URL to laod
 */
function asyncLoad(url) {
    $.get(url, function (data) {

        // Parse the return information
        var parsedData = $(data);
        var contentHTML = parsedData.find("#content").find(".bgbtm").html();

        // Find the page's title
        titleStart = data.indexOf("<title>") + 7;
        titleEnd = data.indexOf("</title>");
        var pageTitle = data.substring(titleStart, titleEnd);
        pageTitle = $("<div/>").html(pageTitle).text();

        // Smoothly load it into the current page
        asyncLoadCallback(contentHTML, pageTitle);
    });
}

/**
 * Do everything we could possibly need to do to render the page
 */
function preparePage() {

    $(".toHide").hide();
    showPage(1, 10, true);
    SyntaxHighlighter.highlight();
}

function asyncLoadCallback(content, title) {

    // Check that we're not reloading data
    if (content != lastHTML) {

        lastHTML = content;

        // Get the content to be reloaded & fade it in
        $("#content").find(".bgbtm").fadeOut('fast', function () {
            $("#content").find(".bgbtm").html(content);
            preparePage();
            $("#content").find(".bgbtm").fadeIn('fast');

            // Change the page title
            document.title = unescape(title);
        });
    }

    // Close the popup
    if (popupStatus == 1) {
        hideProgress();
    }
}
