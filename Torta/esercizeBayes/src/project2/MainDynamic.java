/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project2;

import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.DynamicBayesianNetwork;
import aima.core.probability.bayes.approx.ParticleFiltering;
import aima.core.probability.example.DynamicBayesNetExampleFactory;
import aima.core.probability.example.ExampleRV;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.probability.util.ProbabilityTable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import project2.example.Alarm;
import project2.example.Asia;
import project2.example.UmbrellaWindExample;

/**
 *
 * @author Biondi Giuseppe
 */


/*
 per la rete umbrella :
        DynamicBayesNetExampleFactory.getUmbrellaWorldNetwork()
        ExampleRV -> per prendere le random variable
*/
public class MainDynamic {
    public static void main(String[] args){
        int n = 1000;
        int m = 40  ;
        AssignmentProposition[][] aps = null;
        
        //creo lista evidenze da passare 
        if (m > 0) {
            aps = new AssignmentProposition[m][1];
            for (int i=0; i<m; i++) {
                aps[i][0] = new AssignmentProposition(Alarm.MARYCALLS_t_RV, Boolean.TRUE);
               // aps[i][1] = new AssignmentProposition(Asia.DYSP_t_RV, "no");
            }
        }        
            
        System.out.println("Rete Asia -  rolling up");
        //RollingUpFiltering rp = new RollingUpFiltering(DynamicBayesNetExampleFactory.getUmbrellaWorldNetwork());
        RollingUpFiltering rp = new RollingUpFiltering(UmbrellaWindExample.getExample());
        ParticleFiltering pf = new ParticleFiltering(n,Alarm.getExample());

        for (int i=0; i<m; i++) {
            ProbabilityTable result = rp.rollUp(aps[i], RollingUpFiltering.MIN_FILL_ORDER);
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
