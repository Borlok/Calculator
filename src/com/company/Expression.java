package com.company;

import java.util.Scanner;

/**
 * Этот класс служит для ввода пользовательского выражения и вывода ответа на консоль.
 */
public class Expression {

    public static String getExpression() {
        System.out.println("Пожалуйста, введите выражение: ");
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    public static void showResult(ExpressionCalculate expressionCalculate) {
        System.out.println("Ответ: " + expressionCalculate.getSolution());
    }
}
