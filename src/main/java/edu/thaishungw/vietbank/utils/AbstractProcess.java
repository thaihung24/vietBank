package edu.thaishungw.vietbank.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mservice.shared.exception.MoMoException;
import com.mservice.shared.utils.Execute;

public abstract class AbstractProcess<T, V> {
    protected Execute execute = new Execute();

    public static Gson getGson() {
        return new GsonBuilder()
                .disableHtmlEscaping()
                .create();
    }

    public abstract V execute(T request) throws MoMoException;
}
