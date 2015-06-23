
import ch.aplu.ev3.ColorLabel;
import ch.aplu.ev3.ColorSensor;
import ch.aplu.ev3.Gear;
import ch.aplu.ev3.LegoRobot;
import ch.aplu.ev3.LightSensor;
import ch.aplu.ev3.Sensor;
import ch.aplu.ev3.SensorPort;



public class BarCodeScanner {	
	
	
	public BarCodeScanner() {
		
		
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
				result = startScan(lls, rls, gear, colorArray, maxDigits );
				System.out.println("----------------------------------");
				for (int c = 0; c < maxDigits; c++) {
					System.out.println(result[c]);
				}
				break;
			} else {
				
			}
					
		}
	}
	
	public static color[] startScan(LightSensor lls, LightSensor rls, Gear gear, color[] colorArray, int maxDigits){
		int count = 0;
		int i = 0;
		int wantedCount = 31;
		int motorCount = 0;
		int lightCount = 0;
		double factor = 1.1;
		gear.resetLeftMotorCount();
		int[][] valueArray = new int[1000][2];
		
		while(true){
			
			motorCount = gear.getLeftMotorCount();
			lightCount = getValue(lls, rls);
			
			valueArray[i][0] = motorCount;
			valueArray[i][1] = lightCount;
			
	
			
//			System.out.println("MotorCount: " + motorCount + " lightcount: " + lightCount + " count: " + count + " i: " + i);
			
			if (count == 0) {
				if (lightCount <= 400) {
					colorArray = writeBlack(colorArray, count);
					i = 0;
					count++;
					gear.resetLeftMotorCount();
				}
			} else if (motorCount >= wantedCount+10 && count != 0) {	
				if (count == maxDigits) {
					System.out.println("Max digits reached: " + maxDigits);
					gear.stop();
				}
				System.out.println("i: " + i + " count : " + count + "array i-2: " + valueArray[i-2][1] + 
						" array i: " + valueArray[i][1] + " motorcount " + motorCount );
				if (valueArray[Math.round(i/2)][1] > factor * valueArray[i][1]) {
					colorArray = writeBlack(colorArray, count);
					count++;
				} else if (valueArray[i][1] > factor * valueArray[Math.round(i/2)][1]){
					colorArray = writeWhite(colorArray, count);
					count++;	
				}		
				if (count > 1) {
					wantedCount = 30 - (motorCount - wantedCount);
				}	
				gear.resetLeftMotorCount();
				i = 0;
				
				
			}	
			i++;			
		}
		
	}
	
	public static color[] writeBlack(color[] colorArray, int count){
		System.out.println("black");
		colorArray[count] = color.black;
		return colorArray;
	}
	
	public static color[] writeWhite(color[] colorArray, int count){
		System.out.println("white");
		colorArray[count] = color.white;
		return colorArray;
	}
	
	
	
	public static int getValue(LightSensor lls, LightSensor rls){		
		return ((lls.getValue() + rls.getValue()) / 2);	
	}
	

	public static boolean isRed(ColorSensor cls){
		return (cls.getColorStr() == "RED") || cls.getColorID() == 5 || cls.getColorLabel().toString().equals("RED");		
	}
	
	
	public static void main(String[] args) {
		try{
			new BarCodeScanner();	
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	

	public enum color{
		red, white, black;
	}
	
	
}