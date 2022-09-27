package aws;

import io.micronaut.context.env.Environment;
import io.micronaut.runtime.Micronaut;

public class Application {
    public static void main(String[] args) {
        Micronaut.build(args)
                .mainClass(Application.class)
                .environments(Environment.AMAZON_EC2)
                .start();
    }
}
