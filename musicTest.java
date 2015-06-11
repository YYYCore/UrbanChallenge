
import ch.aplu.ev3.ColorLabel;
import ch.aplu.ev3.ColorSensor;
import ch.aplu.ev3.Gear;
import ch.aplu.ev3.LegoRobot;
import ch.aplu.ev3.LightSensor;
import ch.aplu.ev3.Sensor;
import ch.aplu.ev3.SensorPort;


public class musicTest {	
	
	
	public musicTest() {
		
		
		LegoRobot robot = new LegoRobot("192.168.11.36");	
		


		
		final ColorSensor cls = new ColorSensor(SensorPort.S4);
		final LightSensor lls = new LightSensor(SensorPort.S1);
		final LightSensor rls = new LightSensor(SensorPort.S3);
		
		
		robot.addPart(cls);
		robot.addPart(lls);
		robot.addPart(rls);
		
		lls.activate(true);
		rls.activate(true);
		
		
		
//		final Gear gear = new Gear();
//		robot.addPart(gear);
//		final int speed = 10;
//		gear.setSpeed(speed);
//		gear.forward();
		
		double x = 250;
		double volume = 30;
		
		System.out.println("test");
	
		
		robot.setVolume(80);
		
		

		double c = 523.25;
		double d = 587.33;
		double es = 622.25;
		double e = 659.26;
		double f = 698.4;
		double g = 783.99;
		double a = 880;
		double h = 987.77;
		
		
		
//		alleMeineEntchen(robot, c, d, e, f, g, a, h);
		elise(robot, c, d, e, es, f, g, a, h);
		
		
		
		System.out.println("done");
		
	}
	
public static void elise(LegoRobot robot, double c, double d, double e, double es, double f, double g, double a, double h){
		
		double x = 150;
		int octave = 2;
		
		robot.playTone(octave*e, x);
		robot.playTone(octave*es, x);
		robot.playTone(octave*e, x);
		
		robot.playTone(octave*es, x);
		robot.playTone(octave*e, 2*x);
		robot.playTone(octave*h, 2*x);
		
		robot.playTone(a, x);
		robot.playTone(a, x);
		robot.playTone(a, x);
		robot.playTone(a, x);
		robot.playTone(g, 4*x);
		
		robot.playTone(a, x);
		robot.playTone(a, x);
		robot.playTone(a, x);
		robot.playTone(a, x);
		robot.playTone(g, 4*x);
		
		robot.playTone(f, x);
		robot.playTone(f, x);
		robot.playTone(f, x);
		robot.playTone(f, x);
			
	
		robot.playTone(e, 2*x);
		robot.playTone(e, 2*x);
		
		robot.playTone(e, x);
		robot.playTone(e, x);
		robot.playTone(e, x);
		robot.playTone(e, x);
		
		robot.playTone(c, 4*x);
	}
	
	
	
	
	public static void alleMeineEntchen(LegoRobot robot, double c, double d, double e, double f, double g, double a, double h){
		
		double x = 250;
		
		robot.playTone(c, x);
		robot.playTone(d, x);
		robot.playTone(e, x);
		robot.playTone(f, x);
		robot.playTone(g, 2*x);
		robot.playTone(g, 2*x);
		
		robot.playTone(a, x);
		robot.playTone(a, x);
		robot.playTone(a, x);
		robot.playTone(a, x);
		robot.playTone(g, 4*x);
		
		robot.playTone(a, x);
		robot.playTone(a, x);
		robot.playTone(a, x);
		robot.playTone(a, x);
		robot.playTone(g, 4*x);
		
		robot.playTone(f, x);
		robot.playTone(f, x);
		robot.playTone(f, x);
		robot.playTone(f, x);
			
	
		robot.playTone(e, 2*x);
		robot.playTone(e, 2*x);
		
		robot.playTone(e, x);
		robot.playTone(e, x);
		robot.playTone(e, x);
		robot.playTone(e, x);
		
		robot.playTone(c, 4*x);
	}

	public static void main(String[] args) {
		try{
			new musicTest();	
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	

}