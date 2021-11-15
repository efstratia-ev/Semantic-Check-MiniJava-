import syntaxtree.*;
import types.ProgramData;
import visitors.EvalVisitor;
import visitors.StatementVisitor;

import java.io.*;

public class Main {
    public static void main (String [] args) {
        if (args.length < 1) {
            System.err.println("Usage: java Main <inputFile>");
            System.exit(1);
        }
        java.lang.System.setErr(java.lang.System.out);
        for (int i = 0; i < args.length; i++) {
            FileInputStream fis = null;
            try {
                System.out.println("----------------------"+args[i]+"----------------------");
                fis = new FileInputStream(args[i]);
                MiniJavaParser parser = new MiniJavaParser(fis);
                Goal root = parser.Goal();
                System.err.println("Program parsed successfully.");
                ProgramData data=new ProgramData();
                EvalVisitor eval = new EvalVisitor(data);
                root.accept(eval, null);
                data.check_errors();
                StatementVisitor statementVisitor = new StatementVisitor(data);
                root.accept(statementVisitor, null);
                data.print();
                System.out.flush();
                System.err.flush();
            } catch (ParseException ex) {
                System.out.println(ex.getMessage());
            } catch (FileNotFoundException ex) {
                System.err.println(ex.getMessage());
            } finally {
                try {
                    if (fis != null) fis.close();
                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }
            }
        }
    }
}
