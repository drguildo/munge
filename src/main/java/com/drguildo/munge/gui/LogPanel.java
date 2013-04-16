package com.drguildo.munge.gui;

import java.awt.Font;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.miginfocom.swing.MigLayout;

public class LogPanel extends JPanel {
  private static final long serialVersionUID = -4103972122984288759L;

  private static JTextArea textArea = new JTextArea();

  public LogPanel() {
    textArea.setFont(Font.decode(Font.MONOSPACED));
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);

    setLayout(new MigLayout());

    add(new JScrollPane(textArea), "w 100%, h 100%");
  }

  public static void append(String text) {
    Date date = new Date();
    textArea.append(String.format("[%s] %s", date, text));
  }
}
