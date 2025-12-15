package ru.sinvic.simplealgorithms;


import java.util.*;

public class BalancedBrackets {
    public static void main(String[] args) {
        System.out.println("1. Базовые позитивные кейсы (должно быть true):");
        System.out.println("Пустая строка: " + isBalancedBrackets(""));
        System.out.println("() : " + isBalancedBrackets("()"));
        System.out.println("[] : " + isBalancedBrackets("[]"));
        System.out.println("{} : " + isBalancedBrackets("{}"));
        System.out.println("()[]{} : " + isBalancedBrackets("()[]{}"));
        System.out.println("([{}]) : " + isBalancedBrackets("([{}])"));
        System.out.println("{[()]} : " + isBalancedBrackets("{[()]}"));
        System.out.println("((())) : " + isBalancedBrackets("((()))"));

        System.out.println("\n2. Базовые негативные кейсы (должно быть false):");
        System.out.println("( : " + isBalancedBrackets("("));
        System.out.println(") : " + isBalancedBrackets(")"));
        System.out.println("(] : " + isBalancedBrackets("(]"));
        System.out.println("([)] : " + isBalancedBrackets("([)]"));
        System.out.println("((( : " + isBalancedBrackets("((("));
        System.out.println("))) : " + isBalancedBrackets(")))"));
        System.out.println("([)] : " + isBalancedBrackets("([)]"));

        System.out.println("\n3. Кейсы с посторонними символами (должно быть false):");
        System.out.println("a(b)c : " + isBalancedBrackets("a(b)c"));
        System.out.println("1[2{3}4]5 : " + isBalancedBrackets("1[2{3}4]5"));
        System.out.println("(hello) : " + isBalancedBrackets("(hello)"));
        System.out.println("text : " + isBalancedBrackets("text"));

        System.out.println("\n4. Крайние случаи:");
        System.out.println("null : " + isBalancedBrackets(null));
        System.out.println("пробел : " + isBalancedBrackets(" "));
        System.out.println("\\t\\n : " + isBalancedBrackets("\t\n"));

        System.out.println("\n5. Сложные комбинации:");
        System.out.println("({[]}) : " + isBalancedBrackets("({[]})"));
        System.out.println("([)]{} : " + isBalancedBrackets("([)]{}"));
        System.out.println("(((((())))))) : " + isBalancedBrackets("((((((()))))))"));
        System.out.println("(()))(() : " + isBalancedBrackets("(()))(()"));
    }

    public static boolean isBalancedBrackets(String str) {
        if (str == null || str.length() <= 1) {
            return false;
        }

        ArrayDeque<Character> stack = new ArrayDeque<>();


        for (char ch : str.toCharArray()) {
            switch (ch) {
                case '(':
                case '{':
                case '[':
                    stack.push(ch);
                    break;
                case ')':
                    if (stack.isEmpty() || stack.pop() != '(') return false;
                    break;
                case '}':
                    if (stack.isEmpty() || stack.pop() != '{') return false;
                    break;
                case ']':
                    if (stack.isEmpty() || stack.pop() != '[') return false;
                    break;
                default:
                    return false;
            }
        }
        return stack.isEmpty();
    }
}
