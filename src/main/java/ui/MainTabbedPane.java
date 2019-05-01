package ui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.KeyEvent;

public class MainTabbedPane extends JTabbedPane implements ChangeListener {

    private DatabaseForm databaseForm;

    /**
     * Main tab where is placed two tabs
     * First tab with all saved databases
     * Second tab with all connection files that need to be changed when a database is selected
     * Also implements the change listener to update the information on the tabs if needed
     * @param frame
     */
    public MainTabbedPane(JFrame frame) {
        super();
        databaseForm = new DatabaseForm(frame);
        this.addTab("1 Database", databaseForm.databasePanel);
        this.addTab("2 Connection", new ConnectionFileForm(frame).connectionFieldPanel);
        this.setMnemonicAt(0, KeyEvent.VK_1);
        this.setMnemonicAt(1, KeyEvent.VK_2);
        this.addChangeListener(this);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        databaseForm.setPasswordList();
    }
}
