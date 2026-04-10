<template>
  <el-dialog 
    :title="$t('communitySpaceManage.edit')" 
    :visible.sync="visible"
    width="50%"
    @close="resetForm"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item :label="$t('communitySpaceManage.name')" prop="name">
        <el-input v-model="form.name" />
      </el-form-item>
      
      <el-form-item :label="$t('communitySpaceManage.feeMoney')" prop="feeMoney">
        <el-input v-model="form.feeMoney" />
      </el-form-item>
      
      <el-form-item :label="$t('communitySpaceManage.adminName')" prop="adminName">
        <el-input v-model="form.adminName" />
      </el-form-item>
      
      <el-form-item :label="$t('communitySpaceManage.tel')" prop="tel">
        <el-input v-model="form.tel" />
      </el-form-item>
      
      <el-form-item :label="$t('communitySpaceManage.state')" prop="state">
        <el-select v-model="form.state" style="width:100%">
          <el-option 
            :label="$t('communitySpaceManage.status1001')" 
            value="1001" 
          />
          <el-option 
            :label="$t('communitySpaceManage.status2002')" 
            value="2002" 
          />
        </el-select>
      </el-form-item>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('communitySpaceManage.cancel') }}
      </el-button>
      <el-button type="primary" @click="submitForm">
        {{ $t('communitySpaceManage.save') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { updateCommunitySpace } from '@/api/community/communitySpaceManageApi'

export default {
  name: 'EditCommunitySpace',
  data() {
    return {
      visible: false,
      form: {
        spaceId: '',
        name: '',
        feeMoney: '',
        adminName: '',
        tel: '',
        state: '1001'
      },
      rules: {
        name: [
          { required: true, message: this.$t('communitySpaceManage.selectSpace'), trigger: 'blur' }
        ],
        feeMoney: [
          { required: true, message: this.$t('communitySpaceManage.selectSpace'), trigger: 'blur' }
        ],
        adminName: [
          { required: true, message: this.$t('communitySpaceManage.selectSpace'), trigger: 'blur' }
        ],
        tel: [
          { required: true, message: this.$t('communitySpaceManage.selectSpace'), trigger: 'blur' }
        ],
        state: [
          { required: true, message: this.$t('communitySpaceManage.selectStatus'), trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    open(space) {
      this.form = { ...space }
      this.visible = true
    },
    
    resetForm() {
      this.$refs.form.resetFields()
    },
    
    submitForm() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await updateCommunitySpace(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            console.error('修改场地失败:', error)
          }
        }
      })
    }
  }
}
</script>