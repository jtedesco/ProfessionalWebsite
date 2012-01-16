package cs242.illinois.edu.command;

import cs242.illinois.edu.graph.Edge;
import cs242.illinois.edu.graph.FlightGraphEdge;
import cs242.illinois.edu.graph.FlightGraphVertex;
import cs242.illinois.edu.graph.Graph;
import cs242.illinois.edu.utilities.TextIO;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 3, 2010
 *
 * <p> <p> Command that allows you to search for a city using an indexing system
 */
public class SearchFlightsCommand extends FlightGraphCommand {

    /**
     * {@inheritDoc}
     *
     * This implementation allows us search for cities in our graph using an indexing system
     */
    public boolean execute(Graph graph) {

        //Error-check
         if(!super.execute(graph))
                 return false;

        // Specify the analyzer for tokenizing text.
        //    The same analyzer should be used for indexing and searching
        StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);

        // Create the index
        Directory index = new RAMDirectory();

        //Create a write to write the index
        IndexWriter writer;
        try {
            writer = new IndexWriter(index, analyzer, true, IndexWriter.MaxFieldLength.UNLIMITED);
        } catch (IOException e) {
            TextIO.putln("Failed to initialize indexer");
            return false;
        }

        //Grab a list of flights
        List<Edge> edges = graph.getEdgeList();
        List<FlightGraphEdge> flights = new ArrayList<FlightGraphEdge>();
        for (Edge edge : edges) {
            flights.add((FlightGraphEdge) edge);
        }

        //Build the index based on these flights
        for (FlightGraphEdge flight : flights) {
            try {
                addFlightToIndex(writer, flight);
            } catch (IOException e) {
                TextIO.putln("Error created index from flight data!");
                return false;
            }
        }
        try {
            writer.close();
        } catch (IOException e) {
            TextIO.putln("Error closing index writer!");
        }

        boolean shouldRepeat;
        IndexSearcher searcher = null;
        do {
            //Take a query
            TextIO.putln("Please enter a query:");
            String query = TextIO.getln().trim();
	        TextIO.putln();
            Query queryParser;
            try {
                queryParser = new MultiFieldQueryParser(Version.LUCENE_CURRENT, getFields(), analyzer).parse(query);
            } catch (ParseException e) {
                TextIO.putln("Error parsing query!");
                return false;
            }

            //Perform the search
            int hitsPerPage = Short.MAX_VALUE;

            TopScoreDocCollector collector;
            try {
                searcher = new IndexSearcher(index, true);
                collector = TopScoreDocCollector.create(hitsPerPage, true);
                searcher.search(queryParser, collector);
            } catch (IOException e) {
                TextIO.putln("Error performing query!");
                return false;
            }

            //Grab and rank results
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            System.out.println("Found " + hits.length + " results.");
            for (int currentHit = 0; currentHit < hits.length; ++currentHit) {
                int docId = hits[currentHit].doc;
                Document document;
                try {
                    document = searcher.doc(docId);
                } catch (IOException e) {
                    TextIO.putln("Error grabbing results!");
                    return false;
                }
                TextIO.putln((currentHit + 1) + ") " + document.get("from") + " --> " + document.get("to") + " (" + document.get("length") + " miles)");
            }
            TextIO.putln();

            //Would you like to search again?
            TextIO.putln("Would like to enter another query? (y/n)");
            String answer = TextIO.getln().trim();
            shouldRepeat = true;
            if (!(answer.toLowerCase().equals("y") || answer.toLowerCase().equals("yes"))) {
                shouldRepeat = false;
            }

        } while (shouldRepeat);

        //Close the searcher
        try {
            searcher.close();
        } catch (IOException e) {
            TextIO.putln("Error closing searcher!");
            return false;
        }

        //Success
        return true;
    }

    private void addFlightToIndex(IndexWriter writer, FlightGraphEdge flight) throws IOException {
        Document doc = new Document();
        String[] fields = getFields();
        FlightGraphVertex source = (FlightGraphVertex) flight.getSource();
        FlightGraphVertex target = (FlightGraphVertex) flight.getTarget();
        doc.add(new Field(fields[0], Integer.toString((int) flight.getLength()), Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field(fields[1], source.getAirportCode(), Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field(fields[2], target.getAirportCode(), Field.Store.YES, Field.Index.ANALYZED));
        writer.addDocument(doc);
    }

    private String[] getFields(){
        String[] fields = new String[3];
        fields[0] = "length";
        fields[1] = "from";
        fields[2] = "to";
        return fields;
    }
}
