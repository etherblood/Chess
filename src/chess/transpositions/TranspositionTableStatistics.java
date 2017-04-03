package chess.transpositions;

/**
 *
 * @author Philipp
 */
public class TranspositionTableStatistics {

    public static long requests, hits;
    
    public static void soutHitrate() {
        String hitrate;
        if(requests != 0) {
            hitrate = String.valueOf(100 * hits / requests);
        } else {
            hitrate = "-";
        }
        System.out.println(hitrate + "% hitrate (" + hits + "/" + requests + ")");
    }
    
    public static void clear() {
        requests = hits = 0;
    }
    
}
