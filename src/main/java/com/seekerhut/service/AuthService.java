package com.seekerhut.service;

import com.seekerhut.utils.CommonFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.digest.DigestUtils;

import javax.annotation.Resource;
import java.lang.ref.SoftReference;
import java.util.Date;

@Service("AuthService")
public class AuthService {
    public String captchaGenerator() {
        return "";
    }

}
