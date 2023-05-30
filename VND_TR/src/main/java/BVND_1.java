public class BVND_1 {

    public void improve(TRP_Solution solution) {
        if (solution.getNumPaths() == 0) return;
        TRP_Solution best_solution = new TRP_Solution(solution);
        Local_Search ls = new Local_Search(solution);

        int k = 0;
        int k_max = 3;
        while (k < k_max) {
            solution.copy(best_solution);
            if (k == 0) {
                ls.LS3_first(solution);
            } else if (k == 1) {
                ls.LS2_first(solution);
            } else {
                ls.LS1_first(solution);
            }
            if (solution.getScore() > best_solution.getScore()) {
                best_solution.copy(solution); //Update the best solution
                if (k == 0) {
                    k = 1; //If improved in the first, go to the second
                } else {
                    k = 0; //If improved in the second, go to the first
                }
            } else {
                k += 1; //If it didn't improve, change the neighborhood
            }
        }
        solution.copy(best_solution);

        for (float time : solution.getPathTime()) {
            if (time > solution.getMaxPathTime()) {
                solution.setMaxPathTime(time);
            }
        }
    }

}
