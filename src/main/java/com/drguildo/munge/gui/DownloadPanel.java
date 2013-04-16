package com.drguildo.munge.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class DownloadPanel extends JPanel {
  private static final long serialVersionUID = 1461859514398096054L;

  private final ArrayList<HttpDownloadBar> bars;
  private final DownloadPanelModel model;
  public JTable table;
  private final DownloadPanelPopupMenu menu;
  private final JTextField urlField;
  private final JButton addButton;
  private final JButton startButton;
  private final JButton stopButton;

  public DownloadPanel() {
    bars = new ArrayList<>();
    model = new DownloadPanelModel(bars);
    table = new JTable(model);
    menu = new DownloadPanelPopupMenu(table, bars);

    urlField = new JTextField();
    addButton = new JButton("Add");
    addButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        add(urlField.getText());
      }
    });

    startButton = new JButton("Start");
    startButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        for (int i : table.getSelectedRows()) {
          bars.get(i).start();
        }
      }
    });

    stopButton = new JButton("Stop");
    stopButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        for (int i : table.getSelectedRows()) {
          bars.get(i).stop();
        }
      }
    });

    table.addMouseListener(new PanelPopupAdapter(table, menu));
    table.setFillsViewportHeight(true);
    table.getColumnModel().getColumn(0).setPreferredWidth(600);
    table.getColumnModel().getColumn(1)
        .setCellRenderer(new DownloadProgressRenderer(bars));

    setLayout(new MigLayout());

    add(urlField, "w 100%");
    add(addButton, "wrap");

    add(new JScrollPane(table), "w 100%, h 100%, span, wrap");

    add(startButton);
    add(stopButton);
  }

  public void add(String url) {
    try {
      URL u = new URL(url);
      bars.add(new HttpDownloadBar(u));
    } catch (MalformedURLException e) {
      LogPanel.append(e.getMessage());
    }
    model.fireTableRowsInserted(0, bars.size());
  }
}
