

//http://www.inpharmix.com/jps/PID_Controller_For_Lego_Mindstorms_Robots.html



import ch.aplu.ev3.Gear;
import ch.aplu.ev3.LegoRobot;
import ch.aplu.ev3.LightSensor;
import ch.aplu.ev3.SensorPort;

public class PIDFollowerRemake {
//	private static final
	public static final double VARIANCE = 1.05;
	public static final double CONST_P = 57;	//57
	public static final double CONST_I = 5;					// varI = 1.2*Kc*(dT)/OscPeriod	 ~~	6 or 4
	public static final double CONST_D = 0.0002;			//1./2000.		//varD = 0.6*Kc*OscPeriod/8dT	~~ 1200 or 800
	public static final double MAX_TURN = 0.1;
//	public static final double UNDERGROUND = 1150;
	
	public direction direct = direction.forward;
	
	
	private LightSensor lls;
	private LightSensor rls;
	private LegoRobot robot;
	private Gear gear;
	private int rlsvalue;
	private int llsvalue;
	
	private FollowerData data;

	public PIDFollowerRemake() {
		
		data = new FollowerData();
		robot = new LegoRobot("192.168.11.31");		
		
		lls = new LightSensor(SensorPort.S1);
		rls = new LightSensor(SensorPort.S3);
		robot.addPart(lls);
		robot.addPart(rls);
		lls.activate(true);
		rls.activate(true);
		gear = new Gear();
		robot.addPart(gear);
		final int speed = 25;
		gear.setSpeed(speed);
		gear.forward();
		
		startLineFollower();
	}
	
	
	
	public void startLineFollower() {
		//	int commonValue = 1182;

		
		while (!robot.isEscapeHit()) {
			llsvalue = getAvgValue(lls);
			rlsvalue = getAvgValue(rls);
			System.out.println("lls: " + llsvalue + " rls: " + rlsvalue + " direction: " + direct + " turn: " + data.getTurn());
			if (llsvalue > 400 && llsvalue < 1000 && rlsvalue > 400 && rlsvalue < 1000){
				direct = direction.forward;
			}
			if (isDriveRight()) {
				driveRight();
				if (rlsvalue < 150) {
					direct = direction.right;
				}
			} else if (isDriveLeft()) {
				driveLeft();
				if (llsvalue < 150){
					direct = direction.left;
				};
			}
			else {
				//data = new FollowerData();
				//gear.forward();
				reactToOvershoot();
			}	
		}
	}
	
	
	private void reactToOvershoot() {
		if (isOvershootRight())	{
			System.out.println("test1");
			while(true){
				gear.rightArc(MAX_TURN);
				gear.rightArc(0.1);		//wenn MAX_TURN auf 0.05 geändert wird, muss hier 0.06 hin
					System.out.println("OVERSHOOT RIGHT TURN");
				
				if (getAvgValue(lls) < 800) {
					System.out.println("Overshoot done " +getAvgValue(lls));
					direct = direction.forward;
					break;
			}
			}				
		}
		else if (isOvershootLeft())	{
			System.out.println("test2");
			while(true) {
				gear.leftArc(MAX_TURN);
				gear.leftArc(0.1);
				System.out.println("OVERSHOOT LEFT TURN");
				
				if (getAvgValue(rls) < 800) {
					System.out.println("Overshoot done " + getAvgValue(rls));
					direct = direction.forward;
					break;
				}
			}
		}
		gear.forward();
		data.setIntegral(0);
	}

	private void driveLeft() {
		data = new FollowerData();
		data.setError(rlsvalue - llsvalue);
		data.setDerivative(data.getLastError() - data.getError());
		
//		if (derivative == 0) {	//evtl. <=
//			derivative = 1;
//		}
		
		data.setIntegral(data.getIntegral() + data.getError());
		gear.leftArc(data.getTurn());
		data.setLastError(data.getError());
		
//		System.out.println("test left - Error: " + error + " multiplier: " + multiplier +" Radius: " + turn);
//		System.out.println("rls.getVal: " + rlsvalue  + " lls.getval" + llsvalue + "v*c: " + variance*commonValue);
//		System.out.println("Error: " + error + " Integral: " + integral + " Derivative: " + derivative);
//		System.out.println("***** RIGHT ***** Turn P:" + CONST_P * (1/error) + " Turn I: " + (CONST_I * (1/integral)) + 
//				" Turn D:" + CONST_D * (derivative) + " Turn: " + turn + "\n");			
	
	}

	private void driveRight() {
		data = new FollowerData();
		data.setError(llsvalue - rlsvalue);
		data.setDerivative(data.getLastError()- data.getError());	

//		if (derivative == 0) {
//			derivative = 1;
//		}
		data.setIntegral(data.getIntegral() + data.getError());
		gear.rightArc(data.getTurn());		
		data.setLastError(data.getError());

//		System.out.println("test right - Error: " + error + " multiplier: " + multiplier + " Radius: " + turn);
//		System.out.println("lls.getVal: " + llsvalue + " rls.getval" + rlsvalue + "v*c: " + variance*commonValue);
//		System.out.println("Error: " + error + " Integral: " + integral + " Derivative: " + derivative);
//		
//		System.out.println("***** LEFT ***** Turn P: " + varP * (1/error) + " Turn I: " + (varI * (1/integral)) + 
//				" Turn D: " + varD * (derivative) + " Turn: " + turn + "\n");
	}

	private boolean isOvershootLeft() {
		return (direct == direction.left);
	}

	private boolean isOvershootRight() {
		return (direct == direction.right);
	}

	private boolean isDriveLeft() {
		return rlsvalue > VARIANCE * llsvalue;
	}
	
	public boolean isDriveRight() {
		return llsvalue > VARIANCE * rlsvalue;
	}

	public static int calibrate(int ls1value, int ls2value){
		System.out.println("ASDF: " + (ls1value + ls2value) / 2);
		 return Math.round((ls1value + ls2value) / 2);
	}
	
	public static void main(String[] args) {
		try{
			new PIDFollowerRemake();	
//			Test test = new Test();
//			test.startMeassure();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private int getAvgValue(LightSensor ls) {
//		List<Integer> list = new ArrayList<Integer>();
//		for(int i = 0; i <= 3; i++) {
//			int value = ls.getValue();
//			list.add(value);
//		}
//		int sum = 0;
//		for(Integer item : list) {
//			sum = sum + item;
//		}
//		return Math.round(sum/3);
		
		return ls.getValue();
	}
	
	private class FollowerData {
		private double turn = 0;
		
		public double getTurn() {
			turn = (CONST_P * (1/data.getError()))
			- (CONST_I * (1/data.getIntegral()))
			+ (CONST_D * (data.getDerivative()));
			
//			if (turn < MAX_TURN || data.getDerivative()> 20*data.getError()) { 
//				turn = MAX_TURN;
//			}

			System.out.println((CONST_P * (1/data.getError())) + "---" + (CONST_I * (1/data.getIntegral())) + "----" + (CONST_D * (data.getDerivative())));
			System.out.println(turn);
			if (turn < MAX_TURN) { 
				turn = MAX_TURN;
			}
//			System.out.println("turn: " + turn);
			return turn;
		}
		public double getError() {
			if (error == 0) {
				error = 1;
			}
			return error;
		}
		public void setError(double error) {
			if (error == 0) {
				error = 1;
			}
			this.error = error;
		}
		public double getIntegral() {
			return integral;
		}
		public void setIntegral(double integral) {
			this.integral = integral;
		}
		public double getDerivative() {
			return derivative;
		}
		public void setDerivative(double derivative) {
			this.derivative = derivative;
		}
		public double getLastError() {
			return lastError;
		}
		public void setLastError(double lastError) {
			this.lastError = lastError;
		}
		private double error = 0;
		private double integral = 0;
		private double derivative = 0;				//nextError 
		private double lastError = 0;
	}
	
	
	public enum direction {	
		left, forward, right;		
	}
	
}