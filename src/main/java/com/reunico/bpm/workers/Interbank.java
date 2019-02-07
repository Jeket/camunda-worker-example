package com.reunico.bpm.workers;

import java.util.logging.Logger;

import org.camunda.bpm.client.ExternalTaskClient;

public class Interbank {
    private final static Logger LOGGER = Logger.getLogger(Interbank.class.getName());

    public static void main(String[] args) {
        ExternalTaskClient client = ExternalTaskClient.create()
                .baseUrl("https://demo2.reunico.com/engine-rest")
                .asyncResponseTimeout(10000) // long polling timeout
                .build();

        // subscribe to an external task topic as specified in the process
        client.subscribe("interbank")
                .lockDuration(1000) // the default lock duration is 20 seconds, but you can override this
                .handler((externalTask, externalTaskService) -> {
                    // Put your business logic here

                    // Get a process variable
                    String details = (String) externalTask.getVariable("details");
                            Long amount = (Long) externalTask.getVariable("amount");
                    LOGGER.info("Transaction details '" + details + ". Amount '" + amount + "’.");

                    // Complete the task
                    externalTaskService.complete(externalTask);
                })
                .open();
    }
}