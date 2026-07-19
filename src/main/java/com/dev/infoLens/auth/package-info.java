@org.springframework.modulith.ApplicationModule(
        allowedDependencies = {
                "user :: api",  // Accesses only the named "api" interface of order
                "user ::  authApi",       // Accesses the entire default public surface of catalog
                "common"
        }
)
package com.dev.infoLens.auth;