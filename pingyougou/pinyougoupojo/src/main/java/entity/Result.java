package entity;

import java.io.Serializable;

public class Result implements Serializable {
    private Boolean success;
    private String messages;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public Result(Boolean success, String messages) {
        this.success = success;
        this.messages = messages;
    }
}
