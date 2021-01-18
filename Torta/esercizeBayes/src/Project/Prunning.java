/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Project;

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
    public void prunningNodeMSeparated(){
        HashSet<RandomVariable> nodeToSave = new HashSet();
        for( RandomVariable q : qrv ){ 
            visitaNodo(bn.getNode(q), nodeToSave);

        }
        System.out.println(nodeToSave);
        //creare la nuova rete b
        
    }
    
    private void visitaNodo(Node nodo, HashSet<RandomVariable> nodeToSave){
        if(!checkEvidence(nodo) && !nodeToSave.contains(nodo.getRandomVariable())){ // non evidenza e non già visitato
            nodeToSave.add(nodo.getRandomVariable()); //segno come visitato
            System.out.println("visitato: " + nodo.getRandomVariable());
            // ricorsione sui fratelli
            for (Node son: nodo.getChildren()){
                for(Node sibling: son.getParents()){
                    System.out.println("vado nel sibling: " + sibling.getRandomVariable());
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
    
    public BayesianNetwork prunningNodeAncestors(){
        // estraggo ancestor
        HashSet<RandomVariable> ancestor = new HashSet();
        HashSet<RandomVariable> queries = new HashSet();
        HashSet<RandomVariable> evidence = new HashSet();
        
        for( RandomVariable q : qrv ){ //per ogni randomvariable in query
            getAncestors(bn.getNode(q), ancestor);
            queries.add(q);
        }
        // aggiunto evidenze in ancestor - TODO: modificare libreria per estrarne la randomvariable
        for ( AssignmentProposition e1 : e){
            getAncestors(bn.getNode(e1.getTermVariable()), ancestor); // aggiunge i padri dell'evidenza
            evidence.add(e1.getTermVariable()); // aggiunge l'evidenza
        }
        
        
        List<Node> newRoots = new ArrayList();
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
        
        return bayNet;
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
    
    private List<Node> getRoots(BayesianNetwork bn){  // modificare libreria per aggiungere get roots nella classe bayesnet
        List<Node> roots = new ArrayList();
        for (RandomVariable rv : bn.getVariablesInTopologicalOrder()) {           
            Node node =bn.getNode(rv);
            if(node.isRoot()) // ancestors.contains(node.getRandomVariable())
                roots.add(node);
        }
        return roots;
    }
}
