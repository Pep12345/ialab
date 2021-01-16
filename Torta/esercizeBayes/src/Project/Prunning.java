/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Project;

import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.bayes.Node;
import aima.core.probability.bayes.impl.FullCPTNode;
import aima.core.probability.proposition.AssignmentProposition;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author Biondi Giuseppe
 */
public class Prunning {
    RandomVariable[] qrv;
    AssignmentProposition[] e;
    BayesianNetwork bn;

    public RandomVariable[] getQrv() {
        return qrv;
    }

    public void setQrv(RandomVariable[] qrv) {
        this.qrv = qrv;
    }

    public AssignmentProposition[] getE() {
        return e;
    }

    public void setE(AssignmentProposition[] e) {
        this.e = e;
    }

    public BayesianNetwork getBn() {
        return bn;
    }

    public void setBn(BayesianNetwork bn) {
        this.bn = bn;
    }
    
    public Prunning(final RandomVariable[] qrv, final AssignmentProposition[] e, final BayesianNetwork bn){
        this.qrv = qrv;
        this.e = e;
        this.bn = bn;
    }
    
    /*public BayesianNetwork prunningNodeTh1(){
        BayesianNetwork bn1 = null;
        HashSet<Node> ancestor = new HashSet();
        FullCPTNode s;
        
        for( RandomVariable q : qrv ){ //per ogni randomvariable in query
            getAncestors(bn.getNode(q), ancestor);
        }
        
        /*for ( AssignmentProposition e1 : e){
            ancestor.add(bn.getNode(e1.getRandomVariable));
        }*/
        bn.
        /*for (RandomVariable i : bn.getVariablesInTopologicalOrder()){
            if(!ancestor.contains(i))
                bn.getNode(i); //elimina nodo[ bn.getNode(i)]            
        }*/
    /*    return bn1;
    }
    
    private void getAncestors(Node n, HashSet<Node> hs){
        for( Node father : n.getParents()){
            hs.add(father);
            father.
            getAncestors(father,hs);
        } 
    }*/
    
    public BayesianNetwork prunningNodeTh1(){
        BayesianNetwork bn1 = new B;
        HashSet<RandomVariable> ancestor = new HashSet();
        
        for( RandomVariable q : qrv ){ //per ogni randomvariable in query
            getAncestors(bn.getNode(q), ancestor);
        }
        /*for ( AssignmentProposition e1 : e){
        ancestor.add(e1.getRandomVarable);
        }*/
        
        return bn1;
    }
    
    private void getAncestors(Node n, HashSet<RandomVariable> hs){
        for( Node father : n.getParents()){
            hs.add(father.getRandomVariable());
            getAncestors(father,hs);
        } 
    }      
    
    private List<Node> getRoots(BayesianNetwork bn){
        List<Node> roots = new ArrayList();
        for (RandomVariable rv : bn.getVariablesInTopologicalOrder()) {           
            Node node =bn.getNode(rv);
            if(node.isRoot())
                roots.add(node);
        }
        return roots;
    }
    
    private List<Node> getootsRoots(BayesianNetwork bn, HashSet<RandomVariable> ancestors){
        List<Node> roots = new ArrayList();
        for (RandomVariable rv : bn.getVariablesInTopologicalOrder()) {    
            Node node =bn.getNode(rv);
            if(node.isRoot() && ancestors.contains(node.getRandomVariable()))
                node.
                roots.add(node);
        }
        return roots;
    }
}
