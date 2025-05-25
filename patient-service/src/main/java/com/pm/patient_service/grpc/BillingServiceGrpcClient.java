package com.pm.patient_service.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BillingServiceGrpcClient {

    private final BillingServiceGrpc.BillingServiceBlockingStub blockingStub;

    private static final Logger LOGGER = LoggerFactory.getLogger(BillingServiceGrpcClient.class);

    public BillingServiceGrpcClient(
            @Value("${billing.service.address:localhost}") String serverAddress,
            @Value("${billing.service.grpc.port:9081}") int serverPort ) {

        LOGGER.info("Connecting to Billing Service GRPC service at {}:{}",serverAddress,serverPort);

        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(serverAddress,serverPort).usePlaintext().build();
        blockingStub = BillingServiceGrpc.newBlockingStub(managedChannel);
    }

    public BillingResponse createBillingAccount(String patientID, String name, String email){

        BillingRequest request = BillingRequest.newBuilder()
                .setPatientId(patientID)
                .setName(name)
                .setEmail(email)
                .build();

        BillingResponse response = blockingStub.createBillingAccount(request);
        LOGGER.info("Received response from billing service via GRPC: {}",response);
        return response;
    }
}
