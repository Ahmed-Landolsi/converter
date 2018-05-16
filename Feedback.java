 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor. 
 */
package converter;

import javax.xml.bind.annotation.*;

/**
 *
 * @author foufou
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Feedback {
    private String text= "";
    private String format = "moodle_auto_format";

    /**
     * @return the text
     */
    @XmlElement(name="text")
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the format
     */
    @XmlAttribute(name="format")
    public String getFormat() {
        return format;
    }

    /**
     * @param format the format to set
     */
    public void setFormat(String format) {
        this.format = format;
    }
    
}
