/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.team4213.daisylts;


import edu.wpi.first.wpilibj.Compressor;
import org.team4213.daisylts.subsystems.Catapult;
import org.team4213.daisylts.subsystems.Catcher;
import org.team4213.daisylts.subsystems.Intake;
import org.team4213.daisylts.subsystems.MecanumDrive;
import org.team4213.lib.AIRFLOController;
import org.team4213.lib.MCRIterativeRobot;

    public class DaisyLTS extends MCRIterativeRobot {
    MecanumDrive drivetrain = new MecanumDrive();
    Catapult catapult = new Catapult();
    Catcher catcher = new Catcher();
    Intake intake = new Intake();
    
    AIRFLOController controller = new AIRFLOController(1);
    Compressor compressor = new Compressor(8,8);
    
    public DaisyLTS() {
        this.addLoopable(drivetrain);
        this.addLoopable(catapult);
        this.addLoopable(intake);
        this.addLoopable(catcher);
    }
    
    public void robotInit() {
        compressor.start();
    }
    
    public void teleopPeriodic() {
        /*if (controller.getButton(1))
            catapult.fire();
        else if (controller.getButton(2))
            catapult.winch();*/
        if (controller.getButtonTripped(1))
            catapult.toggle();
        
        drivetrain.driveXYW(controller.getLX(),controller.getLY(),controller.getRX(),controller.getThrottle(),true);
    
        if (controller.getButton(5))
            intake.setIn();
        else if (controller.getButton(6))
            intake.setOut();
        else
            intake.stop();
        
        if (controller.getButton(3))
            catcher.setIn();
        else if (controller.getButton(4))
            catcher.setOut();
        else
            catcher.stop();
            
    }
}
