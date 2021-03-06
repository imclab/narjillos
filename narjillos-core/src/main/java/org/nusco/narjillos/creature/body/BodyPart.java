package org.nusco.narjillos.creature.body;

import org.nusco.narjillos.creature.body.pns.DelayNerve;
import org.nusco.narjillos.creature.body.pns.Nerve;
import org.nusco.narjillos.shared.utilities.ColorByte;

/**
 * A segment in the body of a creature.
 */
public class BodyPart extends MovingOrgan {

	private static final double SKEWING_VELOCITY_RATIO = 0.1;
	
	private final double angleToParentAtRest;
	private final int orientation; // -1 or 1
	private final int amplitude;
	private final int skewing;

	private double currentSkewing = 0;
	private double cachedMetabolicRate = -1;

	public BodyPart(int adultLength, int adultThickness, ColorByte hue, ConnectedOrgan parent, int delay, int angleToParentAtRest, int amplitude, int skewing) {
		super(adultLength, adultThickness, calculateColorMix(parent, hue), parent, new DelayNerve(delay), angleToParentAtRest);
		this.angleToParentAtRest = angleToParentAtRest;
		this.orientation = (int) Math.signum(angleToParentAtRest);
		this.amplitude = amplitude;
		this.skewing = skewing;
	}

	BodyPart(Nerve nerve) {
		this(0, 0, new ColorByte(0), null, 13, 0, 1, 0);
	}

	public double getAngleToParentAtRest() {
		return angleToParentAtRest;
	}

	public int getOrientation() {
		return orientation;
	}

	public int getAmplitude() {
		return amplitude;
	}

	public int getSkewing() {
		return skewing;
	}

	public int getDelay() {
		return ((DelayNerve) getNerve()).getDelay();
	}

	@Override
	protected double calculateNewAngleToParent(double targetAmplitudePercent, double angleToTarget) {
		double unbentAmplitude = orientation * targetAmplitudePercent * amplitude;
		return angleToParentAtRest + unbentAmplitude + calculateSkewing(angleToTarget);
	}
	
	@Override
	protected double calculateAbsoluteAngle() {
		return getParent().getAbsoluteAngle() + getAngleToParent();
	}

	@Override
	protected double getMetabolicRate() {
		if (cachedMetabolicRate == -1)
			cachedMetabolicRate = getParent().getMetabolicRate();
		return cachedMetabolicRate;
	}

	protected double calculateSkewing(double angleToTarget) {
		double targetSkewing = (angleToTarget % 180) / 180 * getSkewing();
		currentSkewing += getSkewingVelocity(targetSkewing);
		return currentSkewing;
	}

	private static ColorByte calculateColorMix(ConnectedOrgan parent, ColorByte color) {
		if (parent == null)
			return color;
		return parent.getColor().mix(color);
	}

	private double getSkewingVelocity(double targetSkewing) {
		double result = targetSkewing - currentSkewing;
		double maxSkewingVelocity = getMetabolicRate() * SKEWING_VELOCITY_RATIO;
		if (Math.abs(result) > maxSkewingVelocity)
			return Math.signum(result) * maxSkewingVelocity;
		return result;
	}
}
