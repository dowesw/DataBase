package com.lymytz.entitymanager.component.android;

import java.text.SimpleDateFormat;

public class MySimpleDateFormat extends SimpleDateFormat {

    private String pattern;

    public MySimpleDateFormat(String pattern) {
        super(pattern);
        this.pattern = pattern;
    }

    public SimpleDateFormat get() {
        return get(pattern);
    }

    public static SimpleDateFormat get(final String pattern) {
        return new ThreadLocal<SimpleDateFormat>() {
            @Override
            protected SimpleDateFormat initialValue() {
                return new SimpleDateFormat(pattern);
            }
        }.get();
    }
}
