/**
 * Submit a query, using the query form for input
 */
function submitQuery(serverRoot) {
    var query = $("#query").val();
    showMessage("Searching for '" + query + "' ...");
    asyncLoad(serverRoot + 'search/' + query);
}

/**
 * Query, using directly given parameters
 *  @param  query       The query to search
 */
function query(query) {
    $("#query").val(query);
    submitQuery('http://' + window.location.host + '/');
}