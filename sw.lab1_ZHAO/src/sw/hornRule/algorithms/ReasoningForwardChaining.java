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
 * @author ZHAO Mengzi
 *
 */
public class ReasoningForwardChaining extends AlogrithmChaining {
 
	/**
	 * @param a knowledge base kb (in a given formalism)
	 * @param facts (in a given formalism)
	 * @return forwardChaining(ruleBase,factBase), also called the saturation of ruleBase w.r.t. factBase, 
	 * mathematically it computes the minimal fix point of KB from facts)
	 */
	//It's your turn to implement the algorithm, including the methods match() and eval()
	
	/**
	 * method match : verify if the condition is in the factBase
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
/*	public boolean eval(HornRule rule, Formalism fb){
		FactBase factBase = (FactBase) fb;
		HashSet<Variable> rule_conditions = rule.getConditions();
		if(this.countNbMatches(rule,factBase)==rule_conditions.size()) {
			return true;
		}else {
			return false;
		}
	}*/

	public boolean eval(HornRule rule, FactBase factBase){
		int cpt = 0;
		HashSet<Variable> rule_conditions = rule.getConditions();
		for(Variable c : rule_conditions) {
			boolean res = match(c,factBase);
			if(res == true) {
				cpt++;
			}
		}
		if(cpt == rule_conditions.size()) {
			return true;
		}else {
			return false;
		}
	}
	
	
	//Father father = new Son();
    //Son son = (Son)father;
	public FactBase forwardChaining(Formalism ruleBase, Formalism factBase){
		FactBase fb = (FactBase) factBase;
		HornRuleBase rules = (HornRuleBase) ruleBase;
		
		//initial size of the fact base
		int init_size = fb.getFact().size();
		//size of the new fact base 
		int taille = 0;
		
		while(taille != init_size) {
			init_size = fb.getFact().size();
			//for all rule R is not yet applied 
			for(int i = 0; i < rules.getRules().size(); i++) {
				//if conditions of the rule are in the fact base, we add conclusions of the rule to the fact base
				if(eval(rules.getRules().get(i),fb)) {
					fb.getFact().addAll(rules.getRules().get(i).getConclusions());
				}
			}
			taille = fb.getFact().size();
		}
		return fb;
	};
	

	public boolean entailment(Formalism ruleBase, Formalism factBase, Formalism query) {
		FactBase allInferredFacts = forwardChaining(ruleBase, factBase);
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

	
	//It's your turn to implement this method
	/*public int countNbMatches(HornRule rule, FactBase factBase) {
		int cpt = 0;
		HashSet<Variable> rule_conditions = rule.getConditions();
		for(Variable c : rule_conditions) {
			boolean res = match(c,factBase);
			if(res == true) {
				cpt++;
			}
		}
		return cpt;
	}*/

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
