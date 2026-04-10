<template>
  <el-dialog
    :title="$t('contractTypeManage.edit.title')"
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
        :label="$t('contractTypeManage.edit.typeName')"
        prop="typeName"
      >
        <el-input
          v-model="form.typeName"
          :placeholder="$t('contractTypeManage.edit.typeNamePlaceholder')"
        />
      </el-form-item>

      <el-form-item
        :label="$t('contractTypeManage.edit.audit')"
        prop="audit"
      >
        <el-select
          v-model="form.audit"
          :placeholder="$t('contractTypeManage.edit.auditPlaceholder')"
          style="width:100%"
        >
          <el-option
            v-for="item in audits"
            :key="item.statusCd"
            :label="item.name"
            :value="item.statusCd"
          />
        </el-select>
      </el-form-item>

      <el-form-item
        :label="$t('contractTypeManage.edit.remark')"
        prop="remark"
      >
        <el-input
          v-model="form.remark"
          type="textarea"
          :rows="3"
          :placeholder="$t('contractTypeManage.edit.remarkPlaceholder')"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="editContractType">
        {{ $t('common.save') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import { updateContractType } from '@/api/contract/contractTypeManageApi'

export default {
  name: 'EditContractType',
  data() {
    return {
      visible: false,
      form: {
        contractTypeId: '',
        typeName: '',
        audit: '',
        remark: '',
        communityId: ''
      },
      audits: [],
      rules: {
        contractTypeId: [
          { required: true, message: this.$t('contractTypeManage.validate.contractTypeIdRequired'), trigger: 'blur' }
        ],
        typeName: [
          { required: true, message: this.$t('contractTypeManage.validate.typeNameRequired'), trigger: 'blur' },
          { max: 64, message: this.$t('contractTypeManage.validate.typeNameMaxLength'), trigger: 'blur' }
        ],
        audit: [
          { required: true, message: this.$t('contractTypeManage.validate.auditRequired'), trigger: 'change' }
        ],
        remark: [
          { max: 200, message: this.$t('contractTypeManage.validate.remarkMaxLength'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(row) {
      this.form = {
        contractTypeId: row.contractTypeId,
        typeName: row.typeName,
        audit: row.audit,
        remark: row.remark,
        communityId: getCommunityId()
      }
      this.getAuditOptions()
      this.visible = true
    },
    async getAuditOptions() {
      try {
        const data = await getDict('contract_type', 'audit')
        this.audits = data
      } catch (error) {
        console.error('获取审核选项失败:', error)
      }
    },
    async editContractType() {
      this.$refs.form.validate(async valid => {
        if (!valid) return

        try {
          const res = await updateContractType(this.form)
          
          if (res.code === 0) {
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } else {
            this.$message.error(res.msg)
          }
        } catch (error) {
          this.$message.error(this.$t('common.operateFailed'))
        }
      })
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.form = {
        contractTypeId: '',
        typeName: '',
        audit: '',
        remark: '',
        communityId: ''
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.el-select {
  width: 100%;
}
</style>