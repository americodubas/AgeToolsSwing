package ui;

import javax.swing.*;

abstract class EnableButton {

    static void onTextChange(JTextField jTextField, String oldText, JButton jButton) {
        jTextField.getDocument().addDocumentListener((CustomDocumentListener) () -> {
            if (!jTextField.getText().equals(oldText)) {
                jButton.setEnabled(true);
            }
        });
    }

}
