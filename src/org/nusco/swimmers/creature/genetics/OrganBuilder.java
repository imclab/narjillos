package org.nusco.swimmers.creature.genetics;

import org.nusco.swimmers.creature.body.Head;
import org.nusco.swimmers.creature.body.BodyPart;

class OrganBuilder {
	
	public static final int MIN_ORGAN_SIZE = 25;

	static final double PART_LENGTH_MULTIPLIER = 1.0;
	static final double PART_THICKNESS_MULTIPLIER = 0.15;
	static final int PART_MAX_ANGLE_TO_PARENT = 70;
	private static final int MAX_DELAY = 30;

	private final int[] genes;

	public OrganBuilder(int[] partGenes) {
		this.genes = partGenes;
	}

	public Head buildHeadSystem() {
		Head result = new Head(getLength(), getThickness(), getRGB());
		result.sproutNeck();
		return result;
	}

	public BodyPart buildSegment(BodyPart parent, int angleSign) {
		if(getLengthGene() <= MIN_ORGAN_SIZE || getThicknessGene() <= MIN_ORGAN_SIZE)
			return parent.sproutConnectiveTissue();
		return parent.sproutOrgan(getLength(), getThickness(), getAngleToParentAtRest(angleSign), getRGB(), getDelay());
	}

	private int getLengthGene() {
		return genes[1];
	}

	private int getThicknessGene() {
		return genes[2];
	}

	private int getLength() {
		return (int)(getLengthGene() * PART_LENGTH_MULTIPLIER);
	}

	private int getThickness() {
		return (int)(getThicknessGene() * PART_THICKNESS_MULTIPLIER);
	}

	private int getDelay() {
		int delayGene = genes[3];
		return delayGene % MAX_DELAY;
	}

	private int getAngleToParentAtRest(int angleSign) {
		int angleToParentGene = genes[4];
		return (angleToParentGene % PART_MAX_ANGLE_TO_PARENT) * angleSign;
	}
	
	private int getRGB() {
		int rgbGene = genes[5];
		return rgbGene;
	}
}
