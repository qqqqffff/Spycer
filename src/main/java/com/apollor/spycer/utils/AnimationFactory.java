package com.apollor.spycer.utils;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class AnimationFactory {
    private AnimationFactory(){
        //TODO: read default colors
    }

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

    /**
     * Generator for Transformation transition for rectangles
     * @param r rectangle
     * @param interpolator interpolator for transition type
     * @param duration duration of the animation
     * @param deltaDimensions vector representing the delta of the target dimensions in form [w, h]. Leave either parameter as 0 to not transform
     * @return animation to play
     */
    public static Animation generateTransformTransition(Rectangle r, Interpolator interpolator, Duration duration, double[] deltaDimensions){
        double[] initialDimensions = new double[]{r.getWidth(), r.getHeight()};
        return new Transition() {
            {
                setCycleDuration(duration);
                setInterpolator(interpolator);
            }
            @Override
            protected void interpolate(double v) {
                r.setWidth(initialDimensions[0] + deltaDimensions[0] * v);
                r.setHeight(initialDimensions[1] + deltaDimensions[1] * v);
            }
        };
    }

    /**
     * Generator for translation transitions using layout parameter
     * @param n Node to translate
     * @param interpolator interpolator for transition type
     * @param duration duration of the transition
     * @param deltaTranslation vector representing the delta of the target dimensions minus the initial dimensions in form [x, y]. Leave either parameter as 0 to not transform
     * @return animation of the transition
     */
    public static Animation generateTranslateTransition(Node n, Interpolator interpolator, Duration duration, double[] deltaTranslation){
        double[] initialDimensions = new double[]{n.getLayoutX(), n.getLayoutY()};
        return new Transition() {
            {
                setCycleDuration(duration);
                setInterpolator(interpolator);
            }
            @Override
            protected void interpolate(double v) {
                n.setLayoutX(initialDimensions[0] + deltaTranslation[0] * v);
                n.setLayoutY(initialDimensions[1] + deltaTranslation[1] * v);
            }
        };
    }

    /**
     * Generator for translate transitions using translation parameter
     * @param n node to translate
     * @param interpolator interpolator
     * @param duration duration of animation
     * @param deltaTranslation vector representing the delta of the target dimensions minus the initial dimensions in form [x, y]. Leave either parameter as 0 to not transform
     * @return animation of the transition
     */
    public static Animation generateTranslateTransition2(Node n, Interpolator interpolator, Duration duration, double[] deltaTranslation){
        double[] initialDimensions = new double[]{n.getTranslateX(), n.getTranslateY()};
        return new Transition() {
            {
                setCycleDuration(duration);
                setInterpolator(interpolator);
            }
            @Override
            protected void interpolate(double v) {
                n.setTranslateX(initialDimensions[0] + deltaTranslation[0] * v);
                n.setTranslateY(initialDimensions[1] + deltaTranslation[1] * v);
            }
        };
    }

    public static Animation generateOpacityTransition(Node n, Interpolator interpolator, Duration duration, boolean direction){
        return new Transition(){
            {
                setCycleDuration(duration);
                setInterpolator(interpolator);
            }
            @Override
            protected void interpolate(double v){
                double delta = direction ? -1*v : v;
                n.setOpacity(1 + delta);
            }
        };
    }

    public static EventHandler<MouseEvent> generateDefaultButtonMouseEnterAnimation(Node n){
        return event -> {
            Animation animation = generateFillTransition(
                    n,
                    Interpolator.EASE_IN,
                    Duration.millis(150),
                    "-fx-background-color: ",
                    9.0,
                    92.0,
                    5.0,
                    -53.0
            );
            animation.play();
        };
    }
    public static EventHandler<MouseEvent> generateDefaultButtonMouseExitAnimation(Node n){
        return event -> {
            Animation animation = AnimationFactory.generateFillTransition(
                    n,
                    Interpolator.EASE_OUT,
                    Duration.millis(150),
                    "-fx-background-color: ",
                    14,
                    39,
                    -5.0,
                    53.0
            );
            animation.play();
        };
    }
    public static EventHandler<MouseEvent> generateDefaultTextFieldMouseEnterAnimation(Node n){
        return event -> {
            Animation animation = AnimationFactory.generateFillTransition(
                    n,
                    Interpolator.EASE_IN,
                    Duration.millis(100),
                    "-fx-background-color: ",
                    29.0,
                    24.0,
                    -15.0,
                    15.0
            );
            animation.play();
        };
    }
    public static EventHandler<MouseEvent> generateDefaultTextFieldMouseExitAnimation(Node n){
        return event -> {
            Animation animation = AnimationFactory.generateFillTransition(
                    n,
                    Interpolator.EASE_OUT,
                    Duration.millis(100),
                    "-fx-background-color: ",
                    14.0,
                    39,
                    15.0,
                    -15.0
            );
            animation.play();
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
