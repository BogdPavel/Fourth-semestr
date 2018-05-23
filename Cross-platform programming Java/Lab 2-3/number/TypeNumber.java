package number;

import human.HumanModel;
import result.CalculateViewController;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Администратор on 14.03.2018.
 */
public class TypeNumber {

    private JDialog typeNumberDialog;
    private static JRadioButton algebraic;
    private static JRadioButton exponential;
    private HumanModel human;

    public TypeNumber(String name, HumanModel human) {
        this.human = human;
        typeNumberDialog = new JDialog();
        algebraic = new JRadioButton("Комплексные числа в алгебраическом виде");
        exponential = new JRadioButton("Комплексные числа в показательном виде");
        addComponentsToPane(typeNumberDialog.getContentPane());
        setWindowProperties(name);
    }

    private void setWindowProperties(String name) {
        typeNumberDialog.setModal(true);
        typeNumberDialog.setTitle(name);
        typeNumberDialog.setResizable(false);
        typeNumberDialog.setSize(310,125);
        typeNumberDialog.setLocationRelativeTo(null);
        typeNumberDialog.setVisible(true);
    }

    private void addComponentsToPane(Container pane) {
        ButtonGroup bg = new ButtonGroup();
        bg.add(algebraic);
        bg.add(exponential);

        JLabel label = new JLabel("С чем будем работать?");

        Box textLayout = Box.createVerticalBox();
        textLayout.add(label);
        textLayout.add(algebraic);
        textLayout.add(exponential);

        JPanel textPanel = new JPanel();
        textPanel.add(textLayout);

        JButton nextButton = new JButton("Далее");
        nextButton.addActionListener(e -> {
            if(algebraic.isSelected()) {
                new CalculateViewController("Калькулятор", "Действительная", "Мнимая", human);
            }
            else if(exponential.isSelected()) {
                new CalculateViewController("Калькулятор", "Модуль", "Фаза", human);
            }
        });

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(nextButton, BorderLayout.EAST);

        Box layout = Box.createVerticalBox();
        layout.add(textPanel);
        layout.add(buttonPanel);

        pane.add(layout);
    }

    public static JRadioButton getAlgebraicButton() {
        return algebraic;
    }
}
