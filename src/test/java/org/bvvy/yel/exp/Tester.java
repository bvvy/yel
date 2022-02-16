package org.bvvy.yel.exp;

/**
 * @author bvvy
 * @date 2022/2/11
 */
public class Tester {

    String name = "Tester";

    public String say(String name) {
        return "Hi " + name + ", I am " + this.name;
    }

    public String say(Integer age) {
        return "I am " + this.name + "; I am " + age + " years old";
    }


}
