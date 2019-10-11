let pageDatas = [
    //用户统计数据
    {
        title: "用户统计数据",
        tabBar: ["活跃用户", "新增用户"],
        tabIndex: "0",
        filterBar: [
            [
                {
                    fields: "time",
                    type: "daterangebtn",
                    label: "时间",
                    value: 0,
                    options: [
                        {
                            label: "昨天",
                            value: 0
                        },
                        {
                            label: "7天",
                            value: 1
                        },
                        {
                            label: "30天",
                            value: 2
                        },
                        {
                            label: "每月",
                            value: 3
                        }
                    ]
                },
                {
                    fields: "userType",
                    type: "select",
                    label: "用户类型",
                    options: []
                },
                {
                    fields: "useType",
                    type: "select",
                    label: "使用类型",
                    options: [
                        {
                            label: "试用",
                            value: 0
                        },
                        {
                            label: "正式",
                            value: 1
                        }
                    ]
                }
            ],
            [
                {
                    fields: "time",
                    type: "daterangebtn",
                    label: "时间",
                    value: 1,
                    options: [
                        {
                            label: "昨天",
                            value: 0
                        },
                        {
                            label: "7天",
                            value: 1
                        },
                        {
                            label: "30天",
                            value: 2
                        },
                        {
                            label: "每月",
                            value: 3
                        }
                    ]
                },
                {
                    fields: "userType",
                    type: "select",
                    label: "用户类型",
                    options: []
                },
                {
                    fields: "useType",
                    type: "select",
                    label: "使用类型",
                    options: [
                        {
                            label: "试用",
                            value: 0
                        },
                        {
                            label: "正式",
                            value: 1
                        }
                    ]
                }
            ]
        ],
        summarizingData: [],
        summarizingLabel: [
            {
                "activeUserTotal": "活跃用户",
                "loginPc2bTotal": "登录pc端用户",
                "loginMobile2bTotal": "登录移动端用户",
                "loginMerchantManageTotal": "登录商家后台用户",
                "accountTotal": "累计总用户",
            }, {
                "newUserTotal": "新增用户",
                "accountTotal": "累计总用户",
                "nonactivatedTotal": "未激活用户"
            }
        ],
        chartType: ["Line", "Line"],
        chartDatas: [],
        detailList: [],
        detailLabel: [
            {
                "startTime": "日期",
                "activeUserCount": "总活跃用户数",
                "loginUserCountPC2B": "登录pc端用户数",
                "loginUserCountMobile2B": "登录移动端用户数",
                "loginUserCountMerchantManage": "登录商家后台用户数",
            }, {
                "startTime": "日期",
                "newUserCount": "新增用户",
            }
        ],
        pageParams: [{ page: 1, limit: 10, total: 0, type: 0 }, { page: 1, limit: 10, total: 0, type: 1 }],
        requestSummarizingUrls: ["/v1/userStatistics/userCountList", "/v1/userStatistics/userCountList"],
        requestTableUrls: ['/v1/userStatistics/userList', '/v1/userStatistics/userList'],
        requestChartUrls: ['/v1/userStatistics/userChartList', '/v1/userStatistics/userChartList'],
        requestExportUrls: ['/v1/userStatistics/activeUserDetailExport', '/v1/userStatistics/newUserDetailExport'],
    },
    //企业统计数据
    {
        title: "企业统计数据",
        tabIndex: "0",
        filterBar: [
            [
                {
                    fields: "time",
                    type: "daterangebtn",
                    label: "时间",
                    value: 0,
                    options: [
                        {
                            label: "昨天",
                            value: 0
                        },
                        {
                            label: "7天",
                            value: 1
                        },
                        {
                            label: "30天",
                            value: 2
                        },
                        {
                            label: "每月",
                            value: 3
                        }
                    ]
                },
                {
                    fields: "companyType",
                    type: "select",
                    label: "企业类型",
                    options: [
                        {
                            label: "厂商",
                            value: 1
                        },
                        {
                            label: "经销商",
                            value: 2
                        },
                        {
                            label: "门店",
                            value: 3
                        },
                        {
                            label: "设计公司",
                            value: 4
                        },
                        {
                            label: "装修公司",
                            value: 5
                        },
                        {
                            label: "设计师",
                            value: 6
                        },
                        {
                            label: "工长",
                            value: 7
                        },
                        {
                            label: "中介",
                            value: 8
                        },
                        {
                            label: "独立经销商",
                            value: 9
                        },
                        {
                            label: "定制柜",
                            value: 10
                        },
                    ]
                },
            ]],
        chartType: ["Bar"],
        chartDatas: [],
        barChartKeys: ['companyName'],
        barChartValues: ["activeUserCount"],
        barChartTitle: ['企业使用排行TOP10'],
        detailList: [],
        detailLabel: [{
            "id": "序号",
            "companyName": "企业名称",
            "companyType": "企业类型",
            "brandName": "品牌名称",
            "newUserCount": "新增用户数",
            "activeUserCount": "活跃用户数",
            "userAccountCount": "开通人数",
            "userEffectiveRate": "使用率",
        }],
        detailListKeyValue: { companyType: { 1: "厂商", 2: "经销商", 3: "门店", 4: "设计公司", 5: "装修公司", 6: "设计师", 7: "工长", 8: "中介", 9: "独立经销商", 10: "定制柜" } },
        pageParams: [{ page: 1, limit: 10, total: 0 }],
        requestTableUrls: ['/v1/companyStatistics/companyDetailList'],
        requestChartUrls: ['/v1/companyStatistics/hotCompanyList'],
        requestExportUrls: ['/v1/companyStatistics/companyDetailExport'],
    },
    //户型统计数据 
    {
        title: "户型统计数据",
        tabBar: ["户型使用统计", "新增户型统计", "热门户型"],
        tabIndex: "0",
        filterBar: [
            [
                {
                    fields: "time",
                    type: "daterangebtn",
                    label: "时间",
                    value: 0,
                    options: [
                        {
                            label: "昨天",
                            value: 0
                        },
                        {
                            label: "7天",
                            value: 1
                        },
                        {
                            label: "30天",
                            value: 2
                        },
                        {
                            label: "每月",
                            value: 3
                        }
                    ]
                },
                {
                    fields: "provinceCode",
                    type: "select",
                    label: "省",
                    options: [],
                    value: undefined
                },
                {
                    fields: "cityCode",
                    type: "select",
                    label: "市",
                    options: []
                }
            ],
            [
                {
                    fields: "time",
                    type: "daterangebtn",
                    label: "时间",
                    value: 0,
                    options: [
                        {
                            label: "昨天",
                            value: 0
                        },
                        {
                            label: "7天",
                            value: 1
                        },
                        {
                            label: "30天",
                            value: 2
                        },
                        {
                            label: "每月",
                            value: 3
                        }
                    ]
                },
                {
                    fields: "provinceCode",
                    type: "select",
                    label: "省",
                    options: [],
                    value: undefined
                },
                {
                    fields: "cityCode",
                    type: "select",
                    label: "市",
                    options: []
                }
            ],
            [
                {
                    fields: "time",
                    type: "daterangebtn",
                    label: "时间",
                    value: 0,
                    options: [
                        {
                            label: "昨天",
                            value: 0
                        },
                        {
                            label: "7天",
                            value: 1
                        },
                        {
                            label: "30天",
                            value: 2
                        },
                        {
                            label: "每月",
                            value: 3
                        }
                    ]
                },
                {
                    fields: "provinceCode",
                    type: "select",
                    label: "省",
                    options: [],
                    value: undefined
                },
                {
                    fields: "cityCode",
                    type: "select",
                    label: "市",
                    options: []
                }
            ],
        ],
        summarizingData: [],
        summarizingLabel: [
            {
                "useHouseTotal": "户型使用量",
                "houseCount": "累计户型量",
            },
            {
                "newHouseTotal": "户型新增量",
                "houseCount": "累计户型量"
            }
        ],
        chartType: ["Line", "Line", "Bar"],
        chartDatas: [],
        barChartKeys: [, , 'livingName+houseName'],
        barChartValues: [, , "useHouseTotal"],
        barChartTitle: ['', '', '热门户型TOP10'],
        detailList: [],
        detailLabel: [
            {
                "startTime": "日期",
                "useHouseCount": "户型使用数据",
            },
            {
                "startTime": "日期",
                "newHouseCount": "户型新增数据",
            },
            {
                "id": "序号",
                "houseCode": "户型编码",
                "houseName": "户型名称",
                "livingName": "小区名称",
            }
        ],
        pageParams: [{ page: 1, limit: 10, total: 0, type: 0 }, { page: 1, limit: 10, total: 0, type: 1 }, { page: 1, limit: 10, total: 0 }],
        requestSummarizingUrls: ["/v1/houseStatistics/houseCountList", "/v1/houseStatistics/houseCountList"],
        requestTableUrls: ['/v1/houseStatistics/houseList', '/v1/houseStatistics/houseList', '/v1/houseStatistics/hotHouseDetailList'],
        requestChartUrls: ['/v1/houseStatistics/houseChartList', '/v1/houseStatistics/houseChartList', '/v1/houseStatistics/hotHouseList'],
        requestExportUrls: ['/v1/houseStatistics/houseUseDetailExport', '/v1/houseStatistics/houseNewDetailExport', '/v1/houseStatistics/hotHouseDetailExport'],
    },
    //方案统计数据 
    {
        title: "方案统计数据",
        tabBar: ["方案使用数据", "新增方案数据", "热门方案"],
        tabIndex: "0",
        filterBar: [
            [
                {
                    fields: "time",
                    type: "daterangebtn",
                    label: "时间",
                    value: 0,
                    options: [
                        {
                            label: "昨天",
                            value: 0
                        },
                        {
                            label: "7天",
                            value: 1
                        },
                        {
                            label: "30天",
                            value: 2
                        },
                        {
                            label: "每月",
                            value: 3
                        }
                    ]
                },
                {
                    fields: "recommendedType",
                    type: "select",
                    label: "方案类型",
                    options: [
                        {
                            label: "普通方案",
                            value: 1
                        },
                        {
                            label: "智能方案",
                            value: 2
                        }]
                },
                {
                    fields: "planGroupStyleId",
                    type: "select",
                    label: "方案风格",
                    options: []
                },
                {
                    fields: "spaceFunctionId",
                    type: "select",
                    label: "空间类型",
                    options: []
                },
                {
                    fields: "companyId",
                    type: "select",
                    label: "所属企业",
                    options: [],
                    filterable: true
                },
                {
                    fields: "planSource",
                    type: "select",
                    label: "方案来源",
                    options: [
                        {
                            label: "内部制作",
                            value: "diy"
                        },
                        {
                            label: "企业交付",
                            value: "deliver"
                        },
                        {
                            label: "户型方案",
                            value: "huxing"
                        },
                        {
                            label: "分享方案",
                            value: "share"
                        },]
                }
            ],
            [
                {
                    fields: "time",
                    type: "daterangebtn",
                    label: "时间",
                    value: 0,
                    options: [
                        {
                            label: "昨天",
                            value: 0
                        },
                        {
                            label: "7天",
                            value: 1
                        },
                        {
                            label: "30天",
                            value: 2
                        },
                        {
                            label: "每月",
                            value: 3
                        }
                    ]
                },
                {
                    fields: "recommendedType",
                    type: "select",
                    label: "方案类型",
                    options: [
                        {
                            label: "普通方案",
                            value: 1
                        },
                        {
                            label: "智能方案",
                            value: 2
                        }]
                },
                {
                    fields: "planGroupStyleId",
                    type: "select",
                    label: "方案风格",
                    options: []
                },
                {
                    fields: "spaceCommonType",
                    type: "select",
                    label: "空间类型",
                    options: []
                },
                {
                    fields: "companyId",
                    type: "select",
                    label: "所属企业",
                    options: [],
                    filterable: true
                },
                {
                    fields: "planSource",
                    type: "select",
                    label: "方案来源",
                    options: [
                        {
                            label: "内部制作",
                            value: "diy"
                        },
                        {
                            label: "企业交付",
                            value: "deliver"
                        },
                        {
                            label: "户型方案",
                            value: "huxing"
                        },
                        {
                            label: "分享方案",
                            value: "share"
                        },]
                }
            ],
            [
                {
                    fields: "time",
                    type: "daterangebtn",
                    label: "时间",
                    value: 0,
                    options: [
                        {
                            label: "昨天",
                            value: 0
                        },
                        {
                            label: "7天",
                            value: 1
                        },
                        {
                            label: "30天",
                            value: 2
                        },
                        {
                            label: "每月",
                            value: 3
                        }
                    ]
                },
                {
                    fields: "planType",
                    type: "select",
                    label: "方案类型",
                    options: [
                        {
                            label: "普通方案",
                            value: 1
                        },
                        {
                            label: "智能方案",
                            value: 2
                        }]
                },
                {
                    fields: "planGroupStyleId",
                    type: "select",
                    label: "方案风格",
                    options: []
                },
                {
                    fields: "spaceCommonType",
                    type: "select",
                    label: "空间类型",
                    options: []
                },
                {
                    fields: "companyId",
                    type: "select",
                    label: "所属企业",
                    options: [],
                    filterable: true
                },
                {
                    fields: "planSource",
                    type: "select",
                    label: "方案来源",
                    options: [
                        {
                            label: "内部制作",
                            value: "diy"
                        },
                        {
                            label: "企业交付",
                            value: "deliver"
                        },
                        {
                            label: "户型方案",
                            value: "huxing"
                        },
                        {
                            label: "分享方案",
                            value: "share"
                        },]
                }
            ],
        ],
        summarizingData: [],
        summarizingLabel: [
            {
                "usePlanTotal": "方案使用数据",
                "shelfPlanCount": "累计上架的方案",
                "planCount": "累计总方案",
            },
            {
                "newPlanTotal": "方案新增数据",
                "shelfPlanCount": "累计上架的方案",
                "planCount": "累计总方案",
            }
        ],
        chartType: ["Line", "Line", "Bar"],
        chartDatas: [],
        barChartKeys: [, , 'planName'],
        barChartValues: [, , "usePlanTotal"],
        barChartTitle: ['', '', '热门方案TOP10'],
        detailList: [],
        detailListKeyValue: { planType: { 1: "普通方案", 2: "智能方案" }, planSource: { "diy": "内部制作", "deliver": "企业交付", "huxing": "户型方案", "share": "分享方案" } },
        detailLabel: [
            {
                "startTime": "日期",
                "usePlanCount": "方案使用数据",
            },
            {
                "startTime": "日期",
                "newPlanCount": "方案新增数据",
            },
            {
                "planName": "方案名称",
                "planCode": "方案编码",
                "planType": "方案类型",
                "planSource": "方案来源",
                "designStyle": "方案风格",
            }
        ],
        pageParams: [{ page: 1, limit: 10, total: 0, type: 0 }, { page: 1, limit: 10, total: 0, type: 1 }, { page: 1, limit: 10, total: 0 }],
        requestSummarizingUrls: ["/v1/planStatistics/planCountList", "/v1/planStatistics/planCountList"],
        requestTableUrls: ['/v1/planStatistics/planDetailList', '/v1/planStatistics/planDetailList', '/v1/planStatistics/hotPlanDetailList'],
        requestChartUrls: ['/v1/planStatistics/planChartList', '/v1/planStatistics/planChartList', '/v1/planStatistics/hotPlanList'],
        requestExportUrls: ['/v1/planStatistics/planUseDetailExport', '/v1/planStatistics/planNewDetailExport', '/v1/planStatistics/hotPlanDetailExport'],
    },
    //区域统计数据 
    {
        title: "区域统计数据",
        tabBar: ["活跃用户", "新增用户"],
        tabIndex: "0",
        filterBar: [
            [
                {
                    fields: "time",
                    type: "daterangebtn",
                    label: "时间",
                    value: 0,
                    options: [
                        {
                            label: "昨天",
                            value: 0
                        },
                        {
                            label: "7天",
                            value: 1
                        },
                        {
                            label: "30天",
                            value: 2
                        },
                        {
                            label: "每月",
                            value: 3
                        }
                    ]
                },
                {
                    fields: "areaType",
                    type: "select",
                    label: "区域范围",
                    options: [
                        {
                            label: "省",
                            value: 0
                        },
                        {
                            label: "市",
                            value: 1
                        },],
                    value: 1,
                    clearable: false,
                },
            ],
            [
                {
                    fields: "time",
                    type: "daterangebtn",
                    label: "时间",
                    value: 0,
                    options: [
                        {
                            label: "昨天",
                            value: 0
                        },
                        {
                            label: "7天",
                            value: 1
                        },
                        {
                            label: "30天",
                            value: 2
                        },
                        {
                            label: "每月",
                            value: 3
                        }
                    ]
                },
                {
                    fields: "areaType",
                    type: "select",
                    label: "区域范围",
                    options: [
                        {
                            label: "省",
                            value: 0
                        },
                        {
                            label: "市",
                            value: 1
                        },],
                    value: 1,
                    clearable: false,
                },
            ],
        ],
        chartType: ["Bar", "Bar"],
        chartDatas: [],
        barChartKeys: ['areaName', 'areaName'],
        barChartValues: ["activeUserCount", "newUserCount"],
        barChartTitle: ['活跃用户区域排名TOP10', '新增用户区域排名TOP10'],
        detailList: [],
        detailLabel: [
            {
                "id": "序号",
                "areaName": "区域",
                "activeUserCount": "用户活跃数",
                "newUserCount": "用户新增数"
            },
            {
                "id": "序号",
                "areaName": "区域",
                "activeUserCount": "用户活跃数",
                "newUserCount": "用户新增数"
            },
        ],
        pageParams: [{ page: 1, limit: 10, total: 0, type: 0 }, { page: 1, limit: 10, total: 0, type: 1 }, { page: 1, limit: 10, total: 0 }],
        requestTableUrls: ['/v1/areaStatistics/activeUserAreaDetailList', '/v1/areaStatistics/newUserAreaDetailList'],
        requestChartUrls: ['/v1/areaStatistics/activeUserAreaList', '/v1/areaStatistics/newUserAreaList'],
        requestExportUrls: ['/v1/areaStatistics/activeUserAreaDetailExport', '/v1/areaStatistics/newUserAreaDetailExport'],
    },
];
let vmCache,
    UserTypeList,
    Provinces,
    PlanStyle,
    SpaceStyle,
    FormCompany;
export function getPageDatas(vm = vmCache, userId) {
    if (!vmCache && vm) vmCache = vm;
    // 用户类型
    if (!UserTypeList)
        this.requestData({ url: "/v1/userStatistics/userTypeList", method: "get" }).then(res => {
            pageDatas[0].filterBar = pageDatas[0].filterBar.map(e => {
                e[1].options = UserTypeList = res.list.map(se => { se.label = se.name; return se }); return e
            })
        });
    // 省市区
    if (!Provinces)
        this.requestData({ url: "/v1/base/area/list", method: "post", params: { areaCode: 0, userId: userId }, basePath: "systemUrl", type: "json" }).then(res => {
            pageDatas[2].filterBar = pageDatas[2].filterBar.map(e => {
                e[1].options = Provinces = res.datalist.map(se => { se.label = se.areaName; se.value = se.areaCode; return se }); return e
            })
        });
    // 方案风格
    if (!PlanStyle)
        this.requestData({ url: "/v1/product/base/style/plan/list", method: "get" }).then(res => {
            pageDatas[3].filterBar = pageDatas[3].filterBar.map(e => {
                e[2].options = PlanStyle = res.data.map(se => { se.label = se.styleName; se.value = se.groupId; return se }); return e
            })
        })
    // 空间类型
    if (!SpaceStyle)
        this.requestData({ url: "/v1/dictionary/group/type/house", method: "get" }).then(res => {
            pageDatas[3].filterBar = pageDatas[3].filterBar.map(e => {
                e[3].options = SpaceStyle = res.data.map(se => { se.label = se.name; se.value = se.id; return se }); return e
            })
        })
    // 所属企业
    if (!FormCompany)
        this.requestData({ url: "/v1/company/list", method: "get" }).then(res => {
            pageDatas[3].filterBar = pageDatas[3].filterBar.map(e => {
                if (!FormCompany)
                    FormCompany = res.data.map(se => { se.label = se.name; se.value = se.id; return se });
                e[4].options = FormCompany
                return e
            })
        })
    return pageDatas;
};
export function requestData(param) {
    param = Object.assign({ method: "post", basePath: 'baseUrl', type: 'formData' }, param);
    return vmCache.API.request(param.method, param.url, param.params, param.basePath, param.type).then(res => {
        if (!res) return;
        if (res.code == 200 || res.success || res.type == "application/vnd.ms-excel") return res;
        else vmCache.$message.error(res.message);
    })
}