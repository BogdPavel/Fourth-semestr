import human.HumanModel;
import human.HumanViewController;
import result.Expression;

import javax.swing.*;

public class Main {
    private static void createMainWindow() {
        Expression expression = new Expression();
        HumanModel human = new HumanModel(expression);
        JFrame mainWindow = new HumanViewController(human);
    }

    public static void main(String [] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createMainWindow();
            }
        });
    }
}
