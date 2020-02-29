/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.vision.VisionThread;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Constants.VisConstants;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;

  NetworkTable table;
  double[] areas;
  double[] defaultValue = new double[0];
  
  UsbCamera targetCam = CameraServer.getInstance().startAutomaticCapture(0);

  Object imgLock = new Object();
  public int count = 0;
  public int pipeCount = 0;
  public double centerX = 0;
  public CvSource outputStream;
  public StripPipeline p;
  public VisionThread v;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();

    table = NetworkTableInstance.getDefault().getTable("GRIP/mycontoursReport");

    targetCam.setVideoMode(VideoMode.PixelFormat.kMJPEG,
                          VisConstants.targetCameraFrameWidth,
                          VisConstants.targetCameraFrameHeight,
                          VisConstants.targetCameraFPS);
  
    outputStream = CameraServer.getInstance().putVideo("Processed in Main", VisConstants.targetCameraFrameWidth, VisConstants.targetCameraFrameHeight);

    v = new VisionThread(targetCam, new StripPipeline(), stripPipeline -> {
                            
      p = stripPipeline; 
      System.out.println("Vision thread is fucking working");          
      if (stripPipeline.filterContoursOutput().isEmpty())
        {SmartDashboard.putString("Filterd Contour Status:", "No Contours Found");
      };

      
      if (!stripPipeline.filterContoursOutput().isEmpty()) {
        

          Rect r = Imgproc.boundingRect(stripPipeline.filterContoursOutput().get(0));
          synchronized (imgLock) {
              centerX = r.x + (r.width / 2);

              SmartDashboard.putNumber("Center X from Subsys VisionThread", centerX);

            
          }
          outputStream.putFrame(stripPipeline.cvAbsdiffOutput);
      }
      
    });
    v.start();
    
  }
    
  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
    
    /*double[] areas = table.getEntry("area").getDoubleArray(defaultValue);

    System.out.print("areas: " );

    for (double area : areas) {
      System.out.print(area + " ");
    }

    System.out.println();*/




    
    //SmartDashboard.putNumber("Number of Contours Found", stripPipeline.findContoursOutput().size());
    //SmartDashboard.putNumber("Number of Contours Found", stripPipeline.filterContoursOutput().size());

  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   */
  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
  }

  /**
   * This autonomous runs the autonomous command selected by your {@link RobotContainer} class.
   */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
