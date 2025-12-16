package com.kuafu.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuafu.web.entity.ProcurementInfo;
import com.kuafu.web.mapper.ProcurementInfoMapper;
import com.kuafu.web.service.ITenderNoticeParserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 招标公告智能解析器实现类
 * 使用正则表达式和关键词匹配提取关键信息
 */
@Slf4j
@Service
public class TenderNoticeParserServiceImpl implements ITenderNoticeParserService {

    @Autowired
    private ProcurementInfoMapper procurementInfoMapper;

    // 预算金额正则表达式模式
    private static final Pattern[] BUDGET_PATTERNS = {
        Pattern.compile("预算金额[：:]\\s*([0-9,]+)\\s*元"),
        Pattern.compile("预算[：:]\\s*([0-9,]+)\\s*元"),
        Pattern.compile("([0-9,]+)\\s*万元"),
        Pattern.compile("¥\\s*([0-9,]+)"),
        Pattern.compile("人民币\\s*([0-9,]+)\\s*元")
    };

    // 日期正则表达式模式
    private static final Pattern[] DATE_PATTERNS = {
        Pattern.compile("截止时间[：:]\\s*(\\d{4}年\\d{1,2}月\\d{1,2}日\\d{1,2}时\\d{1,2}分)"),
        Pattern.compile("投标截止时间[：:]\\s*(\\d{4}年\\d{1,2}月\\d{1,2}日)"),
        Pattern.compile("(\\d{4}年\\d{1,2}月\\d{1,2}日)\\s*\\d{1,2}[:：]\\d{1,2}"),
        Pattern.compile("(\\d{4}-\\d{1,2}-\\d{1,2})"),
        Pattern.compile("(\\d{4}/\\d{1,2}/\\d{1,2})")
    };

    // 采购类型关键词映射
    private static final Map<String, String> PROCUREMENT_TYPE_KEYWORDS = new HashMap<>();
    static {
        PROCUREMENT_TYPE_KEYWORDS.put("货物", "货物采购");
        PROCUREMENT_TYPE_KEYWORDS.put("服务", "服务采购");
        PROCUREMENT_TYPE_KEYWORDS.put("工程", "工程采购");
        PROCUREMENT_TYPE_KEYWORDS.put("设备", "设备采购");
        PROCUREMENT_TYPE_KEYWORDS.put("软件", "软件采购");
        PROCUREMENT_TYPE_KEYWORDS.put("咨询", "咨询服务");
        PROCUREMENT_TYPE_KEYWORDS.put("设计", "设计服务");
        PROCUREMENT_TYPE_KEYWORDS.put("施工", "施工服务");
        PROCUREMENT_TYPE_KEYWORDS.put("监理", "监理服务");
        PROCUREMENT_TYPE_KEYWORDS.put("审计", "审计服务");
    }

    // 地区关键词
    private static final String[] REGION_KEYWORDS = {
        "北京", "上海", "天津", "重庆", "河北", "山西", "辽宁", "吉林", "黑龙江",
        "江苏", "浙江", "安徽", "福建", "江西", "山东", "河南", "湖北", "湖南",
        "广东", "海南", "四川", "贵州", "云南", "陕西", "甘肃", "青海", "台湾",
        "内蒙古", "广西", "西藏", "宁夏", "新疆", "香港", "澳门",
        "石家庄市", "唐山市", "秦皇岛市", "邯郸市", "邢台市", "保定市", "张家口市", "承德市", "沧州市", "廊坊市", "衡水市"
    };

    @Override
    public ProcurementInfo parseTenderNotice(String title, String content, String sourceUrl) {
        log.info("开始解析招标公告: {}", title);
        
        ProcurementInfo info = new ProcurementInfo();
        info.setTitle(title);
        info.setContent(content);
        info.setSourceUrl(sourceUrl);
        info.setSourceType("招标公告");
        info.setStatus("parsed");
        info.setCreateTime(new Date());
        info.setUpdateTime(new Date());

        try {
            // 提取各项信息
            info.setBudget(extractBudget(content));
            info.setDeadline(extractDeadline(content));
            info.setProcurementType(extractProcurementType(title, content));
            info.setRegion(extractRegion(content));
            
            Map<String, String> contactInfo = extractContactInfo(content);
            info.setContactName(contactInfo.get("name"));
            info.setContactPhone(contactInfo.get("phone"));
            info.setContactEmail(contactInfo.get("email"));

            // 验证解析结果
            if (validateProcurementInfo(info)) {
                info.setStatus("validated");
                log.info("招标公告解析成功: {}", title);
            } else {
                info.setStatus("invalid");
                log.warn("招标公告解析验证失败: {}", title);
            }

        } catch (Exception e) {
            log.error("解析招标公告失败: {}", title, e);
            info.setStatus("parse_error");
        }

        return info;
    }

    @Override
    public List<ProcurementInfo> parseTenderNoticesBatch(List<Map<String, String>> tenderNotices) {
        return tenderNotices.stream()
            .map(notice -> parseTenderNotice(
                notice.get("title"), 
                notice.get("content"), 
                notice.get("url")
            ))
            .collect(Collectors.toList());
    }

    @Override
    public Long extractBudget(String content) {
        if (content == null) return null;
        
        for (Pattern pattern : BUDGET_PATTERNS) {
            Matcher matcher = pattern.matcher(content);
            if (matcher.find()) {
                String amountStr = matcher.group(1).replace(",", "");
                try {
                    // 检查是否为万元
                    if (pattern.pattern().contains("万元")) {
                        return Long.parseLong(amountStr) * 10000;
                    }
                    return Long.parseLong(amountStr);
                } catch (NumberFormatException e) {
                    log.warn("预算金额转换失败: {}", amountStr);
                }
            }
        }
        return null;
    }

    @Override
    public Long extractDeadline(String content) {
        if (content == null) return null;
        
        for (Pattern pattern : DATE_PATTERNS) {
            Matcher matcher = pattern.matcher(content);
            if (matcher.find()) {
                String dateStr = matcher.group(1);
                try {
                    Date date = parseChineseDate(dateStr);
                    return date != null ? date.getTime() : null;
                } catch (Exception e) {
                    log.warn("日期解析失败: {}", dateStr);
                }
            }
        }
        return null;
    }

    @Override
    public String extractProcurementType(String title, String content) {
        String combinedText = (title + " " + content).toLowerCase();
        
        for (Map.Entry<String, String> entry : PROCUREMENT_TYPE_KEYWORDS.entrySet()) {
            if (combinedText.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return "其他采购";
    }

    @Override
    public String extractRegion(String content) {
        if (content == null) return null;
        
        for (String region : REGION_KEYWORDS) {
            if (content.contains(region)) {
                return region;
            }
        }
        return "全国";
    }

    @Override
    public Map<String, String> extractContactInfo(String content) {
        Map<String, String> contactInfo = new HashMap<>();
        
        // 提取联系人姓名
        Pattern namePattern = Pattern.compile("联系人[：:]\\s*([^\\s，。,\\(（]+)");
        Matcher nameMatcher = namePattern.matcher(content);
        if (nameMatcher.find()) {
            contactInfo.put("name", nameMatcher.group(1).trim());
        }

        // 提取电话号码
        Pattern phonePattern = Pattern.compile("(\\d{3,4}[-]?\\d{7,8}|\\d{11})");
        Matcher phoneMatcher = phonePattern.matcher(content);
        if (phoneMatcher.find()) {
            contactInfo.put("phone", phoneMatcher.group(1));
        }

        // 提取邮箱
        Pattern emailPattern = Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b");
        Matcher emailMatcher = emailPattern.matcher(content);
        if (emailMatcher.find()) {
            contactInfo.put("email", emailMatcher.group());
        }

        return contactInfo;
    }

    @Override
    public boolean validateProcurementInfo(ProcurementInfo procurementInfo) {
        // 基本验证规则
        if (procurementInfo.getTitle() == null || procurementInfo.getTitle().trim().isEmpty()) {
            return false;
        }
        
        if (procurementInfo.getContent() == null || procurementInfo.getContent().trim().isEmpty()) {
            return false;
        }

        // 预算金额验证（可选字段，但如果存在必须合理）
        if (procurementInfo.getBudget() != null && procurementInfo.getBudget() < 0) {
            return false;
        }

        // 截止日期验证（必须大于当前时间）
        if (procurementInfo.getDeadline() != null && procurementInfo.getDeadline() < System.currentTimeMillis()) {
            return false;
        }

        return true;
    }

    @Override
    public Map<String, Object> getParserStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        // 总解析数量
        QueryWrapper<ProcurementInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("source_type", "招标公告");
        int totalParsed = procurementInfoMapper.selectCount(wrapper);
        statistics.put("totalParsed", totalParsed);

        // 今日解析数量
        wrapper = new QueryWrapper<>();
        wrapper.eq("source_type", "招标公告");
        wrapper.likeRight("create_time", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        int todayParsed = procurementInfoMapper.selectCount(wrapper);
        statistics.put("todayParsed", todayParsed);

        // 验证成功率
        wrapper = new QueryWrapper<>();
        wrapper.eq("source_type", "招标公告");
        wrapper.eq("status", "validated");
        int validatedCount = procurementInfoMapper.selectCount(wrapper);
        
        double successRate = totalParsed > 0 ? (double) validatedCount / totalParsed * 100 : 0;
        statistics.put("validationSuccessRate", String.format("%.2f%%", successRate));

        return statistics;
    }

    /**
     * 解析中文日期字符串
     */
    private Date parseChineseDate(String dateStr) {
        if (dateStr == null) return null;
        
        // 移除中文年月日
        dateStr = dateStr.replace("年", "-").replace("月", "-").replace("日", " ");
        
        SimpleDateFormat[] formats = {
            new SimpleDateFormat("yyyy-MM-dd HH:mm"),
            new SimpleDateFormat("yyyy-MM-dd"),
            new SimpleDateFormat("yyyy/MM/dd"),
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        };

        for (SimpleDateFormat format : formats) {
            try {
                return format.parse(dateStr);
            } catch (ParseException e) {
                // 尝试下一个格式
            }
        }
        
        return null;
    }
}