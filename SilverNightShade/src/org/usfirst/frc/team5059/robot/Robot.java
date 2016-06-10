
package org.usfirst.frc.team5059.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.RobotDrive;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
    final String defaultAuto = "Default";
    final String customAuto = "My Auto";
    final String lowBar		= "Low Bar";
    final String portacullis= "Portacullis";
    final String armUp		= "Arm Up";
    final String stop		= "Stop";
    String autoSelected;
    SendableChooser chooser;
    

    Joystick drivePad;
    double slowDown		= 1.0;
    
    VictorSP rightMotor;
    VictorSP leftMotor;
    TalonSRX armLifty;
    
    AnalogInput armPosition;
    DigitalInput armPortSwitch;
    
    AnalogGyro jiro;
    double Kp = 0.06;
    
    int otto = 1;
    double lowBarPosition;
    double armLiftBuffer;
    double autoLiftSpeed;

    
    int portAuto;

	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        chooser = new SendableChooser();
        chooser.addDefault("Low Bar", lowBar);
        chooser.addObject("Arm Up", armUp);
        chooser.addObject("Portacullis", portacullis);
        SmartDashboard.putData("Auto choices", chooser);


    	rightMotor = new VictorSP(0);
	    leftMotor = new VictorSP(1);
        armLifty	= new TalonSRX(3);
        
        drivePad	= new Joystick(0);
        
        jiro = new AnalogGyro(1);
        armPosition		= new AnalogInput(0);
        armPortSwitch	= new DigitalInput(0);
        

        lowBarPosition		= 1.6;
        armLiftBuffer		= 0.01;
        autoLiftSpeed		= 1.0;
		portAuto = 0;
    }
    
	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString line to get the auto name from the text box
	 * below the interfaces
	 *
	 * You can add additional auto modes by adding additional comparisons to the switch structure below with additional strings.
	 * If using the SendableChooser make sure to add them to the chooser code above as well.
	 */
    public void autonomousInit() {
    	autoSelected = (String) chooser.getSelected();
//		autoSelected = SmartDashboard.getString("Auto Selector", defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
		
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	goStahp();
    	switch(autoSelected) {
    	case portacullis:
    		// portAuto(speed, speedAfterOpen, time, timeAfterOpen, portPositionAuto)
    		portAuto(0.35, 0.75, 10.0, 3.0, 1.6);
            break;
    	case armUp:
    		// goStraight(speed, time)
    		goStraight(0.35, 3.0);
    		break;
    	case lowBar:
    		// lowBarAuto(speed, time, lowBarPositionAuto)
    		lowBarAuto(0.35, 13.0, 1.6);
    		break;
    	case stop:
    	default:
    		goStahp();
            break;
    	}
    	
    	
    }
    
    public void goStraight(double speed, double time) {
    	double timestamp = Timer.getFPGATimestamp();

		jiro.reset();
		System.out.println("here in go straight");
		System.out.println(Timer.getFPGATimestamp() - timestamp);

		
		
    	while((Timer.getFPGATimestamp() - timestamp)< time && isEnabled()){
	    	double angle = jiro.getAngle(); // get current heading
	    	SmartDashboard.putNumber("RIGHT_MOTOR",rightMotor.get());
	    	SmartDashboard.putNumber("LEFT_MOTOR", leftMotor.get());
	    	SmartDashboard.putNumber("ANGLE",angle*Kp);
	    	rightMotor.set(((-speed/2 + -(angle*Kp)/2)*2));
	    	leftMotor.set(((speed/2 - (angle*Kp)/2)*2));
	    	
	    	System.out.println("here in go straight while loop");
	    	System.out.println(Timer.getFPGATimestamp() - timestamp);
	    	debug();
    	
	    	Timer.delay(0.003);
    	}
    	
    	autoSelected = "stop";
    	
    }
    
    public void lowBarAuto(double speed, double time, double lowBarPositionAuto){
    	debug();
    	if(armPosition.getAverageVoltage() <= lowBarPositionAuto - armLiftBuffer){
			armLifty.set(autoLiftSpeed);
		}
		else if(armPosition.getAverageVoltage() >= lowBarPositionAuto + armLiftBuffer ){
			armLifty.set(-autoLiftSpeed);
		}
		else{armLifty.set(0.0);goStraight(speed, time);}
    	
    }
    
    public void portAuto(double speed, double speedAfterOpen, double time, double timeAfterOpen, double portPositionAuto){
    	debug();
    	if(armPosition.getAverageVoltage() <= portPositionAuto){
			armLifty.set(autoLiftSpeed);
		}
		else if(armPortSwitch.get() && armPosition.getAverageVoltage() >= portPositionAuto){
			
			double timestamp = Timer.getFPGATimestamp();
			
			armLifty.set(0.0);
			jiro.reset();
			System.out.println("here in go straight");
			System.out.println(Timer.getFPGATimestamp() - timestamp);

			
			
	    	while((Timer.getFPGATimestamp() - timestamp)< time && isEnabled() && armPortSwitch.get()){
		    	double angle = jiro.getAngle(); // get current heading
		    	SmartDashboard.putNumber("ANGLE",angle*Kp);
		    	rightMotor.set(((-speed/2 + -(angle*Kp)/2)*2));
		    	leftMotor.set(((speed/2 - (angle*Kp)/2)*2));
		    	
		    	System.out.println("here in porticullis straight while loop");
		    	System.out.println(Timer.getFPGATimestamp() - timestamp);
		    	debug();
	    	
		    	Timer.delay(0.003);
	    	}
	    	while((Timer.getFPGATimestamp() - timestamp)>= time && isEnabled() && armPortSwitch.get()){goStahp();}
	    	
			//portAuto = 1;
		}
		else{
			goStraight(speedAfterOpen, timeAfterOpen);
			//portAuto = 2;
			
		}
    	
    	
    }
    
    public void goStahp(){
    	rightMotor.set(0);
    	leftMotor.set(0);
    	Timer.delay(0.003);
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
	    while (isOperatorControl() && isEnabled()) {
	    	double angle = jiro.getAngle();
    		//driver control functions
    		drivingTheBot();
    		debug();
            Timer.delay(0.005);		// wait for a motor update time
        }
    }
    

public void drivingTheBot(){
    	
    	rightMotor.set(((drivePad.getX()/2 + drivePad.getY()/2)*2)*slowDown);
    	leftMotor.set(((drivePad.getX()/2 - drivePad.getY()/2)*2)*slowDown);
    	
    	if (drivePad.getRawButton(8)){slowDown = 0.50;}
    	else {slowDown = 1.0;}
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    public void debug(){
 		System.out.println("JIRO		=		" + jiro.getAngle());
 		SmartDashboard.putNumber("joystick values", drivePad.getY());
		SmartDashboard.putNumber("leftSide", leftMotor.get());
		SmartDashboard.putNumber("rightSide", rightMotor.get());
		SmartDashboard.putNumber("ARM LIFT", armLifty.get());
		SmartDashboard.putNumber("ARM POSITION", armPosition.getAverageVoltage());
		SmartDashboard.putBoolean("PORT SWITCH", armPortSwitch.get());
     }
    
}
