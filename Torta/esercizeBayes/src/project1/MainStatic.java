/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project1;

import aima.core.probability.CategoricalDistribution;
import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.bayes.impl.CPT;
import aima.core.probability.proposition.AssignmentProposition;
import bnparser.BifReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Biondi Giuseppe
 */
public class MainStatic {
    static HashMap<String,RandomVariable> mm = new HashMap();
    static BayesianNetwork bn;
    
    public static void main(String[] args) throws CloneNotSupportedException {
        
        /*
        mappe:
        chooseMap("../reti/0to20nodi/Sprinkler.xml");
        chooseMap("../reti/20to50nodi/insurance.xml");
        chooseMap("../reti/20to50nodi/child.xml");
        chooseMap("../reti/50to100nodi/win95pts.xml");
        chooseMap("../reti/100to1000nodi/pigs.xml");
        */
        
        
        
        //esempio ordinamento, rifare con altre reti più o meno grandi 
        //nota: nel caso di reti da 70+ nodi non usare reverse topological order
        chooseMap("../reti/20to50nodi/insurance.xml");
        RandomVariable[] query = new RandomVariable[]{mm.get("PropCost")};
        AssignmentProposition[] as = new AssignmentProposition[]{new AssignmentProposition(mm.get("DrivingSkill"), "Normal")};
        System.out.println("Test con query: "+ Arrays.asList(query) +" and evidence: "+ Arrays.asList(as));
        testQuery(query,as,bn);
        
        
        //esempio prunning  
        chooseMap("../reti/50to100nodi/win95pts.xml");
        query = new RandomVariable[]{mm.get("PrtData")};
        as = new AssignmentProposition[]{new AssignmentProposition(mm.get("DataFile"), "Correct")
                                                            };
        System.out.println("Test con query: "+ Arrays.asList(query) +" and evidence: "+ Arrays.asList(as));
        testPrunning(query,as,bn);

        //in fondo al file son presenti altri esempi come commento
    }
    
    private static void chooseMap(String path){
        bn = BifReader.readBIF(path);
        mm.clear();
        bn.getVariablesInTopologicalOrder().forEach(var -> mm.put(var.getName(), var));
    }
    
    private static void testPrunning(RandomVariable[] query, AssignmentProposition[] as, BayesianNetwork bn){
        System.out.println("Test prunning using min-fill:");
        System.out.println("number of node on this network: " + bn.getVariablesInTopologicalOrder().size());
        
        System.out.println("\nResult using no prunning:");
        List<RandomVariable> or = new Order(bn).minFillOrder();
        System.out.println("number of node: " + or.size());
        EliminationDarwiche ed = new EliminationDarwiche(or);
        printResult(ed,query,as,bn);
        
        System.out.println("\nResult using ancestor prunning:");
        BayesianNetwork bn1 = Prunning.prunningNodeAncestors(query, as, bn);
        or = new Order(bn1).minFillOrder();
        System.out.println("number of node: " + or.size());
        ed = new EliminationDarwiche(or);
        printResult(ed,query,as,bn1);
        
        System.out.println("\nResult using m-separated prunning:");
        bn1 = Prunning.prunningNodeMSeparated(query, as, bn);
        or = new Order(bn1).minFillOrder();
        System.out.println("number of node: " + or.size());
        ed = new EliminationDarwiche(or);
        printResult(ed,query,as,bn1);
        
        System.out.println("\nResult using edge prunning:");
        bn1 = Prunning.prunningEdge(query, as, bn);
        or = new Order(bn1).minFillOrder();
        System.out.println("number of node: " + or.size());
        ed = new EliminationDarwiche(or);
        printResult(ed,query,as,bn1);
        
        System.out.println("\nResult using all prunning combinated:");
        bn1 = Prunning.prunningNodeAncestors(query, as, bn);
        bn1 = Prunning.prunningNodeMSeparated(query, as, bn1);
        bn1 = Prunning.prunningEdge(query, as, bn1);
        or = new Order(bn1).minFillOrder();
        System.out.println("number of node: " + or.size());
        ed = new EliminationDarwiche(or);
        printResult(ed,query,as,bn1);
    }
    
    
    private static void testQuery(RandomVariable[] query, AssignmentProposition[] as, BayesianNetwork bn){
        Order or = new Order(bn);
        System.out.println("number of node on this network: " + bn.getVariablesInTopologicalOrder().size());
        
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
        
        System.out.println("\nResult using topological order:");
        ed.setOrder(new ArrayList(bn.getVariablesInTopologicalOrder()));
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

/* 
        chooseMap("../reti/0to20nodi/Sprinkler.xml"); 
        query = {mm.get("Grass")};
        as = {new AssignmentProposition(mm.get("Rain"), "Heavy")};
        System.out.println("Test con query: "+ Arrays.asList(query) +" and evidence: "+ Arrays.asList(as));                    
        testQuery(query, as, bn);
        //testPrunning(query,as,bn);
        */
        /*
        chooseMap("../reti/100to1000nodi/pigs.xml");
        query = new RandomVariable[]{mm.get("P48084891")};
        as = new AssignmentProposition[]{new AssignmentProposition(mm.get("P543551889"), "2")};
        System.out.println("Test con query: "+ Arrays.asList(query) +" and evidence: "+ Arrays.asList(as));
        testQuery(query,as,bn);
        //testPrunning(query,as,bn);
        */
        /*
        chooseMap("../reti/20to50nodi/child.xml");      
        query = {mm.get("CO2")};
        as = {new AssignmentProposition(mm.get("Age"), "0-3_days")};
        System.out.println("Test con query: "+ Arrays.asList(query) +" and evidence: "+ Arrays.asList(as));                    
        testQuery(query, as, bn);
        //testPrunning(query,as,bn);
        */
        /*
        chooseMap("../reti/20to50nodi/insurance.xml");
        query = new RandomVariable[]{mm.get("PropCost")};
        as = new AssignmentProposition[]{new AssignmentProposition(mm.get("DrivingSkill"), "Normal")};
        System.out.println("Test con query: "+ Arrays.asList(query) +" and evidence: "+ Arrays.asList(as));
        testQuery(query,as,bn);
        //testPrunning(query,as,bn);
        */
        