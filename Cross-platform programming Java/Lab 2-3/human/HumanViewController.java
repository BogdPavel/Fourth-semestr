package human;

import human.HumanModel;
import number.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by Администратор on 13.03.2018.
 */
public class HumanViewController extends JFrame {

    private HumanModel human;

    public HumanViewController(HumanModel human) {
        super("Калькулятор");
        this.human = human;
        addComponentsToPane(this.getContentPane());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(200, 90);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    private void addComponentsToPane(Container pane) {
        GridLayout grid = new GridLayout(2,2);

        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(grid);

        gridPanel.add(new JLabel("Имя"));

        JTextField nameText = new JTextField();
        nameText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                int code = e.getKeyCode();
                if((code >= e.VK_0 && code <= e.VK_9) || (code >= e.VK_NUMPAD0 && code <= e.VK_NUMPAD9) || code == e.VK_PLUS || code == e.VK_SPACE)
                    nameText.setEditable(false);
                else nameText.setEditable(true);
            }
        });
        gridPanel.add(nameText);

        gridPanel.add(new JLabel("Фамилия"));

        JTextField surnameText = new JTextField();
        gridPanel.add(surnameText);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        JButton nextButton = new JButton("Войти");
        nextButton.setMargin(new Insets(0, 0, 0, 0));
        nextButton.addActionListener(e -> {
            human.setName(nameText.getText());
            human.setSurname(surnameText.getText());

            if(!human.getName().equals("") && !human.getSurname().equals(""))
                new TypeNumber("Калькулятор", human);

            nameText.setText("");
            surnameText.setText("");
        });

        buttonPanel.add(nextButton, BorderLayout.EAST);

        Box layout = Box.createVerticalBox();
        layout.add(gridPanel);
        layout.add(buttonPanel);

        pane.add(layout);
    }
}
