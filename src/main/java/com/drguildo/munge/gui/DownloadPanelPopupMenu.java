package com.drguildo.munge.gui;

import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

class DownloadPanelPopupMenu extends JPopupMenu {
  private static final long serialVersionUID = -8742128923066318131L;

  public DownloadPanelPopupMenu(final JTable table,
      final ArrayList<HttpDownloadBar> bars) {
    JMenuItem copyButton = new JMenuItem("Copy");
    copyButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        StringBuilder builder = new StringBuilder();
        for (int i : table.getSelectedRows()) {
          System.out.println(bars.get(i).getUrl());
        }

        StringSelection selection = new StringSelection(builder.toString());
        getToolkit().getSystemClipboard().setContents(selection, selection);
      }
    });
    add(copyButton);

    JMenuItem removeButton = new JMenuItem("Remove");
    removeButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // XXX This is duplicated in GeneratorPanelPopupMenu

        // We have to remove in reverse because we're modifying the list
        // in-place and so when i is removed, any greater element
        // becomes i-1.
        int[] sr = table.getSelectedRows();
        int[] selectedRows = new int[sr.length];

        int j = 0;
        for (int i = selectedRows.length - 1; i >= 0; i--) {
          selectedRows[j] = sr[i];
          j++;
        }

        for (int i : selectedRows) {
          bars.remove(i);
          ((DownloadPanelModel) table.getModel()).fireTableDataChanged();
        }
      }
    });
    add(removeButton);
  }
}
