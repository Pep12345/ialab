/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project1;

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
import utilsBayesNet.UtilsBayesNet;

/**
 *
 * @author Biondi Giuseppe
 */
public class Prunning {

     // ricorsivamente per ogni nodo prendo i figli e i parent dei figli e poi chiamo ricorisvamente
    // su parent nodo e parent figli
    public static BayesNet prunningNodeMSeparated(final RandomVariable[] qrv, final AssignmentProposition[] e, final BayesianNetwork bn){
        HashSet<RandomVariable> nodeToSave = new HashSet();
        for( RandomVariable q : qrv ){ 
            visitaNodo(bn.getNode(q), nodeToSave, e);
        }
        return UtilsBayesNet.createBayNet(nodeToSave, bn);
    }
        
    public static BayesianNetwork prunningNodeAncestors(final RandomVariable[] qrv, final AssignmentProposition[] e, final BayesianNetwork bn){
         // aggiungo anche query e evidence nella lista ancestor perchè poi 
         // la uso come lista dei nodi che voglio tenere
        HashSet<RandomVariable> ancestor = new HashSet();
        for( RandomVariable q : qrv ){ 
            getAncestors(bn.getNode(q), ancestor);
            ancestor.add(q);
        }
        for ( AssignmentProposition e1 : e){
            getAncestors(bn.getNode(e1.getTermVariable()), ancestor);
            ancestor.add(e1.getTermVariable());
        }
        return UtilsBayesNet.createBayNet(ancestor, bn);
    }
    
    public static BayesNet prunningEdge(final RandomVariable[] qrv, final AssignmentProposition[] e, final BayesianNetwork bn){
        List<Node> newRoots = new ArrayList();
        HashMap<String,Node> newAddedNode = new HashMap();
        
        for(RandomVariable rv : bn.getVariablesInTopologicalOrder()){
            Node i =bn.getNode(rv);
            if(i.isRoot()){
                FullCPTNode n = new FullCPTNode( i.getRandomVariable(), ((CPT)i.getCPD()).getFactorFor(new AssignmentProposition[0]).getValues());
                newRoots.add(n);
                newAddedNode.put(n.getRandomVariable().getName(), n);
            }else{
                List<Node> parents = new ArrayList();
                List<AssignmentProposition> lostParents = new ArrayList();
                for (Node p : i.getParents()){
                    if(!checkEvidence(p, e))
                        parents.add(newAddedNode.get(p.getRandomVariable().getName()));
                    else
                        lostParents.add(getEvidence(p, e));
                }
                
                double[] normalizedArray = UtilsBayesNet.extractNormalizedProbability(i, lostParents.toArray(new AssignmentProposition[0]));
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
        return new BayesNet(newRoots.toArray(new Node[0])); 
    }
    
    
    private static void visitaNodo(Node nodo, HashSet<RandomVariable> nodeToSave, AssignmentProposition[] e){
        if(!checkEvidence(nodo, e) && !nodeToSave.contains(nodo.getRandomVariable())){ // non evidenza e non già visitato
            nodeToSave.add(nodo.getRandomVariable()); //segno come visitato
            // ricorsione sui fratelli
            for (Node son: nodo.getChildren()){
                for(Node sibling: son.getParents()){
                    visitaNodo(sibling, nodeToSave, e);
                }
            }
            //ricorsione sui genitori
            for (Node parent: nodo.getParents())
                visitaNodo(parent, nodeToSave, e
                );
        }else if(checkEvidence(nodo, e) && !nodeToSave.contains(nodo.getRandomVariable())){ //segno evidenza come visitata
            nodeToSave.add(nodo.getRandomVariable() ); //segno evidenza come visitata
        }
    }    
   
    private static boolean checkEvidence(Node nodo, AssignmentProposition[] e){
        boolean result = false; 
        for( AssignmentProposition a: e){
            if(a.getTermVariable().equals(nodo.getRandomVariable()))
                result = true; 
        }
        return result;
    }
    
    private static AssignmentProposition getEvidence(Node nodo, AssignmentProposition[] e){
        boolean result = false; 
        for( AssignmentProposition a: e){
            if(a.getTermVariable().equals(nodo.getRandomVariable()))
                return a; 
        }
        return null;
    }
    
    private static void getAncestors(Node n, HashSet<RandomVariable> hs){
        for( Node father : n.getParents()){
            hs.add(father.getRandomVariable());
            getAncestors(father,hs);
        } 
    }      
}
