/**
 * Author:	RJ Castaneda
 * Date:	2/13
 */
/**
 * TODO
 * 		*Switch or Case statement
 * 		*Check for version control
 * 		*Test Button config
 * 		*Add functions
 * 			*Port
 * 			*Chevelle
 * 			*Rough
 * 			*Rock
 * 			*Moat
 * 			*Rampart
 * 			*Draw
 * 			*Sally Port
 * 			*Low Bar
 */
package org.usfirst.frc.team5059.robot;
//import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.AnalogInput;
//import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.VictorSP;

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
    
    //robot stuff
    //RobotDrive myChassis;
    Joystick drivePad;
    Joystick statesPad;
    TalonSRX spinWheels;
    TalonSRX armLifty;
    VictorSP leftSide;
    VictorSP rightSide;
    //Solenoid solenoidThing;
    CameraServer robotCamera;
    
    AnalogInput armPosition;
    DigitalInput armPortSwitch;
    DigitalInput selectSwitch;
    DigitalInput selectSwitch2;
    AnalogInput selectPot;
    
    
    
    double SPIN_SPEED		= 0.0;
    double chassisOffset	= 1.0;
    
    //autonomous variables	
    int autonomous 			= 1;
	double leftCalibration	= 0.25;
	double rightCalibration	= 0.25;
	AnalogGyro jiro;
    double Kp = 0.06;
    
    //button states
    boolean lBPX;
    boolean bIPX;
    
    //controller buttons
    int ballSpitButton;
    int ballSuckButton;
    
    //driver states modifiers
    int defaultButton	= 5;
    int actionButton	= 6;
    double slowDown		= 1.0;
    
    //controller buttons for states
    int portButton		= 10;
    int chevellButton	= 11;
    int roughButton		= 10;
    int armUpButton		= 2;
    int armDownButton	= 1;
    int rampartButton	= 11;
    int drawButton		= 15;
    int sallyButton		= 13;
    int lowBarButton	= 7;
    int ballGrabButton	= 5;
    int statesBallSpit	= 4;
    int driveStateButton= 6;
    int statesPadAction	= 8;
    int statesPadDefault= 9;
    int armUpStateButton= 12;
    
    //state machine variables
    double autoLiftSpeed;
    double armLiftBuffer;
    double liftOverride;
    double portPosition1;
    double portPosition2;
    double chevellPosition1;
    double chevellPosition2;
    double chevellWheelSpeed;
    double  ballGrabPosition1;
    double lowBarPosition;
    double ballGrabWheelSpeed;
    double ballSpitWheelSpeed;
    double armUpPosition;
    int terrainStates;
    int	portState;
    int chevellState;
    int ballGrabState;
    int driveState;
    int lowBarState;
    int rockState;
    int roughState;
    int moatState;
    int rampartState;
    int drawState;
    int sallyState;
    int defaultState;
    int armUpState;
	int portSwitch;
	int chevellSwitch;
	int ballGrabSwitch;
	int driveSwitch;
	int lowBarSwitch;
	int armUpSwitch;
    
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    
    public void robotInit() {
    	// autonomous stuff
    	//chooser = new SendableChooser();
        //chooser.addDefault("Low Bar", lowBar);
       // chooser.addObject("Arm Up", armUp);
       // chooser.addObject("Portacullis", portacullis);
       // SmartDashboard.putData("Auto choices", chooser);
        selectSwitch	= new DigitalInput(1);
        selectSwitch2	= new DigitalInput(2);
        selectPot		= new AnalogInput(2);
        
       // camera stuff
        robotCamera = CameraServer.getInstance();
        robotCamera.setQuality(50);
        robotCamera.startAutomaticCapture("cam0");

        //our robot stuff
       // myChassis	= new RobotDrive(0,1);
        jiro 		= new AnalogGyro(1);
        leftSide	= new VictorSP(1);
        rightSide	= new VictorSP(0);
        spinWheels	= new TalonSRX(2);
        armLifty	= new TalonSRX(3);
        drivePad	= new Joystick(0);
        statesPad	= new Joystick(1);
        LiveWindow.addActuator("stud", "talonsrx", armLifty);
        
        //drive base 
        
        //arm position stuff
        armPosition		= new AnalogInput(0);
        armPortSwitch	= new DigitalInput(0);
        
        //solenoid stuff
        //solenoidThing = new Solenoid(1);
        
        //init button states
        lBPX			= false;
        bIPX			= false;
        
        //driver buttons
        ballSpitButton	= 4;
        ballSuckButton	= 2;
        
        //state machine variables
        defaultState		= 0;
        portState			= 1;
        chevellState		= 2;
        ballGrabState		= 10;
        driveState			= 11;
        lowBarState			= 8;
        rockState			= driveState;
        roughState			= driveState;
        moatState			= driveState;
        rampartState		= driveState;
        drawState			= driveState;
        sallyState			= driveState;
        armUpState			= 12;
        liftOverride		= 0.09;
        terrainStates		= 0;
        autoLiftSpeed		= 1.0;
        armLiftBuffer		= 0.01;
        //port state variables
        portPosition1		= 1.7;
        portPosition2		= 0.9;
        portSwitch			= 0;
        //chevell state variables
        chevellPosition1	= 1.135;
        chevellPosition2	= 2.0;
        chevellSwitch 		= 0;
        chevellWheelSpeed	= 0.3;
        //ball grab state variables
        ballGrabPosition1	= 1.35;
        ballGrabSwitch		= 0;
        ballGrabWheelSpeed	= -0.635;
        ballSpitWheelSpeed	= 1.0;
        //lowbar state variables
        lowBarPosition		= 1.6;
        lowBarSwitch		= 0;
        //drive state variables
        driveSwitch			= 0;
        //arm up state variables
        armUpSwitch			= 0;
        armUpPosition		= 1.0;
        
    }
    
	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString line to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the switch structure below with additional strings.
	 * If using the SendableChooser make sure to add them to the chooser code above as well.
	 */
    
    public void autonomousInit() {
    	//autoSelected = (String) chooser.getSelected();
		//autoSelected = SmartDashboard.getString("Auto Selector", defaultAuto);
		//System.out.println("Auto selected: " + autoSelected);
		/*if (selectPot.getVoltage() > 9.0 && (selectPot.getVoltage() < 12.3)){
			
			autoSelected = portacullis;
		}
		else if (selectPot.getVoltage() > 13.5 && (selectPot.getVoltage() < 15.0)){
			
			autoSelected = lowBar;
		}
		else{autoSelected = armUp;}*/
		
    	
    	
    	
    	//autoSelected = portacullis;
		autoSelected = armUp;
		//autoSelected = lowBar;
		
		
    }

    /**
     * This function is called periodically during autonomous
     */
    
    public void autonomousPeriodic() {
    	System.out.println("switch1" + selectPot.getVoltage());
    	goStahp();
/*if (selectPot.getVoltage() > 9.0 && (selectPot.getVoltage() < 12.3)){
			
			autoSelected = portacullis;
			System.out.println("here in stuff");
		}
		else if (selectPot.getVoltage() > 13.5 && (selectPot.getVoltage() < 15.0)){
			
			autoSelected = lowBar;
			System.out.println("here in stuff3333333333333333333333");
		}
		else{autoSelected = armUp;System.out.println("here in stuff2222222222222222222222222");}*/
    	switch(autoSelected) {
    	case portacullis:
    		// portAuto(speed, speedAfterOpen, time, timeAfterOpen, portPositionAuto)
    		portAuto(0.6, 0.5, 4.0, 2.0, 1.6);
            break;
    	case armUp:
    		// goStraight(speed, time)
    		goStraight(1.0, 4.0);
    		break;
    	case lowBar:
    		// lowBarAuto(speed, time, lowBarPositionAuto)
    		lowBarAuto(0.35, 5.0, 1.6);
    		break;
    	case stop:
    	default:
    		goStahp();
            break;
    	}
    	
    }
    /**
     *  Autonomous Methods
     */
    public void goStraight(double speed, double time) {
    	double timestamp = Timer.getFPGATimestamp();

		jiro.reset();
		System.out.println("here in go straight");
		System.out.println(Timer.getFPGATimestamp() - timestamp);

		
		
    	while((Timer.getFPGATimestamp() - timestamp)< time && isEnabled()){
	    	double angle = jiro.getAngle(); // get current heading
	    	SmartDashboard.putNumber("RIGHT_MOTOR",rightSide.get());
	    	SmartDashboard.putNumber("LEFT_MOTOR", leftSide.get());
	    	SmartDashboard.putNumber("ANGLE",angle*Kp);
	    	rightSide.set(((-speed/2 + -(angle*Kp)/2)*2));
	    	leftSide.set(((speed/2 - (angle*Kp)/2)*2));
	    	
	    	System.out.println("here in go straight while loop");
	    	System.out.println(Timer.getFPGATimestamp() - timestamp);
	    	smartDashboardVariables();
    	
	    	Timer.delay(0.003);
    	}
    	
    	autoSelected = "stop";
    	
    }
    
    public void lowBarAuto(double speed, double time, double lowBarPositionAuto){
    	smartDashboardVariables();
    	if(armPosition.getAverageVoltage() <= lowBarPositionAuto - armLiftBuffer){
			armLifty.set(autoLiftSpeed);
		}
		else if(armPosition.getAverageVoltage() >= lowBarPositionAuto + armLiftBuffer ){
			armLifty.set(-autoLiftSpeed);
		}
		else{armLifty.set(0.0);goStraight(speed, time);}
    	
    }
    
    public void portAuto(double speed, double speedAfterOpen, double time, double timeAfterOpen, double portPositionAuto){
    	smartDashboardVariables();
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
		    	rightSide.set(((-speed/2 + -(angle*Kp)/2)*2));
		    	leftSide.set(((speed/2 - (angle*Kp)/2)*2));
		    	armLifty.set(autoLiftSpeed);
		    	
		    	System.out.println("here in porticullis straight while loop");
		    	System.out.println(Timer.getFPGATimestamp() - timestamp);
		    	smartDashboardVariables();
	    	
		    	Timer.delay(0.003);
	    	}
	    	while((Timer.getFPGATimestamp() - timestamp)>= time && isEnabled() && armPortSwitch.get()){armLifty.set(0.0); goStahp();}
	    	
			//portAuto = 1;
		}
		else{
			goStraight(speedAfterOpen, timeAfterOpen);
			//portAuto = 2;
			
		}
    	
    	
    }
    
    public void goStahp(){
    	rightSide.set(0);
    	leftSide.set(0);
    	Timer.delay(0.003);
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	
    	while (isOperatorControl() && isEnabled()) {
    		smartDashboardVariables();
    		//driver control functions
    		driverControl();
        	//state machine
        	terrainStates();
            Timer.delay(0.005);		// wait for a motor update time
        }
    	Timer.delay(0.005);
    }
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    	terrainStates();
    	smartDashboardVariables();
    }
    /**
     * These are functions for the teleop
     */
    //arcade style driving with the controller
   //control the lift arm with the right analog stick
    public void driverControl(){
    	rightSide.set(((drivePad.getX()/2 + drivePad.getY()/2)*2)*slowDown);
    	leftSide.set(((drivePad.getX()/2 - drivePad.getY()/2)*2)*slowDown);
    	
    	if (drivePad.getRawButton(8)){slowDown = 1.0;}
    	else {slowDown = 0.65;}
    }
    public void liftArm(){
    	armLifty.set(-drivePad.getThrottle());
    	if(statesPad.getRawButton(armUpButton)){armLifty.set(1.0);}
    	else if(statesPad.getRawButton(armDownButton)){armLifty.set(-1.0);}
    	else {armLifty.set(-drivePad.getThrottle());}
    }
    public void wheelArms(){
    	//wheel arm code
    	if (drivePad.getRawButton(ballSpitButton) && !drivePad.getRawButton(ballSuckButton)|| statesPad.getRawButton(statesBallSpit) && !statesPad.getRawButton(ballGrabButton)){spinWheels.set(ballSpitWheelSpeed);}
    	else if (drivePad.getRawButton(ballSuckButton) && !drivePad.getRawButton(ballSpitButton))
    	{
    		spinWheels.set(ballGrabWheelSpeed);
    	}
    	else{spinWheels.set(0.0);}
    }
    
    /**
     * 	STATES FOR THE TERRAIN
     */
    public void terrainStates(){
    	if(statesPad.getRawButton(portButton)){terrainStates = portState;}
    	else if(statesPad.getRawButton(chevellButton)){terrainStates = chevellState;}
    	else if(statesPad.getRawButton(armUpStateButton)){terrainStates = armUpState;}
    	else if(statesPad.getRawButton(ballGrabButton)){terrainStates = ballGrabState;}
    	else if(statesPad.getRawButton(driveStateButton)){terrainStates = driveState;}
    	else if(statesPad.getRawButton(lowBarButton)){terrainStates = lowBarState;}
    	else if(statesPad.getRawButton(roughButton)){terrainStates = driveState;}
    	else if(statesPad.getRawButton(rampartButton)){terrainStates = driveState;}
    	else if(statesPad.getRawButton(drawButton)){terrainStates = driveState;}
    	else if(statesPad.getRawButton(sallyButton)){terrainStates = driveState;}
    	else if(statesPad.getRawButton(statesPadDefault) || drivePad.getRawButton(defaultButton)){terrainStates = 0;}
    	
    	switch (terrainStates) {
	    	case 1: 
	    		port();
	    		System.out.println("HERE IN ONE");
	            break;
	    	case 2:	
	    		chevell();
	    		System.out.println("HERE IN TWO");
	        	break;
	        case 8: 
	        	lowBar();
	        	System.out.println("HERE IN EIGHT");
	        	break;
	        case 10: 
	        	ballGrab();
				System.out.println("HERE IN TEN");
				break;
			case 11:
				drivingState();
				System.out.println("HERE IN ELEVEN");
				break;
			case 12:
				armUpState();
				System.out.println("HERE IN TWELVE");
				break;
	        default: 
	        	//reset all of the states
	        	portSwitch 		= 0;
	        	chevellSwitch	= 0;
	        	ballGrabSwitch	= 0;
	        	driveSwitch		= 0;
	        	armUpSwitch		= 0;
	        	liftArm();
	        	wheelArms();
	        	System.out.println("HERE IN DEFAULT");
    	}
    	System.out.println("OUTSIDE THE SWITCH");
    }
    public void port(){
    	//TODO
    	if(portSwitch == 0){portSwitch = 1;}
    	if(!(spinWheels.get() == 0)){spinWheels.set(0.0);}
    	switch(portSwitch){
    		case 1: //lower the front wheels
    			if(armPosition.getAverageVoltage() >= portPosition1 && (!armPortSwitch.get() || statesPad.getRawButton(statesPadAction) || drivePad.getRawButton(actionButton))){
    				portSwitch = 2;
    			}else if(drivePad.getThrottle() > 0.05){armLifty.set(-drivePad.getThrottle());}
    			else{armLifty.set(autoLiftSpeed);}
    			System.out.println("HERE IN PORT CASE 1");
    			break;
    		case 2: //raise the port
    			if(armPosition.getAverageVoltage() <= portPosition2){
    				portSwitch = 3;
    			}else if(drivePad.getThrottle() < -0.1){armLifty.set(-drivePad.getThrottle());}
    			else{armLifty.set(-autoLiftSpeed);}
    			System.out.println("HERE IN PORT CASE 2");
    			break;
    		default: //return to driver control
    			armLifty.set(-drivePad.getThrottle());
    			System.out.println("HERE IN PORT CASE DEFAULT");
    			portSwitch = 0;
    			terrainStates = 0;
    			break;
    	}
    	
    	System.out.println("HERE IN PORT");
    }
    public void chevell(){
    	//TODO
    	if(chevellSwitch == 0){chevellSwitch = 1;}
    	switch(chevellSwitch){
    		case 1: //raise the front wheels
    			spinWheels.set(-chevellWheelSpeed);
    			if(armPosition.getAverageVoltage() <= chevellPosition1 - armLiftBuffer && (drivePad.getThrottle() <= liftOverride)){
    				armLifty.set(autoLiftSpeed);
    			}
    			
    			else if(armPosition.getAverageVoltage() >= chevellPosition1 + armLiftBuffer && (drivePad.getThrottle() >= -liftOverride)){
    				armLifty.set(-autoLiftSpeed);
    			}
    			else {armLifty.set(0.0);}
    			if (drivePad.getRawButton(actionButton) || statesPad.getRawButton(statesPadAction)){
    				chevellSwitch = 2;	
    			}
    			System.out.println("HERE IN CHEVELL CASE 1");
    			break;
    		case 2: //lower the chevell while spinning frontwheels
    			if(!statesPad.getRawButton(statesPadAction) || !drivePad.getRawButton(defaultButton) || !drivePad.getRawButton(actionButton))
    				{
    					armLifty.set(autoLiftSpeed);
    					spinWheels.set(-chevellWheelSpeed);
    				}
    			else{chevellSwitch = 3;}
    			System.out.println("HERE IN CHEVELL CASE 2");
    			break;
    		default: //return to driver control
    			armLifty.set(-drivePad.getThrottle());
    			chevellSwitch = 0;
    			terrainStates = 0;
    			spinWheels.set(0.0);
    			System.out.println("HERE IN CHEVELL CASE 3");
    			break;
    	}
    	System.out.println("HERE IN CHEVELL");
    }
    public void lowBar(){
    	//TODO
    	if(lowBarSwitch == 0){lowBarSwitch = 1;}
    	if(!(spinWheels.get() == 0)){spinWheels.set(0.0);}
    	switch(lowBarSwitch){
    		case 1: //raise the front wheels set to the wheel grab position
    			if(armPosition.getAverageVoltage() <= lowBarPosition - armLiftBuffer && (drivePad.getThrottle() <= liftOverride)){
    				armLifty.set(autoLiftSpeed);
    			}
    			else if(armPosition.getAverageVoltage() >= lowBarPosition + armLiftBuffer && (drivePad.getThrottle() >= -liftOverride)){
    				armLifty.set(-autoLiftSpeed);
    			}
    			else {lowBarSwitch = 2;}
    			if (statesPad.getRawButton(actionButton) || drivePad.getRawButton(actionButton)){
    				lowBarSwitch = 2;
    			}
    			break;
    		default: //return to driver control
    			armLifty.set(-drivePad.getThrottle());
    			lowBarSwitch = 0;
    			terrainStates = 0;
    			break;
    	}
    	System.out.println("HERE IN LOWBAR");
    }
    public void ballGrab(){
    	//TODO
    	if(ballGrabSwitch == 0){ballGrabSwitch = 1;}
    	switch(ballGrabSwitch){
    		case 1: //raise the front wheels
    			spinWheels.set(ballGrabWheelSpeed);
    			if(armPosition.getAverageVoltage() <= ballGrabPosition1 - armLiftBuffer && (drivePad.getThrottle() <= liftOverride)){
    				armLifty.set(autoLiftSpeed);
    			}
    			else if(armPosition.getAverageVoltage() >= ballGrabPosition1 + armLiftBuffer && (drivePad.getThrottle() >= -liftOverride)){
    				armLifty.set(-autoLiftSpeed);
    			}
    			else {armLifty.set(-drivePad.getThrottle());}
    			if (statesPad.getRawButton(statesPadAction) || statesPad.getRawButton(statesBallSpit) || drivePad.getRawButton(ballSpitButton) || drivePad.getRawButton(actionButton)){
    				ballGrabSwitch = 2;	
    			}
    			break;
    		default: //return to driver control
    			armLifty.set(-drivePad.getThrottle());
    			ballGrabSwitch = 0;
    			terrainStates = 0;
    			spinWheels.set(0.0);
    			break;
    	}
    	System.out.println("HERE IN BALLGRAB");
    }
    public void armUpState(){
    	if(armUpSwitch == 0){armUpSwitch = 1;}
    	if(!(spinWheels.get() == 0)){spinWheels.set(0.0);}
    	switch(armUpSwitch){
    		case 1: //raise the front wheels set to the maximum up position
    			/*if(armPosition.getAverageVoltage() >= armUpPosition || ( statesPad.getRawButton(statesPadAction) || drivePad.getRawButton(actionButton))){
    				armUpSwitch = 2;
    			}else if(drivePad.getThrottle() > 0.05){armLifty.set(-drivePad.getThrottle());}
    			else{armLifty.set(-autoLiftSpeed);}*/
    			if(armPosition.getAverageVoltage() > 0.25){
    					armLifty.set(-autoLiftSpeed);
    			}else{armUpSwitch = 2;}
    			System.out.println("HERE IN ARM UP CASE 1");
    			break;
    		default: //return to driver control
    			armLifty.set(-drivePad.getThrottle());
    			armUpSwitch = 0;
    			terrainStates = 0;
    			break;
    	}
    	System.out.println("HERE IN ARM UP STATE");
    	
    }
    public void drivingState(){
    	//TODO
    	if(driveSwitch == 0){driveSwitch = 1;}
    	if(!(spinWheels.get() == 0)){spinWheels.set(0.0);}
    	switch(driveSwitch){
    		case 1: //raise the front wheels set to the wheel grab position
    			if(armPosition.getAverageVoltage() <= ballGrabPosition1 - armLiftBuffer && (drivePad.getThrottle() <= liftOverride)){
    				armLifty.set(autoLiftSpeed);
    			}
    			else if(armPosition.getAverageVoltage() >= ballGrabPosition1 + armLiftBuffer && (drivePad.getThrottle() >= -liftOverride)){
    				armLifty.set(-autoLiftSpeed);
    			}
    			else {driveSwitch = 2;}
    			if (statesPad.getRawButton(actionButton)){
    				driveSwitch = 2;
    			}
    			break;
    		default: //return to driver control
    			armLifty.set(-drivePad.getThrottle());
    			driveSwitch = 0;
    			terrainStates = 0;
    			break;
    	}
    	System.out.println("HERE IN DRIVING STATE");
    }
    /**
     * smartDashboardVariables CODE
     */
	public void smartDashboardVariables(){
    	SmartDashboard.putNumber("joystick values", drivePad.getY());
    	SmartDashboard.putNumber("leftSide", leftSide.get());
    	SmartDashboard.putNumber("rightSide", rightSide.get());
    	SmartDashboard.putNumber("ARM LIFT", armLifty.get());
    	SmartDashboard.putNumber("ARM POSITION", armPosition.getAverageVoltage());
    	SmartDashboard.putNumber("ARM WHEELSPEED", spinWheels.get());
    }
}