package org.acme;

import io.smallrye.mutiny.Multi;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class GreetingResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {

        // The example in section 8.2.2
        Multi.createFrom().items("a", "b", "c")
            .onItem()
                .transform(String::toUpperCase)
            .onItem()
                .invoke(s -> System.out.println("Intermediate stage " + s))
            .onItem()
                .transform(s -> s + " item")
            .filter(s -> !s.startsWith("B"))
            .onCompletion()
                .invoke(() -> System.out.println("Stream completed"))
            .subscribe()
                .with(s -> System.out.println("Subscriber received " + s));


        return "Hello from Quarkus REST";
    }
}
