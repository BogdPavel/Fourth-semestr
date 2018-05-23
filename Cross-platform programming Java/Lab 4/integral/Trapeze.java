package integral;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 * Created by Администратор on 13.04.2018.
 */
public class Trapeze {

    private double valueInX0;
    private double valueInXi;
    private double valueInXn;
    private double step;
    private double result;
    private int partitions;

    public Trapeze(double step, int partitions) {

        this.step = step;
        this.partitions = partitions;
        this.valueInXi = 0;

    }

    public void calculate(List<String> postfix) {
        double arrayOfValues[] = new double[partitions];
        int arrayOfXpositions[] = {-1, -1, -1, -1, -1};
        for (int i = 0, j = 0; j < postfix.size(); j++) {
            if (postfix.get(j).equals("x")) {
                arrayOfXpositions[i++] = j;
            }
        }
        for(int i = 0; i < partitions; i++) {
            int j = 0;
            while(arrayOfXpositions[j] != -1) {
                postfix.set(arrayOfXpositions[j++], Double.toString(step * i));
            }
            arrayOfValues[i] = calc(postfix);
        }
        valueInX0 = arrayOfValues[0];
        valueInXn = arrayOfValues[partitions - 1];
        for(int i = 1; i < partitions - 1; i++)
            valueInXi += arrayOfValues[i];
    }

    public double getResult() {
        result = (step / 2) * (valueInX0 + 2 * valueInXi + valueInXn);
        return result;
    }

    private Double calc(List<String> postfix) {
        Thread thread = new Thread();
        thread.start();
        Deque<Double> stack = new ArrayDeque<Double>();
        for (String x : postfix) {
            if (x.equals("sqrt")) stack.push(Math.sqrt(stack.pop()));
            else if (x.equals("cube")) {
                Double tmp = stack.pop();
                stack.push(tmp * tmp * tmp);
            }
            else if (x.equals("pow")) stack.push(Math.pow(stack.pop(), 2));
            else if (x.equals("+")) stack.push(stack.pop() + stack.pop());
            else if (x.equals("-")) {
                Double b = stack.pop(), a = stack.pop();
                stack.push(a - b);
            }
            else if (x.equals("*")) stack.push(stack.pop() * stack.pop());
            else if (x.equals("/")) {
                Double b = stack.pop(), a = stack.pop();
                stack.push(a / b);
            }
            else if (x.equals("u-")) stack.push(-stack.pop());
            else stack.push(Double.valueOf(x));
        }
        return stack.pop();
    }

}
