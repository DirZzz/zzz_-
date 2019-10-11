import SockJS from "sockjs-client";
import Stomp from "stompjs";
//参考文档：https://www.jianshu.com/p/b8aa70bf1340
let socketMixins={
    data(){
        return{
            platform:'',
            // scsystemUrl:'',
            msg:{}
        }
    },
    created(){
      // let basePath = process.env;
      // this.scsystemUrl = basePath.scsystemUrl;
    },
    methods: {

      initWebSocket() {
        this.connection();
        let that = this;
        // 断开重连机制,尝试发送消息,捕获异常发生时重连
        this.timer = setInterval(() => {
          try {
            that.stompClient.send("test");
          } catch (err) {
            console.log("断线了: " + err);
            that.connection();
          }
        }, 5000);
      },
      connection() {
        // 建立连接对象
        console.log(this.scsystemUrl);
        //let socket = new SockJS(`http://systemdev.sanduspace.com/pay/socket?userId=${sessionStorage.getItem('userId')}`);
        let socket = new SockJS(
          `${process.env.scsystemUrl}/pay/socket?userId=${sessionStorage.getItem('userId')}`
          // `https://scsystemtest.sanduspace.com:34003/pay/socket?userId=${sessionStorage.getItem('userId')}`
        );
        // 获取STOMP子协议的客户端对象
        this.stompClient = Stomp.over(socket);
        // 定义客户端的认证信息,按需求配置
        let headers = {
          Authorization: ""
        };
        // 向服务器发起websocket连接
        this.stompClient.connect(
          headers,
          () => {
            this.stompClient.subscribe(
              "/user/notify/buy/activityUser",
              msg => {
                // 订阅服务端提供的某个topic
                console.log("成功");
                console.log("msg",msg); // msg.body存放的是服务端发送给我们的信息
                this.msg = JSON.parse(msg.body);
                // 支付结果，用于transactionRecord和purchasable页面
                if(this.msg.orderStatus == 'SUCCESS'){
                  this.paymentDialog = false;
                  switch(this.pageName){
                    case 'transactionRecord': this.$message.success('支付成功！');this.getOrderList();break;
                    case 'purchasable':this.payResultDialog = true;break;
                  }
                } else {
                  this.paymentDialog = false;
                  switch(this.pageName){
                    case 'transactionRecord': this.$message.warning('支付失败！');break;
                    case 'purchasable':this.payFailDialog = true;break;
                  }   
                }
                if(this.msg.orderStatus == 'CLOSE'){
                  this.paymentDialog = false;
                  this.$message.warning('支付码已过期，订单已关闭！请重新购买');
                }
              },
              headers
            );
          },
          err => {
            // 连接发生错误时的处理函数
            console.log("失败");
            console.log(err);
          }
        );
      },
      disconnect() {
        if (this.stompClient) {
          this.stompClient.disconnect();
          clearInterval(this.timer);
        }
      }, // 断开连接
    }
}
export default socketMixins