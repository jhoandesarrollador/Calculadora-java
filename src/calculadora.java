import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class calculadora {

    private JFrame frame;
    private JTextField display;
    private String currentInput = "";
    private String previousInput = "";
    private String operator = "";

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
        frame = new JFrame("Calculadora");
        frame.setSize(300, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Pantalla para mostrar los números y resultados
        display = new JTextField();
        display.setFont(new Font("Arial", Font.PLAIN, 30));
        display.setHorizontalAlignment(JTextField.RIGHT);
        frame.add(display, BorderLayout.NORTH);

        // Panel para los botones
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 4));

        // Botones de la calculadora
        String[] buttons = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "=", "+"
        };

        // Crear los botones y agregar a la interfaz con colores
        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.PLAIN, 20));
            button.setBackground(new Color(14, 16, 171)); // Color de fondo
            button.setForeground(Color.WHITE); // Color de texto
            button.addActionListener(new ButtonClickListener());
            panel.add(button);
        }

        frame.add(panel, BorderLayout.CENTER);

        // Botón para borrar la pantalla
        JButton clearButton = new JButton("AC");
        clearButton.setFont(new Font("Arial", Font.PLAIN, 20));
        clearButton.setBackground(new Color(125, 203, 83)); // Color de fondo para borrar
        clearButton.setForeground(Color.WHITE); // Color de texto para borrar
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });
        frame.add(clearButton, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    // Acción cuando un botón es presionado
    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String buttonText = e.getActionCommand();

            if (buttonText.equals("=")) {
                // Realiza el cálculo cuando se presiona "="
                calculate();
            } else if (buttonText.equals("+") || buttonText.equals("-") || buttonText.equals("*") || buttonText.equals("/")) {
                // Si el botón es un operador, lo guarda
                setOperator(buttonText);
            } else {
                // Si es un número o punto, lo agrega al número actual
                currentInput += buttonText;
                display.setText(currentInput);
            }
        }
    }

    // Realiza el cálculo basado en el operador
    private void calculate() {
        double num1 = Double.parseDouble(previousInput);
        double num2 = Double.parseDouble(currentInput);
        double result = 0;

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
                    display.setText("Error");
                    return;
                }
                break;
        }

        display.setText(String.valueOf(result));
        currentInput = String.valueOf(result);  // Mantener el resultado
        previousInput = "";  // Borrar el valor previo
        operator = "";  // Borrar el operador
    }

    // Configura el operador y guarda el primer número
    private void setOperator(String operator) {
        this.operator = operator;
        previousInput = currentInput;
        currentInput = "";
    }

    // Limpia la pantalla y las variables
    private void clear() {
        currentInput = "";
        previousInput = "";
        operator = "";
        display.setText("");
    }
}
