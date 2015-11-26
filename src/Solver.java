import java.util.ArrayList;

/**
 * Created by Jacob on 11/19/2015.
 */
public class Solver {
    private Board board;
    private int[] calculatedMove;
    private long maxRuntime;
    private long startRuntime;

    public Solver(Board b, int t) {
        board = b;
        maxRuntime = t * 1000;
    }

    public int[] getMove() {
        return calculatedMove;
    }

    public void solve() {
        startRuntime = System.currentTimeMillis();
        alphaBetaPrune(board, true, Integer.MIN_VALUE, Integer.MAX_VALUE, 5000);
    }

    private int alphaBetaPrune(Board b, boolean maxPlayer, int alpha, int beta, int depth) {
        int score = b.getEvaluationValue();

        //Cutoff test, non-threaded
        if(System.currentTimeMillis() - startRuntime >= maxRuntime || depth == 0) {
            calculatedMove = b.getLastMove();
            return score;
        }

        //populate children
        ArrayList<Board> children = populateChildren(board, maxPlayer ? 'x' : 'o');
        if (maxPlayer) {
            for (Board child : children) {
                score = alphaBetaPrune(child, false, alpha, beta, depth - 1);
                if (score > alpha) {
                    alpha = score;
                } else if (alpha >= beta) {
                    break;
                }
            }
            return alpha;
        } else {
            for (Board child : children) {
                score = alphaBetaPrune(child, true, alpha, beta, depth - 1);
                if (score < beta) {
                    beta = score;
                } else if (alpha <= beta) {
                    break;
                }
            }
            return beta;
        }

    }

    private ArrayList<Board> populateChildren(Board b, char token) {
        ArrayList<Board> children = new ArrayList<Board>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(System.currentTimeMillis() - startRuntime >= maxRuntime) {
                    return children;
                }

                char[][] childBoard = new char[8][8];
                for (int k = 0; k < 8; k++) {
                    for (int l = 0; l < 8; l++) {
                        childBoard[k][l] = b.getBoard()[k][l];
                    }
                }

                if (childBoard[i][j] == '-') {
                    childBoard[i][j] = token;
                    int[] move = new int[2];
                    move[0] = i;
                    move[1] = j;
                    children.add(new Board(childBoard, move));
                }
            }
        }

        return children;
    }
}
