import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class calculadora {

    private JFrame frame;
    private JTextField display;
    private String currentInput = "";
    private String previousInput = "";
    private String operator = "";
    private boolean justCalculated = false;

    public static void main(String[] args) {
        // Ejecuta la interfaz gráfica de la calculadora
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new calculadora();  // Crea la instancia de la calculadora
            }
        });
    }

    public calculadora() {
        frame = new JFrame("Calculadora Mejorada");
        frame.setSize(400, 550);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(new Color(30, 30, 40));

        // Pantalla para mostrar los números y resultados
        display = new JTextField();
        display.setFont(new Font("Arial", Font.BOLD, 32));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setBackground(new Color(40, 40, 60));
        display.setForeground(Color.WHITE);
        display.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(display, BorderLayout.NORTH);

        // Panel para los botones
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 4, 8, 8));
        panel.setBackground(new Color(30, 30, 40));

        // Botones de la calculadora
        String[] buttons = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "=", "+",
                "√", "%", "^", "←",
                "AC"
        };

        // Crear los botones y agregar a la interfaz con colores
        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.BOLD, 22));
            button.setFocusPainted(false);
            if (text.matches("[0-9]")) {
                button.setBackground(new Color(50, 50, 120));
            } else if (text.equals("AC")) {
                button.setBackground(new Color(56, 134, 14));
            } else if (text.equals("←")) {
                button.setBackground(new Color(200, 120, 20));
            } else if (text.equals("=")) {
                button.setBackground(new Color(0, 120, 215));
            } else if (text.equals("√") || text.equals("%") || text.equals("^")) {
                button.setBackground(new Color(120, 20, 120));
            } else {
                button.setBackground(new Color(10, 10, 108));
            }
            button.setForeground(Color.WHITE);
            button.addActionListener(new ButtonClickListener());
            panel.add(button);
        }

        frame.add(panel, BorderLayout.CENTER);

        // Soporte para teclado
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });
        frame.setFocusable(true);

        frame.setVisible(true);
    }

    // Acción cuando un botón es presionado
    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String buttonText = e.getActionCommand();

            if (buttonText.equals("=")) {
                calculate();
            } else if (buttonText.equals("+" ) || buttonText.equals("-" ) || buttonText.equals("*" ) || buttonText.equals("/" ) || buttonText.equals("^")) {
                setOperator(buttonText);
            } else if (buttonText.equals("√")) {
                sqrtOperation();
            } else if (buttonText.equals("%")) {
                percentOperation();
            } else if (buttonText.equals("AC")) {
                clear();
            } else if (buttonText.equals("←")) {
                backspace();
            } else {
                if (justCalculated) {
                    currentInput = "";
                    justCalculated = false;
                }
                currentInput += buttonText;
                display.setText(currentInput);
            }
        }
    }

    // Realiza el cálculo basado en el operador
    private void calculate() {
        if (operator.isEmpty() || previousInput.isEmpty() || currentInput.isEmpty()) {
            display.setText("Entrada incompleta");
            return;
        }
        double num1, num2, result = 0;
        try {
            num1 = Double.parseDouble(previousInput);
            num2 = Double.parseDouble(currentInput);
        } catch (NumberFormatException ex) {
            display.setText("Error de formato");
            return;
        }
        String op = operator;
        try {
            switch (operator) {
                case "+":
                    result = num1 + num2;
                    break;
                case "-":
                    result = num1 - num2;
                    break;
                case "*":
                    result = num1 * num2;
                    break;
                case "/":
                    if (num2 != 0) {
                        result = num1 / num2;
                    } else {
                        display.setText("División por cero");
                        return;
                    }
                    break;
                case "^":
                    result = Math.pow(num1, num2);
                    break;
            }
        } catch (Exception ex) {
            display.setText("Error en cálculo");
            return;
        }
        display.setText(String.valueOf(result));
        currentInput = String.valueOf(result);
        previousInput = "";
        operator = "";
        justCalculated = true;
    }

    // Configura el operador y guarda el primer número
    private void setOperator(String operator) {
        if (!currentInput.isEmpty()) {
            if (!this.operator.isEmpty() && !previousInput.isEmpty()) {
                // Permitir operaciones encadenadas
                calculate();
                previousInput = currentInput;
                currentInput = "";
            } else {
                previousInput = currentInput;
                currentInput = "";
            }
        }
        this.operator = operator;
        justCalculated = false;
    }

    // Operación de raíz cuadrada
    private void sqrtOperation() {
        if (currentInput.isEmpty()) {
            display.setText("Sin número");
            return;
        }
        try {
            double num = Double.parseDouble(currentInput);
            if (num < 0) {
                display.setText("Raíz de negativo");
                return;
            }
            double result = Math.sqrt(num);
            display.setText(String.valueOf(result));
            currentInput = String.valueOf(result);
            justCalculated = true;
        } catch (NumberFormatException ex) {
            display.setText("Error de formato");
        }
    }

    // Operación de porcentaje
    private void percentOperation() {
        if (currentInput.isEmpty()) {
            display.setText("Sin número");
            return;
        }
        try {
            double num = Double.parseDouble(currentInput);
            double result = num / 100.0;
            display.setText(String.valueOf(result));
            currentInput = String.valueOf(result);
            justCalculated = true;
        } catch (NumberFormatException ex) {
            display.setText("Error de formato");
        }
    }

    // Botón de retroceso
    private void backspace() {
        if (!currentInput.isEmpty()) {
            currentInput = currentInput.substring(0, currentInput.length() - 1);
            display.setText(currentInput);
        }
    }

    // Limpia la pantalla y las variables
    private void clear() {
        currentInput = "";
        previousInput = "";
        operator = "";
        display.setText("");
    }

    // Manejo de eventos de teclado
    private void handleKeyPress(KeyEvent e) {
        int keyCode = e.getKeyCode();
        char keyChar = e.getKeyChar();
        if (keyCode == KeyEvent.VK_ENTER || keyChar == '=') {
            calculate();
        } else if (keyCode == KeyEvent.VK_BACK_SPACE) {
            backspace();
        } else if (keyCode == KeyEvent.VK_DELETE || keyChar == 'c' || keyChar == 'C') {
            clear();
        } else if (keyChar == '+') {
            setOperator("+");
        } else if (keyChar == '-') {
            setOperator("-");
        } else if (keyChar == '*') {
            setOperator("*");
        } else if (keyChar == '/') {
            setOperator("/");
        } else if (keyChar == '^') {
            setOperator("^");
        } else if (keyChar == '%') {
            percentOperation();
        } else if (keyChar == 'r' || keyChar == 'R' || keyChar == 'v' || keyChar == 'V') { // r/v para raíz
            sqrtOperation();
        } else if (Character.isDigit(keyChar)) {
            if (justCalculated) {
                currentInput = "";
                justCalculated = false;
            }
            currentInput += keyChar;
            display.setText(currentInput);
        } else if (keyChar == '.') {
            if (justCalculated) {
                currentInput = "";
                justCalculated = false;
            }
            currentInput += ".";
            display.setText(currentInput);
        }
    }
}
