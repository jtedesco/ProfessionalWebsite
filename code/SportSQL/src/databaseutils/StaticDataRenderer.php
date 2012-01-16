<?php

/**
 * Class that abstracts common rendering effects on data pulled from the database.
 *  For instance, utilities methods that return results in tables.
 */
 class StaticDataRenderer{

    /**
     *  Default constructor
     */
    function __construct(){
    }

    /**
     *  Static function that prints out the results as table, including the entire table tag.
     *      This function takes in the crucial parameters, like the style tag of the table,
     *      the data itself, and the names of the columns.
     *
     *  @param  data           The set of data from the query
     *  @param  column_names   The database names of the columns
     *  @param  column_titles  The names of the columns as they should appear on the website
     *  @param  table_class    The style tag to add as the class to the table 
     */
    public function renderResultsAsStyledTable($data, $column_names, $column_titles, $table_class){

        $this->renderResults($data, $column_names, $column_titles, $table_class, '', '', '');
    }

     /**
      *  Static function that prints out the results as table, including the entire table tag.
      *      This function takes in the crucial parameters, like the style tag of the table,
      *      the data itself, and the names of the columns.
      *
      *  @param  data               The set of data from the query
      *  @param  column_names       The database names of the columns
      *  @param  column_titles      The names of the columns as they should appear on the website
      */
     public function renderResultsAsTable($data, $column_names, $column_titles){

         $this->renderResults($data, $column_names, $column_titles, '', '', '', '');
     }

     /**
       *  Static function that prints out the results as table, including the entire table tag and a special highlighted
       *    row.  This function takes in the crucial parameters, like the style tag of the table, the data itself,
       *    the names of the columns, and the row index and style tag for the highlighted row.
       *
       *  @param  data                      The set of data from the query
       *  @param  column_names              The database names of the columns
       *  @param  column_titles             The names of the columns as they should appear on the website
       *  @param  table_class               The style tag to add as the class to the table
       *  @param  highlighted_column_name   The column by whose data to determine to highlight a row
       *  @param  highlighted_column_value  The value to look for in a column's value to determine whether to highlight it
       *  @param  $highlighted_class        The style tag (class) to give the selected row
       */
       public function renderResultsAsStyledTableWithHighlightedRow($data, $column_names, $column_titles, $table_class,
                                                                    $highlighted_column_name, $highlighted_column_value, $highlighted_class){

           $this->renderResults($data, $column_names, $column_titles, $table_class, $highlighted_column_name, $highlighted_column_value,
               $highlighted_class);
       }

     /**
       *  Static function that prints out the results as table, including the entire table tag and a special highlighted
       *    row.  This function takes in the crucial parameters, like the style tag of the table, the data itself,
       *    the names of the columns, and the row index and style tag for the highlighted row.
       *
       *  @param  data                      The set of data from the query
       *  @param  column_names              The database names of the columns
       *  @param  column_titles             The names of the columns as they should appear on the website
       *  @param  table_class               The style tag to add as the class to the table
       *  @param  highlighted_column_name   The column by whose data to determine to highlight a row
       *  @param  highlighted_column_value  The value to look for in a column's value to determine whether to highlight it
       *  @param  $highlighted_class        The style tag (class) to give the selected row
       */
       public function renderResults($data, $column_names, $column_titles, $table_class, $highlighted_column_name,
                                     $highlighted_column_value, $highlighted_class){

          //Print out table tag with style class
          echo "<table class='$table_class'>";

          //Get the number of rows in the data
          $num = mysql_numrows($data);

           //Print out the labels for the table
          echo "<thead>";
          foreach($column_titles as $title){
              echo "<td> $title </td>";
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
                  if($column_names[$column] == $highlighted_column_name && $value==$highlighted_column_value){
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