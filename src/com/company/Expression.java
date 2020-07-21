package com.company;

import java.util.Scanner;

public class Expression {

    public static String getExpression() {
        System.out.println("Пожалуйста, введите выражение: ");
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    public static void showResult(Double result) {
        System.out.println("Ответ: " + result);
    }
}
