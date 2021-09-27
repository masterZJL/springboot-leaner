package com.zjl.study.boottwo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping("/{path}")
    public String pageRouter(@PathVariable String path) {

        return path;
    }
}
