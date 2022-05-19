package amw.ecb.ecbproducer;

import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;


@SpringBootApplication
public class XmlParserApp implements CommandLineRunner {

    private XMLService xmlService;

    private Currency currency;

    private final KafkaTemplate<String, Currency> kafkaTemplate;

    public XmlParserApp(XMLService xmlService, KafkaTemplate<String, Currency> kafkaTemplate){
        this.xmlService = xmlService;
        this.kafkaTemplate = kafkaTemplate;
    }

    public static void main(String[] args){
        SpringApplication app = new SpringApplication(XmlParserApp.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }


    @Override
    public void run(String... args) throws Exception {

        List<Currency> currencies = xmlService.parseCurrency();

        for(int i = 0; i < currencies.size() ; i++) {
            currency = currencies.get(i);
            ListenableFuture<SendResult<String, Currency>> future =
                    kafkaTemplate.send("currenciesTopic", currency);

            future.addCallback(new ListenableFutureCallback<SendResult<String, Currency>>() {

                @Override
                public void onSuccess(SendResult<String, Currency> result) {
                    System.out.println("Message sent with offset=[" + result.getRecordMetadata().offset() + "]");
                }
                @Override
                public void onFailure(Throwable ex) {
                    System.out.println("Unable to send message due to : " + ex.getMessage());
                }
            });
        }
    }
}
