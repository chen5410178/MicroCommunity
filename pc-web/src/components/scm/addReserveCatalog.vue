<template>
  <el-dialog
    :title="$t('reserveCatalogManage.add.title')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="formData"
      :rules="rules"
      label-width="120px"
    >
      <el-form-item
        :label="$t('reserveCatalogManage.form.name')"
        prop="name"
      >
        <el-input
          v-model="formData.name"
          :placeholder="$t('reserveCatalogManage.form.namePlaceholder')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('reserveCatalogManage.form.sort')"
        prop="sort"
      >
        <el-input
          v-model="formData.sort"
          :placeholder="$t('reserveCatalogManage.form.sortPlaceholder')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('reserveCatalogManage.form.type')"
        prop="type"
      >
        <el-select
          v-model="formData.type"
          :placeholder="$t('reserveCatalogManage.form.typePlaceholder')"
          style="width:100%"
        >
          <el-option
            :label="$t('reserveCatalogManage.form.typeOptions.dining')"
            value="1001"
          />
          <el-option
            :label="$t('reserveCatalogManage.form.typeOptions.service')"
            value="2002"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="$t('reserveCatalogManage.form.state')"
        prop="state"
      >
        <el-select
          v-model="formData.state"
          :placeholder="$t('reserveCatalogManage.form.statePlaceholder')"
          style="width:100%"
        >
          <el-option
            :label="$t('reserveCatalogManage.form.stateOptions.show')"
            value="1001"
          />
          <el-option
            :label="$t('reserveCatalogManage.form.stateOptions.hide')"
            value="2002"
          />
        </el-select>
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
import { saveReserveCatalog } from '@/api/scm/reserveCatalogManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddReserveCatalog',
  data() {
    return {
      visible: false,
      formData: {
        name: '',
        sort: '',
        type: '',
        state: '',
        communityId: ''
      },
      rules: {
        name: [
          { required: true, message: this.$t('reserveCatalogManage.validate.nameRequired'), trigger: 'blur' },
          { max: 128, message: this.$t('reserveCatalogManage.validate.nameMaxLength'), trigger: 'blur' }
        ],
        sort: [
          { required: true, message: this.$t('reserveCatalogManage.validate.sortRequired'), trigger: 'blur' },
          { max: 12, message: this.$t('reserveCatalogManage.validate.sortMaxLength'), trigger: 'blur' }
        ],
        type: [
          { required: true, message: this.$t('reserveCatalogManage.validate.typeRequired'), trigger: 'change' }
        ],
        state: [
          { required: true, message: this.$t('reserveCatalogManage.validate.stateRequired'), trigger: 'change' }
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
            await saveReserveCatalog(this.formData)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            this.$message.error(error.message || this.$t('reserveCatalogManage.add.error'))
          }
        }
      })
    }
  }
}
</script>