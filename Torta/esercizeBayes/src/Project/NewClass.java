/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Project;

import aima.core.probability.CategoricalDistribution;
import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.proposition.AssignmentProposition;
import bnparser.BifReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Biondi Giuseppe
 */
public class NewClass {
    static HashMap<String,RandomVariable> mm = new HashMap();
    static BayesianNetwork bn;
    
    public static void main(String[] args) throws CloneNotSupportedException {
        
        chooseMap("../reti/20to50nodi/child.xml");
        
        RandomVariable[] query = {mm.get("CO2")};
        AssignmentProposition[] as = {new AssignmentProposition(mm.get("Age"), "0-3_days")};
        System.out.println("Test con query: "+ Arrays.asList(query) +" and evidence: "+ Arrays.asList(as));                    
        
        bn = Prunning.prunningNodeAncestors(query, as, bn);
        System.out.println("-Added prunning of ancestors");
        bn = Prunning.prunningEdge(query, as, bn);
        System.out.println("-Added prunning of edge");
        
        testQuery(query, as, bn);
        
        chooseMap("../reti/0to20nodi/Sprinkler.xml");
        query = new RandomVariable[]{mm.get("Rain")};
        as = new AssignmentProposition[]{new AssignmentProposition(mm.get("Season"), "winter")};
        System.out.println("Test con query: "+ Arrays.asList(query) +" and evidence: "+ Arrays.asList(as));
        testQuery(query,as,bn);
        
    }
    
    private static void chooseMap(String path){
        bn = BifReader.readBIF(path);
        bn.getVariablesInTopologicalOrder().forEach(var -> mm.put(var.getName(), var));
    }
    
    private static void testQuery(RandomVariable[] query, AssignmentProposition[] as, BayesianNetwork bn){
        Order or = new Order(bn);
        
        System.out.println("\nResult using reverse topological order:");
        List<RandomVariable> order = or.reverseTopologicalOrder();
        EliminationDarwiche ed = new EliminationDarwiche(order);
        printResult(ed,query,as,bn);
        
        System.out.println("\nResult using reverse min fill order:");
        ed.setOrder(or.minFillOrder());
        printResult(ed,query,as,bn);
        
        System.out.println("\nResult using reverse min degree order:");
        ed.setOrder(or.minDegreeOrder());
        printResult(ed,query,as,bn);
        
        System.out.println("\n\n\n");
    }
    
    
    private static void printResult(EliminationDarwiche ed, RandomVariable[] query, AssignmentProposition[] as, BayesianNetwork bn){
        long startTime = System.nanoTime();
        CategoricalDistribution eliminationAsk = ed.eliminationAsk(query, as, bn);
        long endTime = System.nanoTime();
        System.out.println(eliminationAsk);
        System.out.println("Time in seconds: "+(endTime-startTime)/(Math.pow(10, 9)));
    }
}

/* creare tabelle e grafici per reti statiche, fattori più grandi o piccoli
per reti dinamiche servono reti più complesse da una decina di per stato
*/