/**
 *
 * @param permission
 * @param permiss
 * @returns {boolean}
 */
export default function isPermission (permission, permiss) {
  if(permission === true){
      return true;
  }
  if(permission == ''){
      return false;
  }
  if(permission == 'company:view,dealer:view,shop:view'){ //如果是企业设置
      if(sessionStorage.getItem('masterSonType')==2){ //主账号
          return true
      }
  }
  if(permission == 'mainAccountSet'){ //主子账号单独处
      if(sessionStorage.getItem('masterSonType')==2){ //主账号
          return true
      }else {
          return false
      }
  }
  //企业设置线下门店显示（需满足权限 且 用户类型为经销商）
  if(permission=='shopOffline:dealer:view'&&sessionStorage.getItem('userType')!=3){
    return false;
  }
  //拼团活动权限设置(需满足权限 且 用户类型为独立经销商或者厂商)
  if(permission=='groupPurchaseActivity:view' && sessionStorage.getItem('userType')!=2 && sessionStorage.getItem('userType')!=14){
    return false;
  }
  //小程序管理线下门店显示（需满足权限 且 用户类型为厂商）
  if(permission=='shopOffline:view'&&sessionStorage.getItem('userType')!=2){
    return false;
  }
  // console.log(permiss)
  let permissionArr = permission.split(',');
  let havedPermission = false;
  for(let i=0, length= permissionArr.length; i<length; i++){
      if (permiss.permissions) {
         havedPermission = Object.values(permiss.permissions).includes(permission.split(',')[i])
         if (havedPermission) {return havedPermission;}
      }
  }
  return havedPermission;
  // if (permiss.permissions) {
  //     return Object.values(permiss.permissions).includes(per)
  // }
  // return false
};
