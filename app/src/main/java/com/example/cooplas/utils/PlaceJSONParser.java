package com.example.cooplas.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlaceJSONParser {

    /**
     * Receives a JSONObject and returns a list
     */
    public List<HashMap<String, String>> parse(JSONObject jObject) {

        JSONArray jPlaces = null;
        try {
            /** Retrieves all the elements in the 'places' array */
            jPlaces = jObject.getJSONArray("predictions");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /** Invoking getPlaces with the array of json object
         * where each json object represent a place
         */
        return getPlaces(jPlaces);
    }

    public List<HashMap<String, String>> getPublicHolidays(JSONArray jPlaces) {
        int placesCount = jPlaces.length();
        List<HashMap<String, String>> publicHolidayList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> publicHoliday = null;

        /** Taking each place, parses and adds to list object */
        for (int i = 0; i < placesCount; i++) {
            try {
                /** Call getPlace with place JSON object to parse the place */
                publicHoliday = getPublicHoliday((JSONObject) jPlaces.get(i));
                publicHolidayList.add(publicHoliday);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return publicHolidayList;
    }

    public HashMap<String, String> parsePlaceDetails(JSONObject jObject) {

        JSONObject jPlaces = null;
        JSONArray address_components = null;
        try {
            /** Retrieves all the elements in the 'places' array */
            jPlaces = jObject.getJSONObject("result");
            address_components = jPlaces.getJSONArray("address_components");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /** Invoking getPlaces with the array of json object
         * where each json object represent a place
         */
        //return getPlaceDetail(jPlaces);
        return getAddressDetails(address_components);
    }


    private HashMap<String, String> getAddressDetails(JSONArray jPlaces) {
        int placesCount = jPlaces.length();
        List<HashMap<String, String>> addressList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> addressDetails = null;

        /** Taking each place, parses and adds to list object */
        for (int i = 0; i < placesCount; i++) {
            try {
                /** Call getPlace with place JSON object to parse the place */
                addressDetails = getPostalCodeDetails((JSONObject) jPlaces.get(i));
                addressList.add(addressDetails);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return addressDetails;
    }

    private List<HashMap<String, String>> getPlaces(JSONArray jPlaces) {
        int placesCount = jPlaces.length();
        List<HashMap<String, String>> placesList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> place = null;

        /** Taking each place, parses and adds to list object */
        for (int i = 0; i < placesCount; i++) {
            try {
                /** Call getPlace with place JSON object to parse the place */
                place = getPlace((JSONObject) jPlaces.get(i));
                placesList.add(place);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return placesList;
    }

    private HashMap<String, String> getPostalCodeDetails(JSONObject jPlace) {

        HashMap<String, String> addressDetail = new HashMap<String, String>();

        String long_name = "";
        String short_name = "";
        String types = "";

        try {

            long_name = jPlace.getString("long_name");
            short_name = jPlace.getString("short_name");

            types = jPlace.getJSONArray("types").get(0).toString();
            addressDetail.put("long_name", long_name);
            addressDetail.put("short_name", short_name);
            addressDetail.put("types", types);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return addressDetail;
    }

    /**
     * Parsing the Place JSON object
     */
    private HashMap<String, String> getPlace(JSONObject jPlace) {

        HashMap<String, String> place = new HashMap<String, String>();

        String id = "";
        String reference = "";
        String description = "";

        try {

            description = jPlace.getString("description");
            id = jPlace.getString("place_id");
            reference = jPlace.getString("reference");

            place.put("description", description);
            place.put("_id", id);
            place.put("reference", reference);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return place;
    }

    private HashMap<String, String> getPublicHoliday(JSONObject jPlace) {

        HashMap<String, String> place = new HashMap<String, String>();
        JSONObject date;

        String day = "";
        String month = "";
        String year = "";
        String localName = "";

        try {
            date = new JSONObject(jPlace.getString("date"));
            day = date.getString("day");
            month = date.getString("month");
            year = date.getString("year");
            localName = jPlace.getString("localName");

            place.put("day", day);
            place.put("month", month);
            place.put("year", year);
            place.put("local_name", localName);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return place;
    }

    private HashMap<String, String> getPlaceDetail(JSONObject jPlace) {

        HashMap<String, String> place = new HashMap<String, String>();

        String address = "";
        String name = "";

        try {

            address = jPlace.getString("adr_address");
            name = jPlace.getString("name");

            place.put("address", address);
            place.put("name", name);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return place;
    }
}