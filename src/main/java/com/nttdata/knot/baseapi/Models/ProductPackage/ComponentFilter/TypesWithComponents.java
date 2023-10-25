package com.nttdata.knot.baseapi.Models.ProductPackage.ComponentFilter;

import java.util.List;

import com.nttdata.knot.baseapi.Models.ComponentPackage.Component;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TypesWithComponents {
    private String id;
    private String label;
    private List<Component> components;

    public TypesWithComponents() {
    }

    public TypesWithComponents(String id, String label, List<Component> components) {
        this.id = id;
        this.label = label;
        this.components = components;
    }

}
