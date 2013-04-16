package com.drguildo.munge;

import java.io.File;

public class Settings {
  static String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:11.0) Gecko/20100101 Firefox/11.0";
  static String downloadLocation = System.getProperty("user.home")
      + File.separator + "Downloads";
  static int maxConcurrentDownloads = 2;

  public static int getMaxConcurrentDownloads() {
    return maxConcurrentDownloads;
  }

  // XXX newMax should be greater than 0.
  public static void setMaxConcurrentDownloads(int newMax) {
    maxConcurrentDownloads = newMax;
  }

  public static String getDownloadLocation() {
    return downloadLocation;
  }

  public static void setDownloadLocation(String newLocation) {
    downloadLocation = newLocation;
  }
}
