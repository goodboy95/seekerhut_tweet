package com.seekerhut.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seekerhut.utils.CommonFunctions;
import com.seekerhut.utils.EnumAndConstData.EncodeType;
import com.seekerhut.utils.EnumAndConstData.HTTPMethod;
import com.seekerhut.utils.custombean.datagenerator.DataGenConfig;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/api/test")
public class TestController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);
    private static BlockingQueue<Runnable> bq = new LinkedBlockingQueue<>(6);
    private static ThreadPoolExecutor tpe = new ThreadPoolExecutor(5, 8, 0, TimeUnit.SECONDS, bq);

    @RequestMapping(value = "base_test", method = RequestMethod.GET)
    public @ResponseBody String baseTest() {
        return Success("");
    }

    @RequestMapping(value = "xjb_test", method = RequestMethod.POST)
    public @ResponseBody String XJBTest(String strA, String strB) {
        var arrA = Arrays.stream(strA.split(",")).mapToInt(Integer::parseInt).toArray();
        var arrB = Arrays.stream(strB.split(",")).mapToInt(Integer::parseInt).toArray();
        
        
        return Success("");
    }

    @RequestMapping(value = "api_call_test", method = RequestMethod.POST)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "generatorConfig", value = "", paramType = "form", dataType = "String")
    })
    public @ResponseBody String apiCallTest(String generatorConfig) throws IOException {
        CloseableHttpClient hc = HttpClients.createDefault();
        int currentPercent = 0;
        DataGenConfig config = JSON.parseObject(generatorConfig, DataGenConfig.class);
        HTTPMethod method = config.getMethod();
        String url = config.getUrl();
        int cycles = config.getTimes();
        int interval = config.getInterval();
        var fieldsConfig = config.getFieldData();
        try {
            for (int i = 1; i <= cycles; i++) {
                var generatedParams = CommonFunctions.generateQueryDataByConfig(fieldsConfig);
                var finalParams = generatedParams.entrySet().stream()
                    .map(e -> (NameValuePair)new BasicNameValuePair(e.getKey(), e.getValue()))
                    .collect(Collectors.toList());
                if (method == HTTPMethod.get) {
                    URIBuilder builder = new URIBuilder(url);
                    builder.addParameters(finalParams);
                    HttpGet getObj = new HttpGet(builder.build());
                    hc.execute(getObj).close();
                } else if (method == HTTPMethod.post) {
                    HttpPost postObj = new HttpPost(url);
                    HttpEntity entity = config.getPostDataType() == EncodeType.formdata
                        ? new UrlEncodedFormEntity(finalParams, StandardCharsets.UTF_8)
                        : new StringEntity(finalParams.get(0).getValue(), StandardCharsets.UTF_8);
                    postObj.setEntity(entity);
                    hc.execute(postObj).close();
                } else {
                    //TODO: add other query methods
                }
                Thread.sleep(interval);
                if (i * 100 / cycles > currentPercent) {
                    currentPercent = i * 100 / cycles;
                    System.out.println(String.format("Executed %d/%d iterations (%d%%)", i, cycles, currentPercent));
                }
            }
            return Success("");
        } catch (Exception e) {
            return Fail(-1, e.getMessage());
        } finally {
            hc.close();
        }
    }

    @RequestMapping(value = "xxx_test", method = RequestMethod.POST)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "json", value = "", paramType = "form", dataType = "String")
    })
    public @ResponseBody String XXXTest(String json) throws Exception {
        HashSet<String> imgUrlSet = new HashSet<>();
        String baseDir = "/home/duwei/project/seekerhut/seekerhut_blog/italent";
        String baseUrl = "https://users.italent.cn/Document/GetHelpDocument?customerId=8aee33cc-f003-46c2-bd97-3f8e04b3261e&pageId=";
        Pattern imgUrlPattern = Pattern.compile("https://.+?\\.png");
        JSONArray jarr = JSONArray.parseArray(json);
        CloseableHttpClient hc = HttpClients.createDefault();
        HttpGet g = new HttpGet();
        URIBuilder uriBuilder = new URIBuilder();
        g.addHeader("cookie", "ssn_Tita_PC=owyaSzkt3hsoWVk1lhJPGP0AWglOIz89za_knwMiT-WpB3sW39uCD5LED-ZloEXJ");
        g.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        g.addHeader("sec-ch-ua", "\"Chromium\";v=\"92\", \" Not A;Brand\";v=\"99\", \"Google Chrome\";v=\"92\"");
        g.addHeader("sec-ch-ua-mobile", "?0");
        g.addHeader("Sec-Fetch-Dest", "document");
        g.addHeader("Sec-Fetch-Mode", "navigate");
        g.addHeader("Accept-Encoding", "gzip, deflate, br");
        g.addHeader("Accept-Language", "en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7,ga;q=0.6");
        g.addHeader("Host", "users.italent.cn");
        g.addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36");
        for (int i = 0; i < jarr.size(); i++) {
            JSONObject pageObj = jarr.getJSONObject(i);
            int pageId = pageObj.getIntValue("id");
            String pageName = pageObj.getString("tn");
            OutputStream s = new FileOutputStream(baseDir + "/html/" + pageName.replace(" ", "").replace("/", "") + ".html");
            g.setURI(new URI(baseUrl + pageId));
            CloseableHttpResponse resp = hc.execute(g);
            BufferedReader rd = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
            String lineStr = rd.readLine();
            rd.close();
            String htmlStr = JSONObject.parseObject(lineStr).getJSONObject("Data").getString("DocContent");
            htmlStr = htmlStr.replace("&lt;", "<").replace("&gt;", ">").replace("&quot;", "\"").replace("&nbsp;", " ").replace("&amp;", "&");
            Matcher imgUrlMatcher = imgUrlPattern.matcher(htmlStr);
            while (imgUrlMatcher.find()) {
                imgUrlSet.add(imgUrlMatcher.group());
            }
            BufferedWriter wd = new BufferedWriter(new OutputStreamWriter(s));
            wd.write(htmlStr);
            wd.close();
            s.close();
            Thread.sleep(1000);
        }
        for (String imgUrl : imgUrlSet) {
            String[] urlSplit = imgUrl.split("/");
            String fileName = urlSplit[urlSplit.length - 1];
            OutputStream s = new FileOutputStream(baseDir + "/img/" + fileName);
            g.setURI(new URI(imgUrl));
            CloseableHttpResponse resp = hc.execute(g);
            resp.getEntity().writeTo(s);
            resp.close();
            s.close();
            Thread.sleep(1000);
        }
        return Success("");
    }
}
