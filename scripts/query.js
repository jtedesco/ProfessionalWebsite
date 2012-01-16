/**
 * Submit a query
 */
function submitQuery(serverRoot) {
    var query = $("#query").val();
    showMessage("Searching for '" + query + "'...");
    asyncLoad(serverRoot + 'search/' + query);
}
