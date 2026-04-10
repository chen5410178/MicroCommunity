<template>
  <el-dialog
    :title="$t('common.edit')"
    :visible.sync="dialogVisible"
    width="600px"
    :before-close="handleClose"
  >
    <el-form
      ref="addFeeTypeForm"
      :model="addFeeTypeInfo"
      :rules="rules"
      label-width="100px"
    >
      <el-form-item 
        :label="$t('addFeeType.typeName')" 
        prop="ftName"
      >
        <el-input
          v-model="addFeeTypeInfo.ftName"
          :placeholder="$t('addFeeType.typeNamePlaceholder')"
          clearable
        />
      </el-form-item>
      

    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">
        {{ $t('addFeeType.cancel') }}
      </el-button>
      <el-button type="primary" @click="saveFeeTypeInfo" :loading="loading">
        {{ $t('addFeeType.save') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { updateFeeType } from '@/api/fee/feeConfigManageApi'

export default {
  name: 'AddFeeType',
  data() {
    return {
      dialogVisible: false,
      loading: false,
      addFeeTypeInfo: {
        ftId: '',
        feeTypeCd: '',
        ftName: '',
        monthAmount: '0.00'
      },
      rules: {
        ftName: [
          { required: true, message: this.$t('addFeeType.validate.typeNameRequired'), trigger: 'blur' },
          { max: 100, message: this.$t('addFeeType.validate.typeNameMaxLength'), trigger: 'blur' }
        ],
        monthAmount: [
          { required: true, message: this.$t('addFeeType.validate.monthBaseRequired'), trigger: 'blur' },
          { 
            pattern: /^\d+(\.\d{1,2})?$/, 
            message: this.$t('addFeeType.validate.monthBaseFormat'), 
            trigger: 'blur' 
          }
        ]
      }
    }
  },
  methods: {
    open(params) {
      this.dialogVisible = true
      this.$nextTick(() => {
        this.resetForm()
        if (params) {
          Object.assign(this.addFeeTypeInfo, params)
        }
      })
    },
    
    handleClose() {
      this.dialogVisible = false
      this.resetForm()
    },
    
    resetForm() {
      this.addFeeTypeInfo = {
        ftId: '',
        feeTypeCd: '',
        ftName: '',
        monthAmount: '0.00'
      }
      if (this.$refs.addFeeTypeForm) {
        this.$refs.addFeeTypeForm.clearValidate()
      }
    },
    
    async saveFeeTypeInfo() {
      try {
        const valid = await this.$refs.addFeeTypeForm.validate()
        if (!valid) return
        
        this.loading = true
        
        const params = {
          ...this.addFeeTypeInfo,
          communityId: getCommunityId()
        }
        
        await updateFeeType(params)
        
        this.$message.success(this.$t('common.operationSuccess'))
        this.handleClose()
        
        // 通知父组件刷新树
        this.$parent.$emit('listFeeTypeTree')
        
      } catch (error) {
        console.error('保存费用类型失败:', error)
        this.$message.error(error.message || this.$t('addFeeType.saveError'))
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep .el-dialog {
  .el-dialog__body {
    padding: 20px;
  }
  
  .el-form-item {
    margin-bottom: 20px;
  }
}

.dialog-footer {
  text-align: right;
}
</style>