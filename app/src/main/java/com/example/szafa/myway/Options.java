package com.example.szafa.myway;

import java.io.Serializable;

public class Options implements Serializable {
    private TransportMean transportMean;
    private boolean startCurrent;
    private boolean stopCurrent;

    public Options(TransportMean transportMean, boolean startCurrent, boolean stopCurrent) {
        this.transportMean = transportMean;
        this.startCurrent = startCurrent;
        this.stopCurrent = stopCurrent;
    }

    public TransportMean getTransportMean() {
        return transportMean;
    }

    public void setTransportMean(TransportMean transportMean) {
        this.transportMean = transportMean;
    }

    public boolean isStartCurrent() {
        return startCurrent;
    }

    public void setStartCurrent(boolean startCurrent) {
        this.startCurrent = startCurrent;
    }

    public boolean isStopCurrent() {
        return stopCurrent;
    }

    public void setStopCurrent(boolean stopCurrent) {
        this.stopCurrent = stopCurrent;
    }
}
