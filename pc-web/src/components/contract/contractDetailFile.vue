<template>
  <div class="margin-top">
    <el-row class="margin-top-lg"></el-row>
    <div class="margin-top">
      <el-form :model="contractDetailFileInfo" label-width="120px" class="text-left">
        <el-row>
          <el-col :span="10">
            <el-form-item :label="$t('contractDetailFile.relatedFiles')">
              <div v-for="(file, index) in contractDetailFileInfo.files" :key="index" style="margin-left: 50px;">
                <el-link type="primary" @click="_viewFile(file)">
                  {{ file.fileRealName }}
                </el-link>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>
</template>

<script>
import { queryContractFile } from '@/api/contract/contractDetailFileApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ContractDetailFile',
  data() {
    return {
      contractDetailFileInfo: {
        files: [],
        contractId: '',
        roomNum: '',
        allOweFeeAmount: '0'
      },
      pagination: {
        page: 1,
        row: 100
      },
    }
  },
  methods: {
    open(data) {
      this.contractDetailFileInfo.contractId = data.contractId
      this._loadContractDetailFileData(1, this.pagination.row)
    },
    _loadContractDetailFileData(page, row) {
      const params = {
        contractId: this.contractDetailFileInfo.contractId,
        page: page || 1,
        row: row || this.pagination.row
      }

      queryContractFile(params).then(response => {
        this.contractDetailFileInfo.files = response.data
      }).catch(error => {
        console.error('请求失败:', error)
      })
    },
    _viewFile(file) {
      window.open(file.fileSaveName)
    }
  },
  created() {
    this.communityId = getCommunityId()
  }
}
</script>

<style scoped>
.margin-top {
  margin-top: 20px;
}

.margin-top-lg {
  margin-top: 30px;
}
</style>