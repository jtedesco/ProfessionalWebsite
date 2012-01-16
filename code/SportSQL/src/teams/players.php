<?php

                        //Grab an instance of the database
                        $database_connection = new MySqlRootDriver();

                        //The page this page to reload to
                        $target_page = "my_team.php";

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

                            $data_renderer_data = new DataRendererData($table_name, $sort_column, $column_names,
                                $column_labels, $table_class, '', '', '', $database_connection, $target_page);

                            //Create a dynamic data renderer
                            $renderer = new SortableDataRenderer();

                            //Render the table!
                            echo
                            $renderer->renderResultsAsStyledTable($data_renderer_data);

                        } else {
                            //Get data
                            $user = $_SESSION['user'];
                            $team_name = $user . "\'s Team";
                            $database_driver = new MySqlRootDriver();
                            $data = $database_driver->query_database("SELECT name, position, real_team FROM consists_of, player
                            WHERE consists_of.player_id=player.player_id AND team_name='$team_name'");
                            //Build the data rendering data
                            //$table_name = $_SESSION['user'] . "players";
                            //$sort_column = "name";
                            $column_names = array("name","position","real_team");
                            $column_labels = array("Name","Position","Real Team");
                            $table_class = "smallstats";
                            $static_data_renderer = new StaticDataRenderer();
                            $static_data_renderer->renderResultsAsStyledTable($data, $column_names,
                                $column_labels, $table_class);

                            //Create a dynamic data renderer
                            //$renderer = new SortableDataRenderer();

                            //Render the table!
                            //$renderer->renderResultsAsStyledTable($data_renderer_data);
                        }

                    ?>