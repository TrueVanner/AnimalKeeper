import java.util.ArrayList;

/**
 * .
 * TODO:
 * 
 * Documentation
 * 
 * END TODO
 */
public class MyZoo {
    static ArrayList<Animal> animals = new ArrayList<>();
    static ArrayList<Home> homes = new ArrayList<>();
    static ArrayList<Food> foodStorage = new ArrayList<>();

    enum Species {
        LION,
        TIGER,
        LEOPARD,
        ZEBRA,
        ANTELOPE,
        GIRAFFE,
        BEAR
    }

    enum FoodType {
        HAY,
        CORN,
        GRAIN,
        CARROTS,
        CHICKEN,
        BEEF
    }

    enum Type {
        HERBIVORE,
        CARNIVORE,
        OMNIVORE
    }

    static class Home {
        int id;
        int capacity = 0;
        Type resiType;

        /**
         * .
         */

        Home(int id, int capacity, Type resiType) {
            this.id = id;
            this.capacity = capacity;
            this.resiType = resiType;
        }

        ArrayList<Animal> residents = new ArrayList<>(capacity);
    }

    static class Animal {
        String name;
        Species species;
        Type type;
        boolean solitary;
        int homeID;

        /**
         * Determines the type of animal based on it's species.
         */
        static Type getAnimalType(int species) {
            switch (Species.values()[species]) {
                case LION, TIGER, LEOPARD: return Type.CARNIVORE;
                case ZEBRA, ANTELOPE, GIRAFFE: return Type.HERBIVORE;
                default: return Type.OMNIVORE;
            }
        }

        /**
         * Determines whether the animal is solitary based on its species.
         */
        static boolean getSolitary(int species) {
            switch (Species.values()[species]) {
                case TIGER, LEOPARD, BEAR: return true;
                default: return false;
            }
        }

        /**
         * Finds an animal in the "animals" list based on its name.
         * Outputs null if the animal wasn't found.
         * 
         * @param name - name of the animal to find.
         * @return animal with the specified name (or null).
         */
        static Animal findByName(String name) {
            for (Animal a : animals) {
                if (a.name.equals(name)) {
                    return a;
                }
            }
            return null;
        }

        /**
         * Creates an instance of an animal.
         * 
         * @param name       - name of the animal.
         * @param animalType - type of the animal represented by an integer.
         */
        Animal(String name, int species, int homeID) {
            this.name = name;
            this.species = Species.values()[species];
            this.type = getAnimalType(species);
            this.solitary = getSolitary(species);
        }
    }

    /**
     * A class defining a food storage based on the type of food
     * and the amount of food available.
     */

    static class Food {
        FoodType type;
        int amount;
        boolean isMeat = false;

        /**
         * Creates an instance of food storage.
         */
        Food(FoodType type) {
            this.type = type;
            this.amount = 0;
            if (type == FoodType.CHICKEN || type == FoodType.BEEF) {
                this.isMeat = true;
            }
        }
    }

    /**
     * Initialises food storage by defining all food storages and
     * without any food (amount = 0).
     */
    static void initFoodStorage() {
        for (FoodType i : FoodType.values()) {
            foodStorage.add(new Food(i));
        }
    }

    /**
     * Initialises all animal homes with predefined maximum capacity
     * and without animals.
     */
    static void initHomes() {
        int capacity;
        for (int i = 0; i < 15; i++) {
            if (i < 10) {
                capacity = 2;
            } else {
                capacity = 6;
            }
            homes.add(new Home(i, capacity, null));
        }
    }

    /**
     * .
     */
    static boolean addAnimal(int species, String name, int homeID) {
        species -= 1; //since it starts at 0 here
        for (Animal i : animals) {
            if (i.name.equals(name)) {
                return false;
            }
            
            if (i.homeID == homeID && ((i.type == Type.CARNIVORE 
                && Animal.getAnimalType(species) == Type.HERBIVORE) 
                || i.type == Type.OMNIVORE || (i.type == Type.HERBIVORE 
                && Animal.getAnimalType(species) == Type.CARNIVORE) || i.solitary 
                || Animal.getSolitary(species))) {
                return false;
            }
        }
        if (homes.get(homeID).capacity == homes.get(homeID).residents.size()) {
            return false;
        }
        animals.add(new Animal(name, species, homeID));
        homes.get(homeID).residents.add(animals.get(animals.size() - 1));

        
        return true;
    }

    /**
     * Removes the animal with the specified name from the zoo.
     */
    static boolean removeAnimal(String name) {
        Animal toRemove = Animal.findByName(name);
        if (toRemove == null) {
            return false;
        }
        
        homes.get(toRemove.homeID).residents.remove(toRemove);
        animals.remove(toRemove);
        return true;
    }

    /**
     * Moves an animal with specified name to a house with specified ID.
     */
    static boolean moveAnimal(String name, int homeID) {
        Animal toMove = Animal.findByName(name);
        if (toMove == null) {
            return false;
        }
        
        homes.get(toMove.homeID).residents.remove(toMove);
        homes.get(homeID).residents.add(toMove);
        return true;
    }

    /**
     * Increases the amount of food in a specified food storage by a specified
     * amount unless the new value exceeds 100.
     */
    static boolean buyFood(int foodID, int amount) {
        foodID -= 1;
        Food storage = foodStorage.get(foodID);
        if (storage.amount + amount > 100) {
            return false;
        }
        foodStorage.get(foodID).amount = storage.amount + amount;
        return true;
    }

    /**
     * "Feeds" a specific type of food to an animal home with a specified ID.
     * Fails if the food is not compatible with dietary prefences of inhabitants.
     */
    static boolean feed(int foodID, int amount, int homeID) {
        foodID -= 1;
        Food food = foodStorage.get(foodID);
        Home home = homes.get(homeID);
        
        if (food.amount - amount < 0) {
            return false;
        }
        
        if (home.resiType == Type.CARNIVORE && !food.isMeat) {
            return false;
        }
        
        if (home.resiType == Type.OMNIVORE && !(food.isMeat || food.type == FoodType.CARROTS)) {
            return false;
        }
        
        boolean isAntelope = false;
        for (Animal i : home.residents) {
            if (i.species == Species.ANTELOPE) {
                isAntelope = true;
            }
        }

        if ((home.resiType == Type.HERBIVORE && food.isMeat) 
            || (isAntelope && food.type == FoodType.CARROTS)) {
            return false;
        }
        return true;
    }
}
