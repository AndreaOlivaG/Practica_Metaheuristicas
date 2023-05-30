public class TRP_Node {

    private final int node;
    private final int score;
    private final int previousNode;
    private final float time;

    public TRP_Node(int previousNode, int node, int score, float time) {
        this.previousNode = previousNode;
        this.node = node;
        this.score = score;
        this.time = time;
    }

    public int getPreviousNode() {
        return previousNode;
    }

    public int getNode() {
        return this.node;
    }

    public int getScore() {
        return this.score;
    }

    public float getTime() {
        return this.time;
    }

}
