package com.nttdata.knot.baseapi.Models.ProductPackage.ComponentFilter;
import java.util.List;

import com.nttdata.knot.baseapi.Models.ComponentPackage.Component;
import com.nttdata.knot.baseapi.Models.ComponentPackage.Env;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EnvironmentWithComponents {
    private boolean enabled;
    private String envPath;
    private String nameSpace;
    private String version;
    private List<Component> components;

    public EnvironmentWithComponents() {
    }

    public EnvironmentWithComponents(boolean enabled, String envPath, String nameSpace, String version,
            List<Component> components) {
        this.enabled = enabled;
        this.envPath = envPath;
        this.nameSpace = nameSpace;
        this.version = version;
        this.components = components;
    }

    public EnvironmentWithComponents(Env env, List<Component> components) {
        this.enabled = env.isEnabled();
        this.envPath = env.getEnvPath();
        this.nameSpace = env.getNameSpace();
        this.version = env.getVersion();
        this.components = components;
    }
    
}
