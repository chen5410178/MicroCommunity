export const messages = {
  en: {
    storeInfoManage: {
      basicInfo: 'Basic Information',
      storeName: 'Store Name',
      storeAddress: 'Store Address',
      contactNumber: 'Contact Number',
      storeType: 'Store Type',
      legalPerson: 'Legal Person',
      nearbyLandmark: 'Nearby Landmark',
      operation: 'Operation',
      edit: 'Edit',
      storeInfo: 'Store Information',
      attributeName: 'Attribute Name',
      attributeValue: 'Attribute Value',
      fetchError: 'Failed to fetch store information'
    },
    editStoreInfo: {
      title: 'Edit Store Information',
      storeName: 'Store Name',
      storeNamePlaceholder: 'Store name cannot be modified',
      storeAddress: 'Store Address',
      storeAddressPlaceholder: 'Required, please enter store address',
      contactNumber: 'Contact Number',
      contactNumberPlaceholder: 'Required, please enter contact number',
      nearbyLandmark: 'Nearby Landmark',
      nearbyLandmarkPlaceholder: 'Required, please enter nearby landmark',
      mapX: 'Map X Coordinate',
      mapXPlaceholder: 'Optional, please enter X coordinate',
      mapY: 'Map Y Coordinate',
      mapYPlaceholder: 'Optional, please enter Y coordinate',
      validate: {
        nameRequired: 'Store name is required',
        nameMaxLength: 'Store name cannot exceed 100 characters',
        addressRequired: 'Store address is required',
        addressMaxLength: 'Store address cannot exceed 200 characters',
        telRequired: 'Contact number is required',
        telMaxLength: 'Contact number cannot exceed 11 digits',
        landmarkRequired: 'Nearby landmark is required',
        landmarkMaxLength: 'Nearby landmark cannot exceed 200 characters'
      }
    },
    editStoreAttr: {
      title: 'Edit Store Attribute',
      attributePlaceholder: 'Store attribute is required',
      validate: {
        valueRequired: 'Attribute value is required',
        valueMaxLength: 'Attribute value cannot exceed 50 characters',
        attrIdRequired: 'Attribute ID cannot be empty'
      }
    }
  },
  zh: {
    storeInfoManage: {
      basicInfo: '基本信息',
      storeName: '商户名称',
      storeAddress: '商户地址',
      contactNumber: '联系电话',
      storeType: '商户类型',
      legalPerson: '企业法人',
      nearbyLandmark: '附近地标',
      operation: '操作',
      edit: '修改',
      storeInfo: '商户信息',
      attributeName: '属性名称',
      attributeValue: '属性值',
      fetchError: '获取商户信息失败'
    },
    editStoreInfo: {
      title: '修改商户信息',
      storeName: '商户名称',
      storeNamePlaceholder: '商户名称不能修改',
      storeAddress: '商户地址',
      storeAddressPlaceholder: '必填，请填写商户地址',
      contactNumber: '电话',
      contactNumberPlaceholder: '必填，请填写联系电话',
      nearbyLandmark: '附近地标',
      nearbyLandmarkPlaceholder: '必填，请填写附近地标',
      mapX: '地区X坐标',
      mapXPlaceholder: '选填，请填写x坐标',
      mapY: '地区Y坐标',
      mapYPlaceholder: '选填，请填写y坐标',
      validate: {
        nameRequired: '商户名称必填',
        nameMaxLength: '商户名称不能超过100位',
        addressRequired: '商户地址必填',
        addressMaxLength: '商户地址不能超过200位',
        telRequired: '联系电话必填',
        telMaxLength: '联系电话最多11位',
        landmarkRequired: '附件地标必填',
        landmarkMaxLength: '附件地标位置最多200位'
      }
    },
    editStoreAttr: {
      title: '修改商户属性',
      attributePlaceholder: '商户属性必填',
      validate: {
        valueRequired: '必填',
        valueMaxLength: '属性值不能超过50位',
        attrIdRequired: '属性id不能为空'
      }
    }
  }
}