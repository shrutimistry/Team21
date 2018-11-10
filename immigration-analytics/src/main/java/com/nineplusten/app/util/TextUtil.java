package com.nineplusten.app.util;

import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class TextUtil {
  public static double getTextWidth(String str) {
    Text text = new Text(str);
    text.setFont(new Font(16));
    Double val = text.getLayoutBounds().getWidth() + 2d;
    if (str.length() <= 5) {
      val += 5d;
    }
    return val;
  }
}
