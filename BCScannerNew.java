
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
			
		LegoRobot robot = new LegoRobot("192.168.11.37");	
		
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
		
		
		robot.exit();		
	}
	
	public static void start(LegoRobot robot, ColorSensor cls, Gear gear, LightSensor lls, LightSensor rls){
	
		while (!robot.isEscapeHit()) {
			
			if (isRed(cls)) {														// Rote Linie als Beginn des Barcodes
				
				lastColor = color.red;
				gear.resetLeftMotorCount();
				
				startScan(lls, rls, gear, colorArray, maxDigits );					// Eigentlicher Scanvorgang wird gestartet
				
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
			
			if (counter == maxDigits) {																		// Abbruchbedingung fürs erreichen
				gear.stop();																				// der maximalen "Barcodelänge"
				System.out.println("Max Digits reached: " + maxDigits);
				break;
			} else 
				if (lightCount < expectedValue && (lastColor == color.red || lastColor == color.white)) {	// Übergang von Hell auf Dunkel
					if (lastColor == color.red){
						motorCount = 0;
					}
					setNewColor(color.black, motorCount);													// Farbe Schwarz erkannt
					lastColor = color.black;
					gear.resetLeftMotorCount();
				} else if (lightCount >= expectedValue  && lastColor == color.black) {						// Übergang von Dunkel auf Hell
					setNewColor(color.white, motorCount);													// Farbe Weiß erkannt
					lastColor = color.white;
					gear.resetLeftMotorCount();
				}
			
			
			
		}
		
	}
	
	public static void setNewColor(color color, double motorCount){
		
		long count = Math.round(motorCount/25.);									// Relation zwischen Motorcount und Breite der Barcodestreifen
																				
		if (count < 1) {
			count = 1;
		}

		if (lastColor != color.red) {
			
			if (index + count > maxDigits){											// Sicherstellen, dass die Menge der erkannten Streifen nicht
				count = maxDigits - index;											// maxDigits überschreitet
			}
			
			for (long i = count; i > 0; i--){											
				colorArray[index] = lastColor;										// Erkannte Farbe ins auszuwertende Array schreiben
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
		
		String result = "";
		
		for (int i = 0; i < colorArray.length ; i++){
			if (colorArray[i] == color.black) {
				result += "1";											//Farbe Schwarz entspricht einer 1
			} else if (colorArray[i] == color.white) {
				result += "0";											//Farbe Weiß entspricht einer 0
			}
		}
			
		return result;
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