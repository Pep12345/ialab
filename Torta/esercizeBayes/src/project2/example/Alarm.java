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
    public static final RandVar BURGLARY_tm1_RV = new RandVar("Burglary_t-1",
            new BooleanDomain());
    public static final RandVar EARTQUAKE_tm1_RV = new RandVar("EartQuake_t-1",
            new BooleanDomain());
    //t
    public static final RandVar BURGLARY_t_RV = new RandVar("Burglary_t",
            new BooleanDomain());
    public static final RandVar EARTQUAKE_t_RV = new RandVar("EartQuake_t",
            new BooleanDomain());
    public static final RandVar ALARM_t_RV = new RandVar("Alarm_t",
            new BooleanDomain());
    public static final RandVar JOHNCALLS_t_RV = new RandVar("JohnCalls_t",
            new BooleanDomain());
    public static final RandVar MARYCALLS_t_RV = new RandVar("MaryCalls_t",
            new BooleanDomain());
   
    
    public static DynamicBayesianNetwork getExample() {
        FiniteNode prior_burglary_tm1 = new FullCPTNode(Alarm.BURGLARY_tm1_RV,
                new double[]{0.01, 0.99});
        FiniteNode prior_eartquake_tm1 = new FullCPTNode(Alarm.EARTQUAKE_tm1_RV,
                new double[]{0.02, 0.98});

        BayesNet priorNetwork = new BayesNet(prior_burglary_tm1, prior_eartquake_tm1);

        
        // Prior belief state
        FiniteNode burglary_tm1 = new FullCPTNode(Alarm.BURGLARY_tm1_RV,
                new double[]{0.01, 0.99});
        FiniteNode eartquake_tm1 = new FullCPTNode(Alarm.EARTQUAKE_tm1_RV,
                new double[]{0.02, 0.98});


        // Transition Model
        FiniteNode burglary_t = new FullCPTNode(Alarm.BURGLARY_t_RV, new double[]{
            // burglary_tm1 = true, burglary_t = true
            0.2,
            // burglary_tm1 = true, burglary_t = false
            0.8,
            // burglary_tm1 = false, burglary_t = true
            0.6,
            // burglary_tm1 = false, burglary_t = false
            0.4,
        }, burglary_tm1);

        FiniteNode eartquake_t = new FullCPTNode(Alarm.EARTQUAKE_t_RV, new double[]{
            // heartquake_tm1 = true, heartquake_t = true
            0.3,
            // heartquake_tm1 = true, heartquake_t = false
            0.7,
            // heartquake_tm1 = false, heartquake_t = true
            0.3,
            // heartquake_tm1 = false, heartquake_t = false
            0.7}, eartquake_tm1);
        
        FiniteNode alarm_t = new FullCPTNode(Alarm.ALARM_t_RV, new double[]{
            // heartquake_t = true, burglary_t = true, alarm_t = true
            0.95,
            // heartquake_t = true, burglary_t = true, alarm_t = false
            0.05,
            // heartquake_t = true, burglary_t = false, alarm_t = true
            0.29,
            // heartquake_t = true, burglary_t = false, alarm_t = false
            0.71, 
            // heartquake_t = false, burglary_t = true, alarm_t = true
            0.94,
            // heartquake_t = false, burglary_t = true, alarm_t = false
            0.06,
            // heartquake_t = false, burglary_t = false, alarm_t = true
            0.001,
            // heartquake_t = false, burglary_t = false, alarm_t = false
            0.999
            
        },eartquake_t, burglary_t);

        
        FiniteNode johncalls_t = new FullCPTNode(Alarm.JOHNCALLS_t_RV,
        new double[]{
                // alarm_t = true, johncalls_t = true
                0.9,
                // alarm_t = true, johncalls_t = false
                0.1,
                // alarm_t = false, johncalls_t = true
                0.05,
                // alarm_t = false, johncalls_t = false
                0.95}, alarm_t);

        FiniteNode marycalls_t = new FullCPTNode(Alarm.MARYCALLS_t_RV,
        new double[]{
            // alarm_t = true, marycalls_t = true
            0.7,
            // alarm_t = true, marycalls_t = false
            0.3,
            // alarm_t = false, marycalls_t = true
            0.01,
            // alarm_t = false, marycalls_t = false
            0.99}, alarm_t);

        Map<RandomVariable, RandomVariable> X_0_to_X_1 = new HashMap<RandomVariable, RandomVariable>();
        X_0_to_X_1.put(Alarm.BURGLARY_tm1_RV, Alarm.BURGLARY_t_RV);
        X_0_to_X_1.put(Alarm.EARTQUAKE_tm1_RV, Alarm.EARTQUAKE_t_RV);   
        Set<RandomVariable> E_1 = new HashSet<RandomVariable>();
        E_1.add(Alarm.ALARM_t_RV);
        E_1.add(Alarm.JOHNCALLS_t_RV);
        E_1.add(Alarm.MARYCALLS_t_RV);

        return new DynamicBayesNet(priorNetwork, X_0_to_X_1, E_1, eartquake_tm1, burglary_tm1);
    }
}
