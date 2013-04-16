package com.drguildo.munge.gui;

import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

class GeneratorPanelPopupMenu extends JPopupMenu {
  private static final long serialVersionUID = 7977142676505471736L;

  public GeneratorPanelPopupMenu(final JTable table, final List<String> urls) {
    JMenuItem copyButton = new JMenuItem("Copy");
    copyButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        StringBuilder builder = new StringBuilder();
        for (int i : table.getSelectedRows()) {
          builder.append(urls.get(i) + "\n");
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
          Main.downloadPanel.add(urls.get(i));
        }
      }
    });
    add(downloadButton);

    addSeparator();

    JMenuItem deleteButton = new JMenuItem("Delete");
    deleteButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // XXX This is duplicated in DownloadPanelPopupMenu

        // We have to delete in reverse because we're modifying the list
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
          urls.remove(i);
          ((UrlListModel) table.getModel()).fireTableDataChanged();
        }
      }
    });
    add(deleteButton);

    JMenuItem clearButton = new JMenuItem("Clear");
    clearButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (urls.size() > 0) {
          urls.clear();
          ((UrlListModel) table.getModel()).fireTableDataChanged();
        }
      }
    });
    add(clearButton);
  }
}
