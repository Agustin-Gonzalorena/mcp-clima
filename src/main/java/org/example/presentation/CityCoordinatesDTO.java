package org.example.presentation;


public class CityCoordinatesDTO {
    private String name;
    private Double lat;
    private Double lon;

    public CityCoordinatesDTO(String name,Double lat,Double lon){
        this.name = name;
        this.lat=lat;
        this.lon=lon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }
}
