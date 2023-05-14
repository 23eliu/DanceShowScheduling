import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    // would like to make these lists accessible in main method
//    static List<Dance> dancesList;
//    static List<Dancer> dancerList;

    public static void main(String[] args) {
        List<Dance> dancesList = new ArrayList<>();

        // scanning dances data
        try (BufferedReader br = new BufferedReader(new FileReader("data/dances1.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // getting dances in array
                String[] values = line.split(",");

                // adding new dancer to master list
                dancesList.add(new Dance(values[0], values[1]));

            }
        } catch (FileNotFoundException e) {
            System.err.println("Unable to find input csv");
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.err.println("Unable to read input csv");
            throw new RuntimeException(e);
        }

        // scanning dancer data
        List<Dancer> dancerList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("data/dancers.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // getting dances in array
                String[] values = line.split("\"");
                String[] dances = values[1].split(",");

                // adding new dancer to master list
                dancerList.add(new Dancer(values[0].substring(0, values[0].length()-1), dances, Integer.valueOf(values[2].substring(1))));

            }
        } catch (FileNotFoundException e) {
            System.err.println("Unable to find input csv");
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.err.println("Unable to read input csv");
            throw new RuntimeException(e);
        }

//        printDancerList(dancerList);
//        printDancesList(dancesList);
    }

    // prints dancers and their info
    public static void printDancerList(List<Dancer> list) {
        int count = 1;
        for (Dancer d : list) {
            System.out.print(count + ". " + d.name + ", " + d.numDances + ", ");
            for (String s : d.dances) {
                System.out.print(s + ", ");
            }
            System.out.println();
            count++;
        }
    }

    // prints dances and their info
    public static void printDancesList(List<Dance> list) {
        int count = 1;
        for (Dance d : list) {
            System.out.print(count + ". " + d.name + " (" + d.category + ", " + d.size + ") ");
            System.out.println("Dancers: ");
            // NEED TO ADD DANCERS STILL
            for (String s : d.dancers) {
                System.out.print(s + ", ");
            }
            System.out.println();
            count++;
        }
    }

    static class Dancer {
        String name;
        String[] dances;
        int numDances;

        public Dancer(String name, String[] dances, int numDances) {
            this.name = name;
            this.dances = dances;
            this.numDances = numDances;

            // adding dancer to list of dancers in a dance
            for (int i = 0; i < dances.length; i++) {
//                dancesList.get()
            }
        }
    }

    static class Dance {
        String name;
        String category;
        int size;
        String[] dancers;

        public Dance(String name, String category) {
            this.name = name;
            this.category = category;
        }
    }

    static class Show {
        int num;
        List<Dance> lineup;
        int size;

        public Show(int num, List<Dance> lineup) {
            this.num = num;
            this.lineup = lineup;
        }
    }
}
