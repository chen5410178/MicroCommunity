<template>
  <el-dialog
    :title="$t('complaint.edit.title')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form 
      ref="form" 
      :model="form" 
      label-width="120px"
      :rules="rules"
    >
      <el-form-item 
        :label="$t('complaint.edit.type')" 
        prop="typeCd"
      >
        <el-select 
          v-model="form.typeCd" 
          style="width:100%"
          :placeholder="$t('complaint.edit.typePlaceholder')"
        >
          <el-option 
            v-for="item in complaintTypes" 
            :key="item.typeCd"
            :label="item.typeName"
            :value="item.typeCd"
          />
        </el-select>
      </el-form-item>
      <el-form-item 
        :label="$t('complaint.edit.contact')" 
        prop="complaintName"
      >
        <el-input 
          v-model="form.complaintName" 
          :placeholder="$t('complaint.edit.contactPlaceholder')"
        />
      </el-form-item>
      <el-form-item 
        :label="$t('complaint.edit.phone')" 
        prop="tel"
      >
        <el-input 
          v-model="form.tel" 
          :placeholder="$t('complaint.edit.phonePlaceholder')"
        />
      </el-form-item>
      <el-form-item 
        :label="$t('complaint.edit.content')" 
        prop="context"
      >
        <el-input
          type="textarea"
          :rows="4"
          v-model="form.context"
          :placeholder="$t('complaint.edit.contentPlaceholder')"
        />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateComplaint } from '@/api/oa/complaintApi'
import { listComplaintTypes } from '@/api/oa/complaintApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'EditComplaint',
  data() {
    return {
      visible: false,
      communityId: '',
      complaintTypes: [],
      form: {
        complaintId: '',
        typeCd: '',
        complaintName: '',
        tel: '',
        context: '',
        communityId: ''
      },
      rules: {
        typeCd: [
          { required: true, message: this.$t('complaint.validate.type'), trigger: 'blur' }
        ],
        complaintName: [
          { required: true, message: this.$t('complaint.validate.contact'), trigger: 'blur' },
          { max: 200, message: this.$t('complaint.validate.contactMax'), trigger: 'blur' }
        ],
        tel: [
          { required: true, message: this.$t('complaint.validate.phone'), trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: this.$t('complaint.validate.phoneFormat'), trigger: 'blur' }
        ],
        context: [
          { required: true, message: this.$t('complaint.validate.content'), trigger: 'blur' },
          { max: 4000, message: this.$t('complaint.validate.contentMax'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(row) {
      this.communityId = getCommunityId()
      this.form = {
        ...row,
        communityId: this.communityId
      }
      this.getComplaintTypes()
      this.visible = true
    },
    async getComplaintTypes() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.communityId
        }
        const { data } = await listComplaintTypes(params)
        this.complaintTypes = data
      } catch (error) {
        console.error('获取投诉类型失败:', error)
      }
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await updateComplaint(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.$emit('success')
            this.visible = false
          } catch (error) {
            console.error('更新投诉失败:', error)
          }
        }
      })
    },
    handleClose() {
      this.$refs.form.resetFields()
    }
  }
}
</script>