package com.example.orders;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "product-service")
@Path("/products")
public interface ProductServiceClient {

    @POST
    @Path("/{id}/deduct-stock")
    Response deductStock(
        @PathParam("id") int id,
        @QueryParam("quantity") int quantity
    );
}
