//http://192.168.11.212:8081/

// http://legorobotik.ch/index.php?inhalt_links=EV3Direkt/nav_ev3dir.inc.php&inhalt_mitte=EV3Direkt/ev3doc.inc.php

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;

import ch.aplu.ev3.Gear;
import ch.aplu.ev3.LegoRobot;
import ch.aplu.ev3.LightListener;
import ch.aplu.ev3.LightSensor;
import ch.aplu.ev3.Motor;
import ch.aplu.ev3.MotorPort;
import ch.aplu.ev3.SensorPort;

class LineFollower {
	public LineFollower() {
		LegoRobot robot = new LegoRobot("192.168.11.31");
		final LightSensor lls = new LightSensor(SensorPort.S1);
		final LightSensor rls = new LightSensor(SensorPort.S3);
		robot.addPart(lls);
		robot.addPart(rls);
		lls.activate(true);
		rls.activate(true);
		
		final int speed = 15;
		
		
		// GEAR
		//final Gear gear = new Gear();
		//robot.addPart(gear);
		//gear.setSpeed(speed);
		//gear.forward();
		
		
		
		// MOTOR
		final Motor motorLeft = new Motor(MotorPort.A);
		final Motor motorRight = new Motor(MotorPort.B);
		
		robot.addPart(motorRight);
		robot.addPart(motorLeft);
		
		motorRight.setSpeed(speed);
		motorLeft.setSpeed(speed);
		
		motorRight.forward();
		motorLeft.forward();
		
		final int factor = 1;

		
		// Line Follower mit 2 Sensoren, Listener, Radius wird dynamisch berechnet
		lls.addLightListener(new LightListener()	{

			@Override
			public void bright(SensorPort port, int level) {
//				while(rls.getValue() <= 1200){
//					if (rls.getValue() > 300) {
//						motorRight.setSpeed((1/factor) * speed);
//						motorRight.forward();
//					} else {
//						System.out.println("---BACKW left");
//						motorRight.setSpeed(4);
//						motorRight.backward();
//					}
//				}
//				
//				System.out.println("*****LEFT bright " + lls.getValue());
				motorRight.setSpeed(speed);
				motorRight.forward();
			}

			@Override
			public void dark(SensorPort port, int level) {
				double factor = 1;
				int tmpValue;
				//System.out.println("lls: " + lls.getValue() + " rls: " + rls.getValue());
				
				while (lls.getValue() <= 1200) {
					
					int value = (rls.getValue() / 1200) + 1;
					
					motorRight.setSpeed(value * speed);
					motorLeft.setSpeed(1/value * speed);
					
					
//					if (lls.getValue() <= 1200 && lls.getValue() > 900) {
//						System.out.println("test");
//						motorRight.setSpeed((int)Math.round(0.6*speed));
//						motorLeft.setSpeed((int)Math.round(0.4*speed));
//					} else if (lls.getValue() <= 900 && lls.getValue() > 600) {
//						motorRight.setSpeed((int)Math.round(0.7*speed));
//						motorLeft.setSpeed((int)Math.round(0.3*speed));
//					} else if (lls.getValue() <= 600 && lls.getValue() > 300) {
//						motorRight.setSpeed((int)Math.round(speed));
//						motorLeft.setSpeed((int)Math.round(0.4*speed));
//					} else if (lls.getValue() <= 300) {
//						motorRight.setSpeed((int)Math.round(1.2*speed));
//						motorLeft.setSpeed((int)Math.round(0.2*speed));
//					}
					
					motorLeft.forward();
					motorRight.forward();
					
//					tmpValue = lls.getValue();
//				
//					if (lls.getValue() <= 1200 && lls.getValue() > 900) {
//						factor = 1.5;
//					} else if (lls.getValue() <= 900 && lls.getValue() > 600) {
//						factor = 1.8;
//					} else if (lls.getValue() <= 600 && lls.getValue() > 300) {
//						factor = 2;
//					} else if (lls.getValue() <= 300) {
//						factor = 2.5;
//					} else if (lls.getValue() <= 150) {
//						factor = 2.7;
//						//motorLeft.setSpeed(0);
//					} else {
//						break;
//					}
	
	
					//motorRight.setSpeed((int)(motorLeft.getSpeed()));
					//motorRight.forward();
					
					System.out.println("left sensor: " + lls.getValue() + " factor: " + factor);
					System.out.println("right speed: " + motorRight.getSpeed() + " left speed: " + motorLeft.getSpeed());
				}
				//System.out.println("lls Value (links)" + lls.getValue());
				//System.out.println("left: " + motorLeft.getSpeed() + "right: " + motorRight.getSpeed());
				
				
			}
			
		}, 1200);
		rls.addLightListener(new LightListener()	{

			@Override
			public void bright(SensorPort port, int level) {
//				System.out.println("******RIGHT bright " + rls.getValue());
//				while(lls.getValue() < 1200){
//					if (rls.getValue() > 300) {
//						motorLeft.setSpeed((1/factor) * speed);
//						motorLeft.forward();
//					} else {
//						System.out.println("---BACKW left");
//						motorLeft.setSpeed(4);
//						motorLeft.backward();
//					}
//				}
				motorLeft.setSpeed(speed);
				motorLeft.forward();
			}

			@Override
			public void dark(SensorPort port, int level) {
			
				double factor = 1;
				//System.out.println("lls: " + lls.getValue() + " rls: " + rls.getValue());
				
				
				while (rls.getValue() <= 1200) {
					
				int value = (rls.getValue() / 1200) + 1;
					
				motorLeft.setSpeed(value * speed);
				motorRight.setSpeed(1/value * speed);
				
//				if (rls.getValue() <= 1200 && rls.getValue() > 900) {
//					motorLeft.setSpeed((int)Math.round(0.6*speed));
//					motorRight.setSpeed((int)Math.round(0.4*speed));
//				} else if (rls.getValue() <= 900 && rls.getValue() > 600) {
//					motorLeft.setSpeed((int)Math.round(0.7*speed));
//					motorRight.setSpeed((int)Math.round(0.3*speed));
//				} else if (rls.getValue() <= 600 && rls.getValue() > 300) {
//					motorLeft.setSpeed((int)Math.round(speed));
//					motorRight.setSpeed((int)Math.round(0.4*speed));
//				} else if (rls.getValue() <= 300) {
//					motorLeft.setSpeed((int)Math.round(1.2*speed));
//					motorRight.setSpeed((int)Math.round(0.2 * speed));
//					
//				}
				
				motorLeft.forward();
				motorRight.forward();
					
					
					
					
					
					
					
					
					
//					if (rls.getValue() <= 1200 && rls.getValue() > 900) {
//						factor = 1.5;
//					} else if (rls.getValue() <= 900 && rls.getValue() > 600) {
//						factor = 1.8;
//					} else if (rls.getValue() <= 600 && rls.getValue() > 300) {
//						factor = 2;
//					} else if (rls.getValue() <= 300) {
//						factor = 2.5;
//					} else if (rls.getValue() <= 150) {
//						factor = 2.7;
//						//motorRight.setSpeed(0);
//					} else {
//						break;
//					}
//					
//					
//					motorLeft.setSpeed((int)(motorRight.getSpeed()));
//					motorLeft.forward();
//					System.out.println("right sensor: " + rls.getValue() + " factor: " + factor);
//					System.out.println("right speed: " + motorRight.getSpeed() + " left speed: " + motorLeft.getSpeed());
					//System.out.println("left: " + motorLeft.getSpeed() + "right: " + motorRight.getSpeed());
					//System.out.println("rls Value (rechts)" + rls.getValue());
				}
			}
			
			
		}, 1200);
		while (!robot.isEscapeHit()) {
			
			
			//Versuch mit zwei Sensoren
			/*gear.forward();
			System.out.println(lls.getValue() + " lls");
			System.out.println(rls.getValue() + " rls");
			
			while(lls.getValue() < 1200)	{
				gear.leftArc(0.2, variable);
				if(lls.getValue() < 1000)	{
					gear.leftArc(0.05, (variable*3));
				}
				if (lls.getValue() < 800)	{
					gear.left();
				}
			}
				/*while(lls.getValue() < 1200)	{
					gear.leftArc(0.05, 10);
				}
					while (lls.getValue() < 800)	{
						gear.left();
					}
				*/
			
			
			/*while(rls.getValue() < 1200)	{
				gear.rightArc(0.2, variable);
			
				if(rls.getValue() < 1000)	{
					gear.rightArc(0.05, (variable*3));
				}
					if (lls.getValue() < 800)	{
						gear.right();
					}
			}
			
			*/
		}

	}

	public static void Data(){
		
	}
	
	
	public static void main(String[] args) {
		new LineFollower();		
	}

	private static void command(Socket s, String string) {
		try {
			InputStream is = s.getInputStream();
			OutputStream os = s.getOutputStream();
			sendCommand(os, string);
			readResponse(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static String readResponse(InputStream is) throws IOException {
		if (is == null)
			return "";
		byte[] buf = new byte[4096];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		boolean done = false;
		while (!done) {
			int len = is.read(buf);
			if (len == -1)
				throw new IOException("Stream closed");
			baos.write(buf, 0, len);
			if (buf[len - 1] == 10) // \n
				done = true;
		}
		String s = baos.toString("UTF-8");
		return s.substring(0, s.length() - 1); // Remove \n
	}

	private static void sendCommand(OutputStream os, String cmd)
			throws IOException {
		if (cmd == null || cmd.length() == 0 || os == null)
			throw new IOException("sendCommand failed.");
		cmd += "\n"; // Append \n
		byte[] ary = cmd.getBytes(Charset.forName("UTF-8"));
		os.write(ary);
		os.flush();
	}
}