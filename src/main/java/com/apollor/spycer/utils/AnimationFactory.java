package com.apollor.spycer.utils;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class AnimationFactory {
    private AnimationFactory(){}

    /**
     * Fill animation based on HSL for color.
     * Do not include # for style of the color
     * @param n node to animate
     * @param interpolator interpolator transition type
     * @param duration duration of transition
     * @param style style to change
     * @param saturation initial saturation
     * @param lightness initial lightness
     * @param delta_saturation change in saturation
     * @param delta_lightness change in lightness
     * @return animation to play
     */
    public static Animation generateFillTransition(Node n, Interpolator interpolator, Duration duration, String style, double saturation, double lightness, double delta_saturation, double delta_lightness){
        return new Transition() {
            {
                setCycleDuration(duration);
                setInterpolator(interpolator);
            }
            @Override
            protected void interpolate(double v) {
                double s = saturation + (delta_saturation * v);
                double l = lightness + (delta_lightness * v);

                Color c = Color.web("hsl(210, " + s + "%, " + l + "%)");
                n.setStyle(style + " #"+ c.toString().substring(2) + ";");
            }
        };
    }
}
