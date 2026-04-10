<template>
  <el-dialog
    :title="$t('ownerVoting.publish.title')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form ref="form" :model="form" :rules="rules">
      <el-form-item
        :label="$t('ownerVoting.publish.name')"
        prop="qaName"
        label-width="120px"
      >
        <el-input v-model="form.qaName" disabled style="width: 100%" />
      </el-form-item>

      <el-form-item
        :label="$t('ownerVoting.publish.tip')"
        label-width="120px"
      >
        <el-input
          :value="$t('ownerVoting.publish.tipContent')"
          disabled
          style="width: 100%"
        />
      </el-form-item>

      <el-form-item
        :label="$t('ownerVoting.publish.notifyWay')"
        prop="notifyWay"
        label-width="120px"
      >
        <el-select
          v-model="form.notifyWay"
          :placeholder="$t('ownerVoting.publish.notifyWayPlaceholder')"
          style="width: 100%"
        >
          <el-option
            :label="$t('ownerVoting.publish.sms')"
            value="1001"
          />
          <el-option
            :label="$t('ownerVoting.publish.wechat')"
            value="2002"
          />
          <el-option
            :label="$t('ownerVoting.publish.noNotify')"
            value="3003"
          />
        </el-select>
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button
        type="primary"
        @click="handleConfirm"
      >
        {{ $t('ownerVoting.publish.publishBtn') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { publishQuestion } from '@/api/oa/ownerVotingApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'PublishQuestionAnswer',
  data() {
    return {
      visible: false,
      form: {
        qaId: '',
        qaName: '',
        notifyWay: '',
        communityId: getCommunityId()
      },
      rules: {
        notifyWay: [
          { required: true, message: this.$t('ownerVoting.publish.notifyWayRequired'), trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    open(data) {
      this.form = {
        ...this.form,
        qaId: data.qaId,
        qaName: data.qaName
      }
      this.visible = true
    },
    async handleConfirm() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await publishQuestion(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.$emit('success')
            this.visible = false
          } catch (error) {
            this.$message.error(error.message || this.$t('ownerVoting.publish.error'))
          }
        }
      })
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.form = {
        qaId: '',
        qaName: '',
        notifyWay: '',
        communityId: getCommunityId()
      }
    }
  }
}
</script>