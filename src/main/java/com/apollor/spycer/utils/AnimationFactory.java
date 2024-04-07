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

                n.setStyle(style + hslToHex(210, s, l) + ";");
            }
        };
    }

    public static String hslToHex(double h, double s, double l) {
        double r, g, b;

        h /= 360.0;
        s /= 100.0;
        l /= 100.0;

        if (s == 0) {
            r = g = b = l;
        } else {
            double q = l < 0.5 ? l * (1 + s) : l + s - l * s;
            double p = 2 * l - q;
            r = hueToRGB(p, q, h + 1.0/3.0);
            g = hueToRGB(p, q, h);
            b = hueToRGB(p, q, h - 1.0/3.0);
        }

        int red = (int) Math.round(r * 255);
        int green = (int) Math.round(g * 255);
        int blue = (int) Math.round(b * 255);

        Color color = Color.rgb(red, green, blue);
        return "#" + color.toString().substring(2).toUpperCase();
    }

    private static double hueToRGB(double p, double q, double t) {
        if (t < 0) t += 1;
        if (t > 1) t -= 1;
        if (t < 1.0 / 6.0) return p + (q - p) * 6.0 * t;
        if (t < 1.0 / 2.0) return q;
        if (t < 2.0 / 3.0) return p + (q - p) * (2.0 / 3.0 - t) * 6.0;
        return p;
    }
}
