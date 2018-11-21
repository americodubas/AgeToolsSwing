import ui.DatabaseForm;
import ui.MenuBar;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame("AGE TOOLS");
        frame.setJMenuBar(new MenuBar(frame));
        frame.setContentPane(new DatabaseForm(frame).databasePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
