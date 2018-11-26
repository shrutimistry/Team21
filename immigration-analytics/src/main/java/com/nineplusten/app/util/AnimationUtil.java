package com.nineplusten.app.util;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.layout.Region;
import javafx.util.Duration;

public class AnimationUtil {
  public static Timeline getAlertTimeline(Region messageContainer) {
    messageContainer.setOpacity(1);
    final KeyFrame kf1 = new KeyFrame(Duration.seconds(3), new KeyValue(messageContainer.opacityProperty(), 1));
    final KeyFrame kf2 = new KeyFrame(Duration.seconds(4), new KeyValue(messageContainer.opacityProperty(), 0, Interpolator.EASE_IN));
    final Timeline timeline = new Timeline(kf1, kf2);
    return timeline;
  }
}
