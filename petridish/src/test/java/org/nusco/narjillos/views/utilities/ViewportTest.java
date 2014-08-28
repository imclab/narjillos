package org.nusco.narjillos.views.utilities;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;
import org.nusco.narjillos.ecosystem.Ecosystem;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.views.utilities.Viewport;

//TODO: needs to be rethought if I want to test it. I should move all
//the "flyto" behaviour to a subclass of Viewport
@Ignore
public class ViewportTest {

	@Test
	public void hasTheSameSizeAsTheEcosystemByDefault() {
		Viewport viewport = new Viewport(new Ecosystem(100));

		assertMoreOrLessEquals(Vector.cartesian(100, 100), viewport.getSizeSC());
	}

	@Test
	public void hasAMaximumInitialSize() {
		Viewport viewport = new Viewport(new Ecosystem(100000));

		assertMoreOrLessEquals(Vector.cartesian(Viewport.MAX_INITIAL_SIZE_SC, Viewport.MAX_INITIAL_SIZE_SC), viewport.getSizeSC());
	}

	@Test
	public void canBeResized() {
		Viewport viewport = new Viewport(new Ecosystem(100));
		viewport.setSizeSC(Vector.cartesian(1000, 900));
		
		assertMoreOrLessEquals(Vector.cartesian(1000, 900), viewport.getSizeSC());
	}

	@Test
	public void isCenteredOnTheCenterOfTheEcosystemByDefault() {
		Viewport viewport = new Viewport(new Ecosystem(100));

		assertMoreOrLessEquals(Vector.cartesian(50, 50), viewport.getCenterEC());
	}

	@Test
	public void canBeCenteredOnADifferentPosition() {
		Viewport viewport = createAndStabilizeViewport(100);
		viewport.setCenterSC(Vector.cartesian(100, 200));
		
		assertMoreOrLessEquals(Vector.cartesian(100, 200), viewport.getCenterEC());
	}

	@Test
	public void hasAnUpperAngleInTheOriginByDefault() {
		Viewport viewport = createAndStabilizeViewport(100);
		
		System.out.println(viewport.getPositionEC());
		assertMoreOrLessEquals(Vector.ZERO, viewport.getPositionEC());
	}

	@Test
	public void canBeRecentered() {
		Viewport viewport = createAndStabilizeViewport(800);
		viewport.setSizeSC(Vector.cartesian(100, 400));
		assertMoreOrLessEquals(Vector.cartesian(350, 200), viewport.getPositionEC());
		assertMoreOrLessEquals(Vector.cartesian(400, 400), viewport.getCenterEC());
		assertEquals(1, viewport.getZoomLevel(), 0.001);

		viewport.setCenterSC(Vector.cartesian(300, 500));

		assertMoreOrLessEquals(Vector.cartesian(650, 700), viewport.getCenterEC());
		assertMoreOrLessEquals(Vector.cartesian(600, 500), viewport.getPositionEC());
	}

	@Test
	public void hasADefaultZoomLevelOfOne() {
		Viewport viewport = createAndStabilizeViewport(100);

		assertEquals(1, viewport.getZoomLevel(), 0.001);
	}

	@Test
	public void zoomsToViewTheEntireEcosystemAtTheBeginning() {
		final long ecosystemSize = (long)(Viewport.MAX_INITIAL_SIZE_SC * 10);
		Viewport viewport = createAndStabilizeViewport(ecosystemSize);

		assertEquals(0.1, viewport.getZoomLevel(), 0.001);
	}

	@Test
	public void resizingItDoesNotChangeTheZoomLevel() {
		final long ecosystemSize = (long)(Viewport.MAX_INITIAL_SIZE_SC * 10);
		Viewport viewport = createAndStabilizeViewport(ecosystemSize);

		viewport.setSizeSC(Vector.cartesian(100, 10000));
		assertEquals(0.1, viewport.getZoomLevel(), 0.001);
	}

	@Test
	public void zoomingItDoesNotChangeItsCenter() {
		Viewport viewport = new Viewport(new Ecosystem(100));
		assertMoreOrLessEquals(Vector.cartesian(50, 50), viewport.getCenterEC());

		viewport.zoomIn();

		assertMoreOrLessEquals(Vector.cartesian(50, 50), viewport.getCenterEC());

		viewport.zoomOut();

		assertMoreOrLessEquals(Vector.cartesian(50, 50), viewport.getCenterEC());
	}

	@Test
	public void resizingItDoesNotChangeItsCenter() {
		Viewport viewport = new Viewport(new Ecosystem(100));
		assertMoreOrLessEquals(Vector.cartesian(50, 50), viewport.getCenterEC());

		viewport.setSizeSC(Vector.cartesian(20, 1000));

		assertMoreOrLessEquals(Vector.cartesian(50, 50), viewport.getCenterEC());
	}

	@Test
	public void resizingChangesItsPosition() {
		Viewport viewport = createAndStabilizeViewport(300);
		viewport.setSizeSC(Vector.cartesian(50, 60));
		viewport.setCenterEC(Vector.cartesian(100, 200));
		assertMoreOrLessEquals(Vector.cartesian(75, 170), viewport.getPositionEC());
		
		viewport.setSizeSC(Vector.cartesian(40, 90));

		assertMoreOrLessEquals(Vector.cartesian(100, 200), viewport.getCenterEC());
		assertMoreOrLessEquals(Vector.cartesian(80, 155), viewport.getPositionEC());
	}

	@Test
	public void canZoomIn() {
		Viewport viewport = createAndStabilizeViewport(100);
		viewport.setSizeSC(Vector.cartesian(50, 50));
		viewport.setZoomLevel(0.5);

		viewport.zoomIn();
		
		assertEquals(0.515, viewport.getZoomLevel(), 0.001);

		viewport.zoomIn();
		
		assertEquals(0.5304, viewport.getZoomLevel(), 0.001);
	}

	@Test
	public void canZoomOut() {
		Viewport viewport = createAndStabilizeViewport(100);
		viewport.setZoomLevel(0.2);
		for (int i = 0; i < 500; i++)
			viewport.tick();
		
		assertEquals(0.2, viewport.getZoomLevel(), 0.001);

		viewport.zoomOut();
		
		assertEquals(0.485, viewport.getZoomLevel(), 0.001);

		viewport.zoomOut();
		
		assertEquals(0.471, viewport.getZoomLevel(), 0.001);
	}

	@Test
	public void pansTowardsCenterWhenAtMaxZoomLevel() {
		Viewport viewport = createAndStabilizeViewport(100);
		viewport.setCenterSC(Vector.cartesian(60, 60));
		viewport.setZoomLevel(1);
		for (int i = 0; i < 100; i++)
			viewport.tick();
		assertMoreOrLessEquals(Vector.cartesian(50, 50), viewport.getCenterEC());

		viewport.zoomOut();
		for (int i = 0; i < 100; i++)
			viewport.tick();
		
		assertMoreOrLessEquals(Vector.cartesian(50, 50), viewport.getCenterEC());
	}
	
	@Test
	public void cannotZoomInOverAMaximum() {
		Viewport viewport = new Viewport(new Ecosystem(100));
		for (int i = 0; i < 50; i++)
			viewport.zoomIn();
		
		assertEquals(Viewport.MAX_ZOOM, viewport.getZoomLevel(), 0);
	}

	@Test
	public void zoomsOverTheMaxRegressToTheMax() {
		Viewport viewport = new Viewport(new Ecosystem(100));
		viewport.setSizeSC(Vector.cartesian(50, 50));
		viewport.setZoomLevel(Viewport.MAX_ZOOM + 0.2);

		for (int i = 0; i < 500; i++)
			viewport.tick();
		
		assertEquals(Viewport.MAX_ZOOM, viewport.getZoomLevel(), 0.01);
	}

	@Test
	public void cannotZoomOutToSeeMoreThanTheEntireEcosystem() {
		Viewport viewport = new Viewport(new Ecosystem(100));
		for (int i = 0; i < 100; i++)
			viewport.zoomOut();
		
		assertEquals(0.5, viewport.getZoomLevel(), 0);
	}

	private Viewport createAndStabilizeViewport(long size) {
		Viewport viewport = new Viewport(new Ecosystem(size));
		for (int i = 0; i < 1000; i++)
			viewport.tick();
		return viewport;
	}

	private void assertMoreOrLessEquals(Vector expected, Vector actual) {
		if (!moreOrLessEquals(expected, actual))
			throw new RuntimeException("Expected " + expected + ", got " + actual);
	}

	private boolean moreOrLessEquals(Vector expected, Vector actual) {
		final double delta = 0.1;
		return Math.abs(expected.x - actual.x) < delta && Math.abs(expected.y - actual.y) < delta;
	}
}