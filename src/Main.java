import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {
    static HashMap<String, Dance> danceStringHashMap = new HashMap();
    static HashMap<String, Dancer> dancerStringHashMap = new HashMap();

    public static void main(String[] args) {
        ArrayList<Dance> dancesList = new ArrayList<>();
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

        // START ALGORITHM CODE
        // deciding where the mini show splits are
        dancesList.remove(danceStringHashMap.get("SL P"));
        dancesList.remove(danceStringHashMap.get("Jr Musical Theater"));
        int numShows = (dancesList.size())/12;
        int leftover = (dancesList.size())%12;
        Dance[] lineup = new Dance[dancesList.size() + (numShows*2)];

        // setting the opening and closing shows
        ArrayList<Integer> ints = new ArrayList<Integer>();
        int cutOff = 14 + (leftover/numShows);
        for (int i = 0; i < lineup.length; i += cutOff) {
            ints.add(i);
            ints.add(i + cutOff - 1);
            lineup[i] = danceStringHashMap.get("Jr Musical Theater");
            if (i + cutOff >= lineup.length) {
                lineup[lineup.length-1] = danceStringHashMap.get("SL P");
            }
            else {
                lineup[i + cutOff - 1] = danceStringHashMap.get("SL P");
            }
        }

        // Initial assignments: assigning all dances to a random place in the lineup
        for (Dance d : dancesList) {
            int rand = (int) (Math.random() * lineup.length);
            while (ints.contains(rand)) {
                rand = (int) (Math.random() * lineup.length);
            }
            ints.add(rand);
            lineup[rand] = d;
        }

        // performing switches randomly
        for (int i = 0; i < 1000000; i++) {
            // getting and generating random dances
            int r1 = (int) (Math.random() * dancesList.size());
            int r2 = (int) (Math.random() * dancesList.size());
            Dance dance1 = lineup[r1];
            Dance dance2 = lineup[r2];
            // ensuring it's not an opening/closing dance
            while (dance1 == danceStringHashMap.get("SL P") || dance1 == danceStringHashMap.get("Jr Musical Theater")) {
                r1 = (int) (Math.random() * dancesList.size());
                dance1 = lineup[r1];
            }
            while (dance2 == danceStringHashMap.get("SL P") || dance2 == danceStringHashMap.get("Jr Musical Theater")) {
                r2 = (int) (Math.random() * dancesList.size());
                dance2 = lineup[r2];
            }

            // getting arraylist dancers who have back to backs in each dance
            ArrayList<Dancer> btb1 = checkBackToBacks(dance1, r1, lineup, dancesList);
            ArrayList<Dancer> btb2 = checkBackToBacks(dance2, r2, lineup, dancesList);

            // deciding switches
            // if switching fixes the problem then SWITCH
            if (checkBackToBacks(dance1, r2, lineup, dancesList) == null && checkBackToBacks(dance2, r1, lineup, dancesList) == null) {
                lineup[r1] = dance2;
                lineup[r2] = dance1;
            }
            // if switching does not fix either problems then SWITCH
            else if (checkBackToBacks(dance1, r2, lineup, dancesList) != null && checkBackToBacks(dance2, r1, lineup, dancesList) != null) {
                lineup[r1] = dance2;
                lineup[r2] = dance1;
            }
            // if switching does makes the amount of dancers with btbs less then SWITCH
            else if (checkBackToBacks(dance1, r2, lineup, dancesList).size() <  btb1.size() || checkBackToBacks(dance2, r1, lineup, dancesList).size() <  btb2.size()) {
                lineup[r1] = dance2;
                lineup[r2] = dance1;
            }
        }
        // need to update algorithm more...
        printLineup(lineup);

        System.out.println("CHECKING BTBS----------");
        for (int i = 0; i < lineup.length; i++) {
            ArrayList<Dancer> btb = checkBackToBacks(lineup[i], i, lineup, dancesList);
            System.out.print(lineup[i].name + ": ");
            for (Dancer d : btb) {
                System.out.print(d.name + ", ");
            }
            System.out.println();
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
    public static void printDancesList(ArrayList<Dance> list) {
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
            if (lineup[i] == null) {
                System.out.println("Dance not found: " + lineup[i]);
            }
            else {
                System.out.println(i + ". " + lineup[i].name);
            }
        }
    }

    public static ArrayList<Dancer> checkBackToBacks(Dance dance, int position, Dance[] lineup, ArrayList<Dance> dancesList) {
        ArrayList<Dancer> backToBacks = new ArrayList<>();
        for (Dancer d : dance.dancers) {
            // r1 is first dance
            if (position == 0) {
                if (lineup[position+1].dancers.contains(d)) {
                    backToBacks.add(d);
                }
            }
            // r1 is last dance
            else if (position == lineup.length-1) {
                if (lineup[position-1].dancers.contains(d)) {
                    backToBacks.add(d);
                }
            }
            // r1 is not first or last dance
            else {
                if (lineup[position-1].dancers.contains(d) || lineup[position+1].dancers.contains(d)) {
                    backToBacks.add(d);
                }
            }
        }
        return backToBacks;
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
