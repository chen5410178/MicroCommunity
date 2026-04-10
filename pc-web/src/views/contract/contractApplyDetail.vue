<template>
  <div class="contract-apply-detail-container">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('contractApplyDetail.title') }}</span>
            <div>
              <el-button type="primary" size="small" @click="_printContract">
                <i class="el-icon-printer"></i>
                {{ $t('contractApplyDetail.print') }}
              </el-button>
              <el-button type="primary" size="small" style="margin-left: 10px;" @click="_goBack">
                <i class="el-icon-close"></i>
                {{ $t('contractApplyDetail.back') }}
              </el-button>
            </div>
          </div>

          <el-form label-width="120px" class="text-left">
            <el-row :gutter="20">
              <el-col :span="8">
                <el-form-item :label="$t('contractApplyDetail.contractName') + '：'">
                  <span>{{ contractDetailInfo.contractName }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item :label="$t('contractApplyDetail.contractCode') + '：'">
                  <span>{{ contractDetailInfo.contractCode }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item :label="$t('contractApplyDetail.contractType') + '：'">
                  <span>{{ contractDetailInfo.contractTypeName }}</span>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="8">
                <el-form-item :label="$t('contractApplyDetail.partyA') + '：'">
                  <span>{{ contractDetailInfo.partyA }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item :label="$t('contractApplyDetail.partyAContact') + '：'">
                  <span>{{ contractDetailInfo.aContacts }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item :label="$t('contractApplyDetail.partyAPhone') + '：'">
                  <span>{{ contractDetailInfo.aLink }}</span>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="8">
                <el-form-item :label="$t('contractApplyDetail.partyB') + '：'">
                  <span>{{ contractDetailInfo.partyB }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item :label="$t('contractApplyDetail.partyBContact') + '：'">
                  <span>{{ contractDetailInfo.bContacts }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item :label="$t('contractApplyDetail.partyBPhone') + '：'">
                  <span>{{ contractDetailInfo.bLink }}</span>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="8">
                <el-form-item :label="$t('contractApplyDetail.operator') + '：'">
                  <span>{{ contractDetailInfo.operator }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item :label="$t('contractApplyDetail.phone') + '：'">
                  <span>{{ contractDetailInfo.operatorLink }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item :label="$t('contractApplyDetail.contractAmount') + '：'">
                  <span>{{ contractDetailInfo.amount }}</span>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="8">
                <el-form-item :label="$t('contractApplyDetail.startTime') + '：'">
                  <span>{{ contractDetailInfo.startTime }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item :label="$t('contractApplyDetail.endTime') + '：'">
                  <span>{{ contractDetailInfo.endTime }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item :label="$t('contractApplyDetail.signingTime') + '：'">
                  <span>{{ contractDetailInfo.signingTime }}</span>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="24">
                <el-form-item :label="$t('contractApplyDetail.relatedFiles') + '：'">
                  <span v-for="(file, index) in contractDetailInfo.files" :key="index" style="margin-left: 20px;">
                    <a href="javascript:void(0)" @click="_viewFile(file)">{{ file.fileRealName }}</a>
                  </span>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="margin-top">
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('contractApplyDetail.relatedRooms') }}</span>
          </div>
          <table class="table table-stripped" style="width: 100%;">
            <thead>
              <tr>
                <th class="text-center">{{ $t('contractApplyDetail.room') }}</th>
                <th class="text-center">{{ $t('contractApplyDetail.roomCode') }}</th>
                <th class="text-center">{{ $t('contractApplyDetail.builtUpArea') }}</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(room, index) in contractDetailInfo.rooms" :key="index">
                <td class="text-center">{{ room.roomName }}</td>
                <td class="text-center">{{ room.roomId }}</td>
                <td class="text-center">{{ room.builtUpArea }}{{ $t('contractApplyDetail.squareMeter') }}</td>
              </tr>
            </tbody>
          </table>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="margin-top">
      <el-col :span="24">
        <el-card>
          <div slot="header">
            <span>{{ $t('contractApplyDetail.workflowFlow') }}</span>
          </div>
          <table class="table table-stripped" style="width: 100%;">
            <thead>
              <tr>
                <th scope="col" class="text-center">{{ $t('contractApplyDetail.serialNumber') }}</th>
                <th scope="col" class="text-center">{{ $t('contractApplyDetail.processor') }}</th>
                <th scope="col" class="text-center">{{ $t('contractApplyDetail.status') }}</th>
                <th scope="col" class="text-center">{{ $t('contractApplyDetail.processTime') }}</th>
                <th scope="col" class="text-center">{{ $t('contractApplyDetail.timeConsuming') }}</th>
                <th scope="col" class="text-center">{{ $t('contractApplyDetail.opinion') }}</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(item, index) in auditUsers" :key="index">
                <td class="text-center">{{ index + 1 }}</td>
                <td class="text-center">{{ item.userName || item.auditName }}</td>
                <td class="text-center">{{ item.stateName }}</td>
                <td class="text-center">{{ item.auditTime }}</td>
                <td class="text-center">{{ item.duration }}</td>
                <td class="text-center">{{ item.message }}</td>
              </tr>
            </tbody>
          </table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { queryContract, getContractFiles, getContractRooms } from '@/api/contract/contractDetailApi'
import { listWorkflowAuditInfo } from '@/api/contract/contractDetailFlowApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ContractApplyDetail',
  data() {
    return {
      contractDetailInfo: {
        contractId: '',
        contractName: '',
        contractCode: '',
        contractType: '',
        contractTypeName: '',
        partyA: '',
        partyB: '',
        aContacts: '',
        bContacts: '',
        aLink: '',
        bLink: '',
        operator: '',
        operatorLink: '',
        amount: '',
        startTime: '',
        endTime: '',
        signingTime: '',
        param: '',
        planType: '',
        files: [],
        rooms: []
      },
      auditUsers: [],
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.contractDetailInfo.contractId = this.$route.query.contractId
    if (!this.contractDetailInfo.contractId) {
      this.$message.error('缺少合同ID参数')
      this.$router.go(-1)
      return
    }
    this._initMethod()
  },
  methods: {
    async _initMethod() {
      await Promise.all([
        this._listContractApply(),
        this._loadAuditUser(),
        this._loadContractFiles(),
        this._loadContractRooms()
      ])
    },
    async _listContractApply() {
      try {
        const params = {
          page: 1,
          row: 1,
          contractId: this.contractDetailInfo.contractId
        }
        const res = await queryContract(params)
        if (res.data && res.data.length > 0) {
          Object.assign(this.contractDetailInfo, res.data[0])
        }
      } catch (error) {
        console.error('请求失败:', error)
        this.$message.error('获取合同信息失败')
      }
    },
    async _loadAuditUser() {
      try {
        const params = {
          businessKey: this.contractDetailInfo.contractId,
          communityId: this.communityId
        }
        const res = await listWorkflowAuditInfo(params)
        if (res.data) {
          this.auditUsers = res.data
        }
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    async _loadContractFiles() {
      try {
        const params = {
          contractId: this.contractDetailInfo.contractId,
          page: 1,
          row: 100
        }
        const res = await getContractFiles(params)
        if (res.data) {
          this.contractDetailInfo.files = res.data
        }
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    async _loadContractRooms() {
      try {
        const params = {
          contractId: this.contractDetailInfo.contractId,
          page: 1,
          row: 100
        }
        const res = await getContractRooms(params)
        if (res.data) {
          this.contractDetailInfo.rooms = res.data
        }
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    _viewFile(file) {
      if (file.fileSaveName) {
        window.open(file.fileSaveName)
      }
    },
    _goBack() {
      this.$router.go(-1)
    },
    _printContract() {
      const contract = this.contractDetailInfo
      window.open(`/#/views/contract/printContract?contractTypeId=${contract.contractType}&contractId=${contract.contractId}`)
    }
  }
}
</script>

<style lang="scss" scoped>
.contract-apply-detail-container {
  padding: 20px;

  .margin-top {
    margin-top: 20px;
  }

  .text-left {
    text-align: left;
  }

  .text-center {
    text-align: center;
  }

  .table {
    width: 100%;
    border-collapse: collapse;

    th,
    td {
      border: 1px solid #ebeef5;
      padding: 12px;
    }

    thead th {
      background-color: #f5f7fa;
      font-weight: bold;
    }

    tbody tr:hover {
      background-color: #f5f7fa;
    }
  }

  .table-stripped tbody tr:nth-child(even) {
    background-color: #fafafa;
  }
}
</style>

