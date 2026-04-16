<template>
  <el-dialog
    :title="$t('contractTypeManage.add.title')"
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
        :label="$t('contractTypeManage.add.typeName')"
        prop="typeName"
      >
        <el-input
          v-model="form.typeName"
          :placeholder="$t('contractTypeManage.add.typeNamePlaceholder')"
        />
      </el-form-item>

      <el-form-item
        :label="$t('contractTypeManage.add.audit')"
        prop="audit"
      >
        <el-select
          v-model="form.audit"
          :placeholder="$t('contractTypeManage.add.auditPlaceholder')"
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
        :label="$t('contractTypeManage.add.remark')"
        prop="remark"
      >
        <el-input
          v-model="form.remark"
          type="textarea"
          :rows="3"
          :placeholder="$t('contractTypeManage.add.remarkPlaceholder')"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="saveContractTypeInfo">
        {{ $t('common.save') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import { saveContractType } from '@/api/contract/contractTypeManageApi'

export default {
  name: 'AddContractType',
  data() {
    return {
      visible: false,
      form: {
        typeName: '',
        audit: '',
        remark: '',
        communityId: ''
      },
      audits: [],
      rules: {
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
    open() {
      this.visible = true
      this.getAuditOptions()
    },
    async getAuditOptions() {
      try {
        const data = await getDict('contract_type', 'audit')
        this.audits = data
      } catch (error) {
        console.error('获取审核选项失败:', error)
      }
    },
    async saveContractTypeInfo() {
      this.$refs.form.validate(async valid => {
        if (!valid) return

        try {
          this.form.communityId = getCommunityId()
          const res = await saveContractType(this.form)
          
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