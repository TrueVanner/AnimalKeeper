import java.util.Scanner;

/**
 * AnimalKeeper.
 * 
 * TODO:
 * 
 * Documentation
 * 
 * END TODO
 */
public class AnimalKeeper {
    public static void main(String[] args) {
        MyZoo.initFoodStorage();
        MyZoo.initHomes();

        Scanner sc = new Scanner(System.in);
        String output = "";
        boolean currentResult = true;

        while (sc.hasNext()) {
            int operation = sc.nextInt();
            switch (operation) {
                case 0 -> currentResult = MyZoo.addAnimal(sc.nextInt(), sc.next(), sc.nextInt());
                case 1 -> currentResult = MyZoo.moveAnimal(sc.next(), sc.nextInt());
                case 2 -> currentResult = MyZoo.removeAnimal(sc.next());
                case 3 -> currentResult = MyZoo.buyFood(sc.nextInt(), sc.nextInt());
                case 4 -> currentResult = MyZoo.feed(sc.nextInt(), sc.nextInt(), sc.nextInt());
                default -> {
                    sc.close();
                    System.out.println(output);
                    System.exit(0);
                }
            }
            System.out.print(operation + (currentResult ? "" : "!") + " ");
        }
    }
}