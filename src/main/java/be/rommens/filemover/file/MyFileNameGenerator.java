package be.rommens.filemover.file;

import org.springframework.integration.file.DefaultFileNameGenerator;
import org.springframework.messaging.Message;

public class MyFileNameGenerator extends DefaultFileNameGenerator {

    private final NumberGenerator numberGenerator;

    public MyFileNameGenerator(NumberGenerator numberGenerator) {
        this.numberGenerator = numberGenerator;
    }

    @Override
    public String generateFileName(Message<?> message) {
        String filename = super.generateFileName(message);
        return String.format("%02d", numberGenerator.getNext()) + "-" + filename;
    }
}
