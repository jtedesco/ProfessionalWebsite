<?php

/**
 *  This class is used simply holds the data necessary for rendering a sortable table. Presumably, this will be stored in
 *    the session variable to allow the table to be rendered, and contain links that will re-render, and sort, the data.
 */
class DataRendererData {

    /**
     * @var String  The name of the table from which to grab this data
     */
    public $tableName;

    /**
     * @var String  The name of the column by which to sort
     */
    public $sortColumn;

    /**
     * @var Array   The names of the columns of the table
     */
    public $columnNames;

    /**
     * @var Array   The names of the columns to be displayed
     */
    public $columnTitles;

    /**
     * @var String  The style tag with which to render the table
     */
    public $tableClass;

    /**
     * @var String  The name of the column on which to decide whether to highlight a row
     */
    public $highlightedColumnName;

    /**
     * @var String  The value, which if found in the required column, highlights the row
     */
    public $highlightedColumnValue;

    /**
     * @var String  The style tag for highlighting the table
     */
    public $highlightedClass;

    /**
     * @var MySqlRootDriver   A connection to a MySQL database to perform the queries on
     */
    public $databaseConnection;

    /**
     * @var String  The url of the page to receive refresh requests
     */
    public $targetPage;

    /**
     * Builds this data renderer data object
     *
     * @param  $tableName                  String  The name of the table from which to
     * @param  $sortColumn                 String  The name of the column by which to sort
     * @param  $columnNames                Array   The names of the columns in the database
     * @param  $columnTitles               Array   The titles of the colums to be displayed
     * @param  $tableClass                 String  The style tag to use on the table when rendering this table
     * @param  $highlightedColumnCame      String  The name of the column on which to decide whether to highlight a row
     * @param  $highlightedColumnValue     String  The value, which if found in the required column, highlights the row
     * @param  $highlightedClass           String  The style tag for highlighting the table
     * @param  $databaseConnection         MySqlRootDriver   The database object on which to perform this new query
     * @param  $targetPage                 String  The url of the page to receive refresh requests
     */
    public function __construct($tableName, $sortColumn, $columnNames, $columnTitles, $tableClass, $highlightedColumnName,
                         $highlightedColumnValue, $highlightedClass, $databaseConnection, $targetPage) {

        $this->tableName = $tableName;
        $this->sortColumn = $sortColumn;
        $this->columnNames = $columnNames;
        $this->columnTitles = $columnTitles;
        $this->tableClass = $tableClass;
        $this->highlightedColumnName = $highlightedColumnName;
        $this->highlightedColumnValue = $highlightedColumnValue;
        $this->highlightedClass = $highlightedClass;
        $this->databaseConnection = $databaseConnection;
        $this->targetPage = $targetPage;
    }

    /**
     * @return String   The table name
     */
    public function getTableName(){
        return $this->tableName;
    }

    /**
     * @return String   The column by which to sort
     */
    public function getSortColumn(){
        return $this->sortColumn;
    }

    /**
     * @return Array    The array of column names
     */
    public function getColumnNames(){
        return $this->columnNames;
    }

    /**
     * @return Array    The array of column titles
     */
    public function getColumnTitles(){
        return $this->columnTitles;
    }

    /**
     * @return String   The class for the table
     */
    public function getTableClass(){
        return $this->tableClass;
    }

    /**
     * @return String   he name of the column on which to decide whether to highlight a row
     */
    public function getHighlightedColumnName(){
        return $this->highlightedColumnName;
    }

    /**
     * @return String   The value, which if found in the required column, highlights the row
     */
    public function getHighlightedColumnValue(){
        return $this->highlightedColumnValue;
    }

    /**
     * @return String   The style tag to give a 'highlighted' row
     */
    public function getHighlightedClass(){
        return $this->highlightedClass;
    }

    /**
     * @return MySqlRootDriver    The connection to a MySQL database 
     */
    public function getDatabaseConnection(){
        return $this->databaseConnection;
    }

    /**
     * @return String   The url of the page to handle refresh requests
     */
    public function getTargetPage(){
        return $this->targetPage;
    }
}
