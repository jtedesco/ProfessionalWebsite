<? session_start();

require_once("src/utils/mysql_root_driver.php");
require_once("src/utils/sortable_table_renderer.php");
require_once("src/utils/data_renderer_data.php");

function build_song_list(){

    echo "<center><h1>Generations Song List</h1></center><br/>";

    // Grab an instance of the database
    $database_connection = new MySqlRootDriver();

    //The page this page to reload to
    $target_page = "songs.php";

    //If we're supposed to grab the data from the session, grab it, otherwise, build it
    if(isset($_SESSION['render_data_from_session'])){
        unset($_SESSION['render_data_from_session']);


        //Grab the session object holding the render data
        $table_name = $_SESSION['render_data_table_name'];
        $sort_column = $_SESSION['render_data_sort_column'];
        $column_names = $_SESSION['render_data_column_names'];
        $column_labels = $_SESSION['render_data_column_titles'];
        $table_class = $_SESSION['render_data_table_class'];

        //Erase the session data
        unset($_SESSION['render_data_table_name']);
        unset($_SESSION['render_data_sort_column']);
        unset($_SESSION['render_data_column_names']);
        unset($_SESSION['render_data_column_titles']);
        unset($_SESSION['render_data_table_class']);
        unset($_SESSION['render_data_highlighted_column_name']);
        unset($_SESSION['render_data_highlighted_column_value']);
        unset($_SESSION['render_data_highlighted_class']);

        //Render the table!
        $data_renderer_data = new DataRendererData($table_name, $sort_column, $column_names,
            $column_labels, $table_class, '', '', '', $database_connection, $target_page);

        //Create a dynamic data renderer
        $renderer = new SortableDataRenderer();

        //Render the table!
        $renderer->renderResultsAsStyledTableWithHighlightedRow($data_renderer_data);

    } else {

        //Build the data rendering data
        $table_name = "Song";
        $sort_column = "artist ASC";
        $column_names = array("artist", "name");
        $column_labels = array("Artist", "Name");
        $table_class = "songlist";

        //Render the table
        $data_renderer_data = new DataRendererData($table_name, $sort_column, $column_names,
            $column_labels, $table_class, '', '', '', $database_connection, $target_page);

        //Create a dynamic data renderer
        $renderer = new SortableDataRenderer();

        //Render the data
        $renderer->renderResultsAsStyledTableWithHighlightedRow($data_renderer_data);
    }
}
?>
