package com.geololocation.exception;

public class PlaceNotFoundException extends Exception {

    PlaceNotFoundException(){
        printStackTrace();
        System.out.println("The place not searchable");
    }
}
