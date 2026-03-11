package org.example.models;

import com.google.gson.annotations.SerializedName;

/**
 * Logout Response Model
 */
public class LogoutResponse {

    @SerializedName("success")
    private Boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private Object data;

    /**
     * Default constructor
     */
    public LogoutResponse() {
    }

    /**
     * Constructor with success flag and message
     *
     * @param success Success indicator
     * @param message Response message
     */
    public LogoutResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // Getters and Setters

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "LogoutResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}

