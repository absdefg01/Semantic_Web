/**
 * 
 */
package sw.hornRule.algorithms;

import java.util.ArrayList;
import java.util.HashSet;

import sw.hornRule.models.FactBase;
import sw.hornRule.models.Formalism;
import sw.hornRule.models.HornRule;
import sw.hornRule.models.HornRuleBase;
import sw.hornRule.models.Variable;

/**
 * @author  ZHAO Mengzi
 *
 */
public class ReasoningForwardChainingOptimised extends AlogrithmChaining {
	
	/**
	 * @param a knowledge base ruleBase (in a given formalism)
	 * @param a base of facts : factBase (in a given formalism)
	 * @return the saturation of KB w.r.t. facts (the minimal fix point of KB from facts)
	 */
	
	public boolean match(Variable condition, Formalism fb){
		FactBase factBase = (FactBase) fb;
		HashSet<Variable> factBase_list = factBase.getFact();
		for(Variable f : factBase_list) {
			if(f.getNomVariable().equals(condition.getNomVariable())) {
				return true;
			}
		}
		return false;
	}
	
	
	
	/**
	 * method eval : verify if the rule is true
	 */
	public boolean eval(HornRule rule, Formalism fb){
		FactBase factBase = (FactBase) fb;
		HashSet<Variable> rule_conditions = rule.getConditions();
		if(this.countNbMatches(rule,factBase)==rule_conditions.size()) {
			return true;
		}else {
			return false;
		}
	}
	
	
	
	public FactBase forwardChainingOptimise(Formalism ruleBase, Formalism factBase){
		//It's your turn to implement the algorithm
		FactBase fb_init = (FactBase) factBase;
		FactBase fb = (FactBase) factBase;
		
		for(Variable f : fb_init.getFact()) {
			fb.getFact().addAll(this.propagate(f, ruleBase));
		}
		return fb;
	};
	
	
	
	public HashSet<Variable> propagate(Variable fact, Formalism ruleBase) {
		HornRuleBase rb = (HornRuleBase) ruleBase;
		HashSet<Variable> newFacts = new HashSet<Variable>();
		//for all rules which have the fact in their conditions
		for(int i = 0; i < rb.getRules().size(); i++) {
			if(rb.getRules().get(i).getConditions().contains(fact)) {
				HashSet<Variable> conditions = rb.getRules().get(i).getConditions();
				for(Variable c : conditions) {
					conditions.remove(c);
				}
			}
			
			//if the part of conditions in the rule is empty
			//it means the rule is correct
			//we can add its conclusions to the list of new facts
			//we don't need to verify this rule anymore, so we delete this rule from the rule base
			if(rb.getRules().get(i).getConditions().isEmpty()) {
				newFacts.addAll(rb.getRules().get(i).getConclusions());
				rb.getRules().remove(rb.getRules().get(i));
				
			}	
		}
		
		FactBase newFactBase = new FactBase();
		newFactBase.setFact(newFacts);
		
		for(Variable f : newFacts) {
			newFacts.addAll(propagate(f, rb));
		}
		
		return newFacts;
	}

 
	
	public boolean entailment(Formalism ruleBase, Formalism factBase, Formalism query) {
		FactBase allInferredFacts = forwardChainingOptimise(ruleBase, factBase);
		Variable q = (Variable) query; 
		int cpt = 0;
		HashSet<Variable> allFacts = allInferredFacts.getFact();
		ArrayList<Variable> allFacts_list = new ArrayList<Variable>();
		for(Variable f : allFacts) {
			allFacts_list.add(f);
		}
		for(int i = 0; i < allInferredFacts.getFact().size(); i++) {
			if(allFacts_list.get(i).getNomVariable().equals(q.getNomVariable())) {
				cpt++;
			}
		}
		if(cpt>0) {
			return true;
		}else {
			return false;
		}
	}

	
	@Override
	public int countNbMatches(Formalism rule, Formalism fb) {
		int cpt = 0;
		HornRule r = (HornRule) rule;
		FactBase factBase = (FactBase) fb;
		HashSet<Variable> rule_conditions = r.getConditions();
		for(Variable c : rule_conditions) {
			boolean res = match(c,factBase);
			if(res == true) {
				cpt++;
			}
		}
		return cpt;
	}
}
