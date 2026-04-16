<template>
  <el-dialog :title="$t('common.update')" :visible.sync="visible" width="50%" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px" label-position="right">
      <el-form-item :label="$t('editCommunityWechat.name')" prop="name">
        <el-input v-model="form.name" :placeholder="$t('editCommunityWechat.placeholder.name')" />
      </el-form-item>

      <el-form-item label="APPID" prop="appId">
        <el-input v-model="form.appId" :placeholder="$t('editCommunityWechat.placeholder.appId')" />
      </el-form-item>

      <el-form-item :label="$t('editCommunityWechat.appSecret')" prop="appSecret">
        <el-input v-model="form.appSecret" type="password" show-password
          :placeholder="$t('editCommunityWechat.placeholder.appSecret')" />
      </el-form-item>

      <el-form-item :label="$t('editCommunityWechat.description')" prop="remarks">
        <el-input v-model="form.remarks" type="textarea" :placeholder="$t('editCommunityWechat.placeholder.remarks')"
          :rows="3" />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('communityWechat.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleSubmit">
        {{ $t('communityWechat.save') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { updateAdminSmallWeChat } from '@/api/community/communityWechatApi'

export default {
  name: 'EditCommunityWechat',
  data() {
    return {
      visible: false,
      form: {
        wechatId: '',
        weChatId: '',
        name: '',
        appId: '',
        appSecret: '',
        remarks: '',
        payPassword: '1',
        objType: '1000',
        mchId: '1',
        mchName: '1',
        certPath: ''
      },
      rules: {
        name: [
          { required: true, message: this.$t('communityWechat.required'), trigger: 'blur' },
          { max: 100, message: this.$t('communityWechat.maxLength100'), trigger: 'blur' }
        ],
        appId: [
          { required: true, message: this.$t('communityWechat.required'), trigger: 'blur' },
          { max: 100, message: this.$t('communityWechat.maxLength100'), trigger: 'blur' }
        ],
        appSecret: [
          { required: true, message: this.$t('communityWechat.required'), trigger: 'blur' },
          { max: 200, message: this.$t('communityWechat.maxLength200'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(data) {
      this.form = {
        ...this.form,
        ...data,
        wechatId: data.wechatId || data.weChatId,
        weChatId: data.wechatId || data.weChatId
      }
      this.visible = true
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.form = {
        wechatId: '',
        weChatId: '',
        name: '',
        appId: '',
        appSecret: '',
        remarks: '',
        payPassword: '1',
        objType: '1000',
        mchId: '1',
        mchName: '1',
        certPath: ''
      }
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            const res = await updateAdminSmallWeChat(this.form)
            if (res.code == 0) {
              this.$message.success(res.msg)
              this.visible = false
              this.$emit('success')
            } else {
              this.$message.error(res.msg || this.$t('communityWechat.editFailed'))
            }
          } catch (error) {
            console.error('更新公众号失败:', error)
            this.$message.error(error)
          }
        }
      })
    }
  }
}
</script>