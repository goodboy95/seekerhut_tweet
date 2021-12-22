package com.seekerhut.utils;

public class EnumAndConstData {
    public enum HTTPMethod {
        get,
        post,
        put,
        delete
    }

    public enum EncodeType {
        formdata,
        json,
    }
    
    public enum CharRangeKey {
        number,
        uppercase,
        lowercase,
        symbol,
        wordchar,
        chinese,
    }
}
