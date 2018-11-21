package ui

import interfaces.Panel
import javax.swing.JFrame
import javax.swing.event.MenuEvent
import javax.swing.event.MenuListener

class MenuActionListener(val frame: JFrame, val panel: Panel): MenuListener {
    override fun menuSelected(e: MenuEvent?) {
        frame.contentPane.removeAll()
        frame.contentPane = panel.getPanel()
        frame.contentPane.revalidate()
        frame.contentPane.repaint()
    }

    override fun menuCanceled(e: MenuEvent?) {

    }

    override fun menuDeselected(e: MenuEvent?) {

    }
}