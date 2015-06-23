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
		commonValue = 1130;
		commonValue = calibrate(rls.getValue(), lls.getValue());

		double turn = 0;
		String varTurn = "";
		double error = 0;
		double multiplier = (76);
		double variance = 1.10;
		double varP = 0.75;			
		
		double integral = 0;
		double varI = 5;					// varI = 1.2*Kc*(dT)/OscPeriod	 ~~	6 or 4
											// 		Kc = varP ?
		double derivative = 0;				//nextError 
		double lastError = 0;
		double varD = 1./2000.;				//varD = 0.6*Kc*OscPeriod/8dT	~~ 1200 or 800
		
		boolean overshoot = false;
		boolean overshootDone = false;
		
		varD = 0.0001;
		varI = 20;
		
		
		while (!robot.isEscapeHit()) {
							
			double llsvalue = lls.getValue();
			double rlsvalue = rls.getValue();
			
			if (llsvalue > variance * rlsvalue) {
				error = llsvalue - rlsvalue;	
				

//				if (rlsvalue < 500){
//					overshoot = true;
//				}
				if (error <= 0) {
					error = 1;
				}
				
				derivative = error - lastError;	
				
				if (derivative == 0) {
					derivative = 1;
				}
				
				turn = (varP * multiplier * (1/error))
					- (varI * (1/integral))
					+ (varD * (derivative));
				
				integral = 0.66*integral + error;		
				if (turn < 0.05 || derivative > 10*error) { 
					turn = 0.05;
				}
				gear.rightArc(turn);	
				varTurn = "RIGHT";
				lastError = error;
				
//				System.out.println("test right - Error: " + error + " multiplier: " + multiplier + " Radius: " + turn);
//				System.out.println("lls.getVal: " + llsvalue + " rls.getval" + rlsvalue + "v*c: " + variance*commonValue);
//				System.out.println("Error: " + error + "Integral: " + integral + "Derivative: " + derivative);
//				
//				System.out.println("***** RIGHT ***** TurnP:" + varP * multiplier * (1/error) + " Turn I: " + (varI * (1/integral)) + 
//						" Turn D:" + varD * (derivative) + " Turn: " + turn + "\n");

	
			} else if (rlsvalue > variance * llsvalue) {

//				if (llsvalue < 500){
//					overshoot = true;
//				}
				
				
				error = rlsvalue - llsvalue;
				if (error <= 0) {
					error = 1;
				}
				derivative = error - lastError;
				
				if (derivative == 0) {
					derivative = 1;
				}
				
				turn = (varP * multiplier * (1/error))
					- (varI * (1/integral))
					+ (varD * (derivative));
				
//				integral += error;
				integral = 0.66*integral + error;	
				if (turn < 0.05 || derivative > 10*error ) { 
					turn = 0.05;
				}
								
				gear.leftArc(turn);
				varTurn = "LEFT";
				lastError = error;
				
//				System.out.println("test left - Error: " + error + " multiplier: " + multiplier +" Radius: " + turn);
//				System.out.println("rls.getVal: " + rlsvalue  + " lls.getval" + llsvalue + "v*c: " + variance*commonValue);
//				System.out.println("Error: " + error + "Integral: " + integral + "Derivative: " + derivative);
//				System.out.println("***** LEFT ***** TurnP:" + varP * multiplier * (1/error) + " Turn I: " + (varI * (1/integral)) + 
//						" Turn D:" + varD * (derivative) + " Turn: " + turn + "\n");
			} else {
//					while (overshoot){
//						if(varTurn == "RIGHT"){
//							System.out.println("OVERSHOOT RIGHT TURN");
//							gear.rightArc(0.05);
//						} else if (varTurn == "LEFT"){
//							System.out.println("OVERSHOOT RIGHT TURN");
//							gear.leftArc(0.05);
//						}
//						if (lls.getValue() < 0.8* commonValue || rls.getValue() < 0.8*commonValue) {
//							overshootDone = true;
//						}
//						if (Math.abs(lls.getValue() - rls.getValue()) < 200 && overshootDone) {
//							System.out.println("OVERSHOOT DONE!!!!!!!!!!!!!" + Math.abs(lls.getValue() - rls.getValue()));
//							overshoot = false;
//							overshootDone = false;
//						}
//					}			
				
//					if (overshoot) {
//						if(varTurn == "RIGHT"){
//							gear.rightArc(0.05);
//						} else if (varTurn == "LEFT"){
//							gear.leftArc(0.05);
//						}						
//					} else{
						gear.forward();
						integral = 0;
//					}
					
			}			
		}
	}
	
	public static int calibrate(int ls1value, int ls2value){
		System.out.println("commonValue: " + (ls1value + ls2value) / 2);
		 return Math.round((ls1value + ls2value) / 2);
	}
	
	public static void main(String[] args) {
		try{
			new PIDFollowerRemake();	
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}