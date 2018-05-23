package number;

/**
 * Created by Администратор on 15.03.2018.
 */
public class ExponentialComplexNumber extends AbstractComplexNumber {

    private int module;
    private int phase;

    public ExponentialComplexNumber(int module, int phase) {
        this.module = module;
        this.phase = phase;
    }

    public ExponentialComplexNumber() {
        this.module = 0;
        this.phase = 0;
    }

    public ExponentialComplexNumber(AlgebraicComplexNumber number) {
        double x = number.getImaginaryPart() / number.getRealPart();
        this.module = (int)Math.sqrt((int)Math.pow(number.getRealPart(), 2) + (int)Math.pow(number.getImaginaryPart(), 2));
        if(number.getRealPart() > 0) {
            number.setImaginaryPart((int)Math.atan(x));
        }
        else if(number.getRealPart() < 0 && number.getImaginaryPart() > 0) {
            number.setImaginaryPart((int)(Math.PI + Math.atan(x)));
        }
        else if(number.getRealPart() < 0 && number.getImaginaryPart() < 0) {
            number.setImaginaryPart((int)(-Math.PI + Math.atan(x)));
        }
    }

    public Number add(Number right) {
        ExponentialComplexNumber temp = this;
        AlgebraicComplexNumber result = AlgebraicComplexNumber.class.cast(
                AlgebraicComplexNumber.class.cast(temp).add(
                        AlgebraicComplexNumber.class.cast(right))
        );
        temp = new ExponentialComplexNumber(result);
        return temp;
    }

    public Number subtract(Number right) {
        ExponentialComplexNumber temp = this;
        AlgebraicComplexNumber result = AlgebraicComplexNumber.class.cast(AlgebraicComplexNumber.class.cast(temp).subtract(AlgebraicComplexNumber.class.cast(right)));
        temp = new ExponentialComplexNumber(result);
        return temp;
    }

    public Number divide(Number right) {
        ExponentialComplexNumber rightNumber = ExponentialComplexNumber.class.cast(right);
        ExponentialComplexNumber result = new ExponentialComplexNumber();
        result.module = this.module / rightNumber.module;
        result.phase = this.phase - rightNumber.phase;
        return result;
    }

    public Number multiply(Number right) {
        ExponentialComplexNumber rightNumber = ExponentialComplexNumber.class.cast(right);
        ExponentialComplexNumber result = new ExponentialComplexNumber();
        result.module = this.module * rightNumber.module;
        result.phase = this.phase + rightNumber.phase;
        return result;
    }

    public int getModule() {
        return module;
    }

    public void setModule(int module) {
        this.module = module;
    }

    public int getPhase() {
        return phase;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }
}
