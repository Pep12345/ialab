/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project2.example;

import bozza.tst;
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
public class UmbrellaWindExample {
    public static final RandVar WIND_tm1_RV = new RandVar("Wind_t-1",
            new BooleanDomain());
    public static final RandVar WIND_t_RV = new RandVar("Wind_t",
            new BooleanDomain());
    
    public static DynamicBayesianNetwork getExample() {
        FiniteNode prior_rain_tm1 = new FullCPTNode(ExampleRV.RAIN_tm1_RV,
                new double[]{0.5, 0.5});
        FiniteNode prior_wind_tm1 = new FullCPTNode(tst.WIND_tm1_RV,
                new double[]{0.5, 0.5});

        BayesNet priorNetwork = new BayesNet(prior_rain_tm1, prior_wind_tm1);

        // Prior belief state
        FiniteNode rain_tm1 = new FullCPTNode(ExampleRV.RAIN_tm1_RV,
                new double[]{0.5, 0.5});
        FiniteNode wind_tm1 = new FullCPTNode(tst.WIND_tm1_RV,
                new double[]{0.5, 0.5});


        // Transition Model
        FiniteNode rain_t = new FullCPTNode(ExampleRV.RAIN_t_RV, new double[]{
            // R_t-1 = true, W_t-1 = true, R_t = true
            0.6,
            // R_t-1 = true, W_t-1 = true, R_t = false
            0.4,
            // R_t-1 = true, W_t-1 = false, R_t = true
            0.8,
            // R_t-1 = true, W_t-1 = false, R_t = false
            0.2,
            // R_t-1 = false, W_t-1 = true, R_t = true
            0.4,
            // R_t-1 = false, W_t-1 = true, R_t = false
            0.6,
            // R_t-1 = false, W_t-1 = false, R_t = true
            0.2,
            // R_t-1 = false, W_t-1 = false, R_t = false
            0.8
        }, rain_tm1, wind_tm1);

        FiniteNode wind_t = new FullCPTNode(tst.WIND_t_RV, new double[]{
            // W_t-1 = true, W_t = true
            0.7,
            // W_t-1 = true, W_t = false
            0.3,
            // W_t-1 = false, W_t = true
            0.3,
            // W_t-1 = false, W_t = false
            0.7}, wind_tm1);
                
        // Sensor Model
        @SuppressWarnings("unused")
        FiniteNode umbrealla_t = new FullCPTNode(ExampleRV.UMBREALLA_t_RV,
                new double[]{
                    // R_t = true, U_t = true
                    0.9,
                    // R_t = true, U_t = false
                    0.1,
                    // R_t = false, U_t = true
                    0.2,
                    // R_t = false, U_t = false
                    0.8}, rain_t);

        Map<RandomVariable, RandomVariable> X_0_to_X_1 = new HashMap<RandomVariable, RandomVariable>();
        X_0_to_X_1.put(ExampleRV.RAIN_tm1_RV, ExampleRV.RAIN_t_RV);
        X_0_to_X_1.put(tst.WIND_tm1_RV, ExampleRV.RAIN_t_RV);        
        X_0_to_X_1.put(tst.WIND_tm1_RV, tst.WIND_t_RV);
        Set<RandomVariable> E_1 = new HashSet<RandomVariable>();
        E_1.add(ExampleRV.UMBREALLA_t_RV);

        return new DynamicBayesNet(priorNetwork, X_0_to_X_1, E_1, rain_tm1, wind_tm1);
    }
}
