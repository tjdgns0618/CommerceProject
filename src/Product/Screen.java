package Product;

import Exception.LoopEndException;

public interface Screen {
    public String display() throws LoopEndException;
}
