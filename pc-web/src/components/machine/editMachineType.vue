<template>
  <el-dialog :title="$t('machineTypeTree.edit')" :visible.sync="dialogVisible" width="40%" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px" class="text-left">
      <el-form-item :label="$t('machineTypeTree.machineTypeName')" prop="machineTypeName">
        <el-input v-model.trim="form.machineTypeName"
          :placeholder="$t('machineTypeTree.requiredPlaceholder', { field: $t('machineTypeTree.machineTypeName') })"></el-input>
      </el-form-item>
      <el-form-item :label="$t('machineTypeTree.machineTypeCode')" prop="machineTypeCode">
        <el-input v-model.trim="form.machineTypeCode"
          :placeholder="$t('machineTypeTree.requiredPlaceholder', { field: $t('machineTypeTree.machineTypeCode') })"></el-input>
      </el-form-item>
      <el-form-item :label="$t('machineTypeTree.status')" prop="isEnable">
        <el-radio-group v-model="form.isEnable">
          <el-radio :label="'1000'">{{ $t('machineTypeTree.enable') }}</el-radio>
          <el-radio :label="'2000'">{{ $t('machineTypeTree.disable') }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item :label="$t('machineTypeTree.importanceLevel')" prop="importanceLevel">
        <el-select v-model="form.importanceLevel"
          :placeholder="$t('machineTypeTree.requiredPlaceholder', { field: $t('machineTypeTree.importanceLevel') })"
          style="width:100%">
          <el-option v-for="item in importanceLevels" :key="item.statusCd" :label="item.name"
            :value="item.statusCd"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('machineTypeTree.seq')" prop="seq">
        <el-input-number v-model="form.seq"
          :placeholder="$t('machineTypeTree.requiredPlaceholder', { field: $t('machineTypeTree.seq') })"
          controls-position="right" :min="1" style="width:100%"></el-input-number>
      </el-form-item>
      <el-form-item :label="$t('machineTypeTree.unit')">
        <el-input v-model.trim="form.unit"
          :placeholder="$t('machineTypeTree.optionalPlaceholder', { field: $t('machineTypeTree.unit') })"></el-input>
      </el-form-item>
      <el-form-item :label="$t('machineTypeTree.warrantyDeadline')">
        <el-date-picker v-model="form.warrantyDeadline" type="date"
          :placeholder="$t('machineTypeTree.optionalPlaceholder', { field: $t('machineTypeTree.warrantyDeadline') })"
          style="width:100%" value-format="yyyy-MM-dd"></el-date-picker>
      </el-form-item>
      <el-form-item :label="$t('machineTypeTree.remark')">
        <el-input v-model.trim="form.remark" type="textarea"
          :placeholder="$t('machineTypeTree.optionalPlaceholder', { field: $t('machineTypeTree.remark') })"></el-input>
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateMachineType, listMachineType } from '@/api/machine/machineTypeTreeManageApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'EditMachineType',
  data() {
    return {
      dialogVisible: false,
      form: {
        typeId: '',
        machineTypeName: '',
        machineTypeCode: '',
        isEnable: '',
        importanceLevel: '',
        unit: '',
        warrantyDeadline: '',
        seq: 1,
        remark: '',
        communityId: ''
      },
      importanceLevels: [],
      rules: {
        machineTypeName: [
          { required: true, message: this.$t('machineTypeTree.requiredMessage', { field: this.$t('machineTypeTree.machineTypeName') }), trigger: 'blur' },
          { max: 30, message: this.$t('machineTypeTree.maxLengthMessage', { length: 30 }), trigger: 'blur' }
        ],
        machineTypeCode: [
          { required: true, message: this.$t('machineTypeTree.requiredMessage', { field: this.$t('machineTypeTree.machineTypeCode') }), trigger: 'blur' },
          { max: 30, message: this.$t('machineTypeTree.maxLengthMessage', { length: 30 }), trigger: 'blur' }
        ],
        isEnable: [
          { required: true, message: this.$t('machineTypeTree.requiredMessage', { field: this.$t('machineTypeTree.status') }), trigger: 'change' }
        ],
        importanceLevel: [
          { required: true, message: this.$t('machineTypeTree.requiredMessage', { field: this.$t('machineTypeTree.importanceLevel') }), trigger: 'change' }
        ],
        seq: [
          { required: true, message: this.$t('machineTypeTree.requiredMessage', { field: this.$t('machineTypeTree.seq') }), trigger: 'blur' }
        ],
        typeId: [
          { required: true, message: this.$t('machineTypeTree.requiredMessage', { field: 'ID' }), trigger: 'blur' },
          { max: 30, message: this.$t('machineTypeTree.maxLengthMessage', { length: 30 }), trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.form.communityId = getCommunityId()
    this.getDictData()
  },
  methods: {
    async getDictData() {
      try {
        const data = await getDict('machine_type', 'importance_level')
        this.importanceLevels = data
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    open(data) {
      this.form = {
        ...this.form,
        ...data
      }
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.clearValidate()
      })
      this._loadMachienType()
    },
    async _loadMachienType() {

      const res = await listMachineType({ communityId: getCommunityId(), typeId: this.form.typeId, page: 1, row: 1 });
      this.form = { ...res.data[0] }
    },
    handleClose() {
      this.$refs.form.resetFields()
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await updateMachineType(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.dialogVisible = false
            this.$emit('success')
          } catch (error) {
            console.error('修改设备类型失败:', error)
          }
        }
      })
    }
  }
}
</script>