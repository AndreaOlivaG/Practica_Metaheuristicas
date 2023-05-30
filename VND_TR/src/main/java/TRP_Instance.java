import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class TRP_Instance {

    private int numElems;
    private int numPaths;
    private float maxTime;
    private float[][] distances;
    private int[] scores;
    private String name;
    private ArrayList<TRP_City> cities;
    private int maxPossibleScore;

    public TRP_Instance(String path) {
        readInstance(path);
    }

    public void readInstance(String path) {
        Random rnd = new Random(90);
        try {
            String line;
            float x1, x2, y1, y2;
            int aux;
            this.maxPossibleScore = 0;

            path = path.replace("\\", "/");
            BufferedReader br = new BufferedReader(new FileReader(path));

            String[] splits = path.split("/");
            this.name = splits[splits.length - 1].split(".txt")[0].split("^p")[1];
            this.numElems = Integer.parseInt(br.readLine().split(" ")[1]);
            this.numPaths = Integer.parseInt(br.readLine().split(" ")[1]);
            this.maxTime = Float.parseFloat(br.readLine().split(" ")[1]);
            float[][] coordinates = new float[this.numElems][2];
            this.distances = new float[this.numElems][this.numElems];
            this.scores = new int[this.numElems];
            this.cities = new ArrayList<>();

            line = br.readLine();
            aux = 0;
            for (int i = 0; i < this.numElems; i++) {
                String[] parts = line.split("\t");
                coordinates[aux][0] = Float.parseFloat(parts[0]);
                coordinates[aux][1] = Float.parseFloat(parts[1]);
                this.scores[aux] = Integer.parseInt(parts[2]);
                this.maxPossibleScore += this.scores[aux];
                line = br.readLine();
                aux += 1;
            }

            for (int i = 0; i < this.numElems; i++) {
                if (i != 0 && i != this.numElems - 1) {
                    this.cities.add(new TRP_City(i, this.getScores()[i]));
                }
                x1 = coordinates[i][0];
                y1 = coordinates[i][1];
                for (int j = 0; j < this.numElems; j++) {
                    x2 = coordinates[j][0];
                    y2 = coordinates[j][1];
                    this.distances[i][j] = (float) Math.sqrt(Math.pow((y2 - y1), 2) + Math.pow((x2 - x1), 2));
                }
            }

            Collections.shuffle(this.cities, rnd);

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public int getNumElems() {
        return this.numElems;
    }

    public int getNumPaths() {
        return this.numPaths;
    }

    public float getMaxTime() {
        return this.maxTime;
    }

    public float[][] getDistances() {
        return this.distances;
    }

    public int[] getScores() {
        return this.scores;
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<TRP_City> getCities() {
        return this.cities;
    }

    public int getMaxPossibleScore() {
        return maxPossibleScore;
    }

}
