package number;

/**
 * Created by Администратор on 15.03.2018.
 */
public class AlgebraicComplexNumber extends AbstractComplexNumber {

    private int imaginaryPart;
    private int realPart;

    public AlgebraicComplexNumber(int realPart, int imaginaryPart) {
        this.realPart = realPart;
        this.imaginaryPart = imaginaryPart;
    }

    public AlgebraicComplexNumber() {
        this.realPart = 0;
        this.imaginaryPart = 0;
    }

    public AlgebraicComplexNumber(ExponentialComplexNumber object) {

    }

    public Number add(Number right) {
        AlgebraicComplexNumber rightNumber = AlgebraicComplexNumber.class.cast(right);
        AlgebraicComplexNumber result = new AlgebraicComplexNumber(0,0);
        result.imaginaryPart = this.imaginaryPart + rightNumber.imaginaryPart;
        result.realPart = this.realPart + rightNumber.realPart;
        return result;
    }

    public Number subtract(Number right) {
        AlgebraicComplexNumber rightNumber = AlgebraicComplexNumber.class.cast(right);
        AlgebraicComplexNumber result = new AlgebraicComplexNumber();
        result.imaginaryPart = this.imaginaryPart - rightNumber.imaginaryPart;
        result.realPart = this.realPart - rightNumber.realPart;
        return result;
    }

    public Number divide(Number right) {
        AlgebraicComplexNumber rightNumber = AlgebraicComplexNumber.class.cast(right);
        AlgebraicComplexNumber result = new AlgebraicComplexNumber();
        int denominator = (int)Math.pow(rightNumber.realPart, 2) + (int)Math.pow(rightNumber.imaginaryPart, 2);
        result.imaginaryPart = (this.imaginaryPart * (-1) * rightNumber.imaginaryPart + (-1) * this.realPart * rightNumber.realPart) / denominator;
        result.realPart = (this.realPart * (-1) * rightNumber.imaginaryPart + this.imaginaryPart * rightNumber.realPart) / denominator;
        return result;
    }

     public Number multiply(Number right) {
         AlgebraicComplexNumber rightNumber = AlgebraicComplexNumber.class.cast(right);
         AlgebraicComplexNumber result = new AlgebraicComplexNumber();
         result.realPart = (-1) * this.imaginaryPart * rightNumber.imaginaryPart + this.realPart * rightNumber.realPart;
         result.imaginaryPart = this.realPart * rightNumber.imaginaryPart + this.imaginaryPart * rightNumber.realPart;
         return result;
     }

    public int getImaginaryPart() {
        return imaginaryPart;
    }

    public void setImaginaryPart(int imaginaryPart) {
        this.imaginaryPart = imaginaryPart;
    }

    public int getRealPart() {
        return realPart;
    }

    public void setRealPart(int realPart) {
        this.realPart = realPart;
    }
}
