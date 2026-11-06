package net.vinograd.generativeflowfield.presentation;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import net.vinograd.generativeflowfield.element.Particle;
import net.vinograd.generativeflowfield.element.VisualVector;
import net.vinograd.generativeflowfield.field.cosine.TrigonometricField;
import net.vinograd.generativeflowfield.field.integrator.EulerIntegrator;
import net.vinograd.generativeflowfield.field.integrator.RK2Integrator;
import net.vinograd.generativeflowfield.field.vortex.PointVortex;
import net.vinograd.generativeflowfield.field.vortex.SpiralVortex;
import net.vinograd.generativeflowfield.math.rational.Vector2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Controller {

    @FXML
    private StackPane canvasContainer;

    @FXML
    private Canvas canvas;

    @FXML
    private Slider particleSlider;

    @FXML
    private Slider speedSlider;

    @FXML
    private Slider scaleSlider;

    @FXML
    private Slider fadeSlider;

    @FXML
    private Label particleValueLabel;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private Button pauseButton;

    private GraphicsContext graphics;

    private AnimationTimer animationTimer;

    private boolean paused;

    private double time;

    @FXML
    private void initialize() {

        graphics = canvas.getGraphicsContext2D();

        colorPicker.setValue(Color.CORNFLOWERBLUE);

        canvas.widthProperty()
                .bind(canvasContainer.widthProperty());

        canvas.heightProperty()
                .bind(canvasContainer.heightProperty());

        particleSlider.valueProperty().addListener(
                (observable, oldValue, newValue) ->
                        particleValueLabel.setText(
                                Integer.toString(newValue.intValue())
                        )
        );

        init();

        animationTimer = new AnimationTimer() {

            private long previousTime;

            @Override
            public void handle(long now) {

                if (previousTime == 0) {
                    previousTime = now;
                    return;
                }

                double dt = (now - previousTime) / 1_000_000_000.0;

                previousTime = now;

                if (!paused) {
                    update(dt);
                    render(dt);
                }
            }
        };

        animationTimer.start();
    }

    private void update(double dt) {
        time += dt;
    }

    List<Particle> particles = new ArrayList<>();

    Random random = new Random();
    private void init() {
        for (int i = 0; i < 6000; i++) {
            particles.add(new Particle(
                    new Vector2D(random.nextInt(0, 950), random.nextInt(0, 800)),
                    new net.vinograd.generativeflowfield.element.Color(0, 0, 255),
                    5
            ));
        }

    }

    boolean firstFrame = true;
    private void render(double dt) {

        double width = canvas.getWidth();
        double height = canvas.getHeight();

        if (firstFrame) {
            graphics.setFill(Color.rgb(10, 10, 12));
            graphics.fillRect(
                    0,
                    0,
                    width,
                    height
            );
            firstFrame = false;
        }

        graphics.setFill(colorPicker.getValue());

        int vectorSize = 20;

        VisualVector visualVector = new VisualVector(vectorSize, 45);
        TrigonometricField field =
                new TrigonometricField();

        RK2Integrator integrator = new RK2Integrator();

        graphics.setStroke(Color.WHITE);

        graphics.setFill(Color.rgb(10, 10, 12, 0.015));
        graphics.fillRect(
                0,
                0,
                canvas.getWidth(),
                canvas.getHeight()
        );

        graphics.setStroke(Color.WHITE);
        for (Particle particle : particles) {
            Vector2D current = particle.getPosition();
            Vector2D integrate = integrator.integrate(current, field::sample, (float) dt, 100);

            graphics.strokeLine(
                    current.getX(), current.getY(),
                    integrate.getX(), integrate.getY()
            );

            particle.updatePosition(integrate);
        }

//                VisualVector.VectorComponents vectorComponents = visualVector
//                        .forVector(normalizedDirection, x, y);
//                double xEnd = x + vectorComponents.getVectorLineEnd().getX();
//                double yEnd = y + vectorComponents.getVectorLineEnd().getY();
//
//                graphics.setStroke(Color.WHITE);
//                graphics.setLineWidth(1.5);
//                graphics.strokeLine(
//                        x, y,
//                        x + vectorComponents.getVectorLineEnd().getX(), y + vectorComponents.getVectorLineEnd().getY()
//                );
//
//                graphics.strokeLine(
//                        xEnd, yEnd,
//                        xEnd + vectorComponents.getFirstArrowEnd().getX(), yEnd + vectorComponents.getFirstArrowEnd().getY()
//                );
//                graphics.strokeLine(
//                        xEnd, yEnd,
//                        xEnd + vectorComponents.getSecondArrowEnd().getX(), yEnd + vectorComponents.getSecondArrowEnd().getY()
//                );

    }

    @FXML
    private void togglePause() {

        paused = !paused;

        pauseButton.setText(
                paused ? "Resume" : "Pause"
        );
    }

    @FXML
    private void reset() {
        time = 0;
    }

}