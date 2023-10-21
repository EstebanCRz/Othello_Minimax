import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.JPanel;



public class Game extends JPanel {
    boolean player = false;
    boolean running = false;
    static Random random;
    static final int UNIT_PX = 100;
    static final int WIDTH_NB_BLOC = 6;
    static final int HEIGHT_NB_BLOC = 6;
    static final int WIDTH_PX = WIDTH_NB_BLOC * UNIT_PX;
    static final int HEIGHT_PX = HEIGHT_NB_BLOC * UNIT_PX;
    int[][] plateau = new int[WIDTH_NB_BLOC][HEIGHT_NB_BLOC];


    Game() {
        random = new Random();
        this.setPreferredSize(new Dimension(WIDTH_PX, HEIGHT_PX));
        this.setBackground(Color.lightGray);
        this.setFocusable(true);
        //debut de jeu
        plateau[WIDTH_NB_BLOC/2-1][HEIGHT_NB_BLOC/2] = 1;
        plateau[WIDTH_NB_BLOC/2-1][HEIGHT_NB_BLOC/2-1] = 2;
        plateau[WIDTH_NB_BLOC/2][HEIGHT_NB_BLOC/2-1] = 1;
        plateau[WIDTH_NB_BLOC/2][HEIGHT_NB_BLOC/2] = 2;
        //case jouable de début
        startGame();
        initMouseListener();

    }


    public void startGame() {
        Get_case_jouable();
        running = true;
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paint_quadrillage(g);
        paint_case(g);
        if (!running) {
            paint_over(g);
        }
    }
    private void paint_over(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, WIDTH_PX/7));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Winner is "+GetWinner(), (WIDTH_PX - metrics.stringWidth("The Winner is couleur")/2), HEIGHT_PX/2);
    }

    private void paint_case(Graphics g) {
        for (int i = 0; i < WIDTH_NB_BLOC; i++) {
            for (int j = 0; j < HEIGHT_NB_BLOC; j++) {
                //a améliorer
                if (plateau[i][j] < 0) {
                    g.setColor(Color.red);
                    g.fillOval(i * UNIT_PX, j * UNIT_PX, UNIT_PX, UNIT_PX);
                }
                if (plateau[i][j] == 1) {
                    g.setColor(Color.black);
                    g.fillOval(i * UNIT_PX, j * UNIT_PX, UNIT_PX, UNIT_PX);
                }
                if (plateau[i][j] == 2) {
                    g.setColor(Color.white);
                    g.fillOval(i * UNIT_PX, j * UNIT_PX, UNIT_PX, UNIT_PX);
                }
            }
        }
    }
    public void paint_quadrillage(Graphics g) {
        //quadrillage
        //vertical
        for (int i = 1; i < WIDTH_NB_BLOC; i++) {
            g.drawLine(i * UNIT_PX, 0, i * UNIT_PX, HEIGHT_PX);
        }
        //horizontal
        for (int i = 1; i < HEIGHT_NB_BLOC; i++) {
            g.drawLine(0, i * UNIT_PX, WIDTH_PX, i * UNIT_PX);
        }
    }
    public void jouer(int x, int y) {
        if (player) {
            plateau[x][y] = 2;
        } else
            plateau[x][y] = 1;
        reverse_case(x, y);
        player = !player;
        Get_case_jouable();
        if (GameFinished()){
            running = false;
            repaint();
        }
        if(!Is_it_case_jouable()) {
            player = !player;
            Get_case_jouable();
        }
        repaint();
    }

    private String GetWinner() {
        int nbcase = WIDTH_NB_BLOC*HEIGHT_NB_BLOC;
        int Blanc = 0;
        for (int i = 0; i < WIDTH_NB_BLOC; i++) {
            for (int j = 0; j < HEIGHT_NB_BLOC; j++) {
                if (plateau[i][j] == 1)
                    Blanc++;
            }
        }
        if (Blanc == nbcase) {
            return "Egalite";
        }
        else if(Blanc>nbcase){
            return "Noir";
        }
        else
            return "Blanc";
    }
    private boolean GameFinished() {
        for (int i = 0; i < WIDTH_NB_BLOC; i++) {
            for (int j = 0; j < HEIGHT_NB_BLOC; j++) {
                if (plateau[i][j] <= 0) {
                    return false;
                }
            }
        }
        return true;
    }
    private boolean Is_it_case_jouable() {
        for (int i = 0; i < WIDTH_NB_BLOC; i++) {
            for (int j = 0; j < HEIGHT_NB_BLOC; j++) {
                if (plateau[i][j] == -1) {
                    return true;
                }
            }
        }
        return false;
    }
    private void reverse_case(int x, int y) {
        // Test de toutes les directions
        testReverse(x, y, 0, 1);   // Vers le bas
        testReverse(x, y, 0, -1);  // Vers le haut
        testReverse(x, y, 1, 0);   // Vers la droite
        testReverse(x, y, -1, 0);  // Vers la gauche
        testReverse(x, y, 1, 1);   // Diagonale bas-droite
        testReverse(x, y, -1, -1); // Diagonale haut-gauche
        testReverse(x, y, 1, -1);  // Diagonale bas-gauche
        testReverse(x, y, -1, 1);  // Diagonale haut-droite
    }
    private void testReverse(int x, int y, int dx, int dy) {
        int opponent = player ? 1 : 2;
        int ownPiece = player ? 2 : 1;
        int startX = x;
        int startY = y;
        boolean canReverse = false;

        // Avance dans la direction spécifiée
        x += dx;
        y += dy;

        // Tant que les coordonnées sont valides
        while (0 <= x && x < WIDTH_NB_BLOC && 0 <= y && y < HEIGHT_NB_BLOC) {
            if (plateau[x][y] == 0) {
                break; // Sortir de la boucle si une case vide est rencontrée
            } else if (plateau[x][y] == opponent) {
                canReverse = true;
            } else if (plateau[x][y] == ownPiece) {
                if (canReverse) {
                    // Inverser les cases entre (startX, startY) et (x, y)
                    while (x != startX || y != startY) {
                        plateau[x][y] = ownPiece; // Inverser la pièce
                        x -= dx;
                        y -= dy;
                    }
                }
                break;
            }
            x += dx;
            y += dy;
        }
    }
    private void Get_case_jouable() {
        // Réinitialise toutes les cases jouables
        for (int i = 0; i < WIDTH_NB_BLOC; i++) {
            for (int j = 0; j < HEIGHT_NB_BLOC; j++) {
                if (plateau[i][j] == -1) {
                    plateau[i][j] = 0;
                }
            }
        }
        int ownPiece = player ? 2 : 1;
        // Recherche de cases jouables dans toutes les directions
        for (int i = 0; i < WIDTH_NB_BLOC; i++) {
            for (int j = 0; j < HEIGHT_NB_BLOC; j++) {
                if (plateau[i][j] == ownPiece) {

                    // Test de toutes les directions
                    testDirection(i, j, 0, 1);   // Vers le bas
                    testDirection(i, j, 0, -1);  // Vers le haut
                    testDirection(i, j, 1, 0);   // Vers la droite
                    testDirection(i, j, -1, 0);  // Vers la gauche
                    testDirection(i, j, 1, 1);   // Diagonale bas-droite
                    testDirection(i, j, -1, -1); // Diagonale haut-gauche
                    testDirection(i, j, 1, -1);  // Diagonale bas-gauche
                    testDirection(i, j, -1, 1);  // Diagonale haut-droite
                }
            }
        }
    }
    private void testDirection(int x, int y, int dx, int dy) {
        int opponent = player ? 1 : 2;
        int ownPiece = player ? 2 : 1;
        boolean canFlip = false;

        // Avance dans la direction spécifiée
        x += dx;
        y += dy;

        while (0 <= x && x < WIDTH_NB_BLOC && 0 <= y && y < HEIGHT_NB_BLOC) {
            if (plateau[x][y] == 0) {
                if (canFlip) {
                    plateau[x][y] = -1; // Case jouable
                }
                break;
            } else if (plateau[x][y] == opponent) {
                canFlip = true;
            } else if (plateau[x][y] == ownPiece) {
                if (canFlip) {
                    break;
                }
            } else {
                break; // Si une case vide est rencontrée avant une pièce de l'adversaire, sortez de la boucle.
            }
            x += dx;
            y += dy;
        }
    }
    private void initMouseListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX() / UNIT_PX;
                int mouseY = e.getY() / UNIT_PX;
                System.out.println(mouseX + " " + mouseY);
                if (plateau[mouseX][mouseY] < 0) {
                    jouer(mouseX, mouseY);
                    //int valeurIA = MinimaxGame.JouerIA();
                }
            }
        });
    }
}