package org.nusco.narjillos.embryogenesis;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.narjillos.creature.body.Organ;
import org.nusco.narjillos.genomics.Chromosome;
import org.nusco.narjillos.shared.utilities.ColorByte;

public abstract class ConcreteOrganBuilderTest {

	protected abstract ConcreteOrganBuilder createConcreteOrganBuilder(Chromosome chromosome);

	@Test
	public void decodesALengthBetween1And255() {
		assertEquals(0, createConcreteOrganBuilder(new Chromosome(0, 0, 10)).getLength());
		assertEquals(30, createConcreteOrganBuilder(new Chromosome(0, 0, 30)).getLength());
		assertEquals(42, createConcreteOrganBuilder(new Chromosome(0, 0, 42)).getLength());
		assertEquals(255, createConcreteOrganBuilder(new Chromosome(0, 0, 255)).getLength());
	}

	@Test
	public void veryShortLengthsAtrophyTo0() {
		assertEquals(0, createConcreteOrganBuilder(new Chromosome(0, 29)).getLength());
	}

	@Test
	public void decodesAThicknessBetween1And50() {
		assertEquals(1, createConcreteOrganBuilder(new Chromosome(0, 0, 0, 0)).getThickness());
		assertEquals(1, createConcreteOrganBuilder(new Chromosome(0, 0, 0, 1)).getThickness());
		assertEquals(2, createConcreteOrganBuilder(new Chromosome(0, 0, 0, 6)).getThickness());
		assertEquals(10, createConcreteOrganBuilder(new Chromosome(0, 0, 0, 50)).getThickness());
		assertEquals(50, createConcreteOrganBuilder(new Chromosome(0, 0, 0, 255)).getThickness());
	}

	@Test
	public void decodesAHueBetween0And255() {
		assertEquals(new ColorByte(0), createConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 0)).getHue());
		assertEquals(new ColorByte(1), createConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 1)).getHue());
		assertEquals(new ColorByte(255), createConcreteOrganBuilder(new Chromosome(0, 0, 0, 0, 0, 0, 0, 0, 255)).getHue());
	}

	protected void fullyGrow(Organ bodyPart) {
		bodyPart.growBy(100_000);
		bodyPart.updateGeometry();
	}
}
