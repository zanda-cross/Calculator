package Calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Stack;
import javax.swing.border.LineBorder;


public class Calculator implements ActionListener {

    JFrame frame;
    JTextField textfield;
    JButton[] numberButtons = new JButton[10];
    JButton addButton, subButton, mulButton, divButton;
    JButton decimalButton, equalsButton, deleteButton, clearButton;
    JPanel panel;

    StringBuilder input = new StringBuilder();
    Font myFont = new Font("Sans Serif", Font.BOLD, 28);

    // Dark theme colors
    Color darkBg = new Color(30, 30, 30);
    Color buttonBg = new Color(51, 51, 51);
    Color equalBg = new Color(33, 150, 243); // Blue
    Color textColor = equalBg;

    Calculator() {
        frame = new JFrame("Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 550);
        frame.setLayout(null);
        frame.getContentPane().setBackground(darkBg);

        textfield = new JTextField();
        textfield.setBounds(50, 25, 300, 50);
        textfield.setFont(myFont);
        textfield.setEditable(false);
        textfield.setBackground(darkBg);
        textfield.setForeground(Color.WHITE);
        textfield.setCaretColor(Color.WHITE);
        textfield.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Buttons
        addButton = new JButton("+");
        subButton = new JButton("-");
        mulButton = new JButton("*");
        divButton = new JButton("/");
        decimalButton = new JButton(".");
        equalsButton = new JButton("=");
        deleteButton = new JButton("Del");
        clearButton = new JButton("Clr");

        JButton[] functionButtons = {
            addButton, subButton, mulButton, divButton,
            decimalButton, equalsButton, deleteButton, clearButton
        };

        for (JButton btn : functionButtons) {
            styleButton(btn, buttonBg, textColor);
        }

        equalsButton.setBackground(equalBg);
        equalsButton.setForeground(Color.WHITE);

        for (int i = 0; i < 10; i++) {
            numberButtons[i] = new JButton(String.valueOf(i));
            styleButton(numberButtons[i], buttonBg, textColor);
        }

        deleteButton.setBounds(50, 430, 145, 50);
        clearButton.setBounds(200, 430, 145, 50);

        panel = new JPanel();
        panel.setBounds(50, 100, 300, 300);
        panel.setLayout(new GridLayout(4, 4, 10, 10));
        panel.setBackground(darkBg);

        // Add buttons in calculator layout
        panel.add(numberButtons[1]);
        panel.add(numberButtons[2]);
        panel.add(numberButtons[3]);
        panel.add(addButton);

        panel.add(numberButtons[4]);
        panel.add(numberButtons[5]);
        panel.add(numberButtons[6]);
        panel.add(subButton);

        panel.add(numberButtons[7]);
        panel.add(numberButtons[8]);
        panel.add(numberButtons[9]);
        panel.add(mulButton);

        panel.add(decimalButton);
        panel.add(numberButtons[0]);
        panel.add(equalsButton);
        panel.add(divButton);

        // Add components to frame
        frame.add(panel);
        frame.add(deleteButton);
        frame.add(clearButton);
        frame.add(textfield);
        frame.setVisible(true);
    }

    private void styleButton(JButton btn, Color bg, Color fg) {
        btn.setFont(myFont);
        btn.setFocusable(false);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.addActionListener(this); // important to work!
    }

    public static void main(String[] args) {
        new Calculator();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < 10; i++) {
            if (e.getSource() == numberButtons[i]) {
                input.append(i);
                textfield.setText(input.toString());
                return;
            }
        }

        if (e.getSource() == decimalButton) input.append(".");
        else if (e.getSource() == addButton) input.append("+");
        else if (e.getSource() == subButton) input.append("-");
        else if (e.getSource() == mulButton) input.append("*");
        else if (e.getSource() == divButton) input.append("/");
        else if (e.getSource() == clearButton) input.setLength(0);
        else if (e.getSource() == deleteButton) {
            int length = input.length();
            if (length > 0) input.deleteCharAt(length - 1);
        }
        else if (e.getSource() == equalsButton) {
            try {
                double result = evaluate(input.toString());
                textfield.setText(String.valueOf(result));
                input.setLength(0);
                input.append(result);
            } catch (Exception ex) {
                textfield.setText("Error");
                input.setLength(0);
            }
            return;
        }

        textfield.setText(input.toString());
    }

    // Expression Evaluator
    private double evaluate(String expr) {
        Stack<Double> nums = new Stack<>();
        Stack<Character> ops = new Stack<>();
        int i = 0;
        while (i < expr.length()) {
            char ch = expr.charAt(i);
            if (Character.isWhitespace(ch)) {
                i++;
                continue;
            }
            if (Character.isDigit(ch) || ch == '.' || (ch == '-' && (i == 0 || isOperator(expr.charAt(i - 1))))) {
                StringBuilder sb = new StringBuilder();
                sb.append(ch);
                i++;
                while (i < expr.length() && (Character.isDigit(expr.charAt(i)) || expr.charAt(i) == '.')) {
                    sb.append(expr.charAt(i++));
                }
                nums.push(Double.parseDouble(sb.toString()));
                continue;
            } else if (isOperator(ch)) {
                while (!ops.isEmpty() && precedence(ops.peek()) >= precedence(ch)) {
                    double b = nums.pop();
                    double a = nums.pop();
                    char op = ops.pop();
                    nums.push(applyOp(a, b, op));
                }
                ops.push(ch);
            }
            i++;
        }

        while (!ops.isEmpty()) {
            double b = nums.pop();
            double a = nums.pop();
            char op = ops.pop();
            nums.push(applyOp(a, b, op));
        }
        return nums.pop();
    }

    private boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/';
    }

    private int precedence(char op) {
        return (op == '+' || op == '-') ? 1 : 2;
    }

    private double applyOp(double a, double b, char op) {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/': return (b == 0) ? 0 : a / b;
        }
        return 0;
    }
}
