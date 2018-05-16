/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;


/**
 *
 * @author foufou
 */
class ConvertJava {

    ConvertJava(String Classpath, String Testpath, String Output) throws NullPointerException, FileNotFoundException, UnsupportedEncodingException, IOException {
        ReadFile(Classpath, Testpath, Output);
    }
    
    public static void main(String[] args) {
        try {
            ReadFile(args[0],args[1],args[2]);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ConvertJava.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConvertJava.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void ReadFile(String Classpath, String Testpath, String Output) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        String OutputPath = Output;
        Pattern patternParams;
        Matcher matcherParams;
        Pattern patternClass;
        Matcher matcherClass;
        Pattern patternTemplate;
        Matcher matcherTemplate;
        Pattern patternTest;
        Matcher matcherTest;
        patternParams = Pattern.compile("^@cr_");
        patternClass = Pattern.compile("(public[^{]*)([^\\\\Z]*)");
        patternTemplate = Pattern.compile("\\*-([^\\*\\/])");
        patternTest = Pattern.compile("(@cr_testcase.*?)(public void[^{]*)([^@]*)");
        boolean isParam = false;
        boolean isCode = false;
        ArrayList<String> questionParams = new ArrayList<>();
        ArrayList<String> questionCode = new ArrayList<>();
        
        //Dealing with Java Class File
        
        FileInputStream fstreamC = new FileInputStream(Classpath);
        DataInputStream inC = new DataInputStream(fstreamC);
        BufferedReader brC = new BufferedReader(new InputStreamReader(inC, "ISO-8859-15"));
        String strLineC;
        
        System.out.println("\n*** Start reading Class File: " + Classpath +" ***\n");
        while ((strLineC = brC.readLine()) != null) {
            matcherParams = patternParams.matcher(strLineC);
            matcherClass = patternClass.matcher(strLineC);
            matcherTemplate = patternTemplate.matcher(strLineC);
            if (matcherTemplate.find()) { // findet *-------*/ ....
                isCode = false;
                isParam = false;
            }
            if (matcherParams.find()) { // findet @cr_parameter ....
                isParam = true;
                isCode = false;
            }
            if (matcherClass.find()) { // findet Methode Signature ....
                isCode = true;
                isParam = false;
            }
            if (isParam) {
                questionParams.add(strLineC);
            }
            if (isCode) {
                questionCode.add(strLineC);
            }
        }
        String QuestionParamsJoined = String.join(" ", questionParams);
        String QuestionAnswerJoined = String.join("\n", questionCode);
        String[] ParamsArrays = QuestionParamsJoined.split("@cr_");
        
        //Dealing with Java Test Class File
        
        FileInputStream fstreamT = new FileInputStream(Testpath);
        DataInputStream inT = new DataInputStream(fstreamT);
        BufferedReader brT = new BufferedReader(new InputStreamReader(inT, "ISO-8859-15"));
        String strLineT;
        String buffer="";
        System.out.println("\n*** Start reading Test Class File: " + Testpath +" ***\n");
        while ((strLineT = brT.readLine()) != null) {
            buffer+=strLineT.trim();
        }
        matcherTest = patternTest.matcher(buffer);
        List<String> TestParams = new ArrayList<>();
        List<String> TestMethodes = new ArrayList<>();
        while(matcherTest.find()){
            TestParams.add(matcherTest.group(1));
            TestMethodes.add(matcherTest.group(3));
        }        
        CRFetcher(QuestionAnswerJoined, ParamsArrays, TestParams, TestMethodes, OutputPath);
    }
    
    public static void CRFetcher(String QuestionAnswerJoined, String[] ParamsArrays, List<String> TestParams, List<String> TestMethodes, String OutputPath) {
        CR_Quiz quiz = new CR_Quiz();
        System.out.println("\n*** Start parsing Code Runner Question ***\n");
        CR_Question crq = new CR_Question();
        boolean isClass = false;
        boolean isMethod = false;
        String MethodCode = ""; 
        for(String tmp : ParamsArrays){
            if (tmp.contains("coderunnertype")){
                if (tmp.contains("java_class")){
                    isClass = true;
                    crq.setAnswer(QuestionAnswerJoined);
                    break;
                } else if (tmp.contains("java_method")){
                    isMethod = true;
                    String tmp1 = tmp.replaceFirst("(.*)\\|", "").replaceAll("\"|\\)", "").trim();
                    MethodCode = getMethodeBody(QuestionAnswerJoined, tmp1);
                    crq.setAnswer(MethodCode);
                    break;
                }
            }
        }   
        Pattern patternClass;
        Matcher matcherClass = null;
        patternClass = Pattern.compile("(public[^{]*)([^\\\\Z]*)");
        if (isClass){
            matcherClass = patternClass.matcher(QuestionAnswerJoined);
        } else if (isMethod) {
            matcherClass = patternClass.matcher(MethodCode);
        }
        String answerPreload = "";
        if (matcherClass.find()) { // findet Methode Signature ....
            answerPreload = matcherClass.group(1);
            answerPreload += "{\n     //put your answer code hier \n}";
            crq.setAnswerpreload(answerPreload);
        }

        for (String st : ParamsArrays) {
            if (!st.isEmpty()) {
                String[] xy = paramSplitter(st);
                String paramValue = paramValuesplitter(xy[1]);

                if (xy[0].trim().equals("name")) {
                    crq.setName(paramValue.trim());
                }
                if (xy[0].trim().contains("generalfeedback")) {
                    crq.setGeneralfeedback(paramValue.trim());
                }
                if (xy[0].trim().contains("defaultgrade")) {
                    crq.setDefaultgrade(Float.parseFloat(paramValue.trim()));
                }
                if (xy[0].trim().equals("penalty")) {
                    crq.setPenalty(Float.parseFloat(paramValue));
                }
                if (xy[0].trim().contains("hidden")) {
                    crq.setHidden(Integer.parseInt(paramValue.trim()));
                }
                if (xy[0].trim().contains("coderunnertype")) {
                    if (isClass){
                        crq.setCoderunnertype("java_class");
                        System.out.println("Code Runner Question Type is Java Class");
                    } if (isMethod){
                        crq.setCoderunnertype("java_method");
                        System.out.println("Code Runner Question Type is Java Method");
                    }    
                }
                if (xy[0].trim().contains("prototypetype")) {
                    crq.setPrototypetype(Integer.parseInt(paramValue.trim()));
                }
                if (xy[0].trim().contains("allornothing")) {
                    crq.setAllornothing(Integer.parseInt(paramValue.trim()));
                }
                if (xy[0].trim().contains("penaltyregime")) {
                    crq.setPenaltyregime(paramValue.trim());
                }
                if (xy[0].trim().contains("precheck")) {
                    crq.setPrecheck(Integer.parseInt(paramValue.trim()));
                }
                if (xy[0].trim().contains("showsource")) {
                    crq.setShowsource(Integer.parseInt(paramValue.trim()));
                }
                if (xy[0].trim().contains("answerboxlines")) {
                    crq.setAnswerboxlines(Integer.parseInt(paramValue.trim()));
                }
                if (xy[0].trim().contains("answerboxcolumns")) {
                    crq.setAnswerboxcolumns(Integer.parseInt(paramValue.trim()));
                }
                if (xy[0].trim().contains("useace")) {
                    crq.setUseace(Integer.parseInt(paramValue.trim()));
                }
                if (xy[0].trim().contains("cputimelimitsecs")) {
                    crq.setCputimelimitsecs(paramValue.trim());
                }
                if (xy[0].trim().contains("memlimitmb")) {
                    crq.setMemlimitmb(paramValue.trim());
                }
                if (xy[0].trim().contains("validateonsave")) {
                    crq.setValidateonsave(Integer.parseInt(paramValue.trim()));
                }
                if (xy[0].trim().contains("iscombinatortemplate")) {
                    crq.setIscombinatortemplate(paramValue.trim());
                }
                if (xy[0].trim().contains("allowmultiplestdins")) {
                    crq.setAllowmultiplestdins(paramValue.trim());
                }
                if (xy[0].trim().contains("language")) {
                    crq.setLanguage(paramValue.trim());
                }
                if (xy[0].trim().contains("acelang")) {
                    crq.setAcelang(paramValue.trim());
                }
                if (xy[0].trim().equals("sandbox")) {
                    crq.setSandbox(paramValue.trim());
                }
                if (xy[0].trim().contains("grader")) {
                    crq.setGrader(paramValue.trim());
                }     
                if (xy[0].trim().equals("template")) {
                    crq.setTemplate(xy[1]);
                }
                if (xy[0].trim().contains("testsplitterre")) {
                    crq.setTestsplitterre(xy[1]);
                }
                if (xy[0].trim().contains("sandboxparams")) {
                    crq.setSandboxparams(xy[1]);
                }
                if (xy[0].trim().contains("templateparams")) {
                    crq.setTemplateparams(xy[1]);
                }
                if (xy[0].trim().contains("resultcolumns")) {
                    crq.setResultcolumns(xy[1]);
                }
                if (xy[0].trim().contains("questiontext")) {
                    crq.setQuestiontext(xy[1]);
                }
                if (xy[0].trim().contains("answerpreload")) {
                    crq.setAnswerpreload(xy[1]);
                }
                if (xy[0].trim().contains("quiz")) {
                    crq.setFormat("html");
                    quiz.setCategory(paramValue);
                    System.out.println("Quiz name:  "+paramValue);
                }
            }
        }
        System.out.println(TestParams.size()+" Test Cases were found");
        for (int i = 0; i < TestParams.size(); i++) {
        CR_Testcase tc = new CR_Testcase();
        ArrayList<String[]> x = TestParamSplitter(TestParams.get(i));
        String y = TestMethodes.get(i);
            for (int j = 0; j < x.size(); j++) {
                if (x.get(j)[0].trim().equals("mark")) {
                    tc.setMark(Double.parseDouble(x.get(j)[1].trim()));
                }
                if (x.get(j)[0].trim().equals("hiderestiffail")) {
                    tc.setHiderestiffail(Integer.parseInt(x.get(j)[1].trim()));
                }
                if (x.get(j)[0].trim().equals("useasexample")) {
                    tc.setUseasexample(Integer.parseInt(x.get(j)[1].trim()));
                }
                if (x.get(j)[0].trim().equals("testtype")) {
                    tc.setTesttype(Integer.parseInt(x.get(j)[1].trim()));
                }
                if (x.get(j)[0].trim().equals("stdin")) {
                    CR_Stdin stdin = new CR_Stdin();
                    stdin.setText(x.get(j)[1].trim());
                    tc.setStdin(stdin);
                }
                if (x.get(j)[0].trim().equals("extra")) {
                    CR_Extra extra = new CR_Extra();
                    extra.setText(x.get(j)[1].trim());
                    tc.setExtra(extra);
                }
                if (x.get(j)[0].trim().equals("display")) {
                    CR_Display display = new CR_Display();
                    display.setText(x.get(j)[1].trim());
                    tc.setDisplay(display);
                }
            }
            CR_Testcode testcode = new CR_Testcode();
            CR_Expected expect = new CR_Expected();
            String[] y2 = MethodeSplitter(y);
            String[] assertion = AssertSplitter(y2[1]);
            testcode.setText(y2[0]+" System.out.println("+assertion[1]+");");
            expect.setText(assertion[0]);
            tc.setTestcode(testcode);
            tc.setExpected(expect);
            crq.setTestcases(tc);
        }
        quiz.setQuestion(crq);
        OutputToXml(quiz, OutputPath);
    }
    
    public static void OutputToXml(CR_Quiz quiz, String output) {
        try {
            System.out.println("\n*** Start Writing to Output ***\n");
            String out = output;
            File file = new File(out);
            JAXBContext jaxbContext = JAXBContext.newInstance(CR_Quiz.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-15"); //"UTF-8"
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(quiz, file);
            System.out.println("###### File was successfully converted ######");
            JOptionPane.showMessageDialog(null, "Conversion done!   Please check your file on this path:\n"+file);            
        } catch (JAXBException e) {
            JOptionPane.showMessageDialog(null, "Error in marshalling..."+e.toString());
            System.out.println("###### Error in marshalling..."+e.toString()+" ######");
        }  
    }
    
    public static String[] paramSplitter(String str){
        String paramstring;
        paramstring = str;
        String[] params = new String[2];
        Pattern pat;
        Matcher mat;
        pat = Pattern.compile("(\\w*[^(])(.*[^)])");
        mat = pat.matcher(paramstring);
        if(mat.find()){
            params[0] = mat.group(1);
            params[1] = mat.group(2);
        }
        return params;
    }
    
    public static ArrayList<String[]> TestParamSplitter(String str){
        String paramstring;
        paramstring = str;
        String paramString = "";
        ArrayList<String[]> finalParams = new ArrayList<>();
        Pattern pat;
        Matcher mat;
        pat = Pattern.compile("(@cr_testcase\\()(.*[^)])");
        mat = pat.matcher(paramstring);
        if(mat.find()){
            paramString = mat.group(2);
            String[] params = paramString.split(",");
            for (int i = 0; i < params.length; i++) {
                String[] ParamArray = params[i].split("=");
                ParamArray[0] = ParamArray[0].trim();
                ParamArray[1] = ParamArray[1].trim().replaceAll("\"", "");
                finalParams.add(ParamArray);
            }
        }
        return finalParams;
    }
    
    public static String paramValuesplitter(String str) {
        String paramstring = str;
        if (paramstring.contains("value=")) {
            String replace = paramstring.replaceAll("\"|\\[|\\]|\\(|\\)", "");
            String[] paramvalue = replace.split("=");
            return paramvalue[1];
        } else {
            return paramstring;
        }
    }
    
    public static String[] MethodeSplitter(String str){
        String methode;
        methode = str.replaceAll("^\\{|\\}$|\\}\\}$", "");
        String[] methodeArray = new String[2];
        methodeArray = methode.split("assert");
        return methodeArray;
    }
    
    public static String[] AssertSplitter(String str) {
        String assertion = str.trim();
        String[] result = new String[2];
        if(assertion.startsWith("Equals(")){
            System.out.println("    -Converting AssertEquals...");
            String tmp = assertion.replaceAll("^Equals\\(|\\);$", "");
            String[] tmp2 = tmp.split(",");
            result[0] = tmp2[0].replaceAll("^\"|\"$", "");
            result[1] = tmp2[1].trim();
        }
        if(assertion.startsWith("True(")){
            System.out.println("    -Converting AssertTrue...");
            result[0] = "true";
            result[1] = assertion.replaceAll("^True\\(|\\);$", "");
        }
        if(assertion.startsWith("False(")){
            System.out.println("    -Converting AssertFalse...");
            result[0] = "false";
            result[1] = assertion.replaceAll("^False\\(|\\);$", "");
        }
        return result;
    }
    
    public static String getMethodeBody(String code, String methodName){
        String codeString = "";
        String reg1 = "((public|private|protected|static|final|native|synchronized|abstract|transient)+\\s)+[\\$_\\w\\<\\>\\[\\]]*\\s+["+methodName.trim()+"]+\\([^\\)]*\\)?\\s*\\{?[^\\}]*\\}?";
        Matcher mat;
        Pattern pat = Pattern.compile(reg1);
        mat = pat.matcher(code);
        if(mat.find()){
            codeString = mat.group();
        }
        return codeString;
    }
    
// regex Kram: 
//        "(public|private|static|protected|abstract|native|synchronized) +([a-zA-Z0-9<>._?, ]*) +([a-zA-Z0-9_]+) *\\([a-zA-Z0-9<>\\[\\]._?, \\n]*\\) *([a-zA-Z0-9_ ,\\n]*) *\\{";
//        "((public|private|protected|static|final|native|synchronized|abstract|transient)+\\s)+[\\$_\\w\\<\\>\\[\\]]*\\s+[\\$_\\w]+\\([^\\)]*\\)?\\s*\\{?[^\\}]*\\}?"
//        ((public|private|protected|static|final|native|synchronized|abstract|transient)+\s)+[\$_\w\<\>\[\]]*\s+[getStr]+\([^\)]*\)?\s*\{?[^\}]*\}?
    
}
        
