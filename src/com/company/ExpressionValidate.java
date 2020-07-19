package com.company;

import com.company.exception.WrongOperatorsException;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ExpressionValidate {
    private String inputExpression;
    private String arithmeticSignsInCurrentExpression = "\\s*[+\\-*/()]\\s*";
    String[] inputOperands;
    String[] inputOperators;

    public ExpressionValidate(String expression) {
        inputExpression = expression;
    }

    //Проверка на двойные операторы не включая скобки
    //Проверить, если после открывающейся скобки стоит не минус
    //Совпадение количества открывающихся и закрывающихся скобок

    public void validateExpressionWithCurrentArithmeticSigns(String arithmeticSignsInCurrentExpression) {
        this.arithmeticSignsInCurrentExpression = arithmeticSignsInCurrentExpression;
        parseExpressionToElements();
        try {
            validateOperands();
            validateOperators();
            validateBrackets();
        } catch (WrongOperatorsException e) {
            e.printStackTrace();
        }
    }

    private void parseExpressionToElements() {
        inputOperands = Arrays.stream(inputExpression.split(getArithmeticSignsInCurrentExpression())).
                filter(x -> !x.equals(" ") && !x.equals("")).toArray(String[]::new);

        inputOperators = Arrays.stream(inputExpression.split("\\s*\\d\\s*")).
                filter(x -> !x.equals(".") && !x.equals("") && !x.equals(" "))
                .toArray(String[]::new);

        System.out.println(Arrays.toString(inputOperands));
        System.out.println(Arrays.toString(inputOperators));
    }

    private void validateOperands() throws WrongOperatorsException {
        Pattern pa = Pattern.compile("[^ 0-9.]");
        for (String inputOperand : inputOperands) {
            //Matcher matcher = pa.matcher(inputOperand);
            System.out.println(inputOperand);
            if (inputOperand.matches("\\D")) {
                throw new WrongOperatorsException("Символ " + "[" + inputOperand + "]" + " недопустим в этом выражении");
            } else if (inputOperand.charAt(0) == '.') {
                throw new WrongOperatorsException("Символ " + "[" + inputOperand.charAt(0) + "]" +
                        " перед операндом недопустим в этом выражении");
            }
//            else if (matcher.find()) {
//                throw new WrongOperatorsException("Символ " + "[" + inputOperand + "]" + " недопустим в этом выражении");
//            }
        }
    }
    // Здесь для каждого расширенного или нет способа нужна своя проверка допустимых операторов.
    private void validateOperators() throws WrongOperatorsException {
        Pattern pa = Pattern.compile("[^" + getArithmeticSignsInCurrentExpression() + "]");
        System.out.println(getArithmeticSignsInCurrentExpression());
        for (String inputOperator : inputOperators) {
            Matcher matcher = pa.matcher(inputOperator);
            String operator = deleteAllBracketsAndEmptySpaceFromInputOperatorAndReturnRemains(inputOperator);
            if (operator.length() > 1) {
                throw new WrongOperatorsException("Введено более одного оператора вычисления: " + "[" + operator + "]");
            } else if (inputOperator.length() > 1 && !(inputOperator.contains("(") || inputOperator.contains(")"))) {
                throw new WrongOperatorsException("Введено более одного оператора вычисления: " + "[" + inputOperator + "]");
            } else if (!operator.matches(getArithmeticSignsInCurrentExpression())) {
                throw new WrongOperatorsException("Введен неверный оператор вычисления: " + "[" + operator + "]");
            } else if (matcher.find()) {
                throw new WrongOperatorsException("Здесь " + "[" + inputOperator + "]" +
                        " есть недопустимый символ");
            }
        }
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
