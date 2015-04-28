// http://www.inpharmix.com/jps/PID_Controller_For_Lego_Mindstorms_Robots.html



import java.awt.Color;

import ch.aplu.ev3.ColorLabel;
import ch.aplu.ev3.ColorSensor;
import ch.aplu.ev3.Gear;
import ch.aplu.ev3.LegoRobot;
import ch.aplu.ev3.SensorPort;

public class BarCodeScanner {	
	public BarCodeScanner() {
		
		LegoRobot robot = new LegoRobot("192.168.11.31");		
		
		final ColorSensor cls = new ColorSensor(SensorPort.S4);
		
		robot.addPart(cls);

		
		final Gear gear = new Gear();
		robot.addPart(gear);
		final int speed = 10;
		gear.setSpeed(speed);
		gear.forward();
		
		boolean scanning = false;
		
		String[] colorArray = new String[15];
		double[] timeArray = new double[colorArray.length];
		
		
		while (!robot.isEscapeHit()) {
			
//			System.out.println("ID: " + cls.getColorID());
//			System.out.println("INT: " + cls.getColorInt());
//			System.out.println("STR: " + cls.getColorStr());
//			System.out.println("Color: " +  cls.getColor());
//			System.out.println("Label: " + cls.getColorLabel() + "\n");
			
			
			if (getColor(cls.getColorStr(), cls.getColorLabel()) == "RED"){	
				scanning = true;
				double time = 0;
				
				int count = 0;
				
			
				
				double startTime = 0;
				double test = 0;
				double ccc = 0;
				
				
				while (scanning){
					String color = getColor(cls.getColorStr(), cls.getColorLabel());
					if (color == "BLACK" || color == "WHITE") {	
						if ((count == 0) || (colorArray[count-1] != color)){
							colorArray[count] = color;							
							if (count != 0){
								System.out.println(gear.getLeftMotorCount() - ccc);

								timeArray[count -1] = getCount(color, gear.getLeftMotorCount() ,time);
								
								
							}	
							ccc = gear.getLeftMotorCount();
							time = gear.getLeftMotorCount();
							count++;
						}		
					} else if (color == "RED" && count != 0) {	
						
						timeArray[count -1] = getCount(colorArray[count-1], gear.getLeftMotorCount() ,time);
						
						for (int a = 0; a < timeArray.length -1; a++){
							if (timeArray[a] == 2){
								for (int b = colorArray.length-1; b >= 1; b--) {
									if (colorArray[b-1] == null){
										
									}
									colorArray[b] = colorArray[b-1];
								}
								colorArray[a] = colorArray[a+1];															
//							} else if (timeArray[a] == 3){
//								for (int b = colorArray.length-1; b > a; b--) {
//									if (colorArray[b-1] == null){
//										
//									}
//									colorArray[b] = colorArray[b-1];
//								}
//								timeArray[a] = 2;
//							}			
							}
						}			
						for (int i = 0; i < colorArray.length; i++){
//							System.out.println(colorArray[i]);
							System.out.println(colorArray[i] + "  " + timeArray[i]);
							test += timeArray[i];
							
						}	

						gear.setSpeed(0);
						gear.forward();
						scanning = false;
					}
				}	
				double endTime = System.currentTimeMillis();
			}
			
			
			
			
			
			
			
		}
	}
	
	
	public int getCount(String color, double motorCount, double lastCount){
		if (color == "BLACK"){
			if (motorCount - lastCount < 45) {
				return 1;
			} else if (motorCount - lastCount <= 70) {
				return 2;
			} else{
				return 3;
			}
		} if (color == "WHITE") {
			if (motorCount - lastCount < 50) {
				return 1;
			} else if (motorCount - lastCount <= 80) {
				return 2;
			} else{
				return 3;
			}
		}
		else return 0;
	}
	
	
	
	
	public String getColor(String String, ColorLabel Label){
		if ((String == "RED") || (Label.toString() == "RED")) {
			return "RED";
		} else if ((String == "WHITE") || (Label.toString() == "WHITE")){
			return "WHITE";
		} else if ((String == "BLACK") || (Label.toString() == "BLACK"))
			return "BLACK";
		else {
			return null;
		}
	}
	
	
	public static void main(String[] args) {
		try{
			new BarCodeScanner();	
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}