package com.drguildo.munge.gui;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

class Main {
  private static GeneratorPanel generatorPanel = new GeneratorPanel();
  private static ScraperPanel scraperPanel = new ScraperPanel();
  public static DownloadPanel downloadPanel = new DownloadPanel();
  private static SettingsPanel settingsPanel = new SettingsPanel();
  private static LogPanel logPanel = new LogPanel();

  private static void createAndShowGUI() {
    JFrame frame = new JFrame("Munge");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setPreferredSize(new Dimension(800, 600));

    JTabbedPane tabbedPane = new JTabbedPane();
    tabbedPane.add(generatorPanel, "Generator");
    tabbedPane.add(scraperPanel, "Scraper");
    tabbedPane.add(downloadPanel, "Download");
    tabbedPane.add(settingsPanel, "Settings");
    tabbedPane.add(logPanel, "Log");

    frame.add(tabbedPane);

    frame.pack();
    frame.setVisible(true);
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        createAndShowGUI();

        Main.downloadPanel.add("http://www.google.com/robots.txt");
        Main.downloadPanel
            .add("http://ftp.gnome.org/pub/gnome/binaries/win32/gtk+/2.24/gtk+_2.24.10-1_win32.zip");
      }
    });
  }
}
