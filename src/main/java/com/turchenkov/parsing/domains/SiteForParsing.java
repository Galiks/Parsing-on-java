package com.turchenkov.parsing.domains;

public abstract class SiteForParsing {

    private String name;
    private double discount;
    private String label;
    private String pageOnTheSite;
    private String image;

    public SiteForParsing(String name, double discount, String label, String pageOnTheSite, String image) {
        this.name = name;
        this.discount = discount;
        this.label = label;
        this.pageOnTheSite = pageOnTheSite;
        this.image = image;
    }

    public SiteForParsing() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPageOnTheSite() {
        return pageOnTheSite;
    }

    public void setPageOnTheSite(String pageOnTheSite) {
        this.pageOnTheSite = pageOnTheSite;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
