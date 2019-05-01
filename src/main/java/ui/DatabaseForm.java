package ui;

import model.ConnectionFile;
import model.Database;
import services.ConnectionFileServiceKt;
import services.DatabasePasswordFileServiceKt;
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
public class DatabaseForm {
    private JFrame frame;

    public JPanel databasePanel;
    private JList databaseList;
    private JList passwordList;
    private JButton addButton;
    private JButton saveButton;
    private JButton deleteButton;
    private JTextField nameField;
    private JTextField userField;
    private JTextField urlField;
    private JTextField passwordField;
    private JButton changeToThisDatabaseButton;
    private JButton savePasswordButton;

    private DefaultListModel databaseModel;
    private DefaultListModel passwordModel;
    private int databaseId;
    private int connectionFileId;
    private ResourceBundle words = ResourceBundle.getBundle("words");

    DatabaseForm(JFrame frame) {
        this.frame = frame;
        setDatabaseList();
        setPasswordList();
        checkDisableDeleteButton();
        setAddButtonListener();
        setSaveButtonListener();
        setDeleteButtonListener();
        setChangeButtonListener();
        setSavePasswordButtonListener();
    }

    /**
     * Set changeToThisDatabaseButton listener.
     * When clicked change all connection files to the chosen database
     */
    private void setChangeButtonListener() {
        changeToThisDatabaseButton.setMnemonic(KeyEvent.VK_C);
        changeToThisDatabaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeConnectionTo(databaseId);
                Toast.makeText(frame, words.getString("connection.changed"));
            }
        });
    }

    /**
     * Set setSavePasswordButtonListener listener.
     * When clicked save the password for the chosen file and database
     */
    private void setSavePasswordButtonListener() {
        savePasswordButton.setMnemonic(KeyEvent.VK_P);
        savePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DatabasePasswordFileServiceKt.updatePassword(databaseId, connectionFileId, passwordField.getText());
                Toast.makeText(frame, words.getString("password.saved"));
            }
        });
    }

    /**
     * Set deleteButton listener.
     * When clicked delete the selected database as long as it is not the last.
     * After this selected the first database remaining.
     */
    private void setDeleteButtonListener() {
        deleteButton.setMnemonic(KeyEvent.VK_D);
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (canDelete()) {
                    DatabaseServiceKt.deleteDatabaseBy(databaseId);
                    databaseModel.remove(databaseList.getSelectedIndex());
                    checkDisableDeleteButton();
                    selectFirstDatabase();
                } else {
                    Toast.makeText(frame, words.getString("can.not.delete.database"));
                }
            }
        });
    }

    /**
     * If databaseModel size is larger than one, the register can be deleted
     * @return boolean, true if the register can be deleted
     */
    private boolean canDelete() {
        return databaseModel.size() > 1;
    }

    /**
     * Set saveButton listener.
     * When clicked set the fields in the selected database by it's databaseId,
     * Shows a toast if the name is already used by another database.
     */
    private void setSaveButtonListener() {
        saveButton.setMnemonic(KeyEvent.VK_S);
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isMissingRequiredFields()){
                    return;
                }
                if (isDatabaseNameAlreadyUsed(nameField.getText(), databaseId)) {
                    Toast.makeText(frame,words.getString("name.used"));
                    return;
                }
                Database database = DatabaseServiceKt.getDatabaseBy(databaseId);
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

    /**
     * Set addButton listener.
     * When clicked creates a new database and add it's name to databaseModel.
     * Call checkDisableDeleteButton to see if the button needs to be enabled.
     */
    private void setAddButtonListener() {
        addButton.setMnemonic(KeyEvent.VK_A);
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                databaseModel.addElement(DatabaseServiceKt.createDatabase().getName());
                checkDisableDeleteButton();
            }
        });
    }

    /**
     * If there is only one register in databaseModel disable the deleteButton
     */
    private void checkDisableDeleteButton() {
        deleteButton.setEnabled(databaseModel.size() > 1);
    }

    /**
     * Set databaseList from json
     * Set the selection listener and select the first database
     */
    private void setDatabaseList() {
        getDatabaseModelFromJson();
        databaseList.setModel(databaseModel);
        databaseList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        databaseList.setLayoutOrientation(JList.VERTICAL);
        setDatabaseSelectionListener();
        selectFirstDatabase();
    }

    /**
     * Get the names of all connection files and put them in the model. (one password per connection file)
     */
    void setPasswordList() {
        getPasswordModelFromJson();
        passwordList.setModel(passwordModel);
        databaseList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        databaseList.setLayoutOrientation(JList.VERTICAL);
        setPasswordSelectionListener();
        selectFirstPassword();
    }

    /**
     * Set the selection listener.
     * When a file from passwordList is selected, find the connection file and the database and show the stored password
     */
    private void setPasswordSelectionListener() {
        passwordList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (passwordList.getSelectedIndex() < 0) {
                    return;
                }
                ConnectionFile connectionFile = ConnectionFileServiceKt.getConnectionFileBy((String) passwordModel.get(passwordList.getSelectedIndex()));
                if (connectionFile != null) {
                    connectionFileId = connectionFile.getId();
                    passwordField.setText( DatabasePasswordFileServiceKt.getPassword(databaseId, connectionFileId).getPassword() );
                } else {
                    Toast.makeText(frame, words.getString("error.database"));
                }
            }
        });
    }

    /**
     * Get the names of all connection files and put them in the model. (one password per connection file)
     */
    private void getPasswordModelFromJson() {
        passwordModel = new DefaultListModel();
        ArrayList<String> allConnectionFilesNames = ConnectionFileServiceKt.getAllConnectionFilesNames();
        for (String name: allConnectionFilesNames) {
            passwordModel.addElement(name);
        }
    }

    /**
     * Set the selection listener.
     * When a database is selected, fill the fields with it's data
     */
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
                    databaseId = database.getId();
                } else {
                    Toast.makeText(frame, words.getString("error.database"));
                }
            }
        });
    }

    /**
     * Select the first database from the databaseList
     */
    private void selectFirstDatabase() {
        databaseList.setSelectedIndex(0);
    }

    /**
     * Select the first password from the passwordList
     */
    private void selectFirstPassword() {
        passwordList.setSelectedIndex(0);
    }

    /**
     * Get the names of all databases and put them in the model
     */
    private void getDatabaseModelFromJson() {
        databaseModel = new DefaultListModel();
        ArrayList<String> allDatabasesNames = DatabaseServiceKt.getAllDatabasesNames();
        for (String name: allDatabasesNames) {
            databaseModel.addElement(name);
        }
    }

    /**
     * Verify if all required fields are filled
     * @return boolean, true if all required fields are filled
     */
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
}