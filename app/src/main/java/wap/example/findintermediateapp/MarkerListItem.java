package wap.example.findintermediateapp;

public class MarkerListItem {

    private int markerNum;
    private String markerLocation;
    private int deleteMarker;

    public void setMarkerNum(int num) {
        this.markerNum = num;
    }

    public void setMarkerLocation(String location) {
        this.markerLocation = location;
    }

    public void setDeleteMarker(int deleteMarker) { this.deleteMarker = deleteMarker; }

    public int getMarkerNum() {
        return this.markerNum;
    }

    public String getMarkerLocation() {
        return this.markerLocation;
    }

    public int getDeleteMarker() { return this.deleteMarker;}

}
