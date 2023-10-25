package com.nttdata.knot.baseapi.Models.ProductPackage;

import java.util.ArrayList;
import java.util.List;


import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class ProductList {
    private List<Product> products = new ArrayList<Product>();
}
