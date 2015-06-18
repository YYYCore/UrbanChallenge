
import ch.aplu.ev3.ColorLabel;
import ch.aplu.ev3.ColorSensor;
import ch.aplu.ev3.Gear;
import ch.aplu.ev3.LegoRobot;
import ch.aplu.ev3.LightSensor;
import ch.aplu.ev3.Sensor;
import ch.aplu.ev3.SensorPort;


public class BCScannerNew {	
	
	
	public static int index = 0;
	public static color[] colorArray = new color[12];
	public static color lastColor = null;
	public static int counter = 0;
	public static int maxDigits = 10;
	
	
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
		final int speed = 30;
		gear.setSpeed(speed);
		gear.forward();
	
	
		
		System.out.println("Battery: " + robot.getBatteryLevel());

		
		start(robot, cls, gear, lls, rls);
		
		System.out.println("Code: " + translate(colorArray));
		
		
		robot.exit();
		

		
	}
	
	
	
	public static void start(LegoRobot robot, ColorSensor cls, Gear gear, LightSensor lls, LightSensor rls){
		
		
		
	
		while (!robot.isEscapeHit()) {
			
			if (isRed(cls)) {
				
				lastColor = color.red;
				gear.resetLeftMotorCount();
				
				startScan(lls, rls, gear, colorArray, maxDigits );
				
				System.out.println("----------------------------------");
				
				for (int c = 0; c < maxDigits; c++) {
					System.out.println(colorArray[c]);
				}
				
				break;
			} else {
				
			}
					
		}
	}
	
	public static void startScan(LightSensor lls, LightSensor rls, Gear gear, color[] colorArray, int maxDigits){
		
		double motorCount = 0;
		int lightCount = 0;
		int expectedValue  = 600;
		
		
		while(true){
			
			motorCount = gear.getLeftMotorCount();
			lightCount = getValue(lls, rls);
			
			if (counter == maxDigits) {
				gear.stop();
				System.out.println("Max Digits reached: " + maxDigits);
				break;
			} else 
				if (lightCount < expectedValue && (lastColor == color.red || lastColor == color.white)) {
					if (lastColor == color.red){
						motorCount = 0;
					}
					setNewColor(color.black, motorCount);
					lastColor = color.black;
					gear.resetLeftMotorCount();
//					expectedValue = lightCount;
				} else if (lightCount >= expectedValue  && lastColor == color.black) {
					setNewColor(color.white, motorCount);
					lastColor = color.white;
					gear.resetLeftMotorCount();
//					expectedValue = lightCount;
				}
			
			
			
		}
		
	}
	
	public static void setNewColor(color color, double motorCount){
		
		long count = Math.round(motorCount/25.);
		
		
		
		if (count < 1) {
			count = 1;
		}
		
		System.out.println("color: " + lastColor + " count: " + count + " motorcount: " + motorCount);

		if (lastColor != color.red) {
			
			if (index + count > maxDigits){
				count = maxDigits - index;
			}
			
			for (long i = count; i > 0; i--){							
				
				colorArray[index] = lastColor;
				index++;
				counter++;
			}
		}
	}
	
	public static int getValue(LightSensor lls, LightSensor rls){		
		return ((lls.getValue() + rls.getValue()) / 2);	
	}
	

	public static boolean isRed(ColorSensor cls){
		return (cls.getColorStr() == "RED") || cls.getColorID() == 5 || cls.getColorLabel().toString().equals("RED");		
	}
	
	public static String translate (color[] colorArray){
		
		String s = "";
		
		for (int i = 0; i < colorArray.length ; i++){
			if (colorArray[i] == color.black) {
				s += "1";
			} else if (colorArray[i] == color.white) {
				s += "0";
			}
		}
		
		
		
		return s;
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