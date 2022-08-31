package com.easy.dashboard.controller;

import com.easy.dashboard.model.ResultObject;

public class BaseController {
    protected ResultObject result(Boolean result) {
        return result ? ResultObject.success() : ResultObject.error();
    }
}
