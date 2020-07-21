package com.company;

import com.company.exception.WrongCalculateException;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Этот класс получает строку в виде выражения записанного Польской записью, вычисляет и возвращает ответ типа Double.
 */

public class ExpressionCalculate {
    private LinkedList<String> expressionStack;
    private LinkedList<Double> solution;

    public ExpressionCalculate(String expressionAsPolishNotation) {
        solution = new LinkedList<>();
        expressionStack = new LinkedList<>();
        expressionStack.addAll(Arrays.asList(expressionAsPolishNotation.split("\\s")));
        try {
            calculate();
        } catch (WrongCalculateException e) {
            e.printStackTrace();
        }

    }

    private void calculate() throws WrongCalculateException {
        for (String s : expressionStack) {
            if (ifOperand(s)) {
                solution.push(Double.valueOf(s));
            } else {
                operationWithOperator(s);
            }
        }
    }

    private boolean ifOperand(String element) {
        return element.matches("\\d+") || element.matches("\\d*\\.\\d*");
    }

    public void operationWithOperator(String operator) throws WrongCalculateException {
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
            if (secondNumber == 0) {
                throw new WrongCalculateException("Деление на ноль невозможно");
            }
            solution.push(firstNumber / secondNumber);
        }
    }

    public Double getSolution() {
        return solution.pop();
    }

}
