//全局过滤器
const vfilter={
    tofix(val){
        if(val){
            return parseFloat(val).toFixed(2)
        }
    },
    toAreas(val){
        console.log(val);
        this.API.areaList({areaCode:val}).then(res=>{
            res.datalist.map(v=>{
                if(val==v.areaCode)
                    return v.areaNmae;
            })
        })
    },
};
export default vfilter
