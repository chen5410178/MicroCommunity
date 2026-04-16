<template>
  <el-dialog 
    :title="$t('communitySpaceManage.edit') + $t('communitySpaceManage.venue')" 
    :visible.sync="visible"
    width="50%"
    @close="resetForm"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item :label="$t('communitySpaceManage.venueName')" prop="name">
        <el-input v-model="form.name" />
      </el-form-item>
      
      <el-form-item :label="$t('communitySpaceManage.startTime')" prop="startTime">
        <el-time-picker
          v-model="form.startTime"
          value-format="HH:mm"
          placeholder="选择时间"
          style="width:100%"
        />
      </el-form-item>
      
      <el-form-item :label="$t('communitySpaceManage.endTime')" prop="endTime">
        <el-time-picker
          v-model="form.endTime"
          value-format="HH:mm"
          placeholder="选择时间"
          style="width:100%"
        />
      </el-form-item>
      
      <el-form-item :label="$t('communitySpaceManage.description')" prop="remark">
        <el-input type="textarea" v-model="form.remark" />
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
import { updateCommunityVenue } from '@/api/community/communitySpaceManageApi'

export default {
  name: 'EditCommunityVenue',
  data() {
    return {
      visible: false,
      form: {
        venueId: '',
        name: '',
        startTime: '',
        endTime: '',
        remark: ''
      },
      rules: {
        name: [
          { required: true, message: this.$t('communitySpaceManage.selectVenue'), trigger: 'blur' }
        ],
        startTime: [
          { required: true, message: this.$t('communitySpaceManage.selectVenue'), trigger: 'change' }
        ],
        endTime: [
          { required: true, message: this.$t('communitySpaceManage.selectVenue'), trigger: 'change' }
        ],
        remark: [
          { required: true, message: this.$t('communitySpaceManage.selectVenue'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(venue) {
      this.form = { ...venue }
      this.visible = true
    },
    
    resetForm() {
      this.$refs.form.resetFields()
    },
    
    submitForm() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await updateCommunityVenue(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            console.error('修改场馆失败:', error)
          }
        }
      })
    }
  }
}
</script>