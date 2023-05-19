import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {
    // would like to make these lists accessible in main method
    static List<Dance> dancesList;
    static List<Dancer> dancerList;

    static HashMap<String, Dance> danceStringHashMap = new HashMap();
    static HashMap<String, Dancer> dancerStringHashMap = new HashMap();

    public static void main(String[] args) {
        List<Dance> dancesList = new ArrayList<>();
        String[] test = new String[100];
        
        // scanning dances data
        try (BufferedReader br = new BufferedReader(new FileReader("data/dances1.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // getting name of dances in array
                String[] values = line.split(",");

                // adding new dancer to master list with name, empty arrayList, empty dancer list
                Dance d = new Dance(values[0], new ArrayList<Dancer>(), values[1]);
                dancesList.add(d);

                // adding dances to hashMap
                danceStringHashMap.put(values[0], d);
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

                // creating dances list in dancer
                ArrayList<Dance> list = new ArrayList<Dance>();
                for (String name : dances) {
                    Dance dance = danceStringHashMap.get(name.trim());
                    list.add(dance);
                }

                // adding new dancer to master list and HashMap
                Dancer d = new Dancer(values[0].substring(0, values[0].length()-1), list, Integer.valueOf(values[2].substring(1)));
                dancerList.add(d);
                dancerStringHashMap.put(d.name, d);

                for (Dance listObject : list) {
                    listObject.dancers.add(d);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Unable to find input csv");
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.err.println("Unable to read input csv");
            throw new RuntimeException(e);
        }

//        // START ALGORITHM CODE
        int numShows = dancesList.size()/14;
        Dance[] lineup = new Dance[dancesList.size()];

        ArrayList<Integer> ints = new ArrayList<Integer>();
        // Initial assignments: assigning all dances to a random place in the lineup
        for (Dance d : dancesList) {
            int rand = (int) (Math.random() * (dancesList.size() - 0)) + 0;
            while (ints.contains(rand)) {
                rand = (int) (Math.random() * (dancesList.size() - 0)) + 0;
            }
            ints.add(rand);
            lineup[rand] = d;
        }

        // performing switches randomly
        for (int i = 0; i < 1000; i++) {
            int r1 = (int) (Math.random() * dancesList.size());
            int r2 = (int) (Math.random() * dancesList.size());

            Dance dance1 = lineup[r1];
            Dance dance2 = lineup[r2];

            for (Dancer d : dance1.dancers) {
                if (lineup[r1 - 1].dancers.contains(d)) {
                }
            }
        }
    }

    // prints dancers and their info
    public static void printDancerList(List<Dancer> list) {
        int count = 1;
        for (Dancer d : list) {
            System.out.print(count + ". " + d.name + ", " + d.numDances + ", ");
            for (Dance s : d.dances) {
                System.out.print(s.name + ", ");
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
            for (Dancer s : d.dancers) {
                System.out.print(s.name + ", ");
            }
            System.out.println();
            count++;
        }
    }

    public static void printLineup(Dance[] lineup) {
        System.out.println("Lineup: ");
        for (int i = 0; i < lineup.length; i++) {
            System.out.println(i + ". " + lineup[i].name);
        }
    }

    static class Dancer {
        String name;
        ArrayList<Dance> dances;
        int numDances;

        public Dancer(String name, ArrayList<Dance> dances, int numDances) {
            this.name = name;
            this.dances = dances;
            this.numDances = numDances;
        }
    }

    static class Dance {
        String name;
        String category;
        int size;
        ArrayList<Dancer> dancers;

        public Dance(String name, ArrayList<Dancer> dancers, String category) {
            this.name = name;
            this.dancers = dancers;
            this.category = category;
        }
    }

    static class Show {
        int num;
        int[] lineup;
        int size;

        public Show(int num, int[] lineup) {
            this.num = num;
            this.lineup = lineup;
        }
    }
}
