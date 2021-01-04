package com.ntq.vnexpress.model;

import org.simpleframework.xml.Element;

public class Image {
    @Element
    String url;

    @Element
    String title;

    @Element
    String link;
}
