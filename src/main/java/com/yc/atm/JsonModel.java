package com.yc.atm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsonModel<T> implements Serializable {
    private int code;
    private String msg;
    private T data;
}
