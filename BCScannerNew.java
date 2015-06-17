
import ch.aplu.ev3.ColorLabel;
import ch.aplu.ev3.ColorSensor;
import ch.aplu.ev3.Gear;
import ch.aplu.ev3.LegoRobot;
import ch.aplu.ev3.LightSensor;
import ch.aplu.ev3.Sensor;
import ch.aplu.ev3.SensorPort;



public class BCScannerNew {	
	
	
	public static int index = 0;
	public static color[] colorArray = new color[10];
	public static color lastColor = null;
	
	public BCScannerNew() {
		
		
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
		final int speed = 15;
		gear.setSpeed(speed);
		gear.forward();
	
		robot.setVolume(5);
		
		
		
		start(robot, cls, gear, lls, rls);
		
		robot.exit();
		

		
	}
	
	
	
	public static void start(LegoRobot robot, ColorSensor cls, Gear gear, LightSensor lls, LightSensor rls){
		
		
		int maxDigits = 10;
		color[] colorArray = new color[maxDigits];
		color[] result = new color[maxDigits];
	
		while (!robot.isEscapeHit()) {
			
			if (isRed(cls)) {
				
				lastColor = color.red;
				gear.resetLeftMotorCount();
				
				result = startScan(lls, rls, gear, colorArray, maxDigits );
				System.out.println("----------------------------------");
				
				for (int c = 0; c < maxDigits; c++) {
					System.out.println(colorArray[c]);
				}
				
				break;
			} else {
				
			}
					
		}
	}
	
	public static color[] startScan(LightSensor lls, LightSensor rls, Gear gear, color[] colorArray, int maxDigits){
		
		int motorCount = 0;
		int lightCount = 0;
		int counter = 0;
		
		
		while(true){
			
			motorCount = gear.getLeftMotorCount();
			lightCount = getValue(lls, rls);
			
			if (counter == maxDigits) {
				gear.stop();
				System.out.println("Max Digits reached: " + maxDigits);
			} else 
				if (lightCount < 550 && (lastColor == color.red || lastColor == color.white)) {
					setNewColor(color.black, motorCount);
					lastColor = color.black;
					counter++;
					gear.resetLeftMotorCount();
				} else if (lightCount >= 550 && lastColor == color.black) {
					setNewColor(color.white, motorCount);
					lastColor = color.white;
					counter++;
					gear.resetLeftMotorCount();
				}
			
			
			
		}
		
	}
	
	public static void setNewColor(color color, int motorCount){
		int count = Math.round(motorCount/30);
		
		for (int i = count; i > 0; i--){
			colorArray[index] = color;
			index++;
		}
		
	}
	
	public static int getValue(LightSensor lls, LightSensor rls){		
		return ((lls.getValue() + rls.getValue()) / 2);	
	}
	

	public static boolean isRed(ColorSensor cls){
		return (cls.getColorStr() == "RED") || cls.getColorID() == 5 || cls.getColorLabel().toString().equals("RED");		
	}
	
	
	public static void main(String[] args) {
		try{
			new BCScannerNew();	
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	

	public enum color{
		red, white, black;
	}
	
	
}