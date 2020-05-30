package ui;

import model.User;
import services.UserServiceKt;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class UserForm {
    private final JFrame frame;

    public JPanel userFieldPanel;
    private JButton addButton;
    private JButton saveButton;
    private JButton deleteButton;
    private JTextField nameField;
    private JTextField passwordField;
    private JTextField codeField;
    private JList userList;
    private JTextArea descriptionArea;
    private JButton localButton;
    private JButton hsocButton;
    private JButton psocButton;
    private JButton bsocButton;

    private DefaultListModel userModel;
    private int id;
    private final ResourceBundle words = ResourceBundle.getBundle("words");

    UserForm(JFrame frame) {
        this.frame = frame;
        setUserList();
        checkDisableDeleteButton();
        setAddButtonListener();
        setSaveButtonListener();
        setDeleteButtonListener();
        setLocalButtonListener();
        setHSocButtonListener();
        setPSocButtonListener();
        setBSocButtonListener();
    }

    private void setHSocButtonListener() {
        hsocButton.setMnemonic(KeyEvent.VK_H);
        hsocButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                opeUrl("http://www.h-soc.com.br");
            }
        });
    }

    private void setPSocButtonListener() {
        psocButton.setMnemonic(KeyEvent.VK_P);
        psocButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                opeUrl("http://www.p-soc.com.br");
            }
        });
    }

    private void setBSocButtonListener() {
        bsocButton.setMnemonic(KeyEvent.VK_B);
        bsocButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                opeUrl("http://www.b-soc.com.br");
            }
        });
    }

    private void setLocalButtonListener() {
        localButton.setMnemonic(KeyEvent.VK_L);
        localButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                opeUrl("http://localhost:8080");
            }
        });
    }

    /**
     * Try to open the url with current user credentials and with the default browser
     * @param begin start of the site url
     */
    private void opeUrl(String begin) {
        try {
            Desktop.getDesktop().browse(URI.create(getUrlForUser(begin)));
        } catch (Exception ex) {
            Toast.makeText(frame, ex.getMessage());
        }
    }

    /**
     * Get current user credentials to login
     * @param begin start of the site url
     * @return url
     */
    private String getUrlForUser(String begin) throws UnsupportedEncodingException {
        String parameters = "usu=" + URLEncoder.encode(nameField.getText(), StandardCharsets.UTF_8.toString()) +
                "&senha=" + URLEncoder.encode(passwordField.getText(), StandardCharsets.UTF_8.toString()) +
                "&empsoc=" + URLEncoder.encode(codeField.getText(), StandardCharsets.UTF_8.toString());
        return begin + "/WebSoc/LoginAction.do?" + parameters;
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
                    UserServiceKt.deleteUserBy(id);
                    userModel.remove(userList.getSelectedIndex());
                    checkDisableDeleteButton();
                    selectFirstUser();
                } else {
                    Toast.makeText(frame, words.getString("can.not.delete.connection.file"));
                }
            }
        });
    }

    /**
     * If userModel size is larger than one, the register can be deleted
     * @return boolean, true if the register can be deleted
     */
    private boolean canDelete() {
        return userModel.size() > 1;
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
                if (UserServiceKt.isUserNameAlreadyUsed(nameField.getText(), id)) {
                    Toast.makeText(frame,words.getString("name.used"));
                    return;
                }
                User user = UserServiceKt.getUserBy(id);
                if (user != null) {
                    user.setName(nameField.getText());
                    user.setPassword(passwordField.getText());
                    user.setCode(codeField.getText());
                    user.setDescription(descriptionArea.getText());
                    UserServiceKt.updateUser(user);
                    userModel.set(userList.getSelectedIndex(), nameField.getText());
                } else {
                    Toast.makeText(frame,words.getString("error.connection.file"));
                }
            }
        });
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
        if (passwordField.getText().trim().length() == 0) {
            Toast.makeText(frame, words.getString("password.required"));
            missing = true;
        }
        if (codeField.getText().trim().length() == 0) {
            Toast.makeText(frame, words.getString("code.required"));
            missing = true;
        }
        return missing;
    }

    /**
     * Set addButton listener.
     * When clicked creates a new connection file and add it's name to userModel.
     * Call checkDisableDeleteButton to see if the button needs to be enabled.
     */
    private void setAddButtonListener() {
        addButton.setMnemonic(KeyEvent.VK_A);
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                userModel.addElement(UserServiceKt.createUser().getName());
                checkDisableDeleteButton();
            }
        });
    }

    /**
     * If there is only one register in userModel disable the deleteButton
     */
    private void checkDisableDeleteButton() {
        deleteButton.setEnabled(userModel.size() > 1);
    }

    /**
     * Set userList from json
     * Set the selection listener and select the first user
     */
    private void setUserList() {
        getUserModelFromJson();
        userList.setModel(userModel);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userList.setLayoutOrientation(JList.VERTICAL);
        setUserSelectionListener();
        selectFirstUser();
    }

    /**
     * Set the selection listener.
     * When a connection file is selected, fill the fields with it's data
     */
    private void setUserSelectionListener() {
        userList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (userList.getSelectedIndex() < 0) {
                    return;
                }
                User user = UserServiceKt.getUserBy((String) userModel.get(userList.getSelectedIndex()));
                if (user != null) {
                    nameField.setText(user.getName());
                    passwordField.setText(user.getPassword());
                    codeField.setText(user.getCode());
                    descriptionArea.setText(user.getDescription());
                    id = user.getId();
                } else {
                    Toast.makeText(frame, words.getString("error.connection.file"));
                }
            }
        });
    }

    /**
     * Select the first connection file from the userList
     */
    private void selectFirstUser() {
        userList.setSelectedIndex(0);
    }

    /**
     * Get the names of all user and put them in the model
     */
    private void getUserModelFromJson() {
        userModel = new DefaultListModel();
        ArrayList<String> allUsersNames = UserServiceKt.getAllUsersNames();
        for (String name: allUsersNames) {
            userModel.addElement(name);
        }
    }

}
