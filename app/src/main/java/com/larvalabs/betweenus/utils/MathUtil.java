package com.larvalabs.betweenus.utils;

public class MathUtil {

    public static float map(float value, float min1, float max1, float min2, float max2) {
        return map(value, min1, max1, min2, max2, false);
    }

    public static float map(float value, float min1, float max1, float min2, float max2, boolean clamp) {
        float val = lerp( normalize(value, min1, max1), min2, max2);
        if (clamp) {
            return clamp(val, min2, max2);
        } else {
            return val;
        }
    }

    public static float lerp(float normValue, float minimum, float maximum) {
        return minimum + (maximum - minimum) * normValue;
    }

    public static float normalize(float value, float minimum, float maximum) {
        return (value - minimum) / (maximum - minimum);
    }

    public static float clamp(float value, float min, float max) {
        if (min > max) {
            float minb = min;
            min = max;
            max = minb;
        }
        if (value < min) {
            return min;
        } else if (value > max) {
            return max;
        } else {
            return value;
        }
    }
}
