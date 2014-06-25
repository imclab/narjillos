package org.nusco.swimmers.creature.body;

import java.util.LinkedList;
import java.util.List;

import org.nusco.swimmers.creature.body.pns.Nerve;
import org.nusco.swimmers.physics.Vector;

public abstract class Organ {

	protected final int length;
	protected final int thickness;
	protected final double relativeAngle;
	protected final int color;

	private Nerve nerve;

	protected final Organ parent;
	private List<Organ> children = new LinkedList<>();
	private MovementListener movementListener = MovementListener.NULL;
	
	protected Organ(int length, int thickness, int relativeAngle, int rgb, Nerve nerve, Organ parent) {
		this.length = length;
		this.thickness = thickness;
		this.relativeAngle = relativeAngle;
		this.color = rgb;
		this.nerve = nerve;
		this.parent = parent;
	}
	
	public int getLength() {
		return length;
	}

	public int getThickness() {
		return thickness;
	}

	public double getRelativeAngle() {
		return relativeAngle;
	}

	public int getColor() {
		return color;
	}

	public abstract Vector getStartPoint();

	public Vector getEndPoint() {
		return getStartPoint().plus(Vector.polar(getAngle(), length));
	}
	
	public abstract double getAngle();

	public Organ getParent() {
		return parent;
	}

	public List<Organ> getChildren() {
		return children;
	}

	public final Vector tick(Vector inputSignal) {
		Vector beforeVector = getVector();
		Vector beforeStartPoint = getStartPoint();

		Vector outputSignal = getNerve().tick(inputSignal);
		move(outputSignal);
		
		notifyMovementListener(beforeVector, beforeStartPoint, getVector(), getStartPoint());

		tickChildren(outputSignal);

		return outputSignal;
	}

	protected abstract void move(Vector signal);

	private void notifyMovementListener(Vector beforeVector, Vector beforeStartPoint, Vector afterVector, Vector afterStartPoint) {
		movementListener.moveEvent(beforeVector, beforeStartPoint, afterVector, afterStartPoint);
	}

	private void tickChildren(Vector signal) {
		for(Organ child : getChildren())
			child.tick(signal);
	}

	public Vector getVector() {
		return Vector.polar(getAngle(), getLength());
	}
	
	public Nerve getNerve() {
		return nerve;
	}

	public double getMass() {
		return getLength() * getThickness();
	}

	public void setMovementListener(MovementListener listener) {
		movementListener = listener;
		for(Organ child : getChildren())
			child.setMovementListener(listener);
	}
	
	@Override
	public int hashCode() {
		return length;
	}

	@Override
	public boolean equals(Object obj) {
		Organ other = (Organ) obj;
		return color == other.color && length == other.length && relativeAngle == other.relativeAngle  && thickness == other.thickness;
	}

	// for debugging
	public Vector peek = Vector.ZERO;

	public Organ sproutOrgan(int length, int thickness, int relativeAngle, int rgb) {
		return addChild(new Segment(length, thickness, relativeAngle, rgb, this));
	}

	Organ sproutOrgan(Nerve nerve) {
		return addChild(new Segment(nerve));
	}

	public Organ sproutConnectiveTissue() {
		return addChild(new ConnectiveTissue(this));
	}

	protected Organ addChild(Organ child) {
		children.add(child);
		return child;
	}
}