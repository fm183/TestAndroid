package com.example.testandroid.observed;

import android.util.Log;

import java.util.Observable;
import java.util.Observer;

public class PersonalObserver implements Observer {

    private final String name;

    public PersonalObserver(String name){
        this.name = name;
    }

    @Override
    public void update(Observable o, Object arg) {
        Log.d(PersonalObserver.class.getSimpleName(), "update: name="+name+",o="+o+",arg="+arg);
    }
}
