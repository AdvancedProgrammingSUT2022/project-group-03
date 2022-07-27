package model;

import java.io.Serializable;
import java.util.Calendar;


public class Message implements Serializable {
    private User user;
    private final String sender;
    private String content;
    private boolean sent;
    private boolean seen;
    private transient boolean isSelected;
    private Calendar calendar;

    public Message(User user, String content) {
        this.user = user;
        this.sender = user.getUsername();
        this.content = content;
        calendar = Calendar.getInstance();
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void toggleSelected() {
        isSelected = !isSelected;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public User getUser() {
        return user;
    }
}
