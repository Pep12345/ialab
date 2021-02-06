/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Project2;

import Project2.test.tst;
import Project.EliminationDarwiche;
import Project.Order;
import aima.core.probability.CategoricalDistribution;
import aima.core.probability.Factor;
import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.DynamicBayesianNetwork;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.bayes.DynamicBayesianNetwork;
import aima.core.probability.bayes.FiniteNode;
import aima.core.probability.bayes.Node;
import aima.core.probability.bayes.approx.BayesSampleInference;
import aima.core.probability.bayes.approx.RejectionSampling;
import aima.core.probability.bayes.impl.BayesNet;
import aima.core.probability.bayes.impl.CPT;
import aima.core.probability.bayes.impl.FullCPTNode;
import aima.core.probability.domain.BooleanDomain;
import aima.core.probability.example.ExampleRV;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.probability.util.ProbabilityTable;
import aima.core.probability.util.RandVar;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Biondi Giuseppe
 */
public class UmbrellaExample {
    
    public static DynamicBayesianNetwork getExample(){
FiniteNode prior_rain_tm1 = new FullCPTNode(ExampleRV.RAIN_tm1_RV,
                new double[] { 0.5, 0.5 });

                // tm1, t, t1, t2
                //      u, u1, u2
        BayesNet priorNetwork = new BayesNet(prior_rain_tm1);

        // Prior belief state
        FiniteNode rain_tm1 = new FullCPTNode(ExampleRV.RAIN_tm1_RV,
                new double[] { 0.5, 0.5 });
        // Transition Model
            FiniteNode rain_t = new FullCPTNode(ExampleRV.RAIN_t_RV, new double[] {
                // R_t-1 = true, R_t = true
                0.7,
                // R_t-1 = true, R_t = false
                0.3,
                // R_t-1 = false, R_t = true
                0.3,
                // R_t-1 = false, R_t = false
                0.7 }, rain_tm1);
        // Sensor Model
        @SuppressWarnings("unused")
        FiniteNode umbrealla_t = new FullCPTNode(ExampleRV.UMBREALLA_t_RV,
                new double[] {
                        // R_t = true, U_t = true
                        0.9,
                        // R_t = true, U_t = false
                        0.1,
                        // R_t = false, U_t = true
                        0.2,
                        // R_t = false, U_t = false
                        0.8 }, rain_t);

        Map<RandomVariable, RandomVariable> X_0_to_X_1 = new HashMap<RandomVariable, RandomVariable>();
        Map<RandomVariable, RandomVariable> X_1_to_X_0 = new HashMap<RandomVariable, RandomVariable>();
        X_0_to_X_1.put(ExampleRV.RAIN_tm1_RV, ExampleRV.RAIN_t_RV);
        X_1_to_X_0.put(ExampleRV.RAIN_t_RV, ExampleRV.RAIN_tm1_RV);
        Set<RandomVariable> E_1 = new HashSet<RandomVariable>();
        E_1.add(ExampleRV.UMBREALLA_t_RV);
        // Transition Model

        RandVar RAIN_t1_RV = new RandVar("Rain_t1",new BooleanDomain());
        FiniteNode rain_t1 = new FullCPTNode(RAIN_t1_RV, new double[] {
                // R_t-1 = true, R_t = true
                0.7,
                // R_t-1 = true, R_t = false
                0.3,
                // R_t-1 = false, R_t = true
                0.3,
                // R_t-1 = false, R_t = false
                0.7 }, rain_t);
        // Sensor Model
        @SuppressWarnings("unused")
        RandVar UMBREALLA_t1_RV = new RandVar("UMBREALLA_t1",new BooleanDomain());
        FiniteNode umbrealla_t1 = new FullCPTNode(UMBREALLA_t1_RV,
                new double[] {
                        // R_t = true, U_t = true
                        0.9,
                        // R_t = true, U_t = false
                        0.1,
                        // R_t = false, U_t = true
                        0.2,
                        // R_t = false, U_t = false
                        0.8 }, rain_t1);
        X_0_to_X_1.put(ExampleRV.RAIN_t_RV, RAIN_t1_RV);
        X_1_to_X_0.put(RAIN_t1_RV, ExampleRV.RAIN_t_RV);
        E_1.add(UMBREALLA_t1_RV);

        // Transition Model
        RandVar RAIN_t2_RV = new RandVar("Rain_t2",new BooleanDomain());
        FiniteNode rain_t2 = new FullCPTNode(RAIN_t2_RV, new double[] {
                // R_t-1 = true, R_t = true
                0.7,
                // R_t-1 = true, R_t = false
                0.3,
                // R_t-1 = false, R_t = true
                0.3,
                // R_t-1 = false, R_t = false
                0.7 }, rain_t1);
        // Sensor Model
        @SuppressWarnings("unused")
        RandVar UMBREALLA_t2_RV = new RandVar("UMBREALLA_t2",new BooleanDomain());
        FiniteNode umbrealla_t2 = new FullCPTNode(UMBREALLA_t2_RV,
                new double[] {
                        // R_t = true, U_t = true
                        0.9,
                        // R_t = true, U_t = false
                        0.1,
                        // R_t = false, U_t = true
                        0.2,
                        // R_t = false, U_t = false
                        0.8 }, rain_t2);
        X_0_to_X_1.put(RAIN_t1_RV, RAIN_t2_RV);
        X_1_to_X_0.put(RAIN_t2_RV, RAIN_t1_RV);
        E_1.add(UMBREALLA_t2_RV);

        return new aima.core.probability.bayes.impl.DynamicBayesNet(priorNetwork, X_0_to_X_1, E_1, rain_tm1);
    }
    
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

    private static final ProbabilityTable _identity = new ProbabilityTable(
			new double[] { 1.0 });    
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
        BayesNet bn = createBayNet(hs, dbn);
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
        Factor printProduct = pointwiseProduct(factorsFromPreviousStep);
        System.out.println( Arrays.toString(normalizeFactorArray(printProduct, printProduct.getValues().length)));
        factorsFromPreviousStep.forEach(f -> System.out.println(f.getArgumentVariables()+ "  "+ f));
        
        
        System.out.println("\n\n");
        if(stop){
            Factor product = pointwiseProduct(factorsFromPreviousStep);
            return ((ProbabilityTable) product.pointwiseProductPOS(_identity, query)).normalize();
        }else
            return rollUp(dbn, query, ev, next, factorsFromPreviousStep);
        
    }
    
    public static void main(String[] args){
        //creo rete
        DynamicBayesianNetwork example = tst.getRainWindNet();
        
        //salvo variabili
        HashMap<String, RandomVariable> bnRV = new HashMap();
        example.getVariablesInTopologicalOrder().forEach(v -> bnRV.put(v.getName(), v));
        System.out.println("Variabili slice 0: "+ example.getPriorNetwork().getVariablesInTopologicalOrder());
        
        //creo query finale
        RandomVariable[] query = {bnRV.get("Rain_t")};
        
        //creo lista evidenze
        List<AssignmentProposition> ev = new ArrayList();
        ev.add(new AssignmentProposition(bnRV.get("Umbrella_t"), Boolean.TRUE));
       // ev.add(new AssignmentProposition(bnRV.get("UMBREALLA_t1"), Boolean.TRUE));
       // ev.add(new AssignmentProposition(bnRV.get("UMBREALLA_t2"), Boolean.TRUE));
        
        //avvio rolling up
        ProbabilityTable rollUp = rollUp(example, query, ev, example.getPriorNetwork().getVariablesInTopologicalOrder(), new ArrayList());
        System.out.println(rollUp);   
        
    }
    
    
    
    
      
    private static BayesNet createBayNet(HashSet<RandomVariable> acceptedNodeInNewNetwork, BayesianNetwork bn){
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
    private static double[] extractNormalizedProbability(Node i, List<RandomVariable> lostParents) {
        Factor f = ((CPT)i.getCPD()).getFactorFor(new AssignmentProposition[0]);
        f = f.sumOut(lostParents.toArray(new RandomVariable[0]));

        return normalizeFactorArray(f,i.getRandomVariable().getDomain().size());
    }
    
    // dato un array lo normalizza ogni n valori
    // es. n = 2 -> [2,2,4,8] -> [0.5,0.5,0.33,0.66]
    private static double[] normalizeFactorArray(Factor f, int n){
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
    
    private static Factor pointwiseProduct(List<Factor> factors) {

            Factor product = factors.get(0);
            for (int i = 1; i < factors.size(); i++) {
                    product = product.pointwiseProduct(factors.get(i));
            }

            return product;
    }
    
}
