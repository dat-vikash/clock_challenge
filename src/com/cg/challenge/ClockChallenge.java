package com.cg.challenge;
import javax.speech.*;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import javax.speech.synthesis.Voice;
import java.io.File;
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
 */

public class ClockChallenge {
    static String VOICE = "kevin16";
    //initiailize a speech syntesizer
    static Synthesizer synthesizer = null;
    //initialize the speech recoginier
    static Recognizer recognizer = null;


    static private String noSynthesizerMessage() {
        String message =
            "No synthesizer created.  This may be the result of any\n" +
            "number of problems.  It's typically due to a missing\n" +
            "\"speech.properties\" file that should be at either of\n" +
            "these locations: \n\n";
        message += "user.home    : " + System.getProperty("user.home") + "\n";
        message += "java.home/lib: " + System.getProperty("java.home") +
            File.separator + "lib\n\n" +
            "Another cause of this problem might be corrupt or missing\n" +
            "voice jar files in the freetts lib directory.  This problem\n" +
            "also sometimes arises when the freetts.jar file is corrupt\n" +
            "or missing.  Sorry about that.  Please check for these\n" +
            "various conditions and then try again.\n";
        return message;
    }

    private static void speak(String s){
        synthesizer.speakPlainText(s, null);
        try {
            synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
        } catch (InterruptedException e) {
           e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private static Boolean setUpMicrophone(){
          //create a configuration manager to aide in system device lookup
        ConfigurationManager cm = new ConfigurationManager(ClockChallenge.class.getResource("clockchallenge.config.xml"));

        //get a speech recognizer instance (in this case cmu.sphinx4)
        recognizer = (Recognizer) cm.lookup("recognizer");
        recognizer.allocate();

        // start the microphone or exit if the programm if this is not possible
        Microphone microphone = (Microphone) cm.lookup("microphone");
        if (!microphone.startRecording()) {
            System.out.println("Cannot start microphone.");
            recognizer.deallocate();
            return false;
        }
        return true;
    }

    static private Boolean setUpVoiceSynthesizer(){
        //set up speech synthesis
        try {
            /* Find a synthesizer that has the general domain voice
             * we are looking for.
             */
            SynthesizerModeDesc desc = new SynthesizerModeDesc(
                null,          // engine name
                "general",     // mode name
                Locale.US,     // locale
                null,          // running
                null);         // voice
             synthesizer = Central.createSynthesizer(desc);

            /* Just an informational message to guide users that didn't
             * set up their speech.properties file.
             */
            if (synthesizer == null) {
                System.out.println(noSynthesizerMessage());
                return false;
            }

            /* Get the synthesizer ready to speak
             */
            synthesizer.allocate();
            synthesizer.resume();

            /* Choose the voice.
             */
            desc = (SynthesizerModeDesc) synthesizer.getEngineModeDesc();
            Voice[] voices = desc.getVoices();
            Voice voice = null;
            for (int i = 0; i < voices.length; i++) {
                if (voices[i].getName().equals(VOICE)) {
                    voice = voices[i];
                    break;
                }
            }
            if (voice == null) {
                System.err.println(
                    "Synthesizer does not have a voice named "
                    + VOICE + ".");
                return false;
            }
            synthesizer.getSynthesizerProperties().setVoice(voice);

            return true;

            /* Clean up and leave.
             */
//            synthesizer.deallocate();
//            System.exit(0);

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return false;
        }

    }


    public static void main(String[] args) {
        //setup date and time format
        DateFormat dateFormat = new SimpleDateFormat("h:mm a");

        //setup speech recognition with microphone
        if (!setUpMicrophone()){
            System.exit(1);
        }
        //setup speech synthesizer
        if(!setUpVoiceSynthesizer()){
            System.exit(1);
        }

        //alert users to available grammar
        System.out.println("Say: (what time | remind me | temperature )");

        // loop the recognition until the programm exits.
        while (true) {
            System.out.println("Start speaking. Press Ctrl-C to quit.\n");

            //do we recognize anything?
            Result result = recognizer.recognize();

            if (result != null) {
                String resultText = result.getBestFinalResultNoFiller();
                if (resultText.equals("what time")) {
                    System.out.println("You said: " + resultText + '\n');
                    //get time information
                    Date date = new Date();
                    speak(dateFormat.format(date).toString());
                }else if (resultText.equals("remind me")){
                    //
                    speak("You said remind me");
                }
                else if (resultText.equals("temperature")){
                       //
                    speak("You asked for the temperature");
                }

            } else {
                System.out.println("I can't hear what you said.\n");
            }
        }
    }
}
