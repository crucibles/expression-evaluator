import java.security.cert.Extension;
import java.sql.Array;

public class SemanticsHandler {
    public ExpressionEvaluator ee;
    public NRPP nrpp;
    private SymbolTable sTable;
    private String input;

    public SemanticsHandler() {
        this.input = "";
        this.sTable = new SymbolTable();
        assignTypes();
    }

    public SemanticsHandler(SymbolTable st, String evaluatee) {
        this.sTable = st;
        this.input = evaluatee;
    }

    public void semanticChecker(){
        
        ee.symbolTables.get(0).findVariable("x");
    }

    public void assignTypes() {
        String input = "DEFINE INT IDENT IS INT_LIT END COMMAND ADD IDENT INT_LIT END";
        int firstEnd = 0;
        int identHolder = 0;
        String identType = "";
        String[] inputtedWords = nrpp.splitter(input);

        for (int i = 0; i <= inputtedWords.length; i++) {
            if(inputtedWords[i].equals("END")){
                firstEnd = i;
                break;
            }
        }

        for(int x = 0; x <= inputtedWords.length ; x++){
            if(inputtedWords[x].equals("IDENT")){
                identHolder = x;
                identType = inputtedWords[x-1];
                break;
            }
        }

        


    }
}