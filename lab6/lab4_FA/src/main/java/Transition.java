public class Transition {
    private String state1;
    private String state2;
    private String route;

    public Transition(String state1, String state2, String route) {
        this.state1 = state1;
        this.state2 = state2;
        this.route = route;
    }

    public String getState1() {
        return state1;
    }

    public void setState1(String state1) {
        this.state1 = state1;
    }

    public String getState2() {
        return state2;
    }

    public void setState2(String state2) {
        this.state2 = state2;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    @Override
    public String toString() {
        return "d(" + state1 + ", " + route + ") = " + state2;
    }
}
