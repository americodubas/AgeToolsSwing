package ui;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

@FunctionalInterface
public interface CustomDocumentListener extends DocumentListener {

    void change();

    @Override
    default void insertUpdate(DocumentEvent e) {
        change();
    }

    @Override
    default void removeUpdate(DocumentEvent e) {
        change();
    }

    @Override
    default void changedUpdate(DocumentEvent e) {
        change();
    }
}
