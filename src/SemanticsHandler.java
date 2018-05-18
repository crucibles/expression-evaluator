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
        System.out.println("===================================================");
        System.out.println(this.source);
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

        System.out.println("asdasdasdas" + parseWords.length);
        System.out.println(sourceWords.length);

        for (int x = index; x < sourceOutput.length && !parseWords[x].equals("END"); x++) {
            System.out.println(x);
            if (parseWords[x].equals("IDENT")) {
                System.out.println("im in");
                System.out.println(sourceWords[x]);
                if (!isDefined(sourceWords[x], defined)) {
                    System.out.println("undef");
                    error += "Error undefined variable " + sourceWords[x] + " at token number " + x + '\n';
                }
            }

        }
        System.out.println(error);

    }

    public void checkExecutionTypes(int index, String[] sourceOutput, String[] defined) {
        String input = this.parsed;
        String input2 = this.source;

        String[] parseWords = input.split("\\s");
        String[] sourceWords = input2.split("\\s");

        for (int x = index; x < sourceOutput.length && !parseWords[x].equals("END"); x++) {

            if (isConditional(parseWords[x])) {
                String op1 = parseWords[x + 1];

                if (op1.equals("0") || getValue(x + 1, sourceWords).equals("0")) {
                    sourceWords[x + 1] = "false";
                }

                sourceWords[x + 1] = "true";
                x++;
            } else if (isRelational(parseWords[x])) {

                String op1 = parseWords[x + 1];
                String op2 = parseWords[x + 2];

                if (op1.equals("IDENT") && op2.equals("IDENT")) {

                    if (!getValue(x + 1, sourceWords).equals("") && !getValue(x + 1, sourceWords).equals("0")) {
                        setValue(x + 1, sourceWords, "true");
                    } else {
                        setValue(x + 1, sourceWords, "false");
                    }

                    if (!getValue(x + 2, sourceWords).equals("") && !getValue(x + 2, sourceWords).equals("0")) {
                        setValue(x + 2, sourceWords, "true");
                    } else {
                        setValue(x + 2, sourceWords, "false");
                    }

                } else if (op2.equals("IDENT") && !op1.equals("IDENT")) {

                    if (!getValue(x + 2, sourceWords).equals("") && !getValue(x + 2, sourceWords).equals("0")) {
                        setValue(x + 2, sourceWords, "true");
                    } else {
                        setValue(x + 2, sourceWords, "false");
                    }

                    if (!op1.equals("0")) {
                        sourceWords[x + 1] = "true";
                    } else {
                        sourceWords[x + 1] = "false";
                    }

                } else if (op1.equals("IDENT") && !op2.equals("IDENT")) {

                    if (!getValue(x + 1, sourceWords).equals("") && !getValue(x + 1, sourceWords).equals("0")) {
                        setValue(x + 1, sourceWords, "true");
                    } else {
                        setValue(x + 1, sourceWords, "false");
                    }

                    if (!op2.equals("0")) {
                        sourceWords[x + 2] = "true";
                    } else {
                        sourceWords[x + 2] = "false";
                    }

                } else {
                    if (!op1.equals("0")) {
                        sourceWords[x + 1] = "true";
                    } else {
                        sourceWords[x + 1] = "false";
                    }

                    if (!op2.equals("0")) {
                        sourceWords[x + 2] = "true";
                    } else {
                        sourceWords[x + 2] = "false";
                    }
                }

                x += 2;

            } else if (isOperator(parseWords[x])) {

                String op1 = parseWords[x + 1];
                String op2 = parseWords[x + 2];

                if (op1.equals("IDENT") && op2.equals("IDENT")) {
                    if (!(getType(x + 1, sourceWords).equals(getType(x + 2, sourceWords)))) {
                        if (getType(x + 1, sourceWords).equals("INT")) {
                            setType(x + 1, sourceWords, "FLOAT");
                            setValue(x + 1, sourceWords, getValue(x + 1, sourceWords) + ".00");
                        }
                    }
                } else if (op2.equals("IDENT") && !op1.equals("IDENT")) {

                    if (getType(x + 2, sourceWords).equals("INT") && op1.equals("FLOAT_LIT")) {
                        setType(x + 2, sourceWords, "FLOAT");
                        setValue(x + 2, sourceWords, getValue(x + 1, sourceWords) + ".00");
                    } else {
                        sourceWords[x + 1] = sourceWords[x + 1] + ".00";
                    }

                } else if (op1.equals("IDENT") && !op2.equals("IDENT")) {

                    if (getType(x + 1, sourceWords).equals("INT") && op2.equals("FLOAT_LIT")) {
                        setType(x + 1, sourceWords, "FLOAT");
                        setValue(x + 1, sourceWords, getValue(x + 1, sourceWords) + ".00");
                    } else {
                        sourceWords[x + 2] = sourceWords[x + 2] + ".00";
                    }

                } else {

                    if (!op1.equals(op2)) {

                        if (op1.equals("INT_LIT")) {
                            sourceWords[x + 1] = sourceWords[x + 1] + ".00";
                        } else {
                            sourceWords[x + 2] = sourceWords[x + 2] + ".00";
                        }

                    }

                }

                x += 2;

            } else if (parseWords[x].equals("INTO")) {
                String op1 = parseWords[x + 1];
                String op2 = parseWords[x + 3];
                String op3 = parseWords[x + 4];
                String op4 = parseWords[x + 5];
                String op5 = parseWords[x + 6];

                if (op2.equals("IDENT")) {

                    if (!(getType(x + 3, sourceWords).equals(getType(x + 1, sourceWords)))) {
                        error += "Different data type than expected" + '\n';
                    }

                } else if (isOperator(op3)) {

                    if (!(getType(x + 4, sourceWords).equals(getDataTypeOfExpression(op3, op4, sourceWords, x)))) {
                        error += "Different data type than expected " + '\n';
                    }

                } else {
                    error += "error data type near INTO at token number " + x + "\n";
                }

            }

        }

    }

    public String getErrorMsg() {
        return this.error;
    }

    public String getDataTypeOfExpression(String op1, String op2, String[] sourceWords, int x) {

        if (op1.equals("IDENT") && op2.equals("IDENT")) {
            if (!(getType(x + 1, sourceWords).equals(getType(x + 2, sourceWords)))) {
                if (getType(x + 1, sourceWords).equals("INT")) {
                    setType(x + 1, sourceWords, "FLOAT");
                    setValue(x + 1, sourceWords, getValue(x + 1, sourceWords) + ".00");
                    return "FLOAT";
                } else if (getType(x + 2, sourceWords).equals("INT")) {
                    setType(x + 2, sourceWords, "FLOAT");
                    setValue(x + 2, sourceWords, getValue(x + 2, sourceWords) + ".00");
                    return "FLOAT";
                } else {
                    error += "error data type";
                    return "ERROR";
                }
            }
            return "INT";
        } else if (op2.equals("IDENT") && !op1.equals("IDENT")) {

            if (getType(x + 2, sourceWords).equals("INT") && op1.equals("FLOAT_LIT")) {
                setType(x + 2, sourceWords, "FLOAT");
                setValue(x + 2, sourceWords, getValue(x + 2, sourceWords) + ".00");
                return "FLOAT";
            } else if (getType(x + 2, sourceWords).equals("FLOAT") && op1.equals("INT_LIT")) {
                sourceWords[x + 1] = sourceWords[x + 1] + ".00";
                return "FLOAT";
            } else if (getType(x + 2, sourceWords).equals("FLOAT") && op1.equals("FLOAT_LIT")) {
                return "FLOAT";
            } else {
                error += "error data type";
                return "ERROR";
            }

        } else if (op1.equals("IDENT") && !op2.equals("IDENT")) {

            if (getType(x + 1, sourceWords).equals("INT") && op2.equals("FLOAT_LIT")) {
                setType(x + 1, sourceWords, "FLOAT");
                setValue(x + 1, sourceWords, getValue(x + 1, sourceWords) + ".00");
                return "FLOAT";
            } else {
                sourceWords[x + 2] = sourceWords[x + 2] + ".00";
                return "FLOAT";
            }
        } else if (!op1.equals("IDENT") && !op2.equals("IDENT")) {

            if (!op1.equals(op2)) {

                if (op1.equals("INT_LIT")) {
                    sourceWords[x + 1] = sourceWords[x + 1] + ".00";
                    return "FLOAT";
                } else {
                    sourceWords[x + 2] = sourceWords[x + 2] + ".00";
                    return "FLOAT";
                }

            }
            return "INT";
        } else {
            error += "error data type";
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
        return this.st.findVariable(sourceWords[index]).getType();
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