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
public class Asia {
    public static final RandVar WIND_tm1_RV = new RandVar("Wind_t-1",
            new BooleanDomain());
    public static final RandVar WIND_t_RV = new RandVar("Wind_t",
            new BooleanDomain());
    
    public static DynamicBayesianNetwork getExample() {
        FiniteNode prior_asia_tm1 = new FullCPTNode(ExampleRV.RAIN_tm1_RV,
                new double[]{0.5, 0.5});
        FiniteNode prior_smoke_tm1 = new FullCPTNode(tst.WIND_tm1_RV,
                new double[]{0.5, 0.5});
        FiniteNode prior_lung_tm1 = new FullCPTNode(ExampleRV.RAIN_tm1_RV,
                new double[]{0.5, 0.5}, prior_smoke_tm1);
        FiniteNode prior_tub_tm1 = new FullCPTNode(tst.WIND_tm1_RV,
                new double[]{0.5, 0.5}, prior_asia_tm1);

        BayesNet priorNetwork = new BayesNet(prior_asia_tm1, prior_smoke_tm1);

        
        // Prior belief state
        FiniteNode asia_tm1 = new FullCPTNode(ExampleRV.RAIN_tm1_RV,
                new double[]{0.5, 0.5});
        FiniteNode smoke_tm1 = new FullCPTNode(tst.WIND_tm1_RV,
                new double[]{0.5, 0.5});
        FiniteNode lung_tm1 = new FullCPTNode(ExampleRV.RAIN_tm1_RV,
                new double[]{0.5, 0.5}, smoke_tm1);
        FiniteNode tub_tm1 = new FullCPTNode(tst.WIND_tm1_RV,
                new double[]{0.5, 0.5}, asia_tm1);


        // Transition Model
        FiniteNode asia_t = new FullCPTNode(ExampleRV.RAIN_t_RV, new double[]{
            // asia_tm1 = true, asia_t = true
            0.6,
            // asia_tm1 = true, asia_t = false
            0.4,
            // asia_tm1 = false, asia_t = true
            0.8,
            // asia_tm1 = false, asia_t = false
            0.2,
        }, asia_tm1);

        FiniteNode smoke_t = new FullCPTNode(tst.WIND_t_RV, new double[]{
            // smoke_tm1 = true, smoke_t = true
            0.7,
            // smoke_tm1 = true, smoke_t = false
            0.3,
            // smoke_tm1 = false, smoke_t = true
            0.3,
            // smoke_tm1 = false, smoke_t = false
            0.7}, smoke_tm1);
        
        FiniteNode lung_t = new FullCPTNode(ExampleRV.RAIN_t_RV, new double[]{
            // lung_tm1 = true, lung_t = true
            0.6,
            // lung_tm1 = true, lung_t = false
            0.4,
            // lung_tm1 = false, lung_t = true
            0.8,
            // lung_tm1 = false, lung_t = false
            0.2,
        }, lung_tm1);

        FiniteNode tub_t = new FullCPTNode(tst.WIND_t_RV, new double[]{
            // smoke_tm1 = true, smoke_t = true
            0.7,
            // smoke_tm1 = true, smoke_t = false
            0.3,
            // smoke_tm1 = false, smoke_t = true
            0.3,
            // smoke_tm1 = false, smoke_t = false
            0.7}, tub_tm1);

        
        FiniteNode bronc_t = new FullCPTNode(ExampleRV.UMBREALLA_t_RV,
                new double[]{
                    // smoke_t = true, bronc_t = true
                    0.9,
                    // smoke_t = true, bronc_t = false
                    0.1,
                    // smoke_t = false, bronc_t = true
                    0.2,
                    // smoke_t = false, bronc_t = false
                    0.8}, smoke_t);
        
        FiniteNode either_t = new FullCPTNode(ExampleRV.RAIN_t_RV, new double[]{
            // tub_t = true, lung_t = true, either_t = true
            0.6,
            // tub_t = true, lung_t = true, either_t = false
            0.4,
            // tub_t = true, lung_t = false, either_t = true
            0.8,
            // tub_t = true, lung_t = false, either_t = false
            0.2,
            // tub_t = false, lung_t = true, either_t = true
            0.4,
            // tub_t = false, lung_t = true, either_t = false
            0.6,
            // tub_t = false, lung_t = false, either_t = true
            0.2,
            // tub_t = false, lung_t = false, either_t = false
            0.8
        }, tub_t, lung_t);
        
        FiniteNode xray_t = new FullCPTNode(ExampleRV.UMBREALLA_t_RV,
        new double[]{
            // either_t = true, xray_t = true
            0.9,
            // either_t = true, xray_t = false
            0.1,
            // either_t = false, xray_t = true
            0.2,
            // either_t = false, xray_t = false
            0.8}, either_t);
        
        FiniteNode dysp_t = new FullCPTNode(ExampleRV.RAIN_t_RV, new double[]{
            // either_t = true, bronc_t = true, dysp_t = true
            0.6,
            // either_t = true, bronc_t = true, dysp_t = false
            0.4,
            // either_t = true, bronc_t = false, dysp_t = true
            0.8,
            // either_t = true, bronc_t = false, dysp_t = false
            0.2,
            // either_t = false, bronc_t = true, dysp_t = true
            0.4,
            // either_t = false, bronc_t = true, dysp_t = false
            0.6,
            // either_t = false, bronc_t = false, dysp_t = true
            0.2,
            // either_t = false, bronc_t = false, dysp_t = false
            0.8
        }, either_t, bronc_t);

        Map<RandomVariable, RandomVariable> X_0_to_X_1 = new HashMap<RandomVariable, RandomVariable>();
        X_0_to_X_1.put(tst.asia_tm1, tst.asia_t);
        X_0_to_X_1.put(tst.tub_tm1, tst.tub_t);        
        X_0_to_X_1.put(tst.smoke_tm1, tst.smoke_t);
        X_0_to_X_1.put(tst.lung_tm1, tst.lung_t);
        Set<RandomVariable> E_1 = new HashSet<RandomVariable>();
        E_1.add(ExampleRV.UMBREALLA_t_RV);

        return new DynamicBayesNet(priorNetwork, X_0_to_X_1, E_1, asia_tm1, smoke_tm1);
    }
}
