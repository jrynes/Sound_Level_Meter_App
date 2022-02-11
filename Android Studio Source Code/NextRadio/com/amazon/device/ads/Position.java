package com.amazon.device.ads;

import org.json.JSONObject;

class Position {
    private Size size;
    private int f1063x;
    private int f1064y;

    public Position() {
        this.size = new Size(0, 0);
        this.f1063x = 0;
        this.f1064y = 0;
    }

    public Position(Size size, int x, int y) {
        this.size = size;
        this.f1063x = x;
        this.f1064y = y;
    }

    public Size getSize() {
        return this.size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public int getX() {
        return this.f1063x;
    }

    public void setX(int x) {
        this.f1063x = x;
    }

    public int getY() {
        return this.f1064y;
    }

    public void setY(int y) {
        this.f1064y = y;
    }

    public JSONObject toJSONObject() {
        JSONObject json = this.size.toJSONObject();
        JSONUtils.put(json, "x", this.f1063x);
        JSONUtils.put(json, "y", this.f1064y);
        return json;
    }
}
