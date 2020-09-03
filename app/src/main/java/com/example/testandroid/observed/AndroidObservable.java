package com.example.testandroid.observed;

import java.util.Observable;

public class AndroidObservable extends Observable {

        public void post(String message){
            setChanged();
            notifyObservers(message);
        }

}
