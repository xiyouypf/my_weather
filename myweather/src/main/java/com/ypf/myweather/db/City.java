package com.ypf.myweather.db;

import org.litepal.crud.DataSupport;

import lombok.Data;

//@Data
public class City extends DataSupport {
    private int id;
    private String name;
    private int code;
    private int provinceId;

    public City() {
    }

    public City(int id, String name, int code, int provinceId) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.provinceId = provinceId;
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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code=" + code +
                ", provinceId=" + provinceId +
                '}';
    }
}
