package com.gruppo42.app.utils;

import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;

import java.util.HashMap;
import java.util.Map;

public class Utils {
    public static String codeToEmoji(String countryCode) {
        int firstLetter = Character.codePointAt(countryCode, 0) - 0x41 + 0x1F1E6;
        int secondLetter = Character.codePointAt(countryCode, 1) - 0x41 + 0x1F1E6;
        return new String(Character.toChars(firstLetter)) + new String(Character.toChars(secondLetter));
    }

    public static TranslateAnimation shakeError() {
        TranslateAnimation shake = new TranslateAnimation(0, 10, 0, 0);
        shake.setDuration(500);
        shake.setInterpolator(new CycleInterpolator(7));
        return shake;
    }

    public static String getGenre(int id)
    {
        String id2 = id+"";
        Map<String, String> map = new HashMap<>();
        map.put("28","Action");
        map.put("12","Adventure");
        map.put("16","Animation");
        map.put("35","Comedy");
        map.put("80", "Crime");
        map.put("99", "Documentary");
        map.put("182", "Drama");
        map.put("10751", "Family");
        map.put("14", "Fantasy");
        map.put("36", "History");
        map.put("27", "Horror");
        map.put("10402", "Music");
        map.put("9648", "Mystery");
        map.put("10749", "Romance");
        map.put("878", "Science Fiction");
        map.put("10770", "TV Movie");
        map.put("53", "Thriller");
        map.put("10752", "War");
        map.put("37", "Western");
        return map.get(id2);
    }
}
