/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esercizebayes;

import aima.core.probability.Factor;
import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.bayes.FiniteNode;
import aima.core.probability.bayes.Node;
import aima.core.probability.bayes.impl.BayesNet;
import aima.core.probability.bayes.impl.CPT;
import aima.core.probability.bayes.impl.FullCPTNode;
import aima.core.probability.domain.BooleanDomain;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.probability.util.RandVar;
import bnparser.BifReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author giuV3
 */
public class esInfApprossimata {
        public static void main(String[] args) throws CloneNotSupportedException {
        BayesianNetwork bn = BifReader.readBIF("Sprinkler.xml");
        Node node = bn.getNode(new RandVar("Rain",new BooleanDomain()));
        CPT cpt = (CPT)node.getCPD();
       
        System.out.println(cpt.getFor());
        RandVar ev = new RandVar("Cloudy", new BooleanDomain());
        System.out.println(bn.getNode(ev).getRandomVariable().getDomain());
        AssignmentProposition[] as = {};
        Factor f = cpt.getFactorFor(new AssignmentProposition[0]);
        System.out.println(f);
        for(double d : cpt.getValues())
            System.out.print(" " +d);
        RandomVariable[] query = {new RandVar("Cloudy", new BooleanDomain())};
        Factor sumOut = f.sumOut(query);
        System.out.println(sumOut.getArgumentVariables());
        for(double d : sumOut.getValues())
            System.out.print(" " +d);
        return;
        /*List<RandomVariable> rvs = bn.getVariablesInTopologicalOrder();
        List<FullCPTNode> roots = new ArrayList();
        for (RandomVariable rv :rvs) {           
            FullCPTNode node = (FullCPTNode) bn.getNode(rv);
            if(node.isRoot())
                roots.add(node);
        }
        FullCPTNode[] array = new FullCPTNode[roots.size()];
        for(int i = 0; i< array.length;i++){
            array[i] = roots.get(i);
        }
        BayesNet bayNet = new BayesNet(array);
        Node children1 = array[0].getChildren().iterator().next();
        List<Node> ss = new ArrayList();
        for (Node p : array[0].getParents()){
            if(!"Sprintler".equals(p.getRandomVariable().getName()))
                ss.add(p);
        }
        Node[] nodeArray = ss.toArray(new Node[0]);
        Node n = new FullCPTNode(array[0].getRandomVariable(), ((CPT)array[0].getCPT()).getValues(), nodeArray);
        
*/  
        /*List<RandomVariable> rvs = bn.getVariablesInTopologicalOrder();
        List<FullCPTNode> roots = new ArrayList();
        for (RandomVariable rv :rvs) {           
            FullCPTNode node = (FullCPTNode) bn.getNode(rv);
            List<Node> parents = new ArrayList();
            for (Node p : node.getParents()){
                if(!"Rain".equals(p.getRandomVariable().getName()))
                    parents.add(p);
            }
            if (!"Rain".equals(rv.getName())){
                new FullCPTNode(rv, ((CPT)node.getCPT()).getValues(), parents.toArray(new Node[0]));
                if(node.isRoot())
                    roots.add(node);
            }  
        }*/
        Node query = bn.getNode(new RandVar("Road", new BooleanDomain()));
        HashSet<RandomVariable> hs = new HashSet();
        getAncestors(query, hs);
        FullCPTNode n = null;
        List<Node> newRoots = new ArrayList();
        
        List<Node> nodeToVisit = new ArrayList();
        HashMap<String,Node> giaAggiunti = new HashMap();
        
        for( Node root : getRoots(bn)){
            if(hs.contains(root.getRandomVariable())){   // se la radice fa parte degli antenati
                n = new FullCPTNode( root.getRandomVariable(), ((CPT)root.getCPD()).getValues());
                newRoots.add(n);
                nodeToVisit.addAll(root.getChildren());
                giaAggiunti.put(n.getRandomVariable().getName(), n);
            }
        }
        
        for( int j=0; j<nodeToVisit.size(); j++ ){
            Node i = nodeToVisit.get(j);
            if(!giaAggiunti.containsKey(i.getRandomVariable().getName())){ // per evitare di ripassarci
                if(hs.contains(i.getRandomVariable()) || i.getRandomVariable().getName() == query.getRandomVariable().getName()) {   // se fa parte degli antenati
                    
                    System.out.println(i.getRandomVariable());
                    // estraiamo padri mettendo i nodi nuovi della rete al loro posto
                    List<Node> parents = new ArrayList();
                    for (Node p : i.getParents()){
                        parents.add(giaAggiunti.get(p.getRandomVariable().getName()));
                    }
                    
                    n = new FullCPTNode( i.getRandomVariable(), ((CPT)i.getCPD()).getValues(), parents.toArray(new Node[0]));
                    giaAggiunti.put(n.getRandomVariable().getName(), n);
                    nodeToVisit.addAll(i.getChildren());
                }
            }
        }
        System.out.println(giaAggiunti);
        BayesNet bayNet = new BayesNet(newRoots.toArray(new Node[0]));
        System.out.println(bayNet.getVariablesInTopologicalOrder());
        
        
        /*for( RandomVariable parent : hs){
            System.out.println(parent.getName());
            Node p = bn.getNode(parent);
            FullCPTNode nn = new FullCPTNode( p.getRandomVariable(), ((CPT)p.getCPD()).getValues(), p.getParents().toArray(new Node[0]));
            System.out.println(nn.getParents());
            System.out.println(nn.getChildren());
            if(p.isRoot())
                roots.add(nn);
        }*/
        }  
        
        
    private static void getAncestors(Node n, HashSet<RandomVariable> hs){
        for( Node father : n.getParents()){
            hs.add(father.getRandomVariable());
            getAncestors(father,hs);
        } 
    }
        
    private static List<Node> getRoots(BayesianNetwork bn){
        List<Node> roots = new ArrayList();
        for (RandomVariable rv : bn.getVariablesInTopologicalOrder()) {           
            Node node =bn.getNode(rv);
            if(node.isRoot())
                roots.add(node);
        }
        return roots;
    }
}
