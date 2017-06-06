package com.rmv.judy.service;


import com.rmv.judy.domain.CallRequest;
import org.apache.log4j.Logger;
import org.asteriskjava.fastagi.AgiServerThread;
import org.asteriskjava.fastagi.DefaultAgiServer;
import org.asteriskjava.live.DefaultAsteriskServer;
import org.asteriskjava.manager.*;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.response.ManagerResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by ehdi on 3/5/17.
 */
@Service
@Configuration
public class AsteriskManager {

    private Logger log = Logger.getLogger(AsteriskManager.class);
    private ManagerConnection managerConnection;
    private DefaultAsteriskServer asteriskServer;

    @Value("${asterisk.host}")
    private String host;

    @Value("${asterisk.port}")
    private int port;

    @Value("${asterisk.username}")
    private String username;

    @Value("${asterisk.password}")
    private String password;

    @Value("${asterisk.timeout}")
    private long asteriskTimeout;

    private DefaultAgiServer agiServer;

    private static AgiServerThread agiServerThread;


    @PostConstruct
    public void start() {
        log.info("Starting AsteriskManager ...");
        try {
            ManagerConnectionFactory factory = new
                ManagerConnectionFactory(host, port, username, password);
            managerConnection = factory.createManagerConnection();
            asteriskServer = new DefaultAsteriskServer(managerConnection);
            asteriskServer.initialize();
            log.info("Created connection to the Asterisk server, connection status: " + managerConnection.getState());

            log.info("Start the Agi server for Asterisk");
            agiServer = new DefaultAgiServer();
            agiServerThread = new AgiServerThread(agiServer);
            agiServerThread.startup();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public ManagerResponse callOriginator(CallRequest callRequest)
        throws IOException,  AuthenticationFailedException, TimeoutException {

        OriginateAction originateAction = new OriginateAction();
        originateAction.setChannel("SIP/trunk/8654"+callRequest.getSourceNumber());
        originateAction.setContext("judy");
        originateAction.setExten(callRequest.getDestinationNumber());
        originateAction.setCallerId("2196669081");
        originateAction.setPriority(1);
        originateAction.setActionId(UUID.randomUUID().toString());

        ManagerResponse originateResponse = managerConnection.sendAction(originateAction,30000);
        log.info(originateResponse.getResponse());
        return originateResponse;
    }

    @PreDestroy
    public void stop() throws Exception {
        if (managerConnection.getState().equals(ManagerConnectionState.CONNECTED) || managerConnection.getState().equals(ManagerConnectionState.RECONNECTING)) {
            managerConnection.logoff();
        }
        asteriskServer.shutdown();
    }


}
