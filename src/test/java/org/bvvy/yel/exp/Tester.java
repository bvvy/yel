package org.bvvy.yel.exp;

/**
 * @author bvvy
 * @date 2022/2/11
 */
public class Tester {

    private final String name;
    private Tester tester;

    public Tester(String name) {
        this.name = name;
    }

    public Tester getTester() {
        return tester;
    }

    public void setTester(Tester tester) {
        this.tester = tester;
    }

    public String getName() {
        return name;
    }

    public String say(String name) {
        return "Hi " + name + ", I am " + this.name;
    }

    public String say(Integer age) {
        return "I am " + this.name + "; I am " + age + " years old";
    }


}
