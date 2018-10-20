package com.example.ecare_client.Googlemaps;

public class DirectionItem {

    private String data, polyline;
    private double startLat, startLng, endLat, endLng;
    private boolean isPathInstruction;

    public DirectionItem(String data, double startLat, double startLng,
                         double endLat, double endLng, String polyline,
                         boolean isPathInstruction) {
        this.data = data;
        this.startLat = startLat;
        this.startLng = startLng;
        this.endLat = endLat;
        this.endLng = endLng;
        this.polyline = polyline;
        this.isPathInstruction = isPathInstruction;
    }

    public double getEndLat() {
        return endLat;
    }

    public void setEndLat(double endLat) {
        this.endLat = endLat;
    }

    public double getEndLng() {
        return endLng;
    }

    public void setEndLng(double endLng) {
        this.endLng = endLng;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public double getStartLat() {
        return startLat;
    }

    public void setStartLat(double startLat) {
        this.startLat = startLat;
    }

    public double getStartLng() {
        return startLng;
    }

    public void setStartLng(double startLng) {
        this.startLng = startLng;
    }

    public String getPolyline() {
        return polyline;
    }

    public void setPolyline(String polyline) {
        this.polyline = polyline;
    }

    public boolean isPathInstruction() {
        return isPathInstruction;
    }

    public void setPathInstruction(boolean isPathInstruction) {
        this.isPathInstruction = isPathInstruction;
    }
}

