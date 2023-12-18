//Bundle uploaded at 12/18/2023 23:23:58
import java.util.*;
import java.util.stream.Collectors;
class Drone {
}
class Fish {
}
enum FishType {
    JELLY, FISH, CRAB;
}
class Game {
}
record Vector(int x, int y) {
}
class Coordinate {
    int x;
    int y;
    boolean isVisited = false;
    public Coordinate(int x, int y, boolean isVisited) {
        this.x = x;
        this.y = y;
        this.isVisited = isVisited;
    }
}
record FishDetail(int color, int type) {
}
class Fish {
    private int fishId;
    private Vector pos;
    private Vector speed;
    private FishDetail detail;
    private double value;
    private boolean saved;
    public Fish(int fishId, Vector pos, Vector speed, FishDetail detail, double value, boolean saved) {
        this.fishId = fishId;
        this.pos = pos;
        this.speed = speed;
        this.detail = detail;
        this.value = value;
        this.saved = saved;
    }
    public int getFishId() {
        return fishId;
    }
    public void setFishId(int fishId) {
        this.fishId = fishId;
    }
    public Vector getPos() {
        return pos;
    }
    public void setPos(Vector pos) {
        this.pos = pos;
    }
    public Vector getSpeed() {
        return speed;
    }
    public void setSpeed(Vector speed) {
        this.speed = speed;
    }
    public FishDetail getDetail() {
        return detail;
    }
    public void setDetail(FishDetail detail) {
        this.detail = detail;
    }
    public double getValue() {
        return value;
    }
    public void setValue(double value) {
        this.value = value;
    }
    public boolean isSaved() {
        return saved;
    }
    public void setSaved(boolean saved) {
        this.saved = saved;
    }
}
record Drone(int droneId, Vector pos, boolean dead, int battery, List<Integer> scans) {
}
record RadarBlip(int fishId, String dir) {
}
class Player {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        //OK OK So the fishes are bound to their habitat! So rada blips are only useful for fine tuning the drone movements.
        List<Coordinate> coordinatesToVisit = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            int dy = 2500 * i + 1000;
            for (int j = 0; j < 9; j++) {
                int dx = 1000 * j + 1000;
                coordinatesToVisit.add(new Coordinate(dx, dy, false));
            }
        }
        int sinceLastLight = 0;
        Map<Integer, FishDetail> fishDetails = new HashMap<>();
        List<Fish> fishes = new ArrayList<>();
        int fishCount = in.nextInt();
        for (int i = 0; i < fishCount; i++) {
            int fishId = in.nextInt();
            int color = in.nextInt();
            int type = in.nextInt();
            fishDetails.put(fishId, new FishDetail(color, type));
            fishes.add(new Fish(fishId, new Vector(0, 0), new Vector(0, 0), new FishDetail(color, type), type, false));
        }
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
            List<Fish> visibleFishes = new ArrayList<>();
            int myScanCount = in.nextInt();
            for (int i = 0; i < myScanCount; i++) {
                int fishId = in.nextInt();
                myScans.add(fishId);
                for(Fish fish : fishes){
                    if(fish.getFishId() == fishId){
                        fish.setSaved(true);
                    }
                }
            }
            //print out scanned fishes;
            for (Integer fishId : myScans) {
                System.err.printf("Fish %d was saved%n", fishId);
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
                visibleFishes.add(new Fish(fishId, pos, speed, detail, detail.type(), false));
            }
            int myRadarBlipCount = in.nextInt();
            for (int i = 0; i < myRadarBlipCount; i++) {
                int droneId = in.nextInt();
                int fishId = in.nextInt();
                String radar = in.next();
                myRadarBlips.get(droneId).add(new RadarBlip(fishId, radar));
            }
            for (Map.Entry<Integer, List<RadarBlip>> entry : myRadarBlips.entrySet()) {
                System.err.printf("Drone %d has %d radar blips%n", entry.getKey(), entry.getValue().size());
            }
            for (Drone drone : myDrones) {
                if (drone.pos().y() < 500) {
                    System.err.println("drone is surfaced, saving fishies");
                    for (Fish fish : scannedFishes) {
                        fish.setSaved(true);
                    }
                }
            }
            for (Drone drone : myDrones) {
                int x = drone.pos().x();
                int y = drone.pos().y();
                int targetX = 5000;
                int targetY = 5000;
                List<Fish> closeFishes = new ArrayList<>();
                for (Fish fish : visibleFishes) {
                    if (scannedFishes.stream().anyMatch(f -> f.getFishId() == fish.getFishId())) {
                        continue;
                    }
                    int distance = Math.abs(fish.getPos().x() - x) + Math.abs(fish.getPos().y() - y);
                    if (distance < 800 && !fish.isSaved()) {
                        closeFishes.add(fish);
                        System.err.println("Fish is close to drone, adding to list X " + fish.getPos().x() + " Y " + fish.getPos().y());
                    }
                }
                //TODO: make this work with many drones
                scannedFishes.addAll(closeFishes);
                System.err.println("Scanned fishes " + scannedFishes);
                int goToSurface = 0;
                ArrayList<Integer> scannedFishIdsForDecidingSurfacing = new ArrayList<>();
                for (Fish fish : scannedFishes) {
                    if (!fish.isSaved()) {
                        goToSurface = 1;
                        scannedFishIdsForDecidingSurfacing.add(fish.getFishId());
                    }
                }
                System.err.println("Scanned fish ids for deciding surfacing " + scannedFishIdsForDecidingSurfacing);
                //TODO: make utils class for this
                Map<String, Integer> radarBlips = new HashMap<>();
                for (RadarBlip radarBlip : myRadarBlips.get(drone.droneId())) {
                    switch (radarBlip.dir()) {
                        case "TL" -> radarBlips.put("TL", radarBlips.getOrDefault("TL", 0) + 1);
                        case "TR" -> radarBlips.put("TR", radarBlips.getOrDefault("TR", 0) + 1);
                        case "BL" -> radarBlips.put("BL", radarBlips.getOrDefault("BL", 0) + 1);
                        case "BR" -> radarBlips.put("BR", radarBlips.getOrDefault("BR", 0) + 1);
                    }
                }
                int max = 0;
                String maxKey = "";
                for (Map.Entry<String, Integer> entry : radarBlips.entrySet()) {
                    if (entry.getValue() > max) {
                        max = entry.getValue();
                        maxKey = entry.getKey();
                    }
                }
                Vector dronePos = drone.pos();
                if (maxKey.equals("TL")) {
                    if (dronePos.x() - 1000 > 0) {
                        targetX = dronePos.x() - 1000;
                    }
                    if (dronePos.y() - 1000 > 0) {
                        targetY = dronePos.y() - 1000;
                    }
                }
                if (maxKey.equals("TR")) {
                    if (dronePos.x() + 1000 < 10000) {
                        targetX = dronePos.x() + 1000;
                    }
                    if (dronePos.y() - 1000 > 0) {
                        targetY = dronePos.y() - 1000;
                    }
                }
                if (maxKey.equals("BL")) {
                    if (dronePos.x() - 1000 > 0) {
                        targetX = dronePos.x() - 1000;
                    }
                    if (dronePos.y() + 1000 < 10000) {
                        targetY = dronePos.y() + 1000;
                    }
                }
                if (maxKey.equals("BR")) {
                    if (dronePos.x() + 1000 < 10000) {
                        targetX = dronePos.x() + 1000;
                    }
                    if (dronePos.y() + 1000 < 10000) {
                        targetY = dronePos.y() + 1000;
                    }
                }
                //calculate most optimal route to visit all target points in targetPoints list. If no target points left, go to surface.
                if (coordinatesToVisit.size() > 0 && goToSurface == 0) {
                    int minDistance = Integer.MAX_VALUE;
                    Coordinate closestCoordinate = null;
                    for (Coordinate c : coordinatesToVisit) {
                        int distance = Math.abs(c.x - dronePos.x()) + Math.abs(c.y - dronePos.y());
                        if (distance < minDistance && !c.isVisited) {
                            minDistance = distance;
                            closestCoordinate = c;
                        }
                    }
                    if(closestCoordinate != null){
                        targetX = closestCoordinate.x;
                        targetY = closestCoordinate.y;
                        System.err.println("Drone is setting next target to point X " + targetX + " Y " + targetY);
                    }
                }
                boolean closeToCoordinate = false;
                for (Coordinate c : coordinatesToVisit) {
                    if(c.isVisited){
                        continue;
                    }
                    int distance = Math.abs(c.x - dronePos.x()) + Math.abs(c.y - dronePos.y());
                    if (distance < 600) {
                        //error logging
                        System.err.println("Drone is close to target point, removing from list X " + c.x + " Y " + c.y);
                        c.isVisited = true;
                        closeToCoordinate = true;
                    }
                }
                System.err.println("Scanned ids " + myScans);
                System.err.printf("Drone %d is heading to %d %d ", drone.droneId(), targetX, targetY);
                int light = 0;
                System.err.println("Drone battery " + drone.battery());
                if (closeToCoordinate && sinceLastLight > 5) {
                    light = 1;
                    System.err.println("fish close to drone, light time");
                } else if (drone.battery() == 30) {
                    light = 1;
                } else {
                    light = 0;
                    sinceLastLight++;
                }
                System.err.println("Light " + light);
                if (goToSurface == 1) {
                    System.out.printf("MOVE %d %d %d%n", drone.pos().x(), 499, light);
                } else {
                    System.out.printf("MOVE %d %d %d%n", targetX, targetY, light);
                }
            }
        }
    }
}
class RadarBlip {
}
