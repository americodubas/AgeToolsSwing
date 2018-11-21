package ui;

import interfaces.Panel;
import model.Database;
import org.jetbrains.annotations.NotNull;
import services.DatabaseServiceKt;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static services.DatabaseServiceKt.changeConnectionTo;
import static services.DatabaseServiceKt.isDatabaseNameAlreadyUsed;

@SuppressWarnings("unchecked")
public class DatabaseForm implements Panel {
    private JFrame frame;

    public JPanel databasePanel;
    private JList databaseList;
    private JButton addButton;
    private JButton saveButton;
    private JButton deleteButton;
    private JTextField nameField;
    private JTextField userField;
    private JTextField urlField;
    private JButton changeToThisDatabaseButton;

    private DefaultListModel databaseModel;
    private int id;
    private ResourceBundle words = ResourceBundle.getBundle("words");

    public DatabaseForm(JFrame frame) {
        this.frame = frame;
        setDatabaseList();
        checkDisableDeleteButton();
        setAddButtonListener();
        setSaveButtonListener();
        setDeleteButtonListener();
        setChangeButtonListener();
    }

    private void setChangeButtonListener() {
        changeToThisDatabaseButton.setMnemonic(KeyEvent.VK_C);
        changeToThisDatabaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeConnectionTo(id);
                Toast.makeText(frame, words.getString("connection.changed"));
            }
        });
    }

    private void setDeleteButtonListener() {
        deleteButton.setMnemonic(KeyEvent.VK_D);
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (canDelete()) {
                    DatabaseServiceKt.deleteDatabaseBy(id);
                    databaseModel.remove(databaseList.getSelectedIndex());
                    checkDisableDeleteButton();
                    selectFirstDatabase();
                } else {
                    Toast.makeText(frame, words.getString("can.not.delete"));
                }
            }
        });
    }

    private boolean canDelete() {
        return databaseModel.size() > 1;
    }

    private void setSaveButtonListener() {
        saveButton.setMnemonic(KeyEvent.VK_S);
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isMissingRequiredFields()){
                    return;
                }
                if (isDatabaseNameAlreadyUsed(nameField.getText(), id)) {
                    Toast.makeText(frame,words.getString("name.used"));
                    return;
                }
                Database database = DatabaseServiceKt.getDatabaseBy(id);
                if (database != null) {
                    database.setName(nameField.getText());
                    database.setUser(userField.getText());
                    database.setUrl(urlField.getText());
                    DatabaseServiceKt.updateDatabase(database);
                    databaseModel.set(databaseList.getSelectedIndex(), nameField.getText());
                } else {
                    Toast.makeText(frame,words.getString("error.database"));
                }
            }
        });
    }

    private void setAddButtonListener() {
        addButton.setMnemonic(KeyEvent.VK_A);
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                databaseModel.addElement(DatabaseServiceKt.createDatabase().getName());
                checkDisableDeleteButton();
            }
        });
    }

    private void checkDisableDeleteButton() {
        deleteButton.setEnabled(databaseModel.size() > 1);
    }

    private void setDatabaseList() {
        getDatabaseModelFromJson();
        databaseList.setModel(databaseModel);
        databaseList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        databaseList.setLayoutOrientation(JList.VERTICAL);
        setDatabaseSelectionListener();
        selectFirstDatabase();
    }

    private void setDatabaseSelectionListener() {
        databaseList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (databaseList.getSelectedIndex() < 0) {
                    return;
                }
                Database database = DatabaseServiceKt.getDatabaseBy((String) databaseModel.get(databaseList.getSelectedIndex()));
                if (database != null) {
                    nameField.setText(database.getName());
                    userField.setText(database.getUser());
                    urlField.setText(database.getUrl());
                    id = database.getId();
                } else {
                    Toast.makeText(frame, words.getString("error.database"));
                }
            }
        });
    }

    private void selectFirstDatabase() {
        databaseList.setSelectedIndex(0);
    }

    private void getDatabaseModelFromJson() {
        databaseModel = new DefaultListModel();
        ArrayList<String> allDatabasesNames = DatabaseServiceKt.getAllDatabasesNames();
        for (String name: allDatabasesNames) {
            databaseModel.addElement(name);
        }
    }


    private boolean isMissingRequiredFields() {
        boolean missing = false;
        if (nameField.getText().trim().length() == 0) {
            Toast.makeText(frame, words.getString("name.required"));
            missing = true;
        }
        if (!missing && userField.getText().trim().length() == 0) {
            Toast.makeText(frame, words.getString("user.required"));
            missing = true;
        }
        if (!missing && urlField.getText().trim().length() == 0) {
            Toast.makeText(frame, words.getString("url.required"));
            missing = true;
        }
        return missing;
    }

    @NotNull
    @Override
    public JPanel getPanel() {
        return databasePanel;
    }
}