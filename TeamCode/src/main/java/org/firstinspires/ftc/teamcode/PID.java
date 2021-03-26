package org.firstinspires.ftc.teamcode;

public class PID {
    private double P;
    private double I;
    private double D;
    private double pointer = 0;
    private double max_input = 0;
    private double min_input = 0;
    private double max_output = 0;
    private double min_output = 0;
    private double input = 0;
    private double error = 0;
    private double totalError = 0;
    private double previousError = 0;
    private double threshold = 0;
    private int sign = 1;


    public PID(double uP, double uI, double uD){
        P = uP;
        I = uI;
        D = uD;
    }

    public double calculate(double current_value){
        //find the error
        error = pointer - current_value;
        if(Math.abs(error) > (max_input - min_input)/2){
            if(error > 0)
                error = error-max_input+min_input;
            else
                error = error + max_input - min_input;
        }
        //do the integration as long as the integrator is in the bounds of the upper and lower bound output
        //i know this doesn't look like an "integral" but remember we're just summing the errors
        if(Math.abs(totalError + error)*I < max_output && Math.abs(totalError + error)*I > min_output)
            totalError += error;
        double pidResult = (P*error) + (I*totalError) + (D*(error - previousError));
        previousError = error;
        if(pidResult < 0)
            sign = -1;

        //last check for the result in bounds
        if(Math.abs(pidResult) > max_output)
            pidResult = max_output*sign;
        else if(Math.abs(pidResult) < min_output)
            pidResult = min_output * sign;
        return pidResult;

    }
    public double performPID(){
        return calculate(input);
    }

    public double performPID(double u_input){
        setInput(u_input);
        return calculate(input);
    }

    public void setInput(double u_input){
        int sign = 1;
        if(max_input > min_input){
            if(u_input<0)
                sign = -1;
            if(Math.abs(u_input) > max_input)
                input = max_input * sign;
            else if(Math.abs(u_input) < min_input)
                input = min_input*sign;
            else
                input = u_input;
        }
        else
            input = u_input;
    }

    public void rezeroErrors(){
        previousError = 0;
        totalError = 0;
    }
    public void setInputLimits(double min, double max){
        max_input = max;
        min_input = min;
    }
    public void setOutputLimits(double min, double max){
        max_output = max;
        min_output = min;
    }

    public void setPoint(double setPoint){
        if(Math.abs(setPoint) > max_input)
            pointer = max_input * sign;
        else if (Math.abs(setPoint) < min_input)
            pointer = min_input * sign;
        else
            pointer = setPoint;
    }

    public void setThreshold(double percentage){
        threshold = percentage;
    }

    public boolean withinThreshold(){
        return (Math.abs(error) < Math.abs(threshold * (max_input-min_input)));
    }


}
