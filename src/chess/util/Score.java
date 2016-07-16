package chess.util;

/**
 *
 * @author Philipp
 */
public class Score {
    private final static int MATE_SCORE = 30000;
    private final static int MATE_SCORE_LIMIT = 27000;
    
    public static short toTableScore(int score, int currentPly) {
        int sign = (int) Math.signum(score);
        int absoluteScore = sign * score;
        if(absoluteScore >= MATE_SCORE_LIMIT) {
            int matePly = MATE_SCORE - absoluteScore;
            int mateDepth = matePly - currentPly;
            assert fromTableScore((short)(sign * (MATE_SCORE - mateDepth)), currentPly) == score;
            return (short) (sign * (MATE_SCORE - mateDepth));
        }
        assert fromTableScore((short) score, currentPly) == score: score;
        assert !isMateScore(score);
        return (short) score;
    }
    
    public static int fromTableScore(short score, int currentPly) {
        int sign = (int) Math.signum(score);
        int absoluteScore = sign * score;
        if(absoluteScore >= MATE_SCORE_LIMIT) {
            int mateDepth = MATE_SCORE - absoluteScore;
            int matePly = currentPly + mateDepth;
            return sign * (MATE_SCORE - matePly);
        }
        assert !isMateScore(score);
        return score;
    }

    public static int mateLossScore(int currentPly) {
        return -MATE_SCORE + currentPly;
    }
    
    public static int plyFromMateScore(int mateScore) {
        return MATE_SCORE - Math.abs(mateScore);
    }
    
    public static boolean isMateScore(int score) {
        return Math.abs(score) >= MATE_SCORE_LIMIT;
    }

}
