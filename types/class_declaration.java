package types;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class class_declaration {
    String name;
    class_declaration super_class;
    Map< String,String> var_dec;
    Map< String,function_declaration> function_dec;

    public class_declaration(String n){
        name=n;
        super_class=null;
        var_dec = new LinkedHashMap<>();
        function_dec= new LinkedHashMap<>();
    }

    public class_declaration(String n,class_declaration sc){
        name=n;
        super_class=sc;
        var_dec = new LinkedHashMap<>();
        function_dec= new LinkedHashMap<>();
    }

    public boolean add_var_dec(String var_type,String var_name){
        if(var_exists(var_name)){
            System.err.println("variable "+var_name+" has already been defined in this scope");
            return false;
        }
        var_dec.put(var_name,var_type);
        return true;
    }

    public boolean add_function_dec(String function_type,String function_name){
        if(function_exists(function_name)){
            System.err.println("function "+function_name+" has already been defined in this scope");
            return false;
        }
        function_dec.put(function_name,new function_declaration(function_type));
        return true;
    }

    public void add_main(String id){
        function_dec.put("main",new main_declaration(id));
    }

    public boolean var_exists(String var_name){
        return var_dec.containsKey(var_name);
    }

    public boolean function_exists(String function_name){
        return function_dec.containsKey(function_name);
    }

    public boolean function_exists_super(String var_name){
        if(super_class==null) return false;
        else return super_class.function_exists(var_name) || super_class.function_exists_super(var_name);
    }

    public int get_var_counter(){
        int counter=0;
        if(super_class!=null) counter=super_class.get_var_counter();
        for (Map.Entry<String,String> entry : var_dec.entrySet()){
            if(entry.getValue().equals("int")) counter+=4;
            else if(entry.getValue().equals("boolean")) counter+=1;
            else counter +=8;
        }
        return counter;
    }

    public int get_function_counter(){
        int counter=0;
        if(super_class!=null) counter=super_class.get_function_counter();
        for (Map.Entry<String, function_declaration> entry : function_dec.entrySet()){
            if(entry.getValue().is_main()) continue;
            counter+=8;
        }
        return counter;
    }

    public void print(String class_name){
        int counter=0;
        if(super_class!=null) counter=super_class.get_var_counter();
        System.out.println("--Variables---");
        for (Map.Entry<String,String> entry : var_dec.entrySet()){
            System.out.println(class_name+"."+entry.getKey()+" : "+counter);
            if(entry.getValue().equals("int")) counter+=4;
            else if(entry.getValue().equals("boolean")) counter+=1;
            else counter +=8;
        }
        counter=0;
        if(super_class!=null) counter=super_class.get_function_counter();
        System.out.println("---Methods---");
        for (Map.Entry<String,function_declaration> entry : function_dec.entrySet()){
            if(function_exists_super(entry.getKey())) continue;
            System.out.println(class_name+"."+entry.getKey()+" : "+counter);
            counter+=8;
        }
    }

    public boolean add_var_dec(String var_name,String function_name,String var_type){
        return function_dec.get(function_name).add_var_dec(var_type,var_name);
    }

    public boolean add_function_argument(String var_name,String function_name,String var_type){
        return function_dec.get(function_name).add_argument(var_type,var_name);
    }

    public int check_types_availability(ArrayList<String> types){
        int errors=0;
        for (Map.Entry<String,String> entry : var_dec.entrySet()){
            if(!types.contains(entry.getValue())){
                errors++;
                System.err.println("Unknown type "+entry.getValue());
            }
        }
        for (Map.Entry<String,function_declaration> entry : function_dec.entrySet()){
            errors+=entry.getValue().check_types_availability(types);
        }
        return errors;
    }

    public boolean check_functions(String name,function_declaration dec){
        return super_class.function_dec.get(name).check_functions(dec);
    }

    public int check_functions(){
        int errors=0;
        if(super_class==null) return errors;
        for (Map.Entry<String,function_declaration> entry : function_dec.entrySet()){
            if(super_class.function_exists(entry.getKey())){
                if(!check_functions(entry.getKey(),entry.getValue())){
                    System.err.println("function can not be overload");
                    errors++;
                }
            }
        }
        return errors;
    }

    String getType(String var){
        if(var_exists(var)) return var_dec.get(var);
        if(super_class!=null) return super_class.getType(var);
        return null;
    }

    String getType(String var,String function){
        String type=function_dec.get(function).getType(var);
        if(type==null) type=getType(var);
        return type;
    }

    String get_function_type(String function,ArrayList<String> func_args,ProgramData data){
        if(function_exists(function)){
            String err="";
            if(!function_dec.get(function).check_arguments(func_args,data)) err="err:";
            return err+function_dec.get(function).type;
        }
        if(super_class==null) return null;
        return super_class.get_function_type(function,func_args,data);
    }

    boolean is_super_class(String class_name){
        if(super_class==null) return false;
        if(super_class.name.equals(class_name)) return true;
        return super_class.is_super_class(class_name);
    }

    public boolean is_main(){
        if(function_dec.size()!=1) return false;
        for (Map.Entry<String,function_declaration> entry : function_dec.entrySet()){
            if(entry.getValue().is_main()) return true;
        }
        return false;
    }
}
