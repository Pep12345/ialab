/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Project;

import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.domain.BooleanDomain;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.probability.util.RandVar;
import bnparser.BifReader;

/**
 *
 * @author Biondi Giuseppe
 */
public class NewClass {
    public static void main(String[] args) throws CloneNotSupportedException {
        BayesianNetwork bn = BifReader.readBIF("Sprinkler.xml");
        
        // example th 1
        RandomVariable[] query = {new RandVar("Grass", new BooleanDomain())};
        RandVar ev = new RandVar("Rain", new BooleanDomain());
        AssignmentProposition[] as = {new AssignmentProposition(ev, Boolean.TRUE)};
        Prunning p = new Prunning(query, as, bn);
        
        BayesianNetwork bn2 = p.prunningEdge();
        //BayesianNetwork bn2 = p.prunningNodeAncestors();
        System.out.println(bn2.getNode(ev).getChildren());
    }
}