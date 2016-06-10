package org.usfirst.frc.team5059.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
public class Tost extends IterativeRobot {
	RobotDrive myRobot;
	Joystick stick;
	
	
	public void teleopInit () {
		myRobot = new RobotDrive (0,1);
		stick = new Joystick (1);
		
	}
	public void teleopPeriodic () {
		myRobot.arcadeDrive(stick);
	}
}
