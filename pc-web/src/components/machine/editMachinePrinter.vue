<template>
  <el-dialog
    :title="$t('machinePrinterManage.edit.title')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
    >
      <el-form-item
        :label="$t('machinePrinterManage.form.machineName')"
        prop="machineName"
      >
        <el-input
          v-model="form.machineName"
          :placeholder="$t('machinePrinterManage.form.machineNamePlaceholder')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('machinePrinterManage.form.machineCode')"
        prop="machineCode"
      >
        <el-input
          v-model="form.machineCode"
          :placeholder="$t('machinePrinterManage.form.machineCodePlaceholder')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('machinePrinterManage.form.implBean')"
        prop="implBean"
      >
        <el-select
          v-model="form.implBean"
          :placeholder="$t('machinePrinterManage.form.implBeanPlaceholder')"
          style="width: 100%"
        >
          <el-option
            v-for="item in implBeans"
            :key="item.statusCd"
            :label="item.name"
            :value="item.statusCd"
          />
        </el-select>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleSubmit">
        {{ $t('common.confirm') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateMachinePrinter } from '@/api/machine/machinePrinterManageApi'
import { getCommunityId } from '@/api/community/communityApi'
import { getDict } from '@/api/community/communityApi'

export default {
  name: 'EditMachinePrinter',
  data() {
    return {
      visible: false,
      form: {
        machineId: '',
        machineName: '',
        machineCode: '',
        implBean: '',
        communityId: getCommunityId()
      },
      implBeans: [],
      rules: {
        machineId: [
          { required: true, message: this.$t('machinePrinterManage.validate.machineIdRequired'), trigger: 'blur' }
        ],
        machineName: [
          { required: true, message: this.$t('machinePrinterManage.validate.machineNameRequired'), trigger: 'blur' },
          { max: 200, message: this.$t('machinePrinterManage.validate.machineNameMaxLength'), trigger: 'blur' }
        ],
        machineCode: [
          { required: true, message: this.$t('machinePrinterManage.validate.machineCodeRequired'), trigger: 'blur' },
          { max: 30, message: this.$t('machinePrinterManage.validate.machineCodeMaxLength'), trigger: 'blur' }
        ],
        implBean: [
          { required: true, message: this.$t('machinePrinterManage.validate.implBeanRequired'), trigger: 'change' },
          { max: 30, message: this.$t('machinePrinterManage.validate.implBeanMaxLength'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(row) {
      this.form = { ...row }
      this.visible = true
      this.getDictData()
    },
    async getDictData() {
      try {
        const data = await getDict('machine_printer', 'impl_bean')
        this.implBeans = data
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    handleClose() {
      this.$refs.form.resetFields()
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await updateMachinePrinter(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            console.error('修改失败:', error)
          }
        }
      })
    }
  }
}
</script>