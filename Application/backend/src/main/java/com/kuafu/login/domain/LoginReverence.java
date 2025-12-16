package com.kuafu.login.domain;

import com.kuafu.login.domain.SelectVo;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum LoginReverence {

            systemConfig("系统配置","systemConfig"),
        delayedTasks("延迟任务","delayedTasks"),
        generalOrders("系统订单信息","generalOrders"),
        userInfo("用户信息","userInfo"),
        productInfo("商品信息","productInfo"),
        productStatusEnum("商品状态枚举","productStatusEnum"),
        inventoryRecord("库存记录","inventoryRecord"),
        warehouseInfo("仓库信息","warehouseInfo"),
        customerInfo("客户信息","customerInfo"),
        rfmLevelEnum("RFM分层枚举","rfmLevelEnum"),
        customerTagEnum("客户标签枚举","customerTagEnum"),
        orderInfo("订单信息","orderInfo"),
        orderStatusEnum("订单状态枚举","orderStatusEnum"),
        orderItem("订单商品明细","orderItem"),
        purchaseInfo("采购信息","purchaseInfo"),
        purchaseStatusEnum("采购状态枚举","purchaseStatusEnum"),
        transferInfo("调拨信息","transferInfo"),
        transferStatusEnum("调拨状态枚举","transferStatusEnum"),
        businessOpportunity("商机线索","businessOpportunity"),
        aiAssistantLog("AI助手日志","aiAssistantLog"),
        adRecommendInfo("广告推荐","adRecommendInfo"),
        profitBoard("利润看板","profitBoard"),
        procurementSuggestion("采购建议","procurementSuggestion"),
        businessCardOcr("名片OCR建档","businessCardOcr"),
        fissionTool("裂变工具","fissionTool"),
        liveStreamSync("直播带货同步","liveStreamSync"),
        role("角色","role"),
        permission("权限","permission"),
        rolePermission("角色权限关联","rolePermission"),
        userRole("用户角色关联","userRole"),
        permissionLog("权限变更日志","permissionLog"),
    ;


    private String label;
    private String value;

    LoginReverence(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    public static List<SelectVo> all() {
        final LoginReverence[] values = LoginReverence.values();
        return Arrays.stream(values).map(r -> {
            return new SelectVo(r.getValue(), r.getLabel(),r.getLabel());
        }).collect(Collectors.toList());
    }
}
