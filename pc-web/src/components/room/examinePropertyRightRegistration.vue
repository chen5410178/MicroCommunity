<template>
  <el-dialog :title="$t('propertyRightRegistration.examine.title')" :visible.sync="visible" width="50%"
    @close="handleClose">
    <el-form ref="form" :model="formData" label-width="120px">
      <el-form-item :label="$t('propertyRightRegistration.examine.room')">
        <el-input v-model="formData.allNum" disabled
          :placeholder="$t('propertyRightRegistration.examine.roomPlaceholder')" />
      </el-form-item>

      <el-form-item :label="$t('propertyRightRegistration.examine.state')" prop="state"
        :rules="[{ required: true, message: $t('propertyRightRegistration.examine.stateRequired'), trigger: 'change' }]">
        <el-select v-model="formData.state" :placeholder="$t('propertyRightRegistration.examine.statePlaceholder')"
          style="width:100%">
          <el-option v-for="item in stateOptions" :key="item.statusCd" :label="item.name" :value="item.statusCd"
            :disabled="item.statusCd === '0'" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('propertyRightRegistration.examine.remark')">
        <el-input v-model="formData.remark" type="textarea" :rows="3"
          :placeholder="$t('propertyRightRegistration.examine.remarkPlaceholder')" />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getDict ,getCommunityId} from '@/api/community/communityApi'
import { updatePropertyRightRegistration } from '@/api/room/propertyRightRegistrationManageApi'

export default {
  name: 'ExaminePropertyRightRegistration',
  data() {
    return {
      visible: false,
      formData: {
        prrId: '',
        roomId: '',
        floorNum: '',
        unitNum: '',
        roomNum: '',
        allNum: '',
        state: '',
        remark: '',
        flag: '1'
      },
      stateOptions: []
    }
  },
  methods: {
    async open(data) {
      this.visible = true
      await this.getStateOptions()

      this.formData = {
        prrId: data.prrId,
        roomId: data.roomId,
        floorNum: data.floorNum,
        unitNum: data.unitNum,
        roomNum: data.roomNum,
        allNum: `${data.floorNum}-${data.unitNum}-${data.roomNum}`,
        state: data.state === '0' ? '' : data.state,
        remark: data.remark || '',
        flag: '1'
      }
    },
    async getStateOptions() {
      try {
        this.stateOptions = await getDict('property_right_registration', 'state')
      } catch (error) {
        console.error('获取审核状态失败:', error)
        this.$message.error(this.$t('propertyRightRegistration.examine.fetchStateError'))
      }
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.formData = {
        prrId: '',
        roomId: '',
        floorNum: '',
        unitNum: '',
        roomNum: '',
        allNum: '',
        state: '',
        remark: '',
        flag: '1'
      }
    },
    async handleSubmit() {
      try {
        await this.$refs.form.validate()

        const params = {
          ...this.formData,
          communityId: getCommunityId()
        }

        const res = await updatePropertyRightRegistration(params)
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.visible = false
          this.$emit('success')
        } else {
          this.$message.error(res.msg || this.$t('propertyRightRegistration.examine.error'))
        }
      } catch (error) {
        if (error !== 'validate') {
          console.error('审核失败:', error)
          this.$message.error(this.$t('propertyRightRegistration.examine.error'))
        }
      }
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>