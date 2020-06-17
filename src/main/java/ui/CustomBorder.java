package ui;

import javax.swing.*;
import java.awt.*;

abstract class CustomBorder {

    static void setBottomWhiteBorder(JTextField jTextField) {
        jTextField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE));
    }

}
