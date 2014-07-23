package org.nusco.narjillos.creature.body;

import org.nusco.narjillos.creature.body.pns.Nerve;
import org.nusco.narjillos.creature.body.pns.PassNerve;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.ColorByte;

/**
 * An Organ that is invisible to the naked eye---but still contains a Nerve.
 */
class AtrophicOrgan extends BodyPart {

	public AtrophicOrgan(BodyPart parent) {
		this(parent, new PassNerve());
	}

	protected AtrophicOrgan(BodyPart parent, Nerve nerve) {
		super(0, 0, new ColorByte(0), parent, nerve);
	}

	@Override
	public double calculateAbsoluteAngle() {
		return getParent().getAbsoluteAngle();
	}

	@Override
	protected synchronized ColorByte getHue() {
		return getParent().getHue();
	}

	@Override
	public ColorByte calculateColor() {
		return getParent().getColor();
	}

	@Override
	public String toString() {
		return "<null organ>";
	}

	// Optimization: ticking is simpler for ConnectiveTissue.
	// The duplication is ugly, but it does help performance.
	@Override
	public Vector tick(Vector inputSignal) {
		resetAllCaches();
		Vector outputSignal = getNerve().tick(inputSignal);
		tickChildren(outputSignal);
		return outputSignal;
	}

	@Override
	protected void move(Vector signal) {
	}
}