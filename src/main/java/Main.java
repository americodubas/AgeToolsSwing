import ui.CustomColor;
import ui.MainTabbedPane;

import javax.swing.*;
import java.awt.*;

public class Main {

    /**
     * Create main frame with the main tabbed pane
     * @param args not used
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            UIManager.put("TabbedPane.selected", CustomColor.DARK_BLUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JFrame frame = new JFrame("AGE TOOLS");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new MainTabbedPane(frame), BorderLayout.CENTER);
        frame.getContentPane().setBackground(CustomColor.DARK);
        frame.pack();
        frame.setVisible(true);
    }

}
