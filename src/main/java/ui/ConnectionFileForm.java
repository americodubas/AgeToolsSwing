package ui;

import model.ConnectionFile;
import services.ConnectionFileServiceKt;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.ResourceBundle;

class ConnectionFileForm {
    private final JFrame frame;

    public JPanel connectionFieldPanel;
    private JList connectionFileList;
    private JButton addButton;
    private JButton saveButton;
    private JButton deleteButton;
    private JTextField nameField;
    private JTextField filepathField;
    private JTextField userTagField;
    private JTextField urlTagField;
    private JTextField passwordTagField;

    private DefaultListModel connectionFileModel;
    private int id;
    private final ResourceBundle words = ResourceBundle.getBundle("words");

    ConnectionFileForm(JFrame frame) {
        this.frame = frame;
        setConnectionFileList();
        checkDisableDeleteButton();
        setAddButtonListener();
        setSaveButtonListener();
        setDeleteButtonListener();
    }

    /**
     * Set deleteButton listener.
     * When clicked delete the selected connection file as long as it is not the last.
     * After this selected the first connection file remaining.
     */
    private void setDeleteButtonListener() {
        deleteButton.setMnemonic(KeyEvent.VK_D);
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (canDelete()) {
                    ConnectionFileServiceKt.deleteConnectionFileBy(id);
                    connectionFileModel.remove(connectionFileList.getSelectedIndex());
                    checkDisableDeleteButton();
                    selectFirstConnectionFile();
                } else {
                    Toast.makeText(frame, words.getString("can.not.delete.connection.file"));
                }
            }
        });
    }

    /**
     * If connectionFileModel size is larger than one, the register can be deleted
     * @return boolean, true if the register can be deleted
     */
    private boolean canDelete() {
        return connectionFileModel.size() > 1;
    }

    /**
     * Set saveButton listener.
     * When clicked set the fields in the selected connection field by it's id,
     * Shows a toast if the name is already used by another connection file.
     */
    private void setSaveButtonListener() {
        saveButton.setMnemonic(KeyEvent.VK_S);
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isMissingRequiredFields()){
                    return;
                }
                if (ConnectionFileServiceKt.isConnectionFileNameAlreadyUsed(nameField.getText(), id)) {
                    Toast.makeText(frame,words.getString("name.used"));
                    return;
                }
                ConnectionFile connectionFile = ConnectionFileServiceKt.getConnectionFileBy(id);
                if (connectionFile != null) {
                    connectionFile.setName(nameField.getText());
                    connectionFile.setFilepath(filepathField.getText());
                    connectionFile.setUserTag(userTagField.getText());
                    connectionFile.setUrlTag(urlTagField.getText());
                    connectionFile.setPasswordTag(passwordTagField.getText());
                    ConnectionFileServiceKt.updateConnectionFile(connectionFile);
                    connectionFileModel.set(connectionFileList.getSelectedIndex(), nameField.getText());
                } else {
                    Toast.makeText(frame,words.getString("error.connection.file"));
                }
            }
        });
    }

    /**
     * Set addButton listener.
     * When clicked creates a new connection file and add it's name to connectionFileModel.
     * Call checkDisableDeleteButton to see if the button needs to be enabled.
     */
    private void setAddButtonListener() {
        addButton.setMnemonic(KeyEvent.VK_A);
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                connectionFileModel.addElement(ConnectionFileServiceKt.createConnectionFile().getName());
                checkDisableDeleteButton();
            }
        });
    }

    /**
     * If there is only one register in connectionFileModel disable the deleteButton
     */
    private void checkDisableDeleteButton() {
        deleteButton.setEnabled(connectionFileModel.size() > 1);
    }

    /**
     * Set connectionFileList from json
     * Set the selection listener and select the first connection file
     */
    private void setConnectionFileList() {
        getConnectionFileModelFromJson();
        connectionFileList.setModel(connectionFileModel);
        connectionFileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        connectionFileList.setLayoutOrientation(JList.VERTICAL);
        setConnectionFileSelectionListener();
        selectFirstConnectionFile();
    }

    /**
     * Set the selection listener.
     * When a connection file is selected, fill the fields with it's data
     */
    private void setConnectionFileSelectionListener() {
        connectionFileList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (connectionFileList.getSelectedIndex() < 0) {
                    return;
                }
                ConnectionFile connectionFile = ConnectionFileServiceKt.getConnectionFileBy((String) connectionFileModel.get(connectionFileList.getSelectedIndex()));
                if (connectionFile != null) {
                    nameField.setText(connectionFile.getName());
                    filepathField.setText(connectionFile.getFilepath());
                    userTagField.setText(connectionFile.getUserTag());
                    urlTagField.setText(connectionFile.getUrlTag());
                    passwordTagField.setText(connectionFile.getPasswordTag());
                    id = connectionFile.getId();
                } else {
                    Toast.makeText(frame, words.getString("error.connection.file"));
                }
            }
        });
    }

    /**
     * Select the first connection file from the connectionFileList
     */
    private void selectFirstConnectionFile() {
        connectionFileList.setSelectedIndex(0);
    }

    /**
     * Get the names of all connection files and put them in the model
     */
    private void getConnectionFileModelFromJson() {
        connectionFileModel = new DefaultListModel();
        ArrayList<String> allConnectionFilesNames = ConnectionFileServiceKt.getAllConnectionFilesNames();
        for (String name: allConnectionFilesNames) {
            connectionFileModel.addElement(name);
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
        if (filepathField.getText().trim().length() == 0) {
            Toast.makeText(frame, words.getString("path.required"));
            missing = true;
        }
        if (userTagField.getText().trim().length() == 0) {
            Toast.makeText(frame, words.getString("user.tag.required"));
            missing = true;
        }
        if (urlTagField.getText().trim().length() == 0) {
            Toast.makeText(frame, words.getString("url.tag.required"));
            missing = true;
        }
        if (passwordTagField.getText().trim().length() == 0) {
            Toast.makeText(frame, words.getString("password.tag.required"));
            missing = true;
        }
        return missing;
    }

}
