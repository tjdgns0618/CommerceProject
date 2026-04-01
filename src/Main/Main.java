package Main;

import Exception.LoopEndException;
import Commerce.CommerceSystem;

public class Main {
    public static void main(String[] args) throws LoopEndException {
        CommerceSystem commerceSystem = new CommerceSystem();

        commerceSystem.start();
    }
}
