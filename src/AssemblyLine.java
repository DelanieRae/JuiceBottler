import java.util.ArrayList;
import java.util.List;

public class AssemblyLine {
	
	//initialized variables
    private final List<Orange> oranges;
    
    //Creates new ArrayList
    AssemblyLine() {
        oranges = new ArrayList<Orange>();
    }
    
    //adds oranges to list
    public void addOrange(Orange o) {
        oranges.add(o);
    }
    
    //Makes sure an orange is in the list.  If a orange is in the list then grab it.  If not wait until one is there.
    public synchronized Orange getOrange() {
    	while(oranges.isEmpty()) {
    		try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	notify();
        return oranges.get(0);
    }
    
    //Returns size of List
    public int countOranges() {
        return oranges.size();
    }
}