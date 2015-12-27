/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.team4213.daisylts.subsystems;

import edu.wpi.first.wpilibj.Jaguar;
import java.util.Enumeration;
import java.util.Hashtable;
import org.team4213.lib.Loopable;

public class MecanumDrive implements Loopable{
    double currentXPower=0;
    double currentYPower=0;
    double currentWPower=0;
    double currentThrottle=0;
    
    Hashtable motors = new Hashtable();
    
    public MecanumDrive() {
        motors.put("fl", new Jaguar(5));
        motors.put("fr", new Jaguar(3));
        motors.put("rl", new Jaguar(1));
        motors.put("rr", new Jaguar(4));
    }
    
    public void driveXYW(double x, double y, double w, double throttle, boolean throttleW) {
        currentXPower = x*throttle;
        currentYPower = y*throttle;
        currentWPower = w*(throttleW ? throttle : 1);
        currentThrottle = throttle;
    }
    
    public void loop(boolean isTeleop, boolean isAuton) {
        if (isTeleop) {
 
        } else if (isAuton) {
            currentYPower=0;
            currentWPower=0;
            currentXPower=0;
            currentThrottle=0;
        } else {
            currentYPower=0;
            currentWPower=0;
            currentXPower=0;
            currentThrottle=0;
        }
        Hashtable powers = new Hashtable();
        powers.put("fl", Double.valueOf( (currentYPower+currentXPower+currentWPower)));
        powers.put("fr", Double.valueOf(-(currentYPower-currentXPower-currentWPower)));
        powers.put("rl", Double.valueOf( (currentYPower-currentXPower+currentWPower)));
        powers.put("rr", Double.valueOf(-(currentYPower+currentXPower-currentWPower)));
        double max = 0;
        Enumeration e = powers.keys();
        while(e.hasMoreElements()) {
            double cur = Math.abs(((Double)powers.get(e.nextElement())).doubleValue());
            if (cur>max) 
                max = cur;
        }
        
        if (max>currentThrottle) {
            e = motors.keys();
            while(e.hasMoreElements()) {
                Object key = e.nextElement();

                ((Jaguar)motors.get(key)).set(((Double)powers.get(key)).doubleValue()*currentThrottle/max);
            }
        } else {
            e = motors.keys();
            while(e.hasMoreElements()) {
                Object key = e.nextElement();

                ((Jaguar)motors.get(key)).set(((Double)powers.get(key)).doubleValue());
            }
        }
    }
}
