export default {
    /**
     * 下载并保存文件
     * @param param
     * @returns {Promise<unknown>}
     */
    downloadFile(param = {}) {
        uni.showLoading()
        return new Promise((resolve, reject) => {
            if (!param.url) {
                reject(new Error('请提供下载地址'));
            }
            // #ifdef MP-WEIXIN
            uni.downloadFile({
                url: param.url,         // 下载链接，必传
                header: param.header || {},
                timeout: param.timeout || 30000,
                success: (res) => {
                    if (res.statusCode === 200) {
                        // 下载文件
                        uni.saveFile({
                            tempFilePath:res.tempFilePath, // 传入 uni.downloadFile 返回的临时路径
                            success: (res) => {
                                // res.savedFilePath 是本地永久保存路径
                                resolve({
                                    success: true,
                                    // savedFilePath: res.savedFilePath
                                });
                            },
                            fail: (err) => {
                                reject(err);
                            }
                        });

                    } else {
                        reject(new Error(`下载失败，状态码：${res.statusCode}`));
                    }
                },
                fail: (err) => {
                    reject(err);
                }, complete: () => {
                    uni.hideLoading()
                }
            });
            // #endif



            // #ifdef H5
            // 如果是一个普通链接（直接下载）
            try {
                const a = document.createElement('a');
                a.href = param.url;
                a.target = '_blank';
                a.click();
                resolve({ success: true });
            }catch (e){
                reject(new Error(e));
            }finally {
                uni.hideLoading()
            }
            // #endif

        });
    },


    // /**
    //  * 保存文件
    //  * @param param
    //  * @returns {Promise<unknown>}
    //  */
    // saveFileToLocal(param = {}) {
    //
    //     const {tempFilePath} = param
    //     return new Promise((resolve, reject) => {
    //         // #ifdef MP-WEIXIN
    //         if (!tempFilePath) {
    //             reject(new Error('tempFilePath 不能为空'));
    //             return;
    //         }
    //
    //         // #endif
    //     });
    // }
}