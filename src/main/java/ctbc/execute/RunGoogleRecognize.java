package ctbc.execute;

import java.io.IOException;

import ctbc.google.RecognizeSpeech;

public class RunGoogleRecognize {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RecognizeSpeech recognizeSpeech = new RecognizeSpeech();
		try {
			recognizeSpeech.asyncRecognizeWords("gs://cloud-speech-storage-ctbc/03-9129299279550000981.wav");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
