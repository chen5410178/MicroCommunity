<template>
  <el-dialog
    :title="$t('equipmentAccount.changeState')"
    :visible.sync="dialogVisible"
    width="50%"
    @close="handleClose">
    <el-form ref="form" :model="formData" label-width="120px">
      <el-form-item :label="$t('equipmentAccount.state')" prop="state" required>
        <el-select 
          v-model="formData.state" 
          :placeholder="$t('equipmentAccount.selectState')"
          style="width:100%">
          <el-option
            v-for="item in useStatus"
            :key="item.statusCd"
            :label="item.name"
            :value="item.statusCd">
          </el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import { changeStateEquipment } from '@/api/machine/equipmentAccountApi'

export default {
  name: 'ChangeStateEquipment',
  data() {
    return {
      dialogVisible: false,
      communityId: '',
      useStatus: [],
      formData: {
        machineId: '',
        state: '',
        communityId: ''
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.loadDictData()
  },
  methods: {
    async loadDictData() {
      try {
        const data = await getDict('equipment_account', 'state')
        this.useStatus = data
      } catch (error) {
        console.error('获取状态字典失败:', error)
      }
    },
    open(data) {
      this.formData = {
        machineId: data.machineId,
        state: data.state,
        communityId: this.communityId
      }
      this.dialogVisible = true
    },
    handleClose() {
      this.$refs.form.resetFields()
    },
    async handleSubmit() {
      try {
        await changeStateEquipment(this.formData)
        this.$message.success(this.$t('common.operationSuccess'))
        this.dialogVisible = false
        this.$emit('success')
      } catch (error) {
        console.error('修改设备状态失败:', error)
      }
    }
  }
}
</script>