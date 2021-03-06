package org.usfirst.frc.team5059.robot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.VictorSP;

public class Robot extends IterativeRobot {
    final String defaultAuto = "Default";
    final String customAuto  = "My Auto";
    String autoSelected;
    SendableChooser chooser;
    
    RobotDrive myChassis;
    Joystick   drivePad;
    TalonSRX   spinWheels;
    TalonSRX   armLifty;
    VictorSP motorLeft;
    VictorSP motorRight;
    double SPIN_SPEED;
    double STRAIGHT_SPEED = 1.0;
    double 	offset = 0.25;
    int robotState;
    
    public void robotInit() {
        chooser = new SendableChooser();
        chooser.addDefault("Default Auto", defaultAuto);
        chooser.addObject("My Auto", customAuto);
        SmartDashboard.putData("Auto choices", chooser);

        //our robot stuff
        myChassis	= new RobotDrive (0,1);
        spinWheels	= new TalonSRX (2);
        armLifty	= new TalonSRX (3);
        drivePad	= new Joystick (0);
        
    }
    
    public void autonomousInit() {
    	autoSelected = (String) chooser.getSelected();
		autoSelected = SmartDashboard.getString("Auto Selector", defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
    }
    
    public void autonomousPeriodic() {
    	switch(autoSelected) {
    	case customAuto:
        //Put custom auto code here   
            break;
    	case defaultAuto:
    		break;
    	default:
    	//Put default auto code here
            break;
    	}
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	//Function that drives.
    	driveRobot();
    	
    	//Function that makes the Spin Feed go up and down.
    	liftArmUpDown();
    	
    	//This is where we put the "print outs".
    	debug();
    	
    	//This doesn't need it's own function.
    	goStraight();
    	Timer.delay(3.0);
    	stop();
    	Timer.delay(3.0);
    	goLeft();
    	Timer.delay(3.0);
    	goRight();
    	Timer.delay(3.0);
    
    }

    public void testPeriodic() {
    
    }
    public void driveRobot() {
    	myChassis.arcadeDrive(drivePad.getY() * 0.75,-drivePad.getX() * 0.75);
    }
    public void liftArmUpDown() {
    	if (drivePad.getRawButton(4) && !drivePad.getRawButton(2)){spinWheels.set(SPIN_SPEED);}
    	else if (drivePad.getRawButton(2) && !drivePad.getRawButton(4) ) {spinWheels.set(-SPIN_SPEED);}
    	else {spinWheels.set(0);}
    	
    	if (drivePad.getRawButton(5) && !drivePad.getRawButton(6)) {SPIN_SPEED = SPIN_SPEED - 0.01;}
    	else if (drivePad.getRawButton(6) && !drivePad.getRawButton(5)) {SPIN_SPEED = SPIN_SPEED + 0.01;}
    	
    	if (SPIN_SPEED >= 1){SPIN_SPEED = 1.0;}
    	if (SPIN_SPEED <= 0){SPIN_SPEED = 0.0;}

    	armLifty.set(-drivePad.getThrottle());
    	//I really don't know why you commented this out, but it looked important..
       	//spinWheels.set(SPIN_SPEED);
    }
    //public void robotStates() {
    	//switch(robotState) {
    	//case 1:
    	//	goStraight();
    	//	System.out.println("Going Straight");
    	//	break;
    	//case 2:
    	//	goLeft();
    	//	System.out.println("Turning Left");
    	//	break;
    	//case 3:
    	//	goRight();
    	//	System.out.println("Turning Right");
    	//	break;
    	//case 4:
    	//	stop();
    	//	System.out.println("Stop");
    	//}
    public void goStraight() {
    motorLeft.set(STRAIGHT_SPEED * offset);
    motorRight.set(-STRAIGHT_SPEED * offset);
    }
    public void goRight () {
    	motorLeft.set(STRAIGHT_SPEED * offset);
    	motorRight.set(STRAIGHT_SPEED * offset);
    }
    public void goLeft() {
    	motorLeft.set(-STRAIGHT_SPEED * offset);
    	motorRight.set(-STRAIGHT_SPEED * offset);
    }
    public void stop() {
    	motorLeft.set(STRAIGHT_SPEED * 0.0);
    	motorRight.set(STRAIGHT_SPEED * 0.0);
    }
    public void debug() {
    	System.out.println("WHEEL_SPEED = " + SPIN_SPEED);
    }
   
}
