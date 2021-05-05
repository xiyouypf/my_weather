package com.ypf.myweather.db;

import org.litepal.crud.DataSupport;

import lombok.Data;

//@Data
public class County extends DataSupport {
    private int id;
    private String name;
    private String weatherId;
    private int cityId;

    public County() {
    }

    public County(int id, String name, String weatherId, int cityId) {
        this.id = id;
        this.name = name;
        this.weatherId = weatherId;
        this.cityId = cityId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    @Override
    public String toString() {
        return "County{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", weatherId='" + weatherId + '\'' +
                ", cityId=" + cityId +
                '}';
    }
}
