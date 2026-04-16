<template>
  <el-dialog :title="$t('deleteMaintainanceStandardItem.title')" :visible.sync="visible" width="30%" @close="handleClose">
    <el-row>
      <el-col :span="24">

        <div style="text-align: center; margin-bottom: 20px;">
          {{ $t('deleteMaintainanceStandardItem.confirmMessage') }}
        </div>
        <div style="text-align: center;">
          <el-button @click="handleClose">
            {{ $t('deleteMaintainanceStandardItem.cancel') }}
          </el-button>
          <el-button type="primary" @click="confirmDelete" style="margin-left: 20px;">
            {{ $t('deleteMaintainanceStandardItem.confirm') }}
          </el-button>
        </div>
      </el-col>
    </el-row>
  </el-dialog>
</template>

<script>
import { deleteMaintainanceStandardItem } from '@/api/inspection/maintainanceStandardItemApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteMaintainanceStandardItem',
  data() {
    return {
      visible: false,
      form: {
        itemId: '',
        msiId: '',
        standardId: ''
      }
    }
  },
  methods: {
    open(params) {
      this.form = {
        itemId: params.itemId,
        msiId: params.msiId,
        standardId: params.standardId
      }
      this.visible = true
    },
    async confirmDelete() {
      try {
        const params = {
          ...this.form,
          communityId: getCommunityId()
        }
        await deleteMaintainanceStandardItem(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.handleClose()
      } catch (error) {
        this.$message.error(this.$t('deleteMaintainanceStandardItem.deleteError'))
      }
    },
    handleClose() {
      this.visible = false
      this.resetForm()
    },
    resetForm() {
      this.form = {
        itemId: '',
        standardId: ''
      }
    }
  }
}
</script>