package com.seekerhut.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import com.seekerhut.utils.EnumAndConstData.CharRangeKey;
import com.seekerhut.utils.custombean.datagenerator.FieldMetaData;

import io.netty.util.internal.ThreadLocalRandom;

public class CommonFunctions {
    private static String charIndex = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ~!@#$%^&*()_+`-=abcdefghijklmnopqrstuvwxyz{}:|<>?[];',./";
    private static HashMap<CharRangeKey, String> charInRange;
    private static HashMap<String, String> cachedCharRange;
    private static HashMap<Long, Long> prevRandSeedMap;

    static {
        prevRandSeedMap = new HashMap<Long, Long>();
        cachedCharRange = new HashMap<String, String>();
        charInRange = new HashMap<CharRangeKey, String>();
        charInRange.put(CharRangeKey.number, "0123456789");
        charInRange.put(CharRangeKey.lowercase, "abcdefghijklmnopqrstuvwxyz");
        charInRange.put(CharRangeKey.uppercase, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        charInRange.put(CharRangeKey.symbol, "~!@#$%^&*()_+`-={}:|<>?[];',./");
        charInRange.put(CharRangeKey.wordchar, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_abcdefghijklmnopqrstuvwxyz");
        charInRange.put(CharRangeKey.chinese, "的一国在人了有中是年和大业不为发会工经上地市要个产这出行作生家以成到日民来我部对进多全建他公开们场展时理新方主企资实学报制政济用同于法高长现本月定化加动合品重关机分力自外者区能设后就等体下万元社过前面农也得与说之员而务利电文事可种总改三各好金第司其从平代当天水省提商十管内小技位目起海所立已通入量子问度北保心还科");
    }

    public static String generateRandomString(int length) {
        return stringGenBase(length, charIndex);
    }

    public static String generateRandomString(int length, List<CharRangeKey> rangeKeys) {
        String rangeKeyStr = String.join(",", rangeKeys.stream().map(r -> r.toString()).collect(Collectors.toList()));
        String charRange = "";
        if (!cachedCharRange.containsKey(rangeKeyStr)) {
            for (CharRangeKey r : rangeKeys) {
                charRange += charInRange.get(r);
            }
            cachedCharRange.put(rangeKeyStr, charRange);
        } else {
            charRange = cachedCharRange.get(rangeKeyStr);
        }
        return stringGenBase(length, charRange);
    }

    private static String stringGenBase(int length, String charRange) {
        StringBuilder sb = new StringBuilder();
        int rangeLen = charRange.length();
        // Generate random seed, guaranteed to be different every time we generate.
        long randSeedBase = System.nanoTime();
        long threadId = Thread.currentThread().getId();
        long randSeed = randSeedBase ^ (threadId << 32);
        if (prevRandSeedMap.containsKey(threadId) && prevRandSeedMap.get(threadId).equals(randSeed)) {
            randSeed += 10; // you can add whatever you want except zero.
        }
        prevRandSeedMap.put(threadId, randSeed);
        Random r = new Random(randSeed);
        for (int i = 0; i < length; i++) {
            int idx = r.nextInt(rangeLen);
            sb.append(charRange.charAt(idx));
        }
        return sb.toString();
    }

    public static HashMap<String, String> generateQueryDataByConfig(HashMap<String, FieldMetaData> config)
    {
        HashMap<String, String> result = new HashMap<String, String>();
        for (String key : config.keySet()) {
            FieldMetaData data = config.get(key);
            String strBase = data.getStringFormatBase();
            int[] elementLen = data.getElementLength();
            List<List<CharRangeKey>> rangeKeys = data.getElementCharRanges();
            ArrayList<String> elements = new ArrayList<String>();
            for (int i = 0; i < elementLen.length; i++) {
                elements.add(generateRandomString(elementLen[i], rangeKeys.get(i)));
            }
            String curValue = String.format(strBase, elements.toArray(Object[]::new));
            result.put(key, curValue);
        }
        return result;
    }

    private static HashMap<String, AtomicLong> currentPrimaryKeyId = new HashMap<>();
    private static HashMap<String, Long> currentPrimaryKeyMaxId = new HashMap<>();
    private static HashMap<String, String> prevUsingKey = new HashMap<>();

    public static long generatePrimaryKeyId(String tableName) {
        // 每次从redis申请100000个id空间在本机使用，减少和redis的交互次数
        var idsPerRequest = 100000;
        var baseTime = LocalDateTime.of(2021, 1, 1, 0, 0, 0);
        var curTime = LocalDateTime.now();
        // 当前时间距离baseTime相隔的分钟数，26bit（127.6年）
        var hoursToBaseTime = (int)((curTime.toEpochSecond(ZoneOffset.UTC) - baseTime.toEpochSecond(ZoneOffset.UTC)) / 3600);
        // redis节点编号，9bit（512个）
        var nodeNum = 0;
        // 余下的28bit用来生成id，大约每毫秒4000个
        long idBase = (hoursToBaseTime << 37) + (nodeNum << 28);
        var tblKeyBase = ConstValues.RedisKeys.PrimaryKeyIdPrefix + tableName;
        var tblKey = String.format("%s_%d_%d", tblKeyBase, hoursToBaseTime, nodeNum);
        
        long maxId = currentPrimaryKeyMaxId.getOrDefault(tblKey, 0l);
        long finalPKId = currentPrimaryKeyId.getOrDefault(tblKey, new AtomicLong(0l)).incrementAndGet();
        if (finalPKId >= maxId) {
            synchronized (currentPrimaryKeyId) {
                maxId = currentPrimaryKeyMaxId.getOrDefault(tblKey, 0l);
                if (finalPKId >= maxId) {
                    maxId = JedisHelper.incrBy(tblKey, idsPerRequest);
                    JedisHelper.expire(tblKey, 60 * 2);
                    currentPrimaryKeyId.get(tblKey).set(maxId - idsPerRequest);
                    currentPrimaryKeyMaxId.put(tblKey, maxId);
                }
                finalPKId = currentPrimaryKeyId.get(tblKey).incrementAndGet();
                // 清理旧键值
                if (prevUsingKey.containsKey(tblKeyBase) && !prevUsingKey.get(tblKeyBase).equals(tblKey)) {
                    var oldKey = prevUsingKey.get(tblKeyBase);
                    currentPrimaryKeyId.remove(oldKey);
                    currentPrimaryKeyMaxId.remove(oldKey);
                }
                prevUsingKey.put(tblKeyBase, tblKey);
            }
        }
        return idBase + finalPKId;
    }
}