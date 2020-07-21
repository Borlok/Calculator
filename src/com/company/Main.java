package com.company;


import com.company.exception.WrongOperatorsException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws WrongOperatorsException, IOException {
        //1. Реализовать минус
        //2. Деление на ноль
        start();

    }

    private static void start() throws IOException, WrongOperatorsException {
//        String expression = Expression.getExpression();
//        String expression = "(-2)+((-4)*3.5)";
        String expression = "(-2)*  3  + 1-3";
        ExtendsExpressionTransform expressionAsPolishNotation = new ExtendsExpressionTransform(expression);
        String polishNotation = expressionAsPolishNotation.getExpressionAsPolishNotation();
        System.out.println(polishNotation);
        ExpressionCalculate calculate = new ExpressionCalculate(polishNotation);
        Expression.showResult(calculate.getSolution());
    }
}
