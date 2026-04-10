<template>
  <el-dialog :title="$t('supplierManage.add')" :visible.sync="visible" width="800px" @close="resetForm">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item :label="$t('supplierManage.supplierType')" prop="stId">
        <el-select v-model="form.stId"
          :placeholder="$t('supplierManage.pleaseSelect') + $t('supplierManage.supplierType')" class="w-100">
          <el-option v-for="item in supplierTypes" :key="item.stId" :label="item.typeName" :value="item.stId" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('supplierManage.supplierName')" prop="supplierName">
        <el-input v-model="form.supplierName" :placeholder="$t('supplierManage.requiredField')" />
      </el-form-item>

      <el-form-item :label="$t('supplierManage.template')" prop="beanName">
        <el-select v-model="form.beanName"
          :placeholder="$t('supplierManage.pleaseSelect') + $t('supplierManage.template')" class="w-100">
          <el-option v-for="item in beanNames" :key="item.statusCd" :label="item.name" :value="item.statusCd" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('supplierManage.contactPerson')" prop="personName">
        <el-input v-model="form.personName" :placeholder="$t('supplierManage.requiredField')" />
      </el-form-item>

      <el-form-item :label="$t('supplierManage.contactTel')" prop="personTel">
        <el-input v-model="form.personTel" :placeholder="$t('supplierManage.requiredField')" type="tel" />
      </el-form-item>

      <el-form-item :label="$t('supplierManage.startTime')" prop="startTime">
        <el-date-picker v-model="form.startTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
          :placeholder="$t('supplierManage.requiredField')" class="w-100" />
      </el-form-item>

      <el-form-item :label="$t('supplierManage.endTime')" prop="endTime">
        <el-date-picker v-model="form.endTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
          :placeholder="$t('supplierManage.requiredField')" class="w-100" />
      </el-form-item>

      <el-form-item :label="$t('supplierManage.remark')" prop="remark">
        <el-input v-model="form.remark" type="textarea" :rows="3" :placeholder="$t('supplierManage.requiredField')" />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('supplierManage.cancel') }}</el-button>
      <el-button type="primary" @click="saveSupplierInfo">{{ $t('supplierManage.confirm') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveSupplier, listSupplierType } from '@/api/scm/supplierManageApi'
import {getDict} from '@/api/community/communityApi'

export default {
  name: 'AddSupplier',
  props: {
    dictData: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      visible: false,
      form: {
        stId: '',
        supplierName: '',
        beanName: '',
        personName: '',
        personTel: '',
        startTime: '',
        endTime: '',
        remark: ''
      },
      supplierTypes: [],
      beanNames: [],
      rules: {
        stId: [
          { required: true, message: this.$t('supplierManage.supplierType') + this.$t('supplierManage.requiredField'), trigger: 'blur' }
        ],
        supplierName: [
          { required: true, message: this.$t('supplierManage.supplierName') + this.$t('supplierManage.requiredField'), trigger: 'blur' }
        ],
        beanName: [
          { required: true, message: this.$t('supplierManage.template') + this.$t('supplierManage.requiredField'), trigger: 'blur' }
        ],
        personName: [
          { required: true, message: this.$t('supplierManage.contactPerson') + this.$t('supplierManage.requiredField'), trigger: 'blur' }
        ],
        personTel: [
          { required: true, message: this.$t('supplierManage.contactTel') + this.$t('supplierManage.requiredField'), trigger: 'blur' }
        ],
        startTime: [
          { required: true, message: this.$t('supplierManage.startTime') + this.$t('supplierManage.requiredField'), trigger: 'blur' }
        ],
        endTime: [
          { required: true, message: this.$t('supplierManage.endTime') + this.$t('supplierManage.requiredField'), trigger: 'blur' }
        ],
        remark: [
          { required: true, message: this.$t('supplierManage.remark') + this.$t('supplierManage.requiredField'), trigger: 'blur' }
        ]
      }
    }
  },
  mounted() {
    this.listSupplierTypes()
  },
  methods: {
    async open() {
      this.visible = true
      const data = await getDict('supplier','bean_name')
      this.beanNames = data
    },

    async listSupplierTypes() {
      try {
        const res = await listSupplierType({ page: 1, row: 100 })
        this.supplierTypes = res.data
      } catch (error) {
        console.error('查询供应商类型失败:', error)
      }
    },

    async saveSupplierInfo() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            const res = await saveSupplier(this.form)
            if (res.code === 0) {
              this.$message.success(this.$t('common.operationSuccess'))
              this.visible = false
              this.$emit('success')
            } else {
              this.$message.error(res.msg || this.$t('common.saveFailed'))
            }
          } catch (error) {
            console.error('保存供应商失败:', error)
            this.$message.error(this.$t('common.saveFailed'))
          }
        }
      })
    },

    resetForm() {
      this.$refs.form.resetFields()
    }
  }
}
</script>

<style scoped>
.w-100 {
  width: 100%;
}
</style>