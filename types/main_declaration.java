package types;

import java.util.ArrayList;
import java.util.Map;

public class main_declaration extends function_declaration{

    public main_declaration(String arg){
        super("void");
        arguments.put(arg,"String[]");
    }

    public boolean check_functions(function_declaration func){
        return false;
    }

    public int check_types_availability(ArrayList<String> types){
        int errors=0;
        for (Map.Entry<String,String> entry : var_dec.entrySet()){
            if(!types.contains(entry.getValue())){
                errors++;
                System.err.println("Unknown type "+entry.getValue());
            }
        }
        return errors;
    }

    public boolean is_main(){
        return true;
    }
}
