package com.drguildo.munge;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UrlHelper {
  /**
   * Generates a list of URLs by substituting the format specifier with a range
   * of numbers.
   */
  public static List<String> generateList(String prefix, String suffix,
      int padding, int start, int amount) {
    ArrayList<String> urls = new ArrayList<>();

    if (amount < 1) {
      return new ArrayList<>();
    }

    String fmt = "";
    if (padding > 0) {
      fmt = "%0" + padding + "d";
    } else {
      fmt = "%d";
    }

    for (int i = start; i < (start + amount); i++) {
      urls.add(prefix + String.format(fmt, i) + suffix);
    }

    return urls;
  }

  /**
   * Returns the absolute version of a relative URL.
   *
   * @throws MalformedURLException
   */
  public static String absolute(String parent, String child)
      throws MalformedURLException {
    return new URL(new URL(parent), child).toString();
  }

  public static String getFilename(URL url) {
    String defaultFilename = "default.out";

    String path = url.getPath();
    String filename = path.substring(path.lastIndexOf('/') + 1);

    if (filename.length() > 0) {
      return filename;
    } else {
      return defaultFilename;
    }
  }
}
