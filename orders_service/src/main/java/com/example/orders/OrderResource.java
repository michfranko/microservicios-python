package com.example.orders;

import io.smallrye.common.annotation.Blocking;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import java.util.Map;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    private static final Logger LOG = Logger.getLogger(OrderResource.class);

    @Inject
    @RestClient
    ShippingServiceClient shippingClient;

    @Inject
    @RestClient
    ProductServiceClient productClient;

    @POST
    @Blocking
    @Transactional
    public Response createOrder(OrderRequest request) {

        LOG.info("Received order request: productId=" + request.getProductId() + ", destination=" + request.getDestination());

        // A. Validación
        if (request.getProductId() <= 0 || request.getDestination() == null || request.getDestination().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of(
                    "error", "INVALID_ORDER",
                    "message", "Invalid order"
            )).build();
        }

        // B. Shipping
        ShippingResponse shippingResp;
        try {
            ShippingRequest shippingReq = new ShippingRequest(request.getProductId(), request.getDestination());

            shippingResp = shippingClient.calculate(shippingReq);
        } catch (Exception e) {
            LOG.error("Error calling shipping service", e);
            return Response.status(Response.Status.BAD_GATEWAY).entity(Map.of(
                    "error", "SHIPPING_SERVICE_UNAVAILABLE",
                    "message", "Shipping service unavailable"
            )).build();
        }

        // C. Stock
        Response stockResponse;
        try {
            stockResponse = productClient.deductStock(request.getProductId(), 1);
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_GATEWAY).entity(Map.of(
                    "error", "PRODUCT_SERVICE_UNAVAILABLE",
                    "message", "Product service unavailable"
            )).build();
        }

        if (stockResponse.getStatus() != 200) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of(
                    "error", "INSUFFICIENT_STOCK",
                    "message", "Insufficient stock"
            )).build();
        }

        // D. Persistencia
        Order order = new Order();
        order.productIds = String.valueOf(request.getProductId());
        order.destination = request.getDestination();
        order.shippingCost = shippingResp.getCost();
        order.total = shippingResp.getCost() + 10.0;

        order.persist();

        return Response.status(Response.Status.CREATED).entity(order).build();
    }
}
