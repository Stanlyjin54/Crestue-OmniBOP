# 批量修复包名错误的脚本
$files = @(
    "src/main/java/com/kuafu/web/service/IAdRecommendInfoAllService.java",
    "src/main/java/com/kuafu/web/service/IAdRecommendInfoService.java",
    "src/main/java/com/kuafu/web/service/IAiAssistantLogAllService.java",
    "src/main/java/com/kuafu/web/service/IAiAssistantLogService.java",
    "src/main/java/com/kuafu/web/service/IBusinessCardOcrAllService.java",
    "src/main/java/com/kuafu/web/service/IBusinessCardOcrService.java",
    "src/main/java/com/kuafu/web/service/IBusinessOpportunityAllService.java",
    "src/main/java/com/kuafu/web/service/IBusinessOpportunityMatcherService.java",
    "src/main/java/com/kuafu/web/service/IBusinessOpportunityService.java",
    "src/main/java/com/kuafu/web/service/IComplianceService.java",
    "src/main/java/com/kuafu/web/service/ICustomerInfoAllService.java",
    "src/main/java/com/kuafu/web/service/ICustomerInfoService.java",
    "src/main/java/com/kuafu/web/service/ICustomerTagEnumService.java",
    "src/main/java/com/kuafu/web/service/IDelayedTasksService.java",
    "src/main/java/com/kuafu/web/service/IFissionToolService.java",
    "src/main/java/com/kuafu/web/service/IGeneralOrdersService.java",
    "src/main/java/com/kuafu/web/service/IImageSearchService.java",
    "src/main/java/com/kuafu/web/service/IInventoryRecordAllService.java",
    "src/main/java/com/kuafu/web/service/ILiveStreamSyncService.java",
    "src/main/java/com/kuafu/web/service/IOpportunityReviewService.java",
    "src/main/java/com/kuafu/web/service/IOrderInfoAllService.java",
    "src/main/java/com/kuafu/web/service/IOrderInfoService.java",
    "src/main/java/com/kuafu/web/service/IOrderItemAllService.java",
    "src/main/java/com/kuafu/web/service/IOrderItemService.java",
    "src/main/java/com/kuafu/web/service/IOrderStatusEnumService.java",
    "src/main/java/com/kuafu/web/service/IPermissionLogAllService.java",
    "src/main/java/com/kuafu/web/service/IPermissionService.java",
    "src/main/java/com/kuafu/web/service/IProcurementCrawlerService.java",
    "src/main/java/com/kuafu/web/service/IProcurementSuggestionAllService.java",
    "src/main/java/com/kuafu/web/service/IProcurementSuggestionService.java",
    "src/main/java/com/kuafu/web/service/IProductInfoAllService.java",
    "src/main/java/com/kuafu/web/service/IProductInfoService.java",
    "src/main/java/com/kuafu/web/service/IProductStatusEnumService.java",
    "src/main/java/com/kuafu/web/service/IProfitBoardService.java",
    "src/main/java/com/kuafu/web/service/IPurchaseInfoAllService.java",
    "src/main/java/com/kuafu/web/service/IPurchaseInfoService.java",
    "src/main/java/com/kuafu/web/service/IPurchaseStatusEnumService.java",
    "src/main/java/com/kuafu/web/service/IRfmLevelEnumService.java",
    "src/main/java/com/kuafu/web/service/IRolePermissionAllService.java",
    "src/main/java/com/kuafu/web/service/IRolePermissionService.java",
    "src/main/java/com/kuafu/web/service/IRoleService.java",
    "src/main/java/com/kuafu/web/service/ISocialMediaComplianceService.java",
    "src/main/java/com/kuafu/web/service/ISocialMediaOpportunityService.java",
    "src/main/java/com/kuafu/web/service/ISystemConfigService.java",
    "src/main/java/com/kuafu/web/service/ITenderNoticeParserService.java",
    "src/main/java/com/kuafu/web/service/ITransferInfoAllService.java",
    "src/main/java/com/kuafu/web/service/ITransferInfoService.java",
    "src/main/java/com/kuafu/web/service/ITransferStatusEnumService.java",
    "src/main/java/com/kuafu/web/service/IUserInfoAllService.java",
    "src/main/java/com/kuafu/web/service/IUserInfoService.java",
    "src/main/java/com/kuafu/web/service/IUserRoleAllService.java",
    "src/main/java/com/kuafu/web/service/IUserRoleService.java",
    "src/main/java/com/kuafu/web/service/IWarehouseInfoService.java"
)

foreach ($file in $files) {
    if (Test-Path $file) {
        Write-Host "Processing $file..."
        (Get-Content $file) -replace 'package com\.kuafuweb\.web\.service', 'package com.kuafu.web.service' -replace 'import com\.kuafuweb\.', 'import com.kuafu.' | Set-Content $file
    } else {
        Write-Host "File not found: $file"
    }
}

Write-Host "Done!"