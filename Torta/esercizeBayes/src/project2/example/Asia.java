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
import aima.core.probability.domain.ArbitraryTokenDomain;
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
    //tm1
    public static final RandVar ASIA_tm1_RV = new RandVar("Asia_t-1",
            new ArbitraryTokenDomain("yes", "no"));
    public static final RandVar SMOKE_tm1_RV = new RandVar("Smoke_t-1",
            new ArbitraryTokenDomain("yes", "no"));
    public static final RandVar LUNG_tm1_RV = new RandVar("Lung_t-1",
            new ArbitraryTokenDomain("yes", "no"));
    public static final RandVar TUB_tm1_RV = new RandVar("Tub_t-1",
            new ArbitraryTokenDomain("yes", "no"));
    //t
    public static final RandVar ASIA_t_RV = new RandVar("Asia_t",
            new ArbitraryTokenDomain("yes", "no"));
    public static final RandVar SMOKE_t_RV = new RandVar("Smoke_t",
            new ArbitraryTokenDomain("yes", "no"));
    public static final RandVar LUNG_t_RV = new RandVar("Lung_t",
            new ArbitraryTokenDomain("yes", "no"));
    public static final RandVar TUB_t_RV = new RandVar("Tub_t",
            new ArbitraryTokenDomain("yes", "no"));
    public static final RandVar Bronc_t_RV = new RandVar("Bronc_t",
            new ArbitraryTokenDomain("yes", "no"));
    public static final RandVar EITHER_t_RV = new RandVar("Either_t",
            new ArbitraryTokenDomain("yes", "no"));
    public static final RandVar DYSP_t_RV = new RandVar("Dysp_t",
            new ArbitraryTokenDomain("yes", "no"));
    public static final RandVar XRAY_t_RV = new RandVar("Xray_t",
            new ArbitraryTokenDomain("yes", "no"));
    
    public static DynamicBayesianNetwork getExample() {
        FiniteNode prior_asia_tm1 = new FullCPTNode(Asia.ASIA_tm1_RV,
                new double[]{0.01, 0.99});
        FiniteNode prior_smoke_tm1 = new FullCPTNode(Asia.SMOKE_tm1_RV,
                new double[]{0.5, 0.5});
        FiniteNode prior_lung_tm1 = new FullCPTNode(Asia.LUNG_tm1_RV,
                new double[]{0.1, 0.9, 0.01, 0.99}, prior_smoke_tm1);
        FiniteNode prior_tub_tm1 = new FullCPTNode(Asia.TUB_tm1_RV,
                new double[]{0.05, 0.95, 0.01, 0.99}, prior_asia_tm1);

        BayesNet priorNetwork = new BayesNet(prior_asia_tm1, prior_smoke_tm1);

        
        // Prior belief state
        FiniteNode asia_tm1 = new FullCPTNode(Asia.ASIA_tm1_RV,
                new double[]{0.01, 0.99});
        FiniteNode smoke_tm1 = new FullCPTNode(Asia.SMOKE_tm1_RV,
                new double[]{0.5, 0.5});
        FiniteNode lung_tm1 = new FullCPTNode(Asia.LUNG_tm1_RV,
                new double[]{0.1, 0.9, 0.01, 0.99}, smoke_tm1);
        FiniteNode tub_tm1 = new FullCPTNode(Asia.TUB_tm1_RV,
                new double[]{0.05, 0.95, 0.01, 0.99}, asia_tm1);


        // Transition Model
        FiniteNode asia_t = new FullCPTNode(Asia.ASIA_t_RV, new double[]{
            // asia_tm1 = true, asia_t = true
            0.6,
            // asia_tm1 = true, asia_t = false
            0.4,
            // asia_tm1 = false, asia_t = true
            0.8,
            // asia_tm1 = false, asia_t = false
            0.2,
        }, asia_tm1);

        FiniteNode smoke_t = new FullCPTNode(Asia.SMOKE_t_RV, new double[]{
            // smoke_tm1 = true, smoke_t = true
            0.7,
            // smoke_tm1 = true, smoke_t = false
            0.3,
            // smoke_tm1 = false, smoke_t = true
            0.3,
            // smoke_tm1 = false, smoke_t = false
            0.7}, smoke_tm1);
        
        FiniteNode lung_t = new FullCPTNode(Asia.LUNG_t_RV, new double[]{
            // lung_tm1 = true, smoke_t = true, lung_t = true
            0.4,
            // lung_tm1 = true, smoke_t = true, lung_t = false
            0.6,
            // lung_tm1 = true, smoke_t = false, lung_t = true
            0.2,
            // lung_tm1 = true, smoke_t = false, lung_t = false
            0.8, 
            // lung_tm1 = false, smoke_t = true, lung_t = true
            0.1,
            // lung_tm1 = false, smoke_t = true, lung_t = false
            0.9,
            // lung_tm1 = false, smoke_t = false, lung_t = true
            0.2,
            // lung_tm1 = false, smoke_t = false, lung_t = false
            0.8
            
        },lung_tm1, smoke_t);

        FiniteNode tub_t = new FullCPTNode(Asia.TUB_t_RV, new double[]{
            // tub_tm1 = true, asia_t = true, tub_t = true
            0.2,
            // smoke_tm1 = true, smoke_t = false
            0.8,
            // smoke_tm1 = false, smoke_t = true
            0.1,
            // smoke_tm1 = false, smoke_t = false
            0.9,
            0.15, 0.85,
            0.05, 0.95
        
        },tub_tm1, asia_t);

        
        FiniteNode bronc_t = new FullCPTNode(Asia.Bronc_t_RV,
                new double[]{
                    // smoke_t = true, bronc_t = true
                    0.6,
                    // smoke_t = true, bronc_t = false
                    0.4,
                    // smoke_t = false, bronc_t = true
                    0.3,
                    // smoke_t = false, bronc_t = false
                    0.7}, smoke_t);
        
        FiniteNode either_t = new FullCPTNode(Asia.EITHER_t_RV, new double[]{
            // tub_t = true, lung_t = true, either_t = true
            1.0,
            // tub_t = true, lung_t = true, either_t = false
            0.0,
            // tub_t = true, lung_t = false, either_t = true
            1.0,
            // tub_t = true, lung_t = false, either_t = false
            0.0,
            // tub_t = false, lung_t = true, either_t = true
            1.0,
            // tub_t = false, lung_t = true, either_t = false
            0.0,
            // tub_t = false, lung_t = false, either_t = true
            0.0,
            // tub_t = false, lung_t = false, either_t = false
            1.0
        }, tub_t, lung_t);
        
        FiniteNode xray_t = new FullCPTNode(Asia.XRAY_t_RV,
        new double[]{
            // either_t = true, xray_t = true
            0.98,
            // either_t = true, xray_t = false
            0.02,
            // either_t = false, xray_t = true
            0.05,
            // either_t = false, xray_t = false
            0.95}, either_t);
        
        FiniteNode dysp_t = new FullCPTNode(Asia.DYSP_t_RV, new double[]{
            // either_t = true, bronc_t = true, dysp_t = true
            0.9,
            // either_t = true, bronc_t = true, dysp_t = false
            0.1,
            // either_t = true, bronc_t = false, dysp_t = true
            0.8,
            // either_t = true, bronc_t = false, dysp_t = false
            0.2,
            // either_t = false, bronc_t = true, dysp_t = true
            0.7,
            // either_t = false, bronc_t = true, dysp_t = false
            0.3,
            // either_t = false, bronc_t = false, dysp_t = true
            0.1,
            // either_t = false, bronc_t = false, dysp_t = false
            0.9
        }, either_t, bronc_t);

        Map<RandomVariable, RandomVariable> X_0_to_X_1 = new HashMap<RandomVariable, RandomVariable>();
        X_0_to_X_1.put(Asia.ASIA_tm1_RV, Asia.ASIA_t_RV);
        X_0_to_X_1.put(Asia.TUB_tm1_RV, Asia.TUB_t_RV);        
        X_0_to_X_1.put(Asia.SMOKE_tm1_RV, Asia.SMOKE_t_RV);
        X_0_to_X_1.put(Asia.LUNG_tm1_RV, Asia.LUNG_t_RV);
        Set<RandomVariable> E_1 = new HashSet<RandomVariable>();
        E_1.add(Asia.XRAY_t_RV);
        E_1.add(Asia.EITHER_t_RV);
        E_1.add(Asia.DYSP_t_RV);
        E_1.add(Asia.Bronc_t_RV);

        return new DynamicBayesNet(priorNetwork, X_0_to_X_1, E_1, asia_tm1, smoke_tm1, tub_tm1, lung_tm1 );
    }
}
