
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
		final int speed = 4;
		gear.setSpeed(speed);
		gear.forward();
		
		
		start(robot, cls, gear, lls, rls);
		robot.exit();
	}
	
	
	
	
	
	public static void start(LegoRobot robot, ColorSensor cls, Gear gear, LightSensor lls, LightSensor rls){


		while (!robot.isEscapeHit()) {
	
			
			if (isRed(cls)){
				gear.resetLeftMotorCount();
				
				startScan(robot, lls, rls, cls, gear);
				break;
			
			}
		}
	}
	
	
	public static void startScan(LegoRobot robot, LightSensor lls, LightSensor rls, ColorSensor cls, Gear gear){
		color[] array = new color[10];
		boolean scanning = true;
		int value = 0;
		int count = 0;
		int x = 0;
		int length = 12;
		double [] time = new double[length];
		double now = 0;
		int[]  motorcount = new int[length];
		
		
		
		double starttime = System.currentTimeMillis();
		
		while (scanning){
			
			while (getValue(lls, rls) > 600) {
				if (array[0] == color.black) {
					
					if(count < 1 || array[count-1] == color.white) {
						
					} else {
					
						array[count] = color.white;
						now = System.currentTimeMillis();
						time[count-1] =  now - starttime;					
						starttime = now;
						
						motorcount[count-1] = gear.getLeftMotorCount();
						gear.resetLeftMotorCount();
								
						count++;
						break;
					}
				}
			}
			if (getValue(lls, rls) <= 600) {
				if (count < 1 || array[count-1] == color.black) {
					if (count == 0) {
						array[count] = color.black;
					}
				} else {
					System.out.println("count" + count);
					array[count] = color.black;
					now = System.currentTimeMillis();
	
								
					if (count > 0) {
						time[count-1] =  now - starttime;
						
						
						motorcount[count-1] = gear.getLeftMotorCount();
						
					}
					gear.resetLeftMotorCount();
					starttime = now;
									
					count++;
				}
			}	
			
			
			if (isRed(cls)){	
				for (int i = 0; i < length; i++) {
					System.out.println("Color: " + array[i] + "  time: " + time[i] + " motorcount: " + motorcount[i]);
				}			
				gear.stop();
				scanning = false;
				
			}
			
			
			
			
			
		}		
	}
	


	public static boolean isRed(ColorSensor cls){
		return (cls.getColorStr() == "RED") || cls.getColorID() == 5 || cls.getColorLabel().toString().equals("RED");		
	}
	
	public static int getValue(LightSensor lls, LightSensor rls){
		
		return ((lls.getValue() + rls.getValue()) / 2);
		
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