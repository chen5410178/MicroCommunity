<template>
  <el-dialog :visible="visible" :title="$t('editRepairTypeUser.title')" width="40%" @close="handleClose">
    <el-form ref="form" :model="form" label-width="120px">
      <el-form-item :label="$t('editRepairTypeUser.status')" prop="state" required>
        <el-select v-model="form.state" style="width: 100%">
          <el-option value="9999" :label="$t('editRepairTypeUser.online')"></el-option>
          <el-option value="8888" :label="$t('editRepairTypeUser.offline')"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('editRepairTypeUser.description')" prop="remark">
        <el-input v-model="form.remark" :placeholder="$t('editRepairTypeUser.descriptionPlaceholder')"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="saveChanges">{{ $t('common.save') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { updateRepairTypeUser } from '@/api/work/repairTypeUserApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'EditRepairTypeUser',
  data() {
    return {
      visible: false,
      form: {
        typeUserId: '',
        state: '',
        remark: '',
        repairType: ''
      }
    }
  },
  methods: {
    open(repairTypeUser) {
      this.form = {
        typeUserId: repairTypeUser.typeUserId,
        repairType: repairTypeUser.repairType,
        state: repairTypeUser.state,
        remark: repairTypeUser.remark
      }
      this.visible = true
    },
    handleClose() {
      this.visible = false
      this.form = {
        typeUserId: '',
        state: '',
        remark: '',
        repairType: ''
      }
    },
    async saveChanges() {
      try {
        if (!this.form.state) {
          this.$message.warning(this.$t('editRepairTypeUser.statusRequired'))
          return
        }

        const communityId = await getCommunityId()
        const data = {
          ...this.form,
          communityId: communityId
        }

        const response = await updateRepairTypeUser(data)
        if (response.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.$emit('success')
          this.handleClose()
        } else {
          this.$message.error(response.msg || this.$t('common.saveFailed'))
        }
      } catch (error) {
        console.error('Error updating repair type user:', error)
        this.$message.error(this.$t('common.saveFailed'))
      }
    }
  }
}
</script>