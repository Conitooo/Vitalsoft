package com.pmdm.pruebalistview.recognizer;

import okhttp3.*;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import androidx.annotation.NonNull;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class VoiceAssistant {

    private Context context;
    private SpeechRecognizer speechRecognizer;
    private VoiceCallback callback;

    private static final String DEEPSEEK_API_URL = "https://api.deepseek.com/v1/chat/completions";

    // Se recomienda usar BuildConfig para la API Key en lugar de escribirla directamente
    private static final String API_KEY = "sk-6486702aba974a0d85c0113db31fc022";

    public interface VoiceCallback {
        void onDecisionReceived(String decision, String contacto);
        void onError(String errorMessage);
    }

    public VoiceAssistant(Context context, VoiceCallback callback) {
        this.context = context;
        this.callback = callback;
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
    }

    public void startListening() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
                Log.d("VoiceAssistant", "Listo para escuchar");
            }

            @Override
            public void onError(int error) {
                String errorMessage;
                switch (error) {
                    case SpeechRecognizer.ERROR_NO_MATCH:
                        errorMessage = "No se reconoció ninguna palabra.";
                        break;
                    case SpeechRecognizer.ERROR_NETWORK:
                        errorMessage = "Error de red.";
                        break;
                    case SpeechRecognizer.ERROR_AUDIO:
                        errorMessage = "Error en el audio.";
                        break;
                    case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                        errorMessage = "No se detectó habla.";
                        break;
                    default:
                        errorMessage = "Error desconocido: " + error;
                }

                Log.e("VoiceAssistant", "Error de reconocimiento de voz: " + errorMessage);
                callback.onError(errorMessage);
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    String recognizedText = matches.get(0);
                    String finalMessage = "Interpreta la intención del usuario y responde con un JSON en este formato: " +
                            "{\"accion\": \"llamar\" o \"mensaje\", \"contacto\": \"nombre del contacto\"}. " +
                            "Ejemplo: Entrada: \"Quiero hablar con Pedro\" Salida: {\"accion\": \"llamar\", \"contacto\": \"Pedro\"}. " +
                            "Entrada: \"Manda un mensaje a Ana\" Salida: {\"accion\": \"mensaje\", \"contacto\": \"Ana\"}. " +
                            "Usuario: " + recognizedText;

                    sendToDeepSeek(finalMessage);
                }
            }

            @Override public void onPartialResults(Bundle bundle) {}
            @Override public void onEvent(int i, Bundle bundle) {}
            @Override public void onBeginningOfSpeech() {}
            @Override public void onRmsChanged(float v) {}
            @Override public void onBufferReceived(byte[] bytes) {}
            @Override public void onEndOfSpeech() {}
        });

        speechRecognizer.startListening(intent);
    }

    private void sendToDeepSeek(String userInput) {
        OkHttpClient client = new OkHttpClient();

        try {
            // Construcción de la solicitud con modelo y formato correcto
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("model", "deepseek-chat");
            jsonRequest.put("messages", new JSONArray()
                    .put(new JSONObject().put("role", "system").put("content", "Eres un asistente que ayuda a gestionar llamadas y mensajes."))
                    .put(new JSONObject().put("role", "user").put("content", userInput))
            );

            RequestBody body = RequestBody.create(jsonRequest.toString(), MediaType.get("application/json"));

            Request request = new Request.Builder()
                    .url(DEEPSEEK_API_URL)
                    .addHeader("Authorization", "Bearer " + API_KEY)
                    .addHeader("Content-Type", "application/json")
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.e("VoiceAssistant", "Error en la API de DeepSeek: " + e.getMessage());
                    callback.onError("Error en la API de DeepSeek");
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful() && response.body() != null) {
                        String responseBody = response.body().string();
                        try {
                            JSONObject jsonResponse = new JSONObject(responseBody);
                            String respuesta = jsonResponse.getJSONArray("choices")
                                    .getJSONObject(0)
                                    .getJSONObject("message")
                                    .getString("content");

                            JSONObject resultado = new JSONObject(respuesta);
                            String accion = resultado.getString("accion");
                            String contacto = resultado.getString("contacto");

                            Log.d("DeepSeek Response", "Acción: " + accion + ", Contacto: " + contacto);
                            callback.onDecisionReceived(accion, contacto);

                        } catch (Exception e) {
                            callback.onError("Error en la respuesta de DeepSeek");
                        }
                    } else {
                        callback.onError("Fallo en la API de DeepSeek");
                    }
                }
            });
        } catch (Exception e) {
            callback.onError("Error al construir la petición");
        }
    }

    public void destroy() {
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
    }
}
