package com.pm.billing_service.grpc;

import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class BillingGrpcService extends BillingServiceGrpc.BillingServiceImplBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(BillingGrpcService.class);

    // using gRPC we can send multiple response's for one particular request which is not possible in IRestClient or IWebClient
    @Override
    public void createBillingAccount(billing.BillingRequest billingRequest,
                                     StreamObserver<billing.BillingResponse> responseStreamObserver){

        LOGGER.info("createBillingAccount request received {}", billingRequest.toString());

        // Business logic - eg save to database, perform calculations etc

        BillingResponse billingResponse = BillingResponse.newBuilder()
                .setAccountId("12345")
                .setStatus("ACTIVE")
                .build();

        // helpful to send back response from gRPC to client
        responseStreamObserver.onNext(billingResponse);

        // response is completed
        responseStreamObserver.onCompleted();
    }
}
