package human;

import result.Expression;

/**
 * Created by Администратор on 15.03.2018.
 */
public class HumanModel {

    private String name;
    private String surname;
    private Expression expression;

    public HumanModel(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public HumanModel() {}

    public HumanModel(Expression expression) {
        this.expression = expression;
    }

    public String calculateExpression() {
        return expression.getNum().getValue();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }
}
