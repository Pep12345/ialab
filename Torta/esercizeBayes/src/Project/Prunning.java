/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Project;

import aima.core.probability.Factor;
import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.BayesianNetwork;
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
import java.util.Set;

/**
 *
 * @author Biondi Giuseppe
 */
public class Prunning {
    private RandomVariable[] qrv;
    private AssignmentProposition[] e;
    private BayesianNetwork bn;

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

     // ricorsivamente per ogni nodo prendo i figli e i parent dei figli e poi chiamo ricorisvamente
    // su parent nodo e parent figli
    public BayesNet prunningNodeMSeparated(){
        HashSet<RandomVariable> nodeToSave = new HashSet();
        for( RandomVariable q : qrv ){ 
            visitaNodo(bn.getNode(q), nodeToSave);
        }
        return createBayNet(nodeToSave);
    }
        
    public BayesianNetwork prunningNodeAncestors(){
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
        return createBayNet(ancestor);
    }
    
    public BayesNet prunningEdge(){
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
                List<RandomVariable> lostParents = new ArrayList();
                for (Node p : i.getParents()){
                    if(!checkEvidence(p))
                        parents.add(newAddedNode.get(p.getRandomVariable().getName()));
                    else
                        lostParents.add(p.getRandomVariable());
                }

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
        return new BayesNet(newRoots.toArray(new Node[0])); 
    }
    
    
    private void visitaNodo(Node nodo, HashSet<RandomVariable> nodeToSave){
        if(!checkEvidence(nodo) && !nodeToSave.contains(nodo.getRandomVariable())){ // non evidenza e non già visitato
            nodeToSave.add(nodo.getRandomVariable()); //segno come visitato
            // ricorsione sui fratelli
            for (Node son: nodo.getChildren()){
                for(Node sibling: son.getParents()){
                    visitaNodo(sibling, nodeToSave);
                }
            }
            //ricorsione sui genitori
            for (Node parent: nodo.getParents())
                visitaNodo(parent, nodeToSave);
        }else if(checkEvidence(nodo) && !nodeToSave.contains(nodo.getRandomVariable())){ //segno evidenza come visitata
            nodeToSave.add(nodo.getRandomVariable() ); //segno evidenza come visitata
        }
    }    
    
    private BayesNet createBayNet(HashSet<RandomVariable> acceptedNodeInNewNetwork){
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
    
    private boolean checkEvidence(Node nodo){
        boolean result = false; 
        for( AssignmentProposition a: e){
            if(a.getTermVariable().equals(nodo.getRandomVariable()))
                result = true; 
        }
        return result;
    }
    
    private void getAncestors(Node n, HashSet<RandomVariable> hs){
        for( Node father : n.getParents()){
            hs.add(father.getRandomVariable());
            getAncestors(father,hs);
        } 
    }      
    
    // modificare libreria per aggiungere get roots nella classe bayesnet
    private List<Node> getRoots(BayesianNetwork bn){  
        List<Node> roots = new ArrayList();
        for (RandomVariable rv : bn.getVariablesInTopologicalOrder()) {           
            Node node =bn.getNode(rv);
            if(node.isRoot()) // ancestors.contains(node.getRandomVariable())
                roots.add(node);
        }
        return roots;
    }
   
    // ricalcolo tabella togliendo lost parents e restituisco array di probabilità
    private double[] extractNormalizedProbability(Node i, List<RandomVariable> lostParents) {
        Factor f = ((CPT)i.getCPD()).getFactorFor(new AssignmentProposition[0]);
        f = f.sumOut(lostParents.toArray(new RandomVariable[0]));

        return normalizeFactorArray(f,i.getRandomVariable().getDomain().size());
    }
    
    // dato un array lo normalizza ogni n valori
    // es. n = 2 -> [2,2,4,8] -> [0.5,0.5,0.33,0.66]
    private double[] normalizeFactorArray(Factor f, int n){
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
}







 //creo rete bayesiana
        /*List<Node> newRoots = new ArrayList();
        List<Node> nodeToVisit = new ArrayList();
        HashMap<String,Node> newNodeAdded = new HashMap();  // mappa coi nuovi fullcpt node da usare come parent
        
        // creo i nuovi nodi delle radici e aggiungo i figli in nodetovisit
        for( Node root : getRoots(bn)){
            if(ancestor.contains(root.getRandomVariable())){   // se la radice fa parte degli antenati
                //FullCPTNode n = new FullCPTNode( root.getRandomVariable(), ((CPT)root.getCPD()).getValues());
                FullCPTNode n = new FullCPTNode( root.getRandomVariable(), ((CPT)root.getCPD()).getFactorFor(new AssignmentProposition[0]).getValues());
                newRoots.add(n); //salvo la radice per creare la nuova rete
                nodeToVisit.addAll(root.getChildren()); // aggiungo i figli da visitare
                newNodeAdded.put(n.getRandomVariable().getName(), n); // aggiungo la radice nei nuovi nodi 
            }
        }
        
        for( int j=0; j<nodeToVisit.size(); j++ ){
            Node i = nodeToVisit.get(j);
            if(!newNodeAdded.containsKey(i.getRandomVariable().getName())){ // per evitare di ripassarci
                if(ancestor.contains(i.getRandomVariable()) 
                        || queries.contains(i.getRandomVariable())
                            || evidence.contains(i.getRandomVariable())) {   // se fa parte degli antenati
                    
                    // estraiamo padri mettendo i nodi nuovi della rete al loro posto
                    List<Node> parents = new ArrayList();
                    for (Node p : i.getParents()){
                        parents.add(newNodeAdded.get(p.getRandomVariable().getName()));
                    }
                    
                    FullCPTNode n = new FullCPTNode( i.getRandomVariable(), ((CPT)i.getCPD()).getValues(), parents.toArray(new Node[0]));
                    newNodeAdded.put(n.getRandomVariable().getName(), n);
                    nodeToVisit.addAll(i.getChildren());
                }
            }
        }
        BayesNet bayNet = new BayesNet(newRoots.toArray(new Node[0]));
        System.out.println(bayNet.getVariablesInTopologicalOrder());
        
        return bayNet;*/