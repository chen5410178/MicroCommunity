export default {
  en: {
    addOwnerMember: {
      title: 'Add Member',
      personRole: 'Person Role',
      name: 'Name',
      link: 'Phone',
      sex: 'Gender',
      concactLink: 'Alternate Phone',
      address: 'Address',
      idCard: 'ID Card',
      remark: 'Remark',
      photo: 'Photo',
      uploadPhoto: 'Upload Photo',
      placeholder: {
        personRole: 'Required, please select person role',
        name: 'Required, please enter name',
        link: 'Required, please enter phone number',
        sex: 'Required, please select gender',
        concactLink: 'Optional, please enter alternate phone',
        address: 'Optional, please enter address',
        idCard: 'Optional, please enter ID card',
        remark: 'Optional, please enter remark'
      },
      role: {
        tenant: 'Tenant',
        family: 'Family Member',
        staff: 'Company Staff',
        other: 'Other'
      },
      validate: {
        nameLength: 'Name length must be between 2 and 64 characters',
        phoneFormat: 'Invalid phone number format',
        idCardFormat: 'Invalid ID card format',
        remarkLength: 'Remark length cannot exceed 200 characters',
        photoSize: 'Photo size cannot exceed 2MB'
      },
      uploadFailed: 'Photo upload failed'
    }
  },
  zh: {
    addOwnerMember: {
      title: '添加成员',
      personRole: '人员角色',
      name: '客户名称',
      link: '联系手机',
      sex: '性别',
      concactLink: '备用手机',
      address: '地址',
      idCard: '身份证',
      remark: '备注',
      photo: '照片',
      uploadPhoto: '上传照片',
      placeholder: {
        personRole: '必填，请选择人员角色',
        name: '必填，请填写客户名称',
        link: '必填，请填写联系方式',
        sex: '必填，请选择性别',
        concactLink: '可选，请填写备用手机',
        address: '可填，请填写地址',
        idCard: '可选，请填写身份证',
        remark: '可填，请填写备注'
      },
      role: {
        tenant: '租客',
        family: '家庭成员',
        staff: '公司员工',
        other: '其他'
      },
      validate: {
        nameLength: '姓名长度必须在2位至64位',
        phoneFormat: '手机号格式错误',
        idCardFormat: '身份证格式不对',
        remarkLength: '备注长度不能超过200位',
        photoSize: '图片大小不能超过2MB'
      },
      uploadFailed: '照片上传失败'
    }
  }
}