package com.company;

import java.util.HashMap;
import java.util.Map;

public class Parser {
    public enum Param {
        PROGRAM,
        INPUT,
        MEM_LIMIT,
        OP_LIMIT
    }

    static final Map<String, Param> paramMap = Map.of(
        "-program", Param.PROGRAM,
        "-input", Param.INPUT,
        "-mem_limit", Param.MEM_LIMIT,
        "-op_limit", Param.OP_LIMIT
    );

    public static Map<Param, String> parse(String[] args) throws Exception {
        HashMap<Param, String> result = new HashMap<>();
        int len = args.length;
        if (len > 0 && len % 2 == 0) {
            for (int i = 0; i < len; i += 2) {
                Param param = paramMap.get(args[i]);
                if (param != null) {
                    result.put(param, args[i + 1]);
                }
            }
        } else {
            throw new Exception("Wrong params number");
        }
        return result;
    }
}
