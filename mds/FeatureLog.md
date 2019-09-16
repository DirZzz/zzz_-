### 功能：

​	CMS-750 ：合并同款式不同尺寸的产品（处理历史数据），搜索替换逻辑更改为取最近尺寸的产品

### 接口 :

* （取消）合并产品：/v1/product/merge/hard
* 搜索产品：/v1/product/list/library   （添加一个productBatchType 字段）
* 取消合并页面：/v1/product/library/{productId}/info



### 脚本: 

```mysql
drop table  if  exists base_product_extension;
CREATE TABLE base_product_extension
(
    id int PRIMARY KEY AUTO_INCREMENT,
    product_id int NOT NULL COMMENT '产品ID',
    product_merge_flag tinyint COMMENT '商家后台产品合并标识，1：合并产品',
  modifier                   varchar(32)                             null
  comment '修改人',
  gmt_modified               timestamp default '0000-00-00 00:00:00' not null
  comment '修改时间'

);
ALTER TABLE base_product_extension COMMENT = '产品字段拓展表';
ALTER TABLE base_product_extension ADD CONSTRAINT base_product_extension_pk UNIQUE (product_id);
```





render service :

https://svns.sanduspace.cn/svn/nork/project/render/tags/render-services-201900718-release

render app: 

https://svns.sanduspace.cn/svn/nork/project/render/tags/render-app-tags-20190718-release

system : 

https://svns.sanduspace.cn/svn/nork/project/system/tags/sandu-system-20190718-release



生产脚本:

https://svns.sanduspace.cn/svn/nork/decorate/server/databaseScript/merchant/release_C端_20190718

pre 脚本：

https://svns.sanduspace.cn/svn/nork/decorate/server/databaseScript/预发布脚本资源/Release_pre_20190718







sendToUser:

![1563363266474](C:\Users\Sandu\AppData\Roaming\Typora\typora-user-images\1563363266474.png)



sendTo:

![1563363648737](C:\Users\Sandu\AppData\Roaming\Typora\typora-user-images\1563363648737.png)