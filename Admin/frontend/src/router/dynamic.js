export const dynamicRoutes = [{
        path: '/',
        meta: {
            title: '首页',
            isParentView: false
        },
        component: ()=> import('@/views/dashboard_statistic/index.vue'),
    }, {
        path: '/user_info',
        meta: {
            title: '用户信息',
            shownot: false
        },
        component: ()=> import('@/views/user_info/index.vue'),
    }, {
        path: '/product_info',
        meta: {
            title: '商品信息',
            shownot: false
        },
        component: ()=> import('@/views/product_info/index.vue'),
    }, {
        path: '/product_status_enum',
        meta: {
            title: '商品状态枚举',
            shownot: false
        },
        component: ()=> import('@/views/product_status_enum/index.vue'),
    }, {
        path: '/inventory_record',
        meta: {
            title: '库存记录',
            shownot: false
        },
        component: ()=> import('@/views/inventory_record/index.vue'),
    }, {
        path: '/warehouse_info',
        meta: {
            title: '仓库信息',
            shownot: false
        },
        component: ()=> import('@/views/warehouse_info/index.vue'),
    }, {
        path: '/customer_info',
        meta: {
            title: '客户信息',
            shownot: false
        },
        component: ()=> import('@/views/customer_info/index.vue'),
    }, {
        path: '/rfm_level_enum',
        meta: {
            title: 'RFM分层枚举',
            shownot: false
        },
        component: ()=> import('@/views/rfm_level_enum/index.vue'),
    }, {
        path: '/customer_tag_enum',
        meta: {
            title: '客户标签枚举',
            shownot: false
        },
        component: ()=> import('@/views/customer_tag_enum/index.vue'),
    }, {
        path: '/order_info',
        meta: {
            title: '订单信息',
            shownot: false
        },
        component: ()=> import('@/views/order_info/index.vue'),
    }, {
        path: '/order_status_enum',
        meta: {
            title: '订单状态枚举',
            shownot: false
        },
        component: ()=> import('@/views/order_status_enum/index.vue'),
    }, {
        path: '/order_item',
        meta: {
            title: '订单商品明细',
            shownot: false
        },
        component: ()=> import('@/views/order_item/index.vue'),
    }, {
        path: '/purchase_info',
        meta: {
            title: '采购信息',
            shownot: false
        },
        component: ()=> import('@/views/purchase_info/index.vue'),
    }, {
        path: '/purchase_status_enum',
        meta: {
            title: '采购状态枚举',
            shownot: false
        },
        component: ()=> import('@/views/purchase_status_enum/index.vue'),
    }, {
        path: '/transfer_info',
        meta: {
            title: '调拨信息',
            shownot: false
        },
        component: ()=> import('@/views/transfer_info/index.vue'),
    }, {
        path: '/transfer_status_enum',
        meta: {
            title: '调拨状态枚举',
            shownot: false
        },
        component: ()=> import('@/views/transfer_status_enum/index.vue'),
    }, {
        path: '/business_opportunity',
        meta: {
            title: '商机线索',
            shownot: false
        },
        component: ()=> import('@/views/business_opportunity/index.vue'),
    }, {
        path: '/ai_assistant_log',
        meta: {
            title: 'AI助手日志',
            shownot: false
        },
        component: ()=> import('@/views/ai_assistant_log/index.vue'),
    }, {
        path: '/ad_recommend_info',
        meta: {
            title: '广告推荐',
            shownot: false
        },
        component: ()=> import('@/views/ad_recommend_info/index.vue'),
    }, {
        path: '/profit_board',
        meta: {
            title: '利润看板',
            shownot: false
        },
        component: ()=> import('@/views/profit_board/index.vue'),
    }, {
        path: '/procurement_suggestion',
        meta: {
            title: '采购建议',
            shownot: false
        },
        component: ()=> import('@/views/procurement_suggestion/index.vue'),
    }, {        path: '/business_card_ocr',        meta: {            title: '名片OCR建档',            shownot: false        },        component: ()=> import('@/views/business_card_ocr/index.vue'),    }, {        path: '/image_search',        meta: {            title: '图片全网信息获取',            shownot: false        },        component: ()=> import('@/views/image_search/index.vue'),    }, {
        path: '/fission_tool',
        meta: {
            title: '裂变工具',
            shownot: false
        },
        component: ()=> import('@/views/fission_tool/index.vue'),
    }, {
        path: '/live_stream_sync',
        meta: {
            title: '直播带货同步',
            shownot: false
        },
        component: ()=> import('@/views/live_stream_sync/index.vue'),
    }, {
        path: '/role',
        meta: {
            title: '角色',
            shownot: false
        },
        component: ()=> import('@/views/role/index.vue'),
    }, {
        path: '/permission',
        meta: {
            title: '权限',
            shownot: false
        },
        component: ()=> import('@/views/permission/index.vue'),
    }, {
        path: '/role_permission',
        meta: {
            title: '角色权限关联',
            shownot: false
        },
        component: ()=> import('@/views/role_permission/index.vue'),
    }, {
        path: '/user_role',
        meta: {
            title: '用户角色关联',
            shownot: false
        },
        component: ()=> import('@/views/user_role/index.vue'),
    }, {
        path: '/permission_log',
        meta: {
            title: '权限变更日志',
            shownot: false
        },
        component: ()=> import('@/views/permission_log/index.vue'),
    }, {
        path: '/dynamic_api_setting',
        meta: {
            title: 'API配置',
            shownot: false
        },
        component: ()=> import('@/views/dynamic_api_setting/index.vue'),
    }, {
        path: '/user_info/detail',
        meta: {
            title: '用户信息详情',
            shownot: true
        },
        component: ()=> import('@/views/user_info/detail/index.vue'),
    }, {
        path: '/product_info/detail',
        meta: {
            title: '商品信息详情',
            shownot: true
        },
        component: ()=> import('@/views/product_info/detail/index.vue'),
    }, {
        path: '/product_status_enum/detail',
        meta: {
            title: '商品状态枚举详情',
            shownot: true
        },
        component: ()=> import('@/views/product_status_enum/detail/index.vue'),
    }, {
        path: '/inventory_record/detail',
        meta: {
            title: '库存记录详情',
            shownot: true
        },
        component: ()=> import('@/views/inventory_record/detail/index.vue'),
    }, {
        path: '/warehouse_info/detail',
        meta: {
            title: '仓库信息详情',
            shownot: true
        },
        component: ()=> import('@/views/warehouse_info/detail/index.vue'),
    }, {
        path: '/customer_info/detail',
        meta: {
            title: '客户信息详情',
            shownot: true
        },
        component: ()=> import('@/views/customer_info/detail/index.vue'),
    }, {
        path: '/rfm_level_enum/detail',
        meta: {
            title: 'RFM分层枚举详情',
            shownot: true
        },
        component: ()=> import('@/views/rfm_level_enum/detail/index.vue'),
    }, {
        path: '/customer_tag_enum/detail',
        meta: {
            title: '客户标签枚举详情',
            shownot: true
        },
        component: ()=> import('@/views/customer_tag_enum/detail/index.vue'),
    }, {
        path: '/order_info/detail',
        meta: {
            title: '订单信息详情',
            shownot: true
        },
        component: ()=> import('@/views/order_info/detail/index.vue'),
    }, {
        path: '/order_status_enum/detail',
        meta: {
            title: '订单状态枚举详情',
            shownot: true
        },
        component: ()=> import('@/views/order_status_enum/detail/index.vue'),
    }, {
        path: '/order_item/detail',
        meta: {
            title: '订单商品明细详情',
            shownot: true
        },
        component: ()=> import('@/views/order_item/detail/index.vue'),
    }, {
        path: '/purchase_info/detail',
        meta: {
            title: '采购信息详情',
            shownot: true
        },
        component: ()=> import('@/views/purchase_info/detail/index.vue'),
    }, {
        path: '/purchase_status_enum/detail',
        meta: {
            title: '采购状态枚举详情',
            shownot: true
        },
        component: ()=> import('@/views/purchase_status_enum/detail/index.vue'),
    }, {
        path: '/transfer_info/detail',
        meta: {
            title: '调拨信息详情',
            shownot: true
        },
        component: ()=> import('@/views/transfer_info/detail/index.vue'),
    }, {
        path: '/transfer_status_enum/detail',
        meta: {
            title: '调拨状态枚举详情',
            shownot: true
        },
        component: ()=> import('@/views/transfer_status_enum/detail/index.vue'),
    }, {
        path: '/business_opportunity/detail',
        meta: {
            title: '商机线索详情',
            shownot: true
        },
        component: ()=> import('@/views/business_opportunity/detail/index.vue'),
    }, {
        path: '/ai_assistant_log/detail',
        meta: {
            title: 'AI助手日志详情',
            shownot: true
        },
        component: ()=> import('@/views/ai_assistant_log/detail/index.vue'),
    }, {
        path: '/ad_recommend_info/detail',
        meta: {
            title: '广告推荐详情',
            shownot: true
        },
        component: ()=> import('@/views/ad_recommend_info/detail/index.vue'),
    }, {
        path: '/profit_board/detail',
        meta: {
            title: '利润看板详情',
            shownot: true
        },
        component: ()=> import('@/views/profit_board/detail/index.vue'),
    }, {
        path: '/procurement_suggestion/detail',
        meta: {
            title: '采购建议详情',
            shownot: true
        },
        component: ()=> import('@/views/procurement_suggestion/detail/index.vue'),
    }, {
        path: '/business_card_ocr/detail',
        meta: {
            title: '名片OCR建档详情',
            shownot: true
        },
        component: ()=> import('@/views/business_card_ocr/detail/index.vue'),
    }, {
        path: '/fission_tool/detail',
        meta: {
            title: '裂变工具详情',
            shownot: true
        },
        component: ()=> import('@/views/fission_tool/detail/index.vue'),
    }, {
        path: '/live_stream_sync/detail',
        meta: {
            title: '直播带货同步详情',
            shownot: true
        },
        component: ()=> import('@/views/live_stream_sync/detail/index.vue'),
    }, {
        path: '/role/detail',
        meta: {
            title: '角色详情',
            shownot: true
        },
        component: ()=> import('@/views/role/detail/index.vue'),
    }, {
        path: '/permission/detail',
        meta: {
            title: '权限详情',
            shownot: true
        },
        component: ()=> import('@/views/permission/detail/index.vue'),
    }, {
        path: '/role_permission/detail',
        meta: {
            title: '角色权限关联详情',
            shownot: true
        },
        component: ()=> import('@/views/role_permission/detail/index.vue'),
    }, {
        path: '/user_role/detail',
        meta: {
            title: '用户角色关联详情',
            shownot: true
        },
        component: ()=> import('@/views/user_role/detail/index.vue'),
    }, {
        path: '/permission_log/detail',
        meta: {
            title: '权限变更日志详情',
            shownot: true
        },
        component: ()=> import('@/views/permission_log/detail/index.vue'),
    }, {
        path: '/dynamic_api_setting/detail',
        meta: {
            title: 'API配置详情',
            shownot: true
        },
        component: ()=> import('@/views/dynamic_api_setting/detail/index.vue'),
    }, {
        path: '/user',
        meta: {
            title: '用户管理',
            shownot: false
        },
        component: ()=> import('@/views/user/index.vue'),
    }, {
        path: '/customer',
        meta: {
            title: '客户管理',
            shownot: false
        },
        component: ()=> import('@/views/customer/index.vue'),
    }, {
        path: '/customer/detail/:id',
        meta: {
            title: '客户详情',
            shownot: true
        },
        component: ()=> import('@/views/customer/detail/index.vue'),
    }, {
        path: '/customer/edit/:id',
        meta: {
            title: '编辑客户',
            shownot: true
        },
        component: ()=> import('@/views/customer/edit/index.vue'),
    }, {
        path: '/report',
        meta: {
            title: '报表分析',
            shownot: false
        },
        component: ()=> import('@/views/report/index.vue'),
    }, {
        path: '/config',
        meta: {
            title: '系统配置',
            shownot: false
        },
        component: ()=> import('@/views/config/index.vue'),
    }]