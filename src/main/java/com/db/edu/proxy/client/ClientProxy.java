package com.db.edu.proxy.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientProxy {
    public static void main(String[] args) {
        final Logger logger = LoggerFactory.getLogger(ClientProxy.class);

        try {
            Client client = new Client();
            client.run();
        } catch (Exception e) {
            System.out.println("Oops, something wrong has occurred\nIf you want to see more, please see logs\n");
            logger.error("Exception occurred while starting a client\n", e);
        }
    }
}
