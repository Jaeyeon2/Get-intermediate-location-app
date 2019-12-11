package wap.example.findintermediateapp;

public class LocationResultItem {

    private String resultName;
    private String resultLocation;
    private String resultMapx;
    private String resultMapy;
    private int resultNumber = 0;

    public void setResultNumberIncre() { resultNumber = resultNumber + 1; }

    public void setResultName(String name) { resultName = name; }

    public void setResultLocation(String location) {
        resultLocation = location;
    }

    public void setResultMapx(String x) { resultMapx = x; }

    public void setResultMapy(String y) { resultMapy = y; }

    public String getResultName() { return this.resultName; }

    public String getResultLocation() {
        return this.resultLocation;
    }

    public String getResultMapx() { return this.resultMapx; }

    public String getResultMapy() { return this.resultMapy; }

    public int getResultNumber() { return this.resultNumber; }

}
