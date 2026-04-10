<template>
  <div class="history-fee-detail-import-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('historyFeeDetailImport.assetInfo') }}</span>
        <div class="card-header-right">
          <el-button type="primary" size="small" @click="_openDownloadHcExcelTemplate">
            <i class="el-icon-download"></i>{{ $t('historyFeeDetailImport.houseTemplate') }}
          </el-button>
          <el-button type="primary" size="small" @click="_openDownloadHcCarExcelTemplate">
            <i class="el-icon-download"></i>{{ $t('historyFeeDetailImport.carTemplate') }}
          </el-button>
        </div>
      </div>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form label-position="left" label-width="120px" class="text-left">
            <el-form-item :label="$t('historyFeeDetailImport.feeObject')">
              <el-select v-model="historyFeeDetailImportInfo.objType" style="width:100%"
                :placeholder="$t('historyFeeDetailImport.selectFeeObject')">
                <el-option disabled value="" :label="$t('historyFeeDetailImport.requiredSelect')">
                </el-option>
                <el-option value="3333" :label="$t('historyFeeDetailImport.house')">
                </el-option>
                <el-option value="6666" :label="$t('historyFeeDetailImport.car')">
                </el-option>
              </el-select>
            </el-form-item>

            <el-form-item :label="$t('historyFeeDetailImport.selectFile')">
              <el-upload class="upload-demo" action="" :auto-upload="false" :on-change="getExcelTemplate"
                :show-file-list="false">
                <el-button size="small" type="primary">
                  {{ $t('historyFeeDetailImport.clickUpload') }}
                </el-button>
                <div slot="tip" class="el-upload__tip">
                  {{ historyFeeDetailImportInfo.excelTemplate ?
                    historyFeeDetailImportInfo.excelTemplate.name :
                    $t('historyFeeDetailImport.requiredFile') }}
                </div>
              </el-upload>
            </el-form-item>

            <el-form-item :label="$t('historyFeeDetailImport.description')">
              <div class="description-text">
                {{ $t('historyFeeDetailImport.importTip') }}
              </div>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="24" class="text-right">
          <el-button type="primary" @click="_importData"
            :disabled="!historyFeeDetailImportInfo.excelTemplate || !historyFeeDetailImportInfo.objType">
            {{ $t('historyFeeDetailImport.import') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import { importData } from '@/api/system/historyFeeDetailImportApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'HistoryFeeDetailImportList',
  data() {
    return {
      historyFeeDetailImportInfo: {
        communityId: '',
        excelTemplate: null,
        objType: ''
      }
    }
  },
  created() {
    this.historyFeeDetailImportInfo.communityId = getCommunityId()
  },
  methods: {
    _openDownloadHcExcelTemplate() {
      window.open('/import/importFeeDetail.xlsx')
    },
    _openDownloadHcCarExcelTemplate() {
      window.open('/import/importCarFeeDetail.xlsx')
    },
    getExcelTemplate(file) {
      this.historyFeeDetailImportInfo.excelTemplate = file.raw
    },
    async _importData() {
      if (!this.validateForm()) {
        return
      }

      const param = new FormData()
      param.append('uploadFile', this.historyFeeDetailImportInfo.excelTemplate)
      param.append('communityId', this.historyFeeDetailImportInfo.communityId)
      param.append('objType', this.historyFeeDetailImportInfo.objType)

      const _importAdapt = this.historyFeeDetailImportInfo.objType === '6666' ?
        'importCarHistoryFeeDetail' : 'importRoomHistoryFeeDetail'
      param.append('importAdapt', _importAdapt)

      try {
        const res = await importData(param)
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.historyFeeDetailImportInfo.excelTemplate = null
          this.$router.push({
            path: '/views/system/assetImportLogDetail',
            query: {
              logId: res.data.logId,
              logType: _importAdapt
            }
          })
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        this.$message.error(error)
      }
    },
    validateForm() {
      if (!this.historyFeeDetailImportInfo.excelTemplate) {
        this.$message.error(this.$t('historyFeeDetailImport.fileRequired'))
        return false
      }

      if (!this.checkFileType(this.historyFeeDetailImportInfo.excelTemplate.name)) {
        this.$message.error(this.$t('historyFeeDetailImport.invalidFileType'))
        return false
      }

      if (!this.checkFileSize(this.historyFeeDetailImportInfo.excelTemplate.size)) {
        this.$message.error(this.$t('historyFeeDetailImport.fileSizeLimit'))
        return false
      }

      return true
    },
    checkFileType(fileName) {
      const fileExt = fileName.split('.').pop().toLowerCase()
      return ['xls', 'xlsx'].includes(fileExt)
    },
    checkFileSize(fileSize) {
      return fileSize <= 2 * 1024 * 1024 // 2MB
    }
  }
}
</script>

<style lang="scss" scoped>
.history-fee-detail-import-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .card-header-right {
    float: right;
  }

  .description-text {
    color: #606266;
    font-size: 14px;
    line-height: 1.5;
  }

  .text-right {
    text-align: right;
    margin-top: 20px;
  }

  .el-upload__tip {
    margin-left: 10px;
    display: inline-block;
    vertical-align: middle;
  }
}
</style>