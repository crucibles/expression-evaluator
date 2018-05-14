import java.util.Stack;

/**
 * This class evaluates the received string.
 * 
 * @author Sumandang, AJ Ruth H.
 *
 */
public class Evaluator {
	/**
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	public Evaluator(String eval, int[] exclude, String fileName, int numOfErrors) {
		evaluate(eval, exclude, fileName, numOfErrors);
	}

	private String evaluate(String eval, int[] exclude, String fileName, int numOfErrors) {
		Stack<String> stack = new Stack<String>();
		String[] evalWords = eval.trim().split("\\s");
		
		String output = "_____" + "compiled with " + "______" + " errors found. Program test will now be executed...";
		output += "SNuBL Executition: \n\n";

		for(int i = 0; i < evalWords.length; i++){
			String currentWord = evalWords[i];
			if(isOperand(currentWord)){
				stack.push(currentWord);
			} else if(isOperator(currentWord)){
				//some executions
			} else if(isRelationalOp(currentWord)){
				
			} else if(isLogicalOp(currentWord)){
				
			} else if(currentWord.equals("BEG")){
				begInput();
			} else if(currentWord.equals("PRINT")){
				print(currentWord);
			} else if(currentWord.equals("NEWLN")){
				output += "\n";
			} 
		}
		
		output += "Program terminated successfully...";
		return output;
	}
	
	private boolean isOperand(String word){
		String[] operands = {"IDENT", "INT_LIT", "FLOAT_LIT"};
		for(int i = 0; i < operands.length; i++){
			if(word.equals(operands[i])){
				return true;
			}
		}
		return false;
	}
	
	private String begInput(){
		String result = "Input for " + "____" + ": ";
		return result;
	}
	
	private String print(String word){
		String result = "";
		return result;
	}
	
	private boolean isOperator(String word){
		String[] operators = {"ADD", "SUB", "MULT", "DIV", "MOD"};
		for(int i = 0; i < operators.length; i++){
			if(word.equals(operators[i])){
				return true;
			}
		}
		return false;
	}
	
	private boolean isLogicalOp(String word){
		String[] logicalOperators = {"AND?", "OR?", "NOT?"};
		for(int i = 0; i < logicalOperators.length; i++){
			if(word.equals(logicalOperators[i])){
				return true;
			}
		}
		return false;
	}
	
	private boolean isRelationalOp(String word){
		String[] relationalOperators = {"GT?", "GTE?", "LT?", "LTE?", "EQ?", "NEQ?"};
		for(int i = 0; i < relationalOperators.length; i++){
			if(word.equals(relationalOperators[i])){
				return true;
			}
		}
		return false;
	}
}
