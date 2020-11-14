package com.xiaott.blog.utils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CastUtil {

    public static <T> List<T> object2List(Object object,Class<T> clazz) {
        if (object instanceof Set<?>) {
            return ((Set<?>) object).stream()
                                    .map(clazz::cast)
                                    .collect(Collectors.toList());
        } else if (object instanceof List<?>) {
            return ((List<?>) object).stream()
                                     .map(clazz::cast)
                                     .collect(Collectors.toList());
        }
        else {
            return null;
        }
    }
}
