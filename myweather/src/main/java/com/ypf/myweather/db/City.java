package com.ypf.myweather.db;

import org.litepal.crud.DataSupport;

import lombok.Data;

@Data
public class City extends DataSupport {
    private int id;
    private String name;
    private int code;
    private int provinceId;
}
