**目标**
改造合同收费页面，布局类似于车辆收费页面D:\project\hcv2\MicroCommunityWebV2\src\views\fee\carCreateFeeList.vue
**要求**
1、布局方式和车辆收费布局方式一样，左右布局；
2、左边是合同列表；
3、右边中包含合同费用，缴费历史，预付收款，设置费用，业主，合同房屋，补打收据；
4、合同费用tab 在components/fee创建一个 simplifyContractFee.vue,table内容参考D:\project\hcv2\MicroCommunityWebV2\src\components\contract\contractDetailFee.vue；
5、缴费历史tab 沿用车辆收费中的 simplifyHisFee.vue 即可；
6、预付收款tab，也沿用车辆收费中的prePayFeeDetail即可；
7、设置费用tab，在components/fee创建一个feeDetailContractFeeRule.vue,内容参考车辆收费中的；
8、业主tab，沿用车辆收费中的业主
9、合同房屋tab、采用contractDetailRoom.vue
10、补打收据tab,和车辆收费中的simplifyFeeReceipt的一样；
11、合同的payerObjType 是7777
