<template>
  <el-dialog
    :title="$t('common.confirmOperation')"
    :visible.sync="visible"
    width="30%"
  >
    <div style="text-align: center;">
      <p>{{$t('componentConditionManage.confirmDelete')}}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{$t('common.cancel')}}</el-button>
      <el-button type="primary" @click="handleConfirm">{{$t('common.confirm')}}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteReportCustomComponentCondition } from '@/api/report/componentConditionManageApi'

export default {
  name: 'DeleteComponentCondition',
  data() {
    return {
      visible: false,
      conditionId: ''
    }
  },
  methods: {
    open(params) {
      this.conditionId = params.conditionId
      this.visible = true
    },
    async handleConfirm() {
      try {
        await deleteReportCustomComponentCondition({ conditionId: this.conditionId })
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(error.message || this.$t('common.deleteFailed'))
      }
    }
    }
    }
    </script>