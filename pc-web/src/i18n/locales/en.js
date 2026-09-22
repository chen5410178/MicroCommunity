import { messages as loginMessages } from '../../views/user/login/LoginLang'
import { messages as layoutMessages } from '../../views/layout/LayoutLang'
import { messages as commonMessages } from '../commonLang'
import { messages as indexMessages } from '../../views/index/indexLang'
import { messages as updatePwdMessages } from '../../views/user/login/updatePwdLang'

import { messages as categoriesTreeMessages } from '../categoriesTreeLang'
import { messages as orgMessages } from '../../views/org/orgLang'
import { messages as staffMessages } from '../../views/staff/staffLang'
import { messages as addStaffMessages } from '../../views/staff/addStaffLang'
import { messages as roleMessages } from '../../views/role/roleLang'

import { messages as staffDetailMessages } from '../../views/staff/staffDetailLang'
import { messages as reportCustomGroupManageMessages } from '../../views/report/reportCustomGroupManageLang'

import { messages as systemInfoManageMessages } from '../../views/system/systemInfoManageLang'
import { messages as registerProtocolMessages } from '../../views/system/registerProtocolLang'
import { messages as initializeCommunityManageMessages } from '../../views/community/initializeCommunityManageLang'

import { messages as communityManageMessages } from '../../views/community/communityManageLang'
import { messages as propertyCompanyManageMessages } from '../../views/store/propertyCompanyManageLang'
import { messages as propertyCommunityMessages } from '../../views/community/propertyCommunityLang'
import { messages as cityAreaMessages } from '../../views/community/cityAreaLang'
import { messages as merchantManageMessages } from '../../views/mall/merchantManageLang'
import { messages as shopManageMessages } from '../../views/mall/shopManageLang'
import { messages as shopCommunityMessages } from '../../views/mall/shopCommunityLang'
import { messages as shopDetailMessages } from '../../views/shop/shopDetailLang'
import { messages as systemUserMessages } from '../../views/staff/systemUserLang'
import { messages as systemUserDetailMessages } from '../../views/staff/systemUserDetailLang'
import { messages as storeStaffMessages } from '../../views/staff/storeStaffLang'
import { messages as adminStaffDetailMessages } from '../../views/staff/adminStaffDetailLang'
import { messages as adminRoomMessages } from '../../views/community/adminRoomLang'
import { messages as adminOwnerMessages } from '../../views/owner/adminOwnerLang'
import { messages as adminAuthOwnerMessages } from '../../views/owner/adminAuthOwnerLang'
import { messages as adminAccountMessages } from '../../views/account/adminAccountLang'
import { messages as adminAccountDetailMessages } from '../../views/account/adminAccountDetailLang'
import { messages as adminFeeConfigMessages } from '../../views/fee/adminFeeConfigLang'
import { messages as adminEquipmentMessages } from '../../views/resource/adminEquipmentLang'
import { messages as adminRoomFeeMessages } from '../../views/fee/adminRoomFeeLang'
import { messages as adminRepairMessages } from '../../views/work/adminRepairLang'
import { messages as adminRepairDetailMessages } from '../../views/work/adminRepairDetailLang'

import { messages as adminComplaintMessages } from '../../views/complaint/adminComplaintLang'
import { messages as adminComplaintDetailMessages } from '../../views/complaint/adminComplaintDetailLang'
import { messages as adminWorkPoolMessages } from '../../views/work/adminWorkPoolLang'
import { messages as adminWorkDetailMessages } from '../../views/work/adminWorkDetailLang'
import { messages as aAccessControlMessages } from '../../views/iot/aAccessControlLang'
import { messages as adminBarrierMessages } from '../../views/iot/adminBarrierLang'
import { messages as adminChargeMachineMessages } from '../../views/iot/adminChargeMachineLang'
import { messages as adminMeterMessages } from '../../views/iot/adminMeterLang'
import { messages as adminInoutMessages } from '../../views/iot/adminInoutLang'
import { messages as adminChargeOrderMessages } from '../../views/iot/adminChargeOrderLang'
import { messages as adminMeterRechargeMessages } from '../../views/iot/adminMeterRechargeLang'
import { messages as operationalAnalysisMessages } from '../../views/report/operationalAnalysisLang'
import { messages as communityFeeSummaryMessages } from '../../views/report/communityFeeSummaryLang'
import { messages as adminPayFeeDetailMessages } from '../../views/fee/adminPayFeeDetailLang'
import { messages as adminOweFeeDetailMessages } from '../../views/fee/adminOweFeeDetailLang'
import { messages as orderManageMessages } from '../../views/admin/orderManageLang'
import { messages as userLoginMessages } from '../../views/user/userLoginLang'
import { messages as productSjMessages } from '../../views/mall/productSjLang'
import { messages as housekeepingSjServMessages } from '../../views/mall/housekeepingSjServLang'
import { messages as storeOrderCartManageMessages } from '../../views/mall/storeOrderCartManageLang'
import { messages as storeOrderCartReturnMessages } from '../../views/mall/storeOrderCartReturnLang'
import { messages as userIntegralMessages } from '../../views/mall/userIntegralLang'
import { messages as integralDetailMessages } from '../../views/mall/integralDetailLang'
import { messages as marketSmsManageMessages } from '../../views/market/marketSmsManageLang'
import { messages as marketWayMessages } from '../../views/market/marketWayLang'
import { messages as marketGoodsItemManageMessages } from '../../views/market/marketGoodsItemManageLang'
import { messages as marketRuleMessages } from '../../views/market/marketRuleLang'
import { messages as marketBlacklistManageMessages } from '../../views/market/marketBlacklistManageLang'
import { messages as marketLogMessages } from '../../views/market/marketLogLang'
import { messages as advertManageMessages } from '../../views/admin/advertManageLang'
import { messages as convenienceMenusManageMessages } from '../../views/mall/convenienceMenusManageLang'
import { messages as storeInfoManageMessages } from '../../views/mall/storeInfoManageLang'
import { messages as junkRequirementManageMessages } from '../../views/mall/junkRequirementManageLang'
import { messages as couponPoolManageMessages } from '../../views/account/couponPoolManageLang'
import { messages as couponDetailManageMessages } from '../../views/account/couponDetailManageLang'
import { messages as supplierTypeManageMessages } from '../../views/scm/supplierTypeManageLang'
import { messages as supplierManageMessages } from '../../views/scm/supplierManageLang'
import { messages as supplierCouponMessages } from '../../views/scm/supplierCouponLang'
import { messages as supplierCouponBuyMessages } from '../../views/scm/supplierCouponBuyLang'
import { messages as aStaffDetailMessages } from '../../views/staff/aStaffDetailLang'
import { messages as aStaffCommunityMessages } from '../../views/staff/aStaffCommunityLang'
import { messages as communityWechatMessages } from '../../views/community/communityWechatLang'
import { messages as communityMiniMessages } from '../../views/community/communityMiniLang'
import { messages as communityPaymentMessages } from '../../views/fee/communityPaymentLang'
import { messages as enterCommunityMessages } from '../../views/community/enterCommunityLang'
import { messages as roomRenovationManageMessages } from '../../views/community/roomRenovationManageLang'
import { messages as feeConfigManageMessages } from '../../views/fee/feeConfigManageLang'
import { messages as payFeeConfigDiscountManageMessages } from '../../views/fee/payFeeConfigDiscountManageLang'
import { messages as roomMessages } from '../../views/room/roomLang.js'
import { messages as addRoomViewMessages } from '../../views/room/addRoomViewLang'
import { messages as roomBindOwnerMessages } from '../../views/owner/roomBindOwnerLang'
import { messages as deleteOwnerRoomMessages } from '../../views/owner/deleteOwnerRoomLang'
import { messages as shopsMessages } from '../../views/room/shopsLang'
import { messages as listOwnerMessages } from '../../views/owner/listOwnerLang'
import { messages as auditAuthOwnerMessages } from '../../views/owner/auditAuthOwnerLang'
import { messages as accountManageMessages } from '../../views/account/accountManageLang'
import { messages as accountDetailManageMessages } from '../../views/account/accountDetailManageLang'
import { messages as communitySpaceManageMessages } from '../../views/community/communitySpaceManageLang'
import { messages as reportCommunitySpaceMessages } from '../../views/community/reportCommunitySpaceLang'
import { messages as communitySpacePersonManageMessages } from '../../views/community/communitySpacePersonManageLang'
import { messages as communitySpaceConfirmMessages } from '../../views/community/communitySpaceConfirmLang'
import { messages as addOwnerCommitteeMessages } from '../../views/owner/addOwnerCommitteeLang'
import { messages as editOwnerCommitteeMessages } from '../../views/owner/editOwnerCommitteeLang'
import { messages as ownerCommitteeDetailMessages } from '../../views/owner/ownerCommitteeDetailLang'
import { messages as invoiceApplyMessages } from '../../views/fee/invoiceApplyLang'
import { messages as ownerApplyInvoiceMessages } from '../../views/fee/ownerApplyInvoiceLang'
import { messages as invoiceApplyDetailMessages } from '../../views/fee/invoiceApplyDetailLang'
import { messages as feeConfigDetailMessages } from '../../views/fee/feeConfigDetailLang'

import { messages as parkingSpaceCreateFeeMessages } from '../../views/fee/parkingSpaceCreateFeeLang'
import { messages as listCarFeeMessages } from '../../views/fee/listCarFeeLang'
import { messages as repairSettingMessages } from '../../views/work/repairSettingLang'
import { messages as repairTypeUserMessages } from '../../views/work/repairTypeUserLang'

import { messages as ownerRepairManageMessages } from '../../views/work/ownerRepairManageLang'
import { messages as repairPoolManageMessages } from '../../views/work/repairPoolManageLang'
import { messages as repairDetailMessages } from '../../views/work/repairDetailLang'
import { messages as repairDispatchManageMessages } from '../../views/work/repairDispatchManageLang'
import { messages as myRepairDispatchManageMessages } from '../../views/work/myRepairDispatchManageLang'
import { messages as repairReturnVisitMessages } from '../../views/work/repairReturnVisitLang'
import { messages as repairForceFinishManageMessages } from '../../views/work/repairForceFinishManageLang'

import { messages as locationManageMessages } from '../../views/community/locationManageLang'

import { messages as inspectioni18n } from '../inspectionI18n'
import { messages as machineI18n } from '../machineI18n'
import { messages as oaI18n } from '../oaI18n'
import { messages as contractI18n } from '../contractI18n'
import { messages as reportI18n } from '../reportI18n'
import { messages as devI18n } from '../devI18n'
import { messages as resourceI18n } from '../resourceI18n'
import { messages as carI18n } from '../carI18n'
import { messages as scmI18n } from '../scmI18n'
import { messages as userI18n } from '../userI18n'
import { messages as systemI18n } from '../systemI18n'
import { messages as communityI18n } from '../communityI18n'
import { messages as workI18n } from '../workI18n'
import { messages as feeI18n } from '../feeI18n'
import { menuMessages } from '../menuLang'

import { messages as manualBillGenerationMessages } from '../manualBillGenerationI18n'
import { messages as simplifyAcceptanceMessages } from '../../views/simplify/simplifyAcceptanceLang'
import { messages as simplifyParkingSpaceFeeMessages } from '../../views/simplify/simplifyParkingSpaceFeeLang'
import { messages as simplifyContractMessages } from '../../views/simplify/simplifyContractLang'

// 英文语言包（保持与原 i18n/index.js 相同的合并顺序，后合并的覆盖先合并的）
export default {
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
  ...systemInfoManageMessages.en,
  ...registerProtocolMessages.en,
  ...initializeCommunityManageMessages.en,
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
  ...invoiceApplyMessages.en,
  ...ownerApplyInvoiceMessages.en,
  ...invoiceApplyDetailMessages.en,
  ...feeConfigDetailMessages.en,
  ...parkingSpaceCreateFeeMessages.en,
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
  ...manualBillGenerationMessages.en,
  ...simplifyAcceptanceMessages.en,
  ...simplifyParkingSpaceFeeMessages.en,
  ...simplifyContractMessages.en
}
