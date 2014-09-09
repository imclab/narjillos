package org.nusco.narjillos.creature.body.physics;

import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.physics.ZeroVectorException;

/**
 * This class contains the physics of rotations.
 * 
 * Here are the formulas it uses. To understand them, consider that we don't use
 * inertia - so we can safely replace velocities with positions in space. (In
 * other words, we assume that after each movement of each body, the body's
 * velocity drops down to zero).
 * 
 * Angular Momentum (for the body segments, approximated as thin rods):
 * angular_momentum = moment_of_inertia * angular_velocity
 * moment_of_inertia_around_far_end = mass * length^2 * 16 / 48
 * 
 * Also, by the parallel axis theorem:
 * moment_of_inertia_around_body_center = moment_of_inertia_around_far_end +
 *                                        mass * distance_between_far_end_and_body_center^2 
 *                                       = mass * (length^2 * 16 / 48 + distance_between_far_end_and_body_center^2)
 * 
 * So the angular_momentum is the same as the moment_of_inertia_around_body_center,
 * multiplied by the angular_velocity.
 * 
 * Angular Momentum (for the whole body, approximated as a thin disk):
 * total_angular_velocity = total_angular_momentum / total_moment_of_inertia
 *                        = total_angular_momentum / (mass * radius^2 / 4)
 *                  
 * rotation_energy = moment_of_inertia * angular_velocity^2 / 2;
 */
public strictfp class RotationalForceField implements ForceField {

	private final double bodyMass;
	private final double bodyRadius;
	private final Vector centerOfMass;
	private final List<Double> angularMomenta = new LinkedList<>();
	private double rotationEnergy = 0;
	
	public RotationalForceField(double bodyMass, double bodyRadius, Vector centerOfMass) {
		this.bodyMass = bodyMass;
		this.bodyRadius = bodyRadius;
		this.centerOfMass = centerOfMass;
	}

	@Override
	public void registerMovement(Segment initialPositionInSpace, Segment finalPositionInSpace, double mass) {
		double angularVelocity = calculateAngularVelocity(initialPositionInSpace, finalPositionInSpace);
		double momentOfInertia = calculateMomentOfInertia(finalPositionInSpace, mass);
		double angularMomentum = momentOfInertia * angularVelocity;
		angularMomenta.add(angularMomentum);
		rotationEnergy += calculateRotationEnergy(momentOfInertia, angularVelocity);
	}

	private double calculateAngularVelocity(Segment initialPositionInSpace, Segment finalPositionInSpace) {
		try {
			return finalPositionInSpace.getVector().getAngleWith(initialPositionInSpace.getVector());
		} catch (ZeroVectorException e) {
			return 0;
		}
	}

 	private double calculateMomentOfInertia(Segment positionInSpace, double mass) {
		double length = positionInSpace.getVector().getLength();
		double distance = positionInSpace.getStartPoint().minus(centerOfMass).getLength();
		return mass * length * length * 16 / 48 + distance * distance;
	}

	private double calculateRotationEnergy(double momentOfInertia, double angularVelocity) {
		return momentOfInertia * angularVelocity * angularVelocity / 2;
	}

	public double getRotation() {
		return -getTotalAngularMomentum() / (bodyMass * bodyRadius * bodyRadius / 4);
	}

	private double getTotalAngularMomentum() {
		double result = 0;
		for (double angularMomentum : angularMomenta)
			result += angularMomentum;
		return result;
	}

	@Override
	public double getEnergy() {
		return rotationEnergy * ENERGY_SCALE;
	}
}
