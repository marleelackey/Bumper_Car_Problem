package bumper;
import java.util.*;
import java.util.Scanner;

public class Coordinator {
	static int [] ridersInLine; /* */
	static Thread [] allThreads;
	static Rider [] lineOfRiders;
	static int [] lineOfCars;
	static int numCars;
	static int numRiders;
	static int time;
	int carID;
	static Sleeper s = new Sleeper();
	
	public static void main(String args[]) throws InterruptedException {
		/* read in user input */
		Scanner scan = new Scanner(System.in);
		
		System.out.println("Enter the number of bumper cars: ");
		numCars = scan.nextInt(); /* holds # of bumper cars */
		
		System.out.println("Enter the number of riders: ");
		numRiders = scan.nextInt(); /* holds the number of riders */
		
		System.out.println("Enter the time to run the simulation: ");
		time = scan.nextInt(); /* holds the amt of time to run */
		
		/* set the sizes of arrays now that you know # of cars, riders, and time */
		/* may delete later */  ridersInLine = new int[numRiders]; /* makes a line for riders to wait in, 1 in a position if a person is waiting, 0 if not */
		lineOfRiders = new Rider[numRiders]; /* makes a list of threads of riders */
		lineOfCars = new int[numCars];
		allThreads = new Thread[numRiders];
		
		/* makes threads for riders */
		int count = 0;
		while (count < (numRiders )) { /* will fill the line of riders waiting to ride */
			Rider r = new Rider(count);
			lineOfRiders[count] = r;
			count++;
		}
		
		count = 0;
		while (count < numCars) { /* will ready the cars aka preset them to 0 */
			lineOfCars[count] = 0; /* 0 if there isnt a rider in */
			count++;
		}
		
		/* method that will start the timer, threads, let everything run, and then join the thread */
		simulation();	
		System.exit(0);
		
	}
	
	public static void simulation() throws InterruptedException {
		long start = System.nanoTime();
		long end = (start + time * 1000000000);
		
		/* start threads */
		for(int i = 0; i < lineOfRiders.length ; i++) {
			lineOfRiders[i].start();
		}
		
		while (start < end) {  /* time loop */
			start = System.nanoTime();
		}
		for(int a = 0; a < lineOfRiders.length; a++) {
			lineOfRiders[a].interrupt();
		}
	}
	
	public static int getInLine(int rider) {
		for (int i = 0; i < lineOfCars.length; i++) {
			if (lineOfCars[i] == 0) { /* if the car is empty */
				lineOfCars[i] = 1;
				for(int b = 0; b < lineOfRiders.length; b++) {
					if (lineOfRiders[b].getWaiting() == 1) {
						lineOfRiders[b].isNotWaiting();
						s.rideTime(b, lineOfCars, i);
						
						lineOfRiders[rider].isWaiting();
						sortCars();
						return lineOfCars[i];
					}
				}
				/* put the rider in the car */
				s.rideTime(rider, lineOfCars, i);
				
				/* bubble sort to move empty cars up to the front of the line */
				sortCars();
				
				return lineOfCars[i];
			}
		}
		return -1; /* returns -1 if no available cars */
	}
	
	/* will move empty cars forward after a car is filled/returned */
	public static void sortCars() {
		for (int i = 0; i < lineOfCars.length - 1; i++) {
			int minIndex = i;
			for (int j = i + 1; j < lineOfCars.length; j++ ) {
				if (lineOfCars[i] < lineOfCars[minIndex]) {
					minIndex = j;
				}
			}
			int temp = lineOfCars[minIndex];
			lineOfCars[minIndex] = lineOfCars[i];
			lineOfCars[i] = temp;
		}
	}

	public void returnCar(int carID, int riderID) {
		lineOfCars[carID] = 0; /* will be 0 because there is no longer a rider */
		System.out.println("Rider " + riderID + " returned car " + carID + ".");
		sortCars();
		s.walkAround(riderID);
		
	}
	
	/* this method is called if all bumper cars are filled */
	public synchronized void waitForOthers(int id) throws InterruptedException {
		if (getInLine(id) == -1) {
			lineOfRiders[id].sleep(1);
		}
	}
}
