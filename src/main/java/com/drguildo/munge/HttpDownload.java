package com.drguildo.munge;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

// TODO: Handle invalid URLs.
// TODO: Spoof referrer.
// TODO: Handle various HTTP status codes.
public class HttpDownload implements Runnable {
  private final URL url;
  private File path;

  // The current percentage completion of the download.
  private HttpDownloadStatus status = HttpDownloadStatus.READY;

  private final ArrayList<ProgressListener> progressListeners = new ArrayList<>();
  private final ArrayList<StatusListener> statusListeners = new ArrayList<>();

  public HttpDownload(URL u) {
    url = u;
  }

  public HttpDownload(URL u, File p) {
    url = u;
    path = p;
  }

  @Override
  public void run() {
    boolean append = false;
    int progress = 0;

    File outputFile;
    String remoteFilename = UrlHelper.getFilename(url);

    if (path.isDirectory()) {
      if (!path.exists()) {
        path.mkdirs();
      }
      outputFile = new File(path, remoteFilename);
    } else {
      outputFile = path;
    }

    try {
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      int contentLength = connection.getContentLength();

      System.out.println("HTTP response: " + connection.getResponseMessage());

      if (outputFile.exists()) {
        if (outputFile.length() < contentLength) {
          append = true;
        } else {
          changeStatus(HttpDownloadStatus.FAILED);
          throw new IOException(outputFile + " already exists.");
        }
      }

      long offset = 0L;
      int bytesRead = 0;
      byte[] buffer = new byte[1024];

      InputStream in = connection.getInputStream();
      BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(
          outputFile, append));

      if (append) {
        offset = outputFile.length();
        in.skip(outputFile.length());
      }

      changeStatus(HttpDownloadStatus.RUNNING);

      bytesRead = in.read(buffer);
      while (bytesRead != -1 && status == HttpDownloadStatus.RUNNING) {
        offset = offset + bytesRead;

        // We know the file size.
        if (contentLength != -1) {
          int curProgress = (int) ((offset / (float) contentLength) * 100);

          if (curProgress != progress) {
            progress = curProgress;

            for (ProgressListener listener : progressListeners) {
              listener.update(progress);
            }
          }
        }

        out.write(buffer, 0, bytesRead);

        bytesRead = in.read(buffer);
      }

      if (status != HttpDownloadStatus.STOPPED) {
        changeStatus(HttpDownloadStatus.FINISHED);
      }

      in.close();
      out.flush();
      out.close();
    } catch (IOException e) {
      changeStatus(HttpDownloadStatus.FAILED);
    }
  }

  public void stop() {
    if (status == HttpDownloadStatus.RUNNING) {
      changeStatus(HttpDownloadStatus.STOPPED);
    }
  }

  private void changeStatus(HttpDownloadStatus newStatus) {
    status = newStatus;
    for (StatusListener listener : statusListeners) {
      listener.update(status);
    }
  }

  public void registerProgressListener(ProgressListener progressListener) {
    if (!progressListeners.contains(progressListener)) {
      progressListeners.add(progressListener);
    }
  }

  public void registerStatusListener(StatusListener statusListener) {
    if (!statusListeners.contains(statusListener)) {
      statusListeners.add(statusListener);
    }
  }

  public URL getUrl() {
    return url;
  }

  public File getPath() {
    return path;
  }
}
