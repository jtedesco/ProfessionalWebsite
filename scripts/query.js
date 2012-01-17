/**
 * Submit a query, using the query form for input
 *  @param  serverRoot  The root URL of the server
 */
function submitQuery(serverRoot) {
    var query = $("#query").val();
    showMessage("Searching for '" + query + "' ...");
    asyncLoad(serverRoot + 'search/' + query);
}

/**
 * Query, using directly given parameters
 *  @param  query       The query to search
 *  @param  serverRoot  The root URL of the server
 */
function query(query, serverRoot) {
    $("#query").val(query);
    submitQuery(serverRoot);
}