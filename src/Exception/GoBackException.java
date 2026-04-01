package Exception;

public class GoBackException extends RuntimeException {
    private final String nextScreen;

    public GoBackException(String nextScreen) {
        this.nextScreen = nextScreen;
    }
    public String getNextScreen() {
        return nextScreen;
    }
}
