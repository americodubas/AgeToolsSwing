import ui.CustomColor;
import ui.MainTabbedPane;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class Main {

    /**
     * Create main frame with the main tabbed pane
     * @param args not used
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            UIManager.put("TabbedPane.selected", CustomColor.DARK_BLUE);

            for (Map.Entry<Object, Object> entry : javax.swing.UIManager.getDefaults().entrySet()) {
                Object key = entry.getKey();
                Object value = javax.swing.UIManager.get(key);
                if (value instanceof javax.swing.plaf.FontUIResource) {
                    javax.swing.plaf.FontUIResource fr=(javax.swing.plaf.FontUIResource)value;
                    javax.swing.plaf.FontUIResource f = new javax.swing.plaf.FontUIResource(fr.getFamily(), fr.getStyle(), 16);
                    javax.swing.UIManager.put(key, f);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        JFrame frame = new JFrame("AGE TOOLS");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new MainTabbedPane(frame), BorderLayout.CENTER);
        frame.getContentPane().setBackground(CustomColor.DARK);
        frame.pack();
        frame.dispose();
        frame.setVisible(true);
    }

}
