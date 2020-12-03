package com.suffix.fieldforce.akg.util;

public class LocationUtils {
  public static double getDistance(int lat1, int lon1, int lat2, int lon2) {
    double p = 0.017453292519943295; // Math.PI / 180

    double a = 0.5 - Math.cos((lat2 - lat1) * p)/2 +
        Math.cos(lat1 * p) * Math.cos(lat2 * p) *
            (1 - Math.cos((lon2 - lon1) * p))/2;

    return (12742 * Math.asin(Math.sqrt(a))) * 1000; // 2 * R = 12742; R = 6371 km
  }
}
