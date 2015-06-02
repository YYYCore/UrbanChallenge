
import ch.aplu.ev3.ColorLabel;
import ch.aplu.ev3.ColorSensor;
import ch.aplu.ev3.Gear;
import ch.aplu.ev3.LegoRobot;
import ch.aplu.ev3.LightSensor;
import ch.aplu.ev3.Sensor;
import ch.aplu.ev3.SensorPort;


public class BarCodeScannerRe {	
	
	
	public BarCodeScannerRe() {
		
		
		LegoRobot robot = new LegoRobot("192.168.11.31");	
		


		
		final ColorSensor cls = new ColorSensor(SensorPort.S4);
		final LightSensor lls = new LightSensor(SensorPort.S1);
		final LightSensor rls = new LightSensor(SensorPort.S3);
		
		
		robot.addPart(cls);
		robot.addPart(lls);
		robot.addPart(rls);
		
		lls.activate(true);
		rls.activate(true);
		
		
		
		final Gear gear = new Gear();
		robot.addPart(gear);
		final int speed = 12;
		gear.setSpeed(speed);
		gear.forward();
		
		
		start(robot, cls, gear, lls, rls);
		robot.exit();
	}
	
	
	
	
	
	public static void start(LegoRobot robot, ColorSensor cls, Gear gear, LightSensor lls, LightSensor rls){



		while (!robot.isEscapeHit()) {
			
			
			
			
			color color = getColor(cls.getColorStr(), cls.getColorLabel(), cls.getColorID());
			
			if (color == color.red){
				gear.resetLeftMotorCount();
				
				startScan(robot, lls, rls);
				
		
				
			}

		}

	}
	
	
	public static void startScan(LegoRobot robot, LightSensor lls, LightSensor rls){
		color[] array = new color[10];
		boolean scanning = true;
		int value = 0;
		int count = 0;
		
		while (scanning){
			while (value > 400) {
				
			}
			if (value < 400) {
				array[count] = color.black;
				
			}
			
		}
//		If (lls.getValue())
		
		
	}
	
	


	

	

	public static color getColor(String String, ColorLabel Label, int ID){
		if ((String == "RED") || (Label.toString() == "RED") || ID == 5) {
			return color.red;
		} else if ((String == "WHITE") || (Label.toString() == "WHITE") || ID == 6){ 
			return color.white;
		} else if ((String == "BLACK") || (Label.toString() == "BLACK") || ID == 1) 
			return color.black;
		else {
			return null;
		}
	}
	
	
	public static void main(String[] args) {
		try{
			new BarCodeScannerRe();	
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	

	public enum color{
		red, white, black;
	}
	
	
}