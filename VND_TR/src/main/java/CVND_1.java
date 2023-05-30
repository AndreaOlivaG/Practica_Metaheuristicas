public class CVND_1 {

    public void improve(TRP_Solution solution) {
        if (solution.getNumPaths() == 0) return;
        TRP_Solution best_solution = new TRP_Solution(solution);
        Local_Search ls = new Local_Search(solution);

        boolean improved = true;
        while (improved) {
            solution.copy(best_solution);
            improved = false;
            for (int k = 0; k < 3; k++) {
                if (k == 0) {
                    ls.LS3_first(solution);
                } else if (k == 1) {
                    ls.LS2_first(solution);
                } else {
                    ls.LS1_first(solution);
                }
                if (solution.getScore() > best_solution.getScore()) {
                    best_solution.copy(solution); //Update the best solution
                    improved = true;
                }
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
