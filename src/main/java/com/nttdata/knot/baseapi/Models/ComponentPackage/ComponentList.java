package com.nttdata.knot.baseapi.Models.ComponentPackage;

import java.util.ArrayList;
import java.util.List;


import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class ComponentList {
    private List<Component> components = new ArrayList<Component>();
}
