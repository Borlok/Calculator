package com.company;

/**
 * Это программа калькулятор. В данный момент в ней реализованны основные функции вычисления,
 * скобки и отрицательные числа.
 * @version 1.1
 * @author Ерофеевский Юрий Александрович
 */
public class Main {

    public static void main(String[] args) {
        start();
    }

    private static void start() {
        String expression = Expression.getExpression();
        ExpressionCalculate calculate = new ExpressionCalculate(
                new ExtendsExpressionTransform(expression).getExpressionAsPolishNotation());
        Expression.showResult(calculate);
    }
}
