
public class SemanticsHandler {
    public String parsed;
    public String source;
    public SymbolTable st;
    public String error;

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
        String output = "";
        String[] parseWords = input.split("\\s");
        String[] sourceWords = input2.split("\\s");
        String[] defined = new String[parseWords.length];
        int x;

        for (x = 0; x < parseWords.length && !parseWords[x].equals("COMMAND"); x++) {

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

        for (int i = 0; i < parseWords.length; i++) {
            output += sourceWords[i] + " ";
        }
        System.out.println(output);

        checkUndefined(x, sourceWords, defined);
        checkExecutionTypes(x, sourceWords, defined);

    }

    public void checkUndefined(int index, String[] sourceOutput, String[] defined) {
        String input = this.parsed;
        String input2 = this.source;

        String[] parseWords = input.split("\\s");
        String[] sourceWords = input2.split("\\s");


        for (int x = index; x < sourceOutput.length && !parseWords[x].equals("END"); x++) {
            
            if (parseWords[x].equals("IDENT")) {

                if (!isDefined(sourceWords[x], defined)) {
                    error += "Error undefined variable " + sourceWords[x] + " at token number " + x + '\n';
                }

            }

        }

    }

    public void checkExecutionTypes(int index, String[] sourceOutput, String[] defined) {
        String input = this.parsed;
        String input2 = this.source;
        String type = "";

        String[] parseWords = input.split("\\s");
        String[] sourceWords = input2.split("\\s");

        for (int x = index; x < sourceOutput.length && !parseWords[x].equals("END"); x++) {

            if (parseWords[x].equals("INTO")) {
                String op3 = parseWords[x + 4];

                if (isOperator(op3)) {
                    String op4 = parseWords[x + 5];
                    String op5 = parseWords[x + 6];

                    if (isOperator(op4)) {
                        type = exploreExpression(sourceWords, parseWords, x + 5, type, type, x + 5);
                    } else if (isOperator(op5)) {
                        type = exploreExpression(sourceWords, parseWords, x + 6, getType(x + 5, sourceWords), type,
                                x + 6);
                    } else if (!(getType(x + 4, sourceWords)
                            .equals(getDataTypeOfExpression(op4, op5, sourceWords, x)))) {
                        error += "2Different data type than expected at " + parseWords[x] + " token num " + x + '\n';
                    }

                } else if (isOperand(op3)) {

                    if (getType(x + 4, sourceWords).equals("STR")) {
                        error += "1Different data type than expected at " + parseWords[x] + " token num " + x + '\n';
                    }

                }

            } else if (isOperator(parseWords[x])) {
                String op1 = parseWords[x + 1];
                String op2 = parseWords[x + 2];

                if (isOperator(op1)) {
                    type = exploreExpression(sourceWords, parseWords, x + 1, type, type, x + 1);
                } else if (isOperator(op2)) {
                    type = exploreExpression(sourceWords, parseWords, x + 2, getType(x + 1, sourceWords), type, x + 2);
                } else {
                    type = getDataTypeOfExpression(op1, op2, sourceWords, x);
                }

                if (parseWords[x].equals("MOD")) {
                    if (!type.equals("INT")) {
                        error += "Invalid modulo data type at token number " + x +".\n";
                    }
                }

            } else if (isRelational(parseWords[x])) {
                String op1 = parseWords[x + 1];
                String op2 = parseWords[x + 2];

                if (isRelational(op1) || isOperator(op1)) {
                    type = exploreExpression(sourceWords, parseWords, x + 1, type, type, x + 1);
                } else if (isRelational(op2) || isOperator(op2)) {
                    type = exploreExpression(sourceWords, parseWords, x + 2, getType(x + 1, sourceWords), type, x + 2);
                } else {
                    type = getDataTypeOfExpression(op1, op2, sourceWords, x);
                }

            } else if (isConditional(parseWords[x])) {
                String op1 = parseWords[x + 1];

                if (isRelational(op1) || isOperator(op1)) {
                    type = exploreExpression(sourceWords, parseWords, x + 1, type, type, x + 1);
                }
                if (type.equals("STR")) {
                    error += "String type cannot be of type boolean.\n";
                }
            }

        }

    }

    public String getErrorMsg() {
        return this.error;
    }

    private String exploreExpression(String[] sourceWords, String[] parseWords, int originalIndex, String ltype,
            String rtype, int runningIndex) {

        if (isOperator(parseWords[runningIndex]) && isOperator(parseWords[runningIndex + 1])) {

            ltype = exploreExpression(sourceWords, parseWords, originalIndex, ltype, rtype, runningIndex + 1);
            rtype = exploreExpression(sourceWords, parseWords, originalIndex, ltype, rtype, runningIndex + 2);

        } else if (isOperand(parseWords[runningIndex]) && isOperand(parseWords[runningIndex + 1])) {

            return getDataTypeOfExpression(parseWords[runningIndex], parseWords[runningIndex + 1], sourceWords,
                    runningIndex);

        } else if (isOperator(parseWords[runningIndex + 1])) {

            ltype = getType(runningIndex, sourceWords);
            rtype = exploreExpression(sourceWords, parseWords, originalIndex, ltype, rtype, runningIndex + 1);

        } else if (isOperator(parseWords[runningIndex])) {

            rtype = getType(runningIndex + 1, sourceWords);
            ltype = exploreExpression(sourceWords, parseWords, originalIndex, ltype, rtype, runningIndex + 1);

        }

        if (!ltype.equals("") && !rtype.equals("")) {
            if (ltype.equals("STR") || rtype.equals("STR")) {
                error += "expected numeric value but found str.\n";
                return "error";
            }
            return getDataTypeOfExpression(ltype, rtype, sourceWords, originalIndex);
        }

        return getDataTypeOfExpression(ltype, rtype, sourceWords, originalIndex);
    }

    private boolean isOperand(String word) {

        String[] operands = { "IDENT", "INT_LIT", "FLOAT_LIT" };
        for (int i = 0; i < operands.length; i++) {
            if (word.equals(operands[i])) {
                return true;
            }
        }
        return false;

    }

    public String getDataTypeOfExpression(String op1, String op2, String[] sourceWords, int x) {

        if (op1.equals("IDENT") && op2.equals("IDENT")) {
            
            if (!(getType(x + 1, sourceWords).equals(getType(x + 2, sourceWords)))) {
                if (!getType(x + 1, sourceWords).equals("STR")) {
                    return "FLOAT";
                } else {
                    error += "1error data type at token " + x + "\n";
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
                error += "2error data type at token " + x + "\n";
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
            error += "3error data type at token " + x + "\n";
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

    public boolean isOperator(String input) {
        String[] operators = { "ADD", "SUB", "MULT", "DIV", "MOD" };

        for (int i = 0; i < operators.length; i++) {
            if (input.equals(operators[i])) {
                return true;
            }
        }
        return false;
    }

    public boolean isRelational(String input) {
        String[] operators = { "GT?", "GTE?", "LT?", "LTE?", "EQ?", "NEQ?", "AND?", "OR?", "NOT?" };

        for (int i = 0; i < operators.length; i++) {
            if (input.equals(operators[i])) {
                return true;
            }
        }
        return false;
    }

    public boolean isConditional(String input) {

        if (input.equals("IF")) {
            return true;
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

}