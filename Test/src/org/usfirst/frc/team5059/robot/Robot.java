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
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.AnalogInput;
//import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.CameraServer;

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
    
    double SPIN_SPEED		= 0.0;
    double chassisOffset	= 1.0;
    
    //button states
    boolean lBPX;
    boolean bIPX;
    
    //controller buttons
    int ballSpitButton;
    int ballSuckButton;
    
    //driver states modifiers
    int defaultButton	= 5;
    int actionButton	= 6;
    
    //controller buttons for states
    int portButton		= 12;
    int chevellButton	= 14;
    int roughButton		= 10;
    int rockButton		= 1;
    int moatButton		= 2;
    int rampartButton	= 11;
    int drawButton		= 15;
    int sallyButton		= 13;
    int lowBarButton	= 7;
    int ballGrabButton	= 5;
    int statesBallSpit	= 4;
    int driveStateButton= 6;
    int statesPadAction	= 8;
    int statesPadDefault= 9;
    
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
	int portSwitch;
	int chevellSwitch;
	int ballGrabSwitch;
	int driveSwitch;
	int lowBarSwitch;
    
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    
    public void robotInit() {
        chooser = new SendableChooser();
        chooser.addDefault("Default Auto", defaultAuto);
        chooser.addObject("My Auto", customAuto);
        SmartDashboard.putData("Auto choices", chooser);
        
       // camera stuff
        robotCamera = CameraServer.getInstance();
        robotCamera.setQuality(50);
        robotCamera.startAutomaticCapture("cam0");

        //our robot stuff
       // myChassis	= new RobotDrive(0,1);
        leftSide	= new VictorSP(1);
        rightSide	= new VictorSP(0);
        spinWheels	= new TalonSRX(2);
        armLifty	= new TalonSRX(3);
        drivePad	= new Joystick(0);
        statesPad	= new Joystick(1);
        LiveWindow.addActuator("stud", "talonsrx", armLifty);
        
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
        driveSwitch			= 1;
        
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
    	autoSelected = (String) chooser.getSelected();
//		autoSelected = SmartDashboard.getString("Auto Selector", defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
    }

    /**
     * This function is called periodically during autonomous
     */
    
    public void autonomousPeriodic() {
    	switch(autoSelected) {
    	case customAuto:
        //Put custom auto code here   
            break;
    	case defaultAuto:
    	default:
    	//Put default auto code here
            break;
    	}
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	
    	while (isOperatorControl() && isEnabled()) {
    		debug();
    		smartDashboardVariables();
    		//driver control functions
    		driverControl();
        	//state machine
        	terrainStates();
        	//this function always comes last
        	//armSlack();
            Timer.delay(0.005);		// wait for a motor update time
        }
    	Timer.delay(0.005);
    }
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    	terrainStates();
    	debug();
    }
    /**
     * These are functions for the teleop
     */
  //stops the arms if they ever go down far enough to make slack in the chain
    //public void armSlack(){if(!armStopBottom.get()){armLifty.set(0);}}
    //arcade style driving with the controller
    public void drivingTheBot(){
    	//myChassis.arcadeDrive(drivePad.getY() * chassisOffset,-drivePad.getX() * chassisOffset,true);
    	rightSide.set((drivePad.getX()/2 + drivePad.getY()/2)*2);
    	leftSide.set((drivePad.getX()/2 - drivePad.getY()/2)*2);
    	
    	/*if(leftSide.get() >= 1.0){leftSide.set(1.0);}
    	else if(leftSide.get() <= -1.0){leftSide.set(-1.0);}
    	else{}
    	if(rightSide.get() >= 1.0){rightSide.set(1.0);}
    	else if(rightSide.get() <= -1.0){rightSide.set(-1.0);}
    	else{}
    	/*if(drivePad.getRawButton(8)){
    		chassisOffset = 1.00;
    	}
    	else{
    		chassisOffset = 0.75;} 
    	}*/
    }
   //control the lift arm with the right analog stick
    public void liftArm(){armLifty.set(-drivePad.getThrottle());}
    public void wheelArms(){
    	//wheel arm code
    	if (drivePad.getRawButton(ballSpitButton) && !drivePad.getRawButton(ballSuckButton)|| statesPad.getRawButton(statesBallSpit) && !statesPad.getRawButton(ballGrabButton)){spinWheels.set(ballSpitWheelSpeed);}
    	else if (drivePad.getRawButton(ballSuckButton) && !drivePad.getRawButton(ballSpitButton))
    	{
    		spinWheels.set(ballGrabWheelSpeed);
    	}
    	else{spinWheels.set(0.0);}
    }
    public void driverControl(){
    	drivingTheBot();
    }
    /**
     * 	STATES FOR THE TERRAIN
     */
    public void terrainStates(){
    	if(statesPad.getRawButton(portButton)){terrainStates = portState;}
    	else if(statesPad.getRawButton(chevellButton)){terrainStates = chevellState;}
    	else if(statesPad.getRawButton(ballGrabButton)){terrainStates = ballGrabState;}
    	else if(statesPad.getRawButton(driveStateButton)){terrainStates = driveState;}
    	else if(statesPad.getRawButton(lowBarButton)){terrainStates = lowBarState;}
    	else if(statesPad.getRawButton(roughButton)){terrainStates = driveState;}
    	else if(statesPad.getRawButton(rockButton)){terrainStates = driveState;}
    	else if(statesPad.getRawButton(moatButton)){terrainStates = driveState;}
    	else if(statesPad.getRawButton(rampartButton)){terrainStates = driveState;}
    	else if(statesPad.getRawButton(drawButton)){terrainStates = driveState;}
    	else if(statesPad.getRawButton(sallyButton)){terrainStates = driveState;}
    	else if(statesPad.getRawButton(statesPadDefault) || drivePad.getRawButton(defaultButton)){terrainStates = driveState;}
    	
    	switch (terrainStates) {
	    	case 1: 
	    		port();
	    		System.out.println("HERE IN ONE");
	            break;
	    	case 2:	
	    		chevell();
	    		System.out.println("HERE IN TWO");
	        	break;
	        case 3:
	        	roughT();
	        	System.out.println("HERE IN THREE");
	        	break;
	        case 4: 
	        	moat();
	        	System.out.println("HERE IN FOUR");
	        	break;
	        case 5:
	        	rampart();
    			System.out.println("HERE IN FIVE");
    			break;
	        case 6: 
	        	drawBridge();
    			System.out.println("HERE IN SIX");
    			break;
	        case 7: 
	        	sallyPort();
    			System.out.println("HERE IN SEVEN");
    			break;
	        case 8: 
	        	lowBar();
	        	System.out.println("HERE IN EIGHT");
	        	break;
	        case 9: 
	        	rockWall();
    			System.out.println("HERE IN NINE");
    			break;
	        case 10: 
	        	ballGrab();
				System.out.println("HERE IN TEN");
				break;
			case 11:
				drivingState();
				System.out.println("HERE IN TWELVE");
				break;
	        default: 
	        	//reset all of the states
	        	portSwitch 		= 0;
	        	chevellSwitch	= 0;
	        	ballGrabSwitch	= 0;
	        	driveSwitch		= 0;
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
    			if(armPosition.getAverageVoltage() >= portPosition1 && (armPortSwitch.get() || statesPad.getRawButton(statesPadAction) || drivePad.getRawButton(actionButton))){
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
    			if(!statesPad.getRawButton(statesPadDefault)|| !statesPad.getRawButton(statesPadAction) || !drivePad.getRawButton(defaultButton) || !drivePad.getRawButton(actionButton))
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
    public void roughT(){
    	//TODO
    	int roughSwitch = 0;
    	switch(roughSwitch){
    		case 1: //raise the front wheels
    			armLifty.set(autoLiftSpeed);
    			break;
    		default: //return to driver control
    			armLifty.set(-drivePad.getThrottle());//return to driver
    			break;
    	}
    	System.out.println("HERE IN ROUGHT");
    }
    public void moat(){
    	//TODO
    	int moatSwitch = 0;
    	switch(moatSwitch){
    		case 1: //raise the front wheels
    			armLifty.set(autoLiftSpeed);
    			break;
    		default: //return to driver control
    			armLifty.set(-drivePad.getThrottle());//return to driver
    			break;
    	}
    	System.out.println("HERE IN MOAT");
    }
    public void rampart(){
    	//TODO
    	int rampartSwitch = 0;
    	switch(rampartSwitch){
    		case 1: //raise the front wheels
    			armLifty.set(autoLiftSpeed);
    			break;
    		case 2: //lower the wheels
    			armLifty.set(-autoLiftSpeed);
    			break;
    		default: //return to driver control
    			armLifty.set(-drivePad.getThrottle());//return to driver
    			break;
    	}
    	System.out.println("HERE IN RAMPART");
    }
    public void drawBridge(){
    	//TODO
    	int drawSwitch = 0;
    	switch(drawSwitch){
    		case 1: //raise the front wheels
    			armLifty.set(autoLiftSpeed);
    			break;
    		case 2: //lower the front wheels
    			armLifty.set(-autoLiftSpeed);
    			break;
    		default: //return to driver control
    			armLifty.set(-drivePad.getThrottle());;//return to driver
    			break;
    	}
    	System.out.println("HERE IN DRAWBRIDGE");
    }
    public void sallyPort(){
    	//TODO
    	int drawSwitch = 0;
    	switch(drawSwitch){
    		case 1: //raise the front wheels
    			armLifty.set(autoLiftSpeed);
    			break;
    		case 2: //lower the front wheels
    			armLifty.set(-autoLiftSpeed);
    			break;
    		default: //return to driver control
    			armLifty.set(-drivePad.getThrottle());;//return to driver
    			break;
    	}
    	System.out.println("HERE IN SALLYPORT");
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
    public void rockWall(){
    	//TODO
    	int drawSwitch = 0;
    	switch(drawSwitch){
    		case 1: //raise the front wheels
    			armLifty.set(autoLiftSpeed);
    			break;
    		case 2: //lower the front wheels
    			armLifty.set(-autoLiftSpeed);
    			break;
    		default: //return to driver control
    			armLifty.set(-drivePad.getThrottle());;//return to driver
    			break;
    	}
    	System.out.println("HERE IN ROCKWALL");
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
    			if (statesPad.getRawButton(actionButton) || drivePad.getRawButton(actionButton)){
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
     * DEBUG CODE
     */
    public void debug(){
		//System.out.println("ARM_LIFT		=		" + armLifty.get());
		//System.out.println("ARM_POSITION	=		" + armPosition.getAverageVoltage());
    	//System.out.println("ARM_STOP		=   	" + armStopBottom.get());
    	//System.out.println("DRIVE_SPEED		=   	" + drivePad.getY());
    }
	public void smartDashboardVariables(){
    	SmartDashboard.putNumber("joystick values", drivePad.getY());
    	SmartDashboard.putNumber("leftSide", leftSide.get());
    	SmartDashboard.putNumber("rightSide", rightSide.get());
    	SmartDashboard.putNumber("ARM LIFT", armLifty.get());
    	SmartDashboard.putNumber("ARM POSITION", armPosition.getAverageVoltage());
    	SmartDashboard.putNumber("ARM WHEELSPEED", spinWheels.get());
    }
	public void liveWindowVariables(){
		
	}
}