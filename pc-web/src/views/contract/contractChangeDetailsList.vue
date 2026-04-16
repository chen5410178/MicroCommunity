<template>
  <div class="contract-change-details-container">
    <el-row v-for="(contractChangeDetailInfo,index) in contractChangeDetailsInfo.contractDetails" :key="index" class="mb-20">
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header" class="flex justify-between">
            <span>
              <span>{{ $t('contractChangeDetails.contract') }}</span>
              {{contractChangeDetailInfo.operate == 'ADD'?$t('contractChangeDetails.afterChange'):$t('contractChangeDetails.beforeChange')}}
              {{contractChangeDetailInfo.operate == 'ADD'?'('+contractChangeDetailInfo.planTypeName+')':''}}
            </span>
            <div class="card-header-right" v-if="index == 0">
              <el-button type="primary" size="small" style="margin-right:10px;" @click="_goBack()">
                <i class="el-icon-close"></i>{{ $t('common.back') }}
              </el-button>
            </div>
          </div>
          <div class="text item">
            <el-row :gutter="20">
              <el-col :span="8">
                <div class="form-group">
                  <label class="el-form-item__label">
                    <span>{{ $t('contractChangeDetails.contractName') }}</span>：
                  </label>
                  <label class="el-form-item__content">{{contractChangeDetailInfo.contractName}}</label>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="form-group">
                  <label class="el-form-item__label">
                    <span>{{ $t('contractChangeDetails.contractCode') }}</span>：
                  </label>
                  <label class="el-form-item__content">{{contractChangeDetailInfo.contractCode}}</label>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="form-group">
                  <label class="el-form-item__label">
                    <span>{{ $t('contractChangeDetails.contractType') }}</span>：
                  </label>
                  <label class="el-form-item__content">{{contractChangeDetailInfo.contractType}}</label>
                </div>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="8">
                <div class="form-group">
                  <label class="el-form-item__label">
                    <span>{{ $t('contractChangeDetails.partyA') }}</span>：
                  </label>
                  <label class="el-form-item__content">{{contractChangeDetailInfo.partyA}}</label>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="form-group">
                  <label class="el-form-item__label">
                    <span>{{ $t('contractChangeDetails.partyAContacts') }}</span>：
                  </label>
                  <label class="el-form-item__content">{{contractChangeDetailInfo.aContacts}}</label>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="form-group">
                  <label class="el-form-item__label">
                    <span>{{ $t('contractChangeDetails.partyALink') }}</span>：
                  </label>
                  <label class="el-form-item__content">{{contractChangeDetailInfo.aLink}}</label>
                </div>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="8">
                <div class="form-group">
                  <label class="el-form-item__label">
                    <span>{{ $t('contractChangeDetails.partyB') }}</span>：
                  </label>
                  <label class="el-form-item__content">{{contractChangeDetailInfo.partyB}}</label>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="form-group">
                  <label class="el-form-item__label">
                    <span>{{ $t('contractChangeDetails.partyBContacts') }}</span>：
                  </label>
                  <label class="el-form-item__content">{{contractChangeDetailInfo.bContacts}}</label>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="form-group">
                  <label class="el-form-item__label">
                    <span>{{ $t('contractChangeDetails.partyBLink') }}</span>：
                  </label>
                  <label class="el-form-item__content">{{contractChangeDetailInfo.bLink}}</label>
                </div>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="8">
                <div class="form-group">
                  <label class="el-form-item__label">
                    <span>{{ $t('contractChangeDetails.operator') }}</span>：
                  </label>
                  <label class="el-form-item__content">{{contractChangeDetailInfo.operator}}</label>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="form-group">
                  <label class="el-form-item__label">
                    <span>{{ $t('contractChangeDetails.operatorLink') }}</span>：
                  </label>
                  <label class="el-form-item__content">{{contractChangeDetailInfo.operatorLink}}</label>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="form-group">
                  <label class="el-form-item__label">
                    <span>{{ $t('contractChangeDetails.amount') }}</span>：
                  </label>
                  <label class="el-form-item__content">{{contractChangeDetailInfo.amount}}</label>
                </div>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="8">
                <div class="form-group">
                  <label class="el-form-item__label">
                    <span>{{ $t('contractChangeDetails.startTime') }}</span>：
                  </label>
                  <label class="el-form-item__content">{{contractChangeDetailInfo.startTime}}</label>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="form-group">
                  <label class="el-form-item__label">
                    <span>{{ $t('contractChangeDetails.endTime') }}</span>：
                  </label>
                  <label class="el-form-item__content">{{contractChangeDetailInfo.endTime}}</label>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="form-group">
                  <label class="el-form-item__label">
                    <span>{{ $t('contractChangeDetails.signingTime') }}</span>：
                  </label>
                  <label class="el-form-item__content">{{contractChangeDetailInfo.signingTime}}</label>
                </div>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row class="mb-20">
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <h5>
              <span>{{ $t('contractChangeDetails.workflow') }}</span>
            </h5>
          </div>
          <div class="text item">
            <el-table :data="contractChangeDetailsInfo.auditUsers" border style="width: 100%">
              <el-table-column prop="index" :label="$t('contractChangeDetails.serialNumber')" width="80" align="center">
                <template slot-scope="scope">
                  {{scope.$index + 1}}
                </template>
              </el-table-column>
              <el-table-column prop="userName" :label="$t('contractChangeDetails.handler')" align="center">
                <template slot-scope="scope">
                  {{scope.row.userName || scope.row.auditName}}
                </template>
              </el-table-column>
              <el-table-column prop="stateName" :label="$t('contractChangeDetails.status')" align="center"></el-table-column>
              <el-table-column prop="auditTime" :label="$t('contractChangeDetails.processTime')" align="center"></el-table-column>
              <el-table-column prop="duration" :label="$t('contractChangeDetails.timeConsuming')" align="center"></el-table-column>
              <el-table-column prop="message" :label="$t('contractChangeDetails.opinion')" align="center"></el-table-column>
            </el-table>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getContractChangeDetails, getAuditUsers } from '@/api/resource/contractChangeDetailsApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ContractChangeDetailsList',
  data() {
    return {
      contractChangeDetailsInfo: {
        contractDetails: [],
        auditUsers: [],
        planId: ''
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.contractChangeDetailsInfo.planId = this.$route.query.planId
    this._listContractDetails()
    this._loadAuditUser()
  },
  methods: {
    async _listContractDetails() {
      try {
        const param = {
          page: 1,
          row: 10,
          planId: this.contractChangeDetailsInfo.planId
        }
        const { data } = await getContractChangeDetails(param)
        data.sort((_child, _newChild) => {
          return _newChild.operate.charCodeAt(0) - _child.operate.charCodeAt(0)
        })
        this.contractChangeDetailsInfo.contractDetails = data
      } catch (error) {
        console.error('请求合同详情失败:', error)
      }
    },
    async _loadAuditUser() {
      try {
        const param = {
          businessKey: this.contractChangeDetailsInfo.planId,
          communityId: this.communityId
        }
        const { data } = await getAuditUsers(param)
        this.contractChangeDetailsInfo.auditUsers = data
      } catch (error) {
        console.error('请求审核用户失败:', error)
      }
    },
    _goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.contract-change-details-container {
  padding: 20px;
  

  
  .el-card {
    margin-bottom: 20px;
    
    .clearfix {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
    
    .card-header-right {
      display: flex;
      align-items: center;
    }
  }
  
  .form-group {
    margin-bottom: 15px;
    text-align: left;
  }
  
  .el-table {
    margin-top: 20px;
  }
}
</style>