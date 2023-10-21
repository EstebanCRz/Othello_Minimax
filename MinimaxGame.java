import java.util.ArrayList;
import java.util.Random;

public class MinimaxGame {
    boolean gameover = false;
    static ArrayList<Integer> movePossible = new ArrayList<>();
    private static final int Jcourant = 1;
    private static final int Jadverse = -1;
    private static final int MAX_DEPTH = 3;

    public static int JouerIA() {
        // Obtenez un coup aléatoire parmi les coups possibles
        if (movePossible.isEmpty()) {
            return -1; // Aucun coup possible
        }
        Random random = new Random();
        int randomIndex = random.nextInt(movePossible.size());
        int selectedMove = movePossible.get(randomIndex);
        return selectedMove;
    }

    private int evaluation(){
        return 1;
    }

    public int minimax(int depth, int player) {
        if (gameover || depth == 0) {
            return evaluation();
        }

        int bestScore = (player == Jcourant) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int bestMove = -1;

        movePossible();
        for (int m : movePossible) {
            faire_move(m, player);
            int score = minimax(depth - 1, -player);
            defaire_move(m);

            if (player == Jcourant) {
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = m;
                }
            } else {
                if (score < bestScore) {
                    bestScore = score;
                    bestMove = m;
                }
            }
        }

        if (depth == MAX_DEPTH) {
            // Vous pouvez prendre la décision du meilleur mouvement ici.
            System.out.println("Meilleur mouvement : " + bestMove);
        }

        return bestScore;
    }

    private void movePossible() {
        movePossible.clear();
        // Ajoutez les coups possibles à la liste
        for (int i = 0; i < Game.WIDTH_NB_BLOC; i++) {
            for (int j = 0; j < Game.HEIGHT_NB_BLOC; j++) {
                //if (Game.plateau[i][j] == -1) {
                //    movePossible.add(i * Game.WIDTH_NB_BLOC + j);
                //}
            }
        }
    }

    private void defaire_move(int m) {
    }

    private void faire_move(int m, int player) {
    }
}
