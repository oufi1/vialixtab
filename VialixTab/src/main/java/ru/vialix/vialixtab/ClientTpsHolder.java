package ru.vialix.vialixtab;

public class ClientTpsHolder {
    private static float tps = 20.0f;

    public static float getTps() {
        return tps;
    }

    public static void setTps(float value) {
        tps = value;
    }
}