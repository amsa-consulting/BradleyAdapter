package com.bradley.bradleyadapter.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@Data
public class UpperWrapper {
    @XmlElement(name="CustomerLookupResultDS")
    List<DataMerged> CustomerLookupResultDS;
}
