package com.xm1zich.eeadditions.config;

import net.createmod.catnip.config.ConfigBase;

public class CClient extends ConfigBase {
    public final ConfigBool testBool = b(false, "testBool", "Test Config Boolean.");

    @Override
    public String getName() {
        return "client";
    }
}