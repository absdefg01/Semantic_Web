package problem.hornRules;
 
import java.io.OutputStream;
import java.util.HashSet;
import sw.hornRule.algorithms.*;
import sw.hornRule.models.*;

public class ReasoningHorn {

	public static void main(String[] args) {
		
		ReasoningForwardChaining reasoner = new ReasoningForwardChaining();
		Tutorial1 pb = new Tutorial1();
		HornRuleBase kb = pb.getRuleBase();
		FactBase fb = pb.getFactBase();
		
		
		/*for(HornRule r: kb.getRules()){
			System.out.println(r);
		}
		System.out.print("\nThe fact base is: \n");
		System.out.print(fb);*/


		
		
		
		
		/**
		 * test match
		 * 	public boolean match)
		 */
		Variable qq = new Variable("transoceanic_race");
		System.out.println("****************************************************************");
		System.out.println("Test match : " + reasoner.match(qq, fb));
		System.out.println("****************************************************************");
		System.out.println();
		System.out.println();
		System.out.println();
		
		
		/**
		 * test eval
		 */
		HornRule rule = kb.getRules().get(0);		
		/*HashSet<Variable> conditions = rule.getConditions();
		System.out.println(conditions);
		for(Variable c : conditions) {
			System.out.println(reasoner.match(c,fb));
		}*/
		System.out.println("****************************************************************");
		System.out.println("Test the function eval : " + reasoner.eval(rule, fb));
		System.out.println("****************************************************************");
		System.out.println();
		System.out.println();
		System.out.println();
		
		
				
		/**
		 * test forward chaining
		 */		
		
		System.out.println("****************************************************************");
		System.out.println("Test forward chaining : " + reasoner.forwardChaining(kb,fb));
		System.out.println("****************************************************************");
		System.out.println();
		System.out.println();
		System.out.println();
		
		
		//Display all facts inferred by the given knowledge base kb and fact base fb
		HashSet<Variable> inferredAllFacts = reasoner.forwardChaining(kb,fb).getFact();
		System.out.println("All the inferred facts are:");
		for(Variable s: inferredAllFacts){
			System.out.println(s);
		}	
		
		if(reasoner.entailment(kb, fb, qq)) {
			System.out.println("\nYes, the query is entailed by the given rules and facts");
		}
		else {
			System.out.println("\nNo, the query is not entailed based on the given rules and facts");			
		}

		
		
		/**
		 * test forward chaining optimised
		 */
		Formalism f2 = fb;
		Formalism rb2 = kb;
		ReasoningForwardChainingOptimised reasoner2 = new ReasoningForwardChainingOptimised();
		System.out.println("****************************************************************");
		System.out.println("Test the algorithm of forward chaining optimised");
		System.out.println(reasoner2.forwardChainingOptimise(kb, fb));
		if(reasoner2.entailment(kb, fb, qq)) {
			System.out.println("\nYes, the query is entailed by the given rules and facts");
		}
		else {
			System.out.println("\nNo, the query is not entailed based on the given rules and facts");			
		}
		System.out.println("****************************************************************");
		System.out.println();
		System.out.println();
		System.out.println();
		
		
		
		/**
		 * test backward chaining
		 */
		//facts=[cruise_offshore, sailboat, longer_than_13, sailboat_cruise, transoceanic_race, portable, longer_than_10, sailing_dinghy, not_keel, not_portable, habitable, boat, sport, racing_can, sail, longer_than_8]
		Formalism f3 = fb;
		Formalism rb3 = kb;
		Variable query = new Variable();
		Variable query2 = new Variable();
		Variable query3 = new Variable();
		Variable query4 = new Variable();
		Variable query5 = new Variable();
		
		query.setNomVariable("gaff_rig");
		query2.setNomVariable("transoceanic_race");
		query3.setNomVariable("sailboat");
		query4.setNomVariable("longer_than_13");
		query5.setNomVariable("sailboat_cruise");

		Formalism q = (Formalism) query;
		Formalism q2 = (Formalism) query2;
		Formalism q3 = (Formalism) query3;
		Formalism q4 = (Formalism) query4;
		Formalism q5 = (Formalism) query5;
		
		ReasoningBackwardChaining reasoner3 = new ReasoningBackwardChaining();
		System.out.println("****************************************************************");
		System.out.println(reasoner3.backwardChaining(rb3, f3, q));
		System.out.println(reasoner3.backwardChaining(rb3, f3, q2));
		System.out.println(reasoner3.backwardChaining(rb3, f3, q3));
		System.out.println(reasoner3.backwardChaining(rb3, f3, q4));
		System.out.println(reasoner3.backwardChaining(rb3, f3, q5));
		
		if(reasoner3.entailment(kb, fb, q2)) {
			System.out.println("\nYes, the query is entailed by the given rules and facts");
		}else {
			System.out.println("\nNo, the query is not entailed based on the given rules and facts");			
		}
		System.out.println("****************************************************************");
		System.out.println();
		System.out.println();
		System.out.println();

		
		/**
		 * test backward chaining with questions
		 */
		Formalism f4 = fb;
		Formalism rb4 = kb;
		ReasoningBackwardChainingwithQuestions reasoner4 = new ReasoningBackwardChainingwithQuestions();
		Variable query6 = new Variable();
		query6.setNomVariable("gaff_rig");
		Formalism q6 = (Formalism) query6;
		
		System.out.println("****************************************************************");
		System.out.println("Test backward chaining with questions");
		System.out.println(reasoner4.backwardChainingwithQuestions(rb4, f4, q6));
		System.out.println("****************************************************************");
		System.out.println();
		System.out.println();
		System.out.println();
		
	}
}
