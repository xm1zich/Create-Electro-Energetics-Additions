package com.xm1zich.eeadditions.config;

import net.createmod.catnip.config.ConfigBase;

public class CRotor extends ConfigBase {
    public final ConfigFloat powerMultiplier = f(4.0f, 1.0E-4f, "powerMultiplier", "decide output power of the compact rotor through voltage");
    public final ConfigFloat fullLoadCurrent = f(100.0f, 1.0E-4f, "fullLoadCurrent", "[in Amps]");
    public final ConfigFloat stressMultiplier = f(0.5f, 1.0E-4f, "stressMultiplier", "SUs of 1 Watt");

    @Override
    public String getName() {
        return "compactRotor";
    }
}
