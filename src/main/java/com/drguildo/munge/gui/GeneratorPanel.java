package com.drguildo.munge.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.drguildo.munge.UrlHelper;

class GeneratorPanel extends JPanel {
  private static final long serialVersionUID = -8027104826657674139L;

  public GeneratorPanel() {
    final ArrayList<String> urls = new ArrayList<>();
    final UrlListModel model = new UrlListModel(urls);
    JTable table = new JTable(model);
    GeneratorPanelPopupMenu menu = new GeneratorPanelPopupMenu(table, urls);

    final JTextField prefixField = new JTextField();
    final JTextField suffixField = new JTextField();
    final JSpinner startSpinner = new JSpinner();
    final JSpinner amountSpinner = new JSpinner();
    final JSpinner paddingSpinner = new JSpinner();
    JButton goButton = new JButton("Go");
    goButton.setMnemonic(KeyEvent.VK_G);
    goButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String prefix = prefixField.getText();
        String suffix = suffixField.getText();
        int start = Integer.parseInt(startSpinner.getValue().toString());
        int amount = Integer.parseInt(amountSpinner.getValue().toString());
        int padding = Integer.parseInt(paddingSpinner.getValue().toString());

        for (String url : UrlHelper.generateList(prefix, suffix, padding,
            start, amount)) {
          if (!urls.contains(url)) {
            urls.add(url);
          }
        }

        model.fireTableDataChanged();
      }
    });

    table.setTableHeader(null);
    table.setFillsViewportHeight(true);
    table.addMouseListener(new PanelPopupAdapter(table, menu));

    setLayout(new MigLayout());

    add(new JLabel("Prefix"));
    add(prefixField, "w 100%");
    add(goButton, "spany 2, wrap");

    add(new JLabel("Suffix"));
    add(suffixField, "w 100%, wrap");

    add(new JLabel("Start"), "split 6, skip 1");
    add(startSpinner, "w button");
    add(new JLabel("Amount"));
    add(amountSpinner, "w button");
    add(new JLabel("Padding"));
    add(paddingSpinner, "w button, wrap");

    add(new JScrollPane(table), "w 100%, h 100%, span");
  }
}
