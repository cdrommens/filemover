package be.rommens.filemover.file;

import java.io.File;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.FileNameGenerator;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.messaging.MessageHandler;

@Configuration
public class FilePollingIntegrationFlow {

    @Bean
    public IntegrationFlow inboundFileIntegration(
            @Qualifier("sourceDirectory") MessageSource<File> sourceDirectory,
            @Qualifier("targetDirectory") MessageHandler targetDirectory,
            @Value("${delay:5000}") long delay) {
        return IntegrationFlows.from(sourceDirectory,
                c -> c.poller(Pollers.fixedDelay(delay)))
                .filter(source -> ((File) source).getName().endsWith(".cbr") || ((File) source).getName().endsWith(".cbz"))
                .log("in", message -> message.getPayload())
                .handle(targetDirectory)
                .get();
    }

    @Bean(name = "sourceDirectory")
    public MessageSource<File> sourceDirectory(@Value("${source}") String source) {
        FileReadingMessageSource messageSource = new FileReadingMessageSource();
        messageSource.setDirectory(new File(source));
        return messageSource;
    }

    @Bean(name = "targetDirectory")
    public MessageHandler targetDirectory(
            @Value("${source}") String source,
            NumberGenerator numberGenerator) {
        FileWritingMessageHandler handler = new FileWritingMessageHandler(new File(source + "/out"));
        handler.setDeleteSourceFiles(true);
        handler.setFileExistsMode(FileExistsMode.REPLACE);
        handler.setExpectReply(false);
        handler.setFileNameGenerator(processingFileNameGenerator(numberGenerator));
        handler.setLoggingEnabled(true);
        handler.setAutoCreateDirectory(true);
        return handler;
    }

    @Bean
    public FileNameGenerator processingFileNameGenerator(NumberGenerator numberGenerator) {
        return new MyFileNameGenerator(numberGenerator);
    }

    @Bean
    public NumberGenerator numberGenerator(@Value("${lastNumber:}") Integer lastNumber) {
        return new NumberGenerator(lastNumber);
    }

}
