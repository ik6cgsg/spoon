package com.company;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class SpoonInterpreter {
    private final int FIELD_NUM = 30000;
    private final int MAX_LEN = 7;

    private char[] memory;
    private int currentField;
    private ArrayList<Operation> program;
    private StringReader input;
    private String output;
    private int memLimit;
    private int opLimit;

    private enum Operation {
        INCREMENT,
        DECREMENT,
        NEXT,
        PREVIOUS,
        BEGIN_LOOP,
        END_LOOP,
        INPUT,
        OUTPUT
    }

    private static final Map<String, Operation> operations = Map.of(
        "1", Operation.INCREMENT,
        "000", Operation.DECREMENT,
        "010", Operation.NEXT,
        "011", Operation.PREVIOUS,
        "00100", Operation.BEGIN_LOOP,
        "0011", Operation.END_LOOP,
        "0010110", Operation.INPUT,
        "001010", Operation.OUTPUT
    );

    SpoonInterpreter() {
        this.memLimit = FIELD_NUM;
        this.opLimit = Integer.MAX_VALUE;
    }

    SpoonInterpreter(int memLimit, int opLimit) {
        this.memLimit = memLimit;
        this.opLimit = opLimit;
    }

    private boolean parse(String program) {
        boolean res = true;
        for (int i = 0; i < program.length(); i++) {
            Operation op = null;
            for (int l = 1; l <= MAX_LEN; l++) {
                op = operations.get(program.substring(i, i + l));
                if (op != null) {
                    i += l - 1;
                    break;
                }
            }
            if (op != null) {
                this.program.add(op);
            } else {
                res = false;
                break;
            }
        }
        return res;
    }

    public boolean loadProgram(String program, String input) {
        boolean res = false;
        memory = new char[memLimit];
        Arrays.fill(memory, (char)0);
        currentField = 0;
        this.input = new StringReader(input);
        this.output = "";
        this.program = new ArrayList<>();
        if (parse(program)) {
            if (this.program.size() <= opLimit) {
                res = true;
            } else {
                System.out.println("Too much operations used!");
            }
        }
        return res;
    }

    public boolean runProgram() {
        boolean res = true;
        for (int i = 0; i < program.size(); i++) {
            if (!res) {
                break;
            }
            Operation op = program.get(i);
            switch (op) {
                case INCREMENT:
                    memory[currentField]++;
                    break;
                case DECREMENT:
                    memory[currentField]--;
                    break;
                case NEXT:
                    currentField++;
                    if (currentField == memLimit) {
                        System.out.println("Using too much memory!");
                        res = false;
                        continue;
                    }
                    break;
                case PREVIOUS:
                    currentField--;
                    if (currentField < 0) {
                        System.out.println("Memory index less then zero!");
                        res = false;
                        continue;
                    }
                    break;
                case BEGIN_LOOP:
                    if (memory[currentField] != 0) {
                        continue;
                    } else {
                        int openBrackets = 1;
                        while (openBrackets > 0) {
                            i++;
                            Operation opi = program.get(i);
                            if (opi == Operation.BEGIN_LOOP) {
                                openBrackets++;
                            } else if (opi == Operation.END_LOOP) {
                                openBrackets--;
                            }
                        }
                    }
                    break;
                case END_LOOP:
                    if (memory[currentField] == 0) {
                        continue;
                    } else {
                        int closeBrackets = 1;
                        while (closeBrackets > 0) {
                            i--;
                            Operation opi = program.get(i);
                            if (opi == Operation.BEGIN_LOOP) {
                                closeBrackets--;
                            } else if (opi == Operation.END_LOOP) {
                                closeBrackets++;
                            }
                        }
                        i--;
                    }
                    break;
                case INPUT:
                    try {
                        memory[currentField] = (char)input.read();
                    } catch (Exception e) {
                        System.out.println(e.toString());
                        res = false;
                        continue;
                    }
                    break;
                case OUTPUT:
                    String value = String.valueOf(memory[currentField]);
                    output = output.concat(value);
                    //System.out.println(value);
                    break;
            }
        }
        return res;
    }

    public String getResult() {
        return output;
    }
}
