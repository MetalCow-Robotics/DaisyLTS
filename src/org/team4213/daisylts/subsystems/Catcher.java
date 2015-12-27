/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team4213.daisylts.subsystems;

import edu.wpi.first.wpilibj.Relay;
import org.team4213.lib.Loopable;

/**
 *
 * @author Thaddeus
 */
public class Catcher implements Loopable{
    private Relay motor = new Relay(1);
    private Relay.Value power = Relay.Value.kOff;
    
    public void setIn() {
        power = Relay.Value.kForward;
    }
    
    public void setOut() {
        power = Relay.Value.kReverse;
    }
    
    public void stop() {
        power = Relay.Value.kOff;
    }
    
    public void loop(boolean t, boolean a) {
        motor.set(power);
    }
}