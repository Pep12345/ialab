/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Project;

import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.bayes.Node;
import java.util.HashSet;
import java.util.Iterator;
import java.util.*;

/**
 *
 * @author Biondi Giuseppe
 */
public class Order {
    private BayesianNetwork bn;
    
    public Order(final BayesianNetwork bn){
        this.bn = bn;     
    }
    
    public List<RandomVariable> reverseTopologicalOrder(){
        List<RandomVariable> o = new ArrayList();
        bn.getVariablesInTopologicalOrder().forEach(n-> o.add(n));
        return o;
    }
    
    
    public List<RandomVariable> minDegreeOrder(){
        return order(0);
    }

    public List<RandomVariable> minFillOrder(){
        return order(1);
    }    
    
    private List<RandomVariable> order(int n){
        Graph<RandomVariable> bnGraph = createGraph(bn);
        List<RandomVariable> resultList = new ArrayList();
        int numberOfRandomVariable = bn.getVariablesInTopologicalOrder().size();
        for(int i=0; i<numberOfRandomVariable;i++){
            RandomVariable rv = null;
            if(n ==0)
                rv = getMinDegreeRandomVariable(bnGraph);
            else if (n==1)
                rv = getMinFillRandomVariable(bnGraph);
            assert(rv != null);
            bnGraph.marryAdiacentNode(rv);
            bnGraph.delete(rv);
            resultList.add(rv);
        }
        return resultList;
    }
    
    private RandomVariable getMinFillRandomVariable(Graph<RandomVariable> bnGraph) {
        Iterator<RandomVariable> it = bnGraph.iterator();
        RandomVariable minNode = it.next();
        int minEdge = bnGraph.numberOfPossibleMarryEdge(minNode);
        while(it.hasNext()){
            RandomVariable rv = it.next();
            if(minEdge > bnGraph.numberOfPossibleMarryEdge(rv)){
                minNode = rv;
                minEdge = bnGraph.numberOfPossibleMarryEdge(rv);
            }
        }
        return minNode;
    }
    
    private RandomVariable getMinDegreeRandomVariable(Graph<RandomVariable> bnGraph){
        Iterator<RandomVariable> it = bnGraph.iterator();
        RandomVariable minNode = it.next();
        int minEdge = bnGraph.getNumEdges(minNode);
        while(it.hasNext()){
            RandomVariable rv = it.next();
            if(minEdge > bnGraph.getNumEdges(rv)){
                minNode = rv;
                minEdge = bnGraph.getNumEdges(rv);
            }
        }
        return minNode;
    }
    
    private Graph<RandomVariable> createGraph(BayesianNetwork bn){
        
        Graph<RandomVariable> graph = new Graph<>();
        for(RandomVariable rv : bn.getVariablesInTopologicalOrder()){
            graph.addVertex(rv);
            Node n = bn.getNode(rv);
            for(Node parent : n.getParents())
                graph.addEdge(rv, parent.getRandomVariable());
        }
        return graph;
    }
    
}

// based on the work of Robert Sedgewick and Kevin Wayne
class Graph<T> implements Iterable<T> {
    private Map<T, Set<T>> verticesMap;

    private int edgesCount;

    public Graph() {
        verticesMap = new HashMap<>();
    }

    public int getNumVertices() {
        return verticesMap.size();
    }

    public int getNumEdges() {
        return edgesCount;
    }

    public int getNumEdges(T node){
        return verticesMap.get(node).size();
    }

    private void validateVertex(T v) {
        if (!hasVertex(v)) throw new IllegalArgumentException(v.toString() + " is not a vertex");
    }

    public int degree(T v) {
        validateVertex(v);
        return verticesMap.get(v).size();
    }

    public void addEdge(T v, T w) {
        if (!hasVertex(v)) addVertex(v);
        if (!hasVertex(w)) addVertex(w);
        if (!hasEdge(v, w)) edgesCount++;
        verticesMap.get(v).add(w);
        verticesMap.get(w).add(v);
    }

    public void addVertex(T v) {
        if (!hasVertex(v)) verticesMap.put(v, new HashSet<T>());
    }

    public boolean hasEdge(T v, T w) {
        validateVertex(v);
        validateVertex(w);
        return verticesMap.get(v).contains(w);
    }

    public boolean hasVertex(T v) {
        return verticesMap.containsKey(v);
    }

    @Override
    public Iterator<T> iterator() {
        return verticesMap.keySet().iterator();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (T v: verticesMap.keySet()) {
            builder.append(v.toString() + ": ");
            for (T w: verticesMap.get(v)) {
                builder.append(w.toString() + " ");
            }
            builder.append("\n");
        }

        return builder.toString();
    }
    
    int numberOfPossibleMarryEdge(T element){
        //Iterator<T> it1 = verticesMap.get(element).iterator();
        int result = 0;
        T[] adiacents = (T[]) verticesMap.get(element).toArray();
        for(int i=0; i<adiacents.length-1; i++){
            T node1 = adiacents[i];
            for(int j=i+1; j<adiacents.length; j++){
                T node2 = adiacents[j];
                if(!hasEdge(node1,node2))
                    result++;
            }
        }
        return result;
    }
    

    void marryAdiacentNode(T element) {
        Iterator<T> it1 = verticesMap.get(element).iterator();
        while(it1.hasNext()){
            T node1 = it1.next();
            Iterator<T> it2 = verticesMap.get(element).iterator();
            while(it2.hasNext()){
                T node2 = it2.next();
                if(node1 != node2)
                    this.addEdge(node1, node2);
            }
        }
    }

    void delete(T element) {
        Set<T> adiacents = verticesMap.get(element);
        Iterator<T> iterator = adiacents.iterator();
        while(iterator.hasNext()){
            T node = iterator.next();
            verticesMap.get(node).remove(element);
        }
        verticesMap.remove(element);
    }
    
    
}
