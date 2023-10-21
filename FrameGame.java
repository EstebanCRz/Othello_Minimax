import javax.swing.JFrame;

public class  FrameGame extends JFrame {

    FrameGame(){
        add(new Game());
        setTitle("projet_0thell0");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }
}