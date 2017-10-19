/*
  Copyright 2017, Google Inc.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/

package ctbc.google;

import com.google.api.gax.rpc.OperationFuture;
import com.google.cloud.speech.v1.LongRunningRecognizeMetadata;
import com.google.cloud.speech.v1.LongRunningRecognizeResponse;
import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.cloud.speech.v1.WordInfo;
import com.google.longrunning.Operation;
import com.google.protobuf.ByteString;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class RecognizeSpeech {
	public static String languageCode = "cmn-Hant-TW"; //語言別
	public static AudioEncoding audioEncoding =AudioEncoding.MULAW; //音檔格式
	public static String audioLoc = "wav/";//音檔所在資料夾
	public static int sampleRate = 8000;
	
   /*
  /**
   * Performs non-blocking speech recognition on raw PCM audio and prints
   * the transcription. Note that transcription is limited to 60 seconds audio.
   *
   * @param fileName the path to a PCM audio file to transcribe.
   */
  public static void asyncRecognizeFile(String fileName) throws Exception, IOException {
    // Instantiates a client with GOOGLE_APPLICATION_CREDENTIALS
    SpeechClient speech = SpeechClient.create();

    Path path = Paths.get(fileName);
    byte[] data = Files.readAllBytes(path);
    ByteString audioBytes = ByteString.copyFrom(data);

    // Configure request with local raw PCM audio
    RecognitionConfig config = RecognitionConfig.newBuilder()
        .setEncoding(audioEncoding)
        .setLanguageCode(languageCode)
        .setSampleRateHertz(sampleRate)
        .build();
    RecognitionAudio audio = RecognitionAudio.newBuilder()
        .setContent(audioBytes)
        .build();

    // Use non-blocking call for getting file transcription
    OperationFuture<LongRunningRecognizeResponse, LongRunningRecognizeMetadata,
            Operation> response =
        speech.longRunningRecognizeAsync(config, audio);

    while (!response.isDone()) {
      System.out.println("Waiting for response...");
      Thread.sleep(10000);
    }

    List<SpeechRecognitionResult> results = response.get().getResultsList();

    for (SpeechRecognitionResult result: results) {
      // There can be several alternative transcripts for a given chunk of speech. Just use the
      // first (most likely) one here.
      SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
      System.out.printf("Transcription: %s%n", alternative.getTranscript());
    }
    speech.close();
  }

  /**
   * Performs non-blocking speech recognition on remote FLAC file and prints
   * the transcription as well as word time offsets.
   *
   * @param gcsUri the path to the remote LINEAR16 audio file to transcribe.
   */
  public static void asyncRecognizeWords(String gcsUri) throws Exception, IOException {
    // Instantiates a client with GOOGLE_APPLICATION_CREDENTIALS
    SpeechClient speech = SpeechClient.create();

    // Configure remote file request for Linear16
    RecognitionConfig config = RecognitionConfig.newBuilder()
        .setEncoding(audioEncoding)
        .setLanguageCode(languageCode)
        .setSampleRateHertz(sampleRate)
        .setEnableWordTimeOffsets(true)
        .build();
    RecognitionAudio audio = RecognitionAudio.newBuilder()
        .setUri(gcsUri)
        .build();

    // Use non-blocking call for getting file transcription
    OperationFuture<LongRunningRecognizeResponse, LongRunningRecognizeMetadata,
            Operation> response =
        speech.longRunningRecognizeAsync(config, audio);
    while (!response.isDone()) {
      System.out.println("等待Google轉譯...");
      Thread.sleep(10000);
    }

    List<SpeechRecognitionResult> results = response.get().getResultsList();
    int count = 0;
    for (SpeechRecognitionResult result: results) {
      // There can be several alternative transcripts for a given chunk of speech. Just use the
      // first (most likely) one here.
      for(SpeechRecognitionAlternative alternative : result.getAlternativesList()){
    	  count++;
    	  System.out.println("================Alternative "+count+" Start==========================");
          System.out.printf("T: %s\n",alternative.getTranscript());
          System.out.println("Confidence"+alternative.getConfidence());
          for (WordInfo wordInfo: alternative.getWordsList()) {
            System.out.println(wordInfo.getWord());
            System.out.printf("\t%s.%s sec - %s.%s sec\n",
                wordInfo.getStartTime().getSeconds(),
                wordInfo.getStartTime().getNanos() / 100000000,
                wordInfo.getEndTime().getSeconds(),
                wordInfo.getEndTime().getNanos() / 100000000);
          }
          System.out.println("================Alternative "+count+" End==========================");
      }

    }
    speech.close();
  }

  /**
   * Performs non-blocking speech recognition on remote FLAC file and prints
   * the transcription.
   *
   * @param gcsUri the path to the remote LINEAR16 audio file to transcribe.
   */
  public static void asyncRecognizeGcs(String gcsUri) throws Exception, IOException {
    // Instantiates a client with GOOGLE_APPLICATION_CREDENTIALS
    SpeechClient speech = SpeechClient.create();

    // Configure remote file request for Linear16
    RecognitionConfig config = RecognitionConfig.newBuilder()
        .setEncoding(audioEncoding)
        .setLanguageCode(languageCode)
        .setSampleRateHertz(sampleRate)
        .build();
    RecognitionAudio audio = RecognitionAudio.newBuilder()
        .setUri(gcsUri)
        .build();

    // Use non-blocking call for getting file transcription
    OperationFuture<LongRunningRecognizeResponse, LongRunningRecognizeMetadata,
            Operation> response =
        speech.longRunningRecognizeAsync(config, audio);
    while (!response.isDone()) {
      System.out.println("Waiting for response...");
      Thread.sleep(10000);
    }

    List<SpeechRecognitionResult> results = response.get().getResultsList();

    for (SpeechRecognitionResult result: results) {
      // There can be several alternative transcripts for a given chunk of speech. Just use the
      // first (most likely) one here.
      SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
      System.out.printf("Transcription: %s\n",alternative.getTranscript());
    }
    speech.close();
  }
}