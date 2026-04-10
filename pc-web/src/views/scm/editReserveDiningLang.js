export const messages = {
  en: {
    editReserveDining: {
      title: 'Edit Dining Product',
      goodsName: 'Name',
      requiredGoodsName: 'Required, please enter name',
      params: 'Parameters',
      requiredParams: 'Required, please select parameters',
      price: 'Price',
      requiredPrice: 'Required, please enter price',
      sort: 'Sort',
      requiredSort: 'Required, please enter sort',
      startDate: 'Start Date',
      requiredStartDate: 'Required, please select start date',
      endDate: 'End Date',
      requiredEndDate: 'Required, please select end date',
      coverImage: 'Product Cover',
      state: 'Status',
      requiredState: 'Required, please select status',
      stateNotOnShelf: 'Not On Shelf',
      stateOnShelf: 'On Shelf',
      goodsDesc: 'Product Description',
      requiredGoodsDesc: 'Required, please enter product description',
      content: 'Product Details',
      requiredContent: 'Required, please enter product details',
      validate: {
        goodsName: 'Name cannot be empty',
        paramsId: 'Please select parameters',
        price: 'Price cannot be empty',
        sort: 'Sort cannot be empty',
        startDate: 'Please select start date',
        endDate: 'Please select end date',
        imgUrl: 'Please upload product cover',
        state: 'Please select status',
        goodsDesc: 'Product description cannot be empty',
        content: 'Product details cannot be empty'
      },
      fetchParamsError: 'Failed to get parameters',
      fetchGoodsError: 'Failed to get product information'
    },
    uploadImage: {
      validate: {
        size: 'Image size cannot exceed 2MB'
      },
      uploadError: 'Image upload failed'
    }
  },
  zh: {
    editReserveDining: {
      title: '编辑就餐商品',
      goodsName: '名称',
      requiredGoodsName: '必填，请填写名称',
      params: '参数',
      requiredParams: '必填，请选择参数',
      price: '单价',
      requiredPrice: '必填，请填写单价',
      sort: '排序',
      requiredSort: '必填，请填写排序',
      startDate: '开始时间',
      requiredStartDate: '必填，请填写开始时间',
      endDate: '结束时间',
      requiredEndDate: '必填，请填写结束时间',
      coverImage: '产品封面',
      state: '状态',
      requiredState: '必填，请选择状态',
      stateNotOnShelf: '未上架',
      stateOnShelf: '上架',
      goodsDesc: '商品简介',
      requiredGoodsDesc: '必填，请填写商品简介',
      content: '商品详情',
      requiredContent: '必填，请输入商品详情',
      validate: {
        goodsName: '名称不能为空',
        paramsId: '请选择参数',
        price: '单价不能为空',
        sort: '排序不能为空',
        startDate: '请选择开始时间',
        endDate: '请选择结束时间',
        imgUrl: '请上传产品封面',
        state: '请选择状态',
        goodsDesc: '商品简介不能为空',
        content: '商品详情不能为空'
      },
      fetchParamsError: '获取参数失败',
      fetchGoodsError: '获取商品信息失败'
    },
    uploadImage: {
      validate: {
        size: '图片大小不能超过2MB'
      },
      uploadError: '图片上传失败'
    }
  }
}