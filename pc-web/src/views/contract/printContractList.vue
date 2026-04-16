<template>
  <div class="print-contract-container">
    <div class="box-card">
      <div slot="header" class="clearfix">
        <h2>{{ $t('printContract.title') }}</h2>
      </div>

      <el-row v-if="printContractInfo.context" class="margin-top">
        <el-col :span="24">
          <div v-html="printContractInfo.context"></div>
        </el-col>
      </el-row>

      <el-row v-else class="margin-top">
        <el-col :span="24" class="text-center">
          <div>{{ $t('printContract.noTemplate') }}</div>
        </el-col>
      </el-row>

      <div id="print-btn" class="button-group">
        <el-button type="primary" class="float-right" @click="_printContractDiv">
          <i class="el-icon-printer"></i>&nbsp;{{ $t('printContract.print') }}
        </el-button>
        <el-button type="warning" class="float-right" style="margin-right:20px;" @click="_closePage">
          {{ $t('printContract.close') }}
        </el-button>
      </div>
    </div>
  </div>
</template>

<script>
import { getPrintContractTemplate } from '@/api/contract/printContractApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'PrintContractList',
  data() {
    return {
      templatecontent: '',
      contractdata: '',
      contractTypeSpec: '',
      baseRepalce: [],
      attrs: [],
      printContractInfo: {
        contractId: '',
        contractTypeId: '',
        context: ''
      },
      printFlag: '0',
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.printContractInfo.contractTypeId = this.$route.query.contractTypeId
    this.printContractInfo.contractId = this.$route.query.contractId
    this._loadContract()
  },
  methods: {
    _loadContract() {
      const param = {
        page: 1,
        row: 1,
        contractId: this.printContractInfo.contractId,
        contractTypeId: this.printContractInfo.contractTypeId,
        communityId: this.communityId
      }

      getPrintContractTemplate(param)
        .then(res => {
          const _data = res.data
          this.templatecontent = _data.contractTypeTemplate[0].context
          this.contractdata = _data.contract[0]
          this.attrs = _data.contract[0].attrs
          this.contractTypeSpec = _data.contractTypeSpec

          this.contractTypeSpec.forEach(e => {
            const rname = e.specName
            const rspecCd = e.specCd
            this.attrs.forEach(ea => {
              if (rspecCd === ea.specCd) {
                const reg = new RegExp('#' + rname + '#', 'g')
                this.templatecontent = this.templatecontent.replace(reg, ea.value)
              }
            })
          })

          this.baseRepalce = _data.baseRepalce
          if (this.baseRepalce) {
            this.baseRepalce.forEach(e => {
              const rname = e.name
              const rkey = e.key
              const contractarr = Object.keys(this.contractdata)
              for (const a in contractarr) {
                if (rkey === contractarr[a]) {
                  const reg = new RegExp('#' + rname + '#', 'g')
                  this.templatecontent = this.templatecontent.replace(reg, this.contractdata[contractarr[a]])
                }
              }
            })
          }

          this.printContractInfo.context = this.templatecontent
        })
        .catch(error => {
          console.error('请求失败:', error)
        })
    },

    _printContractDiv() {
      this.printFlag = '1'
      document.getElementById('print-btn').style.display = 'none'
      window.print()
      window.opener = null
      window.close()
    },

    _closePage() {
      window.opener = null
      window.close()
    }
  }
}
</script>

<style lang="scss" scoped>
.print-contract-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .margin-top {
    margin-top: 20px;
  }

  .text-center {
    text-align: center;
  }

  .float-right {
    float: right;
  }

  .button-group {
    margin-top: 20px;
    overflow: hidden;
  }
}
</style>