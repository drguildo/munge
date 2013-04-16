package com.drguildo.munge;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Scraper {
  public static Collection<String> scrape(String url, int depth) {
    return scrape(url, depth, new ArrayList<String>());
  }

  public static Collection<String> scrape(String url, int depth,
      List<String> visited) {
    // XXX This shouldn't be an assertion.
    assert depth >= 0;

    if (visited.contains(url)) {
      System.out.println("Skipping " + url);
      return new ArrayList<>();
    }

    System.out.println("Scraping " + url);

    Connection con = Jsoup.connect(url);

    Document doc;
    try {
      doc = con.userAgent(Settings.userAgent).get();
    } catch (IOException e) {
      doc = new Document("");
    }

    TreeSet<String> images = new TreeSet<>(elemAttrs(doc, "img", "abs:src"));
    TreeSet<String> urls = new TreeSet<>();

    for (String u : elemAttrs(doc, "a", "abs:href")) {
      if (u.startsWith("http")) {
        urls.add(u);
      }
    }

    urls.addAll(images);

    if (depth > 0) {
      for (String u : urls) {
        visited.add(url);
        urls.addAll(scrape(u, depth - 1, visited));
      }
    }

    return urls;
  }

  // Returns a list of the values of all the specified tag attribute contained
  // in the specified document.
  private static List<String> elemAttrs(Document doc, String tag, String attr) {
    ArrayList<String> values = new ArrayList<>();

    for (Element element : doc.getElementsByTag(tag)) {
      String value = element.attr(attr);
      if (value.equals("")) {
        values.add(value);
      }
    }

    return values;
  }

  // Searches the specified HTML for strings that look like video URLs.
  public static Collection<String> findVideos(String html) {
    TreeSet<String> videos = new TreeSet<>();
    String fixedHtml = html.replaceAll("\n", "").replaceAll("\r", "")
        .replaceAll("\t", " ");

    for (String str : fixedHtml.split("\"")) {
      if (str.contains(".flv") || str.contains(".mp4")) {
        String url;

        System.out.println(str);

        try {
          url = URLDecoder.decode(str.substring(str.indexOf("http")), "UTF-8");
        } catch (UnsupportedEncodingException e) {
          System.err.println(e.getMessage());
          continue;
        }

        if (str.contains("&amp;")) {
          videos.add(url.substring(0, url.indexOf("&amp;")));
        } else {
          videos.add(url);
        }
      }
    }

    return videos;
  }
}
