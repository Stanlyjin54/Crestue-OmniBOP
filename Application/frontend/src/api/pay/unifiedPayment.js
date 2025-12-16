import service from "@/utils/request";

const BASE_API = "/unifiedPayment/";

export default {
    /**
     * 创建统一支付订单
     * @param params
     * @returns {Promise<any>}
     */
    createPaymentOrder(params = {}) {
        return service({
            url: BASE_API + `create`,
            method: "post",
            data: params
        });
    },

    /**
     * 查询支付状态
     * @param paymentOrderId
     * @returns {Promise<any>}
     */
    checkPaymentStatus(paymentOrderId) {
        return service({
            url: BASE_API + `status/${paymentOrderId}`,
            method: "get"
        });
    },

    /**
     * 处理支付回调
     * @param params
     * @returns {Promise<any>}
     */
    handlePaymentCallback(params = {}) {
        return service({
            url: BASE_API + `callback`,
            method: "post",
            data: params
        });
    },

    /**
     * 获取支付二维码
     * @param params
     * @returns {Promise<any>}
     */
    getPaymentQrCode(params = {}) {
        return service({
            url: BASE_API + `qr-code`,
            method: "post",
            data: params
        });
    },

    /**
     * 申请退款
     * @param params
     * @returns {Promise<any>}
     */
    applyRefund(params = {}) {
        return service({
            url: BASE_API + `refund`,
            method: "post",
            data: params
        });
    },

    /**
     * 查询退款状态
     * @param refundOrderId
     * @returns {Promise<any>}
     */
    checkRefundStatus(refundOrderId) {
        return service({
            url: BASE_API + `refund-status/${refundOrderId}`,
            method: "get"
        });
    },

    /**
     * 获取可用的支付方式
     * @returns {Promise<any>}
     */
    getAvailablePaymentMethods() {
        return service({
            url: BASE_API + `methods`,
            method: "get"
        });
    }
}