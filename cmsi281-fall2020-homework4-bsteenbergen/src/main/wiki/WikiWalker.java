package main.wiki;

import java.util.*;

/**
 * A simplified version of some Web Crawler tools that analyze links between 
 * a given domain's pages.
 * @author Brittany Steenbergen
 *
 */
public class WikiWalker {

    HashMap<String, TreeMap<String, Integer>> wikiMap;
    HashSet<String> contained;

    public WikiWalker() {
        this.wikiMap = new HashMap<String, TreeMap<String, Integer>>();
        this.contained = new HashSet<String>();
    }

    /**
     * Adds an article with the given name to the site map and associates the
     * given linked articles found on the page. Duplicate links in that list are
     * ignored, as should an article's links to itself.
     * 
     * @param articleName
     *            The name of the page's article
     * @param articleLinks
     *            List of names for those articles linked on the page
     */
    public void addArticle(String articleName, List<String> articleLinks) {
        TreeMap<String, Integer> innerMap = new TreeMap<>();
        contained.add(articleName);
        for (String s : articleLinks) {
            innerMap.put(s, 0);
            contained.add(s);
        }
        wikiMap.put(articleName, innerMap);
    }

    /**
     * Determines whether or not, based on the added articles with their links,
     * there is *some* sequence of links that could be followed to take the user
     * from the source article to the destination.
     * 
     * @param src
     *            The beginning article of the possible path
     * @param dest
     *            The end article along a possible path
     * @return boolean representing whether or not that path exists
     */
    public boolean hasPath(String src, String dest) {
        Set<String> visited = new HashSet<>();
        Queue<String> frontier = new LinkedList<>();
        return hasPath(src, dest, frontier, visited);
    }

    /**
     * Increments the click counts of each link along some trajectory. For
     * instance, a trajectory of ["A", "B", "C"] will increment the click count
     * of the "B" link on the "A" page, and the count of the "C" link on the "B"
     * page. Assume that all given trajectories are valid, meaning that a link
     * exists from page i to i+1 for each index i
     * 
     * @param traj
     *            A sequence of a user's page clicks; must be at least 2 article
     *            names in length
     */
    public void logTrajectory(List<String> traj) {
        if (traj.size() <= 1) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < traj.size() - 1; i++) {
            String current = traj.get(i);
            String next = traj.get(i + 1);  
            int mapValue = wikiMap.get(current).get(next);
            wikiMap.get(current).put(next, mapValue + 1);
        }
    }

    /**
     * Returns the number of clickthroughs recorded from the src article to the
     * destination article. If the destination article is not a link directly
     * reachable from the src, returns -1.
     * 
     * @param src
     *            The article on which the clickthrough occurs.
     * @param dest
     *            The article requested by the clickthrough.
     * @throws IllegalArgumentException
     *             if src isn't in site map
     * @return The number of times the destination has been requested from the
     *         source.
     */
    public int clickthroughs(String src, String dest) {
        if (!wikiMap.containsKey(src)) {
            throw new IllegalArgumentException();
        }
        if (wikiMap.get(src).containsKey(dest)) {
            return wikiMap.get(src).get(dest);
        }
        else { return -1; }
    }

    /**
     * Based on the pattern of clickthrough trajectories recorded by this
     * WikiWalker, returns the most likely trajectory of k clickthroughs
     * starting at (but not including in the output) the given src article.
     * Duplicates and cycles are possible outputs along a most likely trajectory. In
     * the event of a tie in max clickthrough "weight," this method will choose
     * the link earliest in the ascending alphabetic order of those tied.
     * 
     * @param src
     *            The starting article of the trajectory (which will not be
     *            included in the output)
     * @param k
     *            The maximum length of the desired trajectory (though may be
     *            shorter in the case that the trajectory ends with a terminal
     *            article).
     * @return A List containing the ordered article names of the most likely
     *         trajectory starting at src.
     */
    public List<String> mostLikelyTrajectory(String src, int k) {
        List<String> visited = new ArrayList<>();
        return mostLikelyTrajectory(src, k, visited);
    }

    // -----------------------------------------------------------
    // Helper Methods
    // -----------------------------------------------------------
    
    /**
     * Recursively determines whether or not there is a sequence of links that could be followed 
     * to take the user from the source article to the destination.
     * 
     * @param dest
     *            The end article along a possible path
     * @param innerMap
     *            The map contained inside the src
     * @param visited
     *            A set of links that have already been visited to prevent a loop
     * @return boolean representing whether or not that path exists
     */
    private boolean hasPath(String src, String dest, Queue<String> frontier, Set<String> visited) {
        if (!contained.contains(src) || !contained.contains(dest)) {
            return false;
        }
        if (visited.contains(src)) {
            return false;
        }
        
        frontier.add(src);
        while (!frontier.isEmpty()) {
            String current = frontier.remove();
            if (current.equals(dest)) {
                return true;
            }
            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);
            if (wikiMap.containsKey(src)) {
                for (Map.Entry<String, Integer> entry : wikiMap.get(src).entrySet()) {
                    frontier.add(entry.getKey());
                }
            }
            if (hasPath(frontier.peek(), dest, frontier, visited)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Recursively returns the most likely trajectory of k clickthroughs
     * starting at the src article. Method will choose the link that is 
     * alphabetically first in the event of a tie in max clickthrough weight.
     * 
     * @param src
     *            The starting article of the trajectory
     * @param k
     *            The maximum length of the desired trajectory (though may be
     *            shorter in the case that the trajectory ends with a terminal
     *            article).
     * @param visited
     *            A list containing the sequential strings that have been visited
     *            which is returned once k or a terminal article is reached
     * @return A List containing the ordered article names of the most likely
     *         trajectory starting at src.
     */
    private List<String> mostLikelyTrajectory(String src, int k, List<String> visited) {
        TreeMap<String, Integer> current = wikiMap.get(src);
        if (current == null) {
            return visited;
        }
        if (k <= 0) {
            return visited;
        }
        
        String maxArticle = current.firstKey();
        int maxClicks = current.get(maxArticle);
        
        if (wikiMap.containsKey(src)) {
            for (Map.Entry<String, Integer> entry : current.entrySet()) {
                if (entry.getValue() > maxClicks) {
                  visited.add(entry.getKey());
                  maxClicks = entry.getValue();
                  maxArticle = entry.getKey();
                }
            }
        }
        if (maxArticle.equals(current.firstKey())) {
            visited.add(maxArticle);
        }
        return mostLikelyTrajectory(maxArticle, k - 1, visited);
    }
}
