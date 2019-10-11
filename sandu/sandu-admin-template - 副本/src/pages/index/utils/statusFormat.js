var orderStatus={
    foemat:function(val){
        let statusMap = {
            0: '待付款',
            1: '待确认',
            2:'待发货',
            3:'关闭',
            4:'待签收',
            5:'完成'
        }
        return statusMap[val]
    }
}
//订单状态
export function formatOrderStatus(val){
    let statusMap = {
        0: '待付款',
        1: '待确认',
        2:'待发货',
        3:'关闭',
        4:'待签收',
        5:'完成'
    }
    return statusMap[val]
}

export function OrderStatusList(){
    return [
        {label:'全部',value:''},
        {label:'待付款',value:0},
        {label:'待确认',value:1},
        {label:'待发货',value:2},
        {label:'待签收',value:4},
        {label:'完成',value:5},
        {label:'关闭',value:3}
    ]    
}


//付款状态
export function formatOrderPaymentStatus(val){
    let statusMap = {
        0: '未支付',
        1: '已支付'
    }
    return statusMap[val]
}

//操作类型
export function formatCreatorType(val){
    let statusMap = {
        0: '用户',
        1: '客服'
    }
    return statusMap[val]
}

