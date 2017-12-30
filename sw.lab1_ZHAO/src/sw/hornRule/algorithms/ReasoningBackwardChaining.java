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
public class ReasoningBackwardChaining extends AlogrithmChaining {
 
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
	
	
	public boolean entailment(Formalism ruleBase, Formalism factBase, Formalism query) {
		return backwardChaining(ruleBase,factBase,query);
	}

	public boolean backwardChaining(Formalism ruleBase, Formalism factBase,
			Formalism query) {
		FactBase fb = (FactBase) factBase;
		Variable q = (Variable) query;
		HornRuleBase rb = (HornRuleBase) ruleBase;
		boolean bool = false;
		int cpt = 0;
		ArrayList<Variable> conditions_list = new ArrayList<Variable>();
		
		//if the query is in the fact base
		if(this.match(q, fb)) {
			return true;
		}else{
			//for all rules
			for(int i = 0; i < rb.getRules().size(); i++) {
				//tq match(query,conclusion)
				if(this.match2(q, rb.getRules().get(i))) {
					bool = true;
					cpt = 1;
					HashSet<Variable> conditions = rb.getRules().get(i).getConditions();
					for(Variable c : conditions) {
						conditions_list.add(c);
					}
					while ((bool)&&(cpt<=rb.getRules().get(i).getConditions().size())) {
						Variable c = conditions_list.get(cpt-1);
						bool = this.backwardChaining(ruleBase, factBase, c);
						cpt++;
					}
					if(bool) {
						return true;
					}
				}
			}
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
