package com.epam.bookshop.command;

import com.epam.bookshop.constant.UtilStringConstants;

/**
 * Wrapper of result, returning by {@link Command} implementations
 */
public class CommandResult {
    public enum ResponseType {
        FORWARD,
        REDIRECT,
        JSON,
        TEXT_PLAIN,
        NO_ACTION
    }

    private ResponseType responseType;
    private String resp;

    public CommandResult(ResponseType responseType, String resp) {
        this.responseType = responseType;
        this.resp = resp;
    }

    public ResponseType getResponseType() {
        return responseType;
    }

    public void setResponseType(ResponseType responseType) {
        if (responseType == null) {
            responseType = ResponseType.NO_ACTION;
        }
        this.responseType = responseType;
    }

    public String getResp() {
        return resp;
    }

    public void setResp(String resp) {
        if (resp == null) {
            resp = UtilStringConstants.EMPTY_STRING;
        }
        this.resp = resp;
    }
}
