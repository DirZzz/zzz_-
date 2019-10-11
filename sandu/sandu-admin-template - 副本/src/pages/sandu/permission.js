import qs from 'qs'
export default function permissions(per) {
    if(per){
        if(qs.parse(sessionStorage.getItem('loginUser')).permissions && Object.values(qs.parse(sessionStorage.getItem('loginUser')).permissions).includes(per)){
            return true
        }else if(per=='noPermission'){ //不用权限
            return true
        }else {
            return false
        }
    }
}