package com.cg.challenge;
import javax.speech.*;
import javax.speech.recognition.*;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.util.props.ConfigurationManager;
import edu.cmu.sphinx.result.Result;

/**
 * Created by IntelliJ IDEA.
 * User: vikash
 * Date: 9/7/12
 * Time: 2:56 PM
 * To change this template use File | Settings | File Templates.
 */

public class ClockChallenge {

    public static void main(String[] args) {
        ConfigurationManager cm;

        if (args.length > 0) {
            cm = new ConfigurationManager(args[0]);
        } else {
            cm = new ConfigurationManager(ClockChallenge.class.getResource("helloworld.config.xml"));
        }

        Recognizer recognizer = (Recognizer) cm.lookup("recognizer");
        recognizer.allocate();

        // start the microphone or exit if the programm if this is not possible
        Microphone microphone = (Microphone) cm.lookup("microphone");
        if (!microphone.startRecording()) {
            System.out.println("Cannot start microphone.");
            recognizer.deallocate();
            System.exit(1);
        }

        System.out.println("Say: (Good morning | Hello) ( Bhiksha | Evandro | Paul | Philip | Rita | Will )");

        // loop the recognition until the programm exits.
        while (true) {
            System.out.println("Start speaking. Press Ctrl-C to quit.\n");

            Result result = recognizer.recognize();

            if (result != null) {
                String resultText = result.getBestFinalResultNoFiller();
//                System.out.println("You said: " + resultText + '\n');
                if (resultText.equals("")) {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                System.out.println(dateFormat.format(date));
                }

            } else {
                System.out.println("I can't hear what you said.\n");
            }
        }
    }
}


//public class ClockChallenge extends ResultAdapter{
//    static Recognizer rec;
//
//    //Recieve RESULT_ACCEPTED event
//    public void resultAccepted(ResultEvent e){
//        Result r = (Result)(e.getSource());
//        ResultToken tokens[] = r.getBestTokens();
//
//        for(int i=0; i < tokens.length; i++){
//            System.out.println(tokens[i].getSpokenText() + " ");
//        }
//
//        //deallocate the recognizer
//        try {
//            rec.deallocate();
//        } catch (EngineException e1) {
//            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
//    }
//
//    public static void main(String args[]){
//        try{
//             RecognizerModeDesc desc;
//            desc = new RecognizerModeDesc(Locale.ENGLISH);
//            EngineList list = Central.availableRecognizers(desc);
//            System.out.println(list);
//            //create a recognizer
//            rec = Central.createRecognizer(new EngineModeDesc(Locale.ENGLISH));
//            rec.allocate();
//
//            FileReader reader = new FileReader("what_time.gram");
//            RuleGrammar gram = rec.loadJSGF(reader);
//            gram.setEnabled(true);
//
//            rec.addResultListener(new ClockChallenge());
//            rec.commitChanges();
//
//            rec.requestFocus();
//            rec.resume();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//}
