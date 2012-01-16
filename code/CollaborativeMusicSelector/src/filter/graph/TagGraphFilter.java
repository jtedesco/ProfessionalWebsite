package filter.graph;

import entity.SongDirectory;
import entity.User;
import entity.UserDAO;
import filter.TagFilter;
import filter.TagType;
import org.jgraph.JGraph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.FloydWarshallShortestPaths;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.SimpleWeightedGraph;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jon
 * Date: 4/14/11
 * Time: 10:21 PM
 *
 * This class holds a bipartite graph of user nodes and tag nodes, and on request for a 'best representative' set of tags
 * of some set of user nodes, provides all tag nodes in the top <code>threshold</code> of tag nodes.
 */
public class TagGraphFilter implements TagFilter {

    /**
     * The bipartite graph for holding the node and tag data. See <see>Graph</see>.
     */
    private SimpleWeightedGraph<TagGraphVertex, TagGraphEdge> tagGraph;

    /**
     * The database data behind this graph
     */
    private final SongDirectory songDirectory;

    /**
     * The percentage of 'best' vertices to take from the graph, based on 'bary' values
     */
    private final double thresholdPercentage;

    /**
     * Initialize this tag graph
     *
     *  @param  thresholdPercentage     The percentage of top results to return
     */
    public TagGraphFilter(double thresholdPercentage) {
        songDirectory = new SongDirectory(Integer.MAX_VALUE);
        this.thresholdPercentage = thresholdPercentage;
    }

    /**
     * Initialize this tag graph
     *
     * @param  songDirectory           The directory of songs from the database
     * @param  thresholdPercentage     The percentage of top results to return
     */
    public TagGraphFilter(SongDirectory songDirectory, double thresholdPercentage) {
        this.songDirectory = songDirectory;
        this.thresholdPercentage = thresholdPercentage;
    }

    /**
     * This function, given a list of users, will return the best <code>thresholdPercentage</code> tag nodes based
     * on bary values from our graph structure, sorted in descending order of bary weights.
     *
     *  @param  userIds The list of users whose common interests we're looking to match
     *  @return         The list of tags that best represents the given list of users, in descending order of relevance
     */
    public List<String> findBestRepresentativeTags(List<String> userIds) {

        // Get the list of users with these ids
        UserDAO userDAO = new UserDAO(songDirectory);
        List<User> users = userDAO.retrieveUsersByUserIDs(userIds);


        // Build the tag graph given the latest user data from the database
        buildTagGraph(users);

        // Now, find the bary weight of each tag vertex of the graph
        computeBaryWeights();

        // Iterate through the vertices, and add vertices to a priority queue based on
        //  vertex bary weight
        PriorityQueue<Tag> queue = new PriorityQueue<Tag>();
        for(TagGraphVertex vertex : tagGraph.vertexSet()) {
            if(vertex instanceof Tag) {
                queue.add((Tag) vertex);
            }
        }

        // Iterate through the priority queue, and keeping taking the lowest weighted tag
        //  vertices until our target number of vertices is met
        List<String> bestRepresentativeTags = new LinkedList<String>();
        int desiredNumberOfTags = (int) ((thresholdPercentage / 100) * (tagGraph.vertexSet().size() - users.size()));
        int tagsFound = 0;
        Iterator<Tag> tagIterator = queue.iterator();
        while(tagsFound<desiredNumberOfTags && tagIterator.hasNext()) {
            bestRepresentativeTags.add(tagIterator.next().getTagValue());
        }

        // Return the best representative set of tags!
        return bestRepresentativeTags;
    }

    public List<String> findBestRepresentativeTagsNonWeighted(List<String> userIds) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Helper function to build the graph using the most recent data from the database.
     *
     * @param users The latest set of users from the database
     */
    private void buildTagGraph(List<User> users) {

        // Build an empty graph
        tagGraph = new SimpleWeightedGraph<TagGraphVertex, TagGraphEdge>(TagGraphEdge.class);

        // Output the graph
        JGraphModelAdapter<TagGraphVertex, TagGraphEdge> adapter = new JGraphModelAdapter<TagGraphVertex, TagGraphEdge>(tagGraph);
        JGraph visualGraph = new JGraph(adapter);
        visualGraph.setBackground(Color.decode( "#FAFBFF" ) );

        // Add all users as nodes in the graph (initially with node weight 0)
        for(User user : users) {
            tagGraph.addVertex(user);
        }

        // Keep a set of tags that we have added to the graph
        HashMap<String, Tag> tagsAdded = new HashMap<String, Tag>();

        // Add an edge for each association of a user to a tag, giving relative weights for tag
        //  types based on artist, album, or song association.
        for(User user : users){

            // Add artist, album, song tags to the graph
            addTagData(tagsAdded, user, TagType.artist, user.getArtistTags());
            addTagData(tagsAdded, user, TagType.album, user.getAlbumTags());
            addTagData(tagsAdded, user, TagType.song, user.getSongTags());
        }

        BufferedImage newImage = visualGraph.getImage(new Color(255, 255, 255), 0);

        try {
            ImageIO.write(newImage, "png", new File("test"));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     * Compute the bary weights of all vertices in the graph
     */
    private void computeBaryWeights() {

        // Build the shortest paths using the Floyd-Warshall algorithm
        FloydWarshallShortestPaths<TagGraphVertex, TagGraphEdge> shortestPathsGraph = new FloydWarshallShortestPaths<TagGraphVertex, TagGraphEdge>(tagGraph);

        // Calculate the bary center for each vertex in the graph
        for(TagGraphVertex tagGraphVertex : tagGraph.vertexSet()) {

            // Get the shortest paths to all other vertices of the graph
            List<GraphPath<TagGraphVertex, TagGraphEdge>> shortestPathsToAllOtherVertices = shortestPathsGraph.getShortestPaths(tagGraphVertex);
            for(GraphPath<TagGraphVertex, TagGraphEdge> shortestPath : shortestPathsToAllOtherVertices) {

                // Add the weight of this path to this vertex's bary weight
                tagGraphVertex.setBaryValue(tagGraphVertex.getBaryValue() + shortestPath.getWeight());
            }
        }
    }

    /**
     * Helper function to add the tags of some user to the given graph, of some type, as denoted by
     *  <code>type</code>.
     *
     * @param tagsAdded     The hash table, indexed by tag value, of tags already added to the graph
     * @param user          The user whose tags we are adding to the graph
     * @param type          The type of tags we are adding
     * @param tags          The set of tags to add
     */
    private void addTagData(HashMap<String, Tag> tagsAdded, User user, TagType type, Set<String> tags) {

        // Add all tag nodes and edges corresponding to artist tags for this user
        for(String artistTag : tags){

            // Add the tag node if it doesn't exist
            if(!tagsAdded.keySet().contains(artistTag)){
                Tag newTag = new Tag(artistTag);
                tagGraph.addVertex(newTag);
                tagsAdded.put(artistTag, newTag);
            }

            // Add an edge between this user and tag, of corresponding weight
            TagGraphEdge tagGraphEdge = new TagGraphEdge(type);
            tagGraph.addEdge(user, tagsAdded.get(artistTag), tagGraphEdge);
            tagGraph.setEdgeWeight(tagGraphEdge, tagGraphEdge.getTagType().getWeight());
        }
    }

    public SimpleWeightedGraph<TagGraphVertex, TagGraphEdge> getTagGraph() {
        return tagGraph;
    }

    public void setTagGraph(SimpleWeightedGraph<TagGraphVertex, TagGraphEdge> tagGraph) {
        this.tagGraph = tagGraph;
    }

    public SongDirectory getSongDirectory() {
        return songDirectory;
    }

    public double getThresholdPercentage() {
        return thresholdPercentage;
    }
}