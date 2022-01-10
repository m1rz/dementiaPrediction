/*
 * SimpleT1FLS.java
 *
 * Created on May 20th 2012
 *
 * Copyright 2012 Christian Wagner All Rights Reserved.
 */
package examples;

import generic.Input;
import generic.Output;
import generic.Tuple;
import tools.JMathPlotter;
import type1.sets.T1MF_Gauangle;
import type1.sets.T1MF_Interface;
import type1.sets.T1MF_Triangular;
import type1.system.T1_Antecedent;
import type1.system.T1_Consequent;
import type1.system.T1_Rule;
import type1.system.T1_Rulebase;

/**
 * A simple example of a type-1 FLS based on the "How much to tip the waiter"
 *  scenario.
 * We have two inputs: food quality and service level and as an output we would
 * like to generate the applicable tip.
 * @author Christian Wagner
 */
public class DementiaPrediction 
{
    Input mmse, age, cdr, mrdelay;    //the inputs to the FLS
    Output dementia;             //the output of the FLS
    T1_Rulebase rulebase;   //the rulebase captures the entire FLS
    
    public DementiaPrediction()
    {
        //Define the inputs
        mmse = new Input("Mini Mental State Examination", new Tuple(0, 30));
        age = new Input("Age", new Tuple(0, 100));
        cdr = new Input("Clinical Dementia Ratio", new Tuple(0, 3));
        mrdelay = new Input("MR Delay", new Tuple(0, 2639));
        dementia = new Output("Dementia State", new Tuple(0, 10));
        // food = new Input("Food Quality", new Tuple(0,10));      //a rating given by a person between 0 and 10
        // service = new Input("Service Level", new Tuple(0,10));  //a rating given by a person between 0 and 10
        // tip = new Output("Tip", new Tuple(0,30));               //a percentage for the tip

        // Set up the inputs
        // MMSE
        T1MF_Triangular severeMMSE_MF = new T1MF_Triangular("MF for severe impairment MMSE score", 0.0, 0.0, 17.0);
        T1MF_Triangular mildMMSE_MF = new T1MF_Triangular("MF for mild impirment MMSE score", 18.0, 20.5, 23.0);
        T1MF_Triangular noneMMSE_MF = new T1MF_Triangular("MF for no impairment MMSE score", 24.0, 30.0, 30.0);
        
        // Age
        T1MF_Gauangle youngAge_MF = new T1MF_Gauangle("Young age", 0.0, 0.0, 24.0);
        T1MF_Gauangle middleAge_MF = new T1MF_Gauangle("Middle-aged", 24.0, 44.0, 64.0);
        T1MF_Gauangle oldAge_MF = new T1MF_Gauangle("Old age", 64.0, 100.0, 100.0);

        // CDR
        T1MF_Triangular noCDR_MF = new T1MF_Triangular("no AD", 0.0, 0.0, 0.5);
        T1MF_Triangular veryMildCDR_MF = new T1MF_Triangular("very mild AD", 0.0, 0.5, 1.0);
        T1MF_Triangular mildCDR_MF = new T1MF_Triangular("mild AD", 0.5, 1.0, 2.0);
        T1MF_Triangular moderateCDR_MF = new T1MF_Triangular("moderate AD", 1.0, 2.0, 3.0);
        T1MF_Triangular severeCDR_MF = new T1MF_Triangular("severe AD", 2.0, 3.0, 3.0);

        // MRI delay
        T1MF_Gauangle shortDelay_MF = new T1MF_Gauangle("Short Delay", 0.0,0.0, 675.9);
        T1MF_Gauangle moderateDelay_MF = new T1MF_Gauangle("Moderate Delay", 676.00, 1319.1, 1456.9);
        T1MF_Gauangle longDelay_MF = new T1MF_Gauangle("Long Delay", 1457.0, 2639, 2639);

        // Dementia - Class label
        T1MF_Gauangle nonDemented_MF = new T1MF_Gauangle("No dementia", 0.0, 2.0, 4.0);
        T1MF_Gauangle converted_MF = new T1MF_Gauangle("Recovered from dementia", 3.0, 5.0, 7.0);
        T1MF_Gauangle demented_MF = new T1MF_Gauangle("Demented", 6.0, 8.0, 10.0);

        // //Set up the antecedents and consequents
        T1_Antecedent severeMMSE = new T1_Antecedent("SevereImpairment", severeMMSE_MF, mmse);
        T1_Antecedent mildMMSE = new T1_Antecedent("MildImpairment", mildMMSE_MF, mmse);
        T1_Antecedent noneMMSE = new T1_Antecedent("NoImpairment", noneMMSE_MF, mmse);

        T1_Antecedent youngAge = new T1_Antecedent("youngAge", youngAge_MF, age);
        T1_Antecedent middleAge = new T1_Antecedent("middleAge", middleAge_MF, age);
        T1_Antecedent oldAge = new T1_Antecedent("oldAge", oldAge_MF, age);

        T1_Antecedent noCDR = new T1_Antecedent("noAD", noCDR_MF, cdr);
        T1_Antecedent veryMildCDR = new T1_Antecedent("veryMildAD", veryMildCDR_MF, cdr);
        T1_Antecedent mildCDR = new T1_Antecedent("mildAD", mildCDR_MF, cdr);
        T1_Antecedent moderateCDR = new T1_Antecedent("moderateAD", moderateCDR_MF, cdr);
        T1_Antecedent severeCDR = new T1_Antecedent("severeAD", severeCDR_MF, cdr);

        T1_Antecedent shortDelay = new T1_Antecedent("Short MR Delay", shortDelay_MF, mrdelay);
        T1_Antecedent moderateDelay = new T1_Antecedent("Moderate MR Delay", moderateDelay_MF, mrdelay);
        T1_Antecedent longDelay = new T1_Antecedent("Long MR Delay", longDelay_MF, mrdelay);

        T1_Consequent demented = new T1_Consequent("Demented", demented_MF, dementia);
        T1_Consequent converted = new T1_Consequent("Recovered from dementia", converted_MF, dementia);
        T1_Consequent nonDemented = new T1_Consequent("No dementia", nonDemented_MF, dementia);

        // T1_Consequent lowTip = new T1_Consequent("LowTip", lowTipMF, tip);
        // T1_Consequent mediumTip = new T1_Consequent("MediumTip", mediumTipMF, tip);
        // T1_Consequent highTip = new T1_Consequent("HighTip", highTipMF, tip);

        // //Set up the rulebase and add rules
        rulebase = new T1_Rulebase(49);

        // Rule 1
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{noCDR}, nonDemented));

        // Rule 2
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{veryMildCDR, shortDelay}, demented));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{mildCDR, shortDelay}, demented));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{moderateCDR, shortDelay}, demented));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{severeCDR, shortDelay}, demented));

        // Rule 3
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{veryMildCDR, moderateDelay, severeMMSE}, demented));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{mildCDR, moderateDelay, severeMMSE}, demented));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{moderateCDR, moderateDelay, severeMMSE}, demented));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{severeCDR, moderateDelay, severeMMSE}, demented));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{veryMildCDR, moderateDelay, mildMMSE}, demented));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{mildCDR, moderateDelay, mildMMSE}, demented));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{moderateCDR, moderateDelay, mildMMSE}, demented));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{severeCDR, moderateDelay, mildMMSE}, demented));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{veryMildCDR, longDelay, severeMMSE}, demented));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{mildCDR, longDelay, severeMMSE}, demented));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{moderateCDR, longDelay, severeMMSE}, demented));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{severeCDR, longDelay, severeMMSE}, demented));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{veryMildCDR, longDelay, mildMMSE}, demented));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{mildCDR, longDelay, mildMMSE}, demented));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{moderateCDR, longDelay, mildMMSE}, demented));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{severeCDR, longDelay, mildMMSE}, demented));

        // RULE 4: IF CDR > 0 AND MR DELAY > 675 AND MMSE > 28 AND MR DELAY > 1456 THEN CONVERTED
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{veryMildCDR, longDelay, noneMMSE}, converted));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{mildCDR, longDelay, noneMMSE}, converted));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{moderateCDR, longDelay, noneMMSE}, converted));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{severeCDR, longDelay, noneMMSE}, converted));

        // RULE 5: IF CDR > 0 AND MR DELAY > 675 AND MMSE <= 28 AND MR DELAY <= 1456 AND AGE > 80 THEN DEMENTED
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{veryMildCDR, moderateDelay, severeMMSE,oldAge}, demented));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{mildCDR, moderateDelay, severeMMSE,oldAge}, demented));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{moderateCDR, moderateDelay, severeMMSE,oldAge}, demented));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{severeCDR, moderateDelay, severeMMSE,oldAge}, demented));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{veryMildCDR, moderateDelay, mildMMSE,oldAge}, demented));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{mildCDR, moderateDelay, mildMMSE,oldAge}, demented));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{moderateCDR, moderateDelay, mildMMSE,oldAge}, demented));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{severeCDR, moderateDelay, mildMMSE,oldAge}, demented));
        
        // RULE 6: IF CDR > 0 AND MR DELAY > 675 AND MMSE <= 28 AND MR DELAY <= 1456 AND AGE <= 80 THEN DEMENTED 
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{veryMildCDR, moderateDelay, severeMMSE, middleAge}, demented));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{mildCDR, moderateDelay, severeMMSE, middleAge}, demented));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{moderateCDR, moderateDelay, severeMMSE, middleAge}, demented));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{severeCDR, moderateDelay, severeMMSE, middleAge}, demented));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{veryMildCDR, moderateDelay, mildMMSE, middleAge}, demented));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{mildCDR, moderateDelay, mildMMSE, middleAge}, demented));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{moderateCDR, moderateDelay, mildMMSE, middleAge}, demented));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{severeCDR, moderateDelay, mildMMSE, middleAge}, demented));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{veryMildCDR, moderateDelay, severeMMSE, youngAge}, demented));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{mildCDR, moderateDelay, severeMMSE, youngAge}, demented));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{moderateCDR, moderateDelay, severeMMSE, youngAge}, demented));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{severeCDR, moderateDelay, severeMMSE, youngAge}, demented));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{veryMildCDR, moderateDelay, mildMMSE, youngAge}, demented));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{mildCDR, moderateDelay, mildMMSE, youngAge}, demented));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{moderateCDR, moderateDelay, mildMMSE, youngAge}, demented));
        rulebase.addRule(new T1_Rule(new T1_Antecedent[]{severeCDR, moderateDelay, mildMMSE, youngAge}, demented));

        
        // //just an example of setting the discretisation level of an output - the usual level is 100
        // tip.setDiscretisationLevel(50);        
        
        // //get some outputs
        // getTip(7,8);
        
        // //plot some sets, discretizing each input into 100 steps.
        plotMFs("Age", new T1MF_Interface[]{youngAge_MF, middleAge_MF, oldAge_MF}, age.getDomain(), 100);
        plotMFs("MMSE Score", new T1MF_Interface[]{severeMMSE_MF, mildMMSE_MF, noneMMSE_MF}, mmse.getDomain(), 100);
        plotMFs("Clinical Dementia Ratio", new T1MF_Interface[]{noCDR_MF, veryMildCDR_MF, mildCDR_MF, moderateCDR_MF, severeCDR_MF}, cdr.getDomain(), 100);
        plotMFs("MR Delay", new T1MF_Interface[]{shortDelay_MF, moderateDelay_MF, longDelay_MF}, mrdelay.getDomain(), 100);
        // plotMFs("Food Quality Membership Functions", new T1MF_Interface[]{badFoodMF, greatFoodMF}, food.getDomain(), 100); 
        // plotMFs("Service Level Membership Functions", new T1MF_Interface[]{unfriendlyServiceMF, okServiceMF, friendlyServiceMF}, service.getDomain(), 100);
        // plotMFs("Level of Tip Membership Functions", new T1MF_Interface[]{lowTipMF, mediumTipMF, highTipMF}, tip.getDomain(), 100);
       
        // //plot control surface
        // //do either height defuzzification (false) or centroid d. (true)
        // plotControlSurface(true, 100, 100);
        
        // //print out the rules
        // System.out.println("\n"+rulebase);        
    }
    
    /**
     * Basic method that prints the output for a given set of inputs.
     * @param foodQuality
     * @param serviceLevel 
     */
    private void getPrediction(double Age, double MMSEScore, double CDRScore, double MRDelay)
    {
        //first, set the inputs
        age.setInput(Age);
        mmse.setInput(MMSEScore);
        cdr.setInput(CDRScore);
        mrdelay.setInput(MRDelay);
        //now execute the FLS and print output
        System.out.println("The age was: "+age.getInput());
        System.out.println("The MMSE score was: "+mmse.getInput());
        System.out.println("The CDR score was: "+cdr.getInput());
        System.out.println("The MR delay was: "+mrdelay.getInput());
        System.out.println("Using height defuzzification, the FLS recommends a prediction of"
                +rulebase.evaluate(0).get(dementia)); 
        System.out.println("Using centroid defuzzification, the FLS recommends a prediction of"
                +rulebase.evaluate(1).get(dementia));     
    }
    
    private void plotMFs(String name, T1MF_Interface[] sets, Tuple xAxisRange, int discretizationLevel)
    {
        JMathPlotter plotter = new JMathPlotter(17,17,15);
        for (int i=0;i<sets.length;i++)
        {
            plotter.plotMF(sets[i].getName(), sets[i], discretizationLevel, xAxisRange, new Tuple(0.0,1.0), false);
        }
        plotter.show(name);
    }

    /* private void plotControlSurface(boolean useCentroidDefuzzification, int input1Discs, int input2Discs)
    {
        double output;
        double[] x = new double[input1Discs];
        double[] y = new double[input2Discs];
        double[][] z = new double[y.length][x.length];
        double incrX, incrY;
        incrX = food.getDomain().getSize()/(input1Discs-1.0);
        incrY = service.getDomain().getSize()/(input2Discs-1.0);

        //first, get the values
        for(int currentX=0; currentX<input1Discs; currentX++)
        {
            x[currentX] = currentX * incrX;        
        }
        for(int currentY=0; currentY<input2Discs; currentY++)
        {
            y[currentY] = currentY * incrY;
        }
        
        for(int currentX=0; currentX<input1Discs; currentX++)
        {
            food.setInput(x[currentX]);
            for(int currentY=0; currentY<input2Discs; currentY++)
            {
                service.setInput(y[currentY]);
                if(useCentroidDefuzzification)
                    output = rulebase.evaluate(1).get(dementia);
                else
                    output = rulebase.evaluate(0).get(dementia);
                z[currentY][currentX] = output;
            }    
        }
        
        //now do the plotting
        JMathPlotter plotter = new JMathPlotter(17, 17, 14);
        plotter.plotControlSurface("Control Surface",
                new String[]{food.getName(), service.getName(), "Tip"}, x, y, z, new Tuple(0.0,30.0), true);   
       plotter.show("Type-1 Fuzzy Logic System Control Surface for Tipping Example");
    } */
    
    public static void main (String args[])
    {
        new DementiaPrediction();
    }
}
