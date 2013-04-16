package com.drguildo.munge.gui;

import java.awt.Component;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

class DownloadProgressRenderer implements TableCellRenderer {
  List<HttpDownloadBar> bars;

  public DownloadProgressRenderer(List<HttpDownloadBar> b) {
    bars = b;
  }

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value,
      boolean isSelected, boolean hasFocus, int row, int column) {
    return bars.get(row);
  }
}
