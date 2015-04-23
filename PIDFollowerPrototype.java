// http://www.inpharmix.com/jps/PID_Controller_For_Lego_Mindstorms_Robots.html



import ch.aplu.ev3.Gear;
import ch.aplu.ev3.LegoRobot;
import ch.aplu.ev3.LightSensor;
import ch.aplu.ev3.SensorPort;

public class PIDFollowerPrototype {
	
	public PIDFollowerPrototype() {
		
		LegoRobot robot = new LegoRobot("192.168.11.31");		
		
		final LightSensor lls = new LightSensor(SensorPort.S1);
		final LightSensor rls = new LightSensor(SensorPort.S3);
		robot.addPart(lls);
		robot.addPart(rls);
		lls.activate(true);
		rls.activate(true);
		final Gear gear = new Gear();
		robot.addPart(gear);
		final int speed = 20;
		gear.setSpeed(speed);
		gear.forward();
		

		int commonValue = 1182;

		double turn = 0;
		double error = 0;
		double multiplier = (76);
		double variance = 1.05;
		double varP = 0.75;			
		
		double integral = 0;
		double varI = 1;					// varI = 1.2*Kc*(dT)/OscPeriod	 ~~	6 or 4
											// 		Kc = varP ?
		double derivative = 0;				//nextError 
		double lastError = 0;
		double varD = 1;					//varD = 0.6*Kc*OscPeriod/8dT	~~ 1200 or 800
		
		
		while (!robot.isEscapeHit()) {
							
			double llsvalue = lls.getValue();
			double rlsvalue = rls.getValue();
			
			if (llsvalue > variance * commonValue) {
				error = llsvalue - rlsvalue;
				if (error < 0) {
					error = 0;
				}
				derivative = error - lastError;	
				
				turn = (varP * multiplier * (1/error))
					+ (varI * (1/integral))
					+ (varD * (1/derivative));
				
				integral += error;		
				if (turn < 0.05) { 
					turn = 0.05;
				}
				gear.rightArc(turn);		
				lastError = error;
				
//				System.out.println("test right - Error: " + error + " multiplier: " + multiplier + " Radius: " + turn);
//				System.out.println("lls.getVal: " + llsvalue + " rls.getval" + rlsvalue + "v*c: " + variance*commonValue);
				
				System.out.println("Error: " + error + "Integral: " + integral + "Derivative: " + derivative);
				
				
			} else if (rlsvalue > variance * commonValue) {
				error = rlsvalue - llsvalue;
				if (error < 0) {
					error = 0;
				}
				derivative = error - lastError;
				
				turn = (varP * multiplier * (1/error))
					+ (varI * (1/integral))
					+ (varD * (1/derivative));
				
				integral += error;
				if (turn < 0.05) { 
					turn = 0.05;
				}
				gear.leftArc(turn);
				lastError = error;
//				System.out.println("test left - Error: " + error + " multiplier: " + multiplier +" Radius: " + turn);
//				System.out.println("rls.getVal: " + rlsvalue  + " lls.getval" + llsvalue + "v*c: " + variance*commonValue);
				System.out.println("Error: " + error + "Integral: " + integral + "Derivative: " + derivative);
				
			} else {
				gear.forward();
				integral = 0;
			}			
		}
	}
	
	public static int calibrate(int ls1value, int ls2value){
		System.out.println("ASDF: " + (ls1value + ls2value) / 2);
		 return Math.round((ls1value + ls2value) / 2);
	}
	
	public static void main(String[] args) {
		try{
			new PIDFollowerPrototype();	
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}