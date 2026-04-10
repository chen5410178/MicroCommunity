<template>
  <el-dialog :title="$t('room.editFloor.title')" :visible.sync="visible" width="50%" :before-close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item :label="$t('room.editFloor.floorNum')" prop="floorNum">
        <el-input v-model="form.floorNum" :placeholder="$t('room.editFloor.floorNumPlaceholder')" />
      </el-form-item>

      <el-form-item :label="$t('room.editFloor.floorName')" prop="floorName">
        <el-input v-model="form.floorName" :placeholder="$t('room.editFloor.floorNamePlaceholder')" />
      </el-form-item>

      <el-form-item :label="$t('room.editFloor.floorArea')" prop="floorArea">
        <el-input v-model="form.floorArea" :placeholder="$t('room.editFloor.floorAreaPlaceholder')">
          <template slot="append">m²</template>
        </el-input>
      </el-form-item>

      <el-form-item :label="$t('room.editFloor.seq')" class="text-left" prop="seq">
        <el-input-number v-model="form.seq" :min="1" />
      </el-form-item>

      <el-form-item :label="$t('room.editFloor.remark')" prop="remark">
        <el-input v-model="form.remark" type="textarea" :placeholder="$t('room.editFloor.remarkPlaceholder')" />
      </el-form-item>

      <el-alert v-if="errorInfo" :title="errorInfo" type="error" show-icon />
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleUpdate">{{ $t('common.update') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { editFloor } from '@/api/room/editFloorApi'

export default {
  name: 'EditFloor',
  data() {
    return {
      visible: false,
      form: {
        floorId: '',
        floorNum: '',
        floorName: '',
        floorArea: '',
        seq: 1,
        remark: '',
        communityId: this.getCommunityId()
      },
      errorInfo: '',
      rules: {
        floorNum: [
          { required: true, message: this.$t('room.editFloor.floorNumRequired'), trigger: 'blur' },
          { min: 1, max: 64, message: this.$t('room.editFloor.floorNumLength'), trigger: 'blur' }
        ],
        floorName: [
          { required: true, message: this.$t('room.editFloor.floorNameRequired'), trigger: 'blur' },
          { min: 2, max: 64, message: this.$t('room.editFloor.floorNameLength'), trigger: 'blur' }
        ],
        floorArea: [
          { required: true, message: this.$t('room.editFloor.floorAreaRequired'), trigger: 'blur' },
          { pattern: /^\d+(\.\d{1,2})?$/, message: this.$t('room.editFloor.floorAreaFormat'), trigger: 'blur' }
        ],
        seq: [
          { required: true, message: this.$t('room.editFloor.seqRequired'), trigger: 'blur' },
          { type: 'number', message: this.$t('room.editFloor.seqNumber'), trigger: 'blur' }
        ],
        remark: [
          { max: 200, message: this.$t('room.editFloor.remarkMaxLength'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(floorInfo) {
      this.form = {
        ...floorInfo,
        communityId: this.getCommunityId()
      }
      this.visible = true
      this.errorInfo = ''
    },
    handleClose() {
      this.visible = false
      this.resetForm()
    },
    resetForm() {
      this.form = {
        floorId: '',
        floorNum: '',
        floorName: '',
        floorArea: '',
        seq: 1,
        remark: '',
        communityId: this.getCommunityId()
      }
      this.errorInfo = ''
    },
    async handleUpdate() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) return

        try {
          const payload = {
            ...this.form,
            name: this.form.floorName
          }

          const response = await editFloor(payload)

          if (response.code == 0) {
            this.$message.success(this.$t('common.operationSuccess'))
            this.handleClose()
            this.$emit('handleRefreshTree', {})

          } else {
            this.errorInfo = response.msg || this.$t('room.editFloor.updateFailed')
          }
        } catch (error) {
          this.errorInfo = this.$t('room.editFloor.updateError')
        }
      })
    },
  }
}
</script>