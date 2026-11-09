package net.vinograd.generativeflowfield.util;

import javafx.scene.paint.Color;
import net.vinograd.generativeflowfield.element.Colour;

public class ColourMapper {

    public static Color mapColor(Colour color) {
        return Color.rgb(
                color.getR(),
                color.getG(),
                color.getB()
        );
    }

}