<template>
  <el-dialog :title="$t('addUnit.title')" :visible.sync="visible" width="50%" :before-close="handleClose">
    <el-form ref="form" :model="addUnitInfo" :rules="rules" label-width="120px">
      <el-form-item :label="$t('addUnit.unitNum')" prop="unitNum">
        <el-input v-model="addUnitInfo.unitNum" :placeholder="$t('addUnit.unitNumPlaceholder')"></el-input>
      </el-form-item>

      <el-form-item :label="$t('addUnit.layerCount')" prop="layerCount">
        <el-input v-model.number="addUnitInfo.layerCount" type="number"
          :placeholder="$t('addUnit.layerCountPlaceholder')"></el-input>
      </el-form-item>

      <el-form-item :label="$t('addUnit.unitArea')" prop="unitArea">
        <el-input v-model="addUnitInfo.unitArea" :placeholder="$t('addUnit.unitAreaPlaceholder')"></el-input>
      </el-form-item>

      <el-form-item :label="$t('addUnit.lift')" prop="lift">
        <el-select v-model="addUnitInfo.lift" :placeholder="$t('addUnit.liftPlaceholder')" style="width: 100%">
          <el-option :label="$t('addUnit.liftOption1')" value="1010"></el-option>
          <el-option :label="$t('addUnit.liftOption2')" value="2020"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('addUnit.remark')">
        <el-input v-model="addUnitInfo.remark" type="textarea" :rows="3"
          :placeholder="$t('addUnit.remarkPlaceholder')"></el-input>
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('addUnit.cancel') }}</el-button>
      <el-button type="primary" @click="addUnit">{{ $t('addUnit.save') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { saveUnit } from '@/api/room/addUnitApi'

export default {
  name: 'AddUnit',
  data() {
    return {
      visible: false,
      addUnitInfo: {
        floorId: '',
        unitNum: '',
        layerCount: '',
        lift: '',
        remark: '',
        communityId: '',
        unitArea: ''
      },
      rules: {
        unitNum: [
          { required: true, message: this.$t('addUnit.unitNumRequired'), trigger: 'blur' }
        ],
        layerCount: [
          { required: true, message: this.$t('addUnit.layerCountRequired'), trigger: 'blur' }
        ],
        unitArea: [
          { required: true, message: this.$t('addUnit.unitAreaRequired'), trigger: 'blur' }
        ],
        lift: [
          { required: true, message: this.$t('addUnit.liftRequired'), trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    open(params) {
      this.refreshAddUnitInfo()
      if (params && params.floorId) {
        this.addUnitInfo.floorId = params.floorId
      }
      this.addUnitInfo.communityId = this.getCommunityId()
      this.visible = true
    },
    handleClose() {
      this.visible = false
    },
    refreshAddUnitInfo() {
      this.addUnitInfo = {
        floorId: '',
        unitNum: '',
        layerCount: '',
        lift: '',
        remark: '',
        communityId: '',
        unitArea: ''
      }
      this.$nextTick(() => {
        if (this.$refs.form) {
          this.$refs.form.clearValidate()
        }
      })
    },
    validateForm() {
      return new Promise((resolve) => {
        this.$refs.form.validate((valid) => {
          resolve(valid)
        })
      })
    },
    async addUnit() {
      const isValid = await this.validateForm()
      if (!isValid) return

      if (this.addUnitInfo.unitNum === '0') {
        this.$message.warning(this.$t('addUnit.zeroUnitWarning'))
        return
      }

      try {
        await saveUnit(this.addUnitInfo)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('handleRefreshTree', {})

        this.handleClose()
      } catch (error) {
        console.error('添加单元失败:', error)
        this.$message.error(error.message || this.$t('addUnit.errorMessage'))
      }
    }
  }
}
</script>