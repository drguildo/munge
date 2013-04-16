package com.drguildo.munge.gui;

import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

class ScraperPanelPopupMenu extends JPopupMenu {
  private static final long serialVersionUID = 5656289941837059997L;

  public ScraperPanelPopupMenu(final JTable table, final List<String> urls) {
    JMenuItem copyButton = new JMenuItem("Copy");
    copyButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        StringBuilder builder = new StringBuilder();
        for (int i : table.getSelectedRows()) {
          builder.append(urls.get(table.convertRowIndexToModel(i)) + "\n");
        }

        StringSelection selection = new StringSelection(builder.toString());
        getToolkit().getSystemClipboard().setContents(selection, selection);
      }
    });
    add(copyButton);

    JMenuItem downloadButton = new JMenuItem("Download");
    downloadButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        for (int i : table.getSelectedRows()) {
          Main.downloadPanel.add(urls.get(table.convertRowIndexToModel(i)));
        }
      }
    });
    add(downloadButton);
  }
}
