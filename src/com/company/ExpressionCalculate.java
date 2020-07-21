package com.company;

import java.util.Arrays;
import java.util.LinkedList;

public class ExpressionCalculate {
    private LinkedList<String> expressionStack;
    private LinkedList<Double> solution;

    public ExpressionCalculate(String expressionAsPolishNotation) {
        solution = new LinkedList<>();
        expressionStack = new LinkedList<>();
        expressionStack.addAll(Arrays.asList(expressionAsPolishNotation.split("\\s")));
        calculate();

    }

    private void calculate() {
        for (int i = 0; i < expressionStack.size(); i++) {
//            System.out.println(expressionStack.get(i) + " isOperand?");
            if (ifOperand(expressionStack.get(i))) {
//                System.out.println(expressionStack.get(i) + " isOperand, put the solution");
                solution.push(Double.valueOf(expressionStack.get(i)));
//                System.out.println(solution);
            }else {
//                System.out.println("No " + expressionStack.get(i) + " isOperator, lets do it!!!");
                operationWithOperator(expressionStack.get(i));
//                System.out.println(solution);
            }
        }
    }
    //Реализовать просчет дабла
    private boolean ifOperand(String element) {
        return element.matches("\\d+") || element.matches("\\d*\\.\\d*");
    }

    public void operationWithOperator(String operator) {
        Double secondNumber = solution.pop();
        Double firstNumber = solution.pop();

        if (operator.equals("+")) {
            solution.push(firstNumber + secondNumber);
        }
        if (operator.equals("-")) {
            solution.push(firstNumber - secondNumber);
        }
        if (operator.equals("*")) {
            solution.push(firstNumber * secondNumber);
        }
        if (operator.equals("/")) {
            solution.push(firstNumber / secondNumber);
        }
    }

    public Double getSolution () {
        return solution.pop();
    }

}
