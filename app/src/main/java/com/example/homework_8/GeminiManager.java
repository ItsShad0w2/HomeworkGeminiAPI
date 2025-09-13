package com.example.homework_8;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.ai.client.generativeai.type.ImagePart;
import com.google.ai.client.generativeai.type.Part;
import com.google.ai.client.generativeai.type.TextPart;

import java.util.ArrayList;
import java.util.List;

import kotlin.Result;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;

public class GeminiManager
{
    private static GeminiManager instance;
    private final GenerativeModel gemini;

    private GeminiManager()
    {
        gemini = new GenerativeModel("gemini-2.0-flash", BuildConfig.GeminiAPIKey);
    }

    public static GeminiManager getInstance()
    {
        if(instance == null)
        {
            instance = new GeminiManager();
        }

        return instance;
    }

    public void sendPhotoPrompt(String prompt, Bitmap photo, GeminiCallBack callback)
    {
        List<Part> parts = new ArrayList<>();
        parts.add(new TextPart(prompt));
        parts.add(new ImagePart(photo));

        Content[] content = new Content[1];
        content[0] = new Content(parts);

        gemini.generateContent(content, new Continuation<GenerateContentResponse>(){
            @NonNull
            @Override
            public CoroutineContext getContext()
            {
                return EmptyCoroutineContext.INSTANCE;
            }

            @Override
            public void resumeWith(@NonNull Object response)
            {
                new android.os.Handler(android.os.Looper.getMainLooper()).post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if(response instanceof Result.Failure)
                        {
                            callback.onFailure(((Result.Failure) response).exception);
                        }
                        else
                        {
                            if(response instanceof GenerateContentResponse)
                            {
                                GenerateContentResponse responseGiven = (GenerateContentResponse) response;
                                String text = responseGiven.getText();

                                if(text != null && !(text.isEmpty()))
                                {
                                    callback.onSuccess(text);
                                }
                                else
                                {
                                    callback.onFailure(new Throwable("The response given was empty"));
                                }
                            }
                            else
                            {
                                callback.onFailure(new Throwable("Something went wrong with the response generation"));
                            }
                        }

                    }
                });
            }
        });
    }
}
