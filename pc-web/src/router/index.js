import Vue from 'vue'
import VueRouter from 'vue-router'
import Layout from '@/views/layout/layout.vue'
import Login from '@/views/user/login/Login.vue'
import printEquipmentAccountLabel from '@/views/resource/printEquipmentAccountLabelList.vue'
import inspectionRouter from './inspectionRouter'
import machineRouter from './machineRouter'
import oaRouter from './oaRouter'
import contractRouter from './contractRouter'
import reportRouter from './reportRouter'
import devRouter from './devRouter'
import resourceRouter from './resourceRouter'
import carRouter from './carRouter'
import scmRouter from './scmRouter'
import userRouter from './userRouter'
import systemRouter from './systemRouter'
import communityRouter from './communityRouter'
import workRouter from './workRouter'
import feeRouter from './feeRouter'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    component: Layout,
    redirect: '/views/index/index',
    children: [
      {
        path: '/views/index/index',
        name: '/views/index/index',
        component: () => import('@/views/index/index.vue'),
        meta: { title: '首页' }
      },

      {
        path: '/pages/frame/changeStaffPwd',
        name: '/pages/frame/changeStaffPwd',
        component: () => import('@/views/user/login/updatePwd.vue'),
        meta: { title: '修改密码' }
      },

      {

        path: '/pages/frame/orgManage',
        name: '/pages/frame/orgManage',
        component: () => import('@/views/org/orgList.vue'),
        meta: { title: '组织信息' }
      },

      {
        path: '/pages/frame/staff',
        name: '/pages/frame/staff',
        component: () => import('@/views/staff/staffList.vue'),
        meta: { title: '员工信息' }
      },
      {
        path: '/views/staff/addStaff',
        name: '/views/staff/addStaff',
        component: () => import('@/views/staff/addStaff.vue'),
      },
      {
        path: '/pages/frame/role',
        name: '/pages/frame/role',
        component: () => import('@/views/role/roleList.vue'),
        meta: { title: '角色权限' }
      },


      {
        path: '/views/staff/staffDetail',
        name: '/views/staff/staffDetail',
        component: () => import('@/views/staff/staffDetailList.vue'),
      },


      {
        path: '/pages/log/mqttLog',
        name: '/pages/log/mqttLog',
        component: () => import('@/views/dev/mqttLogList.vue'),
        meta: { title: 'MQTT日志' }
      },
      {
        path: '/pages/frame/systemInfoManage',
        name: '/pages/frame/systemInfoManage',
        component: () => import('@/views/system/systemInfoManageList.vue'),
        meta: { title: '系统配置' }
      },
      {
        path: '/pages/frame/registerProtocol',
        name: '/pages/frame/registerProtocol',
        component: () => import('@/views/system/registerProtocolList.vue'),
        meta: { title: '注册协议' }
      },

      {
        path: '/views/report/reportCustomComponentRelManage',
        name: '/views/report/reportCustomComponentRelManage',
        component: () => import('@/views/report/reportCustomComponentRelManageList.vue'),
        meta: { title: '自定义报表组件' }
      },
      {
        path: '/views/report/componentConditionManage',
        name: '/views/report/componentConditionManage',
        component: () => import('@/views/report/componentConditionManageList.vue'),
        meta: { title: '组件条件' }
      },
      {
        path: '/views/report/reportCustomComponentFooterManage',
        name: '/views/report/reportCustomComponentFooterManage',
        component: () => import('@/views/report/reportCustomComponentFooterManageList.vue'),
        meta: { title: '报表页脚' }
      },

      {
        path: '/pages/common/communityManage',
        name: '/pages/common/communityManage',
        component: () => import('@/views/community/communityManageList.vue'),
        meta: { title: '小区信息' }
      },
      {
        path: '/pages/admin/propertyCompanyManage',
        name: '/pages/admin/propertyCompanyManage',
        component: () => import('@/views/store/propertyCompanyManageList.vue'),
        meta: { title: '物业公司' }
      },
      {
        path: '/views/community/propertyCommunity',
        name: '/views/community/propertyCommunity',
        component: () => import('@/views/community/propertyCommunityList.vue'),
      },
      {
        path: '/pages/community/cityArea',
        name: '/pages/community/cityArea',
        component: () => import('@/views/community/cityAreaList.vue'),
        meta: { title: '地区' }
      },
      {
        path: '/pages/admin/merchantManage',
        name: '/pages/admin/merchantManage',
        component: () => import('@/views/mall/merchantManageList.vue'),
        meta: { title: '周边商家' }
      },
      {
        path: '/pages/admin/shopManage',
        name: '/views/mall/shopManage',
        component: () => import('@/views/mall/shopManageList.vue'),
        meta: { title: '店铺信息' }
      },
      {
        path: '/pages/admin/shopCommunity',
        name: '/pages/admin/shopCommunity',
        component: () => import('@/views/mall/shopCommunityList.vue'),
        meta: { title: '小区店铺' }
      },
      {
        path: '/pages/staff/systemUser',
        name: '/pages/staff/systemUser',
        component: () => import('@/views/staff/systemUserList.vue'),
        meta: { title: '物业用户' }
      },
      {
        path: '/views/staff/systemUserDetail',
        name: '/views/staff/systemUserDetail',
        component: () => import('@/views/staff/systemUserDetailList.vue'),
      },
      {
        path: '/pages/staff/storeStaff',
        name: '/pages/staff/storeStaff',
        component: () => import('@/views/staff/storeStaffList.vue'),
        meta: { title: '物业员工' }
      },
      {
        path: '/views/staff/adminStaffDetail',
        name: '/views/staff/adminStaffDetail',
        component: () => import('@/views/staff/adminStaffDetail.vue'),
      },
      {
        path: '/pages/community/adminRoom',
        name: '/pages/community/adminRoom',
        component: () => import('@/views/community/adminRoomList.vue'),
        meta: { title: '小区房屋' }
      },
      {
        path: '/pages/owner/adminOwner',
        name: '/pages/owner/adminOwner',
        component: () => import('@/views/owner/adminOwnerList.vue'),
        meta: { title: '小区业主' }
      },
      {
        path: '/pages/owner/adminAuthOwner',
        name: '/pages/owner/adminAuthOwner',
        component: () => import('@/views/owner/adminAuthOwnerList.vue'),
        meta: { title: '业主认证' }
      },
      {
        path: '/pages/owner/adminAccount',
        name: '/pages/owner/adminAccount',
        component: () => import('@/views/account/adminAccountList.vue'),
        meta: { title: '业主账户' }
      },
      {
        path: '/views/account/adminAccountDetail',
        name: '/views/account/adminAccountDetail',
        component: () => import('@/views/account/adminAccountDetailList.vue'),
      },
      {
        path: '/pages/fee/adminFeeConfig',
        name: '/pages/fee/adminFeeConfig',
        component: () => import('@/views/fee/adminFeeConfigList.vue'),
        meta: { title: '小区费用项' }
      },
      {
        path: '/pages/fee/adminRoomFee',
        name: '/pages/fee/adminRoomFee',
        component: () => import('@/views/fee/adminRoomFeeList.vue'),
        meta: { title: '房屋费用' }
      },
      {
        path: '/pages/work/adminRepair',
        name: '/pages/work/adminRepair',
        component: () => import('@/views/work/adminRepairList.vue'),
        meta: { title: '小区报修' }
      },
      {
        path: '/views/work/adminRepairDetail',
        name: '/views/work/adminRepairDetail',
        component: () => import('@/views/work/adminRepairDetailList.vue'),
      },

      {
        path: '/pages/complaint/adminComplaint',
        name: '/pages/complaint/adminComplaint',
        component: () => import('@/views/complaint/adminComplaintList.vue'),
        meta: { title: '小区投诉' }
      },
      {
        path: '/views/complaint/adminComplaintDetail',
        name: '/views/complaint/adminComplaintDetail',
        component: () => import('@/views/complaint/adminComplaintDetailList.vue'),
      },
      {
        path: '/pages/oa/adminWorkPool',
        name: '/pages/oa/adminWorkPool',
        component: () => import('@/views/work/adminWorkPoolList.vue'),
        meta: { title: '小区工作单' }
      },
      {
        path: '/views/work/adminWorkDetail',
        name: '/views/work/adminWorkDetail',
        component: () => import('@/views/work/adminWorkDetailList.vue'),
      },
      {
        path: '/pages/iot/aAccessControl',
        name: '/pages/iot/aAccessControl',
        component: () => import('@/views/iot/aAccessControlList.vue'),
        meta: { title: '小区门禁' }
      },
      {
        path: '/pages/iot/adminBarrier',
        name: '/pages/iot/adminBarrier',
        component: () => import('@/views/iot/adminBarrierList.vue'),
        meta: { title: '道闸相机' }
      },
      {
        path: '/pages/iot/adminMeter',
        name: '/pages/iot/adminMeter',
        component: () => import('@/views/iot/adminMeterList.vue'),
        meta: { title: '水电表' }
      },
      {
        path: '/pages/iot/adminInout',
        name: '/pages/iot/adminInout',
        component: () => import('@/views/iot/adminInoutList.vue'),
        meta: { title: '门禁出入' }
      },
      {
        path: '/pages/iot/adminChargeOrder',
        name: '/pages/iot/adminChargeOrder',
        component: () => import('@/views/iot/adminChargeOrderList.vue'),
        meta: { title: '充电订单' }
      },
      {
        path: '/pages/iot/adminMeterRecharge',
        name: '/pages/iot/adminMeterRecharge',
        component: () => import('@/views/iot/adminMeterRechargeList.vue'),
        meta: { title: '水电充值' }
      },
      {
        path: '/pages/report/operationalAnalysis',
        name: '/pages/report/operationalAnalysis',
        component: () => import('@/views/report/operationalAnalysisList.vue'),
        meta: { title: '运营分析' }
      },
      {
        path: '/pages/report/communityFeeSummary',
        name: '/pages/report/communityFeeSummary',
        component: () => import('@/views/report/communityFeeSummaryList.vue'),
        meta: { title: '小区费用汇总' }
      },
      {
        path: '/pages/fee/adminPayFeeDetail',
        name: '/pages/fee/adminPayFeeDetail',
        component: () => import('@/views/fee/adminPayFeeDetailList.vue'),
        meta: { title: '缴费记录' }
      },
      {
        path: '/pages/fee/adminOweFeeDetail',
        name: '/pages/fee/adminOweFeeDetail',
        component: () => import('@/views/fee/adminOweFeeDetailList.vue'),
        meta: { title: '小区欠费' }
      },
      {
        path: '/pages/admin/orderManage',
        name: '/pages/admin/orderManage',
        component: () => import('@/views/admin/orderManageList.vue'),
        meta: { title: '受理单' }
      },
      {
        path: '/pages/frame/userLogin',
        name: '/pages/frame/userLogin',
        component: () => import('@/views/user/userLoginList.vue'),
        meta: { title: '登录日志' }
      },
      {
        path: '/pages/admin/productSj',
        name: '/pages/admin/productSj',
        component: () => import('@/views/mall/productSjList.vue'),
        meta: { title: '上架商品' }
      },
      {
        path: '/pages/admin/housekeepingSjServ',
        name: '/pages/admin/housekeepingSjServ',
        component: () => import('@/views/mall/housekeepingSjServList.vue'),
        meta: { title: '上架服务' }
      },
      {
        path: '/pages/admin/storeOrderCartManage',
        name: '/pages/admin/storeOrderCartManage',
        component: () => import('@/views/mall/storeOrderCartManageList.vue'),
        meta: { title: '商城订单' }
      },
      {
        path: '/pages/admin/storeOrderCartReturn',
        name: '/pages/admin/storeOrderCartReturn',
        component: () => import('@/views/mall/storeOrderCartReturnList.vue'),
        meta: { title: '退货订单' }
      },
      {
        path: '/pages/admin/userIntegral',
        name: '/pages/admin/userIntegral',
        component: () => import('@/views/mall/userIntegralList.vue'),
        meta: { title: '用户积分' }
      },
      {
        path: '/pages/admin/integralDetail',
        name: '/pages/admin/integralDetail',
        component: () => import('@/views/mall/integralDetailList.vue'),
        meta: { title: '积分交易' }
      },
      {
        path: '/pages/admin/marketSmsManage',
        name: '/pages/admin/marketSmsManage',
        component: () => import('@/views/market/marketSmsManageList.vue'),
        meta: { title: '营销配置' }
      },
      {
        path: '/pages/admin/marketWay',
        name: '/pages/admin/marketWay',
        component: () => import('@/views/market/marketWayList.vue'),
        meta: { title: '营销方式' }
      },
      {
        path: '/views/market/marketGoodsItemManage',
        name: '/views/market/marketGoodsItemManage',
        component: () => import('@/views/market/marketGoodsItemManageList.vue'),
        meta: { title: '商品管理' }
      },
      {
        path: '/pages/admin/marketRule',
        name: '/pages/admin/marketRule',
        component: () => import('@/views/market/marketRuleList.vue'),
        meta: { title: '营销规则' }
      },
      {
        path: '/pages/admin/marketBlacklistManage',
        name: '/pages/admin/marketBlacklistManage',
        component: () => import('@/views/market/marketBlacklistManageList.vue'),
        meta: { title: '营销黑名单' }
      },
      {
        path: '/pages/admin/marketLog',
        name: '/pages/admin/marketLog',
        component: () => import('@/views/market/marketLogList.vue'),
        meta: { title: '营销记录' }
      },
      {
        path: '/pages/property/advertManage',
        name: '/pages/property/advertManage',
        component: () => import('@/views/admin/advertManageList.vue'),
        meta: { title: '发布广告' }
      },
      {
        path: '/pages/admin/convenienceMenusManage',
        name: '/pages/admin/convenienceMenusManage',
        component: () => import('@/views/mall/convenienceMenusManageList.vue'),
      },
      {
        path: '/pages/admin/storeInfoManage',
        name: '/pages/admin/storeInfoManage',
        component: () => import('@/views/mall/storeInfoManageList.vue'),
        meta: { title: '店铺信息' }
      },
      {
        path: '/pages/proxy/junkRequirementManage',
        name: '/pages/proxy/junkRequirementManage',
        component: () => import('@/views/mall/junkRequirementManageList.vue'),
        meta: { title: '需求管理' }
      },
      {
        path: '/pages/admin/couponPoolManage',
        name: '/pages/admin/couponPoolManage',
        component: () => import('@/views/account/couponPoolManageList.vue'),
        meta: { title: '制作优惠券' }
      },
      {
        path: '/pages/admin/couponDetailManage',
        name: '/pages/admin/couponDetailManage',
        component: () => import('@/views/account/couponDetailManageList.vue'),
        meta: { title: '购买记录' }
      },
      {
        path: '/pages/scm/supplierTypeManage',
        name: '/pages/scm/supplierTypeManage',
        component: () => import('@/views/scm/supplierTypeManageList.vue'),
        meta: { title: '供应商类型' }
      },
      {
        path: '/pages/scm/supplierManage',
        name: '/pages/scm/supplierManage',
        component: () => import('@/views/scm/supplierManageList.vue'),
        meta: { title: '供应商' }
      },
      {
        path: '/pages/scm/supplierCoupon',
        name: '/pages/scm/supplierCoupon',
        component: () => import('@/views/scm/supplierCouponList.vue'),
        meta: { title: '优惠券' }
      },
      {
        path: '/pages/scm/supplierCouponBuy',
        name: '/pages/scm/supplierCouponBuy',
        component: () => import('@/views/scm/supplierCouponBuyList.vue'),
        meta: { title: '优惠券订单' }
      },
      {
        path: '/pages/staff/aStaff',
        name: '/pages/staff/aStaff',
        component: () => import('@/views/staff/aStaffList.vue'),
        meta: { title: '运营员工' }
      },
      {
        path: '/views/staff/aStaffDetail',
        name: '/views/staff/aStaffDetail',
        component: () => import('@/views/staff/aStaffDetailList.vue')
      },
      {
        path: '/pages/staff/aStaffCommunity',
        name: '/pages/staff/aStaffCommunity',
        component: () => import('@/views/staff/aStaffCommunityList.vue'),
        meta: { title: '员工小区' }
      },
      {
        path: '/pages/community/communityWechat',
        name: '/pages/community/communityWechat',
        component: () => import('@/views/community/communityWechatList.vue'),
        meta: { title: '小区公众号' }
      },
      {
        path: '/pages/community/communityMini',
        name: '/pages/community/communityMini',
        component: () => import('@/views/community/communityMiniList.vue'),
        meta: { title: '小区小程序' }
      },
      {
        path: '/pages/fee/communityPayment',
        name: '/pages/fee/communityPayment',
        component: () => import('@/views/fee/communityPaymentList.vue'),
        meta: { title: '小区支付' }
      },
      {
        path: '/pages/common/enterCommunity',
        name: '/pages/common/enterCommunity',
        component: () => import('@/views/community/enterCommunityList.vue'),
        meta: { title: '我的小区' }
      },
      {
        path: '/pages/property/roomRenovationManage',
        name: '/pages/property/roomRenovationManage',
        component: () => import('@/views/community/roomRenovationManageList.vue'),
        meta: { title: '房屋装修' }
      },
      {
        path: '/pages/property/feeConfigManage',
        name: '/pages/property/feeConfigManage',
        component: () => import('@/views/fee/feeConfigManageList.vue'),
        meta: { title: '费用项设置' }
      },
      {
        path: '/views/fee/payFeeConfigDiscountManage',
        name: '/views/fee/payFeeConfigDiscountManage',
        component: () => import('@/views/fee/payFeeConfigDiscountManageList.vue'),
      },
      {
        path: '/pages/property/room',
        name: '/pages/property/room',
        component: () => import('@/views/room/roomList.vue'),
        meta: { title: '房屋管理' }
      },
      {
        path: '/views/room/addRoomView',
        name: '/views/room/addRoomView',
        component: () => import('@/views/room/addRoomViewList.vue'),
      },
      {
        path: '/views/owner/roomBindOwner',
        name: '/views/owner/roomBindOwner',
        component: () => import('@/views/owner/roomBindOwnerList.vue'),
      },
      {
        path: '/views/owner/deleteOwnerRoom',
        name: '/views/owner/deleteOwnerRoom',
        component: () => import('@/views/owner/deleteOwnerRoomList.vue'),
      },
      {
        path: '/pages/property/shops',
        name: '/pages/property/shops',
        component: () => import('@/views/room/shopsList.vue'),
        meta: { title: '商铺管理' }
      },
      {
        path: '/views/room/shopBindOwner',
        name: '/views/room/shopBindOwner',
        component: () => import('@/views/room/shopBindOwner.vue'),
      },
      {
        path: '/views/room/shopUnbindOwner',
        name: '/views/room/shopUnbindOwner',
        component: () => import('@/views/room/shopUnbindOwner.vue'),
      },
      {
        path: '/pages/property/listOwner',
        name: '/pages/property/listOwner',
        component: () => import('@/views/owner/listOwner.vue'),
        meta: { title: '业主信息' }
      },
      {
        path: '/views/owner/ownerBindRoom',
        name: '/views/owner/ownerBindRoom',
        component: () => import('@/views/owner/ownerBindRoom.vue'),
      },
      {
        path: '/pages/property/auditAuthOwner',
        name: '/pages/property/auditAuthOwner',
        component: () => import('@/views/owner/auditAuthOwnerList.vue'),
        meta: { title: '业主认证' }
      },
      {
        path: '/pages/property/accountManage',
        name: '/pages/property/accountManage',
        component: () => import('@/views/account/accountManageList.vue'),
        meta: { title: '业主账户' }
      },
      {
        path: '/views/account/accountDetailManage',
        name: '/views/account/accountDetailManage',
        component: () => import('@/views/account/accountDetailManageList.vue'),
      },
      {
        path: '/pages/property/communitySpaceManage',
        name: '/pages/property/communitySpaceManage',
        component: () => import('@/views/community/communitySpaceManageList.vue'),
        meta: { title: '场地管理' }
      },
      {
        path: '/pages/property/reportCommunitySpace',
        name: '/pages/property/reportCommunitySpace',
        component: () => import('@/views/community/reportCommunitySpaceList.vue'),
        meta: { title: '场地预约' }
      },
      {
        path: '/pages/property/communitySpacePersonManage',
        name: '/pages/property/communitySpacePersonManage',
        component: () => import('@/views/community/communitySpacePersonManageList.vue'),
        meta: { title: '场地预约订单' }
      },
      {
        path: '/pages/property/communitySpaceConfirm',
        name: '/pages/property/communitySpaceConfirm',
        component: () => import('@/views/community/communitySpaceConfirmList.vue'),
      },

      {
        path: '/views/owner/addOwnerCommittee',
        name: '/views/owner/addOwnerCommittee',
        component: () => import('@/views/owner/addOwnerCommitteeList.vue'),
      },
      {
        path: '/views/owner/editOwnerCommittee',
        name: '/views/owner/editOwnerCommittee',
        component: () => import('@/views/owner/editOwnerCommitteeList.vue'),
      },
      {
        path: '/views/owner/ownerCommitteeDetail',
        name: '/views/owner/ownerCommitteeDetail',
        component: () => import('@/views/owner/ownerCommitteeDetailList.vue'),
      },
      {
        path: '/pages/fee/ownerInvoice',
        name: '/pages/fee/ownerInvoice',
        component: () => import('@/views/fee/ownerInvoiceList.vue'),
        meta: { title: '发票抬头' }
      },
      {
        path: '/views/fee/addOwnerInvoice',
        name: '/views/fee/addOwnerInvoice',
        component: () => import('@/views/fee/addOwnerInvoiceList.vue'),
      },
      {
        path: '/pages/fee/invoiceApply',
        name: '/pages/fee/invoiceApply',
        component: () => import('@/views/fee/invoiceApplyList.vue'),
        meta: { title: '发票' }
      },
      {
        path: '/views/fee/ownerApplyInvoice',
        name: '/views/fee/ownerApplyInvoice',
        component: () => import('@/views/fee/ownerApplyInvoiceList.vue'),
      },
      {
        path: '/views/fee/invoiceApplyDetail',
        name: '/views/fee/invoiceApplyDetail',
        component: () => import('@/views/fee/invoiceApplyDetailList.vue'),
      },
      {
        path: '/views/fee/feeConfigDetail',
        name: '/views/fee/feeConfigDetail',
        component: () => import('@/views/fee/feeConfigDetailList.vue'),
      },

      {
        path: '/pages/property/carCreateFee',
        name: '/pages/property/carCreateFee',
        component: () => import('@/views/fee/carCreateFeeList.vue'),
        meta: { title: '车辆收费' }
      },
      {
        path: '/views/fee/listCarFee',
        name: '/views/fee/listCarFee',
        component: () => import('@/views/fee/listCarFeeList.vue'),
      },
      {
        path: '/pages/work/repairSetting',
        name: '/pages/work/repairSetting',
        component: () => import('@/views/work/repairSettingList.vue'),
        meta: { title: '报修设置' }
      },
      {
        path: '/views/work/repairTypeUser',
        name: '/views/work/repairTypeUser',
        component: () => import('@/views/work/repairTypeUserList.vue'),
      },
      {
        path: '/pages/property/ownerRepairManage',
        name: '/pages/property/ownerRepairManage',
        component: () => import('@/views/work/ownerRepairManageList.vue'),
        meta: { title: '电话报修' }
      },
      {
        path: '/pages/property/repairPoolManage',
        name: '/pages/property/repairPoolManage',
        component: () => import('@/views/work/repairPoolManageList.vue'),
        meta: { title: '工单池' }
      },
      {
        path: '/views/work/repairDetail',
        name: '/views/work/repairDetail',
        component: () => import('@/views/work/repairDetailList.vue'),
      },
      {
        path: '/pages/property/repairDispatchManage',
        name: '/pages/property/repairDispatchManage',
        component: () => import('@/views/work/repairDispatchManageList.vue'),
        meta: { title: '报修待办' }
      },
      {
        path: '/pages/property/myRepairDispatchManage',
        name: '/pages/property/myRepairDispatchManage',
        component: () => import('@/views/work/myRepairDispatchManageList.vue'),
        meta: { title: '报修已办' }
      },
      {
        path: '/pages/property/repairReturnVisit',
        name: '/pages/property/repairReturnVisit',
        component: () => import('@/views/work/repairReturnVisitList.vue'),
        meta: { title: '报修回访' }
      },
      {
        path: '/pages/property/repairForceFinishManage',
        name: '/pages/property/repairForceFinishManage',
        component: () => import('@/views/work/repairForceFinishManageList.vue'),
        meta: { title: '强制回单' }
      },
      {
        path: '/pages/property/locationManage',
        name: '/pages/property/locationManage',
        component: () => import('@/views/community/locationManageList.vue'),
        meta: { title: '设备位置' }
      },

      ...inspectionRouter,
      ...machineRouter,
      ...oaRouter,
      ...contractRouter,
      ...reportRouter,
      ...devRouter,
      ...resourceRouter,
      ...carRouter,
      ...scmRouter,
      ...userRouter,
      ...systemRouter,
      ...communityRouter,
      ...workRouter,
      ...feeRouter,
      {
        path: '/views/shop/shopDetail',
        name: '/views/shop/shopDetail',
        component: () => import('@/views/shop/shopDetail.vue'),
      },
      // 其他子路由可以在这里添加
    ]
  },
  {
    path: '/views/user/login',
    name: 'views/user/login',
    component: Login,
    meta: {
      title: '登陆页面',
      icon: 'el-icon-s-home'
    }
  },
  {
    path: '/login',
    redirect: '/views/user/login'
  },
  {
    path: '/views/resource/printEquipmentAccountLabel',
    name: '/views/resource/printEquipmentAccountLabel',
    component: printEquipmentAccountLabel
  },
  {
    path: '/pages/question/printQuestionAnswer',
    name: '/pages/question/printQuestionAnswer',
    component: () => import('@/views/oa/printQuestionAnswerList.vue')
  },
  {
    path: '/pages/question/printQuestionAnswerDetail',
    name: '/pages/question/printQuestionAnswerDetail',
    component: () => import('@/views/oa/printQuestionAnswerDetailList.vue')
  },
  {
    path: '/views/oa/printOwnerVoting',
    name: '/views/oa/printOwnerVoting',
    component: () => import('@/views/oa/printOwnerVotingList.vue')
  },
  {
    path: '/views/contract/printContract',
    name: '/views/contract/printContract',
    component: () => import('@/views/contract/printContractList.vue')
  },
  {
    path: '/pages/property/printPayFee',
    name: '/pages/property/printPayFee',
    component: () => import('@/views/fee/printPayFeeList.vue')
  },
  {
    path: '/pages/property/printPayFeeBangTai',
    name: '/pages/property/printPayFeeBangTai',
    component: () => import('@/views/fee/printPayFeeBangTaiList.vue')
  },
  {
    path: '/pages/property/printSmallPayFee',
    name: '/pages/property/printSmallPayFee',
    component: () => import('@/views/fee/printSmallPayFeeList.vue')
  },
  {
    path: '/pages/property/printAccountReceipt',
    name: '/pages/property/printAccountReceipt',
    component: () => import('@/views/account/printAccountReceiptList.vue')
  },
  {
    path: '/pages/property/printSmallAccountReceipt',
    name: '/pages/property/printSmallAccountReceipt',
    component: () => import('@/views/account/printSmallAccountReceiptList.vue')
  },
  {
    path: '/pages/property/printRepairDetail',
    name: '/pages/property/printRepairDetail',
    component: () => import('@/views/work/printRepairDetailList.vue')
  },
  {
    path: '/pages/property/printCommonReportTable',
    name: '/pages/property/printCommonReportTable',
    component: () => import('@/views/report/printCommonReportTableList.vue')
  },
  {
    path:'/pages/property/printAllocationStorehouse',
    name:'/pages/property/printAllocationStorehouse',
    component: () => import('@/views/resource/printAllocationStorehouseList.vue')
    },
    {
      path:'/pages/property/printAssetInventoryInStock',
      name:'/pages/property/printAssetInventoryInStock',
      component: () => import('@/views/resource/printAssetInventoryInStockList.vue')
      },
      {
        path: '/views/fee/printOweFee',
        name: '/views/fee/printOweFee',
        component: () => import('@/views/fee/printOweFeeList.vue')
    },
    {
      path: '/pages/property/printPurchaseApply',
      name: '/views/resource/printPurchaseApply',
      component: () => import('@/views/resource/printPurchaseApply.vue')
    },
    {
      path: '/pages/property/printPurchaseOutApply',
      name: '/views/resource/printPurchaseOutApply',
      component: () => import('@/views/resource/printPurchaseOutApply.vue')
  },
]

const router = new VueRouter({
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 排除静态资源路径
  if (to.path.startsWith('/img/') || to.path.startsWith('/static/') || to.path.startsWith('/js/') || to.path.startsWith('/css/')) {
    return next(); // 直接放行
  }
  if (to.path.startsWith('/formjs/') || to.path.startsWith('/bpmnjs/')
    || to.path.startsWith('/components/frame/')
    || to.path.startsWith('/bigScreen/')) {
    return next(); // 直接放行
  }
  if (to.path.endsWith('.xlsx')) {
    return next(); // 直接放行
  }
  const token = localStorage.getItem('token')
  if (to.path !== '/views/user/login' && !token) {
    next('/views/user/login')
  } else {
    let _title = to.meta.title;
    if (!_title) {
      _title = '物业管理系统'
    } else {
      _title = _title + ' - 物业管理系统'
    }
    document.title = _title
    next()
  }
})

export default router
