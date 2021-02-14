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
        
        //ex ordinamento, rifare con altre reti più o meno grandi 
        //nota: nel caso di reti da 70+ nodi non usare reverse topological order
        chooseMap("../reti/20to50nodi/insurance.xml");
        RandomVariable[] query = new RandomVariable[]{mm.get("PropCost")};
        AssignmentProposition[] as = new AssignmentProposition[]{new AssignmentProposition(mm.get("DrivingSkill"), "Normal")};
        System.out.println("Test con query: "+ Arrays.asList(query) +" and evidence: "+ Arrays.asList(as));
        testQuery(query,as,bn);
        
        /*chooseMap("../reti/0to20nodi/Sprinkler.xml"); 
        RandomVariable[] query = {mm.get("Grass")};
        AssignmentProposition[] as = {new AssignmentProposition(mm.get("Rain"), "Heavy")};
        System.out.println("Test con query: "+ Arrays.asList(query) +" and evidence: "+ Arrays.asList(as));                    
        
        testQuery(query, as, bn);
        
        
        chooseMap("../reti/20to50nodi/insurance.xml");
        query = new RandomVariable[]{mm.get("Antilock")};
        as = new AssignmentProposition[]{new AssignmentProposition(mm.get("OtherCarCost"), "Thousand")};
        System.out.println("Test con query: "+ Arrays.asList(query) +" and evidence: "+ Arrays.asList(as));
        testQuery(query,as,bn);
        
        
        bn = Prunning.prunningNodeAncestors(query, as, bn);
        System.out.println("-Added prunning of ancestors");
        bn = Prunning.prunningEdge(query, as, bn);
        System.out.println("-Added prunning of edge");
        
        testQuery(query, as, bn);
        
        chooseMap("../reti/100to1000nodi/pigs.xml");
        query = new RandomVariable[]{mm.get("P48084891")};
        as = new AssignmentProposition[]{new AssignmentProposition(mm.get("P543551889"), "2")};
        System.out.println("Test con query: "+ Arrays.asList(query) +" and evidence: "+ Arrays.asList(as));
        testQuery(query,as,bn);*/
        
        /*chooseMap("../reti/20to50nodi/child.xml");
        
        RandomVariable[] query = {mm.get("CO2")};
        AssignmentProposition[] as = {new AssignmentProposition(mm.get("Age"), "0-3_days")};
        System.out.println("Test con query: "+ Arrays.asList(query) +" and evidence: "+ Arrays.asList(as));                    
        
        testQuery(query, as, bn);
        
        bn = Prunning.prunningNodeAncestors(query, as, bn);
        System.out.println("-Added prunning of ancestors");
        bn = Prunning.prunningEdge(query, as, bn);
        System.out.println("-Added prunning of edge");
        
        testQuery(query, as, bn);
        
        chooseMap("../reti/20to50nodi/insurance.xml");
        query = new RandomVariable[]{mm.get("PropCost")};
        as = new AssignmentProposition[]{new AssignmentProposition(mm.get("DrivingSkill"), "Normal")};
        System.out.println("Test con query: "+ Arrays.asList(query) +" and evidence: "+ Arrays.asList(as));
        testQuery(query,as,bn);
        
        bn = Prunning.prunningNodeAncestors(query, as, bn);
        System.out.println("-Added prunning of ancestors");
        bn = Prunning.prunningEdge(query, as, bn);
        System.out.println("-Added prunning of edge");
        
        testQuery(query, as, bn);
        
        mm.clear();
        chooseMap("../reti/50to100nodi/win95pts.xml");
        query = new RandomVariable[]{mm.get("EPSGrphc")};
        as = new AssignmentProposition[]{new AssignmentProposition(mm.get("GDIOUT"), "No")};
        System.out.println("Test con query: "+ Arrays.asList(query) +" and evidence: "+ Arrays.asList(as));
        testQuery(query,as,bn);
        
        bn = Prunning.prunningNodeAncestors(query, as, bn);
        System.out.println("-Added prunning of ancestors");
        bn = Prunning.prunningEdge(query, as, bn);
        System.out.println("-Added prunning of edge");
        
        testQuery(query, as, bn);
        
        
        chooseMap("../reti/100to1000nodi/pigs.xml");
        query = new RandomVariable[]{mm.get("P48084891")};
        as = new AssignmentProposition[]{new AssignmentProposition(mm.get("P543551889"), "2")};
        testQuery(query, as, bn);
        
        bn = Prunning.prunningNodeAncestors(query, as, bn);
        System.out.println("-Added prunning of ancestors");
        bn = Prunning.prunningEdge(query, as, bn);
        System.out.println("-Added prunning of edge");
        
        testQuery(query, as, bn);*/
        
    }
    
    private static void chooseMap(String path){
        bn = BifReader.readBIF(path);
        mm.clear();
        bn.getVariablesInTopologicalOrder().forEach(var -> mm.put(var.getName(), var));
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
        ed.setOrder(bn.getVariablesInTopologicalOrder());
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

