package org.firstinspires.ftc.teamcode;

/**
 * 
 */
public class PIDmotor
{
    // Fields 
    
    // The value of the P constant.
    private double P;
    // The value of the I constant.
    private double I;
    // The value of the D constant.
    private double D;
    
    // These variables look kind of similar to the previous three, but these are used for the actual values that will be added
    // to the pid value in the calculate function.

    // The P value
    private double uP;

    // The I value
    private double uI;

    // The D value
    private double yD;

    // This is the desire value. This will be based on the motor's function.
    // For example, for a more precise motor action like the function of a servo this value could be a position.
    // For the drive chassis it might be better to make this value the spinning rate of the motor
    private double deriredValue = 0;
    
    //This it the max I output.
    //A max I ouput is defined because too much power given by the I value will cause some windup and overshoots.
    private double maxIOutput = 0;

    // This is the max output that pid value can be, in this case it is the highest power of the motor.
    private double max_output = 0;

    // The lower value which the pid value can be, int this case it is the lowest power of the motor.
    private double min_output = 0;

    private double input = 0;
    
    // This is the error value. Calculated by subtracting the desired value by the actualy value. 
    private double error = 0;

    // This is the error sum, the sum of all previous errors.
    private double errorSum = 0;

    //private double previousError = 0;
    
    // Not quite sure what this if for yet
    private double threshold = 0;

    // This is true if this is there has been no error before.
    private boolean first;

    // A sign value which can reverse the pid value  
    private int sign = 1;


    //Constructor
    /**
     * This a three parameter constructor that 
     * @param kP
     * @param kI
     * @param kD
     */
    public PIDmotor(double kP, double kI, double kD)
    {
        P = kP;
        I = kI;
        D = kD;
        first = true;
    }

    public void setTarget(double target)
    {
        desiredValue = target;
    }

    public void setMaxOutputs(int min_output, int max_output)
    {
        this.min_output = min_output;
        this.max_output = max_output;
    }

    public void setMaxMinIValues(int minIOutput, int maxIOutput)
    {
        this.minIOutput = minIOutput;
        this.maxIOutput = maxIOutput;
    }

    public void rezeroErrors(){
        previousError = 0;
        totalError = 0;
    }


    //public boolean withinThreshold(){
    //   return (Math.abs(error) < Math.abs(threshold * (max_input-min_input)));
    //}

    public double calculate(double currentVal)
    {
        error = desiredValue - currentVal;
        
        uP = P * error;
        
        if(first)
        {
            previousError = error;
        }

        uD = P * (error - previousError);
        previousError = error;
        
        errorSum += error;

        uI = I * errorSum;
        if(uI > maxIOutput)
        {
            uI = maxIOutput;
        }

        double pidValue = uP + uI + uD;
        
        if(Math.abs(pidValue) > maxOutput)
        {
            pidValue = maxOutput;
        }
        else if(Math.abs(pidValue) < minOutput)
        {
            pidValue = minOutput;
        }

        return pidValue;
    }
}