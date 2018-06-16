package com.zopa;

public class TestUtils {

    private static TestUtils INSTANCE = new TestUtils();

    public static TestUtils getInstance() {
        return INSTANCE;
    }

    public String getResourcePath(String path) {
        return getClass().getClassLoader().getResource(path).getPath();
    }
}
