/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project2;

import static project2.RollingUp.rollUp;
import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.DynamicBayesianNetwork;
import aima.core.probability.bayes.approx.ParticleFiltering;
import aima.core.probability.example.DynamicBayesNetExampleFactory;
import aima.core.probability.example.ExampleRV;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.probability.util.ProbabilityTable;
import bozza.tst;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import project2.example.Asia;
import project2.example.UmbrellaExample;
import project2.example.UmbrellaWindExample;

/**
 *
 * @author Biondi Giuseppe
 */
public class MainDynamic {
    public static void main(String[] args){
        int n = 1000;
        int m = 20  ;
        AssignmentProposition[][] aps = null;
        
        //creo lista evidenze da passare 
        if (m > 0) {
            aps = new AssignmentProposition[m][1];
            for (int i=0; i<m; i++) {
                aps[i][0] = new AssignmentProposition(ExampleRV.UMBREALLA_t_RV, Boolean.TRUE);
               // aps[i][1] = new AssignmentProposition(Asia.DYSP_t_RV, "no");
            }
        }        
            
        System.out.println("Rete Asia -  rolling up");
        //RollingUpFiltering rp = new RollingUpFiltering(DynamicBayesNetExampleFactory.getUmbrellaWorldNetwork());
        RollingUpFiltering rp = new RollingUpFiltering(UmbrellaWindExample.getExample());
        ParticleFiltering pf = new ParticleFiltering(n,UmbrellaWindExample.getExample());

        for (int i=0; i<m; i++) {
            ProbabilityTable result = rp.rollUp(aps[i]);
            System.out.println("Time " + (i+1));
            AssignmentProposition[][] S = pf.particleFiltering(aps[i]);
            printSamples(S, n);
            /*AssignmentProposition[] assignments = {new AssignmentProposition(Asia.ASIA_t_RV,"no"),
                                                   new AssignmentProposition(Asia.SMOKE_t_RV,"no"),
                                                    new AssignmentProposition(Asia.TUB_t_RV,"no"),
                                                    new AssignmentProposition(Asia.LUNG_t_RV,"no")};*/
            System.out.println("rollup: " + result);
        }
            
    }
    
    private static void printSamples(AssignmentProposition[][] S, int n) {
        HashMap<String,Integer> hm = new HashMap<String,Integer>();
        
        int nstates = S[0].length;
        
        for (int i = 0; i < n; i++) {
            String key = "";
            for (int j = 0; j < nstates; j++) {
                AssignmentProposition ap = S[i][j];
                key += ap.getValue().toString();
            }
            Integer val = hm.get(key);
            if (val == null) {
                hm.put(key, 1);
            } else {
                hm.put(key, val + 1);
            }
        }
        
        for (String key : hm.keySet()) {
            System.out.println(key + ": " + hm.get(key)/(double)n);
        }
    }
    
}

// main per rollingup vecchio
        /*//creo rete
        DynamicBayesianNetwork example = UmbrellaExample.getExample();
        
        //salvo variabili
        HashMap<String, RandomVariable> bnRV = new HashMap();
        example.getVariablesInTopologicalOrder().forEach(v -> bnRV.put(v.getName(), v));
        System.out.println("Variabili slice 0: "+ example.getPriorNetwork().getVariablesInTopologicalOrder());
        
        //creo query finale
        //query umbrella
        RandomVariable[] query = {bnRV.get("Rain_t2")};
        //query umbrella-wind
        //RandomVariable[] query = {bnRV.get("Rain_t"),bnRV.get("Wind_t")};
        

        
        //creo lista evidenze
        List<AssignmentProposition> ev = new ArrayList();
        //ev umbrella
        ev.add(new AssignmentProposition(bnRV.get("Umbrella_t"), Boolean.TRUE));
        ev.add(new AssignmentProposition(bnRV.get("UMBREALLA_t1"), Boolean.TRUE));
        ev.add(new AssignmentProposition(bnRV.get("UMBREALLA_t2"), Boolean.TRUE));
        //ev umbrella-wind
        //ev.add(new AssignmentProposition(bnRV.get("Umbrella_t"), Boolean.TRUE));

        //avvio rolling up
        ProbabilityTable rollUp = rollUp(example, query, ev, example.getPriorNetwork().getVariablesInTopologicalOrder(), new ArrayList());
        
        System.out.println(rollUp);   
     */
