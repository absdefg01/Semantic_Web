/**
 * 
 */
package sw.hornRule.algorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import sw.hornRule.models.FactBase;
import sw.hornRule.models.Formalism;
import sw.hornRule.models.HornRule;
import sw.hornRule.models.HornRuleBase;
import sw.hornRule.models.Variable;

/**
 * @author  ZHAO Mengzi
 *
 */


public class ReasoningBackwardChainingwithQuestions extends AlogrithmChaining {

	/**
	 * method match : verify if the conclusion is in the factBase
	 */
	public boolean match(Variable query, Formalism fb){
		FactBase factBase = (FactBase) fb;
		HashSet<Variable> factBase_list = factBase.getFact();
		for(Variable f : factBase_list) {
			if(f.getNomVariable().equals(query.getNomVariable())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * verifiy if the query is in conclusions of the rule r
	 * @param query
	 * @param r
	 * @return
	 */
	public boolean match2(Variable query, Formalism r){
		HornRule rule = (HornRule) r;
		HashSet<Variable> conclusion = rule.getConclusions();
		for(Variable concl : conclusion) {
			if(concl.getNomVariable().equals(query.getNomVariable())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * verifiy if the query is demandable
	 * @param query
	 * @param r
	 * @return
	 */
	public boolean demandable(Variable query,Formalism rb) {
		//if the query doesn't appear in the conclusions of rules
		//it is demandable
		int cpt = 0;
		HornRuleBase ruleBase = (HornRuleBase) rb;

		for(int i = 0; i < ruleBase.getRules().size(); i++) {
			if(ruleBase.getRules().get(i).getConditions().contains(query)) {
				cpt++;
			}
		}
		if(cpt>0) {
			return false;
		}else {
			return true;
		}
	}
	
	public boolean question(Variable query, Formalism rb) {
		boolean ans = false;
		if(this.demandable(query, rb)){
			System.out.println("This query is demandable, please enter your answer (true/false) : ");
			System.out.println();
		    Scanner sc = new Scanner(System.in);
		    try {
			    ans = sc.nextBoolean();
		    }catch(Exception e) {
		        System.out.println("The input that you wrote don't correspond to the good type : true/false");
		        e.printStackTrace();
		    }
		    sc.close();
		}
	    return ans;

	}
	
	public boolean backwardChainingwithQuestions(Formalism ruleBase, Formalism factBase,
			Formalism query) {
		HornRuleBase rb = (HornRuleBase) ruleBase;
		FactBase fb_init = (FactBase) factBase;
		Variable q = (Variable) query;
		boolean bool = false;
		int cpt = 0;
		ArrayList<Variable> conditions_list = new ArrayList<Variable>();

		if(this.match(q, fb_init)) {
			return true;
		}else {
			ArrayList<Variable> alreadyTried = new ArrayList<Variable>();
			for(int i = 0; i < rb.getRules().size(); i++) {
				if(this.match2(q,rb.getRules().get(i))) {
					bool = true;
					cpt = 1;
					//transform hashset list of contisions of rule i to arraylist
					HashSet<Variable> conditions = rb.getRules().get(i).getConditions();
					for(Variable c : conditions) {
						conditions_list.add(c);
					}
					while((bool)&&(cpt <= rb.getRules().get(i).getConditions().size())) {
						if(!alreadyTried.contains(conditions_list.get(cpt-1))) {
							alreadyTried.add(conditions_list.get(cpt-1));
							Variable c = conditions_list.get(cpt-1);
							bool = this.backwardChainingwithQuestions(ruleBase, factBase, c);
						}
						cpt++;
					}
					if(bool) {
						return true;
					}
				}
			}
			//verify if the query is demandable
			if(this.demandable(q, ruleBase)) {
				boolean b = this.question(q, ruleBase);
				System.out.println("This query is : " + b);
				return b ;
			}else {
				return false;
			}
		}
	}
	
	
	
	@Override
	public boolean entailment(Formalism ruleBase, Formalism factBase, Formalism Query) {
		// TODO To complete
		// When a literal (i.e. a variable or its negation) cannot be replied by deductive reasoning, 
		// it will be asked to users to give an answer (if the liter holds according to the user)
		return backwardChainingwithQuestions(ruleBase,factBase,Query);

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
