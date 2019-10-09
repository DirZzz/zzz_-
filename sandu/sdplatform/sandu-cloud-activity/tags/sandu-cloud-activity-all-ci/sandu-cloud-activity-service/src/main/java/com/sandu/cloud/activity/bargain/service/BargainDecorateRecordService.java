package com.sandu.cloud.activity.bargain.service;

import com.sandu.cloud.activity.bargain.model.BargainDecorateRecord;


public interface BargainDecorateRecordService {

    /**
     * 插入
     *
     * @param decoraterecord
     * @return
     */
    void create(BargainDecorateRecord decorateRecord);

    /**
     * 更新
     *
     * @param decoraterecord
     * @return
     */
    int modifyById(BargainDecorateRecord decorateRecord);

    /**
     * 删除
     *
     * @param decoraterecordIds
     * @return
     */
    int remove(String decorateRecordId);

    /**
     * 通过ID获取详情
     *
     * @param decoraterecordId
     * @return
     */
     BargainDecorateRecord get(String decorateRecordId);

     
     BargainDecorateRecord getByRegId(String regId);
     

    
}
