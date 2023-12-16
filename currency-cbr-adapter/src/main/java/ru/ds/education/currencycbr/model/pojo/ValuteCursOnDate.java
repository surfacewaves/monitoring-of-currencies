package ru.ds.education.currencycbr.model.pojo;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName = "ValuteData")
public class ValuteCursOnDate {
    @JacksonXmlProperty(isAttribute = true, localName = "Vname")
    private String vname;
    @JacksonXmlProperty(isAttribute = true, localName = "Vnom")
    private int vnom;
    @JacksonXmlProperty(isAttribute = true, localName = "Vcurs")
    private double vcurs;
    @JacksonXmlProperty(isAttribute = true, localName = "VchCode")
    private String vchCode;
    @JacksonXmlProperty(isAttribute = true, localName = "VunitRate")
    private double vunitRate;
    @JacksonXmlProperty(isAttribute = true, localName = "Vcode")
    private double vcode;
}
