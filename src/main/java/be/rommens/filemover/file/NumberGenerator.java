package be.rommens.filemover.file;

public class NumberGenerator {

    private int number = 0;

    public NumberGenerator(Integer lastNumber) {
        if (lastNumber != null && lastNumber > 0) {
            this.number = lastNumber;
        }
    }

    public int getNext() {
        this.number++;
        return this.number;
    }

}
