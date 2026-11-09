package net.vinograd.generativeflowfield.presentation;

import net.vinograd.generativeflowfield.element.Colour;
import net.vinograd.generativeflowfield.element.Particle;
import net.vinograd.generativeflowfield.field.VectorField;
import net.vinograd.generativeflowfield.field.integrator.RK2Integrator;
import net.vinograd.generativeflowfield.field.integrator.abstr.FieldIntegrator;
import net.vinograd.generativeflowfield.math.rational.Vector2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleEmitter {

    private List<Particle> particles;

    private final VectorField field;

    private final FieldIntegrator integrator;

    public ParticleEmitter(int amount, Colour color, int radius, VectorField field) {
        init(amount, color, radius);

        this.integrator = new RK2Integrator();
        this.field = field;
    }

    private void init(int amount, Colour color, int radius) {
        this.particles = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < amount; i++) {
            particles.add(new Particle(
                          new Vector2D(random.nextInt(0, 950), random.nextInt(0, 800)),
                          color,
                          radius
            ));
        }
    }

    public List<ParticleSegment> sampleAll(float dt, float speed) {
        List<ParticleSegment> samples = new ArrayList<>();

        for (Particle particle : particles) {
            Vector2D current = particle.getPosition();
            Vector2D integrate = integrator.integrate(current, field::sample, dt, speed);

            samples.add(new ParticleSegment(current, integrate, particle.getColor()));
            particle.updatePosition(integrate);
        }

        return samples;
    }

    public record ParticleSegment(Vector2D current, Vector2D integrated, Colour color) {}

}