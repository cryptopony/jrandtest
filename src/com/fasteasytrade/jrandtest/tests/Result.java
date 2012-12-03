package com.fasteasytrade.jrandtest.tests;

import java.util.Collections;
import java.util.Map;

public class Result {

    final public static Result UNKNOWN = new Result(ResultStatus.UNKNOWN, Collections.<String,String>emptyMap());

    final private ResultStatus status;
    final private Map<String,String> details;

    public Result(ResultStatus status, Map<String,String> details) {
        this.status = status;
        this.details = details;
    }

    public ResultStatus status() {
        return status;
    }

    public Map<String,String> details() {
        return details;
    }

}
