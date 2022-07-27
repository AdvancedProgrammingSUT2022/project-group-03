package model;

import java.util.HashMap;

public class Payload {
    private final String header;
    private final HashMap<String, Object> body = new HashMap<>();

    public Payload(String header) {
        this.header = header;
    }

    public void add(String key, Object value) {
        body.put(key, value);
    }

    public Object get(String key) {
        return body.get(key);
    }

    public String getHeader() {
        return header;
    }
}
