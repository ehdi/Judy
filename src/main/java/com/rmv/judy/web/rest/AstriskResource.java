package com.rmv.judy.web.rest;

import com.rmv.judy.domain.CallRequest;
import com.rmv.judy.security.AuthoritiesConstants;
import com.rmv.judy.service.AsteriskManager;
import com.rmv.judy.web.rest.util.HeaderUtil;
import org.apache.log4j.Logger;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.response.ManagerResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


@RestController
@RequestMapping("/api")
public class AstriskResource {

    private Logger log = Logger.getLogger(AstriskResource.class);

    @Inject
    private AsteriskManager asteriskManager;

    @PostMapping("/call-originator")
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<?> callOriginator(@RequestBody @Valid CallRequest callRequest)
        throws URISyntaxException, IOException, AuthenticationFailedException {

        log.debug("REST request to call Asterisk: " + callRequest.toString());

        try {
            ManagerResponse response = asteriskManager.callOriginator(callRequest);

            if(response.getResponse().equals("Success"))
                return ResponseEntity.ok("Call request has been sent");
            else
                return ResponseEntity.badRequest().body(response.getMessage());

        } catch (TimeoutException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

}
