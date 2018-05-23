package result;

import human.HumanModel;
import number.AlgebraicComplexNumber;
import number.ExponentialComplexNumber;
import number.TypeNumber;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by Администратор on 14.03.2018.
 */
public class CalculateViewController {

    private JDialog typeNumberDialog;
    private Expression expression;
    private JTextField firstWordFirst;
    private JTextField firstWordSecond;
    private JTextField secondWordFirst;
    private JTextField secondWordSecond;
    private JTextField expressionText;

    public CalculateViewController(String name, String firstWord, String secondWord, HumanModel human) {
        this.expression = human.getExpression();
        typeNumberDialog = new JDialog();
        addComponentsToPane(typeNumberDialog.getContentPane(), firstWord, secondWord);
        setWindowProperties(name);
    }

    private void setWindowProperties(String name) {
        typeNumberDialog.setModal(true);
        typeNumberDialog.setTitle(name);
        typeNumberDialog.setResizable(false);
        typeNumberDialog.setSize(410,150);
        typeNumberDialog.setLocationRelativeTo(null);
        typeNumberDialog.setVisible(true);
    }

    private void addComponentsToPane(Container pane, String firstWord, String secondWord) {
        GridLayout topLayout = new GridLayout(1,2);

        JPanel gridTopPanel = new JPanel();
        gridTopPanel.setLayout(topLayout);
        gridTopPanel.add(new JLabel("Первое число"));
        gridTopPanel.add(new JLabel("Второе число"));

        GridLayout gridText = new GridLayout(2,4);

        JPanel gridTextPanel = new JPanel();
        gridTextPanel.setLayout(gridText);
        gridTextPanel.add(new JLabel(firstWord));
        gridTextPanel.add(new JLabel(secondWord));
        gridTextPanel.add(new JLabel(firstWord));
        gridTextPanel.add(new JLabel(secondWord));

        firstWordFirst = new JTextField();
        firstWordFirst.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                textValidation(e, firstWordFirst);
            }
        });

        firstWordSecond = new JTextField();
        firstWordSecond.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                textValidation(e, firstWordSecond);
            }
        });

        secondWordFirst = new JTextField();
        secondWordFirst.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                textValidation(e, secondWordFirst);
            }
        });

        secondWordSecond = new JTextField();
        secondWordSecond.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                textValidation(e, secondWordSecond);
            }
        });

        gridTextPanel.add(firstWordFirst);
        gridTextPanel.add(firstWordSecond);
        gridTextPanel.add(secondWordFirst);
        gridTextPanel.add(secondWordSecond);

        expressionText = new JTextField();
        expressionText.setEditable(false);
        expressionText.setBorder(BorderFactory.createEmptyBorder());

        Box expressionLayout = Box.createHorizontalBox();
        expressionLayout.add(new JLabel("Ваше выражение"), BorderLayout.WEST);
        expressionLayout.add(expressionText);

        JButton plusButton = new JButton("+");
        plusButton.setMargin(new Insets(0,0,0,0));
        plusButton.addActionListener(e -> {
            buttonListener('+');
        });

        JButton minusButton = new JButton("-");
        minusButton.setMargin(new Insets(0,0,0,0));
        minusButton.addActionListener(e -> {
            buttonListener('-');
        });

        GridLayout gridButton1 = new GridLayout(1,4);

        JPanel gridButtonPanel1 = new JPanel();
        gridButtonPanel1.setLayout(gridButton1);
        gridButtonPanel1.add(plusButton);
        gridButtonPanel1.add(minusButton);

        JButton divideButton = new JButton("/");
        divideButton.setMargin(new Insets(0,0,0,0));
        divideButton.addActionListener(e -> {
            buttonListener('/');
        });

        JButton multiplyButton = new JButton("*");
        multiplyButton.setMargin(new Insets(0,0,0,0));
        multiplyButton.addActionListener(e -> {
            buttonListener('*');
        });

        GridLayout gridButton2 = new GridLayout(1,4);

        JPanel gridButtonPanel2 = new JPanel();
        gridButtonPanel2.setLayout(gridButton2);
        gridButtonPanel2.add(divideButton);
        gridButtonPanel2.add(multiplyButton);

        GridLayout gridButton = new GridLayout(1,4);

        JPanel gridButtonPanel = new JPanel();
        gridButtonPanel.setLayout(gridButton);
        gridButtonPanel.add(new JLabel());
        gridButtonPanel.add(gridButtonPanel1);
        gridButtonPanel.add(gridButtonPanel2);
        gridButtonPanel.add(new JLabel());

        JLabel resultText = new JLabel();

        JButton resultButton = new JButton("Вычислить");
        resultButton.setMargin(new Insets(0,0,0,0));
        resultButton.addActionListener(e -> {
            if(!expressionText.getText().equals(""))
                resultText.setText(expression.getNum().getValue());
        });

        JLabel otherFormText = new JLabel();

        JButton otherFormButton = new JButton("Преобразовать");
        otherFormButton.setMargin(new Insets(0,0,0,0));
        otherFormButton.addActionListener(e -> {
            if(!expressionText.getText().equals("")) {
                if (TypeNumber.getAlgebraicButton().isSelected()) {
                    ExponentialComplexNumber temp = new ExponentialComplexNumber(AlgebraicComplexNumber.class.cast(expression.getNumbers().get(2)));
                    otherFormText.setText(new String("|" + Integer.toString(temp.getModule()) + "|e^(i" + Integer.toString(temp.getPhase()) + ")"));
                } else {
                    AlgebraicComplexNumber temp = new AlgebraicComplexNumber(ExponentialComplexNumber.class.cast(expression.getNumbers().get(2)));
                    otherFormText.setText(new String(Integer.toString(temp.getRealPart()) + (temp.getImaginaryPart() > 0 ? ("+" + Integer.toString(temp.getImaginaryPart())) : Integer.toString(temp.getImaginaryPart())) + "i"));
                }
            }
        });

        GridLayout gridResult = new GridLayout(1,4);

        JPanel gridResultPanel = new JPanel();
        gridResultPanel.setLayout(gridResult);
        gridResultPanel.add(resultButton);
        gridResultPanel.add(resultText);
        gridResultPanel.add(otherFormButton);
        gridResultPanel.add(otherFormText);

        Box layout = Box.createVerticalBox();
        layout.add(gridTopPanel);
        layout.add(gridTextPanel);
        layout.add(gridButtonPanel);
        layout.add(expressionLayout);
        layout.add(gridResultPanel);
        pane.add(layout);
        typeNumberDialog.pack();
    }

    private void buttonListener(char operation) {
        if(TypeNumber.getAlgebraicButton().isSelected()) {
            expression.setOperation(operation);
            expression.calculateResult(Integer.parseInt(firstWordFirst.getText()), Integer.parseInt(firstWordSecond.getText()),Integer.parseInt(secondWordFirst.getText()), Integer.parseInt(secondWordSecond.getText()));
            expressionText.setText("(" + firstWordFirst.getText() + (Integer.parseInt(firstWordSecond.getText()) > 0 ? ("+" + firstWordSecond.getText()) : firstWordSecond.getText()) + "i)" + operation + "(" + secondWordFirst.getText() + (Integer.parseInt(secondWordSecond.getText()) > 0 ? ("+" + secondWordSecond.getText()) : secondWordSecond.getText()) + "i)");
        }
        else {
            expression.setOperation(operation);
            expression.calculateResult(Integer.parseInt(firstWordFirst.getText()), Integer.parseInt(firstWordSecond.getText()),Integer.parseInt(secondWordFirst.getText()), Integer.parseInt(secondWordSecond.getText()));
        }
    }

    private void textValidation(KeyEvent e, JTextField text) {
        int code = e.getKeyCode();
        if((code >= e.VK_0 && code <= e.VK_9) || (code >= e.VK_NUMPAD0 && code <= e.VK_NUMPAD9) || code == e.VK_BACK_SPACE)
                text.setEditable(true);
        else text.setEditable(false);
    }
}
