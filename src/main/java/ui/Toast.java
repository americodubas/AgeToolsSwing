package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;


class Toast extends JDialog {

    static void makeText(JFrame frame, String message) {
        final JDialog dialog = new Toast(frame, message);
        Timer timer = new Timer(Toast.LENGTH_SHORT, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
                dialog.dispose();
            }
        });
        timer.setRepeats(false);
        timer.start();
        dialog.setVisible(true);
    }

    private final String msg;
    private final JFrame frame;
    private static final int LENGTH_SHORT = 2000;

    private Toast(JFrame frame, String msg) {
        super(frame, true);
        this.msg = msg;
        this.frame=frame;
        initComponents();
    }

    private void initComponents() {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        addComponentListener(new ComponentAdapter() {
            // Give the window an rounded rect shape.

            @Override
            public void componentResized(ComponentEvent e) {
                setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
            }
        });
        setUndecorated(true);
        setSize(300, 50);
        setLocationRelativeTo(frame);// adding toast to frame or use null
        getContentPane().setBackground(Color.BLACK);

        // Determine what the GraphicsDevice can support.
        GraphicsEnvironment ge =
                GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        final boolean isTranslucencySupported =
                gd.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.TRANSLUCENT);

        //If shaped windows aren't supported, exit.
        if (!gd.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.PERPIXEL_TRANSPARENT)) {
            System.err.println("Shaped windows are not supported");
        }

        //If translucent windows are supported, 
        //create an opaque window.
        // Set the window to 50% translucency, if supported.
        if (isTranslucencySupported) {
            setOpacity(0.5f);
        } else {
            System.out.println("Translucency is not supported.");
        }

        JLabel label = new JLabel();
        label.setForeground(Color.WHITE);
        label.setText(msg);
        label.setFont(new Font("Verdana",Font.BOLD, 18));
        add(label);

    }
}