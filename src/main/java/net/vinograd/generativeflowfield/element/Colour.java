package net.vinograd.generativeflowfield.element;

import lombok.Getter;

@Getter
public class Colour {

    private final int r, g, b;

    public Colour(int r, int g, int b) {
        validateOrThrow(r);
        validateOrThrow(g);
        validateOrThrow(b);

        this.r = r;
        this.g = g;
        this.b = b;
    }

    public static int bound(int component) {
        return Math.min(Math.max(component, 0), 255);
    }

    private void validateOrThrow(int component) {
        if (component < 0 || component > 255)
            throw new IllegalArgumentException("Invalid color component " + component);
    }

}