package com.insta2phase.controllers.responses;

public class Vote {

    private CanCommit canCommit;

    private String requestId;

    public Vote() {
    }

    public Vote(CanCommit canCommit, String requestId) {
        setCanCommit(canCommit);
        setRequestId(requestId);
    }

    public CanCommit getCanCommit() {
        return canCommit;
    }

    public void setCanCommit(CanCommit canCommit) {
        this.canCommit = canCommit;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "canCommit=" + canCommit +
                ", requestId='" + requestId + '\'' +
                '}';
    }
}
