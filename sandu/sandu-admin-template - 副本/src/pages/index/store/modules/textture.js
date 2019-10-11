import state from "./product/state";

const textture = {
    state:{
        fileLoading:false,
        percentage:0,    //上传文件进度条
        texttureName:'', //材质名称
        interval:'',
        interval1:'',
        isStart:false //是否完成上传文件
    },
    getters:{
        gettersFileLoading:state=>state.fileLoading,
        gettersPercentage:state=>state.percentage,
        gettersTexttureName:state=>state.texttureName,
    },
    mutations: {
        SET_CLOSEMD:(state,data)=>{
            state.fileLoading=data.isshow;
            if(data.isshow){
               state.percentage=0;
               state.interval=setInterval(()=>{
                   if(state.percentage>=100){
                       clearInterval(state.interval);
                       state.fileLoading=false;
                       state.percentage=0;
                       data.vm.$message.success(data.tip || '批量上传贴图成功');
                   }else {
                       if(state.isStart){ //文件已经上传完成
                           state.percentage=state.percentage+1;
                       }else{ //还在上传中
                           if(state.percentage>=80){ //暂停...
                               clearInterval(state.interval);
                               state.interval1=setInterval(()=>{
                                   if(state.isStart){
                                       if(state.percentage>=100){
                                           data.vm.$message.success(data.tip || '批量上传贴图成功');
                                           state.fileLoading=false;
                                           state.percentage=0;
                                           clearInterval(state.interval1)
                                       }else {
                                           state.percentage=state.percentage+1;
                                       }
                                   }
                               },500)
                           }else {
                               state.percentage=state.percentage+1;
                           }

                       }

                   }

               },50)
            }else {
                state.fileLoading=false;
                clearInterval(state.interval);
                clearInterval(state.interval1);
            }
        },
        SET_OTHER_INTERVAL:(state,data)=>{
           let t=setInterval(()=>{
                if(state.isStart){
                    if(state.percentage>=100){
                        data.vm.$message.success(data.tip || '批量上传贴图成功');
                        state.percentage=0;
                        clearInterval(t)
                    }else {
                        state.percentage=state.percentage+1;
                    }
                }
            },500)
        },
        SET_TXETTRUE_NAME:(state,data)=>{
            state.texttureName=data;
        },
        SET_TXETTRUE_STATE:(state,data)=>{
            state.isStart=data;
        }
     },
    actions:{
        fileLoadingDialog({ commit },data){  //弹窗改变
            commit('SET_CLOSEMD',data)
        },
        texttureName({ commit },data){
            commit('SET_TXETTRUE_NAME',data);
        },
        texttureIsstart({ commit },data){
            commit('SET_TXETTRUE_STATE',data);
        }
    }
}
export default textture;