package trafficControlAndDetection;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;

import controlsystem.ControlSystem;
import controlsystem.model.GraphPart;
import controlsystem.model.Node;
import trafficControlAndDetection.Actuator.State;

public class TrafficControlAndDetection {
	private ControlSystem controller;
	private ControlSystemInterface comms;
	List<Sensor> sensors;
	List<Actuator> actuators;
	ScheduledExecutorService exec;
	
	public TrafficControlAndDetection(ControlSystem controller) {
		this.controller = controller;
		comms = new ControlSystemInterface(controller, this);
		sensors = new ArrayList<Sensor>();
		actuators = new ArrayList<Actuator>();
		exec = new ScheduledThreadPoolExecutor(5);
	}

	HashMap<GraphPart, Integer> detectParticipants() {
		HashMap<GraphPart, Integer> results = new HashMap<GraphPart, Integer>();
		for (Sensor s : sensors) {
			results.put(s.getLocation(), s.getData());
		}
		return results;
	}
	
	public ControlSystemInterface getInterface(){
		return comms;
	}

	void receiveCommand(Command command) throws UnsupportedOperationException {
		if (command.kind == Command.Kind.getData) {
			transmitParticipantData(detectParticipants());
		} else if (command.kind == Command.Kind.setState) {
			boolean success = false;
			for (Actuator a : actuators) { //search for the target device, set the new state
				if (a.id == command.target_id) {
					if (a instanceof TrafficLight && command.state >= 0
							&& command.state < TrafficLight.TrafficLightState.values().length) {
						executeCommand(a, TrafficLight.TrafficLightState.values()[command.state]);
						success = true;
					} else if (a instanceof TrafficSign && command.state >= 0
							&& command.state < TrafficSign.TrafficSignState.values().length) {
						executeCommand(a, TrafficSign.TrafficSignState.values()[command.state]);
						success = true;
					}
				}
			}
			
			if (!success) {
				throw new UnsupportedOperationException("Cannot set state of a sensor.");
			}
		}
	}

	private void transmitParticipantData(HashMap<GraphPart, Integer> map) {
		comms.sendData(map);
	}

	private void executeCommand(Actuator device, State nextState) {
		device.setSignal(nextState);
	}

	private void makeGUI() {
		// create simple GUI
		JFrame frame = new JFrame("Traffic Control and Detection - Device Management");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		final JList<Actuator> actuatorList = new JList<Actuator>();
		ActuatorListModel actuatorListModel = new ActuatorListModel(actuators);
		actuatorList.setModel(actuatorListModel);
		actuatorList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JList<Sensor> sensorList = new JList<Sensor>();
		SensorListModel sensorListModel = new SensorListModel(sensors);
		sensorList.setModel(sensorListModel);
		sensorList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JPanel listPanel = new JPanel();
		listPanel.add(actuatorList);
		JScrollPane scrollpane = new JScrollPane(listPanel);
		scrollpane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		// create the panel for filtering devices
		JPanel selectionPanel = new JPanel();
		selectionPanel.setLayout(new GridLayout(1, 5));
		selectionPanel.add(new JLabel("Type:"));
		JComboBox<String> typeBox = new JComboBox<String>(new String[] { "Actuator", "Sensor" });
		typeBox.addActionListener(ev -> {
			if (typeBox.getSelectedIndex() == 0) {
				listPanel.remove(sensorList);
				listPanel.add(actuatorList);
			} else {
				listPanel.remove(actuatorList);
				listPanel.add(sensorList);
			}

			listPanel.validate();
			listPanel.repaint();
			scrollpane.validate();
			scrollpane.repaint();
		});
		selectionPanel.add(typeBox);
		selectionPanel.add(new JLabel(""));
		selectionPanel.add(new JLabel("Location:"));
		JComboBox<String> locationBox = new JComboBox<String>();
		locationBox.setEditable(true);
		selectionPanel.add(locationBox);

		// create the button panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(4, 1));
		JButton edit = new JButton("Edit");
		edit.addActionListener(ev -> {
			if (typeBox.getSelectedIndex() == 0) {
				if (actuatorList.getSelectedIndex() >= 0) {
					editActuatorDialog(actuators.get(actuatorList.getSelectedIndex()));
					actuatorListModel.fireEvents();
				}
			} else {
				if (sensorList.getSelectedIndex() >= 0) {
					editSensorDialog(sensors.get(sensorList.getSelectedIndex()));
					sensorListModel.fireEvents();
				}
			}
		});
		buttonPanel.add(edit);

		JButton delete = new JButton("Delete");
		delete.addActionListener(ev -> {
			if (typeBox.getSelectedIndex() == 0) {
				int index = actuatorList.getSelectedIndex();
				if (index < 0) {
					JOptionPane.showMessageDialog(frame, "Please select an item to remove first.", "Error",
							JOptionPane.PLAIN_MESSAGE);
				} else {
					actuators.remove(index);
					actuatorListModel.fireEvents();
					actuatorList.getSelectionModel().clearSelection();
				}
			} else {
				int index = sensorList.getSelectedIndex();
				if (index < 0) {
					JOptionPane.showMessageDialog(frame, "Please select an item to remove first.", "Error",
							JOptionPane.PLAIN_MESSAGE);
				} else {
					sensors.remove(index);
					sensorListModel.fireEvents();
					sensorList.getSelectionModel().clearSelection();
				}
			}
		});
		buttonPanel.add(delete);

		JButton create = new JButton("Create");
		create.addActionListener(ev -> {
			Device newDev = newDeviceDialog();
			if (newDev != null) {
				if (newDev instanceof Actuator) {
					actuators.add((Actuator) newDev);
					actuatorListModel.fireEvents();
				} else {
					sensors.add((Sensor) newDev);
					sensorListModel.fireEvents();
				}
			}
		});
		buttonPanel.add(create);

		JButton showState = new JButton("Show State");
		showState.addActionListener(ev -> {
			if (typeBox.getSelectedIndex() == 0) {
				if (actuatorList.getSelectedIndex() >= 0) {
					showStateDialog(actuators.get(actuatorList.getSelectedIndex()));
				}
			} else {
				if (sensorList.getSelectedIndex() >= 0) {
					showStateDialog(sensors.get(sensorList.getSelectedIndex()));
				}
			}
		});
		buttonPanel.add(showState);

		// build the main window
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(selectionPanel, BorderLayout.NORTH);
		mainPanel.add(scrollpane, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.EAST);
		frame.add(mainPanel);
		frame.pack();
		frame.setVisible(true);
	}

	private Device newDeviceDialog() {
		JComboBox<String> typeBox = new JComboBox<String>(
				new String[] { "Camera", "Induction Loop", "Traffic Sign", "Traffic Light" });
		JTextField name = new JTextField();
		JTextField location = new JTextField();

		JComponent[] fields = new JComponent[] { new JLabel("Type:"), typeBox, new JLabel("Name:"), name,
				new JLabel("Location"), location };

		int res = JOptionPane.showConfirmDialog(null, fields, "New Device", JOptionPane.PLAIN_MESSAGE);
		if (res == JOptionPane.OK_OPTION) {
			// create the correct device, using dummy data for the node
			switch (typeBox.getSelectedIndex()) {
			case 0:
				return new Camera(name.getText(), new Node(0, 0, 0), location.getText());
			case 1:
				return new InductionLoop(name.getText(), new Node(0, 0, 0), location.getText());
			case 2:
				return new TrafficSign(name.getText(), new Node(0, 0, 0), location.getText());
			case 3:
				return new TrafficLight(name.getText(), new Node(0, 0, 0), location.getText());
			default:
				return null;
			}
		}

		return null;
	}

	private Device editActuatorDialog(Actuator toEdit) {
		JTextField name = new JTextField(toEdit.name);
		JTextField location = new JTextField(toEdit.locationName);

		JComponent[] fields = new JComponent[] { new JLabel("Device to edit:"), new JLabel(toEdit.toString()),
				new JLabel("Name:"), name, new JLabel("Location"), location };

		int res = JOptionPane.showConfirmDialog(null, fields, "New Device", JOptionPane.PLAIN_MESSAGE);
		if (res == JOptionPane.OK_OPTION) {
			toEdit.name = name.getText();
			toEdit.locationName = location.getText();
		}

		return toEdit;
	}

	private Device editSensorDialog(Sensor toEdit) {
		JTextField name = new JTextField(toEdit.name);
		JTextField location = new JTextField(toEdit.locationName);

		JComponent[] fields = new JComponent[] { new JLabel("Sensor to edit:"), new JLabel(toEdit.toString()),
				new JLabel("Name:"), name, new JLabel("Location"), location };

		int res = JOptionPane.showConfirmDialog(null, fields, "Editing Sensor", JOptionPane.PLAIN_MESSAGE);
		if (res == JOptionPane.OK_OPTION) {
			toEdit.name = name.getText();
			toEdit.locationName = location.getText();
		}

		return toEdit;
	}

	private void showStateDialog(Actuator act) {
		JComponent[] fields = new JComponent[] { new JLabel("Name: " + act.name),
				new JLabel("Location: " + act.location),
				new JLabel("Status: " + (act.isFunctional() ? "ready" : "not ready")),
				new JLabel("Current state: " + act.getState()) };

		JOptionPane.showMessageDialog(null, fields, "Status of device", JOptionPane.PLAIN_MESSAGE);
	}

	private void showStateDialog(Sensor sensor) {
		JComponent[] fields = new JComponent[] { new JLabel("Name: " + sensor.name),
				new JLabel("Location: " + sensor.location),
				new JLabel("Status: " + (sensor.isFunctional() ? "ready" : "not ready")),
				new JLabel("Current data: " + sensor.getData()) };

		JOptionPane.showMessageDialog(null, fields, "Status of device", JOptionPane.PLAIN_MESSAGE);
	}

	void init() {
		// add some test data
		actuators.add(new TrafficSign("Stop", new Node(0, 0, 0), "Main street"));
		actuators.add(new TrafficLight("North-South", new Node(0, 0, 0), "Main street"));
		actuators.add(new TrafficLight("West-East", new Node(0, 0, 0), "Main street"));
		actuators.add(new TrafficLight("Pedestrian", new Node(0, 0, 0), "Main street"));

		sensors.add(new Camera("North", new Node(0, 0, 0), "Main street"));
		sensors.add(new Camera("Traffic", new Node(0, 0, 0), "Main street"));
		sensors.add(new InductionLoop("Traffic", new Node(0, 0, 0), "City boulevard"));

		this.makeGUI();

		// check for participants and transmit the data to control system, do
		// this every 5 secs
		this.exec.scheduleAtFixedRate(() -> {
			transmitParticipantData(detectParticipants());
		} , 5, 5, TimeUnit.SECONDS);

		// traffic lights can dynamically change because participants press the
		// respective buttons on them
		this.exec.scheduleAtFixedRate(() -> {
			int target = (int) (Math.random() * actuators.size());
			if (actuators.get(target) instanceof TrafficLight
					&& ((TrafficLight) actuators.get(target)).getState() == TrafficLight.TrafficLightState.GREEN) {
				((TrafficLight) actuators.get(target)).setSignal(TrafficLight.TrafficLightState.RED);
				try {
					Thread.sleep(3500);
				} catch (Exception e) {
					e.printStackTrace();
				}
				((TrafficLight) actuators.get(target)).setSignal(TrafficLight.TrafficLightState.GREEN);
			}
		} , 2500, 2500, TimeUnit.MILLISECONDS);

	}
	
	void shutdown() {
		exec.shutdown();
	}
	
	public static void main(String[] args) {
		TrafficControlAndDetection sys = new TrafficControlAndDetection(null);
		sys.init();
	}
}
