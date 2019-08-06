package com.jonmoore;

import java.io.*;
import java.util.*;

public class Locations implements Map<Integer, Location> {
    private static Map<Integer, Location> locations = new LinkedHashMap<>();

    public static void main(String[] args) throws IOException {
        try (DataOutputStream locFile = new DataOutputStream(new BufferedOutputStream(
                        new FileOutputStream("locations.dat")))) {
            for (Location loc : locations.values()) {
                locFile.writeInt(loc.getLocationID());
                locFile.writeUTF(loc.getDescription());
                System.out.println("Writing location " + loc.getLocationID() + ": " + loc.getDescription());
                System.out.println("Writing " + (loc.getExits().size() -1) + " exits");
                locFile.writeInt(loc.getExits().size());
                for (String direction : loc.getExits().keySet()) {
                    if (!direction.equalsIgnoreCase("Q")) {
                        System.out.println("\t\t" + direction + "," + loc.getExits().get(direction));
                        locFile.writeUTF(direction);
                        locFile.writeInt(loc.getExits().get(direction));
                    }
                }
            }
        }
        // try(
        //         BufferedWriter locFile = new BufferedWriter(new FileWriter("locations.txt"));
        //         BufferedWriter dirFile = new BufferedWriter(new FileWriter("directions.txt"))
        // ) {
        //     for (Location location : locations.values()) {
        //         locFile.write(location.getLocationID() + "," + location.getDescription() + "\n");
        //         for (String direction : location.getExits().keySet()) {
        //             dirFile.write(location.getLocationID() + "," + direction + "," + location.getExits().get(direction) + "\n");
        //         }
        //     }
        // }
    }
    static {
        try(Scanner scanner = new Scanner(
                new BufferedReader(new FileReader("locations_big.txt")))) {
            scanner.useDelimiter(",");
            while (scanner.hasNextLine()) {
                int loc = scanner.nextInt();
                scanner.skip(scanner.delimiter());
                String description = scanner.nextLine();
                System.out.println("Imported loc: " + loc + ": " + description);
                Map<String, Integer> tempExit = new LinkedHashMap<>();
                locations.put(loc, new Location(loc, description, tempExit));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try(BufferedReader dirFile = new BufferedReader(new FileReader("directions_big.txt"))) {
            String input;
            while ((input = dirFile.readLine()) != null) {
                String[] data = input.split(",");
                int loc = Integer.parseInt(data[0]);
                String direction = data[1];
                int destination = Integer.parseInt(data[2]);
                System.out.println(loc + ": " + direction + ": " + destination);
                Location location = locations.get(loc);
                location.addExit(direction, destination);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int size() {
        return locations.size();
    }

    @Override
    public boolean isEmpty() {
        return locations.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return locations.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return locations.containsValue(value);
    }

    @Override
    public Location get(Object key) {
        return locations.get(key);
    }

    @Override
    public Location put(Integer key, Location value) {
        return locations.put(key, value);
    }

    @Override
    public Location remove(Object key) {
        return locations.remove(key);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Location> m) {

    }

    @Override
    public void clear() {
        locations.clear();
    }

    @Override
    public Set<Integer> keySet() {
        return locations.keySet();
    }

    @Override
    public Collection<Location> values() {
        return locations.values();
    }

    @Override
    public Set<Entry<Integer, Location>> entrySet() {
        return locations.entrySet();
    }
}
