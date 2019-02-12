package com.insta2phase.controllers.responses;

public class CommitResponse {
    private String requestId;
    private boolean succeed;

    public CommitResponse() {
    }

    public CommitResponse(String requestId, boolean succeed) {
        setRequestId(requestId);
        setSucceed(succeed);
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public boolean isSucceed() {
        return succeed;
    }

    public void setSucceed(boolean succeed) {
        this.succeed = succeed;
    }
}
