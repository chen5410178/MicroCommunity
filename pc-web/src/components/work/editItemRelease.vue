<template>
  <el-dialog :title="$t('itemReleaseManage.edit.title')" :visible.sync="visible" width="60%" @close="handleClose">
    <el-form ref="form" :model="formData" :rules="rules" label-width="120px" label-position="right">
      <el-form-item :label="$t('itemReleaseManage.form.releaseType')" prop="typeId">
        <el-select v-model="formData.typeId" :placeholder="$t('itemReleaseManage.placeholder.requiredSelect')"
          style="width: 100%">
          <el-option v-for="item in releaseTypes" :key="item.typeId" :label="item.typeName" :value="item.typeId" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('itemReleaseManage.form.applyCompany')" prop="applyCompany">
        <el-input v-model="formData.applyCompany" :placeholder="$t('itemReleaseManage.placeholder.requiredInput')" />
      </el-form-item>

      <el-form-item :label="$t('itemReleaseManage.form.applyPerson')" prop="applyPerson">
        <el-input v-model="formData.applyPerson" :placeholder="$t('itemReleaseManage.placeholder.requiredInput')" />
      </el-form-item>

      <el-form-item :label="$t('itemReleaseManage.form.idCard')" prop="idCard">
        <el-input v-model="formData.idCard" :placeholder="$t('itemReleaseManage.placeholder.requiredInput')"
          maxlength="18" />
      </el-form-item>

      <el-form-item :label="$t('itemReleaseManage.form.phone')" prop="applyTel">
        <el-input v-model="formData.applyTel" :placeholder="$t('itemReleaseManage.placeholder.requiredInput')"
          maxlength="11" />
      </el-form-item>

      <el-form-item :label="$t('itemReleaseManage.form.passTime')" prop="passTime">
        <el-input v-model="formData.passTime" :placeholder="$t('itemReleaseManage.placeholder.requiredInput')" />
      </el-form-item>

      <el-form-item :label="$t('itemReleaseManage.form.releaseItems')" prop="resName">
        <el-input v-model="formData.resName" :placeholder="$t('itemReleaseManage.placeholder.requiredInput')" />
      </el-form-item>

      <el-form-item :label="$t('itemReleaseManage.form.itemAmount')" prop="amount">
        <el-input v-model="formData.amount" :placeholder="$t('itemReleaseManage.placeholder.requiredInput')" />
      </el-form-item>

      <el-form-item :label="$t('itemReleaseManage.form.status')" prop="state">
        <el-select v-model="formData.state" :placeholder="$t('itemReleaseManage.placeholder.requiredSelect')"
          style="width: 100%">
          <el-option :label="$t('itemReleaseManage.status.waiting')" value="W" />
          <el-option :label="$t('itemReleaseManage.status.processing')" value="D" />
          <el-option :label="$t('itemReleaseManage.status.completed')" value="C" />
          <el-option :label="$t('itemReleaseManage.status.failed')" value="F" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('itemReleaseManage.form.carNumber')" prop="carNum">
        <el-input v-model="formData.carNum" :placeholder="$t('itemReleaseManage.placeholder.requiredInput')" />
      </el-form-item>

      <el-form-item :label="$t('itemReleaseManage.form.remark')" prop="remark">
        <el-input v-model="formData.remark" type="textarea"
          :placeholder="$t('itemReleaseManage.placeholder.optionalInput')" />
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleSubmit">
        {{ $t('common.save') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { updateItemRelease, listItemReleaseType } from '@/api/work/itemReleaseManageApi'

export default {
  name: 'EditItemRelease',
  data() {
    return {
      visible: false,
      communityId: '',
      releaseTypes: [],
      formData: {
        irId: '',
        typeId: '',
        applyCompany: '',
        applyPerson: '',
        idCard: '',
        applyTel: '',
        passTime: '',
        resName: '',
        amount: '',
        state: '',
        carNum: '',
        remark: '',
        communityId: ''
      },
      rules: {
        typeId: [
          { required: true, message: this.$t('itemReleaseManage.validate.required'), trigger: 'blur' },
          { max: 30, message: this.$t('itemReleaseManage.validate.maxLength30'), trigger: 'blur' }
        ],
        applyCompany: [
          { required: true, message: this.$t('itemReleaseManage.validate.required'), trigger: 'blur' },
          { max: 64, message: this.$t('itemReleaseManage.validate.maxLength64'), trigger: 'blur' }
        ],
        applyPerson: [
          { required: true, message: this.$t('itemReleaseManage.validate.required'), trigger: 'blur' },
          { max: 64, message: this.$t('itemReleaseManage.validate.maxLength64'), trigger: 'blur' }
        ],
        idCard: [
          { required: true, message: this.$t('itemReleaseManage.validate.required'), trigger: 'blur' },
          { max: 18, message: this.$t('itemReleaseManage.validate.maxLength18'), trigger: 'blur' }
        ],
        applyTel: [
          { required: true, message: this.$t('itemReleaseManage.validate.required'), trigger: 'blur' },
          { max: 11, message: this.$t('itemReleaseManage.validate.maxLength11'), trigger: 'blur' }
        ],
        passTime: [
          { required: true, message: this.$t('itemReleaseManage.validate.required'), trigger: 'blur' },
        ],
        resName: [
          { required: true, message: this.$t('itemReleaseManage.validate.required'), trigger: 'blur' },
          { max: 512, message: this.$t('itemReleaseManage.validate.maxLength512'), trigger: 'blur' }
        ],
        amount: [
          { required: true, message: this.$t('itemReleaseManage.validate.required'), trigger: 'blur' },
          { max: 12, message: this.$t('itemReleaseManage.validate.maxLength12'), trigger: 'blur' }
        ],
        state: [
          { required: true, message: this.$t('itemReleaseManage.validate.required'), trigger: 'blur' },
          { max: 12, message: this.$t('itemReleaseManage.validate.maxLength12'), trigger: 'blur' }
        ],
        carNum: [
          { required: true, message: this.$t('itemReleaseManage.validate.required'), trigger: 'blur' },
          { max: 12, message: this.$t('itemReleaseManage.validate.maxLength12'), trigger: 'blur' }
        ],
        remark: [
          { max: 512, message: this.$t('itemReleaseManage.validate.maxLength512'), trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.loadReleaseTypes()
  },
  methods: {
    async loadReleaseTypes() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.communityId
        }
        const { data } = await listItemReleaseType(params)
        this.releaseTypes = data
      } catch (error) {
        console.error('获取放行类型失败:', error)
      }
    },
    open(data) {
      this.formData = {
        ...this.formData,
        ...data,
        communityId: this.communityId
      }
      this.visible = true
    },
    handleClose() {
      this.$refs.form.resetFields()
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await updateItemRelease(this.formData)
            this.$message.success(this.$t('common.operationSuccess'))
            this.$emit('success')
            this.visible = false
          } catch (error) {
            console.error('更新物品放行失败:', error)
          }
        }
      })
    }
  }
}
</script>