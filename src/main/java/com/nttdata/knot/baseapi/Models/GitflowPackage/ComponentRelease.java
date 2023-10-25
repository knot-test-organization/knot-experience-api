package com.nttdata.knot.baseapi.Models.GitflowPackage;

import com.nttdata.knot.baseapi.Models.ComponentPackage.Component;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComponentRelease {

    private Component component;
    private Release release;

    public ComponentRelease() {
    }

    public ComponentRelease(Component component, Release release) {
        this.component = component;
        this.release = release;
    }

}
