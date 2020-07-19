package com.company;

import com.company.exception.WrongOperatorsException;

public class ExtendsExpressionTransform extends ExpressionTransform {
    private final String arithmeticSignsInCurrentExpression = "\\s*[+\\-*/()]\\s*";

    public ExtendsExpressionTransform(String inputExpression) {
        super(inputExpression);
    }

    @Override
    public String getArithmeticSignsInCurrentExpression() {
        return arithmeticSignsInCurrentExpression;
    }

    @Override
    public void ifMatcherFindAnOperator(String operator) {
        while (!getOperatorStack().isEmpty()) {
            String lastOperator = getOperatorStack().pop();
            if (lastOperator.equals("(")) {
                getOperatorStack().push(lastOperator);
                break;
            } else if (operator.equals(")")) {
                getOperatorStack().push(lastOperator);
                ifClosingBracket();
                return;
            } else {
                if (getPriorityOfOperator(lastOperator) < getPriorityOfOperator(operator)) {
                    getOperatorStack().push(lastOperator);
                    break;
                } else {
                    getOutputExpression().append(lastOperator).append(" ");
                }
            }
        }
        getOperatorStack().push(operator);
//        System.out.println(getOutputExpression() + " " + getOperatorStack());
    }

    private void ifClosingBracket() {
        String lastOperator = getOperatorStack().pop();
        while (true) {
            if (!lastOperator.equals("(")) {
                getOutputExpression().append(lastOperator).append(" ");
                lastOperator = getOperatorStack().pop();
            } else {
                break;
            }
        }
    }

    @Override
    public int getPriorityOfOperator(String operator) {
        if (operator.equals("+") || operator.equals("-"))
            return 1;
        else if (operator.equals("(") || operator.equals(")"))
            return 3;
        else
            return 2;
    }
}
