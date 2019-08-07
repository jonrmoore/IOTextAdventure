package com.jonmoore;

import java.io.*;
import java.util.*;

public class Locations implements Map<Integer, Location> {
    private static Map<Integer, Location> locations = new LinkedHashMap<>();
    private static Map<Integer, IndexRecord> index = new LinkedHashMap<>();

    public static void main(String[] args) throws IOException {
        /**
         * RANDOM ACCESS
         */
        try (RandomAccessFile rao = new RandomAccessFile("locations_rand.dat", "rwd")) {
            // The first 4 bytes contain the number of locations
            rao.writeInt(locations.size());

            // The next 4 bytes contain the start offset and locations section
            int indexSize = locations.size() * 3 * Integer.BYTES;

            // The next section of the file contains the index. The index is 1692 bytes long, starting at 8 and ending at 1699
            int locationStart = (int) (indexSize + rao.getFilePointer() + Integer.BYTES);

            rao.writeInt(locationStart);
            long indexStart = rao.getFilePointer();

            int startPointer = locationStart;
            rao.seek(startPointer);

            for (Location location : locations.values()) {
                rao.writeInt(location.getLocationID());
                rao.writeUTF(location.getDescription());
                StringBuilder sb = new StringBuilder();
                for (String direction : location.getExits().keySet()) {
                    if (!direction.equalsIgnoreCase("Q")) {
                        sb.append(direction);
                        sb.append(",");
                        sb.append(location.getExits().get(direction));
                        sb.append(",");
                    }
                }
                rao.writeUTF(sb.toString());
                IndexRecord record = new IndexRecord(startPointer, (int) (rao.getFilePointer() - 1));
                index.put(location.getLocationID(), record);

                startPointer = (int) rao.getFilePointer();
            }
        }

        /**
         * SEQUENTIAL
         */
        // try (ObjectOutputStream locFile = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("locations.dat")))) {
        //     for (Location location : locations.values()) {
        //         locFile.writeObject(location);
        //     }
        // }
    }

    static {
        try (ObjectInputStream locFile = new ObjectInputStream(new BufferedInputStream(new FileInputStream("locations.dat")))) {
            boolean eof = false;
            while (!eof) {
                try {
                    Location location = (Location) locFile.readObject();
                    System.out.println("Read location " + location.getLocationID() + ": " + location.getDescription());
                    System.out.println("Found " + location.getExits().size() + " exits");
                    locations.put(location.getLocationID(), location);
                } catch (EOFException e) {
                    eof = true;
                }
            }
        } catch (IOException e) {
            System.out.println("IOException");
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException" + e.getMessage());
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
