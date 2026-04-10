export const messages = {
  en: {
    reserveCatalogManage: {
      search: {
        title: 'Search Conditions',
        namePlaceholder: 'Please enter name',
        typePlaceholder: 'Please select type',
        typeOptions: {
          all: 'All',
          dining: 'Reservation Dining',
          service: 'Reservation Service'
        }
      },
      list: {
        title: 'Reservation Catalog List'
      },
      table: {
        type: 'Type',
        name: 'Name',
        sort: 'Sort',
        state: 'Status',
        createTime: 'Create Time',
        typeOptions: {
          dining: 'Reservation Dining',
          service: 'Reservation Service'
        },
        stateOptions: {
          show: 'Show',
          hide: 'Hide'
        }
      },
      form: {
        name: 'Catalog Name',
        sort: 'Sort',
        type: 'Type',
        state: 'Status',
        namePlaceholder: 'Required, please enter catalog name',
        sortPlaceholder: 'Required, please enter sort number',
        typePlaceholder: 'Required, please select type',
        statePlaceholder: 'Required, please select status',
        typeOptions: {
          dining: 'Reservation Dining',
          service: 'Reservation Service'
        },
        stateOptions: {
          show: 'Show',
          hide: 'Hide'
        }
      },
      validate: {
        nameRequired: 'Catalog name is required',
        nameMaxLength: 'Catalog name cannot exceed 128 characters',
        sortRequired: 'Sort is required',
        sortMaxLength: 'Sort cannot exceed 12 characters',
        typeRequired: 'Type is required',
        stateRequired: 'Status is required',
        catalogIdRequired: 'Catalog ID is required'
      },
      add: {
        title: 'Add Reservation Catalog',
        success: 'Add successfully',
        error: 'Failed to add'
      },
      edit: {
        title: 'Edit Reservation Catalog',
        success: 'Edit successfully',
        error: 'Failed to edit'
      },
      delete: {
        title: 'Delete Confirmation',
        confirmText: 'Are you sure to delete this reservation catalog?',
        name: 'Catalog Name',
        success: 'Delete successfully',
        error: 'Failed to delete'
      },
      fetchError: 'Failed to fetch data'
    }
  },
  zh: {
    reserveCatalogManage: {
      search: {
        title: '查询条件',
        namePlaceholder: '请输入名称',
        typePlaceholder: '请选择类型',
        typeOptions: {
          all: '全部',
          dining: '预约就餐',
          service: '预约服务'
        }
      },
      list: {
        title: '预约目录列表'
      },
      table: {
        type: '类型',
        name: '名称',
        sort: '排序',
        state: '状态',
        createTime: '创建时间',
        typeOptions: {
          dining: '预约就餐',
          service: '预约服务'
        },
        stateOptions: {
          show: '展示',
          hide: '不展示'
        }
      },
      form: {
        name: '目录名称',
        sort: '排序',
        type: '类型',
        state: '状态',
        namePlaceholder: '必填，请填写预约目录',
        sortPlaceholder: '必填，请填写排序',
        typePlaceholder: '必填，请选择类型',
        statePlaceholder: '必填，请选择状态',
        typeOptions: {
          dining: '预约就餐',
          service: '预约服务'
        },
        stateOptions: {
          show: '展示',
          hide: '不展示'
        }
      },
      validate: {
        nameRequired: '预约名称不能为空',
        nameMaxLength: '预约名称不能超过128个字符',
        sortRequired: '排序不能为空',
        sortMaxLength: '排序不能超过12个字符',
        typeRequired: '类型不能为空',
        stateRequired: '状态不能为空',
        catalogIdRequired: '编号不能为空'
      },
      add: {
        title: '添加预约目录',
        success: '添加成功',
        error: '添加失败'
      },
      edit: {
        title: '修改预约目录',
        success: '修改成功',
        error: '修改失败'
      },
      delete: {
        title: '删除确认',
        confirmText: '确定删除该预约目录吗？',
        name: '目录名称',
        success: '删除成功',
        error: '删除失败'
      },
      fetchError: '获取数据失败'
    }
  }
}