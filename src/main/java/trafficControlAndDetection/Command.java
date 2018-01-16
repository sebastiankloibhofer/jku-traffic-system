package trafficControlAndDetection;

public class Command {
	enum Kind {
		getData, setState
	}
	
	Kind kind;
	int target_id;
	int state;
	
	public Command(Kind kind, int target_id, int state){
		this.kind = kind;
		this.target_id = target_id;
		this.state = state;
	}
}
