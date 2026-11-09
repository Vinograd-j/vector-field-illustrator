package net.vinograd.generativeflowfield.element;

import lombok.Getter;
import net.vinograd.generativeflowfield.math.rational.Vector2D;

@Getter
public class Particle {

    private Vector2D position;

    private Colour color;

    private final double radius;

    public Particle(Vector2D initialPosition, Colour color, double radius) {
        this.position = initialPosition;
        this.color = color;
        this.radius = radius;
    }

    public void updateColor(Colour color) {
        this.color = color;
    }

    public void updatePosition(Vector2D position) {
        this.position = position;
    }

}