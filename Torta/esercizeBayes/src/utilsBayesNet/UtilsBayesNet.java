package utilsBayesNet;


import aima.core.probability.Factor;
import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.bayes.Node;
import aima.core.probability.bayes.impl.BayesNet;
import aima.core.probability.bayes.impl.CPT;
import aima.core.probability.bayes.impl.FullCPTNode;
import aima.core.probability.proposition.AssignmentProposition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Biondi Giuseppe
 */
public class UtilsBayesNet {
    public static BayesNet createBayNet(HashSet<RandomVariable> acceptedNodeInNewNetwork, BayesianNetwork bn){
        List<Node> newRoots = new ArrayList();
        HashMap<String,Node> newAddedNode = new HashMap();
        
        for(RandomVariable rv : bn.getVariablesInTopologicalOrder()){
            Node i =bn.getNode(rv);
            if(acceptedNodeInNewNetwork.contains(i.getRandomVariable()) ) {
                if(i.isRoot()){
                    FullCPTNode n = new FullCPTNode( i.getRandomVariable(), ((CPT)i.getCPD()).getFactorFor(new AssignmentProposition[0]).getValues());
                    newRoots.add(n);
                    newAddedNode.put(n.getRandomVariable().getName(), n);
                }else{
                    List<Node> parents = new ArrayList();
                    List<RandomVariable> lostParents = new ArrayList();
                    for (Node p : i.getParents()){
                        if(newAddedNode.containsKey(p.getRandomVariable().getName()))
                            parents.add(newAddedNode.get(p.getRandomVariable().getName()));
                        else
                            lostParents.add(p.getRandomVariable());
                    }

                    //estraggo e riduco tabella
                    double[] normalizedArray = extractNormalizedProbability(i, lostParents);
                    
                    //creo nuovo nodo
                    FullCPTNode n;
                    if(parents.size()>0)
                        n = new FullCPTNode( i.getRandomVariable(), normalizedArray, parents.toArray(new Node[0]));
                    else{
                        n = new FullCPTNode( i.getRandomVariable(), normalizedArray);
                        newRoots.add(n);
                    }
                    newAddedNode.put(n.getRandomVariable().getName(), n);
                }
            }
        }
        return new BayesNet(newRoots.toArray(new Node[0]));
    }
    
    
     // ricalcolo tabella togliendo lost parents e restituisco array di probabilità
    public static double[] extractNormalizedProbability(Node i, List<RandomVariable> lostParents) {
        Factor f = ((CPT)i.getCPD()).getFactorFor(new AssignmentProposition[0]);
        f = f.sumOut(lostParents.toArray(new RandomVariable[0]));

        return normalizeFactorArray(f,i.getRandomVariable().getDomain().size());
    }
    
    // dato un array lo normalizza ogni n valori
    // es. n = 2 -> [2,2,4,8] -> [0.5,0.5,0.33,0.66]
    public static double[] normalizeFactorArray(Factor f, int n){
        double[] result = new double[f.getValues().length];
        for(int i=0; i< f.getValues().length; i += n){
            double sum = 0;
            for(int j=i; j< i+n; j++)
                sum += f.getValues()[j];
            for(int j=i; j< i+n; j++)
                result[j] = f.getValues()[j] / sum;
        }
        return result;
    }
    
    public static Factor pointwiseProduct(List<Factor> factors) {

            Factor product = factors.get(0);
            for (int i = 1; i < factors.size(); i++) {
                    product = product.pointwiseProduct(factors.get(i));
            }

            return product;
    }
    
}
