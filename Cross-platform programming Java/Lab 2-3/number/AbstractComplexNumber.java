package number;

import number.Number;

/**
 * Created by Администратор on 15.03.2018.
 */
abstract public class AbstractComplexNumber extends Number {

    abstract public Number add(Number right);

    abstract public Number subtract(Number right);

    abstract public Number divide(Number right);

    abstract public Number multiply(Number right);

}
