public record TRP_City(int node, int score) implements Comparable<TRP_City> {

    @Override
    public int compareTo(TRP_City c) {
        return -(this.score() - c.score());
    }

}
