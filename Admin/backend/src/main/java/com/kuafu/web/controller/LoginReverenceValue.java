package com.kuafuweb.web.controller;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public enum LoginReverenceValue {
generalOrders("generalOrders","order_info_all.get_general_orders_list"),role("role","role.get_select_list"),live_stream_sync("live_stream_sync","live_stream_sync.get_select_list"),product_info("product_info","product_info.get_select_list"),product_status_enum("product_status_enum","product_status_enum.get_select_list"),business_card_ocr("business_card_ocr","business_card_ocr.get_select_list"),order_status_enum("order_status_enum","order_status_enum.get_select_list"),user_role("user_role","user_role.get_select_list"),user_info("user_info","user_info.get_select_list"),transfer_status_enum("transfer_status_enum","transfer_status_enum.get_select_list"),warehouse_info("warehouse_info","warehouse_info.get_select_list"),permission_log("permission_log","permission_log.get_select_list"),customer_info("customer_info","customer_info.get_select_list"),inventory_record("inventory_record","inventory_record.get_select_list"),dynamic_api_setting("dynamic_api_setting","dynamic_api_setting.get_select_list"),customer_tag_enum("customer_tag_enum","customer_tag_enum.get_select_list"),purchase_status_enum("purchase_status_enum","purchase_status_enum.get_select_list"),profit_board("profit_board","profit_board.get_select_list"),rfm_level_enum("rfm_level_enum","rfm_level_enum.get_select_list"),permission("permission","permission.get_select_list"),ai_assistant_log("ai_assistant_log","ai_assistant_log.get_select_list"),delayed_tasks("delayed_tasks","delayed_tasks.get_select_list"),order_info("order_info","order_info.get_select_list"),general_orders("general_orders","general_orders.get_select_list"),transfer_info("transfer_info","transfer_info.get_select_list"),system_config("system_config","system_config.get_select_list"),procurement_suggestion("procurement_suggestion","procurement_suggestion.get_select_list"),fission_tool("fission_tool","fission_tool.get_select_list"),order_item("order_item","order_item.get_select_list"),role_permission("role_permission","role_permission.get_select_list"),business_opportunity("business_opportunity","business_opportunity.get_select_list"),ad_recommend_info("ad_recommend_info","ad_recommend_info.get_select_list"),purchase_info("purchase_info","purchase_info.get_select_list"),;
    private String label;
    private String value;


    LoginReverenceValue(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public static String getValue(String label) {
        for (LoginReverenceValue item : values()) {
            if (StringUtils.equalsIgnoreCase(item.getLabel(), label)) {
                return item.getValue();
            }
        }
        return null;
    }
}





