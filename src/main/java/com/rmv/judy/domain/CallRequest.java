package com.rmv.judy.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by ehdi on 3/5/17.
 */
public class CallRequest {



    @NotNull
    @Size(min=3,max=20)
    private String sourceNumber;
    @NotNull
    @Size(min=3,max=20)
    private String destinationNumber;

    public String getSourceNumber() {
        return sourceNumber;
    }

    public void setSourceNumber(String sourceNumber) {
        this.sourceNumber = sourceNumber;
    }

    public String getDestinationNumber() {
        return destinationNumber;
    }

    public void setDestinationNumber(String destinationNumber) {
        this.destinationNumber = destinationNumber;
    }

    @Override
    public String toString() {
        return "CallRequest{" +
            "sourceNumber='" + sourceNumber + '\'' +
            ", destinationNumber='" + destinationNumber + '\'' +
            '}';
    }
}
