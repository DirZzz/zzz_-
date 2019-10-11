import {fetch} from '../utils/request'
let basePath = process.env;
export default{
    // 礼品操作
    getGifts(params) {//获取礼品列表
        let url=basePath.systemUrl+'/v1/gift/list';
        return fetch(url,params,'get',true);
    },
    createGift(params){//新增礼品
        let url=basePath.systemUrl+'/v1/gift/create';
        return fetch(url,params,'post',true)
    },
    updateGift(params){//修改礼品
        let url=basePath.systemUrl+'/v1/gift';
        return fetch(url,params,'put',true)
    },
    updateGiftPutaway(params){//礼品上下架
        let url=basePath.systemUrl+'/v1/gift/updatePutaway';
        return fetch(url,params,'get',true)
    },
    deleteGiftImageById(params){//删除图片
        let url=basePath.systemUrl+'/v1/gift/deleteGiftImageById';
        return fetch(url,params,'get',true)
    },
    updateGiftCover(params){//设置封面
        let url=basePath.systemUrl+'/v1/gift/updateCover';
        return fetch(url,params,'get',true)
    },
    updateGiftSmall(params){//设置缩略图
        let url=basePath.systemUrl+'/v1/gift/updateSmall';
        return fetch(url,params,'get',true)
    },
    getGift(params){//获取礼品信息
        let url=basePath.systemUrl+'/v1/gift/getGift';
        return fetch(url,params,'get',true)
    },
    deleteGift(params){//删除礼品
        let url=basePath.systemUrl+'/v1/gift';
        return fetch(url,params,'delete',true)
    },
    //积分订单操作
    getImallOrderList(params){//获取积分订单列表
        let url=basePath.systemUrl+'/v1/imallOrder/list';
        return fetch(url,params,'get',true)
    },
    getImallOrder(params){//获取积分订单信息
        let url=basePath.systemUrl+'/v1/imallOrder/getImallOrder';
        return fetch(url,params,'get',true)
    },
    imallOrderAffirm(params){//积分订单确认
        let url=basePath.systemUrl+'/v1/imallOrder/affirm';
        return fetch(url,params,'get',true)
    },
    imallOrderRefundment(params){//积分订单退款
        let url=basePath.systemUrl+'/v1/imallOrder/refundment';
        return fetch(url,params,'get',true)
    },
    imallOrderSend(params){//积分订单发货
        let url=basePath.systemUrl+'/v1/imallOrder/send';
        return fetch(url,params,'get',true)
    },
    updateImallOrderAddress(params){//修改配送信息
        let url=basePath.systemUrl+'/v1/imallOrder/updateAddress';
        return fetch(url,params,'post',true)
    },
    imallOrderCollectStatusCount(params){//积分订单统计
        let url=basePath.systemUrl+'/v1/imallOrder/collectStatusCount';
        return fetch(url,params,'get',true)
    },
    deleteSlideById(params) {//幻灯片删除
        let url=basePath.systemUrl+'/v1/imallSlide/delete';
        return fetch(url,params,'get',true)
    },
    getSlideList(){//幻灯片查询
        let url=basePath.systemUrl+'/v1/imallSlide/list';
        return fetch(url,{type:0},'get',true)
    },
    completeOrder(params){
        let url=basePath.systemUrl+`/v1/imallOrder/complete`;
        return fetch(url,params,'get',true)
    }
}
