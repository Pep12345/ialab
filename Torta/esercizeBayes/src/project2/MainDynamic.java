/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project2;

import static project2.RollingUp.rollUp;
import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.DynamicBayesianNetwork;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.probability.util.ProbabilityTable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import project2.example.UmbrellaExample;

/**
 *
 * @author Biondi Giuseppe
 */
public class MainDynamic {
    public static void main(String[] args){
        //creo rete
        DynamicBayesianNetwork example = UmbrellaExample.getExample();
        
        //salvo variabili
        HashMap<String, RandomVariable> bnRV = new HashMap();
        example.getVariablesInTopologicalOrder().forEach(v -> bnRV.put(v.getName(), v));
        System.out.println("Variabili slice 0: "+ example.getPriorNetwork().getVariablesInTopologicalOrder());
        
        //creo query finale
        //query umbrella
        RandomVariable[] query = {bnRV.get("Rain_t2")};
        //query umbrella-wind
        //RandomVariable[] query = {bnRV.get("Rain_t")};
        
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
        
    }
}
