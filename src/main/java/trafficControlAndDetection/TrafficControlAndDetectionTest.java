package trafficControlAndDetection;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import controlsystem.ControlSystem;
import controlsystem.model.GraphPart;

import trafficControlAndDetection.TrafficSign.TrafficSignState;

public class TrafficControlAndDetectionTest {

	private TrafficControlAndDetection sys;
	int counter = 0;
	
	@Before
	public void setUp() throws Exception {
		//create instance using interface with test-implementation of the control system
		counter = 0;
		
		sys = new TrafficControlAndDetection(new ControlSystem() {
			@Override
			public void updateParticipantCount(int laneId, int count) {
				assertTrue(laneId >= 0 && count >= 0);
				counter++;
			}

			@Override
			public void reportDamage(int laneId, int severity) {
				// not needed
				
			}

			@Override
			public void reportAccident(int laneId) {
				// not needed
			}
		});
		sys.init();
	}

	@After
	public void tearDown() throws Exception {
		sys.shutdown();
	}

	@Test
	public void testDetectParticipants() {
		HashMap<GraphPart, Integer> data = sys.detectParticipants();
		//check if all sensors delivered valid data
		data.forEach((location, n) -> assertTrue(location != null && n >= 0));
	}
	
	@Test
	public void testTransmitParticipantData() {
		//try getting the data
		Command getData = new Command(Command.Kind.getData, 0, 0);
		sys.receiveCommand(getData);
		
		//check if data from all sensors were transmitted
		assertEquals(sys.sensors.size(), counter);
	}
	
	@Test
	public void testExecuteCommand() {
		//try setting a light to red_yellow
		Command setStateTrafficLight = new Command(Command.Kind.setState, 1, TrafficLight.TrafficLightState.RED_YELLOW.ordinal());
		sys.receiveCommand(setStateTrafficLight);
		assertTrue(sys.actuators.get(1).getState() == TrafficLight.TrafficLightState.RED_YELLOW);
		
		//try setting a sign to speed limit 100
		Command setStateTrafficSign = new Command(Command.Kind.setState, 0, TrafficSignState.SPEED_100.ordinal());
		sys.receiveCommand(setStateTrafficSign);
		assertTrue(sys.actuators.get(0).getState() == TrafficSignState.SPEED_100);
		
		//try setting a camera to something
		Command setStateError = new Command(Command.Kind.setState, 6, 2);
		try {
			sys.receiveCommand(setStateError);
			fail("Setting state of sensor did not fail with an exception.");
		} catch (UnsupportedOperationException e){
		}
	}
	
	@Test
	public void testPedestrianButton() {
		sys.actuators.remove(0); //first element is a sign, remove it for this test
		Actuator.State[] states = new Actuator.State[sys.actuators.size()];
		for (int i = 0; i < sys.actuators.size(); i++) {
			states[i] = sys.actuators.get(i).getState();
		}
		
		try {
			Thread.sleep(8000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//check how many states are still the same
		int matching = 0;
		for (int i = 0; i < sys.actuators.size(); i++) {
			if (sys.actuators.get(i).getState() == states[i]) {
				matching++;
			}
		}
		assertFalse(states.length == matching);
		
	}
}
