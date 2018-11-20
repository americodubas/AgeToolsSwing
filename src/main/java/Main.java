import javax.swing.*;

public class Main {

    private static JFrame frame;

    public static void main(String[] args) {
        frame = new JFrame("AGE TOOLS");
        frame.setContentPane(new DatabaseForm(frame).databasePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
