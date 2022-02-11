package com.nineoldandroids.animation;

import org.xbill.DNS.Type;

public class ArgbEvaluator implements TypeEvaluator {
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        int startInt = ((Integer) startValue).intValue();
        int startA = startInt >> 24;
        int startR = (startInt >> 16) & Type.ANY;
        int startG = (startInt >> 8) & Type.ANY;
        int startB = startInt & Type.ANY;
        int endInt = ((Integer) endValue).intValue();
        return Integer.valueOf(((((((int) (((float) ((endInt >> 24) - startA)) * fraction)) + startA) << 24) | ((((int) (((float) (((endInt >> 16) & Type.ANY) - startR)) * fraction)) + startR) << 16)) | ((((int) (((float) (((endInt >> 8) & Type.ANY) - startG)) * fraction)) + startG) << 8)) | (((int) (((float) ((endInt & Type.ANY) - startB)) * fraction)) + startB));
    }
}
