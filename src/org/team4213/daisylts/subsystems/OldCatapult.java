package org.team4213.daisylts.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Timer;
import org.team4213.lib.Loopable;

/**
 *
 * @author Thaddeus
 */
public class OldCatapult implements Loopable{
    private Jaguar winch = new Jaguar(7);
    private DigitalInput winchSwitch = new DigitalInput(1);
    private DoubleSolenoid dog = new DoubleSolenoid(4,5);
    private Timer winchTimer = null;
    
    boolean setToFired = true;
    
    public static double winchDownTime = 2;
    
    public void toggleState() {
        if (setToFired) {
            //winchTimer = null;
            setToFired = false;
        } else {
            //winchTimer = null;
            setToFired = true;
        }
    }
    
    public void fire() {
        //winchTimer = null;
        setToFired = true; 
    }
    
    public void winch() {
        //winchTimer = null;
        setToFired = false;
    }
    
    public void loop(boolean isTele, boolean isAuto) {
        if (setToFired) {
            dog.set(DoubleSolenoid.Value.kForward);
            winchTimer = null;
        } else {
            dog.set(DoubleSolenoid.Value.kReverse);
            if (winchTimer==null) {
                winchTimer = new Timer();
                winchTimer.start();
            }
            if (winchTimer.get()<winchDownTime && !winchSwitch.get()) {
                winch.set(0.9);
            } else {
                winch.set(0);
            }
        }
    }
}
