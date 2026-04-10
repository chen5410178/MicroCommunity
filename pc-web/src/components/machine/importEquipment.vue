<template>
  <el-dialog
    :title="$t('equipmentAccount.importEquipment')"
    :visible.sync="dialogVisible"
    width="50%"
    @close="handleClose">
    <el-form ref="form" :model="formData" label-width="120px">
      <el-form-item :label="$t('equipmentAccount.selectFile')" prop="excelFile" required>
        <el-upload
          ref="upload"
          action=""
          :auto-upload="false"
          :on-change="handleFileChange"
          :file-list="fileList"
          :limit="1"
          accept=".xls,.xlsx">
          <el-button slot="trigger" size="small" type="primary">{{ $t('common.selectFile') }}</el-button>
          <div slot="tip" class="el-upload__tip">
            {{ $t('equipmentAccount.fileTip') }}
          </div>
        </el-upload>
      </el-form-item>
      
      <el-form-item :label="$t('equipmentAccount.downloadTemplate')">
        <div>
          {{ $t('equipmentAccount.downloadTip1') }}
          <el-link type="primary" @click="downloadTemplate">{{ $t('equipmentAccount.downloadTemplate') }}</el-link>
          {{ $t('equipmentAccount.downloadTip2') }}
        </div>
      </el-form-item>
    </el-form>
    
    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleImport" :loading="loading">{{ $t('common.import') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { importEquipmentData, exportImportTemplate } from '@/api/machine/equipmentAccountApi'

export default {
  name: 'ImportEquipment',
  data() {
    return {
      dialogVisible: false,
      loading: false,
      communityId: '',
      formData: {
        excelFile: null,
        typeId: ''
      },
      fileList: []
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(params) {
      this.formData.typeId = params.typeId
      this.dialogVisible = true
    },
    handleClose() {
      this.fileList = []
      this.formData.excelFile = null
      this.$refs.upload.clearFiles()
    },
    handleFileChange(file) {
      this.formData.excelFile = file.raw
    },
    async handleImport() {
      if (!this.formData.excelFile) {
        this.$message.warning(this.$t('equipmentAccount.selectFileFirst'))
        return
      }

      if (!this.checkFileType(this.formData.excelFile.name)) {
        this.$message.warning(this.$t('equipmentAccount.invalidFileType'))
        return
      }

      if (!this.checkFileSize(this.formData.excelFile.size)) {
        this.$message.warning(this.$t('equipmentAccount.fileSizeExceed'))
        return
      }

      try {
        this.loading = true
        const formData = new FormData()
        formData.append('uploadFile', this.formData.excelFile)
        formData.append('communityId', this.communityId)
        formData.append('typeId', this.formData.typeId)

        await importEquipmentData(formData)
        this.$message.success(this.$t('common.operationSuccess'))
        this.dialogVisible = false
        this.$emit('success')
      } catch (error) {
        console.error('导入设备失败:', error)
      } finally {
        this.loading = false
      }
    },
    downloadTemplate() {
      if (!this.formData.typeId) {
        this.$message.warning(this.$t('equipmentAccount.selectTypeFirst'))
        return
      }
      exportImportTemplate({
        typeId: this.formData.typeId,
        communityId: this.communityId
      })
    },
    checkFileType(filename) {
      const ext = filename.split('.').pop().toLowerCase()
      return ['xls', 'xlsx'].includes(ext)
    },
    checkFileSize(size) {
      return size <= 2 * 1024 * 1024 // 2MB
    }
  }
}
</script>

<style scoped>
.el-upload__tip {
  margin-top: 7px;
  color: #606266;
  font-size: 12px;
}
</style>