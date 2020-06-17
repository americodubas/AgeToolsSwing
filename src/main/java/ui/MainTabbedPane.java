package ui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.KeyEvent;

public class MainTabbedPane extends JTabbedPane implements ChangeListener {

    private final DatabaseForm databaseForm;

    /**
     * Main tab where is placed two tabs
     * First tab with all saved databases
     * Second tab with all connection files that need to be changed when a database is selected
     * Also implements the change listener to update the information on the tabs if needed
     * @param frame frame that will contain the MainTabbedPane
     */
    public MainTabbedPane(JFrame frame) {
        super();
        databaseForm = new DatabaseForm(frame);
        this.addTab("1 Database", CustomIcon.getInstance().storageIcon, databaseForm.databasePanel);
        this.addTab("2 Connection", CustomIcon.getInstance().syncIcon,  new ConnectionFileForm(frame).connectionFieldPanel);
        this.addTab("3 User", CustomIcon.getInstance().accountIcon, new UserForm(frame).userFieldPanel);
        this.setMnemonicAt(0, KeyEvent.VK_1);
        this.setMnemonicAt(1, KeyEvent.VK_2);
        this.setMnemonicAt(2, KeyEvent.VK_3);
        this.addChangeListener(this);
        this.setBackground(CustomColor.DARK_GREY);
        this.setForeground(Color.WHITE);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        databaseForm.setPasswordList();
    }
}
