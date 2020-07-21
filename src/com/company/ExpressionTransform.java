package com.company;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Этот класс служит для преобразования, введенного пользователем выражения в виде строки, в Польскую запись.
 * Можно разделить на два класса:
 *      1. Разбиение на элементы входящей строки типа String и возврат списка с элементами типа LinkedList.
 *      2. Преобразование списка с элементами типа LinkedList в строку в виде Польской записи.
 */
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

    private void doPolishNotationFromInputExpression() {
        parseExpression();
        transformToPolishNotation();
    }

    public String getArithmeticSignsInCurrentExpression() {
        return arithmeticSignsInCurrentExpression;
    }

    private void parseExpression() {
        operandStack = Arrays.stream(inputExpression.split(getArithmeticSignsInCurrentExpression())).
                filter(x -> !x.equals(" ") && !x.equals("")).
                collect(Collectors.toCollection(LinkedList::new));

        operatorStack = Arrays.stream(inputExpression.split("\\s*\\d\\s*")).
                filter(x -> !x.equals(".") && !x.equals("") && !x.equals(" ")).
                collect(Collectors.toCollection(LinkedList::new));

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

    private boolean isFirstSymbolIsNumber() {
        return inputExpression.charAt(0) >= '0';
    }

    private void createExpressionAsElementsIfFirstSymbolIsNumber() {
        while (!operatorStack.isEmpty()) {
            expressionAsElements.add(operandStack.pop());
            appendOperatorsInExpressionAsElements();
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

    public void appendOperatorsInExpressionAsElements() {
        String operator = operatorStack.pop();
        Pattern negativeNumberPattern = Pattern.compile("\\.*\\(\\s*-\\.*");
        Pattern negativeNumberPatternWithoutBrackets = Pattern.compile("^-\\.*");
        Matcher matchNegativeNumber = negativeNumberPattern.matcher(operator);
        Matcher matchNegativeNumberWithoutBrackets = negativeNumberPatternWithoutBrackets.matcher(operator);

        if (operator.length() > 1) {
            if (matchNegativeNumber.find()) {
                insertNullBeforeMinus(operator);
            } else {
                parseOperatorsOnElements(operator);
            }
        } else {
            if (matchNegativeNumberWithoutBrackets.find()) {
                insertNullBeforeMinus(operator);
            }else
            expressionAsElements.add(operator);
        }
    }

    private void insertNullBeforeMinus(String operator) {
        expressionAsElements.addAll(Arrays.stream(operator.split("\\.*")).
                filter(x -> !x.equals(" ") && !x.equals("")).
                collect(Collectors.toCollection(LinkedList::new)));

        expressionAsElements.pollLast();
        expressionAsElements.add(String.valueOf(0));
        expressionAsElements.add("-");
    }

    private void parseOperatorsOnElements(String operator) {
        expressionAsElements.addAll(Arrays.stream(operator.split("\\.*")).
                filter(x -> !x.equals(" ") && !x.equals("")).
                collect(Collectors.toCollection(LinkedList::new)));
    }

    private void joinRemainsOperands() {
        while (!operandStack.isEmpty()) {
            expressionAsElements.add(operandStack.pop());
        }
    }

    private void transformToPolishNotation() {
        Pattern allNumberPattern = Pattern.compile("[0-9]");
        for (String expressionAsElement : expressionAsElements) {
            Matcher matchAllNumber = allNumberPattern.matcher(expressionAsElement);
            if (matchAllNumber.find()) {
                outputExpression.append(expressionAsElement).append(" ");
            } else {
                transformToPolishNotationIfOperatorIs(expressionAsElement);
            }
        }
        while (!operatorStack.isEmpty()) {
            outputExpression.append(operatorStack.pop()).append(" ");
        }
    }

    public void transformToPolishNotationIfOperatorIs(String operator) {
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
