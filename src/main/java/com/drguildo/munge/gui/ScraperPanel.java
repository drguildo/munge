package com.drguildo.munge.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.PatternSyntaxException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;

import net.miginfocom.swing.MigLayout;

import com.drguildo.munge.Scraper;

class ScraperPanel extends JPanel {
  private static final long serialVersionUID = 5620914977366371318L;

  public ScraperPanel() {
    final ArrayList<String> urls = new ArrayList<>();
    final UrlListModel model = new UrlListModel(urls);
    JTable table = new JTable(model);
    ScraperPanelPopupMenu menu = new ScraperPanelPopupMenu(table, urls);

    final SpinnerNumberModel depthSpinnerModel = new SpinnerNumberModel(0, 0,
        10, 1);
    JSpinner depthSpinner = new JSpinner(depthSpinnerModel);

    final JTextField urlField = new JTextField();
    JButton scrapeButton = new JButton("Scrape");
    scrapeButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Thread scraper = new Thread(new Runnable() {
          @Override
          public void run() {
            String url = urlField.getText();
            LogPanel.append("Attempting to scrape " + url);

            try {
              final Collection<String> links = Scraper.scrape(url,
                  depthSpinnerModel.getNumber().intValue());

              SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                  urls.clear();

                  for (String link : links) {
                    urls.add(link);
                  }

                  model.fireTableDataChanged();
                }
              });
            } catch (IllegalArgumentException e) {
              LogPanel.append(e.getMessage());
            }
          }
        });
        scraper.start();
      }
    });

    final TableRowSorter<UrlListModel> sorter = new TableRowSorter<>();
    final JTextField filterField = new JTextField();
    filterField.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void removeUpdate(DocumentEvent e) {
        newFilter();
      }

      @Override
      public void insertUpdate(DocumentEvent e) {
        newFilter();
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
      }

      public void newFilter() {
        RowFilter<UrlListModel, Object> rowFilter = null;
        try {
          rowFilter = RowFilter.regexFilter(filterField.getText(), 0);
        } catch (PatternSyntaxException e) {
          return;
        }
        sorter.setRowFilter(rowFilter);
      }

    });

    table.setFillsViewportHeight(true);
    table.setTableHeader(null);
    table.setRowSorter(sorter);
    table.addMouseListener(new PanelPopupAdapter(table, menu));

    setLayout(new MigLayout());

    add(new JLabel("URL"));
    add(urlField, "w 100%");
    add(new JLabel("Depth"));
    add(depthSpinner);
    add(scrapeButton, "wrap");

    add(new JScrollPane(table), "w 100%, h 100%, span");

    add(new JLabel("Filter"));
    add(filterField, "w 100%, span");
  }
}
