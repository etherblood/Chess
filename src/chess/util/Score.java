package chess.util;

/**
 *
 * @author Philipp
 */
public class Score {

    private static final boolean FAIL_HARD = false;

    public static int boundScore(int alpha, int score, int beta) {
        assert alpha < beta;
        if (FAIL_HARD) {
            if (score < alpha) {
                return alpha;
            }
            if (beta < score) {
                return beta;
            }
        }
        return score;
    }

}
