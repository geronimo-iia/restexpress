package org.restexpress.plugin.jaxrs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/**
 * {@link EchoService} implmentation.
 * 
 */
@Path("/")
public class EchoService {

    public EchoService() {
    }

    @GET
    public String read(@QueryParam("echo") String echo) {
        if (echo == null) {
            return "Please set query-string parameter 'echo' (e.g. ?echo=value)";
        }
        return echo;
    }
}
