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
@XmlRootElement(name="name")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class QuestionName {
    private String text = "";

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
}
