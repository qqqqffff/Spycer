package com.apollor.spycer.utils;

import com.apollor.spycer.Application;
import io.ipgeolocation.api.GeolocationParams;
import io.ipgeolocation.api.IPGeolocationAPI;
import io.ipgeolocation.api.exceptions.IPGeolocationError;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Geolocation {
    private Geolocation(){}
    public static double[] findLatLong() throws IOException {
        URL url = new URL("http://checkip.amazonaws.com/");
        BufferedReader sc = new BufferedReader(new InputStreamReader(url.openStream()));

        IPGeolocationAPI ipGeolocationAPI = new IPGeolocationAPI(Application.geolocationKey);

        //TODO: replace with system language code
        GeolocationParams geolocationParams = GeolocationParams.builder()
                .withIPAddress(sc.readLine().trim())
                .withLang("en")
                .build();

        try {
            io.ipgeolocation.api.Geolocation location = ipGeolocationAPI.getGeolocation(geolocationParams);

            return new double[]{location.getLatitude().doubleValue(), location.getLongitude().doubleValue()};
        }catch(IPGeolocationError ignored){

        }
        return null;
    }
}
