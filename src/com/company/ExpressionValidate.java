package com.company;

import com.company.exception.WrongOperatorsException;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Этот класс отлавливает ошибки ввода. Реализован отлов не всех ошибок.
 */
public class ExpressionValidate {
    private final String inputExpression;
    private String arithmeticSignsInCurrentExpression = "\\s*[ +\\-*/]\\s*";
    private String[] inputOperands;
    private String[] inputOperators;

    public ExpressionValidate(String expression) {
        inputExpression = expression;
    }

    public void validateExpressionWithCurrentArithmeticSigns(String arithmeticSignsInCurrentExpression) {
        this.arithmeticSignsInCurrentExpression = arithmeticSignsInCurrentExpression;
        parseExpressionToElements();

        try {
            validateOperands();
            validateOperators();
            validateBrackets();
        } catch (WrongOperatorsException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private void parseExpressionToElements() {
        inputOperands = Arrays.stream(inputExpression.split(getArithmeticSignsInCurrentExpression())).
                filter(x -> !x.equals(" ") && !x.equals("")).toArray(String[]::new);

        inputOperators = Arrays.stream(inputExpression.split("\\s*\\d\\s*")).
                filter(x -> !x.equals(".") && !x.equals("") && !x.equals(" ")).toArray(String[]::new);
    }

    public void validateOperands() throws WrongOperatorsException {
        Pattern pa = Pattern.compile("[^ 0-9.]");

        for (String inputOperand : inputOperands) {
            Matcher matcher = pa.matcher(inputOperand);

            if (inputOperand.matches("\\D")) {
                throw new WrongOperatorsException("Символ " + "[" + inputOperand + "]" + " недопустим в этом выражении");
            }
            else if (inputOperand.charAt(0) == '.') {
                throw new WrongOperatorsException("Символ " + "[" + inputOperand.charAt(0) + "]" +
                        " перед операндом недопустим в этом выражении");
            }
            else if (matcher.find()) {
                throw new WrongOperatorsException("Символ " + "[" + inputOperand + "]" + " недопустим в этом выражении");
            }
        }
    }

    public void validateOperators() throws WrongOperatorsException {
        Pattern availableSigns = Pattern.compile(getExceptAvailableArithmeticSignsInCurrentExpression());
        Pattern negativeNumber = Pattern.compile("\\.*\\(\\s*-");

        for (String inputOperator : inputOperators) {
            Matcher matchAvailableSigns = availableSigns.matcher(inputOperator);
            Matcher matchNegativeNumber = negativeNumber.matcher(inputOperator);

            String operator = deleteAllBracketsAndEmptySpaceFromInputOperatorAndReturnRemains(inputOperator);

            if (inputOperator.equals("(") || inputOperator.equals(")")) {
                continue;
            }
            if (matchNegativeNumber.find()) {
                continue;
            }
            if (operator.length() > 1) {
                throw new WrongOperatorsException("Введено более одного оператора вычисления: " + "[" + operator + "]");
            }
            if (inputOperator.length() > 1 && !(inputOperator.contains("(") || inputOperator.contains(")"))) {
                throw new WrongOperatorsException("Введено более одного оператора вычисления: " + "[" + inputOperator + "]");
            }
            if (!operator.matches(getArithmeticSignsInCurrentExpression())) {
                throw new WrongOperatorsException("Введен неверный оператор вычисления: " + "[" + operator + "]");
            }
            if (matchAvailableSigns.find()) {
                throw new WrongOperatorsException("Здесь " + "[" + inputOperator + "]" +
                        " есть недопустимый символ");
            }
        }
    }

    private String getExceptAvailableArithmeticSignsInCurrentExpression() {
        String x = arithmeticSignsInCurrentExpression.substring(
                arithmeticSignsInCurrentExpression.indexOf("["),
                arithmeticSignsInCurrentExpression.indexOf("]") + 1);

        return x.replace("[", "[^");
    }

    private String deleteAllBracketsAndEmptySpaceFromInputOperatorAndReturnRemains(String inputOperator) {
        return Arrays.stream(inputOperator.split("\\.*")).
                filter(x -> !x.equals("(") && !x.equals(")") && !x.equals("") && !x.equals(" ")).
                collect(Collectors.joining(""));
    }

    private void validateBrackets() throws WrongOperatorsException {
        String[] openBrackets = Arrays.stream(inputExpression.split("\\.*")).
                filter(x -> x.equals("(")).toArray(String[]::new);

        String[] closeBrackets = Arrays.stream(inputExpression.split("\\.*")).
                filter(x -> x.equals(")")).toArray(String[]::new);

        if (openBrackets.length != closeBrackets.length) {
            throw new WrongOperatorsException("Неверное соотношение открывающихся и закрывающихся скобок");
        }
    }

    public String getArithmeticSignsInCurrentExpression() {
        return arithmeticSignsInCurrentExpression;
    }
}
