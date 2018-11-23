package com.geololocation.core

import org.json.JSONArray
import org.json.JSONObject

import java.text.ParseException

class ConsoleCommunicator {
    BufferedReader br
    SearchService searchService = new SearchService()
    def regEx = '\\s'

    ConsoleCommunicator() throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in))
    }

    void searchStart() throws IOException, ParseException {
        print 'Input the longitude:'
        def longitude =  Double.parseDouble(br.readLine())
        print 'Input the latitude:'
        def latitude = Double.parseDouble(br.readLine())
        println 'You entered: longitude: ' + longitude + 'and the latitude: ' + latitude
        def result = searchService.getLocation(longitude, latitude)
        if (result instanceof JSONArray) {
            inputToSpecifyPlace(result)
        } else println(result)
    }

    void inputToSpecifyPlace(JSONArray array) throws IOException, ParseException {
        printPlacesList(array)
        def placeName = br.readLine().replaceAll(regEx, '')
        def result = searchService.findPlacesByName(array, placeName)
        if (result instanceof JSONArray) {
            if (result.isEmpty()) {
                println('The place is undefined')
                inputToSpecifyPlace(array)
            } else {
                printPlacesList(result)
                placeName = br.readLine()
                println(searchService.getOneByName(result, placeName))
            }
        } else println(result)
    }

    void printPlacesList(JSONArray array) {
        println('There are the nearest places below:')
        def count = 0
        for (JSONObject obj : array) {
            count++
            println(count + '). ' + 'NAME: ' + obj.getAt('name') + '; COORDINATES:' + obj.getAt('place').getAt('geometry').getAt('coordinates'))
        }
        println('Please enter NAME of the place to choose the concrete place from the list above: ')
    }
}
