package com.drguildo.munge.gui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

class UrlListModel extends AbstractTableModel {
  private static final long serialVersionUID = 6599211631415900209L;

  private final String[] columnNames;
  List<String> urls;

  public UrlListModel(List<String> u) {
    columnNames = new String[] { "URL" };
    urls = u;
  }

  @Override
  public int getColumnCount() {
    return columnNames.length;
  }

  @Override
  public int getRowCount() {
    return urls.size();
  }

  @Override
  public Object getValueAt(int row, int column) {
    if (column == 0) {
      return urls.get(row);
    } else {
      return null;
    }
  }

  @Override
  public String getColumnName(int column) {
    return columnNames[column];
  }

  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    return false;
  }
}
