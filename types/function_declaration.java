package types;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class function_declaration {
    String type;
    Map< String,String> var_dec;
    Map< String,String> arguments;

    public function_declaration(String type) {
        this.type=type;
        var_dec = new LinkedHashMap<>();
        arguments = new LinkedHashMap<>();
    }

    public boolean add_var_dec(String var_type,String var_name){
        if(var_exists(var_name) || argument_exists(var_name)){
            System.err.println("variable "+var_name+" has already been defined in this scope");
            return false;
        }
        var_dec.put(var_name,var_type);
        return true;
    }

    public boolean add_argument(String var_type,String var_name){
        if(argument_exists(var_name)){
            System.err.println("variable "+var_name+" has already been defined in this scope");
            return false;
        }
        arguments.put(var_name,var_type);
        return true;
    }

    public boolean var_exists(String var_name){
        return var_dec.containsKey(var_name);
    }

    public boolean argument_exists(String var_name){
        return arguments.containsKey(var_name);
    }

    public int check_types_availability(ArrayList<String> types){
        int errors=0;
        if(!types.contains(type)){
            errors++;
            System.err.println("Unknown type "+type);
        }
        for (Map.Entry<String,String> entry : var_dec.entrySet()){
            if(!types.contains(entry.getValue())){
                errors++;
                System.err.println("Unknown type "+entry.getValue());
            }
        }

        for (Map.Entry<String,String> entry : arguments.entrySet()){
            if(!types.contains(entry.getValue())){
                errors++;
                System.err.println("Unknown type "+entry.getValue());
            }
        }
        return errors;
    }

    public boolean check_functions(function_declaration func){
        if(!type.equals(func.type)) return false;
        if (arguments.size()!=func.arguments.size()) return false;
        for(int i=0; i<arguments.size(); i++){
            if(!arguments.values().toArray()[i].equals(func.arguments.values().toArray()[i]))
                return false;
        }
        return true;
    }

    public boolean check_arguments(ArrayList<String> args,ProgramData data){
        ArrayList<String> list = new ArrayList<>(arguments.values());
        if(args.size()>list.size()){
            System.err.println("too many arguments are given");
            return false;
        }
        if(args.size()<list.size()){
            System.err.println("not enough arguments are given");
            return false;
        }
        for(int i=0; i<args.size(); i++){
            if(args.get(i).equals(list.get(i))) continue;
            if(data.class_exists(args.get(i)) && data.class_dec.get(args.get(i)).is_super_class(list.get(i))) continue;
            System.err.println("arguments do not match");
            return false;
        }
        return true;
    }

    String getType(String var){
        if(var_exists(var)) return var_dec.get(var);
        if(argument_exists(var)) return arguments.get(var);
        return null;
    }

    public boolean is_main(){
        return false;
    }
}
