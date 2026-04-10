<template>
  <div class="resource-out-manage-container">
    <div class="box-card">
      <div class="card-header flex justify-between">
        <span>{{ $t('resourceOutManage.orderId') }}：{{ resourceOutManageInfo.applyOrderId }}</span>
      </div>
      
      <div class="table-container">
        <!-- 加载状态 -->
        <div v-if="loading" class="loading-overlay">
          <div class="loading-spinner">加载中...</div>
        </div>
        
        <!-- 普通HTML表格 -->
        <table class="data-table" v-if="!loading">
          <thead>
            <tr>
              <th class="table-header">{{ $t('resourceOutManage.itemType') }}</th>
              <th class="table-header">{{ $t('resourceOutManage.itemName') }}</th>
              <th class="table-header">{{ $t('resourceOutManage.itemSpec') }}</th>
              <th class="table-header">{{ $t('resourceOutManage.itemCode') }}</th>
              <th class="table-header">{{ $t('resourceOutManage.itemStock') }}</th>
              <th class="table-header">{{ $t('resourceOutManage.applyQuantity') }}</th>
              <th class="table-header">{{ $t('resourceOutManage.issueQuantity') }}</th>
              <th class="table-header">{{ $t('resourceOutManage.remark') }}</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(item, index) in resourceOutManageInfo.purchaseApplyDetailVo" :key="index" class="table-row">
              <td class="table-cell">{{ item.rstName || '-' }}</td>
              <td class="table-cell">{{ item.resName }}</td>
              <td class="table-cell">{{ item.specName || '-' }}</td>
              <td class="table-cell">{{ item.resCode }}</td>
              <td class="table-cell">{{ item.stock }}</td>
              <td class="table-cell">{{ item.quantity }}</td>
              <td class="table-cell">
                <input 
                  v-model.number="item.purchaseQuantity" 
                  type="number" 
                  class="form-input"
                  :placeholder="$t('resourceOutManage.issueQuantityPlaceholder')"
                  @input="validateQuantity(item)"
                />
              </td>
              <td class="table-cell">
                <input 
                  v-model="item.purchaseRemark" 
                  type="text" 
                  class="form-input"
                  :placeholder="$t('resourceOutManage.remarkPlaceholder')"
                />
              </td>
            </tr>
          </tbody>
        </table>
        
        <!-- 空数据状态 -->
        <div v-if="!loading && resourceOutManageInfo.purchaseApplyDetailVo.length === 0" class="empty-data">
          暂无数据
        </div>
      </div>

      <div class="footer-row">
        <div class="text-right">
          <button type="button" class="btn btn-primary" @click="handleSubmit">
            {{ $t('resourceOutManage.submit') }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { listPurchaseApplys, submitResourceOut } from '@/api/resource/resourceOutManageApi'

export default {
  name: 'ResourceOutManageList',
  data() {
    return {
      loading: false,
      resourceOutManageInfo: {
        purchaseApplyDetailVo: [],
        applyOrderId: '',
        taskId: '',
        resOrderType: ''
      }
    }
  },
  created() {
    this.resourceOutManageInfo.applyOrderId = this.$route.query.applyOrderId
    this.resourceOutManageInfo.resOrderType = this.$route.query.resOrderType
    this.resourceOutManageInfo.taskId = this.$route.query.taskId
    this.getList()
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = {
          page: 1,
          row: 10,
          applyOrderId: this.resourceOutManageInfo.applyOrderId,
          resOrderType: this.resourceOutManageInfo.resOrderType
        }
        const data = await listPurchaseApplys(params)
        const purchaseApply = data.purchaseApplys[0]
        this.resourceOutManageInfo = {
          ...this.resourceOutManageInfo,
          ...purchaseApply
        }
        this.resourceOutManageInfo.purchaseApplyDetailVo.forEach(item => {
          item.purchaseQuantity = ''
          item.price = ''
          item.purchaseRemark = ''
        })
      } catch (error) {
        console.error('Failed to fetch purchase apply list:', error)
        this.$message.error(this.$t('resourceOutManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    
    // 新增：验证数量输入
    validateQuantity(item) {
      if (item.purchaseQuantity && parseFloat(item.purchaseQuantity) > parseFloat(item.stock)) {
        this.$message.warning('出库数量不能超过库存数量')
      }
    },
    
    async handleSubmit() {
      try {
        // Validate form
        let isValid = true
        this.resourceOutManageInfo.purchaseApplyDetailVo.forEach(item => {
          if (!item.purchaseQuantity || parseFloat(item.purchaseQuantity) <= 0) {
            this.$message.error(this.$t('resourceOutManage.quantityRequired'))
            isValid = false
            return
          }
          item.purchaseQuantity = parseFloat(item.purchaseQuantity)
          if (item.purchaseQuantity > parseFloat(item.stock)) {
            this.$message.error(this.$t('resourceOutManage.insufficientStock'))
            isValid = false
            return
          }
        })

        if (!isValid) return

        const response = await submitResourceOut(this.resourceOutManageInfo)
        if (response.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.$router.go(-1)
        } else {
          this.$message.error(response.msg)
        }
      } catch (error) {
        console.error('Failed to submit resource out:', error)
        this.$message.error(this.$t('resourceOutManage.submitError'))
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.resource-out-manage-container {
  padding: 20px;

  .box-card {
    background: #fff;
    border-radius: 4px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    margin-bottom: 20px;
  }

  .card-header {
    padding: 20px;
    border-bottom: 1px solid #ebeef5;
    font-size: 16px;
    font-weight: 500;
    color: #303133;
  }

  .table-container {
    position: relative;
    padding: 20px;
  }

  .loading-overlay {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(255, 255, 255, 0.8);
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 10;
  }

  .loading-spinner {
    color: #409eff;
    font-size: 14px;
  }

  .data-table {
    width: 100%;
    border-collapse: collapse;
    border: 1px solid #ebeef5;
    border-radius: 4px;
    overflow: hidden;
  }

  .table-header {
    background-color: #fafafa;
    color: #606266;
    font-weight: 500;
    padding: 12px 8px;
    text-align: center;
    border-bottom: 1px solid #ebeef5;
    font-size: 14px;
    min-width: 120px;
  }

  .table-row {
    &:hover {
      background-color: #f5f7fa;
    }
    
    &:nth-child(even) {
      background-color: #fafafa;
    }
  }

  .table-cell {
    padding: 12px 8px;
    text-align: center;
    border-bottom: 1px solid #ebeef5;
    font-size: 14px;
    color: #606266;
    vertical-align: middle;
  }

  .form-input {
    width: 100%;
    padding: 8px 12px;
    border: 1px solid #dcdfe6;
    border-radius: 4px;
    font-size: 14px;
    color: #606266;
    background-color: #fff;
    transition: border-color 0.2s;
    box-sizing: border-box;
    
    &:focus {
      outline: none;
      border-color: #409eff;
      box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
    }
    
    &::placeholder {
      color: #c0c4cc;
    }
    
    &:hover {
      border-color: #c0c4cc;
    }
  }

  .empty-data {
    text-align: center;
    padding: 40px;
    color: #909399;
    font-size: 14px;
  }

  .footer-row {
    margin-top: 20px;
    padding: 20px;
    border-top: 1px solid #ebeef5;
  }

  .text-right {
    text-align: right;
  }

  .btn {
    padding: 10px 20px;
    border: none;
    border-radius: 4px;
    font-size: 14px;
    cursor: pointer;
    transition: all 0.3s;
    
    &.btn-primary {
      background-color: #409eff;
      color: #fff;
      
      &:hover {
        background-color: #66b1ff;
      }
      
      &:active {
        background-color: #3a8ee6;
      }
      
      &:disabled {
        background-color: #a0cfff;
        cursor: not-allowed;
      }
    }
  }

  // 响应式设计
  @media (max-width: 768px) {
    .data-table {
      font-size: 12px;
    }
    
    .table-header,
    .table-cell {
      padding: 8px 4px;
      min-width: 80px;
    }
    
    .form-input {
      padding: 6px 8px;
      font-size: 12px;
    }
  }
}
</style>