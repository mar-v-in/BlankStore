package com.google.tools;

import com.google.android.AndroidContext;

import java.util.*;
import java.util.regex.Pattern;

public class RequestContext {
    public static final Pattern FALSE_PATTERN = Pattern.compile("^(0|false|f|off|no|n)$", Pattern.CASE_INSENSITIVE);
    public static final Pattern TRUE_PATTERN = Pattern.compile("^(1|true|t|on|yes|y)$", Pattern.CASE_INSENSITIVE);
    private final HashMap<String, Object> data;

    public RequestContext() {
        data = new HashMap<String, Object>();
    }

    public RequestContext(Map<? extends String, ?> m) {
        data = new HashMap<String, Object>(m);
    }

    public RequestContext(RequestContext ctx) {
        this(ctx.data);
    }

    public Long getLong(String key) {
        try {
            return Long.parseLong(getString(key));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public String getString(Object key) {
        return (String) data.get(key);
    }

    public Integer getInt(String key) {
        try {
            return Integer.parseInt(getString(key));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public <T extends RequestContext> T set(String key, int value) {
        return set(key, Integer.toString(value));
    }

    public <T extends RequestContext> T set(String key, Long value) {


        if (value == null) {
            data.remove(key);
            return (T) this;
        }
        return set(key, Long.toString(value));
    }

    public <T extends RequestContext> T set(String key, long value) {
        return set(key, Long.toString(value));
    }

    public <T extends RequestContext> T set(String key, boolean bool) {
        return set(key, bool ? 1 : 0);
    }

    public <T> T get(String key, T defaultValue) {
        if (data.containsKey(key) && data.get(key) != null) {
            try {
                return (T) data.get(key);
            } catch (ClassCastException ignored) {
            }
        }
        return defaultValue;
    }

    public <T extends RequestContext> T set(String key, Object value) {
        if (value != null && !(value instanceof String) && !(value instanceof Collection)) {
            System.out.println("Trying to store value of class " + value.getClass());
        } else {
            data.put(key, value);
        }
        return (T) this;
    }

    public Boolean getBoolean(String key, boolean defaultValue) {
        String string = getString(key);
        boolean res = defaultValue;
        if ((string != null) && !string.isEmpty()) {
            if (TRUE_PATTERN.matcher(string).matches()) {
                res = true;
            } else if (FALSE_PATTERN.matcher(string).matches()) {
                res = false;
            }
        }
        return res;
    }

    public int getInt(String key, int defaultValue) {
        try {
            return getInt(key);
        } catch (Exception ignored) {
            return defaultValue;
        }
    }

    public long getLong(String key, long defaultValue) {
        try {
            return getLong(key);
        } catch (Exception ignored) {
            return defaultValue;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("RequestContext{\r\n");
        ArrayList<String> strings = new ArrayList<String>(data.keySet());
        Collections.sort(strings);
        for (String key : strings) {
            sb.append("\t").append(key).append("=").append(data.get(key)).append("\r\n");
        }
        return sb.append("}").toString();
    }
}
