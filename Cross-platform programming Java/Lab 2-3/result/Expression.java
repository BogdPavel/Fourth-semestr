package result;

import number.AlgebraicComplexNumber;
import number.ExponentialComplexNumber;
import number.Number;
import number.TypeNumber;
import java.util.ArrayList;

/**
 * Created by Администратор on 16.03.2018.
 */
public class Expression {

    private ArrayList<Number> numbers = new ArrayList<>();
    private char operation;
    private Number num;

    public Expression(Number first, Number second, char operation) {
        numbers.add(first);
        numbers.add(second);
        this.operation = operation;
    }

    public Expression() {

    }

    public Number calculateResult(int a, int b, int c, int d) {
        num = new Number();
        if(TypeNumber.getAlgebraicButton().isSelected()) {
            AlgebraicComplexNumber result = new AlgebraicComplexNumber();
            AlgebraicComplexNumber first = new AlgebraicComplexNumber(a, b); setNumbers(first);
            AlgebraicComplexNumber second = new AlgebraicComplexNumber(c, d); setNumbers(second);
            switch (operation) {
                case '+': result = AlgebraicComplexNumber.class.cast(first.add(second)); break;
                case '-': result = AlgebraicComplexNumber.class.cast(first.subtract(second)); break;
                case '/': result = AlgebraicComplexNumber.class.cast(first.divide(second)); break;
                case '*': result = AlgebraicComplexNumber.class.cast(first.multiply(second)); break;
            }
            numbers.add(result);
            num.setValue(new String(Integer.toString(result.getRealPart()) + (result.getImaginaryPart() > 0 ? ("+" + Integer.toString(result.getImaginaryPart())) : Integer.toString(result.getImaginaryPart())) + "i"));
        }
        else {
            ExponentialComplexNumber result = new ExponentialComplexNumber();
            ExponentialComplexNumber first = new ExponentialComplexNumber(a, b); setNumbers(first);
            ExponentialComplexNumber second = new ExponentialComplexNumber(c, d); setNumbers(second);
            switch (operation) {
                case '+': result = ExponentialComplexNumber.class.cast(first.add(second)); break;
                case '-': result = ExponentialComplexNumber.class.cast(first.subtract(second)); break;
                case '/': result = ExponentialComplexNumber.class.cast(first.divide(second)); break;
                case '*': result = ExponentialComplexNumber.class.cast(first.multiply(second)); break;
            }
            numbers.add(result);
            num.setValue(new String("|" + Integer.toString(result.getModule()) + "|e^(i" + Integer.toString(result.getPhase()) + ")"));
        }
        return num;
    }

    public ArrayList<Number> getNumbers() {
        return numbers;
    }

    public void setNumbers(Number number) {
        numbers.add(number);
    }

    public char getOperation() {
        return operation;
    }

    public void setOperation(char operation) {
        this.operation = operation;
    }

    public Number getNum() { return num; }

    public void setNum(Number num) { this.num = num; }
}
