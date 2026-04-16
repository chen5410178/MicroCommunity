<template>
  <el-dialog
    :title="$t('editUnit.title')"
    :visible.sync="visible"
    width="50%"
    :before-close="handleClose"
  >
    <el-form ref="form" :model="editUnitInfo" :rules="rules" label-width="120px">
      <el-form-item :label="$t('editUnit.unitNum')" prop="unitNum">
        <el-input
          v-model="editUnitInfo.unitNum"
          :placeholder="$t('editUnit.unitNumPlaceholder')"
        ></el-input>
      </el-form-item>
      
      <el-form-item :label="$t('editUnit.layerCount')" prop="layerCount">
        <el-input
          v-model.number="editUnitInfo.layerCount"
          type="number"
          :placeholder="$t('editUnit.layerCountPlaceholder')"
        ></el-input>
      </el-form-item>
      
      <el-form-item :label="$t('editUnit.unitArea')" prop="unitArea">
        <el-input
          v-model="editUnitInfo.unitArea"
          :placeholder="$t('editUnit.unitAreaPlaceholder')"
        ></el-input>
      </el-form-item>
      
      <el-form-item :label="$t('editUnit.lift')" prop="lift">
        <el-select 
          v-model="editUnitInfo.lift" 
          :placeholder="$t('editUnit.liftPlaceholder')"
          style="width: 100%"
        >
          <el-option 
            :label="$t('editUnit.liftOption1')" 
            value="1010"
          ></el-option>
          <el-option 
            :label="$t('editUnit.liftOption2')" 
            value="2020"
          ></el-option>
        </el-select>
      </el-form-item>
      
      <el-form-item :label="$t('editUnit.remark')">
        <el-input
          v-model="editUnitInfo.remark"
          type="textarea"
          :rows="3"
          :placeholder="$t('editUnit.remarkPlaceholder')"
        ></el-input>
      </el-form-item>
    </el-form>
    
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('editUnit.cancel') }}</el-button>
      <el-button type="primary" @click="editUnit">{{ $t('editUnit.save') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateUnit } from '@/api/room/editUnitApi'

export default {
  name: 'EditUnit',
  data() {
    return {
      visible: false,
      editUnitInfo: {
        floorId: '',
        unitId: '',
        unitNum: '',
        layerCount: '',
        lift: '',
        remark: '',
        communityId: '',
        unitArea: ''
      },
      rules: {
        unitNum: [
          { required: true, message: this.$t('editUnit.unitNumRequired'), trigger: 'blur' }
        ],
        layerCount: [
          { required: true, message: this.$t('editUnit.layerCountRequired'), trigger: 'blur' }
        ],
        unitArea: [
          { required: true, message: this.$t('editUnit.unitAreaRequired'), trigger: 'blur' }
        ],
        lift: [
          { required: true, message: this.$t('editUnit.liftRequired'), trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    open(params) {
      Object.assign(this.editUnitInfo, params)
      this.editUnitInfo.communityId = this.getCommunityId()
      this.visible = true
    },
    handleClose() {
      this.visible = false
    },
    validateForm() {
      return new Promise((resolve) => {
        this.$refs.form.validate((valid) => {
          resolve(valid)
        })
      })
    },
    async editUnit() {
      const isValid = await this.validateForm()
      if (!isValid) return
      
      if (this.editUnitInfo.unitNum === '0') {
        this.$message.warning(this.$t('editUnit.zeroUnitWarning'))
        return
      }
      
      try {
        await updateUnit(this.editUnitInfo)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('handleRefreshTree', {})

        this.handleClose()
      } catch (error) {
        console.error('修改单元失败:', error)
        this.$message.error(error.message || this.$t('editUnit.errorMessage'))
      }
    }
  }
}
</script>