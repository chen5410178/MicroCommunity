import Vue from 'vue'
import VueI18n from 'vue-i18n'
import ElementLocale from 'element-ui/lib/locale'
import config from '@/conf/config'
import { messages as loginMessages } from '../views/user/login/LoginLang'
import { messages as layoutMessages } from '../views/layout/LayoutLang'
import { messages as commonMessages } from './commonLang'
import { messages as indexMessages } from '../views/index/indexLang'
import { messages as updatePwdMessages } from '../views/user/login/updatePwdLang'

import { messages as categoriesTreeMessages } from '../i18n/categoriesTreeLang'
import { messages as orgMessages } from '../views/org/orgLang'
import { messages as staffMessages } from '../views/staff/staffLang'
import { messages as addStaffMessages } from '../views/staff/addStaffLang'
import { messages as roleMessages } from '../views/role/roleLang'

import { messages as staffDetailMessages } from '../views/staff/staffDetailLang'
import { messages as reportCustomGroupManageMessages } from '../views/report/reportCustomGroupManageLang'

import { messages as transactionLogManageMessages } from '../views/log/transactionLogManageLang'
import { messages as mqttLogMessages } from '../views/log/mqttLogLang'
import { messages as systemInfoManageMessages } from '../views/system/systemInfoManageLang'
import { messages as registerProtocolMessages } from '../views/system/registerProtocolLang'
import { messages as initializeCommunityManageMessages } from '../views/community/initializeCommunityManageLang'

import { messages as transactionOutLogMessages } from '../views/log/transactionOutLogLang'
import { messages as communityManageMessages } from '../views/community/communityManageLang'
import { messages as propertyCompanyManageMessages } from '../views/store/propertyCompanyManageLang'
import { messages as propertyCommunityMessages } from '../views/community/propertyCommunityLang'
import { messages as cityAreaMessages } from '../views/community/cityAreaLang'
import { messages as merchantManageMessages } from '../views/mall/merchantManageLang'
import { messages as shopManageMessages } from '../views/mall/shopManageLang'
import { messages as shopCommunityMessages } from '../views/mall/shopCommunityLang'
import { messages as shopDetailMessages } from '../views/shop/shopDetailLang'
import { messages as systemUserMessages } from '../views/staff/systemUserLang'
import { messages as systemUserDetailMessages } from '../views/staff/systemUserDetailLang'
import { messages as storeStaffMessages } from '../views/staff/storeStaffLang'
import { messages as adminStaffDetailMessages } from '../views/staff/adminStaffDetailLang'
import { messages as adminRoomMessages } from '../views/community/adminRoomLang'
import { messages as adminOwnerMessages } from '../views/owner/adminOwnerLang'
import { messages as adminAuthOwnerMessages } from '../views/owner/adminAuthOwnerLang'
import { messages as adminAccountMessages } from '../views/account/adminAccountLang'
import { messages as adminAccountDetailMessages } from '../views/account/adminAccountDetailLang'
import { messages as adminFeeConfigMessages } from '../views/fee/adminFeeConfigLang'
import { messages as adminEquipmentMessages } from '../views/resource/adminEquipmentLang'
import { messages as adminRoomFeeMessages } from '../views/fee/adminRoomFeeLang'
import { messages as adminRepairMessages } from '../views/work/adminRepairLang'
import { messages as adminRepairDetailMessages } from '../views/work/adminRepairDetailLang'

import { messages as adminComplaintMessages } from '../views/complaint/adminComplaintLang'
import { messages as adminComplaintDetailMessages } from '../views/complaint/adminComplaintDetailLang'
import { messages as adminWorkPoolMessages } from '../views/work/adminWorkPoolLang'
import { messages as adminWorkDetailMessages } from '../views/work/adminWorkDetailLang'
import { messages as aAccessControlMessages } from '../views/iot/aAccessControlLang'
import { messages as adminBarrierMessages } from '../views/iot/adminBarrierLang'
import { messages as adminChargeMachineMessages } from '../views/iot/adminChargeMachineLang'
import { messages as adminMeterMessages } from '../views/iot/adminMeterLang'
import { messages as adminInoutMessages } from '../views/iot/adminInoutLang'
import { messages as adminChargeOrderMessages } from '../views/iot/adminChargeOrderLang'
import { messages as adminMeterRechargeMessages } from '../views/iot/adminMeterRechargeLang'
import { messages as operationalAnalysisMessages } from '../views/report/operationalAnalysisLang'
import { messages as communityFeeSummaryMessages } from '../views/report/communityFeeSummaryLang'
import { messages as adminPayFeeDetailMessages } from '../views/fee/adminPayFeeDetailLang'
import { messages as adminOweFeeDetailMessages } from '../views/fee/adminOweFeeDetailLang'
import { messages as orderManageMessages } from '../views/admin/orderManageLang'
import { messages as userLoginMessages } from '../views/user/userLoginLang'
import { messages as productSjMessages } from '../views/mall/productSjLang'
import { messages as housekeepingSjServMessages } from '../views/mall/housekeepingSjServLang'
import { messages as storeOrderCartManageMessages } from '../views/mall/storeOrderCartManageLang'
import { messages as storeOrderCartReturnMessages } from '../views/mall/storeOrderCartReturnLang'
import { messages as userIntegralMessages } from '../views/mall/userIntegralLang'
import { messages as integralDetailMessages } from '../views/mall/integralDetailLang'
import { messages as marketSmsManageMessages } from '../views/market/marketSmsManageLang'
import { messages as marketWayMessages } from '../views/market/marketWayLang'
import { messages as marketGoodsItemManageMessages } from '../views/market/marketGoodsItemManageLang'
import { messages as marketRuleMessages } from '../views/market/marketRuleLang'
import { messages as marketBlacklistManageMessages } from '../views/market/marketBlacklistManageLang'
import { messages as marketLogMessages } from '../views/market/marketLogLang'
import { messages as advertManageMessages } from '../views/admin/advertManageLang'
import { messages as convenienceMenusManageMessages } from '../views/mall/convenienceMenusManageLang'
import { messages as storeInfoManageMessages } from '../views/mall/storeInfoManageLang'
import { messages as junkRequirementManageMessages } from '../views/mall/junkRequirementManageLang'
import { messages as couponPoolManageMessages } from '../views/account/couponPoolManageLang'
import { messages as couponDetailManageMessages } from '../views/account/couponDetailManageLang'
import { messages as supplierTypeManageMessages } from '../views/scm/supplierTypeManageLang'
import { messages as supplierManageMessages } from '../views/scm/supplierManageLang'
import { messages as supplierCouponMessages } from '../views/scm/supplierCouponLang'
import { messages as supplierCouponBuyMessages } from '../views/scm/supplierCouponBuyLang'
import { messages as aStaffDetailMessages } from '../views/staff/aStaffDetailLang'
import { messages as aStaffCommunityMessages } from '../views/staff/aStaffCommunityLang'
import { messages as communityWechatMessages } from '../views/community/communityWechatLang'
import { messages as communityMiniMessages } from '../views/community/communityMiniLang'
import { messages as communityPaymentMessages } from '../views/fee/communityPaymentLang'
import { messages as enterCommunityMessages } from '../views/community/enterCommunityLang'
import { messages as roomRenovationManageMessages } from '../views/community/roomRenovationManageLang'
import { messages as feeConfigManageMessages } from '../views/fee/feeConfigManageLang'
import { messages as payFeeConfigDiscountManageMessages } from '../views/fee/payFeeConfigDiscountManageLang'
import { messages as roomMessages } from '../views/room/roomLang.js'
import { messages as addRoomViewMessages } from '../views/room/addRoomViewLang'
import { messages as roomBindOwnerMessages } from '../views/owner/roomBindOwnerLang'
import { messages as deleteOwnerRoomMessages } from '../views/owner/deleteOwnerRoomLang'
import { messages as shopsMessages } from '../views/room/shopsLang'
import { messages as listOwnerMessages } from '../views/owner/listOwnerLang'
import { messages as auditAuthOwnerMessages } from '../views/owner/auditAuthOwnerLang'
import { messages as accountManageMessages } from '../views/account/accountManageLang'
import { messages as accountDetailManageMessages } from '../views/account/accountDetailManageLang'
import { messages as communitySpaceManageMessages } from '../views/community/communitySpaceManageLang'
import { messages as reportCommunitySpaceMessages } from '../views/community/reportCommunitySpaceLang'
import { messages as communitySpacePersonManageMessages } from '../views/community/communitySpacePersonManageLang'
import { messages as communitySpaceConfirmMessages } from '../views/community/communitySpaceConfirmLang'
import { messages as addOwnerCommitteeMessages } from '../views/owner/addOwnerCommitteeLang'
import { messages as editOwnerCommitteeMessages } from '../views/owner/editOwnerCommitteeLang'
import { messages as ownerCommitteeDetailMessages } from '../views/owner/ownerCommitteeDetailLang'
import { messages as ownerInvoiceMessages } from '../views/fee/ownerInvoiceLang'
import { messages as invoiceApplyMessages } from '../views/fee/invoiceApplyLang'
import { messages as ownerApplyInvoiceMessages } from '../views/fee/ownerApplyInvoiceLang'
import { messages as invoiceApplyDetailMessages } from '../views/fee/invoiceApplyDetailLang'
import { messages as feeConfigDetailMessages } from '../views/fee/feeConfigDetailLang'

import { messages as carCreateFeeMessages } from '../views/fee/carCreateFeeLang'
import { messages as listCarFeeMessages } from '../views/fee/listCarFeeLang'
import { messages as repairSettingMessages } from '../views/work/repairSettingLang'
import { messages as repairTypeUserMessages } from '../views/work/repairTypeUserLang'

import { messages as ownerRepairManageMessages } from '../views/work/ownerRepairManageLang'
import { messages as repairPoolManageMessages } from '../views/work/repairPoolManageLang'
import { messages as repairDetailMessages } from '../views/work/repairDetailLang'
import { messages as repairDispatchManageMessages } from '../views/work/repairDispatchManageLang'
import { messages as myRepairDispatchManageMessages } from '../views/work/myRepairDispatchManageLang'
import { messages as repairReturnVisitMessages } from '../views/work/repairReturnVisitLang'
import { messages as repairForceFinishManageMessages } from '../views/work/repairForceFinishManageLang'


import { messages as locationManageMessages } from '../views/community/locationManageLang'

import { messages as inspectioni18n } from './inspectionI18n'
import { messages as machineI18n } from './machineI18n'
import { messages as oaI18n } from './oaI18n'
import { messages as contractI18n } from './contractI18n'
import { messages as reportI18n } from './reportI18n'
import { messages as devI18n } from './devI18n'
import { messages as resourceI18n } from './resourceI18n'
import { messages as carI18n } from './carI18n'
import { messages as scmI18n } from './scmI18n'
import { messages as userI18n } from './userI18n'
import { messages as systemI18n } from './systemI18n'
import { messages as communityI18n } from './communityI18n'
import { messages as workI18n } from './workI18n'
import { messages as feeI18n } from './feeI18n'
import { menuMessages } from './menuLang'

import { messages as simplifyAcceptanceMessages } from '../views/simplify/simplifyAcceptanceLang'
import { messages as simplifyCarFeeMessages } from '../views/simplify/simplifyCarFeeLang'
import { messages as simplifyContractMessages } from '../views/simplify/simplifyContractLang'

Vue.use(VueI18n)

// 合并所有语言配置
const messages = {
  en: {
    ...loginMessages.en,
    ...layoutMessages.en,
    ...commonMessages.en,
    ...indexMessages.en,
    ...updatePwdMessages.en,
    ...categoriesTreeMessages.en,
    ...orgMessages.en,
    ...staffMessages.en,
    ...addStaffMessages.en,
    ...roleMessages.en,
    ...staffDetailMessages.en,
    ...reportCustomGroupManageMessages.en,
    ...transactionLogManageMessages.en,
    ...mqttLogMessages.en,
    ...systemInfoManageMessages.en,
    ...registerProtocolMessages.en,
    ...initializeCommunityManageMessages.en,
    ...transactionOutLogMessages.en,
    ...communityManageMessages.en,
    ...propertyCompanyManageMessages.en,
    ...propertyCommunityMessages.en,
    ...cityAreaMessages.en,
    ...merchantManageMessages.en,
    ...shopManageMessages.en,
    ...shopCommunityMessages.en,
    ...shopDetailMessages.en,
    ...systemUserMessages.en,
    ...systemUserDetailMessages.en,
    ...storeStaffMessages.en,
    ...adminStaffDetailMessages.en,
    ...adminRoomMessages.en,
    ...adminOwnerMessages.en,
    ...adminAuthOwnerMessages.en,
    ...adminAccountMessages.en,
    ...adminAccountDetailMessages.en,
    ...adminFeeConfigMessages.en,
    ...adminEquipmentMessages.en,
    ...adminRoomFeeMessages.en,
    ...adminRepairMessages.en,
    ...adminRepairDetailMessages.en,
    ...adminComplaintMessages.en,
    ...adminComplaintDetailMessages.en,
    ...adminWorkPoolMessages.en,
    ...adminWorkDetailMessages.en,
    ...aAccessControlMessages.en,
    ...adminBarrierMessages.en,
    ...adminChargeMachineMessages.en,
    ...adminMeterMessages.en,
    ...adminInoutMessages.en,
    ...adminChargeOrderMessages.en,
    ...adminMeterRechargeMessages.en,
    ...operationalAnalysisMessages.en,
    ...communityFeeSummaryMessages.en,
    ...adminPayFeeDetailMessages.en,
    ...adminOweFeeDetailMessages.en,
    ...orderManageMessages.en,
    ...userLoginMessages.en,
    ...productSjMessages.en,
    ...housekeepingSjServMessages.en,
    ...storeOrderCartManageMessages.en,
    ...storeOrderCartReturnMessages.en,
    ...userIntegralMessages.en,
    ...integralDetailMessages.en,
    ...marketSmsManageMessages.en,
    ...marketWayMessages.en,
    ...marketGoodsItemManageMessages.en,
    ...marketRuleMessages.en,
    ...marketBlacklistManageMessages.en,
    ...marketLogMessages.en,
    ...advertManageMessages.en,
    ...convenienceMenusManageMessages.en,
    ...storeInfoManageMessages.en,
    ...junkRequirementManageMessages.en,
    ...couponPoolManageMessages.en,
    ...couponDetailManageMessages.en,
    ...supplierTypeManageMessages.en,
    ...supplierManageMessages.en,
    ...supplierCouponMessages.en,
    ...supplierCouponBuyMessages.en,
    ...aStaffDetailMessages.en,
    ...aStaffCommunityMessages.en,
    ...communityWechatMessages.en,
    ...communityMiniMessages.en,
    ...communityPaymentMessages.en,
    ...enterCommunityMessages.en,
    ...roomRenovationManageMessages.en,
    ...feeConfigManageMessages.en,
    ...payFeeConfigDiscountManageMessages.en,
    ...roomMessages.en,
    ...addRoomViewMessages.en,
    ...roomBindOwnerMessages.en,
    ...deleteOwnerRoomMessages.en,
    ...shopsMessages.en,
    ...listOwnerMessages.en,
    ...auditAuthOwnerMessages.en,
    ...accountManageMessages.en,
    ...accountDetailManageMessages.en,
    ...communitySpaceManageMessages.en,
    ...reportCommunitySpaceMessages.en,
    ...communitySpacePersonManageMessages.en,
    ...communitySpaceConfirmMessages.en,
    ...addOwnerCommitteeMessages.en,
    ...editOwnerCommitteeMessages.en,
    ...ownerCommitteeDetailMessages.en,
    ...ownerInvoiceMessages.en,
    ...invoiceApplyMessages.en,
    ...ownerApplyInvoiceMessages.en,
    ...invoiceApplyDetailMessages.en,
    ...feeConfigDetailMessages.en,
   
    ...carCreateFeeMessages.en,
    ...listCarFeeMessages.en,
    ...repairSettingMessages.en,
    ...repairTypeUserMessages.en,
    ...ownerRepairManageMessages.en,
    ...repairPoolManageMessages.en,
    ...repairDetailMessages.en,
    ...repairDispatchManageMessages.en,
    ...myRepairDispatchManageMessages.en,
    ...repairReturnVisitMessages.en,
    ...repairForceFinishManageMessages.en,

  
    ...locationManageMessages.en,
    ...inspectioni18n.en,
    ...machineI18n.en,
    ...oaI18n.en,
    ...contractI18n.en,
    ...reportI18n.en,
    ...devI18n.en,
    ...resourceI18n.en,
    ...carI18n.en,
    ...scmI18n.en,
    ...userI18n.en,
    ...systemI18n.en,
    ...communityI18n.en,
    ...workI18n.en,
    ...feeI18n.en,
    ...menuMessages.en,
    ...simplifyAcceptanceMessages.en,
    ...simplifyCarFeeMessages.en,
    ...simplifyContractMessages.en,
  },
  zh: {
    ...loginMessages.zh,
    ...layoutMessages.zh,
    ...commonMessages.zh,
    ...indexMessages.zh,
    ...updatePwdMessages.zh,
    ...categoriesTreeMessages.zh,
    ...orgMessages.zh,
    ...staffMessages.zh,
    ...addStaffMessages.zh,
    ...roleMessages.zh,
    ...staffDetailMessages.zh,
    ...reportCustomGroupManageMessages.zh,
    ...transactionLogManageMessages.zh,
    ...mqttLogMessages.zh,
    ...systemInfoManageMessages.zh,
    ...registerProtocolMessages.zh,
    ...initializeCommunityManageMessages.zh,
    ...transactionOutLogMessages.zh,
    ...communityManageMessages.zh,
    ...propertyCompanyManageMessages.zh,
    ...propertyCommunityMessages.zh,
    ...cityAreaMessages.zh,
    ...merchantManageMessages.zh,
    ...shopManageMessages.zh,
    ...shopCommunityMessages.zh,
    ...shopDetailMessages.zh,
    ...systemUserMessages.zh,
    ...systemUserDetailMessages.zh,
    ...storeStaffMessages.zh,
    ...adminStaffDetailMessages.zh,
    ...adminRoomMessages.zh,
    ...adminOwnerMessages.zh,
    ...adminAuthOwnerMessages.zh,
    ...adminAccountMessages.zh,
    ...adminAccountDetailMessages.zh,
    ...adminFeeConfigMessages.zh,
    ...adminEquipmentMessages.zh,
    ...adminRoomFeeMessages.zh,
    ...adminRepairMessages.zh,
    ...adminRepairDetailMessages.zh,
    ...adminComplaintMessages.zh,
    ...adminComplaintDetailMessages.zh,
    ...adminWorkPoolMessages.zh,
    ...adminWorkDetailMessages.zh,
    ...aAccessControlMessages.zh,
    ...adminBarrierMessages.zh,
    ...adminChargeMachineMessages.zh,
    ...adminMeterMessages.zh,
    ...adminInoutMessages.zh,
    ...adminChargeOrderMessages.zh,
    ...adminMeterRechargeMessages.zh,
    ...operationalAnalysisMessages.zh,
    ...communityFeeSummaryMessages.zh,
    ...adminPayFeeDetailMessages.zh,
    ...adminOweFeeDetailMessages.zh,
    ...orderManageMessages.zh,
    ...userLoginMessages.zh,
    ...productSjMessages.zh,
    ...housekeepingSjServMessages.zh,
    ...storeOrderCartManageMessages.zh,
    ...storeOrderCartReturnMessages.zh,
    ...userIntegralMessages.zh,
    ...integralDetailMessages.zh,
    ...marketSmsManageMessages.zh,
    ...marketWayMessages.zh,
    ...marketGoodsItemManageMessages.zh,
    ...marketRuleMessages.zh,
    ...marketBlacklistManageMessages.zh,
    ...marketLogMessages.zh,
    ...advertManageMessages.zh,
    ...convenienceMenusManageMessages.zh,
    ...storeInfoManageMessages.zh,
    ...junkRequirementManageMessages.zh,
    ...couponPoolManageMessages.zh,
    ...couponDetailManageMessages.zh,
    ...supplierTypeManageMessages.zh,
    ...supplierManageMessages.zh,
    ...supplierCouponMessages.zh,
    ...supplierCouponBuyMessages.zh,
    ...aStaffDetailMessages.zh,
    ...aStaffCommunityMessages.zh,
    ...communityWechatMessages.zh,
    ...communityMiniMessages.zh,
    ...communityPaymentMessages.zh,
    ...enterCommunityMessages.zh,
    ...roomRenovationManageMessages.zh,
    ...feeConfigManageMessages.zh,
    ...payFeeConfigDiscountManageMessages.zh,
    ...roomMessages.zh,
    ...addRoomViewMessages.zh,
    ...roomBindOwnerMessages.zh,
    ...deleteOwnerRoomMessages.zh,
    ...shopsMessages.zh,
    ...listOwnerMessages.zh,
    ...auditAuthOwnerMessages.zh,
    ...accountManageMessages.zh,
    ...accountDetailManageMessages.zh,
    ...communitySpaceManageMessages.zh,
    ...reportCommunitySpaceMessages.zh,
    ...communitySpacePersonManageMessages.zh,
    ...communitySpaceConfirmMessages.zh,
    ...addOwnerCommitteeMessages.zh,
    ...editOwnerCommitteeMessages.zh,
    ...ownerCommitteeDetailMessages.zh,
    ...ownerInvoiceMessages.zh,
    ...invoiceApplyMessages.zh,
    ...ownerApplyInvoiceMessages.zh,
    ...invoiceApplyDetailMessages.zh,
    ...feeConfigDetailMessages.zh,
    ...carCreateFeeMessages.zh,
    ...listCarFeeMessages.zh,
    ...repairSettingMessages.zh,
    ...repairTypeUserMessages.zh,
    ...ownerRepairManageMessages.zh,
    ...repairPoolManageMessages.zh,
    ...repairDetailMessages.zh,
    ...repairDispatchManageMessages.zh,
    ...myRepairDispatchManageMessages.zh,
    ...repairReturnVisitMessages.zh,
    ...repairForceFinishManageMessages.zh,

    ...locationManageMessages.zh,
    ...inspectioni18n.zh,
    ...machineI18n.zh,
    ...oaI18n.zh,
    ...contractI18n.zh,
    ...reportI18n.zh,
    ...devI18n.zh,
    ...resourceI18n.zh,
    ...carI18n.zh,
    ...scmI18n.zh,
    ...userI18n.zh,
    ...systemI18n.zh,
    ...communityI18n.zh,
    ...workI18n.zh,
    ...feeI18n.zh,
    ...menuMessages.zh,
    ...simplifyAcceptanceMessages.zh,
    ...simplifyCarFeeMessages.zh,
    ...simplifyContractMessages.zh,
  },
  'zh-TW': {
    ...loginMessages['zh-TW'],
    ...layoutMessages['zh-TW'],
    ...commonMessages['zh-TW'],
    ...indexMessages['zh-TW'],
    ...updatePwdMessages['zh-TW'],
    ...categoriesTreeMessages['zh-TW'],
    ...orgMessages['zh-TW'],
    ...staffMessages['zh-TW'],
    ...addStaffMessages['zh-TW'],
    ...roleMessages['zh-TW'],
    ...staffDetailMessages['zh-TW'],
    ...reportCustomGroupManageMessages['zh-TW'],
    ...transactionLogManageMessages['zh-TW'],
    ...mqttLogMessages['zh-TW'],
    ...systemInfoManageMessages['zh-TW'],
    ...registerProtocolMessages['zh-TW'],
    ...initializeCommunityManageMessages['zh-TW'],
    ...transactionOutLogMessages['zh-TW'],
    ...communityManageMessages['zh-TW'],
    ...propertyCompanyManageMessages['zh-TW'],
    ...propertyCommunityMessages['zh-TW'],
    ...cityAreaMessages['zh-TW'],
    ...merchantManageMessages['zh-TW'],
    ...shopManageMessages['zh-TW'],
    ...shopCommunityMessages['zh-TW'],
    ...shopDetailMessages['zh-TW'],
    ...systemUserMessages['zh-TW'],
    ...systemUserDetailMessages['zh-TW'],
    ...storeStaffMessages['zh-TW'],
    ...adminStaffDetailMessages['zh-TW'],
    ...adminRoomMessages['zh-TW'],
    ...adminOwnerMessages['zh-TW'],
    ...adminAuthOwnerMessages['zh-TW'],
    ...adminAccountMessages['zh-TW'],
    ...adminAccountDetailMessages['zh-TW'],
    ...adminFeeConfigMessages['zh-TW'],
    ...adminEquipmentMessages['zh-TW'],
    ...adminRoomFeeMessages['zh-TW'],
    ...adminRepairMessages['zh-TW'],
    ...adminRepairDetailMessages['zh-TW'],
    ...adminComplaintMessages['zh-TW'],
    ...adminComplaintDetailMessages['zh-TW'],
    ...adminWorkPoolMessages['zh-TW'],
    ...adminWorkDetailMessages['zh-TW'],
    ...aAccessControlMessages['zh-TW'],
    ...adminBarrierMessages['zh-TW'],
    ...adminChargeMachineMessages['zh-TW'],
    ...adminMeterMessages['zh-TW'],
    ...adminInoutMessages['zh-TW'],
    ...adminChargeOrderMessages['zh-TW'],
    ...adminMeterRechargeMessages['zh-TW'],
    ...operationalAnalysisMessages['zh-TW'],
    ...communityFeeSummaryMessages['zh-TW'],
    ...adminPayFeeDetailMessages['zh-TW'],
    ...adminOweFeeDetailMessages['zh-TW'],
    ...orderManageMessages['zh-TW'],
    ...userLoginMessages['zh-TW'],
    ...productSjMessages['zh-TW'],
    ...housekeepingSjServMessages['zh-TW'],
    ...storeOrderCartManageMessages['zh-TW'],
    ...storeOrderCartReturnMessages['zh-TW'],
    ...userIntegralMessages['zh-TW'],
    ...integralDetailMessages['zh-TW'],
    ...marketSmsManageMessages['zh-TW'],
    ...marketWayMessages['zh-TW'],
    ...marketGoodsItemManageMessages['zh-TW'],
    ...marketRuleMessages['zh-TW'],
    ...marketBlacklistManageMessages['zh-TW'],
    ...marketLogMessages['zh-TW'],
    ...advertManageMessages['zh-TW'],
    ...convenienceMenusManageMessages['zh-TW'],
    ...storeInfoManageMessages['zh-TW'],
    ...junkRequirementManageMessages['zh-TW'],
    ...couponPoolManageMessages['zh-TW'],
    ...couponDetailManageMessages['zh-TW'],
    ...supplierTypeManageMessages['zh-TW'],
    ...supplierManageMessages['zh-TW'],
    ...supplierCouponMessages['zh-TW'],
    ...supplierCouponBuyMessages['zh-TW'],
    ...aStaffDetailMessages['zh-TW'],
    ...aStaffCommunityMessages['zh-TW'],
    ...communityWechatMessages['zh-TW'],
    ...communityMiniMessages['zh-TW'],
    ...communityPaymentMessages['zh-TW'],
    ...enterCommunityMessages['zh-TW'],
    ...roomRenovationManageMessages['zh-TW'],
    ...feeConfigManageMessages['zh-TW'],
    ...payFeeConfigDiscountManageMessages['zh-TW'],
    ...roomMessages['zh-TW'],
    ...addRoomViewMessages['zh-TW'],
    ...roomBindOwnerMessages['zh-TW'],
    ...deleteOwnerRoomMessages['zh-TW'],
    ...shopsMessages['zh-TW'],
    ...listOwnerMessages['zh-TW'],
    ...auditAuthOwnerMessages['zh-TW'],
    ...accountManageMessages['zh-TW'],
    ...accountDetailManageMessages['zh-TW'],
    ...communitySpaceManageMessages['zh-TW'],
    ...reportCommunitySpaceMessages['zh-TW'],
    ...communitySpacePersonManageMessages['zh-TW'],
    ...communitySpaceConfirmMessages['zh-TW'],
    ...addOwnerCommitteeMessages['zh-TW'],
    ...editOwnerCommitteeMessages['zh-TW'],
    ...ownerCommitteeDetailMessages['zh-TW'],
    ...ownerInvoiceMessages['zh-TW'],
    ...invoiceApplyMessages['zh-TW'],
    ...ownerApplyInvoiceMessages['zh-TW'],
    ...invoiceApplyDetailMessages['zh-TW'],
    ...feeConfigDetailMessages['zh-TW'],
    ...carCreateFeeMessages['zh-TW'],
    ...listCarFeeMessages['zh-TW'],
    ...repairSettingMessages['zh-TW'],
    ...repairTypeUserMessages['zh-TW'],
    ...ownerRepairManageMessages['zh-TW'],
    ...repairPoolManageMessages['zh-TW'],
    ...repairDetailMessages['zh-TW'],
    ...repairDispatchManageMessages['zh-TW'],
    ...myRepairDispatchManageMessages['zh-TW'],
    ...repairReturnVisitMessages['zh-TW'],
    ...repairForceFinishManageMessages['zh-TW'],

    ...locationManageMessages['zh-TW'],
    ...inspectioni18n['zh-TW'],
    ...machineI18n['zh-TW'],
    ...oaI18n['zh-TW'],
    ...contractI18n['zh-TW'],
    ...reportI18n['zh-TW'],
    ...devI18n['zh-TW'],
    ...resourceI18n['zh-TW'],
    ...carI18n['zh-TW'],
    ...scmI18n['zh-TW'],
    ...userI18n['zh-TW'],
    ...systemI18n['zh-TW'],
    ...communityI18n['zh-TW'],
    ...workI18n['zh-TW'],
    ...feeI18n['zh-TW'],
    ...menuMessages['zh-TW'],
    ...simplifyAcceptanceMessages['zh-TW'],
    ...simplifyCarFeeMessages['zh-TW'],
    ...simplifyContractMessages['zh-TW'],
  }
}

// 创建 i18n 实例
const i18n = new VueI18n({
  locale: config.lang,
  messages
})

// 配置Element UI的语言
ElementLocale.i18n((key, value) => i18n.t(key, value))

export default i18n