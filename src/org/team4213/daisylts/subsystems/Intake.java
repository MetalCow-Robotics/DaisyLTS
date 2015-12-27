/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team4213.daisylts.subsystems;

import edu.wpi.first.wpilibj.Jaguar;
import org.team4213.lib.Loopable;

/**
 *
 * @author Thaddeus
 */
public class Intake implements Loopable{
    private Jaguar motor = new Jaguar(9);
    private double power = 0;
    
    public void setIn() {
        power = -0.9;
    }
    
    public void setOut() {
        power = 1;
    }
    
    public void stop() {
        power = 0;
    }
    
    public void loop(boolean t, boolean a) {
        motor.set(power);
    }
}
