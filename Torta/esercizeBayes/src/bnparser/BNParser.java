package bnparser;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.List;

import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.RandomVariable;
/**
 *
 * @author torta
 */
public class    BNParser {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BayesianNetwork bn = BifReader.readBIF("../reti/100to1000nodi/pigs.xml");
        List<RandomVariable> rvs = bn.getVariablesInTopologicalOrder();
        System.out.println(rvs.size());
        }   
    
}
