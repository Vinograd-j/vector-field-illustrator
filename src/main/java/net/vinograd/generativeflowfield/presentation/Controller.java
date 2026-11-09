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
import net.vinograd.generativeflowfield.element.Colour;
import net.vinograd.generativeflowfield.element.Particle;
import net.vinograd.generativeflowfield.field.cosine.TrigonometricField;
import net.vinograd.generativeflowfield.math.rational.Vector2D;
import net.vinograd.generativeflowfield.util.ColourMapper;

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
                    new Colour(0, 0, 255),
                    5
            ));
        }

    }

    boolean firstFrame = true;
    boolean debug = true;
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

            if (debug)
                drawVectorField();

            firstFrame = false;
        }

        if (debug)
            return;

        graphics.setFill(colorPicker.getValue());

        graphics.setStroke(Color.WHITE);

        renderParticles(dt);
        fadeScreen();

    }

    TrigonometricField field = new  TrigonometricField();
    private final ParticleEmitter emitter = new ParticleEmitter(6000, new Colour(0, 0, 255), 5, field);
    private void renderParticles(double dt) {
        graphics.setStroke(Color.WHITE);
        emitter.sampleAll((float) dt, 100).forEach(em ->  {
            graphics.setStroke(ColourMapper.mapColor(em.color()));
            graphics.strokeLine(
                em.current().getX(), em.current().getY(),
                em.integrated().getX(), em.integrated().getY()
            );
        });
    }

    private void fadeScreen() {
        graphics.setFill(Color.rgb(10, 10, 12, 0.015));
        graphics.fillRect(
                0,
                0,
                canvas.getWidth(),
                canvas.getHeight()
        );
    }

    private void drawVectorField() {
        VectorVisualizer vectorVisualizer = new VectorVisualizer(field, 40, (int) canvas.getHeight(), (int) canvas.getWidth());

        List<VectorVisualizer.VisualVectorSample> samples = vectorVisualizer.sample();

        graphics.setStroke(Color.WHITE);
        samples.forEach(sample -> {
            graphics.setLineWidth(1.5);
            graphics.strokeLine(
                    sample.x(), sample.y(),
                    sample.lineEnd().getX(), sample.lineEnd().getY()
            );

            graphics.strokeLine(
                    sample.xEnd(), sample.yEnd(),
                    sample.firstArrowEnd().getX(), sample.firstArrowEnd().getY()
            );
            graphics.strokeLine(
                    sample.xEnd(), sample.yEnd(),
                    sample.secondArrowEnd().getX(), sample.secondArrowEnd().getY()
            );

        });

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