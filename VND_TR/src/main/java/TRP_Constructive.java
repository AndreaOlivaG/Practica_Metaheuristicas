public class TRP_Constructive {

    public TRP_Constructive() {
    }

    public TRP_Solution constructSolution(TRP_Instance instance) {
        TRP_Solution solution = new TRP_Solution(instance);
        TRP_Node node;
        int o, d;

        for (int numPath = 0; numPath < instance.getNumPaths(); numPath++) {
            o = 0;
            for (TRP_City city : instance.getCities()) {
                d = city.node();
                if (solution.getVisitedNodes()[d] || d == 0 || d == instance.getNumElems() - 1) {
                    continue;
                }
                node = new TRP_Node(o, d, instance.getScores()[d], instance.getDistances()[o][d]);
                float totalTime = node.getTime() + instance.getDistances()[d][instance.getNumElems() - 1];
                if (solution.isFeasible(numPath, totalTime)) { //Check if the solution is feasible
                    solution.addNodeToPath(0, new TRP_Node(-1, 0, instance.getScores()[0], 0), numPath);
                    solution.addNodeToPath(instance.getNumElems() - 1, node, numPath); //Add the node to the path
                    o = d;
                    d = instance.getNumElems() - 1;
                    node = new TRP_Node(o, d, instance.getScores()[d], instance.getDistances()[o][d]); //Add the node to the path
                    solution.addNodeToPath(instance.getNumElems() - 1, node, numPath);
                    solution.setNumPaths(solution.getNumPaths() + 1);
                    break;
                }
            }
        }
        return solution;
    }

}
