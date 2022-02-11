package com.amazon.device.ads;

import java.util.HashMap;

class ParameterMap {
    private final HashMap<String, Object> parameters;

    ParameterMap() {
        this.parameters = new HashMap();
    }

    public void setParameter(String key, Object value) {
        this.parameters.put(key, value);
    }

    public Object getParameter(String key) {
        return this.parameters.get(key);
    }

    public String getStringParameter(String key) {
        return (String) this.parameters.get(key);
    }

    public Boolean getBooleanParameter(String key) {
        return (Boolean) this.parameters.get(key);
    }

    public Integer getIntParameter(String key) {
        return (Integer) this.parameters.get(key);
    }

    public Double getDoubleParameter(String key) {
        return (Double) this.parameters.get(key);
    }

    public Long getLongParameter(String key) {
        return (Long) this.parameters.get(key);
    }
}
