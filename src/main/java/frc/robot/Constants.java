/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants.  This class should not be used for any other purpose.  All constants should be
 * declared globally (i.e. public static).  Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

    // group constants by classes for easier use

    public static final class DriveConstants {
        public static final int leftMotor1Port = 1;
        public static final int leftMotor2Port = 2;
        public static final int leftMotor3Port = 3;
        public static final int rightMotor1Port = 4;
        public static final int rightMotor2Port = 5;
        public static final int rightMotor3Port = 6;

    }

    public static final class VisConstants {
        public static final int targetCameraFrameWidth = 1280;
        public static final int targetCameraFrameHeight = 720;
        public static final int targetCameraFPS = 30;
        public static final double allowableTargetError = 10;
        public static final double chaseSpeed = 0.25;

    }

    public static final class DStationConstants {
        public static final int leftJoystickUSBPort = 0;
        
    }


}
