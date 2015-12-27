/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team4213.daisylts.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Timer;
import org.team4213.lib.Loopable;

/**
 *
 * @author MetalCow Robotics
 */
public class Catapult implements Loopable{
    public static final int ENGAGING = 0;
    public static final int WINCHING = 1;
    public static final int WINCHED = 2;
    public static final int FIRING = 3;
    public static final int FIRED = 4;
    
    private int state = FIRED;
    
    private final DoubleSolenoid dog = new DoubleSolenoid(7,8);
    private final SpeedController winch = new Jaguar(8);
    private final DigitalInput limitSwitch = new DigitalInput(6);
    
    private Timer engagementTimer = null;
    private Timer winchingTimer = null;
    private Timer firingTimer = null;
    
    public void fire() {
        if (this.state != FIRED)
            this.state = FIRING;
    }
    
    public void winch() {
        if (this.state == FIRED)
            this.state = ENGAGING;
    }
    
    public void toggle() {
        switch(this.state) {
            case ENGAGING:
            case WINCHING:
            case WINCHED:
                this.state = FIRING;
                break;
            case FIRING:
                // Don't allow switching mode while catapult still going up
                break;
            case FIRED:
                this.state = ENGAGING;
                break;
        }
    }
    
    public void loop(boolean isTele, boolean isAuto) {
        if (isTele || isAuto) {
            switch(this.state) {
                case ENGAGING:
                    // Deallocate timers used by other states
                    winchingTimer = null;
                    firingTimer = null;
                    // Create engagementTimer if we don't already have it
                    if (engagementTimer == null) {
                        engagementTimer = new Timer();
                        engagementTimer.reset();
                        engagementTimer.start();
                    }
                    
                    if (engagementTimer.get()>0.65) {
                        // If we've spent enough time engaging, move on to winching.
                        // This is done since there isn't a break; statement.
                        this.state = WINCHING;
                    } else {
                        // If we haven't spent enough time engaging, set output values for it.
                        dog.set(DoubleSolenoid.Value.kReverse);
                        winch.set(0);
                        break;
                    }
                    
                case WINCHING:
                    // Deallocate
                    engagementTimer = null;
                    firingTimer = null;
                    // Create winchingTimer
                    if (winchingTimer == null) {
                        winchingTimer = new Timer();
                        winchingTimer.reset();
                        winchingTimer.start();
                    }
                    
                    if (winchingTimer.get()>3.5 || !limitSwitch.get()) {
                        // If enough time spent on winching, or limit switch is hit (remember, input readings are inverted for pull-ups), move onto winched state.
                        // This is done since there isn't a break; statement.
                        this.state = WINCHED;
                    } else {
                        dog.set(DoubleSolenoid.Value.kReverse);
                        winch.set(-0.75);
                        break;
                    }
                case WINCHED:
                    // Placeholder state. We stay at this winched state until told to advance by the user...
                    winchingTimer = null;
                    engagementTimer = null;
                    firingTimer = null;
                    dog.set(DoubleSolenoid.Value.kReverse);
                    winch.set(0);
                    break;
                case FIRING:
                    // FIRE!!!
                    engagementTimer = null;
                    winchingTimer = null;
                    
                    if (firingTimer == null) {
                        firingTimer = new Timer();
                        firingTimer.reset();
                        firingTimer.start();
                    }
                    
                    if (firingTimer.get()>1.3) {
                        // If we've spent enough time firing, move on to fired.
                        // This is done since there isn't a break; statement.
                        this.state = FIRED;
                    } else {
                        // If we haven't spent enough time engaging, set output values for it.
                        dog.set(DoubleSolenoid.Value.kForward);
                        winch.set(0);
                        break;
                    }
                case FIRED:
                    // Another placeholder state. We stay at this fired state until told to advance by the user...
                    winchingTimer = null;
                    engagementTimer = null;
                    firingTimer = null;
                    dog.set(DoubleSolenoid.Value.kForward);
                    winch.set(0);
                    break;
            }
        } else {
            switch(this.state) {
                case FIRED:
                case FIRING:
                case ENGAGING:
                    // If already set to fire, or just recently engaged, when the robot is disabled, we want to cancel that intent when teleoperated comes back up.
                    winchingTimer = null;
                    engagementTimer = null;
                    firingTimer = null;
                    dog.set(DoubleSolenoid.Value.kForward);
                    winch.set(0);
                    state = FIRED;
                    break;
                case WINCHED:
                case WINCHING:
                    // If we're winching or already winched down, consider the robot fully winched. That way the timer avoids doing weird things.
                    winchingTimer = null;
                    engagementTimer = null;
                    firingTimer = null;
                    dog.set(DoubleSolenoid.Value.kReverse);
                    winch.set(0);
                    state = WINCHED;
                    break;
                
                    
            }
        }
    }
    
}
