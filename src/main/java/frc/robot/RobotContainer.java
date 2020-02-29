/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
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
import edu.wpi.first.vision.VisionThread;
import edu.wpi.first.wpilibj.GenericHID;

//**************************************************************

//**************************************************************
// Human Interface Devices (HID) Imports

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
//
import edu.wpi.first.wpilibj2.command.PerpetualCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

// *************************************************************

// *************************************************************
import frc.robot.Constants.DStationConstants;
import frc.robot.Constants.VisConstants;
// ***********************************************************
// Commands and Subsystems
import frc.robot.commands.ExampleCommand;
import frc.robot.subsystems.ExampleSubsystem;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();

  private final ExampleCommand m_autoCommand = new ExampleCommand(m_exampleSubsystem);
  
  UsbCamera targetCam = CameraServer.getInstance().startAutomaticCapture(0);

    Object imgLock = new Object();
    public int count = 0;
    public int pipeCount = 0;
    public double centerX = 0;

  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the button bindings
    
    
    
      
    targetCam.setVideoMode(VideoMode.PixelFormat.kMJPEG,
                          VisConstants.targetCameraFrameWidth,
                          VisConstants.targetCameraFrameHeight,
                          VisConstants.targetCameraFPS);
  
    CvSource outputStream = CameraServer.getInstance().putVideo("Processed in Main", VisConstants.targetCameraFrameWidth, VisConstants.targetCameraFrameHeight);
    
    new VisionThread(targetCam, new StripPipeline(), stripPipeline -> {
                            
      SmartDashboard.putNumber("Number of Contours Found", stripPipeline.findContoursOutput().size());
                                     
      if (stripPipeline.filterContoursOutput().isEmpty())
        {SmartDashboard.putString("Filterd Contour Status:", "No Contours Found");
      };

      
      if (!stripPipeline.filterContoursOutput().isEmpty()) {
        
        SmartDashboard.putNumber("Number of Contours Found", stripPipeline.filterContoursOutput().size());

          Rect r = Imgproc.boundingRect(stripPipeline.filterContoursOutput().get(0));
          synchronized (imgLock) {
              centerX = r.x + (r.width / 2);

              SmartDashboard.putNumber("Center X from Subsys VisionThread", centerX);

            
          }
          outputStream.putFrame(stripPipeline.cvAbsdiffOutput);
      }
      
    });
                        
    
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {

  


  }


  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return m_autoCommand;
  }
}
