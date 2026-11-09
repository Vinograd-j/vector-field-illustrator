package net.vinograd.generativeflowfield.element;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.vinograd.generativeflowfield.math.rational.Vector2D;

@AllArgsConstructor
@Getter
public class Line {

    private Vector2D startPoint;

    private Vector2D endPoint;

    private int thickness;

    private Colour color;

}