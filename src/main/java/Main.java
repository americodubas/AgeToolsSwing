import ui.MainTabbedPane;

import javax.swing.*;
import java.awt.*;

public class Main {

    /**
     * Create main frame with the main tabbed pane
     * @param args not used
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("AGE TOOLS");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new MainTabbedPane(frame), BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

}
