package com.geololocation.core

import java.util.regex.Matcher
import java.util.regex.Pattern

class MatchUtil {
    def isClosest(def lon1, def lat1, def lon2, def lat2, def longitude, def latitude) {
        if (Math.abs(longitude - lon1) <= Math.abs(longitude - lon2) && Math.abs(latitude - lat1) <= Math.abs(latitude - lat2))
            return true
        else if (Math.abs(longitude - lon1) <= Math.abs(longitude - lon2) && Math.abs(latitude - lat1) > Math.abs(latitude - lat2)) {
            return Math.abs(longitude - lon1) > Math.abs(latitude - lat2)
        } else if (Math.abs(longitude - lon1) > Math.abs(longitude - lon2) && Math.abs(latitude - lat1) <= Math.abs(latitude - lat2)) {
            return Math.abs(longitude - lon2) < Math.abs(latitude - lat1)
        }
    }

   def hasTheSameLocation(def lon1, def lat1, def lon2, def lat2){
        return lon1 == lon2 && lat1 == lat2
    }

    def isMatchedName(def str1, def str2) {
        Pattern p = Pattern.compile("(${str1})", Pattern.CASE_INSENSITIVE)
        Matcher m = p.matcher(str2)
        return m.find()
    }
}

