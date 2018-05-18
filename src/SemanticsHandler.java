public class SemanticsHandler {
    public String parsed;
    public String source;
    public SymbolTable st;

    public SemanticsHandler(String parsed, String source, SymbolTable st) {
        this.parsed = parsed;
        this.source = source;
        this.st = st;
        semanticChecker();
    }

    public void semanticChecker() {
        assignTypes();
    }

    public String[] assignTypes() {
        String input = this.parsed;
        String input2 = this.source;
        int identHolder = 0;
        String identType = "";
        String identValue = "";
        String output = "";
        String[] parseWords = input.split("\\s");
        String[] sourceWords = input2.split("\\s");

        System.out.println("asdasdasdas" + parseWords.length);
        System.out.println(sourceWords.length);

        for (int x = 0; x < parseWords.length && !parseWords[x].equals("COMMAND"); x++) {

            if (isConditional(parseWords[x])) {
                String op1 = parseWords[x + 1];

                if (op1.equals("0")) {
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

            } else if (parseWords[x].equals("IDENT")) {

                if (!parseWords[x - 1].equals("STR")) {

                    identHolder = x;
                    identType = parseWords[x - 1];
                    identValue = sourceWords[x + 2];
                    this.st.findVariable(sourceWords[identHolder]).setType(identType);

                    if (sourceWords[x + 1].equals("IS")) {
                        this.st.findVariable(sourceWords[identHolder]).setValue(identValue);
                    } else {
                        System.out.println("dakshgdjhasgdhjasgjhdgashjhdghjasgdjhasgdgasdgasjgdjhasgdjasgdgajsgdjashgjg");
                        if (identType.equals("INT")) {
                            this.st.findVariable(sourceWords[identHolder]).setValue("0");
                        } else if (identType.equals("FLOAT")) {
                            this.st.findVariable(sourceWords[identHolder]).setValue("0.0");
                        }
                    }

                } else {
                    identHolder = x;
                    identType = parseWords[x - 1];
                    this.st.findVariable(sourceWords[identHolder]).setType(identType);
                    this.st.findVariable(sourceWords[identHolder]).setValue("");

                }

            }

        }

        for (int i = 0; i < parseWords.length; i++) {
            output += sourceWords[i] + " ";
        }
        System.out.println(output);
        return sourceWords;

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