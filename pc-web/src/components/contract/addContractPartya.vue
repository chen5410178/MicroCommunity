<template>
  <el-dialog
    :title="$t('contractPartyaManage.add.title')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form ref="form" :model="formData" :rules="rules" label-width="120px">
      <el-form-item
        :label="$t('contractPartyaManage.form.partyA')"
        prop="partyA"
      >
        <el-input
          v-model="formData.partyA"
          :placeholder="$t('contractPartyaManage.form.partyAPlaceholder')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('contractPartyaManage.form.aContacts')"
        prop="aContacts"
      >
        <el-input
          v-model="formData.aContacts"
          :placeholder="$t('contractPartyaManage.form.aContactsPlaceholder')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('contractPartyaManage.form.aLink')"
        prop="aLink"
      >
        <el-input
          v-model="formData.aLink"
          :placeholder="$t('contractPartyaManage.form.aLinkPlaceholder')"
        />
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
import { saveContractPartya } from '@/api/contract/contractPartyaManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddContractPartya',
  data() {
    return {
      visible: false,
      formData: {
        partyA: '',
        aContacts: '',
        aLink: '',
        communityId: ''
      },
      rules: {
        partyA: [
          {
            required: true,
            message: this.$t('contractPartyaManage.validate.partyARequired'),
            trigger: 'blur'
          },
          {
            max: 200,
            message: this.$t('contractPartyaManage.validate.partyAMaxLength'),
            trigger: 'blur'
          }
        ],
        aContacts: [
          {
            required: true,
            message: this.$t('contractPartyaManage.validate.aContactsRequired'),
            trigger: 'blur'
          },
          {
            max: 64,
            message: this.$t('contractPartyaManage.validate.aContactsMaxLength'),
            trigger: 'blur'
          }
        ],
        aLink: [
          {
            required: true,
            message: this.$t('contractPartyaManage.validate.aLinkRequired'),
            trigger: 'blur'
          },
          {
            pattern: /^1[3-9]\d{9}$/,
            message: this.$t('contractPartyaManage.validate.aLinkFormat'),
            trigger: 'blur'
          }
        ]
      }
    }
  },
  methods: {
    open() {
      this.visible = true
      this.formData.communityId = getCommunityId()
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.resetFields()
      })
    },
    handleClose() {
      this.$refs.form.resetFields()
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await saveContractPartya(this.formData)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            this.$message.error(error.message || this.$t('common.addFailed'))
          }
        }
      })
    }
  }
}
</script>