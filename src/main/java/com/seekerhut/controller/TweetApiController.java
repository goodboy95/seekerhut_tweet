package com.seekerhut.controller;

import java.util.Arrays;
import java.util.Date;

import javax.annotation.Resource;

import com.seekerhut.service.TweetCommonService;
import com.seekerhut.utils.JedisHelper;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

@Controller
@RequestMapping("/api/tweet")
public class TweetApiController extends BaseController {
    @Resource
    private TweetCommonService tweetCommonService;

    @RequestMapping(value = "/redistest", method = RequestMethod.GET)
    @ApiImplicitParams({
        //@ApiImplicitParam(name = "replyJson", value = "", paramType = "body", dataType = "String"),
    })
    public @ResponseBody String RedisTest() {
        try {
            JedisHelper.incr("testincr");
            return "";
        }
        catch (Exception e) {
            return Fail(-1, String.join(",", Arrays.stream(e.getStackTrace()).map(s -> s.toString()).toArray(String[] :: new)));
        }
    }
}
