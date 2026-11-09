package net.vinograd.generativeflowfield.presentation;


import net.vinograd.generativeflowfield.element.VisualVector;
import net.vinograd.generativeflowfield.field.VectorField;
import net.vinograd.generativeflowfield.math.rational.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class VectorVisualizer {

    private final VectorField vectorField;

    private final int gridStep;

    private final VisualVector visualVector;

    private final int height;
    private final int width;

    public VectorVisualizer(VectorField vectorField, int gridSize, int height, int width) {
        this.vectorField = vectorField;
        this.gridStep = gridSize;
        this.visualVector = new VisualVector(gridSize / 2, 45);
        this.height = height;
        this.width = width;
    }

    public List<VisualVectorSample> sample() {
        List<VisualVectorSample> samples = new ArrayList<VisualVectorSample>();
        for (int x = 0; x < width; x += gridStep) {
            for (int y = 0; y < height; y += gridStep) {
                VisualVector.VectorComponents vc = visualVector.forVector(vectorField.sample(new Vector2D(x, y)), x, y);

                double xEnd = x + vc.getVectorLineEnd().getX();
                double yEnd = y + vc.getVectorLineEnd().getY();

                Vector2D lineEnd = new Vector2D(xEnd, yEnd);

                VisualVectorSample sample = new VisualVectorSample(
                        lineEnd,
                        new Vector2D(
                                xEnd + vc.getFirstArrowEnd().getX(),
                                yEnd + vc.getFirstArrowEnd().getY()
                        ),
                        new Vector2D(
                                xEnd + vc.getSecondArrowEnd().getX(),
                                yEnd + vc.getSecondArrowEnd().getY()
                        ),
                        x, y, xEnd, yEnd
                );
                samples.add(sample);
            }
        }

        return samples;
    }

    public record VisualVectorSample(Vector2D lineEnd, Vector2D firstArrowEnd, Vector2D secondArrowEnd, int x, int y, double xEnd,  double yEnd) {}

}