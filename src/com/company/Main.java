package com.company;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        try {
            Map<Parser.Param, String> params = Parser.parse(args);
            String program = params.getOrDefault(Parser.Param.PROGRAM, "");
            String input = params.getOrDefault(Parser.Param.INPUT, "");
            String memLimit = params.getOrDefault(Parser.Param.MEM_LIMIT, "");
            String opLimit = params.getOrDefault(Parser.Param.OP_LIMIT, "");
            SpoonInterpreter si = new SpoonInterpreter(Integer.parseInt(memLimit), Integer.parseInt(opLimit));
            if (si.loadProgram(program, input)) {
                si.runProgram();
                System.out.println(si.getResult());
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
