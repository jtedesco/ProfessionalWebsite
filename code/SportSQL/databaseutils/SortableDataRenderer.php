<?php

/**
 * Class that abstracts common dynamic rendering effects on data pulled from the database.
 *  For instance, utilities methods that return results in tables.
 */
 class SortableDataRenderer{

    /**
     *  Default constructor
     */
    function __construct(){
    }

     /**
      *  Static function that prints out the results as table, including the entire table tag.
      *
      *  @param  renderer_data   DataRendererData    The data necessary for rendering a dynamic table
      */
     public function renderResultsAsTable($renderer_data){
         $this->renderResults($renderer_data->getTableName(), $renderer_data->getColumnNames(),
             $renderer_data->getColumnTitles(), '', '', '', '', $renderer_data->getDatabaseConnection(), 
             $renderer_data->getSortColumn(), $renderer_data->getTargetPage());
     }

    /**
     *  Static function that prints out the results as sortable table, including the entire table tag.
     *
     *  @param  renderer_data   DataRendererData    The data necessary for rendering a dynamic table
     */
    public function renderResultsAsStyledTable($renderer_data){

        $this->renderResults($renderer_data->getTableName(), $renderer_data->getColumnNames(),
            $renderer_data->getColumnTitles(), $renderer_data->getTableClass(), '', '', '',
            $renderer_data->getDatabaseConnection(), $renderer_data->getSortColumn(), $renderer_data->getTargetPage());
    }

     /**
       *  Static function that prints out the results as table, including the entire table tag and a special highlighted
       *    row.
       *
      *  @param  renderer_data   DataRendererData    The data necessary for rendering a dynamic table
       */
       public function renderResultsAsStyledTableWithHighlightedRow($renderer_data){

           $this->renderResults($renderer_data->getTableName(), $renderer_data->getColumnNames(),
               $renderer_data->getColumnTitles(), $renderer_data->getTableClass(),
               $renderer_data->getHighlightedColumnName(), $renderer_data->getHighlightedColumnValue(),
               $renderer_data->getHighlightedClass(), $renderer_data->getDatabaseConnection(),
               $renderer_data->getSortColumn(), $renderer_data->getTargetPage());
       }

     /**
       *  Static function that prints out the results as table, including the entire table tag and a special highlighted
       *    row.  This function takes in the crucial parameters, like the style tag of the table, the data itself,
       *    the names of the columns, and the row index and style tag for the highlighted row.
       *
       *  @param  table_name                String  The name of the table from which to grab this data
       *  @param  column_names              Array   The database names of the columns
       *  @param  column_titles             Array   The names of the columns as they should appear on the website
       *  @param  table_class               String  The style tag to add as the class to the table
       *  @param  highlighted_column_name   String  The column by whose data to determine to highlight a row
       *  @param  highlighted_column_value  String  The value to look for in a column's value to determine whether to highlight it
       *  @param  highlighted_class         String  The style tag (class) to give the selected row
       *  @param  database_connection       MySqlRootDriver   A connection to a MySQL database to perform the queries on
       *  @param  target_page               String  The url of the page to which to refresh on clicking sorting links
       */
       public function renderResults($table_name, $column_names, $column_titles, $table_class, $highlighted_column_name,
                                     $highlighted_column_value, $highlighted_class, $database_connection, $sort_column,
                                     $target_page){

          //Build a hidden form to submit when we click one of the
          echo "<form name=\"hiddenform\" action=\"src/databaseutils/SortableDataRedirect.php\" method=\"post\" style=\"display: none;\"><input name=\"sortcolumn\" type=\"text\" id=\"sortcolumn\" style=\"display: none;\"></form>";

          //Print out table tag with style class
          echo "<table class='$table_class'>";

          //Build a comma separated list of columns
          $column_list = '';
          foreach($column_names as $column_name){
              $column_list = $column_list.$column_name.', ';
          }
          $column_list = substr($column_list, 0, strlen($column_list)-2);

          //Store the render data in the session
          $_SESSION['render_data_table_name'] = $table_name;
          $_SESSION['render_data_column_names'] = $column_names;
          $_SESSION['render_data_column_titles'] = $column_titles;
          $_SESSION['render_data_table_class'] = $table_class;
          $_SESSION['render_data_highlighted_column_name'] = $highlighted_column_name;
          $_SESSION['render_data_highlighted_column_value'] = $highlighted_column_value;
          $_SESSION['render_data_highlighted_class'] = $highlighted_class;
          $_SESSION['target_page'] = $target_page;

          //Form the query and query the database
          $query = "SELECT ".$column_list." FROM ".$table_name." ORDER BY ".$sort_column.";";

          //Query the database
          $data = $database_connection->query_database($query);

          //Get the number of rows in the data
          $num = mysql_numrows($data);

          //Print out the labels for the table, with links to sort the page
          echo "<thead>";
          for($column_num = 0; $column_num < count($column_titles); $column_num++){
              $name = $column_names[$column_num];
              $title = $column_titles[$column_num];
              echo "<td><a href=\"javascript:submit_hidden_form('$name')\"/>$title</a></td>";
          }
          echo "</thead>";


          //Loop through all the results
          for($row = 0; $row < $num; $row++){

              //Get the number of columns
              $num_columns = count($column_names);

              //Loop through each column name, and determine whether to highlight this row
              $highlight_this_row = false;
              for($column = 0; $column < $num_columns; $column++){

                  $value = mysql_result($data, $row, $column_names[$column]);

                  //Check if this entry qualifies the row to be highlighted
                  if(strcmp($column_names[$column], $highlighted_column_name)==0 && strcmp($value, $highlighted_column_value)==0){
                      $highlight_this_row = true;
                  }
              }

              //Start the row, adding the special tag if this is the highlighted row
              if($highlight_this_row){
                  echo "<tr class='$highlighted_class'>";
              } else {
                  echo "<tr>";
              }

              //Loop through each column name, and print out its data
              for($column = 0; $column < $num_columns; $column++){

                  $value = mysql_result($data, $row, $column_names[$column]);
                  echo "<td> $value </td>";
              }

              //End the row
              echo "</tr>";
          }

          //Print ending tag
          echo "</table>";
      }
 }
?>