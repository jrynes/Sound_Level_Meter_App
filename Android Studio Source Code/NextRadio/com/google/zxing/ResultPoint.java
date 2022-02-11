package com.google.zxing;

import org.apache.activemq.command.ActiveMQDestination;

public class ResultPoint {
    private final float f2072x;
    private final float f2073y;

    public ResultPoint(float x, float y) {
        this.f2072x = x;
        this.f2073y = y;
    }

    public final float getX() {
        return this.f2072x;
    }

    public final float getY() {
        return this.f2073y;
    }

    public boolean equals(Object other) {
        if (!(other instanceof ResultPoint)) {
            return false;
        }
        ResultPoint otherPoint = (ResultPoint) other;
        if (this.f2072x == otherPoint.f2072x && this.f2073y == otherPoint.f2073y) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (Float.floatToIntBits(this.f2072x) * 31) + Float.floatToIntBits(this.f2073y);
    }

    public String toString() {
        StringBuilder result = new StringBuilder(25);
        result.append('(');
        result.append(this.f2072x);
        result.append(ActiveMQDestination.COMPOSITE_SEPERATOR);
        result.append(this.f2073y);
        result.append(')');
        return result.toString();
    }

    public static void orderBestPatterns(ResultPoint[] patterns) {
        ResultPoint pointB;
        ResultPoint pointA;
        ResultPoint pointC;
        float zeroOneDistance = distance(patterns[0], patterns[1]);
        float oneTwoDistance = distance(patterns[1], patterns[2]);
        float zeroTwoDistance = distance(patterns[0], patterns[2]);
        if (oneTwoDistance >= zeroOneDistance && oneTwoDistance >= zeroTwoDistance) {
            pointB = patterns[0];
            pointA = patterns[1];
            pointC = patterns[2];
        } else if (zeroTwoDistance < oneTwoDistance || zeroTwoDistance < zeroOneDistance) {
            pointB = patterns[2];
            pointA = patterns[0];
            pointC = patterns[1];
        } else {
            pointB = patterns[1];
            pointA = patterns[0];
            pointC = patterns[2];
        }
        if (crossProductZ(pointA, pointB, pointC) < 0.0f) {
            ResultPoint temp = pointA;
            pointA = pointC;
            pointC = temp;
        }
        patterns[0] = pointA;
        patterns[1] = pointB;
        patterns[2] = pointC;
    }

    public static float distance(ResultPoint pattern1, ResultPoint pattern2) {
        float xDiff = pattern1.f2072x - pattern2.f2072x;
        float yDiff = pattern1.f2073y - pattern2.f2073y;
        return (float) Math.sqrt((double) ((xDiff * xDiff) + (yDiff * yDiff)));
    }

    private static float crossProductZ(ResultPoint pointA, ResultPoint pointB, ResultPoint pointC) {
        float bX = pointB.f2072x;
        float bY = pointB.f2073y;
        return ((pointC.f2072x - bX) * (pointA.f2073y - bY)) - ((pointC.f2073y - bY) * (pointA.f2072x - bX));
    }
}
