package bumper;

public class Rider extends Thread {
	int riderID;
	int waiting = 0; 
	
	Coordinator c = new Coordinator();
	public Rider(int id) {
		riderID = id;
	}
	
	public void isWaiting() {
		waiting = 1;
	}
	
	public void isNotWaiting() {
		waiting = 0;
	}
	
	public int  getWaiting() {
		return waiting;
	}
	public void run() {
		/* starts the process by putting the rider in line */
		if (c.getInLine(riderID) == -1) {
			try {
				c.waitForOthers(riderID);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
	
	
}
