package com.ember.client.setting;

public class NumberSetting extends Setting<Double> {
    public final double min;
    public final double max;
    public final double step;

    public NumberSetting(String name, double value, double min, double max, double step) {
        super(name, value);
        this.min = min;
        this.max = max;
        this.step = step;
    }

    @Override
    public void set(Double v) {
        double clamped = Math.max(min, Math.min(max, v));
        if (step > 0) {
            clamped = Math.round(clamped / step) * step;
            clamped = Math.max(min, Math.min(max, clamped));
        }
        value = clamped;
    }

    public int intValue() {
        return (int) Math.round(value);
    }

    public double fraction() {
        return (value - min) / (max - min);
    }
}
