 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;

/**
 *
 * @author foufou
 */
@XmlRootElement(name="question")

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(propOrder={"name", "questiontext", "generalfeedback", "defaultgrade", "penalty", "hidden", "correctfeedback", "partiallycorrectfeedback", "incorrectfeedback", "answers"})

public class Question {

    /**
     * @param subquestions the subquestions to set
     */
    public void setSubquestions(ArrayList<Subquestion> subquestions) {
        this.subquestions = subquestions;
    }

    private boolean single = true;
    private boolean shuffleanswers = true;
    private int hidden = 0;
    private int usecase ;
    private String type = "";
    private String format = "moodle_auto_format";
    private String answernumbering  = "123";
    private float penalty;
    private float defaultgrade;
    private float unitpenalty;
    private int unitgradingtype;
    private int showunits;
    private int unitsleft;
    ArrayList<Unit> units;
    ArrayList<Subquestion> subquestions;
    private final QuestionName name = new QuestionName();
    private final QuestionText questiontext = new QuestionText();
    private final Feedback correctfeedback = new Feedback();
    private final Feedback partiallycorrectfeedback = new Feedback();
    private final Feedback incorrectfeedback = new Feedback();
    private final Feedback generalfeedback = new Feedback();
    
    ArrayList<Answer> answers;     
 
    ArrayList<AnswerNumerical> answersnum; 
    
    public static void main(String[] args){}
    public Question() {
        this.answers = new ArrayList<>();
        this.answersnum = new ArrayList<>();
        this.units = new ArrayList<>();
        this.subquestions = new ArrayList<>();
    }
    @Override
    public String toString() {
        return ("\nQuestionName:"+this.getName()+
                    "\nQuestion Numbering: "+ this.getAnswernumbering() +
                    "\nShuffle: "+ this.isShuffleanswers() +
                    "\nSingle : " + this.isSingle());
   }
    public void setAnswer(Answer answer) {
        Answer ans = answer;
        this.answers.add(ans);
    }
    
    public void setAnswernum(AnswerNumerical answer) {
        AnswerNumerical ans = answer;
        this.answersnum.add(ans);
    } 
    
//    @XmlElements({
//    @XmlElement(name="question", type=TrueFalseQuestion.class),
//    @XmlElement(name="question", type=ShortAnswerQuestion.class),
//    @XmlElement(name="question", type=MultiChoiceQuestion.class)
//})

    /**
     * @return the format
     */
    //@XmlAttribute(for all feedback elements)
    @XmlTransient
    public String getFormat() {
        return format;
    }

    /**
     * @param format the format to set
     */
    public void setFormat(String format) {
        this.format = format;
        this.questiontext.setFormat(format);
        this.correctfeedback.setFormat(format); 
        this.partiallycorrectfeedback.setFormat(format) ;
        this.incorrectfeedback.setFormat(format);
        this.generalfeedback.setFormat(format);
        //this.subquestions.forEach(setFormat(format););
    }

    /**
     * @return the answernumbering
     */
    //@XmlElement
    public String getAnswernumbering() {
        return answernumbering;
    }

    /**
     * @param answernumbering the answernumbering to set
     */
    public void setAnswernumbering(String answernumbering) {
        this.answernumbering = answernumbering;
    }

    /**
     * @return the single
     */
    //@XmlElement
    public boolean isSingle() {
        return single;
    }

    /**
     * @param single the single to set
     */
    public void setSingle(boolean single) {
        this.single = single;
    }

    /**
     * @return the shuffleanswers
     */
    //@XmlElement
    public boolean isShuffleanswers() {
        return shuffleanswers;
    }

    /**
     * @param shuffleanswers the shuffleanswers to set
     */
    public void setShuffleanswers(boolean shuffleanswers) {
        this.shuffleanswers = shuffleanswers;
    }

     /**
     * @return the incorrectfeedback
     */
    @XmlElement
    public Feedback getIncorrectfeedback() {
        return incorrectfeedback;
    }

    /**
     * @param incorrectfeedback the incorrectfeedback to set
     */
    public void setIncorrectfeedback(String incorrectfeedback) {
        this.incorrectfeedback.setText(incorrectfeedback);
    }

    /**
     * @return the partiallycorrectfeedback
     */
    @XmlElement
    public Feedback getPartiallycorrectfeedback() {
        return partiallycorrectfeedback;
    }

    /**
     * @param partiallycorrectfeedback the partiallycorrectfeedback to set
     */
    public void setPartiallycorrectfeedback(String partiallycorrectfeedback) {
        this.partiallycorrectfeedback.setText(partiallycorrectfeedback);
    }

    /**
     * @return the correctfeedback
     */
    @XmlElement
    public Feedback getCorrectfeedback() {
        return correctfeedback;
    }

    /**
     * @param correctfeedback the correctfeedback to set
     */
    public void setCorrectfeedback(String correctfeedback) {
        this.correctfeedback.setText(correctfeedback);
    }

      
    public void setName(String name)
    {
        this.name.setText(name);
    }
    @XmlElement
    public QuestionName getName()
    {
        return name;
    }
 
 
    public void setQuestiontext(String questiontext)
    {
        this.questiontext.setText(questiontext);
    }
    @XmlElement
    public QuestionText getQuestiontext() {
        return questiontext;
    }

    /**
     * @return the penalty
     */
    @XmlElement
    public float getPenalty() {
        return penalty;
    }

    /**
     * @param penalty the penalty to set
     */
    public void setPenalty(float penalty) {
        this.penalty = penalty;
    }

    /**
     * @return the generalfeedback
     */
    @XmlElement
    public Feedback getGeneralfeedback() {
        return generalfeedback;
    }

    /**
     * @param generalfeedback the generalfeedback to set
     */
    public void setGeneralfeedback(String generalfeedback) {
        this.generalfeedback.setText(generalfeedback);
    }

    /**
     * @return the defaultgrade
     */
    @XmlElement
    public float getDefaultgrade() {
        return defaultgrade;
    }

    /**
     * @param defaultgrade the defaultgrade to set
     */
    public void setDefaultgrade(float defaultgrade) {
        this.defaultgrade = defaultgrade;
    }

    /**
     * @return the hidden
     */
    @XmlElement
    public int getHidden() {
        return hidden;
    }

    /**
     * @param hidden the hidden to set
     */
    public void setHidden(int hidden) {
        this.hidden = hidden;
    }

    /**
     * @param image the images to set
     */
    public void setImages(Image image) {
        this.questiontext.setImages(image);
    }    

    /**
     * @return the type
     */
    @XmlAttribute
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
    
    //@XmlElement(name="usecase")
    //@XmlTransient
    public int getUsecase() {
        return usecase;
    }
     
    public void setUsecase(int usecase) {
        this.usecase = usecase;
    }

    /**
     * @return the units
     */
    public ArrayList<Unit> getUnits() {
        return units;
    }

    
    public void setUnits(Unit uni) {
        Unit unit = uni;
        this.units.add(unit);
    }

    /**
     * @return the answers
     */
    @XmlElement(name="answer")
    public ArrayList<Answer> getAnswers() {
        return answers;
    }
    
    /**
     * @return the unitgradingtype
     */
    public int getUnitgradingtype() {
        return unitgradingtype;
    }

    /**
     * @param unitgradingtype the unitgradingtype to set
     */
    public void setUnitgradingtype(int unitgradingtype) {
        this.unitgradingtype = unitgradingtype;
    }

    /**
     * @return the showunits
     */
    public int getShowunits() {
        return showunits;
    }

    /**
     * @param showunits the showunits to set
     */
    public void setShowunits(int showunits) {
        this.showunits = showunits;
    }

    /**
     * @return the unitsleft
     */
    public int getUnitsleft() {
        return unitsleft;
    }

    /**
     * @param unitsleft the unitsleft to set
     */
    public void setUnitsleft(int unitsleft) {
        this.unitsleft = unitsleft;
    }

    /**
     * @return the unitpenalty
     */
    public float getUnitpenalty() {
        return unitpenalty;
    }

    /**
     * @param unitpenalty the unitpenalty to set
     */
    public void setUnitpenalty(float unitpenalty) {
        this.unitpenalty = unitpenalty;
    }

    /**
     * @param answersnum the answersnum to set
     */
    public void setAnswersnum(ArrayList<AnswerNumerical> answersnum) {
        this.answersnum = answersnum;
    }

    /**
     * @return the answersnum
     */
    
    
    public ArrayList<AnswerNumerical> getAnswersnum() {
        return answersnum;
    }

    /**
     * @return the subquestions
     */
    public ArrayList<Subquestion> getSubquestions() {
        return subquestions;
    }
    public void setSubquestions(Subquestion subq) {
        Subquestion subquestion = subq;
        this.subquestions.add(subquestion);
    }
}