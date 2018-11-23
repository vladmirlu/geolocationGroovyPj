package com.geololocation.core

import org.apache.http.HttpException
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.util.EntityUtils
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class RestClient {
    def placeSearchURL = 'http://www.mapquestapi.com/search/v4/place?sort=distance&pageSize=50&key='
    Properties prop = null
    def key
    def radius
    RestClient (){
        setPropAllKeys()
    }

    void setPropAllKeys(){
        InputStream is = null
        try {
            this.prop = new Properties()
            is = this.getClass().getResourceAsStream("/app.properties")
            prop.load(is)
        } catch (FileNotFoundException e) {
            e.printStackTrace()
        } catch (IOException e) {
            e.printStackTrace()
        }
        key = prop.getProperty('mapquest_key')
        radius = prop.getProperty('radius')
    }

    JSONObject getCloserByCircle(def longitude, def latitude) throws IOException {
        def circle = '&circle=' + longitude + ',' + latitude + ',' + radius
        def url = placeSearchURL + key + circle
        return getJSONObject(url)
    }

    JSONArray getCloserByName(JSONArray array, def name) throws IOException {
        def location = '&location=' + array.get(0).getAt('place').getAt('geometry').getAt('coordinates').get(0) + ',' + array.get(0).getAt('place').getAt('geometry').getAt('coordinates').get(1)
        def q = '&q=' + name
        def url = placeSearchURL + key + q + location
        JSONObject jsonObj = getJSONObject(url)
        return jsonObj.getJSONArray('results')
    }

    JSONObject getJSONObject (def url) throws HttpException, JSONException{
        HttpClient client = new DefaultHttpClient()
        HttpGet request = new HttpGet(url)
        HttpResponse response = client.execute(request)
        String jsonStr = EntityUtils.toString(response.getEntity())
        return new JSONObject(jsonStr)
    }
}
