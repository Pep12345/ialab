/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project2;

import utilsBayesNet.UtilsBayesNet;
import bozza.tst;
import project1.EliminationDarwiche;
import project1.Order;
import aima.core.probability.Factor;
import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.bayes.DynamicBayesianNetwork;
import aima.core.probability.bayes.Node;
import aima.core.probability.bayes.impl.BayesNet;
import aima.core.probability.bayes.impl.CPT;
import aima.core.probability.bayes.impl.FullCPTNode;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.probability.util.ProbabilityTable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import utilsBayesNet.UtilsBayesNet;

/**
 *
 * @author Biondi Giuseppe
 */
public class RollingUp {
    
    private static final ProbabilityTable _identity = new ProbabilityTable(
                    new double[] { 1.0 });  
    
    // metodo per trovare la lista di nodi dello slice successivo a previous
    public static List<RandomVariable> getNext(DynamicBayesianNetwork dbn, List<RandomVariable> previous){
        List<RandomVariable> nextNodes = new ArrayList();
        for(RandomVariable rn: previous){
            Node n = dbn.getNode(rn);
            for(Node child : n.getChildren()){
                if(!previous.contains(child.getRandomVariable()))
                    nextNodes.add(child.getRandomVariable());
            }
            for(Node child : n.getChildren()){
                for(Node nepewh : child.getChildren()){
                    if(dbn.getX_1().contains(nepewh.getRandomVariable())){
                        if(!nextNodes.contains(dbn.getX_1_to_X_0().get(nepewh.getRandomVariable())))
                            nextNodes.add(nepewh.getRandomVariable());
                    }else
                        nextNodes.add(nepewh.getRandomVariable());
                }
            }
        }
        return nextNodes;
    }
  
    //metodo per calcolare la probabilità mediante il medoto del rolling up
    public static ProbabilityTable rollUp(DynamicBayesianNetwork dbn, RandomVariable[] query,
                                        List<AssignmentProposition> ev, List<RandomVariable> prevStep,
                                        List<Factor> factorsFromPreviousStep){

        //calcolo slice attuale
        boolean stop = false;
        List<RandomVariable> next = getNext(dbn, prevStep);
        System.out.println("Variable in this slice: " + next);
        
        //creo rete bayesiana usando slice passato e attuale
        HashSet<RandomVariable> hs = new HashSet(prevStep);
        hs.addAll(next);
        BayesNet bn = UtilsBayesNet.createBayNet(hs, dbn);
        System.out.println("Rete Bayesiana: " + bn.getVariablesInTopologicalOrder());
        
        //creo ordinamento e creo classe elimination
        Order or = new Order(bn); 
        EliminationDarwiche edd = new EliminationDarwiche(or.reverseTopologicalOrder());
        
        //calcolo query per VE in questa rete (quei nodi che avranno un arco verso lo slice del prossimo turno)
        List<RandomVariable> queryForThisStep = new ArrayList();
        for(RandomVariable r : next) {
            if(dbn.getX_0().contains(r))
                queryForThisStep.add(r);
        }
        if(queryForThisStep.isEmpty()){
            queryForThisStep.addAll(Arrays.asList(query));
            stop = true;
        }
        System.out.println("Query for this BN"+queryForThisStep);
        
        //calcolo evidenze per VE in questa rete (cerco tra le evidenze quelle che appaiono in questo slice)
        List<AssignmentProposition> evidenceForThisStep = new ArrayList();
        for(AssignmentProposition as: ev){
            if(next.contains(as.getTermVariable()))
                evidenceForThisStep.add(as);
        }
        System.out.println("Evidence for this BN"+evidenceForThisStep);
        
        //eseguo VE passando query, evidenze, rete e lista fattori step precedente da aggiungere
        factorsFromPreviousStep = edd.dynamicEliminationAsk(queryForThisStep.toArray(new RandomVariable[0]), 
                                                evidenceForThisStep.toArray(new AssignmentProposition[0]), 
                                                bn, factorsFromPreviousStep);
        System.out.println("result: ");
        Factor printProduct = UtilsBayesNet.pointwiseProduct(factorsFromPreviousStep);
        System.out.println( Arrays.toString(UtilsBayesNet.normalizeFactorArray(printProduct, printProduct.getValues().length)));
        factorsFromPreviousStep.forEach(f -> System.out.println(f.getArgumentVariables()+ "  "+ f));
        
        
        System.out.println("\n\n");
        if(stop){
            Factor product = UtilsBayesNet.pointwiseProduct(factorsFromPreviousStep);
            return ((ProbabilityTable) product.pointwiseProductPOS(_identity, query)).normalize();
        }else
            return rollUp(dbn, query, ev, next, factorsFromPreviousStep);
        
    }
    
}
