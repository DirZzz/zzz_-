package com.sandu.service.companyshop.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sandu.api.companyshop.input.CompanyshopQuery;
import com.sandu.api.companyshop.model.BaseCompany;
import com.sandu.api.companyshop.model.Companyshop;
import com.sandu.api.companyshop.service.CompanyshopService;
import com.sandu.api.dictionary.model.Dictionary;
import com.sandu.api.dictionary.model.SysDictionary;
import com.sandu.api.goods.model.ResPic;
import com.sandu.api.user.model.User;
import com.sandu.common.util.Utils;
import com.sandu.service.companyshop.dao.BaseCompanyMapper;
import com.sandu.service.companyshop.dao.CompanyshopMapper;
import com.sandu.service.dictionary.dao.DictionaryDao;
import com.sandu.service.goods.dao.ResPicMapper;
import com.sandu.service.user.dao.UserDao;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * CopyRight (c) 2018 Sandu Technology Inc.
 * <p>
 * store_demo
 *
 * @author sandu <dev@sanduspace.cn>
 * @datetime 2018-Oct-22 16:51
 */
@Slf4j
@Service("companyshopService")
public class CompanyshopServiceImpl implements CompanyshopService {

    @Autowired
    private CompanyshopMapper companyshopMapper;

    @Autowired
    private DictionaryDao dictionaryMapper;

    @Autowired
    private BaseCompanyMapper baseCompanyMapper;

    @Autowired
    private ResPicMapper resPicMapper;

    @Autowired
    private UserDao userDao;

    @Override
    public int insert(Companyshop companyshop) {
        int result = companyshopMapper.insert(companyshop);
        if (result > 0) {
            return companyshop.getId();
        }
        return 0;
    }

    @Override
    public int update(Companyshop companyshop) {
        return companyshopMapper.updateByPrimaryKey(companyshop);
    }

    @Override
    public int delete(Set<Integer> companyshopIds) {
        return companyshopMapper.deleteByPrimaryKey(companyshopIds);
    }

    @Override
    public Companyshop getById(int companyshopId) {
        Companyshop result = companyshopMapper.selectByPrimaryKey(companyshopId);
        List<BaseCompany> companyList = baseCompanyMapper.queryCompany();
        companyList.forEach(s -> {
            if(s.getId() == result.getCompanyId().longValue()) {
                result.setCompanyName(s.getCompanyName());
            }
        });
        List<String> coverResPaths = new ArrayList<>();
        if(result.getCoverResIds() != null && !result.getCoverResIds().equals("")) {
            String[] picIds = result.getCoverResIds().split(",");
            for(String picId : picIds) {
                ResPic renderPic = resPicMapper.selectByPrimaryKey(Long.valueOf(picId));
                if(renderPic!=null){
                    coverResPaths.add(renderPic.getPicPath());
                }
            }
            result.setCoverResPaths(coverResPaths);
        }
        List<String> coverPicPaths = new ArrayList<>();
        String[] picIds = result.getLogoPicId().toString().split(",");
        for(String picId : picIds) {
            ResPic renderPic = resPicMapper.selectByPrimaryKey(Long.valueOf(picId));
            if(renderPic!=null){
                coverPicPaths.add(renderPic.getPicPath());
            }
        }
        result.setCoverPicPaths(coverPicPaths);
        String creator = result.getCreator();
        User user = userDao.selectByNameOrId(creator);
        result.setCreatorAccount(user.getUserName());
        result.setCreatorPhone(user.getMobile());
        Dictionary dictionary = dictionaryMapper.getByTypeAndValue("userType", user.getUserType());
        result.setUserTypeStr(dictionary.getName());
        fetchCompanyShopAreaInfo(result);
        return result;
    }

    @Override
    public PageInfo<Companyshop> findAll(CompanyshopQuery query) {
        PageHelper.startPage(query.getPage(), query.getLimit());
        
        // ==========处理businessType add by huangsongbo 2019.09.04 ->start
        if (StringUtils.isNotBlank(query.getBusinessType())) {
        	List<Integer> businessTypeList = Utils.getIntegerListFromStringList(query.getBusinessType());
        	query.setBusinessTypeList(businessTypeList);
        }
        // ==========处理businessType add by huangsongbo 2019.09.04 ->end
        
        List<Companyshop> results =companyshopMapper.findAll(query);
        List<BaseCompany> companyList = baseCompanyMapper.queryCompany();
        List<Dictionary> dictionaryList = dictionaryMapper.listByAloneType("shopType");
        List<Dictionary> userTypeList = dictionaryMapper.listByAloneType("userType");
        for (Companyshop result : results) {
            //联系人信息
            String creator = result.getCreator();
            User user = userDao.selectByNameOrId(creator);
            if(user != null) {
                result.setCreatorAccount(user.getUserName());
                result.setCreatorPhone(user.getMobile());
            }
            if(result.getRecommendedTime() > 0) {
                //时间戳
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                long lt = new Long(result.getRecommendedTime());
                Date date = new Date(lt);
                result.setRecommendedDate(date);
            }else{
                result.setRecommendedDate(null);
            }
            dictionaryList.forEach(s -> {
                if (s.getValue().equals(result.getBusinessType())) {
                    result.setBusinessTypeName(s.getName());
                }
            });
            companyList.forEach(s -> {
                if(s.getId() == result.getCompanyId().longValue()) {
                    result.setCompanyName(s.getCompanyName());
                }
            });
            List<String> coverPicPaths = new ArrayList<>();
            String[] picIds = result.getLogoPicId().toString().split(",");
            for(String picId : picIds) {
                ResPic renderPic = resPicMapper.selectByPrimaryKey(Long.valueOf(picId));
                if(renderPic!=null){
                    coverPicPaths.add(renderPic.getPicPath());
                }
            }
            ResPic renderPic = resPicMapper.selectByPrimaryKey(Long.valueOf(picIds[0]));
            result.setLogoPicPath(renderPic==null?null:renderPic.getPicPath());
            result.setCoverPicPaths(coverPicPaths);

            /**
             * 获取店铺省市区信息
             */
            fetchCompanyShopAreaInfo(result);

            /**
             * 用户类型
             */
            result.setUserTypeStr(
                    userTypeList.stream()
                            .filter(
                                    type -> Objects.equals(result.getUserType(),type.getValue())
                            )
                            .map(
                                    Dictionary::getName
                            )
                            .findFirst()
                            .orElse(null)
            );
        }
        return new PageInfo<>(results);
    }

    private void fetchCompanyShopAreaInfo(Companyshop result) {
        List<String> codeList = Arrays.asList(result.getProvinceCode(), result.getCityCode(), result.getAreaCode(),result.getStreetCode());
        List<String> areaNameList = companyshopMapper.selectCompanyShopAreaInfo(codeList);
        areaNameList.add(result.getShopAddress());
        String areaInfoName = areaNameList.stream().reduce("", (a, b) -> a + b);
        result.setAreaInfoName(areaInfoName);
    }

    @Override
    public int companyshopToTop(String shopId, String topId) {
        if("1".equals(topId)){
            //置顶
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            String newDate = simpleDateFormat.format(new Date());
            try {
                date = simpleDateFormat.parse(newDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Long ts = date.getTime();
            return companyshopMapper.companyshopToTop(shopId,ts.toString());
        }else{
            return companyshopMapper.companyshopToTop(shopId,"0");
        }
    }

    @Override
    public int companyshopToRefresh(String companyshopId,String topId) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            String newDate = simpleDateFormat.format(new Date());
            try {
                date = simpleDateFormat.parse(newDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Long ts = date.getTime();
            return companyshopMapper.companyshopToRefresh(companyshopId);
    }

    @Override
    public Integer checkHasOfflineShop(Long shopOfflineId) {
        if (shopOfflineId == null || shopOfflineId <= 0) {
            log.warn("参数异常：shopOfflineId 不能为空！");
            return 1;
        }

        return companyshopMapper.checkHasOfflineShop(shopOfflineId);
    }

    @Override
    public Integer checkHasOfflineShop2(Long claimUserId) {
        if (claimUserId == null || claimUserId <= 0) {
            log.warn("参数异常：shopOfflineId 不能为空！");
            return 1;
        }

        return companyshopMapper.checkHasOfflineShop2(claimUserId);
    }

    @Override
    public int modifyEnableScore(Integer enableScore, Double handScore, Integer shopId) {
        return companyshopMapper.updateEnableScore(enableScore,handScore,shopId);
    }
}
