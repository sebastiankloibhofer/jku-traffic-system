package trafficControlAndDetection;

import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class SensorListModel extends AbstractListModel<Sensor> implements ListModel<Sensor> {
	private static final long serialVersionUID = 1L;
	private List<Sensor> sensors;
	
	public SensorListModel(List<Sensor> sensors) {
		this.sensors = sensors;
	}

	@Override
	public Sensor getElementAt(int index) {
		return sensors.get(index);
	}

	@Override
	public int getSize() {
		return sensors.size();
	}

	public void fireEvents(){
		getListDataListeners();
		for( ListDataListener l : getListDataListeners()  ){
			l.contentsChanged(new ListDataEvent(this, 0 , 0, getSize()));
		}
	}
}