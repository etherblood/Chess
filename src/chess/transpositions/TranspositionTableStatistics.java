package chess.transpositions;

/**
 *
 * @author Philipp
 */
public class TranspositionTableStatistics {

    public static long requests, hits;
    
    public static void soutHitrate() {
        System.out.println(100 * hits / requests + "% hitrate (" + hits + "/" + requests + ")");
    }
    
    public static void clear() {
        requests = hits = 0;
    }
    
}
