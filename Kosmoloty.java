//package com.example.basics;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

public class Kosmoloty {
    public static void main(String[] args)
    {

        // kosmolot
        abstract class Spacecraft {
            String name;

            Integer x;
            Integer xv;

            Integer y;
            Integer yv;

            Integer distance;

            public Spacecraft(String name, Integer xv, Integer yv, Integer x, Integer y) {

                if( name.matches("[a-zA-Z0-9]{1,10}") ) {
                    this.name = name;
                } else {
                    System.out.println("klops");
                    System.exit(0);
                }


                this.x = x;

                if ( xv >= -10000 && xv <= 10000 && yv >= -10000 && yv <= 10000) {
                    this.yv = yv;
                    this.xv = xv;
                } else {
                    System.out.println("klops");
                    System.exit(0);
                }

                this.y = y;

                this.distance = 0;
            }

            public String getName() {
                return name;
            }

            public Integer getX() {
                return x;
            }

            public Integer getXv() {
                return xv;
            }

            public Integer getY() {
                return y;
            }

            public Integer getYv() {
                return yv;
            }

            public void setX(Integer x) {
                this.x = x;
            }

            public void setY(Integer y) {
                this.y = y;
            }

            public void setDistance(Integer distance) {
                this.distance = distance;
            }

            @Override
            public String toString() {
                return "Spacecraft{" +
                        "name='" + name + '\'' +
                        ", xv=" + xv +
                        ", yv=" + yv +
                        ", x=" + x +
                        ", y=" + y +
                        ", distance=" + distance +
                        '}';
            }


        }

        class SpacecraftToStartList extends Spacecraft {
            public SpacecraftToStartList(String name, Integer xv, Integer yv, Integer x, Integer y) {
                super(name, xv, yv, x, y);
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                SpacecraftToStartList that = (SpacecraftToStartList) o;
                return Objects.equals(name, that.name);
            }

            @Override
            public int hashCode() {
                return Objects.hash(name);
            }
        }

        class SpacecraftToRace extends Spacecraft {
            public SpacecraftToRace(String name, Integer xv, Integer yv, Integer x, Integer y) {
                super(name, xv, yv, x, y);
            }

            public SpacecraftToRace move(){
                this.x += this.xv;
                this.y += this.yv;
                return this;
            }

            public Integer calculateDistancePerAtomicOperation() {
                return abs(this.xv) + abs(this.yv);
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                SpacecraftToRace that = (SpacecraftToRace) o;
                return Objects.equals(x, that.x) && Objects.equals(y, that.y);
            }

            @Override
            public int hashCode() {
                return Objects.hash(x, y);
            }

        }


        class Torus {
            Integer x;
            Integer y;
            ArrayList<SpacecraftToStartList> spacecraftsStartList;
            ArrayList<SpacecraftToRace> spacecraftsRace;
            Integer teleportationCount;

            public Torus(Integer x, Integer y) {
                this.x = x;
                this.y = y;
                this.teleportationCount = 0;
                this.spacecraftsStartList = new ArrayList<SpacecraftToStartList>();
                this.spacecraftsRace = new ArrayList<SpacecraftToRace>();
            }

            public void addSpacecraft(SpacecraftToStartList spacecraft) {

                if(this.spacecraftsStartList.contains(spacecraft)) {
                    System.out.println("klops");
                    System.exit(0);
                } else {
                    this.spacecraftsStartList.add(spacecraft);
                }
                if(this.spacecraftsStartList.size() > 100000) {
                    System.out.println("klops");
                    System.exit(0);
                }
            }

            private void startListToRace() {
                // przepisujemy do listy ToRace - nadpisana metoda porownywania na współrzędne
                for (int i = 0; i < this.spacecraftsStartList.size(); i++) {
                    this.spacecraftsRace.add(
                            new SpacecraftToRace(this.spacecraftsStartList.get(i).getName(),
                                    this.spacecraftsStartList.get(i).getXv(),
                                    this.spacecraftsStartList.get(i).getYv(),
                                    this.spacecraftsStartList.get(i).getX(),
                                    this.spacecraftsStartList.get(i).getY())
                    );
                }

                // Przepisanie statków ze startu do Seta
                Set<SpacecraftToRace> spacecraftsRaceSet = new HashSet<SpacecraftToRace>(this.spacecraftsRace);

                // Jeżeli na starcie są kolizje to kończymy
                if(spacecraftsRaceSet.size() < this.spacecraftsRace.size()) {
                    /* There are duplicates */
                    System.out.println("klops");
                    System.exit(0);
                }
            }

            public void atomicOperation() {
                // jeżeli jest to pierwsza teleportacja
                if (this.teleportationCount == 0) {
                    startListToRace();
                }

                // przesunięcie statków
                this.spacecraftsRace.forEach(spacecraft -> spacecraft.move());

                // przepisanie do Seta (wykrywanie statków na tych samych polach)
                Set<SpacecraftToRace> spacecraftsRaceSet = new HashSet<SpacecraftToRace>(this.spacecraftsRace);
                if(spacecraftsRaceSet.size() < this.spacecraftsRace.size()) {
                    anihilation();
                }

                //jeżeli wszystkie uległy anihilacji
                if(this.spacecraftsRace.size() == 0) {
                    System.out.println("remis");
                    System.exit(0);
                }

                // jeżeli inne uległy anihilacji
                if(this.spacecraftsRace.size() == 1) {
                    System.out.println(spacecraftsRace.get(0).getName());
                    System.exit(0);
                }

                // zwiększamy licznik
                this.teleportationCount++;
            }

            private void anihilation() {
                // Set do którego będzie przepisywana lista statków
                Set<SpacecraftToRace> spacecraftsRaceSet = new HashSet<SpacecraftToRace>();

                // Set z duplikatami na pozycji
                Set<SpacecraftToRace> spacecraftsRaceSetOfDuplicates = new HashSet<SpacecraftToRace>();

                // Pętla po wszystkich statkach biorących udział w wyścigu
                for(int i = 0; i < this.spacecraftsRace.size(); i++) {

                    // Dodajemy do seta kolejne statki, jeżeli się nie uda to jest to duplikat
                    if ( !spacecraftsRaceSet.add(this.spacecraftsRace.get(i)) ) {
                        spacecraftsRaceSetOfDuplicates.add(this.spacecraftsRace.get(i));
                    }
                }

                // Anihilacja
                this.spacecraftsRace.removeAll(spacecraftsRaceSetOfDuplicates);
            }

            public String findWinner() {
                Map<String, Integer> nameDistanceMap = new HashMap<>();
                for (int i = 0; i < this.spacecraftsRace.size(); i++) {
                    nameDistanceMap.put(this.spacecraftsRace.get(i).getName(), this.spacecraftsRace.get(i).calculateDistancePerAtomicOperation());
                }

                List<Integer> distances = nameDistanceMap.values().stream().sorted().map(v -> v).collect(Collectors.toList());

                if (distances.get(distances.size() - 2) == distances.get(distances.size() - 1)) {
                    return "remis";
                } else {
                    for (Map.Entry<String, Integer> entry : nameDistanceMap.entrySet()) {
                        if (entry.getValue().equals(distances.get(distances.size() - 1))) {
                            return entry.getKey();
                        }

                    }
                }
                return "klops";
            }

            @Override
            public String toString() {
                return "Torus{" +
                        "x=" + x +
                        ", y=" + y +
                        ", spacecraftsStartList=" + spacecraftsStartList +
                        ", spacecraftsRace=" + spacecraftsRace +
                        ", teleportationCount=" + teleportationCount +
                        '}';
            }

            public Integer getTeleportationCount() {
                return teleportationCount;
            }
        }
	// write your code here

        Integer torusX = null;
        Integer torusY = null;
        // walidacja torusa wg. typu
        try {
            torusX = Integer.valueOf(args[0]);
            torusY = Integer.valueOf(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("klops");
            System.exit(0);
        }

        Torus torus = null;
        // walidacja torusa wg. wartości
        if (torusX >= 1 && torusX <= 100000 && torusY >=1 && torusY <= 100000) {
            torus = new Torus(torusX, torusY);
        } else {
            System.out.println("klops");
            System.exit(0);
        }

        Scanner sc = null;
        try {
            sc = new Scanner(System.in, "UTF-8");
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                List<String> spacecraftDetailsList = Arrays.stream(line.split(",")).toList();
                SpacecraftToStartList spacecraftStartList = new SpacecraftToStartList(
                        spacecraftDetailsList.get(0),
                        Integer.valueOf(spacecraftDetailsList.get(1)),
                        Integer.valueOf(spacecraftDetailsList.get(2)),
                        Integer.valueOf(spacecraftDetailsList.get(3)),
                        Integer.valueOf(spacecraftDetailsList.get(4))

                );

                if (spacecraftStartList.getX() >= 0 && spacecraftStartList.getX() <= torusX - 1 && spacecraftStartList.getY() >= 0 && spacecraftStartList.getY() <= torusY - 1) {
                    torus.addSpacecraft(spacecraftStartList);
                } else {
                    System.out.println("klops");
                    System.exit(0);
                }


            }
            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        } catch (Exception e) {
            System.out.println("klops");
            System.exit(0);
        }

        while (torus.getTeleportationCount() < (24 * 3600)) {
            torus.atomicOperation();
        }
        System.out.println(torus.findWinner());
        System.exit(0);


    }
}
