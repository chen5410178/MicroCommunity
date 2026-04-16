<template>
  <el-dialog
    :title="$t('smallWeChatManage.editTitle')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form ref="form" :model="formData" :rules="rules" label-width="120px">
      <el-form-item :label="$t('smallWeChatManage.name')" prop="name">
        <el-input
          v-model="formData.name"
          :placeholder="$t('smallWeChatManage.namePlaceholder')"
        />
      </el-form-item>
      <el-form-item label="APPID" prop="appId">
        <el-input
          v-model="formData.appId"
          :placeholder="$t('smallWeChatManage.appIdPlaceholder')"
        />
      </el-form-item>
      <el-form-item :label="$t('smallWeChatManage.appSecret')" prop="appSecret">
        <el-input
          v-model="formData.appSecret"
          :placeholder="$t('smallWeChatManage.appSecretPlaceholder')"
          show-password
        />
      </el-form-item>
      <el-form-item :label="$t('smallWeChatManage.remarks')">
        <el-input
          v-model="formData.remarks"
          type="textarea"
          :placeholder="$t('smallWeChatManage.remarksPlaceholder')"
          :rows="3"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { updateSmallWeChat } from '@/api/system/smallWeChatManageApi'
import { getCommunityId } from '@/api/community/communityApi'
import { getDict } from '@/api/community/communityApi'

export default {
  name: 'EditSmallWechat',
  data() {
    return {
      visible: false,
      formData: {
        wechatId: '',
        name: '',
        appId: '',
        appSecret: '',
        remarks: '',
        objType: '1000',
        objId: '',
        payPassword: '1',
        mchId: '1',
        mchName: '1',
        certPath: ''
      },
      rules: {
        name: [
          { required: true, message: this.$t('smallWeChatManage.nameRequired'), trigger: 'blur' },
          { max: 100, message: this.$t('smallWeChatManage.nameMaxLength'), trigger: 'blur' }
        ],
        appId: [
          { required: true, message: this.$t('smallWeChatManage.appIdRequired'), trigger: 'blur' },
          { max: 100, message: this.$t('smallWeChatManage.appIdMaxLength'), trigger: 'blur' }
        ],
        appSecret: [
          { required: true, message: this.$t('smallWeChatManage.appSecretRequired'), trigger: 'blur' },
          { max: 200, message: this.$t('smallWeChatManage.appSecretMaxLength'), trigger: 'blur' }
        ],
        wechatId: [
          { required: true, message: this.$t('smallWeChatManage.wechatIdRequired'), trigger: 'blur' }
        ]
      },
      objTypes: []
    }
  },
  methods: {
    open(data) {
      this.formData = { ...data }
      this.formData.objId = getCommunityId()
      this.getDictData()
      this.visible = true
    },
    async getDictData() {
      try {
        const objTypes = await getDict('small_wechat', 'obj_type')
        this.objTypes = objTypes
      } catch (error) {
        console.error('Failed to get dict data:', error)
      }
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.formData = {
        wechatId: '',
        name: '',
        appId: '',
        appSecret: '',
        remarks: '',
        objType: '1000',
        objId: '',
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
            await updateSmallWeChat(this.formData)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            this.$message.error(error.message || this.$t('smallWeChatManage.editFailed'))
          }
        }
      })
    }
  }
}
</script>