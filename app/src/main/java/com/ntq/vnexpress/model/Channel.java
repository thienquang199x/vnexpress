package com.ntq.vnexpress.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;

@Root(name = "channel")
public class Channel {
    @Element(name = "title", required = false)
    private String title;

    @Element(name = "description", required = false)
    private String description;

    @Element(name = "image", required = false)
    private Image image;

    @Element(name = "pubDate", required = false)
    private String pubDate;

    @Element(name = "generator", required = false)
    private String generator;

    @Element(name = "link", required = false)
    private String link;

    @ElementList(name = "item", type = ExpressNew.class, required = false, inline = true)
    private ArrayList<ExpressNew> items;

    public ArrayList<ExpressNew> getItems() {
        return items;
    }

    public void setItems(ArrayList<ExpressNew> items) {
        this.items = items;
    }
}
