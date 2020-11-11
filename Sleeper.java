package bumper;

import java.util.Random;
public class Sleeper {
	
	static Coordinator c = new Coordinator();
	public static void rideTime (int rider, int[] carArray, int car) {
		Random generator = new Random ( );
		int milliseconds = (generator.nextInt(5) +1 ) * 1000;
		System.out.println ("Rider " + rider + " is now riding in car " + car);

		try {
			Thread.currentThread().sleep( milliseconds);
		}
		catch( InterruptedException e ){}
		c.returnCar(car, rider);
		walkAround(rider);
	}

	public static void walkAround (int rider) {

		Random generator = new Random ( );
		int milliseconds = (generator.nextInt(10) +1 ) * 1000;

		System.out.println ("Rider " + rider + " is walking around the park.");
		try {
			Thread.currentThread().sleep( milliseconds);
		}
		catch( InterruptedException e ){ }
		c.getInLine(rider);

	}

}

