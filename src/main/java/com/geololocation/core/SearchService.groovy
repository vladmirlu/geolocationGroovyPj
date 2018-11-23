package com.geololocation.core

import com.geololocation.exception.PlaceNotFoundException
import org.json.JSONArray
import org.json.JSONObject

class SearchService {
    MatchUtil mathUtil = new MatchUtil()
    RestClient restClient = new RestClient()

    def getLocation(def longitude, def latitude) {
        JSONObject jsonObj = restClient.getCloserByCircle(longitude, latitude)
        if (!jsonObj.getJSONArray('results').isEmpty()) {
            JSONArray places = getPlaces(jsonObj.getJSONArray('results'), longitude, latitude)
            if (places.size() == 1) return places.get(0).getAt('displayString')
            else return places
        } else throw new PlaceNotFoundException()
    }

    def findPlacesByName(JSONArray places, def name) {
        JSONArray closerPlaces = restClient.getCloserByName(places, name)
        JSONArray theClosestPlaces = putClosestPlaces(closerPlaces, places.get(0))
        if (theClosestPlaces.size() == 1)
            theClosestPlaces.get(0).getAt('displayString') + ', Coordinates:' + closerPlaces.get(0).getAt('place').getAt('geometry').getAt('coordinates')
        else theClosestPlaces
    }

    JSONArray putClosestPlaces(JSONArray closerPlaces, JSONObject place) {
        JSONArray theClosestPlaces = new JSONArray()
        for (JSONObject obj : closerPlaces) {
            if (obj.getAt('place').getAt('geometry').getAt('coordinates').get(0) == place.getAt('place').getAt('geometry').getAt('coordinates').get(0)
                && obj.getAt('place').getAt('geometry').getAt('coordinates').get(1) == place.getAt('place').getAt('geometry').getAt('coordinates').get(1))
                theClosestPlaces.put(obj)
        }
        return theClosestPlaces
    }

    JSONArray getPlaces(JSONArray jsonArray, def longitude, def latitude) {
        JSONArray closestPlaces = new JSONArray()
        JSONObject closestObj = jsonArray.get(0)
        def lon1, lat1, lon2, lat2

        for (JSONObject object : jsonArray) {
            println(object.getAt('place').getAt('geometry').getAt('coordinates'))
            lon1 = object.getAt('place').getAt('geometry').getAt('coordinates').get(0)
            lat1 = object.getAt('place').getAt('geometry').getAt('coordinates').get(1)
            lon2 = closestObj.getAt('place').getAt('geometry').getAt('coordinates').get(0)
            lat2 = closestObj.getAt('place').getAt('geometry').getAt('coordinates').get(1)
            if ((mathUtil.isClosest(lon1, lat1, lon2, lat2, longitude, latitude)))
                closestObj = object
        }
        for (JSONObject obj : jsonArray) {
            lon1 = obj.getAt('place').getAt('geometry').getAt('coordinates').get(0)
            lat1 = obj.getAt('place').getAt('geometry').getAt('coordinates').get(1)
            if (mathUtil.hasTheSameLocation(lon1, lat1, lon2, lat2))
                closestPlaces.put(obj)
        }
        return closestPlaces
    }

    def getOneByName(JSONArray array, def name) {
        def result = 'there is no location with such name'
        for (JSONObject object : array) {
            if (name == object.getAt('name')) {
                result = object.getAt('displayString') + '; COORDINATES: ' + object.getAt('place').getAt('geometry').getAt('coordinates')
            } else if (mathUtil.isMatchedName(name, object.getAt('name')))
                result = object.getAt('displayString') + '; COORDINATES: ' + object.getAt('place').getAt('geometry').getAt('coordinates')
        }
        return result
    }
}
