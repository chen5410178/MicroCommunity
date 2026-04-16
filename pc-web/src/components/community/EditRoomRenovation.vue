<template>
  <el-dialog :title="$t('roomRenovationManage.modify')" :visible.sync="visible" width="40%" @close="resetForm">
    <el-form :model="form" ref="form" label-width="120px">
      <el-form-item :label="$t('roomRenovationManage.room')" prop="roomName">
        <el-input v-model.trim="form.roomName" disabled />
      </el-form-item>

      <el-form-item :label="$t('roomRenovationManage.contactPerson')" prop="personName" required>
        <el-input v-model.trim="form.personName" :placeholder="$t('roomRenovationManage.contactPersonPlaceholder')" />
      </el-form-item>

      <el-form-item :label="$t('roomRenovationManage.phone')" prop="personTel" required>
        <el-input v-model.trim="form.personTel" :placeholder="$t('roomRenovationManage.phonePlaceholder')" />
      </el-form-item>

      <el-form-item :label="$t('roomRenovationManage.renovationStartTime')" prop="startTime" required>
        <el-date-picker v-model="form.startTime" type="date"
          :placeholder="$t('roomRenovationManage.startTimePlaceholder')" value-format="yyyy-MM-dd" />
      </el-form-item>

      <el-form-item :label="$t('roomRenovationManage.renovationEndTime')" prop="endTime" required>
        <el-date-picker v-model="form.endTime" type="date" :placeholder="$t('roomRenovationManage.endTimePlaceholder')"
          value-format="yyyy-MM-dd" />
      </el-form-item>

      <el-form-item :label="$t('roomRenovationManage.isPostponed')" prop="isPostpone" required>
        <el-select v-model="form.isPostpone">
          <el-option :label="$t('roomRenovationManage.yes')" value="Y" />
          <el-option :label="$t('roomRenovationManage.no')" value="N" />
        </el-select>
      </el-form-item>

      <el-form-item v-if="form.isPostpone === 'Y'" :label="$t('roomRenovationManage.postponeTime')" prop="postponeTime">
        <el-date-picker v-model="form.postponeTime" type="date"
          :placeholder="$t('roomRenovationManage.endTimePlaceholder')" value-format="yyyy-MM-dd" />
      </el-form-item>

      <el-form-item :label="$t('roomRenovationManage.renovationCompany')" prop="renovationCompany" required>
        <el-input v-model.trim="form.renovationCompany"
          :placeholder="$t('roomRenovationManage.renovationCompanyPlaceholder')" value-format="yyyy-MM-dd" />
      </el-form-item>

      <el-form-item :label="$t('roomRenovationManage.renovationManager')" prop="personMain" required>
        <el-input v-model.trim="form.personMain"
          :placeholder="$t('roomRenovationManage.renovationManagerPlaceholder')" />
      </el-form-item>

      <el-form-item :label="$t('roomRenovationManage.managerPhone')" prop="personMainTel" required>
        <el-input v-model.trim="form.personMainTel" :placeholder="$t('roomRenovationManage.managerPhonePlaceholder')" />
      </el-form-item>

      <!-- <el-form-item :label="$t('roomRenovationManage.status')" prop="state" required>
        <el-select v-model="form.state">
          <el-option v-for="(item, index) in states" :key="index" :label="item.name" :value="item.statusCd" />
        </el-select>
      </el-form-item> -->

      <el-form-item :label="$t('roomRenovationManage.isViolation')" prop="isViolation" required>
        <el-select v-model="form.isViolation">
          <el-option :label="$t('roomRenovationManage.yes')" value="Y" />
          <el-option :label="$t('roomRenovationManage.no')" value="N" />
        </el-select>
      </el-form-item>

      <el-form-item v-if="form.isViolation === 'Y'" :label="$t('roomRenovationManage.violationDesc')"
        prop="violationDesc">
        <el-input v-model.trim="form.violationDesc" type="textarea"
          :placeholder="$t('roomRenovationManage.violationDescPlaceholder')" />
      </el-form-item>

      <el-form-item :label="$t('roomRenovationManage.remark')" prop="remark">
        <el-input v-model.trim="form.remark" type="textarea"
          :placeholder="$t('roomRenovationManage.remarkPlaceholder')" />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('roomRenovationManage.cancel') }}
      </el-button>
      <el-button type="primary" @click="editRoomRenovation">
        {{ $t('roomRenovationManage.save') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { updateRoomRenovation } from '@/api/community/roomRenovationManageApi'
import { getDict } from '@/api/community/communityApi'
import { dateFormat } from '@/utils/dateUtil'
export default {
  name: 'EditRoomRenovation',
  data() {
    return {
      visible: false,
      states: [],
      form: {
        rId: '',
        roomName: '',
        personName: '',
        personTel: '',
        startTime: '',
        endTime: '',
        isPostpone: 'N',
        postponeTime: '',
        renovationCompany: '',
        personMain: '',
        personMainTel: '',
        state: '',
        isViolation: '',
        violationDesc: '',
        remark: '',
        communityId: ''
      }
    }
  },
  watch: {
    'form.isPostpone'(val) {
      if (val === 'N') {
        this.form.postponeTime = ''
      }
    }
  },
  methods: {
    open(row) {
      this.getDictData()
      this.form = {
        ...row,
        communityId: this.getCommunityId()
      }
      this.form.startTime = dateFormat(this.form.startTime)
      this.form.endTime = dateFormat(this.form.endTime)

      this.visible = true
    },

    resetForm() {
      this.form = {
        rId: '',
        roomName: '',
        personName: '',
        personTel: '',
        startTime: '',
        endTime: '',
        isPostpone: 'N',
        postponeTime: '',
        renovationCompany: '',
        personMain: '',
        personMainTel: '',
        state: '',
        isViolation: '',
        violationDesc: '',
        remark: '',
        communityId: ''
      }
    },

    async getDictData() {
      try {
        const res = await getDict('room_renovation','state')
        this.states = res || []
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },

    async editRoomRenovation() {
      try {
        await updateRoomRenovation(this.form)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        console.error('更新装修信息失败:', error)
        this.$message.error(error.message || this.$t('common.operationFailed'))
      }
    }
  }
}
</script>
<style scoped>
.el-date-editor{  
  width: 100%;
}
.el-date-editor.el-input{
  width: 100%;
}
.el-form-item{
  width: 95%;
}
</style>