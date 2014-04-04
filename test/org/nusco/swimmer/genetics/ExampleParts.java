package org.nusco.swimmer.genetics;

import java.util.LinkedList;
import java.util.List;

import org.nusco.swimmer.body.Organ;
import org.nusco.swimmer.physics.Vector;

public class ExampleParts {

	public final static Organ HEAD = Organ.createHead(60, 6, 123);
	public final static Organ CHILD_1 = HEAD.sproutVisibleOrgan(new Vector(50, 0), 50, 9, 30, 123);
	public final static Organ CHILD_2 = HEAD.sproutVisibleOrgan(new Vector(50, 0), 50, 9, -30, 123);
	public final static Organ CHILD_1_1 = CHILD_1.sproutVisibleOrgan(new Vector(30, 0), 30, 7, 30, 123);
	public final static Organ CHILD_1_2 = CHILD_1.sproutVisibleOrgan(new Vector(30, 0), 30, 7, -30, 123);
	public final static Organ CHILD_2_1 = CHILD_2.sproutVisibleOrgan(new Vector(30, 0), 30, 7, 30, 123);
	public final static Organ CHILD_2_2 = CHILD_2.sproutVisibleOrgan(new Vector(30, 0), 30, 7, -30, 123);
	public final static Organ CHILD_1_1_1 = CHILD_1_1.sproutVisibleOrgan(new Vector(50, 0), 50, 6, 20, 123);
	public final static Organ CHILD_1_1_2 = CHILD_1_1.sproutVisibleOrgan(new Vector(50, 0), 50, 6, -20, 123);
	public final static Organ CHILD_2_2_1 = CHILD_2_2.sproutVisibleOrgan(new Vector(50, 0), 50, 6, 20, 123);
	public final static Organ CHILD_2_2_2 = CHILD_2_2.sproutVisibleOrgan(new Vector(50, 0), 50, 6, -20, 123);

	public static List<Organ> asList() {
		List<Organ> expected = new LinkedList<>();
		expected.add(HEAD);
		expected.add(CHILD_1);
		expected.add(CHILD_1_1);
		expected.add(CHILD_1_1_1);
		expected.add(CHILD_1_1_2);
		expected.add(CHILD_1_2);
		expected.add(CHILD_2);
		expected.add(CHILD_2_1);
		expected.add(CHILD_2_2);
		expected.add(CHILD_2_2_1);
		expected.add(CHILD_2_2_2);
		return expected;
	}
}