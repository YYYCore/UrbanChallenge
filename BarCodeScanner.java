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
		String[] codeArray = new String[timeArray.length];
		
		
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
				
			
				double starttime;
				
				
				while (scanning){
					String color = getColor(cls.getColorStr(), cls.getColorLabel());
					if (color == "BLACK" || color == "WHITE") {	
						if ((count == 0) || (colorArray[count-1] != color)){
							colorArray[count] = color;							
							if (count != 0){
								timeArray[count-1] = getCount(colorArray[count-1], gear.getLeftMotorCount() ,time);
//								System.out.println(" t: " + (gear.getLeftMotorCount() -time) + timeArray[count-1]);							
							}	
							starttime = gear.getLeftMotorCount();
							time = gear.getLeftMotorCount();
							count++;
						}		
					} else if (color == "RED" && count != 0) {	
						System.out.println("END");
						timeArray[count -1] = getCount(colorArray[count-1], gear.getLeftMotorCount() ,time);						
						int c = 0;						
						for (int a = 0; a < colorArray.length; a++) {
							for (int b = 0; b < timeArray[a]; b++) {
//									System.out.println("color: " + colorArray[a] + " count: " + timeArray[a] + " t: " + ) );
									codeArray[c] = colorArray[a];
									c++;
							}
						}												
						for (int i = 0; i < codeArray.length; i++){
							if (codeArray[i] != null){
								System.out.println(codeArray[i]);
							}
						}	
						gear.setSpeed(0);
						gear.forward();
						scanning = false;
					}
				}	
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