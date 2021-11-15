package visitors;

import syntaxtree.*;
import types.ProgramData;
import visitor.GJDepthFirst;

public class StatementVisitor extends GJDepthFirst<String, String> {
    ProgramData data;
    String args;

    public StatementVisitor(ProgramData data){
        super();
        this.data=data;
    }


    /**
     * f0 -> "boolean"
     * f1 -> "["
     * f2 -> "]"
     */
    public String visit(BooleanArrayType n, String argu) {
        String _ret="boolean[]";
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "int"
     * f1 -> "["
     * f2 -> "]"
     */
    public String visit(IntegerArrayType n, String argu) {
        String _ret="int[]";
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> <IDENTIFIER>
     */
    public String visit(Identifier n, String argu) {
        return "id:"+n.f0.accept(this, argu);
    }

    public String visit(NodeToken n, String argu) {
        return n.toString();
    }

    /**
     * f0 -> <INTEGER_LITERAL>
     */
    public String visit(IntegerLiteral n, String argu) {
        n.f0.accept(this, argu);
        return "int";
    }


    /**
     * f0 -> "true"
     */
    public String visit(TrueLiteral n, String argu) {
         n.f0.accept(this, argu);
         return "boolean";
    }

    /**
     * f0 -> "false"
     */
    public String visit(FalseLiteral n, String argu) {
        n.f0.accept(this, argu);
        return "boolean";
    }

    /**
     * f0 -> "new"
     * f1 -> Identifier()
     * f2 -> "("
     * f3 -> ")"
     */
    public String visit(AllocationExpression n, String argu) {
        String id,class_name;
        n.f0.accept(this, argu);
        id=n.f1.accept(this, argu);
        class_name=data.get_class(id);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        return class_name;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "<"
     * f2 -> PrimaryExpression()
     */
    public String visit(CompareExpression n, String argu) {
        String ex1,ex2;
        ex1=n.f0.accept(this, argu);
        data.get_integer(argu,ex1);
        n.f1.accept(this, argu);
        ex2=n.f2.accept(this, argu);
        data.get_integer(argu,ex2);
        return "boolean";
    }

    public String visit(PlusExpression n, String argu) {
        String ex1,ex2;
        ex1=n.f0.accept(this, argu);
        data.get_integer(argu,ex1);
        n.f1.accept(this, argu);
        ex2=n.f2.accept(this, argu);
        data.get_integer(argu,ex2);
        return "int";
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "-"
     * f2 -> PrimaryExpression()
     */
    public String visit(MinusExpression n, String argu) {
        String ex1,ex2;
        ex1=n.f0.accept(this, argu);
        data.get_integer(argu,ex1);
        n.f1.accept(this, argu);
        ex2=n.f2.accept(this, argu);
        data.get_integer(argu,ex2);
        return "int";
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "*"
     * f2 -> PrimaryExpression()
     */
    public String visit(TimesExpression n, String argu) {
        String ex1,ex2;
        ex1=n.f0.accept(this, argu);
        data.get_integer(argu,ex1);
        n.f1.accept(this, argu);
        ex2=n.f2.accept(this, argu);
        data.get_integer(argu,ex2);
        return "int";
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "["
     * f2 -> PrimaryExpression()
     * f3 -> "]"
     */
    public String visit(ArrayLookup n, String argu) {
        String ex1,ex2,type;
        ex1=n.f0.accept(this, argu);
        type=data.get_array_type(argu,ex1);
        n.f1.accept(this, argu);
        ex2=n.f2.accept(this, argu);
        data.get_integer(argu,ex2);
        n.f3.accept(this, argu);
        return type;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "."
     * f2 -> "length"
     */
    public String visit(ArrayLength n, String argu) {
        String ex;
        ex=n.f0.accept(this, argu);
        data.get_array_type(argu,ex);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return "int";
    }

    /**
     * f0 -> "new"
     * f1 -> "boolean"
     * f2 -> "["
     * f3 -> Expression()
     * f4 -> "]"
     */
    public String visit(BooleanArrayAllocationExpression n, String argu) {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        String ex=n.f3.accept(this, argu);
        data.get_integer(argu,ex);
        n.f4.accept(this, argu);
        return "boolean[]";
    }

    /**
     * f0 -> "new"
     * f1 -> "int"
     * f2 -> "["
     * f3 -> Expression()
     * f4 -> "]"
     */
    public String visit(IntegerArrayAllocationExpression n, String argu) {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        String ex=n.f3.accept(this, argu);
        data.get_integer(argu,ex);
        n.f4.accept(this, argu);
        return "int[]";
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "."
     * f2 -> Identifier()
     * f3 -> "("
     * f4 -> ( ExpressionList() )?
     * f5 -> ")"
     */
    public String visit(MessageSend n, String argu) {
        String _ret;
        String class_name,function_name;
        class_name=n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        function_name=n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        args=null;
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        _ret=data.get_function_type(argu,class_name,function_name,args);
        return _ret;
    }

    /**
     * f0 -> Expression()
     * f1 -> ExpressionTail()
     */
    public String visit(ExpressionList n, String argu) {
        String _ret=null;
        String ex;
        ex=n.f0.accept(this, argu);
        if(ex!=null) args=ex;
        n.f1.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> ","
     * f1 -> Expression()
     */
    public String visit(ExpressionTerm n, String argu) {
        String ex;
        n.f0.accept(this, argu);
        ex=n.f1.accept(this, argu);
        if(ex!=null) args+=","+ex;
        return null;
    }

    /**
     * f0 -> NotExpression()
     *       | PrimaryExpression()
     */
    public String visit(Clause n, String argu) {
        return n.f0.accept(this, argu);
    }

    /**
     * f0 -> Clause()
     * f1 -> "&&"
     * f2 -> Clause()
     */
    public String visit(AndExpression n, String argu) {
        String c1,c2;
        c1=n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        c2=n.f2.accept(this, argu);
        data.get_boolean(argu,c1);
        data.get_boolean(argu,c2);
        return "boolean";
    }

    /**
     * f0 -> "!"
     * f1 -> Clause()
     */
    public String visit(NotExpression n, String argu) {
        n.f0.accept(this, argu);
        String ex=n.f1.accept(this, argu);
        data.get_boolean(argu,ex);
        return "boolean";
    }

    /**
     * f0 -> "while"
     * f1 -> "("
     * f2 -> Expression()
     * f3 -> ")"
     * f4 -> Statement()
     */
    public String visit(WhileStatement n, String argu) {
        String _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        String ex=n.f2.accept(this, argu);
        data.get_boolean(argu,ex);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "if"
     * f1 -> "("
     * f2 -> Expression()
     * f3 -> ")"
     * f4 -> Statement()
     * f5 -> "else"
     * f6 -> Statement()
     */
    public String visit(IfStatement n, String argu) {
        String _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        String ex=n.f2.accept(this, argu);
        data.get_boolean(argu,ex);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        n.f6.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> Identifier()
     * f1 -> "="
     * f2 -> Expression()
     * f3 -> ";"
     */
    public String visit(AssignmentStatement n, String argu) {
        String _ret=null;
        String id,extype;
        id=n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        extype=n.f2.accept(this, argu);
        data.check_types(argu,id,extype);
        n.f3.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> Identifier()
     * f1 -> "["
     * f2 -> Expression()
     * f3 -> "]"
     * f4 -> "="
     * f5 -> Expression()
     * f6 -> ";"
     */
    public String visit(ArrayAssignmentStatement n, String argu) {
        String _ret=null;
        String id,extype,intex;
        id=n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        intex=n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        extype=n.f5.accept(this, argu);
        n.f6.accept(this, argu);
        data.check_types(argu,id,extype,intex);
        return _ret;
    }

    /**
     * f0 -> "public"
     * f1 -> Type()
     * f2 -> Identifier()
     * f3 -> "("
     * f4 -> ( FormalParameterList() )?
     * f5 -> ")"
     * f6 -> "{"
     * f7 -> ( VarDeclaration() )*
     * f8 -> ( Statement() )*
     * f9 -> "return"
     * f10 -> Expression()
     * f11 -> ";"
     * f12 -> "}"
     */
    public String visit(MethodDeclaration n, String argu) {
        String _ret=null;
        String type,return_type,method_name;
        n.f0.accept(this, argu);
        type=n.f1.accept(this, argu);
        method_name=n.f2.accept(this, argu);
        method_name=method_name.substring(3);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        n.f6.accept(this, argu);
        n.f7.accept(this, argu);
        n.f8.accept(this, argu+method_name+":");
        n.f9.accept(this, argu);
        return_type=n.f10.accept(this, argu+method_name+":");
        n.f11.accept(this, argu);
        n.f12.accept(this, argu);
        data.return_type(argu+method_name+":",method_name,type,return_type);
        return _ret;
    }

    /**
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "{"
     * f3 -> ( VarDeclaration() )*
     * f4 -> ( MethodDeclaration() )*
     * f5 -> "}"
     */
    public String visit(ClassDeclaration n, String argu) {
        String _ret=null;
        String name;
        n.f0.accept(this, argu);
        name=n.f1.accept(this, argu);
        name=name.substring(3);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, name+":");
        n.f5.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "extends"
     * f3 -> Identifier()
     * f4 -> "{"
     * f5 -> ( VarDeclaration() )*
     * f6 -> ( MethodDeclaration() )*
     * f7 -> "}"
     */
    public String visit(ClassExtendsDeclaration n, String argu) {
        String _ret=null;
        String name;
        n.f0.accept(this, argu);
        name=n.f1.accept(this, argu);
        name=name.substring(3);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        n.f6.accept(this, name+":");
        n.f7.accept(this, argu);
        return _ret;
    }


    /**
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "{"
     * f3 -> "public"
     * f4 -> "static"
     * f5 -> "void"
     * f6 -> "main"
     * f7 -> "("
     * f8 -> "String"
     * f9 -> "["
     * f10 -> "]"
     * f11 -> Identifier()
     * f12 -> ")"
     * f13 -> "{"
     * f14 -> ( VarDeclaration() )*
     * f15 -> ( Statement() )*
     * f16 -> "}"
     * f17 -> "}"
     */
    public String visit(MainClass n, String argu) {
        String _ret=null;
        String id;
        n.f0.accept(this, argu);
        id=n.f1.accept(this, argu);
        id=id.substring(3);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        n.f6.accept(this, argu);
        n.f7.accept(this, argu);
        n.f8.accept(this, argu);
        n.f9.accept(this, argu);
        n.f10.accept(this, argu);
        n.f11.accept(this, argu);
        n.f12.accept(this, argu);
        n.f13.accept(this, argu);
        n.f14.accept(this, argu);
        n.f15.accept(this, id+":main");
        n.f16.accept(this, argu);
        n.f17.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "System.out.println"
     * f1 -> "("
     * f2 -> Expression()
     * f3 -> ")"
     * f4 -> ";"
     */
    public String visit(PrintStatement n, String argu) {
        String ex;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        ex=n.f2.accept(this, argu);
        data.get_integer(argu,ex);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        return null;
    }

    /**
     * f0 -> "("
     * f1 -> Expression()
     * f2 -> ")"
     */
    public String visit(BracketExpression n, String argu) {
        String _ret;
        n.f0.accept(this, argu);
        _ret=n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return _ret;
    }
}
