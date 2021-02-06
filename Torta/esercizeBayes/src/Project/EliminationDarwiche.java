/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Project;

import aima.core.probability.CategoricalDistribution;
import aima.core.probability.Factor;
import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.bayes.ConditionalProbabilityDistribution;
import aima.core.probability.bayes.FiniteNode;
import aima.core.probability.bayes.Node;
import aima.core.probability.bayes.exact.EliminationAsk;
import aima.core.probability.bayes.impl.BayesNet;
import aima.core.probability.bayes.impl.CPT;
import aima.core.probability.bayes.impl.FullCPTNode;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.probability.util.ProbabilityTable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author Biondi Giuseppe
 */
public class EliminationDarwiche extends EliminationAsk {
    private static final ProbabilityTable _identity = new ProbabilityTable(
			new double[] { 1.0 });
    List<RandomVariable> order = new ArrayList();
    
    public EliminationDarwiche(List<RandomVariable> order){
        super();
        this.order = order;
    }
    
    public List<RandomVariable> getOrder() {
        return order;
    }

    public void setOrder(List<RandomVariable> order) {
        this.order = order;
    }
    
    @Override 
    public CategoricalDistribution eliminationAsk(final RandomVariable[] Query,
			final AssignmentProposition[] e, final BayesianNetwork bn) {
        
        // creo S
        List<Factor> factors = new ArrayList();
        bn.getVariablesInTopologicalOrder().forEach(var -> factors.add(makeFactor(var,e,bn)));
        
        //rimuovo query da order list
        for(RandomVariable q: Query)
            order.remove(q);
        
        //ciclo 
        for(RandomVariable var: order){
            List<Factor> toMultiply = new ArrayList();
            //cerco le tab da moltiplicare
            for(Factor f: factors){
                if(f.getArgumentVariables().contains(var))
                    toMultiply.add(f);
            }
            // moltiplico e sommo
            if(toMultiply.size()>0){
                Factor f = pointwiseProduct(toMultiply).sumOut(var);
                factors.removeAll(toMultiply);
                factors.add(f);
            }
        }
        
        Factor product = pointwiseProduct(factors);
        return ((ProbabilityTable) product.pointwiseProductPOS(_identity, Query)).normalize();
    }

    public List<Factor> dynamicEliminationAsk(final RandomVariable[] Query,
			final AssignmentProposition[] e, final BayesianNetwork bn,
                        List<Factor> prevStep) {
        
        // creo S
        List<Factor> factors = new ArrayList();
        bn.getVariablesInTopologicalOrder().forEach(var -> factors.add(makeFactor(var,e,bn)));
        factors.addAll(prevStep);
        
        //rimuovo query da order list
        for(RandomVariable q: Query)
            order.remove(q);
        
        //ciclo 
        for(RandomVariable var: order){
            List<Factor> toMultiply = new ArrayList();
            //cerco le tab da moltiplicare
            for(Factor f: factors){
                if(f.getArgumentVariables().contains(var))
                    toMultiply.add(f);
            }
            // moltiplico e sommo
            if(toMultiply.size()>0){
                Factor f = pointwiseProduct(toMultiply).sumOut(var);
                factors.removeAll(toMultiply);
                factors.add(f);
            }
        }

        return factors;
    }
    
    
    private Factor makeFactor(RandomVariable var, AssignmentProposition[] e,
                BayesianNetwork bn) {

        Node n = bn.getNode(var);
        if (!(n instanceof FiniteNode)) {
                throw new IllegalArgumentException(
                                "Elimination-Ask only works with finite Nodes.");
        }
        FiniteNode fn = (FiniteNode) n;
        List<AssignmentProposition> evidence = new ArrayList<AssignmentProposition>();
        for (AssignmentProposition ap : e) {
                if (fn.getCPT().contains(ap.getTermVariable())) {
                        evidence.add(ap);
                }
        }

        return fn.getCPT().getFactorFor(
                        evidence.toArray(new AssignmentProposition[evidence.size()]));
    }

    private List<Factor> sumOut(RandomVariable var, List<Factor> factors,
                    BayesianNetwork bn) {
            List<Factor> summedOutFactors = new ArrayList<Factor>();
            List<Factor> toMultiply = new ArrayList<Factor>();
            for (Factor f : factors) {
                    if (f.contains(var)) {
                            toMultiply.add(f);
                    } else {
                            // This factor does not contain the variable
                            // so no need to sum out - see AIMA3e pg. 527.
                            summedOutFactors.add(f);
                    }
            }

            summedOutFactors.add(pointwiseProduct(toMultiply).sumOut(var));

            return summedOutFactors;
    }

    private Factor pointwiseProduct(List<Factor> factors) {

            Factor product = factors.get(0);
            for (int i = 1; i < factors.size(); i++) {
                    product = product.pointwiseProduct(factors.get(i));
            }

            return product;
    }
    
}
