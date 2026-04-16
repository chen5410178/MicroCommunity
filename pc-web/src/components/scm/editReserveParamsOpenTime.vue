<template>
  <el-dialog
    :title="$t('reserveParamsManage.setTimeTitle')"
    :visible.sync="visible"
    width="80%"
    @close="handleClose"
  >
    <el-row :gutter="20">
      <el-col
        v-for="(item, index) in formData.openTimes"
        :key="index"
        :span="6"
        class="time-item"
      >
        <div class="time-wrapper">
          <span class="time-label">{{ item.hours }}{{ $t('reserveParamsManage.hour') }}</span>
          <el-select
            v-model="item.isOpen"
            :placeholder="$t('reserveParamsManage.selectStatus')"
            @change="handleStatusChange(item)"
          >
            <el-option
              :label="$t('reserveParamsManage.canReserve')"
              value="Y"
            />
            <el-option
              :label="$t('reserveParamsManage.cannotReserve')"
              value="N"
            />
          </el-select>
        </div>
      </el-col>
    </el-row>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.close') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { updateReserveParamsOpenTime } from '@/api/scm/reserveParamsManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'EditReserveParamsOpenTime',
  data() {
    return {
      visible: false,
      formData: {
        openTimes: []
      }
    }
  },
  methods: {
    open(row) {
      this.visible = true
      this.formData.openTimes = row.openTimes.map(item => ({
        ...item,
        paramsId: row.paramsId,
        communityId: getCommunityId()
      }))
    },
    async handleStatusChange(item) {
      try {
        await updateReserveParamsOpenTime(item)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
      } catch (error) {
        this.$message.error(error.message || this.$t('common.updateFailed'))
      }
    },
    handleClose() {
      this.formData.openTimes = []
    }
  }
}
</script>

<style scoped>
.time-item {
  margin-bottom: 20px;
}

.time-wrapper {
  display: flex;
  align-items: center;
}

.time-label {
  width: 60px;
  margin-right: 10px;
  text-align: right;
}
</style>