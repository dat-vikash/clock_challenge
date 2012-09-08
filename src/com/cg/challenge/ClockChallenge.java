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

        //create a configuration manager to aide in system device lookup
        ConfigurationManager cm = new ConfigurationManager(ClockChallenge.class.getResource("clockchallenge.config.xml"));

        //get a speech recognizer instance (in this case cmu.sphinx4)
        Recognizer recognizer = (Recognizer) cm.lookup("recognizer");
        recognizer.allocate();

        // start the microphone or exit if the programm if this is not possible
        Microphone microphone = (Microphone) cm.lookup("microphone");
        if (!microphone.startRecording()) {
            System.out.println("Cannot start microphone.");
            recognizer.deallocate();
            System.exit(1);
        }

        System.out.println("Say: (What time is it | remind me in x minutes |  what is the temp )");

        // loop the recognition until the programm exits.
        while (true) {
            System.out.println("Start speaking. Press Ctrl-C to quit.\n");

            Result result = recognizer.recognize();

            if (result != null) {
                String resultText = result.getBestFinalResultNoFiller();
                if (resultText.equals("what time is it")) {
                System.out.println("You said: " + resultText + '\n');

                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                System.out.println(dateFormat.format(date));
                }else if (resultText.equals("remind me in ")){
                    //
                }
                else if (resultText.equals("what is the temp")){
                       //
                }

            } else {
                System.out.println("I can't hear what you said.\n");
            }
        }
    }
}
