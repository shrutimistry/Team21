package com.nineplusten.app.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import com.openhtmltopdf.DOMBuilder;

public class ReportUtil {
  public static Document html5ParseDocument(String urlStr, int timeoutMs) throws IOException 
  {
      URL url = new URL(urlStr);
      org.jsoup.nodes.Document doc;
      
      if (url.getProtocol().equalsIgnoreCase("file")) {
          doc = Jsoup.parse(new File(url.getPath()), "UTF-8");
      }
      else {
          doc = Jsoup.parse(url, timeoutMs);  
      }
      
      return DOMBuilder.jsoup2DOM(doc);
  }
}
