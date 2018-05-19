
public class SemanticsHandler {
    public String parsed;
    public String source;
    public SymbolTable st;
    public String error;
    private Helper helper = new Helper();

    public SemanticsHandler(String parsed, String source, SymbolTable st) {
        this.parsed = parsed;
        this.source = source;
        this.st = st;
        this.error = "";
        semanticChecker();
    }

    public void semanticChecker() {
        semanticsAnalyze();
    }

    public void semanticsAnalyze() {
        String input = this.parsed;
        String input2 = this.source;
        int identHolder = 0;
        String identType = "";
        String identValue = "";
        String[] parseWords = input.split("\\s");
        String[] sourceWords = input2.split("\\s");
        String[] parseStmts = input.split("\n");

        int[] indexes = getIndexes(parseStmts);
        int lineNum = indexes[0] + 1;
        int currLength = indexes[1];
        
        String[] parseStmt = parseStmts[lineNum - 1].split("\\s");

        String[] defined = new String[parseWords.length];
        int x;

        for (x = 0; x < parseWords.length && !parseWords[x].equals("COMMAND"); x++) {
        	if(currLength < parseStmt.length){
        		currLength++;
        	} else {
        		System.out.println(parseStmt.length);
        		parseStmt = parseStmts[lineNum].split("\\s");
        		lineNum++;
        		currLength = 0; 
        	}
        	
            if (parseWords[x].equals("IDENT")) {

                if (!parseWords[x - 1].equals("STR")) {

                    identHolder = x;
                    identType = parseWords[x - 1];
                    identValue = sourceWords[x + 2];
                    this.st.findVariable(sourceWords[identHolder]).setType(identType);

                    if (parseWords[x + 1].equals("IS")) {
                        this.st.findVariable(sourceWords[identHolder]).setValue(identValue);
                    } else {
                        if (identType == "INT") {
                            this.st.findVariable(sourceWords[identHolder]).setValue("0");
                        } else if (identType == "FLOAT") {
                            this.st.findVariable(sourceWords[identHolder]).setValue("0.0");
                        }
                    }

                } else {
                    identHolder = x;
                    identType = parseWords[x - 1];
                    this.st.findVariable(sourceWords[identHolder]).setType(identType);
                    this.st.findVariable(sourceWords[identHolder]).setValue("");

                }

                defined[x] = sourceWords[x];

            }

        }

//        for (int i = 0; i < parseWords.length; i++) {
//            output += sourceWords[i] + " ";
//        }
//        System.out.println(output);

        checkUndefined(x, sourceWords, defined, indexes[0] + 1, indexes[1]);
        checkExecutionTypes(x, sourceWords, defined, indexes[0] + 1, indexes[1]);

    }

    public void checkUndefined(int index, String[] sourceOutput, String[] defined, int lineNum, int currLength) {
    	System.out.println("CHECK UNDEF");
    	System.out.println("linenum:" + lineNum);
    	System.out.println("currlength:" + currLength);
        String input = this.parsed;
        String input2 = this.source;

        String[] parseStmts = input.split("\n");
        String[] parseStmt = parseStmts[lineNum - 1].split(" ");
        String[] parseWords = input.split("\\s");
        String[] sourceWords = input2.split("\\s");
        
        for (int x = index; x < sourceOutput.length && !parseWords[x].equals("END"); x++) {
        	if(currLength >= parseStmt.length){
        		lineNum++;
        		System.out.println("<NEWLINENUM>:" + lineNum);
        		currLength = 0;        		
        		parseStmt = parseStmts[lineNum - 1].split("\\s");
        	}
        	System.out.println("curr:" + parseWords[x]);
        	System.out.println("curr2:" + parseStmt[currLength]);
        	
            
            if (parseWords[x].equals("IDENT")) {

                if (!isDefined(sourceWords[x], defined)) {
                    error += "(Line #" + lineNum + ")" + " Semantic Error: Error undefined variable " + sourceWords[x] + '\n';
                }

            }
            if(currLength < parseStmt.length){
        		currLength++;
        	}
        }

    }

    public void checkExecutionTypes(int index, String[] sourceOutput, String[] defined, int lineNum, int currLength) {
        String input = this.parsed;
        String input2 = this.source;
        String type = "";

        String[] parseStmts = input.split("\n");
        String[] parseStmt = parseStmts[lineNum - 1].split(" ");
        String[] parseWords = input.split("\\s");
        String[] sourceWords = input2.split("\\s");
        
        for (int x = index; x < sourceOutput.length && !parseWords[x].equals("END"); x++) {
        	if (currLength >= parseStmt.length){
        		lineNum++;
        		System.out.println("NEWLINENUM:" + lineNum);
        		currLength = 0;      
        		parseStmt = parseStmts[lineNum - 1].split("\\s");
        	}
        	
        	System.out.println("curr:" + parseWords[x]);

            if (parseWords[x].equals("INTO")) {
                String op3 = parseWords[x + 4];

                if (helper.isOperator(op3)) {
                    String op4 = parseWords[x + 5];
                    String op5 = parseWords[x + 6];

                    if (helper.isOperator(op4)) {
                        type = exploreExpression(sourceWords, parseWords, x + 5, type, type, x + 5, lineNum);
                    } else if (helper.isOperator(op5)) {
                        type = exploreExpression(sourceWords, parseWords, x + 6, getType(x + 5, sourceWords), type,
                                x + 6, lineNum);
                    } else if (!(getType(x + 4, sourceWords)
                            .equals(getDataTypeOfExpression(op4, op5, sourceWords, x, lineNum)))) {
                        error += "(Line #" + lineNum + ")" + " Semantic Error: Different data type than expected at " + parseWords[x] + " token num " + x + '\n';
                    }

                } else if (helper.isOperand(op3)) {

                    if (getType(x + 4, sourceWords).equals("STR")) {
                        error += "(Line #" + lineNum + ")" + " Semantic Error: Different data type than expected at " + parseWords[x] + " token num " + x + '\n';
                    }

                }

            } else if (helper.isOperator(parseWords[x])) {
                String op1 = parseWords[x + 1];
                String op2 = parseWords[x + 2];

                if (helper.isOperator(op1)) {
                    type = exploreExpression(sourceWords, parseWords, x + 1, type, type, x + 1, lineNum);
                } else if (helper.isOperator(op2)) {
                    type = exploreExpression(sourceWords, parseWords, x + 2, getType(x + 1, sourceWords), type, x + 2, lineNum);
                } else {
                    type = getDataTypeOfExpression(op1, op2, sourceWords, x, lineNum);
                }

                if (parseWords[x].equals("MOD")) {
                    if (!type.equals("INT")) {
                        error += "(Line #" + lineNum + ")" + " Semantic Error: Invalid modulo data type.\n";
                    }
                }

            } else if (helper.isRelational(parseWords[x])) {
                String op1 = parseWords[x + 1];
                String op2 = parseWords[x + 2];

                if (helper.isRelational(op1) || helper.isOperator(op1)) {
                    type = exploreExpression(sourceWords, parseWords, x + 1, type, type, x + 1, lineNum);
                } else if (helper.isRelational(op2) || helper.isOperator(op2)) {
                    type = exploreExpression(sourceWords, parseWords, x + 2, getType(x + 1, sourceWords), type, x + 2, lineNum);
                } else {
                    type = getDataTypeOfExpression(op1, op2, sourceWords, x, lineNum);
                }

            } else if (helper.isConditional(parseWords[x])) {
                String op1 = parseWords[x + 1];

                if (helper.isRelational(op1) || helper.isOperator(op1)) {
                    type = exploreExpression(sourceWords, parseWords, x + 1, type, type, x + 1, lineNum);
                }
                if (type.equals("STR")) {
                    error += "(Line #" + lineNum + ")" + " Semantic Error: String type cannot be of type boolean.\n";
                }
            }
            if(currLength < parseStmt.length){
        		currLength++;
        	}
        }

    }

    public String getErrorMsg() {
        return this.error;
    }

    private String exploreExpression(String[] sourceWords, String[] parseWords, int originalIndex, String ltype,
            String rtype, int runningIndex, int lineNum) {

        if (helper.isOperator(parseWords[runningIndex]) && helper.isOperator(parseWords[runningIndex + 1])) {

            ltype = exploreExpression(sourceWords, parseWords, originalIndex, ltype, rtype, runningIndex + 1, lineNum);
            rtype = exploreExpression(sourceWords, parseWords, originalIndex, ltype, rtype, runningIndex + 2, lineNum);

        } else if (helper.isOperand(parseWords[runningIndex]) && helper.isOperand(parseWords[runningIndex + 1])) {

            return getDataTypeOfExpression(parseWords[runningIndex], parseWords[runningIndex + 1], sourceWords,
                    runningIndex, lineNum);

        } else if (helper.isOperator(parseWords[runningIndex + 1])) {

            ltype = getType(runningIndex, sourceWords);
            rtype = exploreExpression(sourceWords, parseWords, originalIndex, ltype, rtype, runningIndex + 1, lineNum);

        } else if (helper.isOperator(parseWords[runningIndex])) {

            rtype = getType(runningIndex + 1, sourceWords);
            ltype = exploreExpression(sourceWords, parseWords, originalIndex, ltype, rtype, runningIndex + 1, lineNum);

        }

        if (!ltype.equals("") && !rtype.equals("")) {
            if (ltype.equals("STR") || rtype.equals("STR")) {
                error += "(Line #" + lineNum + ")" + " Semantic Error: Expected numeric value but found str.\n";
                return "error";
            }
            return getDataTypeOfExpression(ltype, rtype, sourceWords, originalIndex, lineNum);
        }

        return getDataTypeOfExpression(ltype, rtype, sourceWords, originalIndex, lineNum);
    }

    public String getDataTypeOfExpression(String op1, String op2, String[] sourceWords, int x, int lineNum) {

        if (op1.equals("IDENT") && op2.equals("IDENT")) {
            
            if (!(getType(x + 1, sourceWords).equals(getType(x + 2, sourceWords)))) {
                if (!getType(x + 1, sourceWords).equals("STR")) {
                    return "FLOAT";
                } else {
                    error += "(Line #" + lineNum + ")" + " Semantic Error: error data type.\n";
                    return "ERROR";
                }
            }

            return getType(x + 1, sourceWords);
        } else if (op2.equals("IDENT") && !op1.equals("IDENT")) {

            if (getType(x + 2, sourceWords).equals("INT") && op1.equals("FLOAT_LIT")) {
                return "FLOAT";
            } else if (getType(x + 2, sourceWords).equals("FLOAT") && op1.equals("INT_LIT")) {
                return "FLOAT";
            } else if (getType(x + 2, sourceWords).equals("FLOAT") && op1.equals("FLOAT_LIT")) {
                return "FLOAT";
            } else {
                error += "(Line #" + lineNum + ")" + " Semantic Error: error data type.";
                return "ERROR";
            }

        } else if (op1.equals("IDENT") && !op2.equals("IDENT")) {

            if (getType(x + 1, sourceWords).equals("INT") && op2.equals("FLOAT_LIT")) {
                return "FLOAT";
            } else {
                return "FLOAT";
            }

        } else if (!op1.equals("IDENT") && !op2.equals("IDENT")) {

            if (!op1.equals(op2)) {

                return "FLOAT";

            }
            return "INT";
        } else {
            error += "(Line #" + lineNum + ")" + " Semantic Error: error data type.";
            return "ERROR";
        }

    }

    public boolean isDefined(String ident, String[] defined) {
        for (int x = 0; x < defined.length; x++) {
            if (ident.equals(defined[x])) {
                return true;
            }
        }
        return false;
    }

    public String getType(int index, String[] sourceWords) {
        System.out.println(sourceWords[index]);
        if (sourceWords[index].charAt(0) != '*') {
            
            return this.st.findVariable(sourceWords[index]).getType();

        }
        return "ERROR";

    }

    public void setType(int index, String[] sourceWords, String type) {
        this.st.findVariable(sourceWords[index]).setType(type);
    }

    public void setValue(int index, String[] sourceWords, String value) {
        this.st.findVariable(sourceWords[index]).setValue(value);
    }

    public String getValue(int index, String[] sourceWords) {
        return this.st.findVariable(sourceWords[index]).getValue();
    }

    public SymbolTable getSymbolTable() {
        return this.st;
    }
    
    public int[] getIndexes(String[] stmts){
    	for(int i = 0; i < stmts.length; i++){
    		String[] stmt = stmts[i].split("\n");
    		for(int j = 0; j < stmt.length; j++){
    			if(stmt[j].equals("COMMAND")){
    				System.out.println("LINENUM:" + i);
    				int[] result = {i , j};
    				return result;
    			}
    		}
    	}
    	int[] result = {};
    	return result;
    }

}