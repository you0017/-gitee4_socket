package com.yc.util;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "ArrayOfString", namespace = "http://WebXml.com.cn/")
@XmlAccessorType(XmlAccessType.FIELD)
public class ArrayOfString {

    @XmlElement(name = "string")
    private List<String> strings;

    // Getter and Setter
    public List<String> getStrings() {
        return strings;
    }

    public void setStrings(List<String> strings) {
        this.strings = strings;
    }
}