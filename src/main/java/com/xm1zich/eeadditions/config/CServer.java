package com.xm1zich.eeadditions.config;

import net.createmod.catnip.config.ConfigBase;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.NotNull;

public class CServer extends ConfigBase {

    public final CResistances resistanceValues = nested(1, CResistances::new, "Resistance Values");
    public final CVoltages voltageValues = nested(1, CVoltages::new, "Voltage Values");

    @Override
    public @NotNull String getName() {
        return "server";
    }

    private ConfigDouble d(double current, double min, String name, String... comments) {
        return new ConfigDouble(name, current, min,  Double.MAX_VALUE, comments);
    }

    public class ConfigDouble extends CValue<Double, ModConfigSpec.DoubleValue> {

        public ConfigDouble(String name, double current, double min, double max, String... comment) {
            super(name, builder -> builder.defineInRange(name, current, min, max), comment);
        }
    }
}
