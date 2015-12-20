import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class inference {

	
	public static KnowledgeBase kb = new KnowledgeBase();
	public static int sentenceNumber;
	
	public static void main(String[] args) throws FileNotFoundException {
		
		//String inputFileName = "F:\\workspace\\java\\AIAssignment1\\inferenceInputs\\Testing.txt";
		
		String inputFileName = args[1];
		String outputFileName = "output.txt";
		
		
		Scanner sc = new Scanner(new File(inputFileName));
		
		int numQueries = Integer.parseInt(sc.nextLine());
		
		List<Predicate> goalQueries = new ArrayList<Predicate>();
		for (int i =0;i< numQueries;i++){			
			String query = sc.nextLine();
			goalQueries.add(parseAtomicSentence(query));			
		}
		
		int numSentences = Integer.parseInt(sc.nextLine());		
		
		for (int i =0;i< numSentences;i++){
			String sentence = sc.nextLine();
			int sentenceNum = i + 1;
			sentenceNumber = sentenceNum;
			parseSentence(sentence , sentenceNum);			
		}
		boolean[] resultArray = new boolean[goalQueries.size()];
		int index = 0;
		for(Predicate query : goalQueries){	
			Map<Variable,Term> theta= new HashMap<Variable,Term>();
			List<Predicate> goalList = new ArrayList<Predicate>();
			List<Map<Variable,Term>>  result=  fol_bc_or(kb, query, theta, goalList);
			if(!result.isEmpty()){
				resultArray[index++] = true;
			}
			else{
				resultArray[index++] = false;
			}			
		}
		PrintWriter writer = new PrintWriter(outputFileName);
		for(int i =0;i< resultArray.length;i++){
			if(resultArray[i])
				writer.println("TRUE");
			else
				writer.println("FALSE");
		}
		writer.close();
		sc.close();		
	}
	
	
	public boolean[] backwardChainingAlorithm(String fileName) throws FileNotFoundException{		
		Scanner sc = new Scanner(new File(fileName));
		
		kb.premiseMap = new HashMap<Integer,List<Predicate>>();
		kb.conclusionMap = new HashMap<Integer,Predicate>();
		int numQueries = Integer.parseInt(sc.nextLine());
		List<Predicate> goalQueries = new ArrayList<Predicate>();
		for (int i =0;i< numQueries;i++){
			String query = sc.nextLine();
			goalQueries.add(parseAtomicSentence(query));			
		}
		
		int numSentences = Integer.parseInt(sc.nextLine());		
		
		for (int i =0;i< numSentences;i++){
			String sentence = sc.nextLine();
			int sentenceNum = i + 1;
			sentenceNumber = sentenceNum;
			parseSentence(sentence , sentenceNum);			
		}
		
		boolean[] resultArray = new boolean[goalQueries.size()];
		int index = 0;
		for(Predicate query : goalQueries){
			Map<Variable,Term> theta= new HashMap<Variable,Term>();
			List<Predicate> goalList = new ArrayList<Predicate>();
			List<Map<Variable,Term>>  result=  fol_bc_or(kb, query, theta, goalList);
			if(!result.isEmpty()){
				resultArray[index++] = true;
			}
			else{
				resultArray[index++] = false;
			}
		}
		sc.close();	
		return resultArray;
		
	}
	
	public static List<Map<Variable,Term>> fol_bc_or (KnowledgeBase kb , Predicate goalQuery , Map<Variable,Term> theta, List<Predicate> goalList){
		SubstitutionHelper subHelper = new SubstitutionHelper();
		Predicate gQuery = (Predicate)subHelper.substitute(theta, goalQuery);
		Map<Integer, Predicate> conclusionMap = kb.conclusionMap;		
		List<Map<Variable,Term>> substList = new ArrayList<Map<Variable,Term>>();
		if(goalList.contains(gQuery)){
			return null;
		}else{
			goalList.add(gQuery);
		}
		
		for (Object key : conclusionMap.keySet()) {
			
			Integer sentenceNum = (Integer)key;
			Predicate pred = conclusionMap.get(key);
			
			if(pred.pName.equals(gQuery.getName()) && pred.isNegated == gQuery.isNegated){
				UnificationHelper unfHelper = new UnificationHelper();
				Map<Variable, Term> thetaTemp = new LinkedHashMap<Variable, Term>();
				thetaTemp.putAll(theta);				
				
				Map<Variable, Term> thetaDelta = unfHelper.unify(pred, gQuery, thetaTemp);				
				
				List<Predicate> premiseList = kb.premiseMap.get(sentenceNum);				
				
				if(thetaDelta != null){
					List<Map<Variable,Term>> andList = fol_bc_and(kb,premiseList , thetaDelta , goalList);
					 if(andList!= null){
						 substList.addAll(andList);
					 }					
				}				
			}			
		}		
		return substList;
	}
	
	public static List<Map<Variable,Term>> fol_bc_and(KnowledgeBase kb , List<Predicate> predList , Map<Variable,Term> theta , List<Predicate> goalList){
		List<Map<Variable,Term>> returnSubstitutionList = new ArrayList<Map<Variable,Term>>();
		SubstitutionHelper subHelper = new SubstitutionHelper();		
		
		if(theta == null){
			return null;			
		}
		else if (predList == null || predList.isEmpty()){
			returnSubstitutionList.add(theta);
			return returnSubstitutionList;
		}
		else{
			
			Predicate pred = predList.get(0);
			Predicate gQuery = (Predicate)subHelper.substitute(theta, pred);
			if(goalList.contains(gQuery)){
				return null;
			}
			List<Predicate> remainingList = new ArrayList<Predicate>();
			for(int i=1;i<predList.size();i++){
				remainingList.add(predList.get(i));
			}
			
			List<String> currentSentence = new ArrayList<String>();
			ArrayList<Term> terms = (ArrayList<Term>)pred.terms;
			
			String digitPattern = "\\d[0-9]*";
			 
			Matcher digitMatch = Pattern.compile(digitPattern).matcher(terms.get(0).toString());
			while (digitMatch.find()) {
				currentSentence.add(digitMatch.group());
			}
			Map<Variable, Term> thetaTemp = new LinkedHashMap<Variable, Term>();
			for (Variable variable : theta.keySet()) {
				List<String> incomingSentence = new ArrayList<String>();
				String name = variable.vName;
				Matcher digitMatchAll = Pattern.compile(digitPattern).matcher(name);
				while (digitMatchAll.find()) {
					incomingSentence.add(digitMatchAll.group());
				}
				if (currentSentence.equals(incomingSentence))
					thetaTemp.put(variable, theta.get(variable));
			}		
			
			List<Predicate> goalListTemp=new ArrayList<Predicate>();
			goalListTemp.addAll(goalList);			
			
			
			if(!isVariableTerm(gQuery)){
				thetaTemp.clear();
			}
			
			List<Map<Variable,Term>> orList = fol_bc_or(kb,gQuery,thetaTemp, goalListTemp);
			
			for (Map<Variable, Term> tempMap : orList) {
				tempMap.putAll(theta);
				}			
			
			for (Map<Variable, Term> tempMap : orList) {
				subsituteVariables(tempMap);
				}
		
			for(Map<Variable,Term> item : orList){
				List<Map<Variable,Term>> andList = fol_bc_and(kb,remainingList,item,goalList);
				if(andList != null){
					if(!andList.isEmpty()){
						returnSubstitutionList.addAll(andList);
					}
				}
			}
		}		
		return returnSubstitutionList;
	}
	
	public static boolean isVariableTerm(Predicate query){
		List<Term> terms1 = query.terms;
		Class<Variable> v1 = Variable.class;
		boolean variableFlag = false;
		for(Term t : terms1){
			if(t.getClass() == v1){
				variableFlag = true;
				break;
			}
		}
		return variableFlag;
	}
	
	public static Map<Variable,Term>  subsituteVariables(Map<Variable,Term> theta){
		if(theta == null){
			return theta;
		}		
		Class<Constant> constant = Constant.class;
		for (Variable var : theta.keySet()) {			
			Term value = theta.get(var);
			
			if(value.getClass() == constant){
				subsituteVariable(var , value , theta);
			}
		}		
		
		return theta;
	}
	
	public static void  subsituteVariable(Term varibleSubst ,Term constantValue ,Map<Variable,Term> theta){		
		Class<Variable> variable = Variable.class;
		for (Variable var : theta.keySet()) {			
			Term value = theta.get(var);			
			if(value.getClass() == variable && value.toString().equals(varibleSubst.toString())){
				theta.put(var, constantValue);
			}			
		}
	}
	
	public static void parseSentence(String sentence , int sentenceNum){		  
		  String[] sentenceParts = sentence.split("=>");
		  String premisePart,conclusionPart;		  	  
		  
		  if(sentenceParts.length == 1){
			  conclusionPart = sentenceParts[0].trim();
			  kb.conclusionMap.put(sentenceNum, parseAtomicSentence(conclusionPart));
		  }
		  else{
			  premisePart = sentenceParts[0].trim();
			  conclusionPart = sentenceParts[1].trim();
			  kb.premiseMap.put(sentenceNum, parseSentencePremise(premisePart));
			  kb.conclusionMap.put(sentenceNum, parseAtomicSentence(conclusionPart));
		  }
		  
	}
	
	public static List<Predicate> parseSentencePremise(String premise){
		String[] premiseParts = premise.split("\\^");
		List<Predicate> predList = new ArrayList<Predicate>();
		for (int i = 0; i < premiseParts.length; i++) {
			String string = premiseParts[i];
			Predicate pred = parseAtomicSentence(string);
			predList.add(pred);			
		  }
		return predList;
	}
	
	public static Predicate parseAtomicSentence(String query){
		  String negatedPattern = "~(.*?)\\(";
		  String positivePattern = "(.*?)\\(";
		  
		  String termsPattern = "\\(([^)]+)\\)";	  
		  
		  String predicateName = getSubstring(negatedPattern, query);
		  boolean isNegated = false;
		  if(!predicateName.isEmpty()){			  
			  isNegated = true;
		  }
		  else{
			  predicateName = getSubstring(positivePattern, query);
		  }
		  
		  String termsString = getSubstring(termsPattern, query);
		  String[] terms = termsString.split(",");
		  List<Term> lterms = new ArrayList<Term>();
		  for (int i = 0; i < terms.length; i++) {
			String termName = terms[i];
			Term term = null;
			if(isLowerCase(termName.charAt(0))){
				term = new Variable(termName + sentenceNumber);
			}
			else{
				term = new Constant(termName);
			}
			lterms.add(term);			
		  }
		  Predicate pred = new Predicate(predicateName, lterms);
		  if(isNegated == true){
			  pred.isNegated = isNegated;
		  }  
		  
		  return pred;
	}	
	
	public static String getSubstring(String pattern , String query){
		String result = ""; 
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(query);
		if(m.find()){			
			result = m.group(1);
		}
		return result;
	}
	
	public static boolean isLowerCase(char ch) {
		return ch >= 'a' && ch <= 'z';
	}
}

interface BaseNode{
	String getName();
	boolean isComplexTerm();
	List<? extends BaseNode> getArguments();
	Object acceptNode(SubstitutionHelper v, Object arg);
	BaseNode copyNode();
}

interface Term extends BaseNode{
	List<Term> getArguments();
	Term copyNode();
}

class Variable implements Term{
	String vName;
	
	public Variable(String name){
		vName = name.trim();
	}
	
	public String getValue() {
		return vName;
	}
	
	public String getName() {
		return getValue();
	}

	public boolean isComplexTerm() {
		return false;
	}

	public List<Term> getArguments() {
		return null;
	}
	
	public Object acceptNode(SubstitutionHelper v, Object arg) {
		return v.getVariableTerm(this, arg);
	}

	public Variable copyNode() {
		return new Variable(vName);
	}

	@Override
	public String toString() {
		return vName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((vName == null) ? 0 : vName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Variable other = (Variable) obj;
		if (vName == null) {
			if (other.vName != null)
				return false;
		} else if (!vName.equals(other.vName))
			return false;
		return true;
	}	
}

class Constant implements Term{
	String cName;
	
	public Constant(String name){		
		cName = name.trim();
	}
	
	public String getValue() {
		return cName;
	}

	public String getName() {
		return getValue();
	}

	public boolean isComplexTerm() {
		return false;
	}

	public List<Term> getArguments() {
		return null;
	}
	
	public Object acceptNode(SubstitutionHelper v, Object arg) {
		return v.getConstantTerm(this, arg);
	}

	public Constant copyNode() {
		return new Constant(cName);
	}

	@Override
	public String toString() {
		return cName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cName == null) ? 0 : cName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Constant other = (Constant) obj;
		if (cName == null) {
			if (other.cName != null)
				return false;
		} else if (!cName.equals(other.cName))
			return false;
		return true;
	}	
}

class Predicate implements BaseNode {
	String pName;
	List<Term> terms = new ArrayList<Term>();
	boolean isNegated = false;
	
	public Predicate(String predicateName, List<Term> terms) {
		this.pName = predicateName.trim();
		this.terms.addAll(terms);
	}
	
	public String getPredicateName() {
		return pName;
	}
	
	public List<Term> getTerms() {
		return Collections.unmodifiableList(terms);
	}
	
	public String getName() {
		return getPredicateName();
	}

	public boolean isComplexTerm() {
		return true;
	}

	public List<Term> getArguments() {
		return getTerms();
	}
	
	public Object acceptNode(SubstitutionHelper v, Object arg) {
		return v.getPredicateTerm(this, arg);
	}
	
	public Predicate copyNode() {
		List<Term> copyTerms = new ArrayList<Term>();
		for (Term t : terms) {
			copyTerms.add(t.copyNode());
		}
		return new Predicate(pName, copyTerms);
	}
	
	
	@Override
	public String toString() {
		return pName + "," + terms + "," + isNegated;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isNegated ? 1231 : 1237);
		result = prime * result + ((pName == null) ? 0 : pName.hashCode());
		result = prime * result + ((terms == null) ? 0 : terms.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Predicate other = (Predicate) obj;
		if (isNegated != other.isNegated)
			return false;
		if (pName == null) {
			if (other.pName != null)
				return false;
		} else if (!pName.equals(other.pName))
			return false;
		if (terms == null) {
			if (other.terms != null)
				return false;
		} else if (!terms.equals(other.terms))
			return false;
		return true;
	}	
}

class KnowledgeBase{
	Map<Integer, List<Predicate>> premiseMap = new HashMap<Integer,List<Predicate>>();
	Map<Integer, Predicate> conclusionMap = new HashMap<Integer,Predicate>();
	@Override
	public String toString() {
		return "KB-PMap=" + premiseMap + ", CMap=" + conclusionMap;
	}	
}

class UnificationHelper {
	private static SubstitutionHelper subHelper = new SubstitutionHelper();

	public UnificationHelper() {

	}

	public Map<Variable, Term> unify(BaseNode x, BaseNode y) {
		return unify(x, y, new LinkedHashMap<Variable, Term>());
	}

	
	public Map<Variable, Term> unify(BaseNode nodeOne, BaseNode nodeTwo,Map<Variable, Term> substitutionList) {
		if (substitutionList == null) {
			return null;
		} else if (nodeOne.equals(nodeTwo)) {			
			return substitutionList;
		} else if (nodeOne instanceof Variable) {
			return unifyVariables((Variable) nodeOne, nodeTwo, substitutionList);
		} else if (nodeTwo instanceof Variable) {
			return unifyVariables((Variable) nodeTwo, nodeOne, substitutionList);
		} else if (isComplexTerm(nodeOne) && isComplexTerm(nodeTwo)) {
			return unify(arguments(nodeOne), arguments(nodeTwo), unifyTerms(options(nodeOne), options(nodeTwo), substitutionList));
		} else {
			return null;
		}
	}	
	
	public Map<Variable, Term> unify(List<? extends BaseNode> x,
			List<? extends BaseNode> y, Map<Variable, Term> theta) {
		if (theta == null) {
			return null;
		} else if (x.size() != y.size()) {
			return null;
		} else if (x.size() == 0 && y.size() == 0) {
			return theta;
		} else if (x.size() == 1 && y.size() == 1) {
			return unify(x.get(0), y.get(0), theta);
		} else {
			return unify(x.subList(1, x.size()), y.subList(1, y.size()),
					unify(x.get(0), y.get(0), theta));
		}
	}	
	
	private Map<Variable, Term> unifyVariables(Variable var, BaseNode x,
			Map<Variable, Term> theta) {

		if (!Term.class.isInstance(x)) {
			return null;
		} else if (theta.keySet().contains(var)) {
			return unify(theta.get(var), x, theta);
		} else if (theta.keySet().contains(x)) {
			return unify(var, theta.get(x), theta);
		}else {
			cascadeSubstitution(theta, var, (Term) x);
			return theta;
		}
	}

	private Map<Variable, Term> unifyTerms(String x, String y,
			Map<Variable, Term> theta) {
		if (theta == null) {
			return null;
		} else if (x.equals(y)) {
			return theta;
		} else {
			return null;
		}
	}

	private List<? extends BaseNode> arguments(BaseNode x) {
		return x.getArguments();
	}

	private String options(BaseNode x) {
		return x.getName();
	}

	private boolean isComplexTerm(BaseNode x) {
		return x.isComplexTerm();
	}
	
	private Map<Variable, Term> cascadeSubstitution(Map<Variable, Term> theta,
			Variable var, Term x) {
		theta.put(var, x);
		for (Variable v : theta.keySet()) {
			theta.put(v, subHelper.substitute(theta, theta.get(v)));
		}
		
		return theta;
	}
}

class SubstitutionHelper {

	public SubstitutionHelper() {
	}	

	public Predicate substitute(Map<Variable, Term> theta, Predicate pred) {
		return (Predicate) pred.acceptNode(this, theta);
	}

	public Term substitute(Map<Variable, Term> theta, Term aTerm) {
		return (Term) aTerm.acceptNode(this, theta);
	}
	
	@SuppressWarnings("unchecked")	
	public Object getVariableTerm(Variable variable, Object arguments) {
		Map<Variable, Term> substitution = (Map<Variable, Term>) arguments;
		if (substitution.containsKey(variable)) {
			return substitution.get(variable).copyNode();
		}
		return variable.copyNode();
	}	

	public Object getPredicateTerm(Predicate predicate, Object arguments) {
		List<Term> terms = predicate.getTerms();
		List<Term> newTerms = new ArrayList<Term>();
		for (int i = 0; i < terms.size(); i++) {
			Term t = terms.get(i);
			Term subsTerm = (Term) t.acceptNode(this, arguments);
			newTerms.add(subsTerm);
		}
		Predicate pred = new Predicate(predicate.getPredicateName(), newTerms);
		pred.isNegated = predicate.isNegated;
		return pred;

	}
	
	public Object getConstantTerm(Constant constant, Object arguments) {
		return constant;
	}	
}