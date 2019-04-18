import ui.ConnectionFileForm;
import ui.DatabaseForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Main {

    /**
     * Create main frame with two tabs.
     * First tab with all saved databases.
     * Second tab with all connection files that need to be changed when a database is selected
     * @param args not used
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("AGE TOOLS");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("1 Database", new DatabaseForm(frame).databasePanel);
        tabbedPane.addTab("2 Connection", new ConnectionFileForm(frame).connectionFieldPanel);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
        frame.add(tabbedPane, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);
    }

}
