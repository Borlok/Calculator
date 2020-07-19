package com.company;


import com.company.exception.WrongOperatorsException;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws WrongOperatorsException, IOException {
	// write your code here
        //Первый класс - считывающий с консоли Expression expression = new Expression();
        //Второй - разбор на составные части и перевод в польскую запись TransformTheExpression expressionTransform = new TransformTheExpression(expression);
        //Третий - расчет CalculateTheExpression calculate = new CalculateTheExpression(expressionTransform);
        //Задачи:
        //1. Реализовать минус
        start();

    }

    private static void start() throws IOException, WrongOperatorsException {

//        Expression entriesExpression = new Expression();
//        String expression = entriesExpression.getExpression();
        String expression = "1.2 * ( (2+3 ) * (5/ 5)) *6";
        //String expression = "4.2+2*3/3- 6.1";
        //ValidateTheExpression validateTheExpression = new ValidateTheExpression(expression);
        //validateTheExpression.getValidExpression();
//        ExpressionTransform expressionTransform = new ExpressionTransform(expression);
        ExtendsExpressionTransform expressionTransform = new ExtendsExpressionTransform(expression);
        System.out.println(expressionTransform.getExpressionAsPolishNotation());
    }
}
