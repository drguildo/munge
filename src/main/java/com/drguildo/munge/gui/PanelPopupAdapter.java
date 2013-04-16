package com.drguildo.munge.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;
import javax.swing.JTable;

class PanelPopupAdapter extends MouseAdapter {
  private final JTable table;
  private final JPopupMenu menu;

  public PanelPopupAdapter(JTable t, JPopupMenu m) {
    table = t;
    menu = m;
  }

  private void maybeShowPopup(MouseEvent e) {
    if (table.getSelectedRowCount() > 0 && e.isPopupTrigger()) {
      menu.show(e.getComponent(), e.getX(), e.getY());
    }
  }

  @Override
  public void mousePressed(MouseEvent e) {
    maybeShowPopup(e);
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    maybeShowPopup(e);
  }
}
