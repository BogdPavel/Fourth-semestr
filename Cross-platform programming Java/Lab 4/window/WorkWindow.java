package window;

import integral.Trapeze;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import polish.ExpressionParser;
import java.util.List;

public class WorkWindow {

    private Display display;
    private Shell shell;

    public WorkWindow() {

        display = new Display();
        shell = new Shell(display, SWT.CLOSE | SWT.TITLE);
        shell.setBounds(400, 200, 400, 200);
        shell.setText("Integral");


        createAndWork();

        shell.pack();
        shell.open();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }

        display.dispose();

    }

    private void createAndWork() {

        //Composite layer = new Composite(shell, SWT.NONE);
        GridLayout exprLayout = new GridLayout();
        exprLayout.numColumns = 3;
        shell.setLayout(exprLayout);

        Text finishText = new Text(shell, SWT.NONE);
        finishText.setText("100");
        GridData data = new GridData();
        data.horizontalSpan = 2;
        data.horizontalAlignment = SWT.RIGHT;
        finishText.setLayoutData(data);
        Label buf = new Label(shell, SWT.NONE);

        Label label = new Label(shell, SWT.NONE);
        label.setText("Input expression");

        Label integralLabel = new Label(shell, SWT.NONE);
        integralLabel.setText("∫");
        data = new GridData();
        data.horizontalSpan = 1;
        data.horizontalAlignment = SWT.RIGHT;
        integralLabel.setLayoutData(data);

        Text exprText = new Text(shell, SWT.NONE);
        exprText.setText("");
        data = new GridData();
        data.minimumWidth = 70;
        data.horizontalSpan = 1;
        data.horizontalAlignment = SWT.FILL;
        exprText.setLayoutData(data);

        Text startText = new Text(shell, SWT.NONE);
        startText.setText("-10");
        data = new GridData();
        data.horizontalSpan = 2;
        data.horizontalAlignment = SWT.RIGHT;
        startText.setLayoutData(data);
        buf = new Label(shell, SWT.NONE);

        Label numPartLabel = new Label(shell, SWT.NONE);
        numPartLabel.setText("Number of partitions");
        Text numPartText = new Text(shell, SWT.NONE);
        numPartText.setText("10");
        Button upButton = new Button(shell, SWT.PUSH);
        upButton.setText("UP");
        data = new GridData();
        data.horizontalSpan = 1;
        data.horizontalAlignment = SWT.CENTER;
        upButton.setLayoutData(data);

        Button resultButton = new Button(shell, SWT.PUSH);
        resultButton.setText("Get result");
        data = new GridData();
        data.horizontalSpan = 2;
        data.horizontalAlignment = SWT.FILL;
        resultButton.setLayoutData(data);

        Text resultText = new Text(shell, SWT.NONE);
        resultText.setText("");

        Listener listener = new Listener() {
            @Override
            public void handleEvent(Event event) {
                if(event.widget == resultButton) {

                    int start = Integer.parseInt(startText.getText());
                    int finish = Integer.parseInt(finishText.getText());
                    int partitions = Integer.parseInt(numPartText.getText());

                    ExpressionParser n = new ExpressionParser();
                    List<String> expression = n.parse(exprText.getText());
                    Trapeze trapeze = new Trapeze( (finish - start) / (double)partitions, partitions);
                    trapeze.calculate(expression);
                    resultText.setText(Double.toString(trapeze.getResult()));
                }
                if(event.widget == upButton) {
                    numPartText.setText(Integer.toString(Integer.parseInt(numPartText.getText()) + 1));
                }

            }
        };

        resultButton.addListener(SWT.Selection, listener);
        upButton.addListener(SWT.Selection, listener);

    }


}
