package trafficControlAndDetection;

import org.junit.Before;
import org.junit.Test;

import controlsystem.model.Node;

public class SensorTest {

	private Camera cam;
	private InductionLoop loop;
	
	@Before
	public void setUp() throws Exception {
		cam = new Camera("testcam", new Node(1, 1, 1), "Main Street");
		loop = new InductionLoop("testloop", new Node(1, 1, 1), "Non-Main Street");
	}

	@Test
	public void testCamera() {
		assert(cam.getData() >= 0);
	}
	
	@Test
	public void testInductionLoop() {
		assert(loop.getData() >= 0);
	}

}
