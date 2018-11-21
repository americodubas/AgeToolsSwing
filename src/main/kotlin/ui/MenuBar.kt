package ui

import javax.swing.*
import javax.swing.event.MenuEvent
import javax.swing.event.MenuListener
import java.awt.event.KeyEvent

class MenuBar(frame: JFrame) : JMenuBar() {

    init {
        val databaseMenu = JMenu("1 Database")
        val connectionFileMenu = JMenu("2 Connection")
        databaseMenu.mnemonic = KeyEvent.VK_1
        connectionFileMenu.mnemonic = KeyEvent.VK_2
        databaseMenu.addMenuListener(MenuActionListener(frame, DatabaseForm(frame)))
        connectionFileMenu.addMenuListener(MenuActionListener(frame, ConnectionFileForm(frame)))
        this.add(databaseMenu)
        this.add(connectionFileMenu)
    }
}
