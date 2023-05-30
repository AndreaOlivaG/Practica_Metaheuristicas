import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class TRP_Solution {

    private TRP_Instance instance;
    private List<TRP_Node>[] paths;
    private boolean[] visitedNodes;
    private int numElems;
    private int numPaths;
    private float maxTime;
    private float[] pathTime;
    private int score;
    private float maxPathTime;

    public TRP_Solution(TRP_Instance instance) {
        this.instance = instance;
        this.numElems = instance.getNumElems();
        this.visitedNodes = new boolean[numElems];
        this.paths = new ArrayList[instance.getNumPaths()];
        this.numPaths = 0;
        this.score = 0;
        this.maxTime = instance.getMaxTime();
        this.pathTime = new float[instance.getNumPaths()];
        this.maxPathTime = 0;
    }

    public TRP_Solution(TRP_Solution solution) {
        copy(solution);
    }

    public void copy(TRP_Solution solution) {
        this.instance = solution.getInstance();
        this.numElems = solution.getNumElems();
        this.visitedNodes = Arrays.copyOf(solution.getVisitedNodes(), this.numElems);
        this.paths = new ArrayList[solution.getNumPaths()];
        this.numPaths = solution.getNumPaths();
        for (int i = 0; i < this.paths.length; i++) {
            List<TRP_Node> listaOriginal = solution.getPaths()[i];
            List<TRP_Node> listaCopia = new ArrayList<>(listaOriginal);
            this.paths[i] = listaCopia;
        }
        this.score = solution.getScore();
        this.maxTime = solution.getMaxTime();
        this.pathTime = solution.getPathTime();
        this.maxPathTime = solution.getMaxPathTime();
    }

    public boolean isFeasible(int numPath, TRP_Node n) {
        int d = n.getNode();
        if (this.getPathTime()[numPath] + n.getTime() <= this.getMaxTime()) {
            if (d == 0 || d == this.numElems - 1) {
                return true;
            } else {
                return !this.getVisitedNodes()[d];
            }
        } else {
            return false;
        }
    }

    public boolean isFeasible(int numPath, float time) {
        return this.getPathTime()[numPath] + time <= this.getMaxTime();
    }

    public TRP_Node removeNodeFromPath(int node, int numPath) {
        if (node == 0) return null; //The first node can't be deleted
        TRP_Node toDelete = null;

        if (node == this.numElems - 1) { //If the node is the last one, delete it
            toDelete = this.paths[numPath].get(this.paths[numPath].size() - 1);
            this.paths[numPath].remove(toDelete);
            this.pathTime[numPath] -= toDelete.getTime(); //Subtract its time
            return toDelete;
        }

        for (TRP_Node n : this.paths[numPath]) {
            if (n.getNode() == node) { //Check if the node is the one we want to delete
                toDelete = n; //Save the node to delete later
                this.score -= n.getScore(); //Subtract its score
                this.pathTime[numPath] -= n.getTime(); //Subtract its time
                this.visitedNodes[node] = false; //Mark it as not visited
                this.paths[numPath].remove(toDelete); //Delete the node
                return toDelete;
            }
        }
        return null;
    }

    public void addNodeToPath(int position, TRP_Node n, int numPath) {
        if (isFeasible(numPath, n)) {
            if (this.paths[numPath] == null) { //If the path doesn't exist, create it
                this.paths[numPath] = new ArrayList<>();
                this.paths[numPath].add(n);
            } else {
                if (position == this.numElems - 1) { //If the position is the last possible, add it at the end
                    this.paths[numPath].add(n);
                } else {
                    ListIterator<TRP_Node> it = this.paths[numPath].listIterator(); //Iterator to add the node in the position
                    int i = 0;
                    while (i != position) {
                        it.next();
                        i += 1;
                    }
                    it.add(n); //Add the node in the position
                }
            }
            this.score += n.getScore(); //Add the score to the path
            this.pathTime[numPath] += n.getTime(); //Add the time to the path
            if (n.getNode() != this.numElems - 1 && n.getNode() != 0) {
                this.setVisited(n.getNode());
            }
        }
    }

    public TRP_Instance getInstance() {
        return this.instance;
    }

    public List<TRP_Node>[] getPaths() {
        return this.paths;
    }

    public boolean[] getVisitedNodes() {
        return this.visitedNodes;
    }

    public void setVisited(int node) {
        this.visitedNodes[node] = true;
    }

    public int getNumElems() {
        return this.numElems;
    }

    public int getScore() {
        return this.score;
    }

    public float getMaxTime() {
        return this.maxTime;
    }

    public float[] getPathTime() {
        return this.pathTime;
    }

    public int getNumPaths() {
        return this.numPaths;
    }

    public void setNumPaths(int numPaths) {
        this.numPaths = numPaths;
    }

    public float getMaxPathTime() {
        return maxPathTime;
    }

    public void setMaxPathTime(float maxPathTime) {
        this.maxPathTime = maxPathTime;
    }

}
