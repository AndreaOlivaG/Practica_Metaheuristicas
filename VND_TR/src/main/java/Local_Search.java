import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Local_Search {

    private final TRP_Instance instance;
    private final int numElems;
    private final Random rnd;
    private final int origin;
    private final int destination;
    private final List<Integer> pathNumbers = new LinkedList<>();

    public Local_Search(TRP_Solution solution) {
        this.instance = solution.getInstance();
        this.numElems = solution.getNumElems();
        this.rnd = new Random(90);
        this.origin = 0;
        this.destination = this.numElems - 1;
        for (int i = 0; i < solution.getNumPaths(); i++) {
            this.pathNumbers.add(i);
        }
    }

    public void LS1_first(TRP_Solution solution) { //Add a last node to a path before reaching the destination
        TRP_Solution auxSol = new TRP_Solution(solution);
        int bestScore = solution.getScore();
        int newScore, d;
        boolean improve = true;
        boolean improvedInPath;
        float totalTime;
        List<Integer> paths = new LinkedList<>(this.pathNumbers);
        Collections.shuffle(paths, this.rnd); //Shuffle the paths

        while (improve) {
            improve = false;
            for (int numPath : paths) {
                improvedInPath = false;
                TRP_Node deletedNode = auxSol.removeNodeFromPath(this.destination, numPath); //Delete last node
                if (deletedNode == null) continue; //If the path is empty, continue
                for (TRP_City city : instance.getCities()) {
                    d = city.node(); //Destination for the new node
                    if (auxSol.getVisitedNodes()[d])
                        continue; //If the node is already visited or is the deleted node, continue
                    TRP_Node newNode = createNode(deletedNode.getPreviousNode(), d);
                    TRP_Node lastNode = createNode(d, this.destination); //Create the node after the new one, which is the one that was deleted
                    totalTime = newNode.getTime() + lastNode.getTime();
                    if (auxSol.isFeasible(numPath, totalTime)) { //Check if is feasible to add both nodes
                        newScore = auxSol.getScore() + newNode.getScore() + lastNode.getScore();
                        if (newScore > bestScore) {
                            auxSol.addNodeToPath(this.destination, newNode, numPath);
                            auxSol.addNodeToPath(this.destination, lastNode, numPath);
                            bestScore = newScore;
                            solution.copy(auxSol); //Copy the best solution found
                            improve = true;
                            improvedInPath = true;
                            break; //First improvement
                        }
                    }
                }
                if (!improvedInPath) { //If not improved with any city, add the deleted node
                    auxSol.addNodeToPath(this.destination, deletedNode, numPath); //Add the deleted node again
                }
            }
        }
    }

    public void LS2_first(TRP_Solution solution) { //Add a first node to a path after leaving the origin
        TRP_Solution auxSol = new TRP_Solution(solution);
        int bestScore = solution.getScore();
        int newScore, d;
        boolean improve = true;
        boolean improvedInPath;
        float totalTime;
        List<Integer> paths = new LinkedList<>(this.pathNumbers);
        Collections.shuffle(paths, this.rnd); //Shuffle the paths

        while (improve) {
            improve = false;
            for (int numPath : paths) {
                improvedInPath = false;
                int toDelete = auxSol.getPaths()[numPath].get(1).getNode();
                TRP_Node deletedNode = auxSol.removeNodeFromPath(toDelete, numPath); //Delete first node
                if (deletedNode == null) continue; //If the path is empty, continue
                for (TRP_City city : instance.getCities()) {
                    d = city.node(); //Destination for the new node
                    if (auxSol.getVisitedNodes()[d] || d == toDelete)
                        continue; //If the node is already visited or is the deleted node, continue
                    TRP_Node newNode = createNode(this.origin, d);
                    TRP_Node nextNode = createNode(d, toDelete);
                    totalTime = newNode.getTime() + nextNode.getTime();
                    if (auxSol.isFeasible(numPath, totalTime)) { //Check if is feasible to add the two nodes
                        newScore = auxSol.getScore() + newNode.getScore() + nextNode.getScore();
                        if (newScore > bestScore) {
                            auxSol.addNodeToPath(1, nextNode, numPath);
                            auxSol.addNodeToPath(1, newNode, numPath);
                            bestScore = newScore;
                            solution.copy(auxSol); //Update the best solution found
                            improve = true;
                            improvedInPath = true;
                            break; //First improvement
                        }
                    }
                }
                if (!improvedInPath) { //If not improved with any city, add the deleted node
                    auxSol.addNodeToPath(1, deletedNode, numPath); //Add deleted node again
                }
            }
        }
    }

    public void LS3_first(TRP_Solution solution) {
        TRP_Solution auxSol = new TRP_Solution(solution);
        int bestScore = solution.getScore();
        boolean improve = true;
        int maxIterations = Math.min(this.numElems, 60);
        int newScore, toDelete, o, d, node_index, city_index;
        float distance, totalTime;
        TRP_Node firstNode, nextNode;
        List<Integer> paths = new LinkedList<>(this.pathNumbers);
        Collections.shuffle(paths, this.rnd); //Shuffle the paths

        while (improve) {
            improve = false;
            for (int numPath : paths) {
                int numIterations = 0;
                while (numIterations < maxIterations) {
                    numIterations += 1;
                    node_index = 1 + rnd.nextInt(auxSol.getPaths()[numPath].size() - 2); //Get a random node from the path as origin
                    city_index = 1 + rnd.nextInt(this.instance.getCities().size() - 2); //Get a new random city as destination
                    o = auxSol.getPaths()[numPath].get(node_index).getNode();
                    d = this.instance.getCities().get(city_index).node(); //Get the city as destination
                    //Check if the destination is already visited
                    if (auxSol.getVisitedNodes()[d])
                        continue;
                    firstNode = createNode(o, d);
                    toDelete = auxSol.getPaths()[numPath].get(node_index + 1).getNode();
                    distance = instance.getDistances()[d][toDelete]; //Get the distance between the new node and the next one
                    totalTime = firstNode.getTime() + distance;
                    //Check if is feasible to add the new node and then go to the next node
                    if (!auxSol.isFeasible(numPath, totalTime))
                        continue;
                    nextNode = createNode(d, toDelete);
                    newScore = auxSol.getScore() + firstNode.getScore();
                    //If the new score is better, update the solution
                    if (newScore > bestScore) {
                        auxSol.removeNodeFromPath(toDelete, numPath); //Remove the next node from the path to update it
                        auxSol.addNodeToPath(node_index + 1, firstNode, numPath);
                        auxSol.addNodeToPath(node_index + 2, nextNode, numPath);
                        bestScore = newScore;
                        solution.copy(auxSol); //Copy the best solution found
                        improve = true;
                        break; //First improvement
                    }
                }
            }
        }
    }

    public void LS1_best(TRP_Solution solution) { //Add a last node to a path before reaching the destination
        TRP_Solution auxSol = new TRP_Solution(solution);
        int bestScore = solution.getScore();
        int newScore, d;
        boolean improve = true;
        boolean improvedInPath;
        float totalTime;
        List<Integer> paths = new LinkedList<>(this.pathNumbers);
        Collections.shuffle(paths, this.rnd); //Shuffle the paths

        while (improve) {
            improve = false;
            for (int numPath : paths) {
                TRP_Node bestNode = null;
                improvedInPath = false;
                TRP_Node deletedNode = auxSol.removeNodeFromPath(this.destination, numPath); //Delete last node
                if (deletedNode == null) //Delete last node, then it will be added it again
                    continue; //If the path is empty, continue
                for (TRP_City city : instance.getCities()) {
                    d = city.node(); //Destination for the new node
                    if (auxSol.getVisitedNodes()[d])
                        continue; //If the node is already visited or is the deleted node, continue
                    TRP_Node newNode = createNode(deletedNode.getPreviousNode(), d);
                    totalTime = newNode.getTime() + instance.getDistances()[d][this.destination];
                    if (auxSol.isFeasible(numPath, totalTime)) { //Check if is feasible to add the node and then go to the destination
                        newScore = auxSol.getScore() + newNode.getScore();
                        if (newScore > bestScore) {
                            bestNode = newNode;
                            bestScore = newScore;
                            improve = true;
                            improvedInPath = true;
                        }
                    }
                }
                if (improvedInPath) {
                    auxSol.addNodeToPath(this.destination, bestNode, numPath);
                    TRP_Node lastNode = createNode(bestNode.getNode(), this.destination);
                    auxSol.addNodeToPath(this.destination, lastNode, numPath);
                } else {
                    auxSol.addNodeToPath(this.destination, deletedNode, numPath);
                }
            }
            if (improve) {
                solution.copy(auxSol);
            }
        }
    }

    public void LS2_best(TRP_Solution solution) { //Add a first node to a path after leaving the origin
        TRP_Solution auxSol = new TRP_Solution(solution);
        int bestScore = solution.getScore();
        int newScore, d;
        boolean improve = true;
        boolean improvedInPath;
        float totalTime;
        List<Integer> paths = new LinkedList<>(this.pathNumbers);
        Collections.shuffle(paths, this.rnd); //Shuffle the paths

        while (improve) {
            improve = false;
            for (int numPath : paths) {
                TRP_Node bestNode = null;
                improvedInPath = false;
                int toDelete = auxSol.getPaths()[numPath].get(1).getNode();
                TRP_Node deletedNode = auxSol.removeNodeFromPath(toDelete, numPath); //Delete first node
                if (deletedNode == null) //Delete last node, then it will be added it again
                    continue; //If the path is empty, continue
                for (TRP_City city : instance.getCities()) {
                    d = city.node(); //Destination for the new node
                    if (auxSol.getVisitedNodes()[d] || d == toDelete)
                        continue; //If the node is already visited or is the deleted node, continue
                    TRP_Node newNode = createNode(this.origin, d);
                    TRP_Node nextNode = createNode(d, toDelete); //Create the node after the new one, which is the one that was deleted
                    totalTime = newNode.getTime() + nextNode.getTime();
                    if (auxSol.isFeasible(numPath, totalTime)) { //Check if is feasible to add the two nodes
                        newScore = auxSol.getScore() + newNode.getScore() + nextNode.getScore();
                        if (newScore > bestScore) {
                            bestNode = newNode;
                            bestScore = newScore;
                            improve = true;
                            improvedInPath = true;
                        }
                    }
                }
                if (improvedInPath) {
                    TRP_Node nextNode = createNode(bestNode.getNode(), toDelete);
                    auxSol.addNodeToPath(1, nextNode, numPath);
                    auxSol.addNodeToPath(1, bestNode, numPath);
                } else {
                    auxSol.addNodeToPath(1, deletedNode, numPath);
                }
            }
            if (improve) {
                solution.copy(auxSol);
            }
        }
    }

    public TRP_Node createNode(int o, int d) {
        return new TRP_Node(o, d, instance.getScores()[d], instance.getDistances()[o][d]);
    }


}
