import java.util.*;

record Vector(int x, int y) {
}

record FishDetail(int color, int type) {
}

record Fish(int fishId, Vector pos, Vector speed, FishDetail detail, String value) {
}

record ScannedFish(int fishId, Vector pos, FishDetail detail, String value) {
}

record Drone(int droneId, Vector pos, boolean dead, int battery, List<Integer> scans) {
}

record RadarBlip(int fishId, String dir) {
}

class Player {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        Map<Integer, FishDetail> fishDetails = new HashMap<>();

        int fishCount = in.nextInt();
        for (int i = 0; i < fishCount; i++) {
            int fishId = in.nextInt();
            int color = in.nextInt();
            int type = in.nextInt();
            fishDetails.put(fishId, new FishDetail(color, type));
        }
        List<Fish> visibleFishes = new ArrayList<>();
        List<Fish> scannedFishes = new ArrayList<>();
        // game loop
        while (true) {
            List<Integer> myScans = new ArrayList<>();
            List<Integer> foeScans = new ArrayList<>();
            Map<Integer, Drone> droneById = new HashMap<>();
            List<Drone> myDrones = new ArrayList<>();
            List<Drone> foeDrones = new ArrayList<>();

            Map<Integer, List<RadarBlip>> myRadarBlips = new HashMap<>();

            int myScore = in.nextInt();
            int foeScore = in.nextInt();

            int myScanCount = in.nextInt();
            for (int i = 0; i < myScanCount; i++) {
                int fishId = in.nextInt();
                myScans.add(fishId);
            }

            int foeScanCount = in.nextInt();
            for (int i = 0; i < foeScanCount; i++) {
                int fishId = in.nextInt();
                foeScans.add(fishId);
            }


            int myDroneCount = in.nextInt();
            for (int i = 0; i < myDroneCount; i++) {
                int droneId = in.nextInt();
                int droneX = in.nextInt();
                int droneY = in.nextInt();
                boolean dead = in.nextInt() == 1;
                int battery = in.nextInt();
                Vector pos = new Vector(droneX, droneY);
                Drone drone = new Drone(droneId, pos, dead, battery, new ArrayList<>());
                droneById.put(droneId, drone);
                myDrones.add(drone);
                myRadarBlips.put(droneId, new ArrayList<>());
            }

            int foeDroneCount = in.nextInt();
            for (int i = 0; i < foeDroneCount; i++) {
                int droneId = in.nextInt();
                int droneX = in.nextInt();
                int droneY = in.nextInt();
                boolean dead = in.nextInt() == 1;
                int battery = in.nextInt();
                Vector pos = new Vector(droneX, droneY);
                Drone drone = new Drone(droneId, pos, dead, battery, new ArrayList<>());
                droneById.put(droneId, drone);
                foeDrones.add(drone);
            }

            int droneScanCount = in.nextInt();
            for (int i = 0; i < droneScanCount; i++) {
                int droneId = in.nextInt();
                int fishId = in.nextInt();
                droneById.get(droneId).scans().add(fishId);
            }

            int visibleFishCount = in.nextInt();
            for (int i = 0; i < visibleFishCount; i++) {
                int fishId = in.nextInt();
                int fishX = in.nextInt();
                int fishY = in.nextInt();
                int fishVx = in.nextInt();
                int fishVy = in.nextInt();
                Vector pos = new Vector(fishX, fishY);
                Vector speed = new Vector(fishVx, fishVy);
                FishDetail detail = fishDetails.get(fishId);
                visibleFishes.add(new Fish(fishId, pos, speed, detail));
            }

            int myRadarBlipCount = in.nextInt();
            for (int i = 0; i < myRadarBlipCount; i++) {
                int droneId = in.nextInt();
                int fishId = in.nextInt();
                String radar = in.next();
                myRadarBlips.get(droneId).add(new RadarBlip(fishId, radar));
            }

            for (Drone drone : myDrones) {
                int x = drone.pos().x();
                int y = drone.pos().y();
                int targetX = 5000;
                int targetY = 5000;

                Fish closestFish = null;
                int closestDistance = Integer.MAX_VALUE;
                for (Fish fish : visibleFishes) {
                    if (scannedFishes.stream().anyMatch(f -> f.fishId() == fish.fishId())) {
                        continue;
                    }
                    int distance = Math.abs(fish.pos().x() - x) + Math.abs(fish.pos().y() - y);
                    if (distance < closestDistance) {
                        closestFish = fish;
                        closestDistance = distance;
                    }
                }

                if (closestFish != null) {
                    targetX = closestFish.pos().x();
                    targetY = closestFish.pos().y();
                }

                System.err.println(String.format("Drone %d is heading to %d %d", drone.droneId(), targetX, targetY));

                int light = 0;

                if (Math.abs(targetX - x) + Math.abs(targetY - y) <= 1 && closestFish != null) {
                    light = 1;
                    System.out.println(String.format("MOVE %d %d %d", targetX, targetY, light));

                    scannedFishes.add(new Fish(closestFish.fishId(), closestFish.pos(), closestFish.speed(), closestFish.detail()));
                } else {
                    light = 0;
                    System.out.println(String.format("MOVE %d %d %d", targetX, targetY, light));
                }


            }
        }
    }
}
