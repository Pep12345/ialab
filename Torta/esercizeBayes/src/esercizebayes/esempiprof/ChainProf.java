/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esercizebayes.esempiprof;

import aima.core.probability.CategoricalDistribution;
import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.BayesInference;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.bayes.FiniteNode;
import aima.core.probability.bayes.Node;
import aima.core.probability.bayes.exact.EliminationAsk;
import aima.core.probability.bayes.exact.EnumerationAsk;
import aima.core.probability.bayes.impl.BayesNet;
import aima.core.probability.bayes.impl.FullCPTNode;
import aima.core.probability.domain.BooleanDomain;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.probability.util.RandVar;
import java.util.HashSet;

/**
 *
 * @author Biondi Giuseppe
 */
public class ChainProf {
    public static void main(String[] args) {
        int n = Integer.parseInt("10");
        
        System.out.println("Creo chain di " + n + " nodi");
        RandomVariable[] allrv = new RandomVariable[n];
        
        for (int i=0; i<n; i++) {
            allrv[i] = new RandVar("X"+(i+1), new BooleanDomain());
        }
        
        FiniteNode fn = new FullCPTNode(allrv[0], new double[] {0.5, 0.5 });
        FiniteNode fn0=fn;
        for (int i=1; i<n; i++) {
            fn = new FullCPTNode(allrv[i], new double[] {0.5, 0.5, 0.5, 0.5}, fn);
        }
        BayesianNetwork bn = new BayesNet(fn0);
        
        System.out.println("Creo query variable e evidenza Xn=true");
        RandomVariable[] qrv = new RandomVariable[1];
        qrv[0] = allrv[0]; // P(x0)
        AssignmentProposition[] ap = new AssignmentProposition[1]; 
        ap[0] = new AssignmentProposition(allrv[n-1], Boolean.TRUE); //e = x9 = TRUE
        
        /*CategoricalDistribution cd;
        BayesInference[] allbi = new BayesInference[] {new EnumerationAsk(), new EliminationAsk(), new MyEliminationAsk()};
        //BayesInference[] allbi = new BayesInference[] {new MyEliminationAsk()};
        
        for (BayesInference bi  : allbi) {
            System.out.println("Simple query con " + bi.getClass());
            cd = bi.ask(qrv, ap, bn);
            System.out.print("P(X0|xn) = <");
            for (int i = 0; i < cd.getValues().length; i++) {
                System.out.print(cd.getValues()[i]);
                if (i < (cd.getValues().length - 1)) {
                    System.out.print(", ");
                } else {
                    System.out.println(">");
                }
            }
        }     */
        
        System.out.println(bn.getNode(allrv[0]).getChildren());
    }
    

}
