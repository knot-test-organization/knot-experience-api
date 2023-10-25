package com.nttdata.knot.baseapi.Models.ProductPackage;

import java.util.List;

import com.nttdata.knot.baseapi.Models.ComponentPackage.Env;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Product {
    private String id;
    private String name;
    private String description;
    private String organization;
    private String area;
    private String po;
    private List<String> teams;
    private String version;
    private List<Env> environments;
    private List<String> components;
    private String date;

    public Product() {
    }
    public Product(String id, String name, String description, String organization, String area, String po,
            List<String> teams, String version, List<Env> environments, List<String> components, String date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.organization = organization;
        this.area = area;
        this.po = po;
        this.teams = teams;
        this.version = version;
        this.environments = environments;
        this.components = components;
        this.date = date;
    }

    

}
