import ch.aplu.ev3.ColorLabel;
import ch.aplu.ev3.ColorSensor;
import ch.aplu.ev3.Gear;
import ch.aplu.ev3.LegoRobot;
import ch.aplu.ev3.SensorPort;


public class BarCodeScanner {	
	
	public static int count = 0;
	public static double endcount=0;	
	
	public BarCodeScanner() {
		
		LegoRobot robot = new LegoRobot("192.168.11.31");		
		
		final ColorSensor cls = new ColorSensor(SensorPort.S4);
		
		robot.addPart(cls);

		
		final Gear gear = new Gear();
		robot.addPart(gear);
		final int speed = 10;
		gear.setSpeed(speed);
		gear.forward();
		

		
		String code = startScan(robot, cls, gear);
		System.out.println(code);
	}
	
	public static String startScan(LegoRobot robot, ColorSensor cls, Gear gear){
		color[] colorArray = new color[15];
		double[] timeArray = new double[colorArray.length];
		color[] codeArray = new color[timeArray.length+1];

		
		while (!robot.isEscapeHit()) {
			
			if (getColor(cls.getColorStr(), cls.getColorLabel()) == color.red){	
				
				double startcount=0;
				endcount=0;				
				count = 0;
				
				while (true){
					color color = getColor(cls.getColorStr(), cls.getColorLabel());
					if (isRed(color) && count != 0) {
						codeArray = Red(gear, colorArray, timeArray, codeArray);
						break;
					}
					if (isBlackOrWhite(color)) {	
						BlackOrWhite(gear, colorArray, timeArray, color,startcount);
					}
				}
				return translate(codeArray);
			}
		}
		return null;
	}
	
	public static boolean isBlackOrWhite(color color){
		return (color == color.black || color == color.white);
	}
	
	public static boolean isRed(color color){
		return (color == color.red);
	}
	
	public static void BlackOrWhite(Gear gear, color[] colorArray, double[] timeArray, color color,double startcount){
		if ((count == 0) || (colorArray[count-1] != color)){
			colorArray[count] = color;							
			if (count != 0){
				timeArray[count-1] = getCount(colorArray[count-1], gear.getLeftMotorCount() ,endcount);
//				System.out.println(" t: " + (gear.getLeftMotorCount() -endcount) +"   "+ timeArray[count-1]);							
			}	
			startcount = gear.getLeftMotorCount();
			endcount = gear.getLeftMotorCount();
			count++;
		}	
	}
	
	public static color[] Red(Gear gear, color[] colorArray, double[] timeArray, color[] codeArray){
//		System.out.println(" t: " + (gear.getLeftMotorCount() -endcount) +"   "+ timeArray[count-1]);	
//		System.out.println("END");
		timeArray[count -1] = getCount(colorArray[count-1], gear.getLeftMotorCount() ,endcount);						
		int c = 0;						
		for (int a = 0; a < colorArray.length; a++) {
			for (int b = 0; b < timeArray[a]; b++) {
//					System.out.println("color: " + colorArray[a] + " count: " + timeArray[a] + " t: " );
					codeArray[c] = colorArray[a];
					c++;
			}
		}												
//		for (int i = 0; i < codeArray.length; i++){
//			if (codeArray[i] != null){
//				System.out.println(codeArray[i]);
//			}
//		}	
		gear.setSpeed(0);
		gear.forward();
		return(codeArray);
	}
	
	
	public static int getCount(color color, double motorCount, double lastCount){
		if (color == color.black){
			if (motorCount - lastCount < 45) {
				return 1;
			} else if (motorCount - lastCount <= 70) {
				return 2;
			} else{
				return 3;
			}
		} if (color == color.white) {
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
	

	public static color getColor(String String, ColorLabel Label){
		if ((String == "RED") || (Label.toString() == "RED")) {
			return color.red;
		} else if ((String == "WHITE") || (Label.toString() == "WHITE")){
			return color.white;
		} else if ((String == "BLACK") || (Label.toString() == "BLACK"))
			return color.black;
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
	
	public static String translate(color[] codearray){
		String result = "";
		
		for (int i = 0; i < codearray.length; i++){
			if (codearray[i] == null){
				//do nothing
			} else {
				if (codearray[i] == color.black){
					result += 1;
				} else if (codearray[i] == color.white) {
					result += 0;
				} else {
					throw new IllegalStateException();
				}
			}
		}
		return result;
	}
	
	
	public enum color{
		red, white, black;
	}
	
	
}