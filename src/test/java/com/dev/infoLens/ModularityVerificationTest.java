package com.dev.infoLens;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

class ModularityVerificationTest {

    @Test
    void verifyModularBoundaries() {
        // 1. Scans your entire project structure
        ApplicationModules modules = ApplicationModules.of(InfoLensApplication.class);

        // 2. Prints the detected modules into your test logs (Helpful debug)
        System.out.println(modules);

        // 3. Enforces the package-info.java boundaries and allowedDependencies
        modules.verify();
    }
}

