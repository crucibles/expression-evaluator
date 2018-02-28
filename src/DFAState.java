import java.util.Vector;

public class DFAState {
	private String stateCategory;
	private String stateName;
    private String destination0;
    private String destination1;
	private Vector<DFAState> dfaVector = new Vector<DFAState>();

	public DFAState() {

	}

	public DFAState(String stateCategory, String stateName, String destination0, String destination1) {
        this.stateCategory = stateCategory;
		this.stateName = stateName;
		this.destination0 = destination0;
		//DFAState ds = new DFAState(stateCategory, stateName, destination0, destination1);
		this.dfaVector.add(this);
	}

	public String getStateCategory() {
		return this.stateCategory;
	}

	public String getStateName() {
		return this.stateName;
	}

	public String getDestination0() {
		return this.destination0;
    }
    
    public String getDestination1() {
		return this.destination1;
	}

	public Vector<DFAState> getVector() {
		return this.dfaVector;
	}
}