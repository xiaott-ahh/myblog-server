package com.xiaott.blog.result;

public class ResultFactory {


    public static Result buildSuccessRep(String message) {
        Result result = new Result();
        result.setCode(200);
        result.setMessage(message);
        return result;
    }

    public static Result buildFailRep(String message) {
        Result result = new Result();
        result.setCode(400);
        result.setMessage(message);
        return result;
    }
}
