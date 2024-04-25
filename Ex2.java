import java.util.Scanner;

class Pizza {
    protected double price;

    public Pizza() {
        this.price = 10.0;
    }

    public double getPrice() {
        return price;
    }
}

abstract class PizzaDecorator extends Pizza {
    protected Pizza pizza;

    public PizzaDecorator(Pizza pizza) {
        this.pizza = pizza;
    }

    public double getPrice() {
        return pizza.getPrice();
    }
}

class PepperoniTopping extends PizzaDecorator {
    public PepperoniTopping(Pizza pizza) {
        super(pizza);
    }

    public double getPrice() {
        return super.getPrice() + 2.0;
    }
}

class MushroomTopping extends PizzaDecorator {
    public MushroomTopping(Pizza pizza) {
        super(pizza);
    }

    public double getPrice() {
        return super.getPrice() + 1.5;
    }
}

public class Ex2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of pepperoni toppings: ");
        int pepperoniCount = scanner.nextInt();

        System.out.print("Enter the number of mushroom toppings: ");
        int mushroomCount = scanner.nextInt();

        Pizza pizza = new Pizza();

        for (int i = 0; i < pepperoniCount; i++) {
            pizza = new PepperoniTopping(pizza);
        }

        for (int i = 0; i < mushroomCount; i++) {
            pizza = new MushroomTopping(pizza);
        }

        System.out.println("Final Price: $" + pizza.getPrice());

    }
}
