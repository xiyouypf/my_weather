package com.ypf.myweather.db;

import org.litepal.crud.DataSupport;

import lombok.Data;

@Data
public class Province extends DataSupport {
    private int id;
    private String name;
    private int code;
}
