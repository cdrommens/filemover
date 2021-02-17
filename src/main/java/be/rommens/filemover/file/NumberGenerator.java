package be.rommens.filemover.file;

public class NumberGenerator {

    private int number = 0;

    public int getNext() {
        this.number++;
        return this.number;
    }

}
