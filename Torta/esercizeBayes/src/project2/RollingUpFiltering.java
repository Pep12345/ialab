/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project2;

import project1.EliminationDarwiche;
import project1.Order;
import aima.core.probability.Factor;
import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.DynamicBayesianNetwork;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.probability.util.ProbabilityTable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import utilsBayesNet.UtilsBayesNet;

/**
 *
 * @author Biondi Giuseppe
 */
public class RollingUpFiltering {
    
    private static final ProbabilityTable _identity = new ProbabilityTable(
                    new double[] { 1.0 });  
    private DynamicBayesianNetwork dbn;
    private List<Factor> factorsFromPreviousStep;
    public static final int REVERSE_ORDER = 0;
    public static final int MIN_FILL_ORDER = 1;
    public static final int MIN_DEGREE_ORDER = 2;
    
    public RollingUpFiltering(DynamicBayesianNetwork dbn){
        this.dbn = dbn;
        factorsFromPreviousStep = new ArrayList();
    }
    
    public ProbabilityTable rollUp(AssignmentProposition[] evidences, int order){
        
        List<RandomVariable> orderList = chooseOrder(order);
        EliminationDarwiche edd = new EliminationDarwiche(orderList);
        
        RandomVariable[] query = dbn.getX_1().toArray(new RandomVariable[0]);
        
        List<Factor> newFactors = edd.dynamicEliminationAsk(query, evidences, dbn, factorsFromPreviousStep);
        factorsFromPreviousStep.clear();
        newFactors.forEach(f->{
            List<RandomVariable> prec = new ArrayList();
            f.getArgumentVariables().forEach(rv-> prec.add(dbn.getX_1_to_X_0().get(rv)));
            factorsFromPreviousStep.add(new ProbabilityTable(f.getValues(), prec.toArray(new RandomVariable[0])));
        });
        
        //printInfo(newFactors);
        
        Factor product = UtilsBayesNet.pointwiseProduct(newFactors);
        return ((ProbabilityTable) product.pointwiseProductPOS(_identity, query)).normalize();        
    }   
    
    private List<RandomVariable> chooseOrder(int order){
        Order or = new Order(dbn);
         switch(order){
            case REVERSE_ORDER:
                return or.reverseTopologicalOrder();    
            case MIN_FILL_ORDER:
                return or.minFillOrder();
            case MIN_DEGREE_ORDER:
                return or.minDegreeOrder();  
            default:
                return dbn.getVariablesInTopologicalOrder();
        }
    }
    
    private void printInfo(List<Factor> newFactors){
        
        System.out.println(newFactors.get(0).getArgumentVariables());
        Factor printProduct = UtilsBayesNet.pointwiseProduct(newFactors);
        System.out.println( Arrays.toString(UtilsBayesNet.normalizeFactorArray(printProduct, printProduct.getValues().length)));
        newFactors.forEach(f -> System.out.println(f.getArgumentVariables()+ "  "+ f));

    }
}
