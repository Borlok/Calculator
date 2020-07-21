package com.company;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ExpressionTransform {
    private LinkedList<String> operatorStack;
    private LinkedList<String> operandStack;
    private LinkedList<String> expressionAsElements;
    private ExpressionValidate validation;
    private final String inputExpression;
    private final String arithmeticSignsInCurrentExpression = "\\s*[ +\\-*/]\\s*";
    private StringBuilder outputExpression;

    public ExpressionTransform(String inputExpression) {
        this.inputExpression = inputExpression;
        operatorStack = new LinkedList<>();
        operandStack = new LinkedList<>();
        expressionAsElements = new LinkedList<>();
        outputExpression = new StringBuilder();

        validation = new ExpressionValidate(inputExpression);
        validation.validateExpressionWithCurrentArithmeticSigns(getArithmeticSignsInCurrentExpression());

        doPolishNotationFromInputExpression();
    }

    public void doPolishNotationFromInputExpression() {
        parseExpression();
        transformToPolishNotation();
    }

    public String getArithmeticSignsInCurrentExpression() {
        return arithmeticSignsInCurrentExpression;
    }

    public void parseExpression() {
        operandStack = Arrays.stream(inputExpression.split(getArithmeticSignsInCurrentExpression())).
                filter(x -> !x.equals(" ") && !x.equals("")).
                collect(Collectors.toCollection(LinkedList::new));
        System.out.println(operandStack);
        operatorStack = Arrays.stream(inputExpression.split("\\s*\\d\\s*")).
                filter(x -> !x.equals(".") && !x.equals("") && !x.equals(" ")).
                collect(Collectors.toCollection(LinkedList::new));
        System.out.println(operatorStack);
        joinElementsOfOperandsAndOperatorsInOneExpression();
    }

    private void joinElementsOfOperandsAndOperatorsInOneExpression() {
        if (isFirstSymbolIsNumber()) {
            createExpressionAsElementsIfFirstSymbolIsNumber();
        } else {
            createExpressionAsElementsIfFirstSymbolIsSymbol();
        }
        joinRemainsOperands();

    }

    //Менять при реализации минуса
    private boolean isFirstSymbolIsNumber() {
        if (inputExpression.charAt(0) >= '0') {
            return true;
        } else return false;
    }

    private void createExpressionAsElementsIfFirstSymbolIsNumber() {
        while (!operatorStack.isEmpty()) {
//            System.out.println(operatorStack);
//            System.out.println(operandStack);
            expressionAsElements.add(operandStack.pop());
            appendOperatorsInExpressionAsElements();
//            System.out.println(expressionAsElements);
        }
    }

    private void createExpressionAsElementsIfFirstSymbolIsSymbol() {
        while (!operatorStack.isEmpty()) {
            appendOperatorsInExpressionAsElements();
            if (!operandStack.isEmpty()) {
                expressionAsElements.add(operandStack.pop());
            }
        }
    }
//    Негативное выражение  - изменить название паттерна и матчера
    private void appendOperatorsInExpressionAsElements() {
        String operator = operatorStack.pop();
        Pattern pattern = Pattern.compile("\\.*\\(\\s*-\\.*");
        Matcher matcher = pattern.matcher(operator);

        if (operator.length() > 1) {
            if (matcher.find()) {
                expressionAsElements.addAll(Arrays.stream(operator.split("\\.*")).
                        filter(x -> !x.equals(" ") && !x.equals("")).
                        collect(Collectors.toCollection(LinkedList::new)));

                expressionAsElements.pollLast();
                expressionAsElements.add(String.valueOf(0));
                expressionAsElements.add("-");
            } else {
                expressionAsElements.addAll(Arrays.stream(operator.split("\\.*")).
                        filter(x -> !x.equals(" ") && !x.equals("")).
                        collect(Collectors.toCollection(LinkedList::new)));
            }
        } else {
            expressionAsElements.add(operator);
        }
        System.out.println(expressionAsElements);
    }

    private void joinRemainsOperands() {
        while (!operandStack.isEmpty()) {
            expressionAsElements.add(operandStack.pop());
        }
    }

    public void transformToPolishNotation() {
        Pattern pattern = Pattern.compile("[0-9]");
        for (int i = 0; i < expressionAsElements.size(); i++) {
            Matcher matcher = pattern.matcher(expressionAsElements.get(i));
            if (matcher.find()) {
                outputExpression.append(expressionAsElements.get(i)).append(" ");
            } else {
                ifMatcherFindAnOperator(expressionAsElements.get(i));
            }
        }
        while (!operatorStack.isEmpty()) {
            outputExpression.append(operatorStack.pop()).append(" ");
        }
    }

    public void ifMatcherFindAnOperator(String operator) {
        if (!operatorStack.isEmpty()) {
            String lastOperator = operatorStack.pop();
            if (getPriorityOfOperator(lastOperator) < getPriorityOfOperator(operator)) {
                operatorStack.push(lastOperator);
            } else {
                outputExpression.append(lastOperator).append(" ");
            }
        }
        operatorStack.push(operator);
    }

    public int getPriorityOfOperator(String operator) {
        if (operator.equals("+") || operator.equals("-")) {
            return 1;
        } else return 2;
    }

    public String getExpressionAsPolishNotation() {
        return String.valueOf(outputExpression);
    }

    public StringBuilder getOutputExpression() {
        return outputExpression;
    }

    public LinkedList<String> getOperatorStack() {
        return operatorStack;
    }

    public LinkedList<String> getOperandStack() {
        return operandStack;
    }

    public LinkedList<String> getExpressionAsElements() {
        return expressionAsElements;
    }
}
