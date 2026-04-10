<template>
  <el-dialog :title="$t('editMachineTranslate.title')" :visible.sync="visible" width="50%">
    <el-form :model="form" label-width="120px">
      <el-form-item :label="$t('editMachineTranslate.machineCode')">
        <el-input v-model="form.machineCode" disabled></el-input>
      </el-form-item>
      <el-form-item :label="$t('editMachineTranslate.objectType')">
        <el-input v-model="form.typeCdName" disabled></el-input>
      </el-form-item>
      <el-form-item :label="$t('editMachineTranslate.objectName')">
        <el-input v-model="form.objName" disabled></el-input>
      </el-form-item>
      <el-form-item :label="$t('editMachineTranslate.remark')">
        <el-input type="textarea" v-model="form.remark"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{$t('common.cancel')}}</el-button>
      <el-button type="primary" @click="handleSubmit">{{$t('common.confirm')}}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { resyncMachineTranslate } from '@/api/property/editMachineTranslateApi'

export default {
  name: 'EditMachineTranslate',
  data() {
    return {
      visible: false,
      form: {
        machineTranslateId: '',
        machineCode: '',
        typeCdName: '',
        objName: '',
        remark: ''
      }
    }
  },
  methods: {
    open(data) {
      this.form = {
        machineTranslateId: data.machineTranslateId,
        machineCode: data.machineCode,
        typeCdName: data.typeCdName,
        objName: data.objName,
        remark: data.remark || ''
      }
      this.visible = true
    },
    handleSubmit() {
      resyncMachineTranslate(this.form).then(response => {
        console.log(response)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('refresh')
      }).catch(error => {
        this.$message.error(error.message)
      })
    }
  }
}
</script>