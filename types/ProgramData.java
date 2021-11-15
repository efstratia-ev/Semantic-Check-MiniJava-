package types;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class ProgramData {
    int errors;
    Map< String,class_declaration> class_dec;

    public ProgramData(){
        errors=0;
        class_dec= new LinkedHashMap<>();
    }

    public void add_class_dec(String class_name){
        if(class_exists(class_name)){
            System.err.println("Class "+class_name+" already exists");
            errors++;
        }
        class_dec.put(class_name,new class_declaration(class_name));
    }

    public void add_class_dec(String class_name,String super_name){
        if(class_exists(class_name)){
            errors++;
            System.err.println("class "+class_name+" has already been defined in this scope");
        }
        if(class_exists(super_name)) class_dec.put(class_name,new class_declaration(class_name,class_dec.get(super_name)));
        else{
            add_class_dec(class_name);
            errors++;
            System.err.println("Class "+class_name+" extends class "+super_name+", but class "+super_name+" doesn't exist");
        }
    }

    public boolean class_exists(String class_name){
        return class_dec.containsKey(class_name);
    }

    public void print(){
        if(errors!=0) return;
        for (Map.Entry<String,class_declaration> entry : class_dec.entrySet()){
            if(entry.getValue().is_main()) continue;
            System.out.println("-----------Class "+entry.getKey()+"-----------");
            entry.getValue().print(entry.getKey());
        }
    }

    public void add_function_dec(String class_name,String function_name,String function_type){
        if(!class_dec.get(class_name).add_function_dec(function_type,function_name)) errors++;
    }

    public void add_main(String class_name,String id){
        class_dec.get(class_name).add_main(id);
    }

    public void add_var_dec(String class_function_name,String var_name,String var_type){
        String[] names =class_function_name.split(":");
        boolean err;
        if(names.length>1) err=class_dec.get(names[0]).add_var_dec(var_name,names[1],var_type);
        else err=class_dec.get(names[0]).add_var_dec(var_type,var_name);
        if(!err) errors++;
    }

    public void add_function_argument(String class_function_name,String var_name,String var_type){
        String[] names =class_function_name.split(":");
        if(!class_dec.get(names[0]).add_function_argument(var_name,names[1],var_type)) errors++;
    }

    public ArrayList<String> get_available_types(){
        ArrayList<String> types = new ArrayList<>(class_dec.keySet());
        types.add("int");
        types.add("boolean");
        types.add("int[]");
        types.add("boolean[]");
        return types;
    }


    public void check_types_availability(){
        ArrayList<String> types=get_available_types();
        for (Map.Entry<String,class_declaration> entry : class_dec.entrySet()){
            errors+=entry.getValue().check_types_availability(types);
        }
    }

    public void check_errors(){
        check_types_availability();
        for (Map.Entry<String,class_declaration> entry : class_dec.entrySet()){
            errors+=entry.getValue().check_functions();
        }
    }

    String print_identifier(String var){
        if(var.startsWith("id:")) var=var.substring(3);
        return var;
    }

    String get_type(String args,String var){
        if(var.startsWith("id:")) var=var.substring(3);
        else return var;
        String[] names=args.split(":");
        String type;
        if(names.length>1) type=class_dec.get(names[0]).getType(var,names[1]);
        else type=class_dec.get(names[0]).getType(var);
        if(type==null){
            System.err.println("Variable with name "+print_identifier(var)+" has not been declared in this scope");
            errors++;
        }
        return type;
    }

    public String get_array_type(String args,String var){
        if(var.equals("int[]")) return "int";
        if(var.equals("boolean[]")) return "boolean";
        if(var.equals("boolean") || var.equals("int") || var.equals("this")){
            errors++;
            System.err.println("expression needs an array instead of "+var);
            return null;
        }
        String type=get_type(args,var);
        if(type==null) return null;
        if(type.equals("int[]")) return "int";
        else if(type.equals("boolean[]")) return "boolean";
        errors++;
        System.err.println("Variable "+print_identifier(var)+" of type "+type+" is given when an array was expected");
        return null;
    }

    public String get_class(String name){
        name=name.substring(3);
        if(class_exists(name)) return name;
        System.err.println("Class "+name+" is not defined");
        errors++;
        return null;
    }

    public String get_current_class(String name){
        String[] names=name.split(":");
        if(class_exists(names[0])) return names[0];
        System.err.println("Class "+name+" is not defined");
        errors++;
        return null;
    }

    public void get_integer(String args,String var){
        if(var==null) return;
        if(var.equals("int")) return;
        if(var.equals("boolean") || var.equals("boolean[]") || var.equals("int[]") || var.equals("this")){
            errors++;
            System.err.println("expression needs an integer instead of "+var);
            return;
        }
        String type=get_type(args,var);
        if(type==null) return;
        if(!type.equals("int")){
            errors++;
            System.err.println("Variable "+print_identifier(var)+"of type "+type+"is given when an integer was expected");
        }
    }

    public void get_boolean(String args,String var){
        if(var==null) return;
        if(var.equals("boolean")) return;
        if(var.equals("int") || var.equals("boolean[]") || var.equals("int[]") || var.equals("this")){
            errors++;
            System.err.println("expression needs a boolean instead of "+var);
            return;
        }
        String type=get_type(args,var);
        if(type==null) return;
        if(!type.equals("boolean")){
            errors++;
            System.err.println("Variable "+print_identifier(var)+"of type "+type+"is given when an boolean was expected");
        }
    }

    public String get_function_type(String args,String class_name,String function_name,String func_args){
        function_name=function_name.substring(3);
        if(class_name.equals("boolean") || class_name.equals("boolean[]") || class_name.equals("int[]") || class_name.equals("int")){
            errors++;
            System.err.println("A "+print_identifier(class_name)+"does not have any fields to call");
            return null;
        }
        String type;
        if(class_name.equals("this")){
            String[] names=args.split(":");
            type=names[0];
        }
        else type=get_type(args,class_name);
        if(type==null) return null;
        if(!class_exists(type)){
            errors++;
            System.err.println("A "+print_identifier(class_name)+"does not have any fields to call");
            return null;
        }
        ArrayList<String> arguments = new ArrayList<>();
        if(func_args!=null) {
            String[] split_args = func_args.split(",");
            for (String split_arg : split_args) {
                if(split_arg.equals("this")) arguments.add(get_current_class(args));
                else arguments.add(get_type(args, split_arg));
            }
        }
        String return_type=class_dec.get(type).get_function_type(function_name,arguments,this);
        if(return_type==null){
            errors++;
            System.err.println("Class "+type+" has no method named "+function_name);
        }
        else if(return_type.startsWith("err:")){
            errors++;
            return_type=return_type.substring(4);
        }
        return return_type;
    }

    public void check_types(String args,String identifier,String ex_type){
        String id_type=get_type(args,identifier);
        if(id_type==null) return;
        if(ex_type==null) return;
        if(ex_type.equals("this")) ex_type=get_current_class(args);
        ex_type=get_type(args,ex_type);
        if(ex_type==null) return;
        if(id_type.equals(ex_type)) return;
        if(class_exists(id_type) && class_exists(ex_type) && class_dec.get(ex_type).is_super_class(id_type)) return;
        System.err.println("Identifier "+print_identifier(identifier)+" of type "+id_type+" can not be assigned with expression type "+ex_type);
        errors++;
    }

    public void check_types(String args,String identifier,String ex_type,String int_ex){
        if(ex_type==null || int_ex==null) return;
        get_integer(args,int_ex);
        String id_type=get_type(args,identifier);
        if(!id_type.equals("int[]") && !id_type.equals("boolean[]")){
            System.err.println("identifier "+print_identifier(identifier)+" must be an array");
            errors++;
            return;
        }
        String expected_type;
        if(id_type.equals("int[]")) expected_type="int";
        else expected_type="boolean";
        if(expected_type.equals(ex_type)) return;
        if(ex_type.equals("this") || ex_type.equals("int[]") || ex_type.equals("boolean[]")){
            System.err.println("Identifier "+print_identifier(identifier)+" of type "+id_type+" can not be assigned with expression type "+ex_type);
            errors++;
            return;
        }
        ex_type=get_type(args,ex_type);
        if(expected_type.equals(ex_type)) return;
        System.err.println("Identifier "+print_identifier(identifier)+" of type "+id_type+" can not be assigned with expression type "+ex_type);
        errors++;
    }


    public void return_type(String args,String method_name,String ex_type,String type){
        if(type==null) return;
        if(ex_type.startsWith("id:")) ex_type=ex_type.substring(3);
        if(type.startsWith("id:")) type=get_type(args,type);
        if(type.equals("this")) type=get_current_class(args);
        if(type==null || ex_type.equals(type)) return;
        if(class_exists(type) && class_dec.get(type).is_super_class(ex_type)) return;
        errors++;
        System.err.println("Method "+method_name+" expected to return type "+ex_type+" when expression is "+type);
    }
}
