// http://www.inpharmix.com/jps/PID_Controller_For_Lego_Mindstorms_Robots.html



import ch.aplu.ev3.Gear;
import ch.aplu.ev3.LegoRobot;
import ch.aplu.ev3.LightSensor;
import ch.aplu.ev3.SensorPort;

public class PIDFollower {
	int counter = 0;
	int[] arrayls1 = new int [20];
	int[] arrayls2 = new int [20];
	
	
	

	public PIDFollower() {
		

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
//		commonValue = calibrate(lls.getValue(), rls.getValue());
		System.out.println("COMMON VALUE CALIBRATED: " + commonValue);

		double turn = 0;
		double error = 0;
		double multiplier = (76);
		double variance = 1.015;
		double varP = 0.8;		
		
		double integral = 1;
//		double varI = 1./1000.;					// varI = 2*varP*(dT)/OscPeriod	 ~~	6
		double varI = 0.00001;
		
		double derivative = 0;					//nextError 
		double lastError = 0;
		double varD = 1./2000.;					//varD = 0.6*Kc*OscPeriod/8dT	~~ 1200 or 800
		
		
		while (!robot.isEscapeHit()) {
					
			double llsvalue = lls.getValue();
			double rlsvalue = rls.getValue();
			
			if (llsvalue > variance * commonValue) {
				error = llsvalue - rlsvalue;
				if (error < 0) {
					error = 0;
				}
				
				derivative = error - lastError;
				
				turn = varP * multiplier * (1/error) 	//P-Part
						- (varI * (integral))			//I-Part
						- (varD * derivative);			//D-Part
				integral += error;
				if (turn < 0.05) { 
					turn = 0.05;
				}
				gear.rightArc(turn);
				
				if (error < 150) {
					integral = 0;
				}
				
				
				lastError = error;
				
				System.out.println("RIGHT - Error: " + error + " integral: " + integral + " derivative: " + derivative);
				System.out.println("turnP: " + (varP * multiplier * (1/error)) + " I-part: " + (varI * integral) + " D-Part: " + (varD * derivative));
				System.out.println("turnPID: " + turn);
				
				
			} else if (rlsvalue > variance * commonValue) {
				error = rlsvalue - llsvalue;
				if (error < 0) {
					error = 0;
				}
				
				derivative = error - lastError;
				
				
				turn = varP * multiplier * (1/error)
						- (varI * (integral))
						- (varD * derivative);
				integral += error;
				if (turn < 0.05) { 
					turn = 0.05;
				}
				gear.leftArc(turn);
				
				if (error < 150) {
					integral = 0;
				}
				lastError = error;
				
				
				System.out.println("LEFT - Error: " + error + " integral: " + integral + " derivative: " + derivative);
				System.out.println("turnP: " + (varP * multiplier * (1/error)) + " I-part: " + (varI  * integral) + " D-Part: " + (varD * derivative));
				System.out.println("turnPID: " + turn);
			} else {
				System.out.println("SET INTEGRAL = 0");
				gear.forward();
				integral = 0;
			}			
		}
	}
	
	public static int calibrate(int ls1value, int ls2value){
		
		return (ls1value + ls2value) / 2;

	}
	
	public static void main(String[] args) {
		try{
			new PIDFollower();	
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	

	

	

}