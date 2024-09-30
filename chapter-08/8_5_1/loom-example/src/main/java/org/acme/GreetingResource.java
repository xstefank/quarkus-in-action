package org.acme;

import io.quarkus.logging.Log;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class GreetingResource {

    // Invoked at :8080/hello/virtualThread
    @GET
    @Path("/virtualThread")
    @RunOnVirtualThread
    public void virtualThread() {
        Log.info("Running on " + Thread.currentThread().getName());
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from Quarkus REST";
    }
}
