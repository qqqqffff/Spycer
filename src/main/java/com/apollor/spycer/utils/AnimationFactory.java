package com.apollor.spycer.utils;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
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
     * @param hsl array of the hue saturation and lightness
     * @param delta_hsl array of the delta hue, delta saturation, and delta lightness
     * @return animation to play
     */
    public static Animation generateFillTransition(Node n, Interpolator interpolator, Duration duration, String style, double[] hsl, double[] delta_hsl){
        return new Transition() {
            {
                setCycleDuration(duration);
                setInterpolator(interpolator);
            }
            @Override
            protected void interpolate(double v) {
                double h = hsl[0] + (delta_hsl[0] * v);
                double s = hsl[1] + (delta_hsl[1] * v);
                double l = hsl[2] + (delta_hsl[2] * v);

                n.setStyle(style + ColorHandler.hslToHex(h, s, l) + ";");
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

    /**
     * Generator for opacity transition using a directional parameter
     * @param n node to animate
     * @param interpolator interpolator
     * @param duration duration of animation
     * @param direction true for increase in opacity, false otherwise
     * @return animation of the transition
     */
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

    /**
     * Generator for opacity transition using a directional parameter
     * @param n node to animate
     * @param interpolator interpolator
     * @param duration duration of animation
     * @param direction true for increase in opacity, false otherwise
     * @param delta difference in the starting and ending opacities
     * @return animation of the transition
     */
    public static Animation generateOpacityTransition2(Node n, Interpolator interpolator, Duration duration, boolean direction, double delta){
        double opacityi = n.getOpacity();
        System.out.println(opacityi);
        return new Transition(){
            {
                setCycleDuration(duration);
                setInterpolator(interpolator);
            }
            @Override
            protected void interpolate(double v){
                double d = direction ? -1 * v * delta : v * delta;
                System.out.println(d);
                n.setOpacity(opacityi + d);
            }
        };
    }

    public static EventHandler<MouseEvent> generateDefaultButtonMouseEnterAnimation(Node n){
        //TODO: make it so there are no repeated computation
        double[] hsl1 = ColorHandler.hsvToHSL(ColorHandler.hexToHSV(ColorHandler.palette.get("-tertiary-color")));
        double[] hsl2 = ColorHandler.hsvToHSL(ColorHandler.hexToHSV(ColorHandler.palette.get("-t-tertiary-color")));

        double[] delta_hsl = new double[3];
        for(int i = 0; i < hsl1.length; i++){
            delta_hsl[i] = hsl2[i] - hsl1[i];
        }

        return event -> {
            Animation animation = generateFillTransition(
                    n,
                    Interpolator.EASE_IN,
                    Duration.millis(150),
                    "-fx-background-color: ",
                    hsl1,
                    delta_hsl
            );
            animation.play();
        };
    }
    public static EventHandler<MouseEvent> generateDefaultButtonMouseExitAnimation(Node n){
        double[] hsl1 = ColorHandler.hsvToHSL(ColorHandler.hexToHSV(ColorHandler.palette.get("-tertiary-color")));
        double[] hsl2 = ColorHandler.hsvToHSL(ColorHandler.hexToHSV(ColorHandler.palette.get("-t-tertiary-color")));

        double[] delta_hsl = new double[3];
        for(int i = 0; i < hsl1.length; i++){
            delta_hsl[i] = hsl1[i] - hsl2[i];
        }

        return event -> {
            Animation animation = AnimationFactory.generateFillTransition(
                    n,
                    Interpolator.EASE_OUT,
                    Duration.millis(150),
                    "-fx-background-color: ",
                    hsl2,
                    delta_hsl
            );
            animation.play();
        };
    }

    public static EventHandler<MouseEvent> generateDefaultTextFieldMouseEnterAnimation(Node n){
        double[] hsl1 = ColorHandler.hsvToHSL(ColorHandler.hexToHSV(ColorHandler.palette.get("-secondary-color")));
        double[] hsl2 = ColorHandler.hsvToHSL(ColorHandler.hexToHSV(ColorHandler.palette.get("-t-secondary-color")));

        double[] delta_hsl = new double[3];
        for(int i = 0; i < hsl1.length; i++){
            delta_hsl[i] = hsl2[i] - hsl1[i];
        }

        return event -> {
            Animation animation = AnimationFactory.generateFillTransition(
                    n,
                    Interpolator.EASE_IN,
                    Duration.millis(150),
                    "-fx-background-color: ",
                    hsl1,
                    delta_hsl
            );
            animation.play();
        };
    }
    public static EventHandler<MouseEvent> generateDefaultTextFieldMouseExitAnimation(Node n){
        double[] hsl1 = ColorHandler.hsvToHSL(ColorHandler.hexToHSV(ColorHandler.palette.get("-secondary-color")));
        double[] hsl2 = ColorHandler.hsvToHSL(ColorHandler.hexToHSV(ColorHandler.palette.get("-t-secondary-color")));

        double[] delta_hsl = new double[3];
        for(int i = 0; i < hsl1.length; i++){
            delta_hsl[i] = hsl1[i] - hsl2[i];
        }

        return event -> {
            Animation animation = generateFillTransition(
                    n,
                    Interpolator.EASE_OUT,
                    Duration.millis(150),
                    "-fx-background-color: ",
                    hsl2,
                    delta_hsl
            );
            animation.play();
        };
    }


    public static EventHandler<MouseEvent> generateDefault2TextFieldMouseEnterAnimation(Node n) {
        double[] hsl1 = ColorHandler.hsvToHSL(ColorHandler.hexToHSV(ColorHandler.palette.get("-primary-color")));
        double[] hsl2 = ColorHandler.hsvToHSL(ColorHandler.hexToHSV(ColorHandler.palette.get("-t-primary-color")));

        double[] delta_hsl = new double[3];
        for(int i = 0; i < hsl1.length; i++){
            delta_hsl[i] = hsl2[i] - hsl1[i];
        }

        return event -> {
            Animation animation = generateFillTransition(
                    n,
                    Interpolator.EASE_IN,
                    Duration.millis(150),
                    "-fx-background-color: ",
                    hsl1,
                    delta_hsl
            );
            if(!n.isFocused()) {
                animation.play();
            }
        };
    }

    public static EventHandler<MouseEvent> generateDefault2TextFieldMouseExitAnimation(Node n) {
        double[] hsl1 = ColorHandler.hsvToHSL(ColorHandler.hexToHSV(ColorHandler.palette.get("-primary-color")));
        double[] hsl2 = ColorHandler.hsvToHSL(ColorHandler.hexToHSV(ColorHandler.palette.get("-t-primary-color")));

        double[] delta_hsl = new double[3];
        for(int i = 0; i < hsl1.length; i++){
            delta_hsl[i] = hsl1[i] - hsl2[i];
        }

        return event -> {
            Animation animation = generateFillTransition(
                    n,
                    Interpolator.EASE_OUT,
                    Duration.millis(150),
                    "-fx-background-color: ",
                    hsl2,
                    delta_hsl
            );
            if(!n.isFocused()) {
                animation.play();
            }
        };
    }
}
