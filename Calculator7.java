package Stack;

import java.util.Stack;
import java.util.Scanner;
import java.lang.Math;

public class Calculator7 {

    public static void main(String[] args) {
        Scanner inputScanner = new Scanner(System.in);

        System.out.print("Please separate each term with a space:\n");

        while (true) {
            System.out.print("Enter an expression (type 'exit' to quit): ");
            String inputExpression = inputScanner.nextLine();

            if (inputExpression.equalsIgnoreCase("exit")) {
                break;
            }

            try {
                String result = evaluateExpression(inputExpression);
                System.out.println("Result: " + result);
            } catch (ArithmeticException e) {
                System.out.println("Error: Division by zero");
            } catch (Exception e) {
                System.out.println("Error: Invalid expression");
            }
        }

        inputScanner.close();
    }

    public static String evaluateExpression(String inputExpression) {
        Stack<Double> numberStack = new Stack<>();
        Stack<Character> operatorStack = new Stack<>();

        String[] expressionComponents = inputExpression.split(" ");

        for (String component : expressionComponents) {
            if (isNumeric(component)) {
                numberStack.push(Double.parseDouble(component));
            } else if (component.equals("(")) {
                operatorStack.push('(');
            } else if (component.equals(")")) {
                while (!operatorStack.isEmpty() && operatorStack.peek() != '(') {
                    applyOperator(numberStack, operatorStack.pop());
                }
                operatorStack.pop();
            } else if (component.startsWith("!")) {
                double operand = numberStack.pop();
                double result = calculateFactorial((int) operand);
                numberStack.push(result);
            } else if (component.equals("^")) {
                operatorStack.push('^');
            } else if (component.equals("e")) {
                numberStack.push(Math.E);
            } else if (component.equals("PI")) {
                // The constant 'π'
                numberStack.push(Math.PI);
            } else if (isTrigonometricFunction(component)) {
                operatorStack.push(component.charAt(0));
            } else if (isArcTrigonometricFunction(component)) {
                operatorStack.push(component.charAt(2));
            } else if (isCotangentFunction(component)) {
                operatorStack.push('o');
            } else if (isArcCotangentFunction(component)) {
                operatorStack.push('O');
            } else if (component.equals("log")) {
                operatorStack.push('g');
            } else if (component.equals("ln")) {
                operatorStack.push('l');
            } else if (component.equals("sgn")) {
                operatorStack.push('n');
            } else {
                while (!operatorStack.isEmpty() && getPrecedence(component.charAt(0)) <= getPrecedence(operatorStack.peek())) {
                    applyOperator(numberStack, operatorStack.pop());
                }
                operatorStack.push(component.charAt(0));
            }
        }

        while (!operatorStack.isEmpty()) {
            applyOperator(numberStack, operatorStack.pop());
        }

        return String.valueOf(numberStack.pop());
    }

    private static void applyOperator(Stack<Double> numberStack, char operator) {
        if (operator == '+') {
            double operand2 = numberStack.pop();
            double operand1 = numberStack.pop();
            double result = operand1 + operand2;
            numberStack.push(result);
        } else if (operator == '-') {
            double operand2 = numberStack.pop();
            double operand1 = numberStack.pop();
            double result = operand1 - operand2;
            numberStack.push(result);
        } else if (operator == '*') {
            double operand2 = numberStack.pop();
            double operand1 = numberStack.pop();
            double result = operand1 * operand2;
            numberStack.push(result);
        } else if (operator == '/') {
            double operand2 = numberStack.pop();
            if (operand2 == 0) {
                throw new ArithmeticException("Division by zero");
            }
            double operand1 = numberStack.pop();
            double result = operand1 / operand2;
            numberStack.push(result);
        } else if (operator == '%') {
            double operand2 = numberStack.pop();
            double operand1 = numberStack.pop();
            double result = operand1 % operand2;
            numberStack.push(result);
        } else if (operator == '^') {
            double operand2 = numberStack.pop();
            double operand1 = numberStack.pop();
            double result = Math.pow(operand1, operand2);
            numberStack.push(result);
        } else if (operator == 's') {
            double operand = numberStack.pop();
            double result = Math.sin(Math.toRadians(operand));
            numberStack.push(result);
        } else if (operator == 'c') {
            double operand = numberStack.pop();
            double result = Math.cos(Math.toRadians(operand));
            numberStack.push(result);
        } else if (operator == 't') {
            double operand = numberStack.pop();
            double result = Math.tan(Math.toRadians(operand));
            numberStack.push(result);
        } else if (operator == 'o') {
            double operand = numberStack.pop();
            double result = 1 / Math.tan(Math.toRadians(operand));
            numberStack.push(result);
        } else if (operator == 'a') {
            double operand = numberStack.pop();
            double result = Math.toDegrees(Math.asin(operand));
            numberStack.push(result);
        } else if (operator == 'A') {
            double operand = numberStack.pop();
            double result = Math.toDegrees(Math.acos(operand));
            numberStack.push(result);
        } else if (operator == 'T') {
            double operand = numberStack.pop();
            double result = Math.toDegrees(Math.atan(operand));
            numberStack.push(result);
        } else if (operator == 'O') {
            double operand = numberStack.pop();
            double result = 90 - Math.toDegrees(Math.atan(operand));
            numberStack.push(result);
        } else if (operator == 'g') {
            double operand = numberStack.pop();
            double result = Math.log10(operand);
            numberStack.push(result);
        } else if (operator == 'l') {
            double operand = numberStack.pop();
            double result = Math.log(operand);
            numberStack.push(result);
        } else if (operator == 'n') {
            double operand = numberStack.pop();
            double result = Math.signum(operand);
            numberStack.push(result);
        }
    }

    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static int getPrecedence(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
            case '%':
                return 2;
            case '^':
                return 3;
            case 's':
            case 'c':
            case 't':
            case 'o':
            case 'g':
            case 'l':
            case 'n':
            case 'a':
            case 'A':
            case 'T':
            case 'O':
                return 4; 
            default:
                return 0;
        }
    }

    private static double calculateFactorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Factorial cannot be calculated for negative numbers");
        }

        double result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    private static boolean isTrigonometricFunction(String str) {
        return str.equals("sin") || str.equals("cos") || str.equals("tan");
    }

    private static boolean isArcTrigonometricFunction(String str) {
        return str.equals("asin") || str.equals("acos") || str.equals("atan");
    }

    private static boolean isCotangentFunction(String str) {
        return str.equals("cot");
    }

    private static boolean isArcCotangentFunction(String str) {
        return str.equals("acot");
    }
}

