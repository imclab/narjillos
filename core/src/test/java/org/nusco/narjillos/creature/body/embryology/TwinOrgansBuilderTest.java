package org.nusco.narjillos.creature.body.embryology;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.nusco.narjillos.creature.body.Organ;
import org.nusco.narjillos.creature.body.BodyPart;
import org.nusco.narjillos.creature.body.embryology.OrganBuilder;
import org.nusco.narjillos.creature.body.embryology.TwinOrgansBuilder;

public class TwinOrgansBuilderTest {
	private static final int MIRRORING = TwinOrgansBuilder.MIRROR_ORGAN_BIT;
	private static final int NOT_MIRRORING = TwinOrgansBuilder.MIRROR_ORGAN_BIT ^ TwinOrgansBuilder.MIRROR_ORGAN_BIT;

	int[] notMirroringGenes1 = new int[] {NOT_MIRRORING, 60, 70, 80, 90, 100};
	int[] notMirroringGenes2 = new int[] {NOT_MIRRORING, 61, 71, 81, 91, 101};
	int[] mirroringGenes1 = new int[] {MIRRORING, 62, 72, 82, 92, 102};
	int[] mirroringGenes2 = new int[] {MIRRORING, 63, 73, 83, 93, 103};

	Organ parent = new OrganBuilder(new int[]{0, 7, 7, 7, 7, 7}).buildHead();

	@Test
	public void buildsRegularSegmentIfNeitherGenesIsMirroring() {
		List<Organ> bodyParts = new TwinOrgansBuilder(notMirroringGenes1, notMirroringGenes2).buildBodyPart(parent);
		
		assertEquals(60, bodyParts.get(0).getLength());
		assertEquals(61, bodyParts.get(1).getLength());
	}

	@Test
	public void buildsMirrorSegmentOfSecondSegmentIfBothGenesAreMirroring() {
		List<Organ> bodyParts = new TwinOrgansBuilder(mirroringGenes1, mirroringGenes2).buildBodyPart(parent);
		
		assertEquals(63, bodyParts.get(0).getLength());
		assertEqualOrgans(bodyParts.get(0), bodyParts.get(1));
	}

	@Test
	public void buildsMirrorSegmentIfFirstSegmentGenesAreMirroring() {
		TwinOrgansBuilder builder = new TwinOrgansBuilder(mirroringGenes1, notMirroringGenes2);
		List<Organ> bodyParts = builder.buildBodyPart(parent);
		
		assertEqualOrgans(bodyParts.get(0), bodyParts.get(1));
	}

	@Test
	public void buildsMirrorSegmentIfSecondSegmentGenesAreMirroring() {
		TwinOrgansBuilder builder = new TwinOrgansBuilder(notMirroringGenes1, mirroringGenes2);
		List<Organ> bodyParts = builder.buildBodyPart(parent);
		
		assertEqualOrgans(bodyParts.get(0), bodyParts.get(1));
	}

	private void assertEqualOrgans(BodyPart organ1, BodyPart organ2) {
		assertEquals(organ1.getLength(), organ2.getLength());
		assertEquals(organ1.getThickness(), organ2.getThickness());
		assertEquals(organ1.getColor(), organ2.getColor());
		assertEquals(-organ1.getAbsoluteAngle(), organ2.getAbsoluteAngle(), 0);
	}
}
