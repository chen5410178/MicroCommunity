<template>
  <el-dialog :title="$t('addCommunityWechat.title')" :visible.sync="visible" width="50%" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px" label-position="right">
      <el-form-item :label="$t('addCommunityWechat.name')" prop="name">
        <el-input v-model="form.name" :placeholder="$t('addCommunityWechat.placeholder.name')" />
      </el-form-item>

      <el-form-item label="APPID" prop="appId">
        <el-input v-model="form.appId" :placeholder="$t('addCommunityWechat.placeholder.appId')" />
      </el-form-item>

      <el-form-item :label="$t('addCommunityWechat.appSecret')" prop="appSecret">
        <el-input v-model="form.appSecret" type="password" show-password
          :placeholder="$t('addCommunityWechat.placeholder.appSecret')" />
      </el-form-item>

      <el-form-item :label="$t('addCommunityWechat.description')" prop="remarks">
        <el-input v-model="form.remarks" type="textarea" :placeholder="$t('addCommunityWechat.placeholder.remarks')"
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
import { saveAdminSmallWeChat } from '@/api/community/communityWechatApi'

export default {
  name: 'AddCommunityWechat',
  data() {
    return {
      visible: false,
      form: {
        name: '',
        appId: '',
        appSecret: '',
        remarks: '',
        weChatType: '1100',
        objType: '1000',
        objId: '',
        communityId: '',
        payPassword: '1',
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
        objId: data.communityId,
        communityId: data.communityId
      }
      this.visible = true
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.form = {
        name: '',
        appId: '',
        appSecret: '',
        remarks: '',
        weChatType: '1100',
        objType: '1000',
        objId: '',
        communityId: '',
        payPassword: '1',
        mchId: '1',
        mchName: '1',
        certPath: ''
      }
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            const res = await saveAdminSmallWeChat(this.form)
            if (res.code === 0) {
              this.$message.success(this.$t('common.operationSuccess'))
              this.visible = false
              this.$emit('success')
            } else {
              this.$message.error(res.msg || this.$t('communityWechat.addFailed'))
            }
          } catch (error) {
            console.error('添加公众号失败:', error)
            this.$message.error(this.$t('communityWechat.addFailed'))
          }
        }
      })
    }
  }
}
</script>