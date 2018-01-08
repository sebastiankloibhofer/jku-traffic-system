package trafficControlAndDetection;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

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

public class TrafficControlAndDetection {

	private ControlSystemInterface comms = new ControlSystemInterface();
	List<Sensor> sensors = new ArrayList<Sensor>();
	List<Actuator> actuators = new ArrayList<Actuator>();

	private void detectParticipants() {

	}

	private void transmitParticipantData() {

	}

	private void executeCommand(String command) {

	}

	private void makeGUI() {
		// create simple GUI
		JFrame frame = new JFrame("Traffic Control and Detection - Device Management");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		actuators.add(new TrafficSign("Stop", "Main street"));
		actuators.add(new TrafficLight("North-South", "Main street"));
		actuators.add(new TrafficLight("West-East", "Main street"));
		actuators.add(new TrafficLight("Pedestrian", "Main street"));

		sensors.add(new Camera("North", "Main street"));
		sensors.add(new Camera("Traffic", "Main street"));
		sensors.add(new InductionLoop("Traffic", "City boulevard"));

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
			switch (typeBox.getSelectedIndex()) {
			case 0:
				return new Camera(name.getText(), location.getText());
			case 1:
				return new InductionLoop(name.getText(), location.getText());
			case 2:
				return new TrafficSign(name.getText(), location.getText());
			case 3:
				return new TrafficLight(name.getText(), location.getText());
			default:
				return null;
			}
		}

		return null;
	}

	private Device editActuatorDialog(Actuator toEdit) {
		JTextField name = new JTextField(toEdit.name);
		JTextField location = new JTextField(toEdit.location);

		JComponent[] fields = new JComponent[] { new JLabel("Device to edit:"), new JLabel(toEdit.toString()),
				new JLabel("Name:"), name, new JLabel("Location"), location };

		int res = JOptionPane.showConfirmDialog(null, fields, "New Device", JOptionPane.PLAIN_MESSAGE);
		if (res == JOptionPane.OK_OPTION) {
			toEdit.name = name.getText();
			toEdit.location = location.getText();
		}

		return toEdit;
	}

	private Device editSensorDialog(Sensor toEdit) {
		JTextField name = new JTextField(toEdit.name);
		JTextField location = new JTextField(toEdit.location);

		JComponent[] fields = new JComponent[] { new JLabel("Sensor to edit:"), new JLabel(toEdit.toString()),
				new JLabel("Name:"), name, new JLabel("Location"), location };

		int res = JOptionPane.showConfirmDialog(null, fields, "Editing Sensor", JOptionPane.PLAIN_MESSAGE);
		if (res == JOptionPane.OK_OPTION) {
			toEdit.name = name.getText();
			toEdit.location = location.getText();
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

	public static void main(String[] args) {
		TrafficControlAndDetection sys = new TrafficControlAndDetection();
		sys.makeGUI();
	}

}
