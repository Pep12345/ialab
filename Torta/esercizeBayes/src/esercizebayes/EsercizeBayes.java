/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esercizebayes;

import aima.core.probability.bayes.ConditionalProbabilityTable;
import aima.core.probability.bayes.impl.BayesNet;
import aima.core.probability.bayes.impl.CPT;
import aima.core.probability.bayes.impl.FullCPTNode;
import aima.core.probability.domain.BooleanDomain;
import aima.core.probability.util.RandVar;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author giuV3
 */
public class EsercizeBayes {

        
    public static final int N = 5;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic her
        List<RandVar> listVar= new ArrayList();
        List<FullCPTNode> listNode= new ArrayList();
        for(int i=0;i<N;i++){
            RandVar randVar = new RandVar("Dice"+i,new BooleanDomain());
            listVar.add(randVar);
            int lenght =0;
            if(i==0)
                lenght = 2;
            else
                lenght = 4;
            double[] distribution = new double[lenght];
            Arrays.fill(distribution,0.5);
            if(i==0)
                listNode.add(new FullCPTNode(randVar, distribution));
            else
                listNode.add(new FullCPTNode(randVar, distribution,listNode.get(i-1)));
            
        }
        
        BayesNet bayNet = new BayesNet(listNode.get(0));
        System.out.println(bayNet.getVariablesInTopologicalOrder());
        System.out.println(bayNet.getNode(new RandVar("Dice0", new BooleanDomain())).getParents());
        
        //bayNet.getNode(new RandVar("Dice1", new BooleanDomain()));
        //System.out.println(bayNet.getVariablesInTopologicalOrder());
        //System.out.println(bayNet.getNode(new RandVar("Dice1", new BooleanDomain())).getChildren());
        System.out.println(bayNet.getNode(new RandVar("Dice1", new BooleanDomain())).getCPD().getOn());
        double[] cpt = ((CPT)bayNet.getNode(new RandVar("Dice1", new BooleanDomain())).getCPD()).getValues();
        
        for( double d : cpt)
            System.out.println(d);
        
    }
    
}
