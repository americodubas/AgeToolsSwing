package ui;

import javax.swing.*;

final class CustomIcon {

    private static final CustomIcon INSTANCE = new CustomIcon();

    private CustomIcon(){}

    static CustomIcon getInstance() {
        return INSTANCE;
    }

    final ImageIcon accountIcon = new ImageIcon(this.getClass().getResource("/images/account.png"));
    final ImageIcon addIcon = new ImageIcon(this.getClass().getResource("/images/add.png"));
    final ImageIcon clearIcon = new ImageIcon(this.getClass().getResource("/images/clear.png"));
    final ImageIcon saveIcon = new ImageIcon(this.getClass().getResource("/images/save.png"));
    final ImageIcon storageIcon = new ImageIcon(this.getClass().getResource("/images/storage.png"));
    final ImageIcon syncIcon = new ImageIcon(this.getClass().getResource("/images/sync.png"));
}
