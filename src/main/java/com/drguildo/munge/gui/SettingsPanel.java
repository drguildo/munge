package com.drguildo.munge.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.drguildo.munge.Settings;

class SettingsPanel extends JPanel {
  private static final long serialVersionUID = -1236567283468247600L;

  public SettingsPanel() {
    setLayout(new MigLayout());

    final JTextField browseField = new JTextField();
    browseField.setText(Settings.getDownloadLocation());

    JButton browseButton = new JButton("Browse");
    browseButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int returnVal = fileChooser.showOpenDialog(Main.downloadPanel);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
          String chosenPath = fileChooser.getSelectedFile().toString();

          Settings.setDownloadLocation(chosenPath);
          browseField.setText(chosenPath);
        }
      }
    });

    JPanel downloadPanel = new JPanel();
    downloadPanel.setLayout(new MigLayout());
    downloadPanel.setBorder(BorderFactory
        .createTitledBorder("Default Download Directory"));

    downloadPanel.add(browseField, "w 100%, span 2");
    downloadPanel.add(browseButton, "wrap");
    downloadPanel.add(new JCheckBox());
    downloadPanel.add(new JLabel("Stuff"));

    JPanel networkPanel = new JPanel();
    networkPanel.setLayout(new MigLayout());
    networkPanel.setBorder(BorderFactory.createTitledBorder("Network"));

    networkPanel.add(new JLabel("Concurrent Downloads"));
    networkPanel.add(new JSpinner());

    add(downloadPanel, "w 100%, wrap");
    add(networkPanel, "w 100%");
  }
}
