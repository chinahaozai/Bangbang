package halewang.com.bangbang.model;

/**
 * Created by p_whaohwang on 2017/4/1.
 */

public class MessageEvent {
    private String message;

    public MessageEvent(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
