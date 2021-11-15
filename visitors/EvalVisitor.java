package visitors;

import syntaxtree.*;
import types.ProgramData;
import visitor.GJDepthFirst;

public class EvalVisitor extends GJDepthFirst<String, String>{

    ProgramData data;

    public EvalVisitor(ProgramData data){
        super();
        this.data=data;
    }

    /**
     * f0 -> Type()
     * f1 -> Identifier()
     * f2 -> ";"
     */
    public String visit(VarDeclaration n, String argu) {
        String _ret=null;
        String type,identifier;
        type=n.f0.accept(this, argu);
        identifier=n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        data.add_var_dec(argu,identifier,type);
        return _ret;
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

    public String visit(NodeToken n, String argu) {
        return n.toString();
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
        String identifier,type;
        n.f0.accept(this, argu);
        type=n.f1.accept(this, argu);
        identifier=n.f2.accept(this, argu);
        data.add_function_dec(argu,identifier,type);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu+":"+identifier);
        n.f5.accept(this, argu);
        n.f6.accept(this, argu);
        n.f7.accept(this, argu+":"+identifier);
        n.f8.accept(this, argu);
        n.f9.accept(this, argu);
        n.f10.accept(this, argu);
        n.f11.accept(this, argu);
        n.f12.accept(this, argu);
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
        String identifier;
        n.f0.accept(this, argu);
        identifier=n.f1.accept(this, argu);
        data.add_class_dec(identifier);
        n.f2.accept(this, argu);
        n.f3.accept(this, identifier);
        n.f4.accept(this, identifier);
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
        String identifier,superclass;
        n.f0.accept(this, argu);
        identifier=n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        superclass=n.f3.accept(this, argu);
        data.add_class_dec(identifier,superclass);
        n.f4.accept(this, argu);
        n.f5.accept(this, identifier);
        n.f6.accept(this, identifier);
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
        String identifier,arg;
        n.f0.accept(this, argu);
        identifier=n.f1.accept(this, argu);
        data.add_class_dec(identifier);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        n.f6.accept(this, argu);
        n.f7.accept(this, argu);
        n.f8.accept(this, argu);
        n.f9.accept(this, argu);
        n.f10.accept(this, argu);
        arg=n.f11.accept(this, argu);
        data.add_main(identifier,arg);
        n.f12.accept(this, argu);
        n.f13.accept(this, argu);
        n.f14.accept(this, identifier+":main");
        n.f15.accept(this, argu);
        n.f16.accept(this, argu);
        n.f17.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> Type()
     * f1 -> Identifier()
     */
    public String visit(FormalParameter n, String argu) {
        String _ret=null;
        String type,identifier;
        type=n.f0.accept(this, argu);
        identifier=n.f1.accept(this, argu);
        data.add_function_argument(argu,identifier,type);
        return _ret;
    }

}
