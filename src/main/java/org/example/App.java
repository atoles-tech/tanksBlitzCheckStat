package org.example;


public class App {

    public static void main(String[] args) throws Exception {
        Model model = new Model();
        Controller controller = new Controller(model);
        controller.run();
    }
}