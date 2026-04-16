<template>
  <el-dialog :title="$t('resourceStoreManage.importResource')" :visible.sync="dialogVisible" width="40%"
    :close-on-click-modal="false" @closed="handleClosed">
    <el-form ref="importForm" :model="formData" :rules="rules" label-width="120px" label-position="right" class="text-left">
      <el-form-item :label="$t('resourceStoreManage.storehouse')" prop="shId">
        <el-select v-model="formData.shId" style="width:100%">
          <el-option :label="$t('resourceStoreManage.selectStorehouse')" value="" disabled></el-option>
          <el-option v-for="(item, index) in storehouses" :key="index" :label="item.shName"
            :value="item.shId"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('common.select')" prop="file">
        <el-upload class="upload-demo" action="" :auto-upload="false" :on-change="handleFileChange"
          :show-file-list="false">
          <el-button size="small" type="primary">
            {{ $t('resourceStoreManage.browse') }}
          </el-button>
          <div slot="tip" class="el-upload__tip">
            {{ fileName || $t('resourceStoreManage.selectDataFile') }}
          </div>
        </el-upload>
      </el-form-item>

      <el-form-item :label="$t('resourceStoreManage.downloadTemplate')">
        <div>
          {{ $t('resourceStoreManage.downloadTemplateTip1') }}
          <a href="/import/importResourceStore.xlsx" target="_blank">
            {{ $t('resourceStoreManage.resourceTemplate') }}
          </a>
          {{ $t('resourceStoreManage.downloadTemplateTip2') }}
        </div>
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="importData" :loading="importing">
        {{ $t('resourceStoreManage.import') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { importData, listStorehouses } from '@/api/resource/resourceStoreManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ImportResourceStore',
  data() {
    return {
      dialogVisible: false,
      formData: {
        shId: '',
        file: null,
        communityId: getCommunityId()
      },
      storehouses: [],
      fileName: '',
      importing: false,
      rules: {
        shId: [
          { required: true, message: this.$t('resourceStoreManage.storehouseRequired'), trigger: 'change' }
        ],
        file: [
          { required: true, message: this.$t('resourceStoreManage.fileRequired'), trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    open() {
      this.dialogVisible = true
      this.listStorehouses()
    },

    async listStorehouses() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: getCommunityId()
        }
        const response = await listStorehouses(params)
        this.storehouses = response.data || []
      } catch (error) {
        console.error('加载仓库列表失败:', error)
      }
    },

    handleFileChange(file) {
      this.formData.file = file.raw
      this.fileName = file.name
      this.$refs.importForm.validateField('file')
    },

    async importData() {
      this.$refs.importForm.validate(async valid => {
        if (!valid) return

        // 检查文件类型
        const ext = this.fileName.split('.').pop().toLowerCase()
        if (!['xls', 'xlsx'].includes(ext)) {
          this.$message.error(this.$t('resourceStoreManage.invalidExcelFormat'))
          return
        }

        // 检查文件大小 (2MB)
        if (this.formData.file.size > 2 * 1024 * 1024) {
          this.$message.error(this.$t('resourceStoreManage.fileSizeLimit'))
          return
        }

        try {
          this.importing = true

          const formData = new FormData()
          formData.append('uploadFile', this.formData.file)
          formData.append('shId', this.formData.shId)
          formData.append('communityId', this.formData.communityId)

         const res = await importData(formData).catch(error => {
            this.$message.error(error)
          })
          if (res && res.code === 0) {
            this.$message.success(this.$t('common.operationSuccess'))
            this.dialogVisible = false
            this.$emit('success')
          }
        } catch (error) {
          console.error('导入物品失败:', error)
          this.$message.error(error)
        } finally {
          this.importing = false
        }
      })
    },

    handleClosed() {
      this.$refs.importForm.resetFields()
      this.formData = {
        shId: '',
        file: null,
        communityId: getCommunityId()
      }
      this.fileName = ''
    }
  }
}
</script>

<style scoped>
.upload-demo {
  display: flex;
  align-items: center;
}

.el-upload__tip {
  margin-left: 10px;
  color: #606266;
}
</style>