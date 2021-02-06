/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package project2.example;

import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.DynamicBayesianNetwork;
import aima.core.probability.bayes.FiniteNode;
import aima.core.probability.bayes.impl.BayesNet;
import aima.core.probability.bayes.impl.DynamicBayesNet;
import aima.core.probability.bayes.impl.FullCPTNode;
import aima.core.probability.domain.BooleanDomain;
import aima.core.probability.example.ExampleRV;
import aima.core.probability.util.RandVar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Biondi Giuseppe
 */
public class UmbrellaExample {
    public static DynamicBayesianNetwork getExample(){
        FiniteNode prior_rain_tm1 = new FullCPTNode(ExampleRV.RAIN_tm1_RV,
                new double[] { 0.5, 0.5 });
        // tm1, t, t1, t2
        //      u, u1, u2
        BayesNet priorNetwork = new BayesNet(prior_rain_tm1);
        
        // Prior belief state
        FiniteNode rain_tm1 = new FullCPTNode(ExampleRV.RAIN_tm1_RV,
                new double[] { 0.5, 0.5 });
        // Transition Model
        FiniteNode rain_t = new FullCPTNode(ExampleRV.RAIN_t_RV, new double[] {
            // R_t-1 = true, R_t = true
            0.7,
            // R_t-1 = true, R_t = false
            0.3,
            // R_t-1 = false, R_t = true
            0.3,
            // R_t-1 = false, R_t = false
            0.7 }, rain_tm1);
        // Sensor Model
        @SuppressWarnings("unused")
        FiniteNode umbrealla_t = new FullCPTNode(ExampleRV.UMBREALLA_t_RV,
                new double[] {
                    // R_t = true, U_t = true
                    0.9,
                    // R_t = true, U_t = false
                    0.1,
                    // R_t = false, U_t = true
                    0.2,
                    // R_t = false, U_t = false
                    0.8 }, rain_t);
        
        Map<RandomVariable, RandomVariable> X_0_to_X_1 = new HashMap<RandomVariable, RandomVariable>();
        Map<RandomVariable, RandomVariable> X_1_to_X_0 = new HashMap<RandomVariable, RandomVariable>();
        X_0_to_X_1.put(ExampleRV.RAIN_tm1_RV, ExampleRV.RAIN_t_RV);
        X_1_to_X_0.put(ExampleRV.RAIN_t_RV, ExampleRV.RAIN_tm1_RV);
        Set<RandomVariable> E_1 = new HashSet<RandomVariable>();
        E_1.add(ExampleRV.UMBREALLA_t_RV);
        // Transition Model
        
        RandVar RAIN_t1_RV = new RandVar("Rain_t1",new BooleanDomain());
        FiniteNode rain_t1 = new FullCPTNode(RAIN_t1_RV, new double[] {
            // R_t-1 = true, R_t = true
            0.7,
            // R_t-1 = true, R_t = false
            0.3,
            // R_t-1 = false, R_t = true
            0.3,
            // R_t-1 = false, R_t = false
            0.7 }, rain_t);
        // Sensor Model
        @SuppressWarnings("unused")
        RandVar UMBREALLA_t1_RV = new RandVar("UMBREALLA_t1",new BooleanDomain());
        FiniteNode umbrealla_t1 = new FullCPTNode(UMBREALLA_t1_RV,
                new double[] {
                    // R_t = true, U_t = true
                    0.9,
                    // R_t = true, U_t = false
                    0.1,
                    // R_t = false, U_t = true
                    0.2,
                    // R_t = false, U_t = false
                    0.8 }, rain_t1);
        X_0_to_X_1.put(ExampleRV.RAIN_t_RV, RAIN_t1_RV);
        X_1_to_X_0.put(RAIN_t1_RV, ExampleRV.RAIN_t_RV);
        E_1.add(UMBREALLA_t1_RV);
        
        // Transition Model
        RandVar RAIN_t2_RV = new RandVar("Rain_t2",new BooleanDomain());
        FiniteNode rain_t2 = new FullCPTNode(RAIN_t2_RV, new double[] {
            // R_t-1 = true, R_t = true
            0.7,
            // R_t-1 = true, R_t = false
            0.3,
            // R_t-1 = false, R_t = true
            0.3,
            // R_t-1 = false, R_t = false
            0.7 }, rain_t1);
        // Sensor Model
        @SuppressWarnings("unused")
        RandVar UMBREALLA_t2_RV = new RandVar("UMBREALLA_t2",new BooleanDomain());
        FiniteNode umbrealla_t2 = new FullCPTNode(UMBREALLA_t2_RV,
                new double[] {
                    // R_t = true, U_t = true
                    0.9,
                    // R_t = true, U_t = false
                    0.1,
                    // R_t = false, U_t = true
                    0.2,
                    // R_t = false, U_t = false
                    0.8 }, rain_t2);
        X_0_to_X_1.put(RAIN_t1_RV, RAIN_t2_RV);
        X_1_to_X_0.put(RAIN_t2_RV, RAIN_t1_RV);
        E_1.add(UMBREALLA_t2_RV);
        
        return new DynamicBayesNet(priorNetwork, X_0_to_X_1, E_1, rain_tm1);
    }
}
