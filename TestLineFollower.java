// http://www.inpharmix.com/jps/PID_Controller_For_Lego_Mindstorms_Robots.html



import ch.aplu.ev3.Gear;
import ch.aplu.ev3.LegoRobot;
import ch.aplu.ev3.LightSensor;
import ch.aplu.ev3.SensorPort;

public class TestLineFollower {
	int counter = 0;
	int[] arrayls1 = new int [20];
	int[] arrayls2 = new int [20];
	
	
	

	public TestLineFollower() {
		
		
		
		
		LegoRobot robot = new LegoRobot("192.168.11.31");		
		
		final LightSensor ls1 = new LightSensor(SensorPort.S1);
		final LightSensor ls2 = new LightSensor(SensorPort.S3);
		robot.addPart(ls1);
		robot.addPart(ls2);
		ls1.activate(true);
		ls2.activate(true);
		final Gear gear = new Gear();
		robot.addPart(gear);
		final int variable = 20;
		gear.setSpeed(variable);
		gear.forward();
		
		
		
		
		
		while (!robot.isEscapeHit()) {
			
			arrayls1[counter] = ls1.getValue();
			counter++;
			if (counter == 20)	{
				counter = 0;
			}
			
			for (int i = 0; i < arrayls1.length; i++)	{
				System.out.print(" " + arrayls1[i]);
			}
			
			System.out.println();
			
			gear.forward();
			
			
			while(ls1.getValue() <= 1200)	{
				if (ls1.getValue() <= 800) {
					gear.left();
				} else if (ls1.getValue() <= 1000 && ls1.getValue() > 800) {
					gear.leftArc(0.05);
				} else {
					gear.leftArc(0.2);
				}			
				System.out.println(ls1.getValue() + " ls1");
			}
		
			while(ls2.getValue() <= 1200)	{
				if (ls2.getValue() <= 800) {
					gear.right();
				} else if (ls2.getValue() <= 1000 && ls2.getValue() > 800) {
					gear.rightArc(0.05);
				} else {
					gear.rightArc(0.2);
				}
				System.out.println(ls1.getValue() + " ls2");

			}
			
			
		}
	}
	
	public static void main(String[] args) {
		new TestLineFollower();		
	}

	

	

}