package com.drguildo.munge.gui;

import java.lang.Thread.UncaughtExceptionHandler;
import java.net.URL;

import javax.swing.JProgressBar;

import com.drguildo.munge.HttpDownload;
import com.drguildo.munge.HttpDownloadStatus;
import com.drguildo.munge.ProgressListener;
import com.drguildo.munge.StatusListener;

public class HttpDownloadBar extends JProgressBar {
  private static final long serialVersionUID = 4105196859291532752L;

  private final URL url;
  private final HttpDownload download;
  private Thread thread;

  public HttpDownloadBar(URL u) {
    url = u;

    download = new HttpDownload(url);
    thread = new Thread(download);

    download.registerProgressListener(new ProgressListener() {
      @Override
      public void update(int progress) {
        setIndeterminate(false);
        setStringPainted(true);
        setValue(progress);
      }
    });
    download.registerStatusListener(new StatusListener() {
      @Override
      public void update(HttpDownloadStatus status) {
        System.out.println(status);
        if (status == HttpDownloadStatus.FINISHED) {
          setIndeterminate(false);
          setValue(100);
        }
      }
    });

    setBorderPainted(false);
  }

  public void start() {
    LogPanel.append(String.format("Attempting to download %s to %s",
        download.getUrl(), download.getPath()));

    if (thread.getState() == Thread.State.TERMINATED) {
      // The download thread has already been run once so we need to
      // create
      // a new one to run it again.
      thread = new Thread(download);
    }

    setIndeterminate(true);

    thread.setUncaughtExceptionHandler(handler);
    thread.start();
  }

  public void stop() {
    download.stop();
  }

  UncaughtExceptionHandler handler = new UncaughtExceptionHandler() {
    @Override
    public void uncaughtException(Thread thread, Throwable exception) {
      setIndeterminate(false);
      setToolTipText(exception.toString());
    }
  };

  /*
   * <datura> drguildo: from looking at BasicProgressBarUI the problem could be
   * that the JProgressBar is not Displayable because it's not in any widet
   * containment hierarchy. if that's the case, the BasicProgressBarUI's
   * animation timer is not started. <datura> drguildo:
   * http://www.javalobby.org/java/forums/t16703.html#91957095
   */
  @Override
  public boolean isDisplayable() {
    return true;
  }

  @Override
  public void repaint() {
    // TODO: This is pig disgusting and needs fixing.
    Main.downloadPanel.table.repaint();
  }

  public URL getUrl() {
    return url;
  }
}
