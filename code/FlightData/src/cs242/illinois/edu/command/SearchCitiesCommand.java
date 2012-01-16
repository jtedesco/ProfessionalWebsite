package cs242.illinois.edu.command;

import cs242.illinois.edu.graph.FlightGraphVertex;
import cs242.illinois.edu.graph.Graph;
import cs242.illinois.edu.graph.Vertex;
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
public class SearchCitiesCommand extends FlightGraphCommand {

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
        IndexWriter writer = null;
        try {
            writer = new IndexWriter(index, analyzer, true, IndexWriter.MaxFieldLength.UNLIMITED);
        } catch (IOException e) {
            TextIO.putln("Failed to initialize indexer");
            return false;
        }

        //Grab a list of cities
        List<Vertex> vertices = graph.getVertexList();
        List<FlightGraphVertex> cities = new ArrayList<FlightGraphVertex>();
        for (Vertex vertex : vertices) {
            cities.add((FlightGraphVertex) vertex);
        }

        //Build the index based on these cities
        for (FlightGraphVertex city : cities) {
            try {
                addCityToIndex(writer, city);
            } catch (IOException e) {
                TextIO.putln("Error created index from city data!");
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
            int hitsPerPage = 20;

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
                Document document = null;
                try {
                    document = searcher.doc(docId);
                } catch (IOException e) {
                    TextIO.putln("Error grabbing results!");
                    return false;
                }
                TextIO.putln((currentHit + 1) + ") " + document.get("name") + " (" + document.get("airportCode") + ")");
            }

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

    private void addCityToIndex(IndexWriter writer, FlightGraphVertex city) throws IOException {
        Document doc = new Document();
        String[] fields = getFields();
        doc.add(new Field(fields[0], city.getName(), Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field(fields[1], city.getContinent(), Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field(fields[2], city.getAirportCode(), Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field(fields[3], city.getCountry(), Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field(fields[4], city.getLatitude().toString(), Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field(fields[5], city.getLongitude().toString(), Field.Store.YES, Field.Index.ANALYZED));
        writer.addDocument(doc);
    }

    private String[] getFields(){
        String[] fields = new String[6];
        fields[0] = "name";
        fields[1] = "continent";
        fields[2] = "airportCode";
        fields[3] = "country";
        fields[4] = "longitude";
        fields[5] = "latitude";
        return fields;
    }
}
