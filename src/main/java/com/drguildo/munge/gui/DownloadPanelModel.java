package com.drguildo.munge.gui;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

class DownloadPanelModel extends AbstractTableModel {
  private static final long serialVersionUID = 2141604764607813502L;

  private final String[] columnNames;
  private final ArrayList<HttpDownloadBar> bars;

  public DownloadPanelModel(ArrayList<HttpDownloadBar> b) {
    columnNames = new String[] { "URL", "Progress" };
    bars = b;
  }

  @Override
  public int getColumnCount() {
    return columnNames.length;
  }

  @Override
  public int getRowCount() {
    return bars.size();
  }

  @Override
  public Object getValueAt(int row, int column) {
    if (column == 0) {
      return bars.get(row).getUrl();
    } else {
      return null;
    }
  }

  @Override
  public String getColumnName(int column) {
    return columnNames[column];
  }

  @Override
  public boolean isCellEditable(int arg0, int arg1) {
    return false;
  }
}
