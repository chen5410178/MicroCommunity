<template>
  <div class="add-owner-committee-container">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card class="ibox">
          <div slot="header" class="flex justify-between">
            <div>{{ $t('addOwnerCommittee.title') }}</div>
            <div class="ibox-tools">
              <el-button type="primary" size="small" @click="goBack">
                {{ $t('common.back') }}
              </el-button>
            </div>
          </div>
          <el-form ref="form" :model="addOwnerCommitteeInfo" :rules="rules" label-width="120px" label-position="right">
            <el-row>
              <el-col :span="12">
                <el-form-item :label="$t('addOwnerCommittee.name')" prop="name">
                  <el-input v-model="addOwnerCommitteeInfo.name" :placeholder="$t('addOwnerCommittee.requiredName')" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('addOwnerCommittee.sex')" prop="sex">
                  <el-select v-model="addOwnerCommitteeInfo.sex" :placeholder="$t('addOwnerCommittee.requiredSex')"
                    style="width:100%">
                    <el-option disabled value="">{{ $t('addOwnerCommittee.requiredSex') }}</el-option>
                    <el-option :label="$t('addOwnerCommittee.male')" value="B">{{ $t('addOwnerCommittee.male') }}</el-option>
                    <el-option :label="$t('addOwnerCommittee.female')" value="G">{{ $t('addOwnerCommittee.female') }}</el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item :label="$t('addOwnerCommittee.phone')" prop="link">
                  <el-input v-model="addOwnerCommitteeInfo.link" :placeholder="$t('addOwnerCommittee.requiredPhone')" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('addOwnerCommittee.idCard')" prop="idCard">
                  <el-input v-model="addOwnerCommitteeInfo.idCard"
                    :placeholder="$t('addOwnerCommittee.requiredIdCard')" />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item :label="$t('addOwnerCommittee.address')" prop="address">
                  <el-input v-model="addOwnerCommitteeInfo.address"
                    :placeholder="$t('addOwnerCommittee.requiredAddress')" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('addOwnerCommittee.position')" prop="position">
                  <el-input v-model="addOwnerCommitteeInfo.position"
                    :placeholder="$t('addOwnerCommittee.requiredPosition')" />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item :label="$t('addOwnerCommittee.post')" prop="post">
                  <el-input v-model="addOwnerCommitteeInfo.post" :placeholder="$t('addOwnerCommittee.requiredPost')" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('addOwnerCommittee.postDesc')">
                  <el-input v-model="addOwnerCommitteeInfo.postDesc"
                    :placeholder="$t('addOwnerCommittee.optionalPostDesc')" />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item :label="$t('addOwnerCommittee.appointTime')" prop="appointTime">
                  <el-input v-model="addOwnerCommitteeInfo.appointTime"
                    :placeholder="$t('addOwnerCommittee.requiredAppointTime')" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('addOwnerCommittee.curTime')" prop="curTime">
                  <el-input v-model="addOwnerCommitteeInfo.curTime"
                    :placeholder="$t('addOwnerCommittee.requiredCurTime')" />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item :label="$t('addOwnerCommittee.state')" prop="state">
                  <el-select v-model="addOwnerCommitteeInfo.state" :placeholder="$t('addOwnerCommittee.requiredState')"
                    style="width:100%">
                    <el-option disabled value="">{{ $t('addOwnerCommittee.requiredState') }}</el-option>
                    <el-option :label="$t('addOwnerCommittee.onJob')" value="1000">{{ $t('addOwnerCommittee.onJob') }}</el-option>
                    <el-option :label="$t('addOwnerCommittee.leaveJob')" value="2000">{{ $t('addOwnerCommittee.leaveJob') }}</el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('addOwnerCommittee.remark')">
                  <el-input v-model="addOwnerCommitteeInfo.remark" type="textarea"
                    :placeholder="$t('addOwnerCommittee.optionalRemark')" />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="">
      <el-col :span="24">
        <el-card class="ibox">
          <div slot="header" class="flex justify-between">
            <div>{{ $t('addOwnerCommittee.emergencyContact') }}</div>
            <div class="ibox-tools">
              <el-button type="primary" size="small" @click="addContract">
                <i class="el-icon-plus"></i> {{ $t('common.add') }}
              </el-button>
            </div>
          </div>
          <el-table :data="addOwnerCommitteeInfo.contracts" border style="width: 100%">
            <el-table-column :label="$t('addOwnerCommittee.contactRelationship')" align="center">
              <template slot-scope="scope">
                <el-input v-model="scope.row.relName" :placeholder="$t('addOwnerCommittee.requiredRelationship')" />
              </template>
            </el-table-column>
            <el-table-column :label="$t('addOwnerCommittee.contactName')" align="center">
              <template slot-scope="scope">
                <el-input v-model="scope.row.name" :placeholder="$t('addOwnerCommittee.requiredName')" />
              </template>
            </el-table-column>
            <el-table-column :label="$t('addOwnerCommittee.contactPhone')" align="center">
              <template slot-scope="scope">
                <el-input v-model="scope.row.link" :placeholder="$t('addOwnerCommittee.requiredPhone')" />
              </template>
            </el-table-column>
            <el-table-column :label="$t('addOwnerCommittee.contactAddress')" align="center" width="250">
              <template slot-scope="scope">
                <el-input v-model="scope.row.address" :placeholder="$t('addOwnerCommittee.requiredAddress')" />
              </template>
            </el-table-column>
            <el-table-column :label="$t('common.operation')" align="center" width="100">
              <template slot-scope="scope">
                <el-button type="danger" size="mini" @click="deleteContract(scope.row)">
                  {{ $t('common.delete') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <div class="margin-top">
      <el-button type="primary" class="float-right" @click="saveOwnerCommitteeInfo">
        <i class="el-icon-check"></i>&nbsp;
        {{ $t('common.save') }}
      </el-button>
      <el-button type="warning" class="float-right" style="margin-right:20px;" @click="goBack">
        {{ $t('common.back') }}
      </el-button>
    </div>
  </div>
</template>

<script>
import { saveOwnerCommittee } from '@/api/owner/addOwnerCommitteeApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddOwnerCommitteeList',
  data() {
    return {
      addOwnerCommitteeInfo: {
        ocId: '',
        name: '',
        sex: '',
        link: '',
        idCard: '',
        address: '',
        position: '',
        post: '',
        postDesc: '',
        appointTime: '',
        curTime: '',
        state: '',
        remark: '',
        contracts: []
      },
      rules: {
        name: [
          { required: true, message: this.$t('addOwnerCommittee.requiredName'), trigger: 'blur' },
          { max: 30, message: this.$t('addOwnerCommittee.maxName', { max: 30 }), trigger: 'blur' }
        ],
        sex: [
          { required: true, message: this.$t('addOwnerCommittee.requiredSex'), trigger: 'change' },
          { max: 12, message: this.$t('addOwnerCommittee.maxSex', { max: 12 }), trigger: 'blur' }
        ],
        link: [
          { required: true, message: this.$t('addOwnerCommittee.requiredPhone'), trigger: 'blur' },
          { max: 11, message: this.$t('addOwnerCommittee.maxPhone', { max: 11 }), trigger: 'blur' }
        ],
        idCard: [
          { required: true, message: this.$t('addOwnerCommittee.requiredIdCard'), trigger: 'blur' },
          { max: 18, message: this.$t('addOwnerCommittee.maxIdCard', { max: 18 }), trigger: 'blur' }
        ],
        address: [
          { required: true, message: this.$t('addOwnerCommittee.requiredAddress'), trigger: 'blur' },
          { max: 256, message: this.$t('addOwnerCommittee.maxAddress', { max: 256 }), trigger: 'blur' }
        ],
        position: [
          { required: true, message: this.$t('addOwnerCommittee.requiredPosition'), trigger: 'blur' },
          { max: 64, message: this.$t('addOwnerCommittee.maxPosition', { max: 64 }), trigger: 'blur' }
        ],
        post: [
          { required: true, message: this.$t('addOwnerCommittee.requiredPost'), trigger: 'blur' },
          { max: 64, message: this.$t('addOwnerCommittee.maxPost', { max: 64 }), trigger: 'blur' }
        ],
        postDesc: [
          { max: 64, message: this.$t('addOwnerCommittee.maxPostDesc', { max: 64 }), trigger: 'blur' }
        ],
        appointTime: [
          { required: true, message: this.$t('addOwnerCommittee.requiredAppointTime'), trigger: 'blur' },
          { max: 256, message: this.$t('addOwnerCommittee.maxAppointTime', { max: 256 }), trigger: 'blur' }
        ],
        curTime: [
          { required: true, message: this.$t('addOwnerCommittee.requiredCurTime'), trigger: 'blur' },
          { max: 256, message: this.$t('addOwnerCommittee.maxCurTime', { max: 256 }), trigger: 'blur' }
        ],
        state: [
          { required: true, message: this.$t('addOwnerCommittee.requiredState'), trigger: 'change' },
          { max: 12, message: this.$t('addOwnerCommittee.maxState', { max: 12 }), trigger: 'blur' }
        ],
        remark: [
          { max: 200, message: this.$t('addOwnerCommittee.maxRemark', { max: 200 }), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    goBack() {
      this.$router.go(-1)
    },
    addContract() {
      this.addOwnerCommitteeInfo.contracts.push({
        id: this.generateUUID(),
        relName: '',
        name: '',
        link: '',
        address: ''
      })
    },
    deleteContract(contract) {
      const index = this.addOwnerCommitteeInfo.contracts.findIndex(item => item.id === contract.id)
      if (index !== -1) {
        this.addOwnerCommitteeInfo.contracts.splice(index, 1)
      }
    },
    generateUUID() {
      return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        const r = Math.random() * 16 | 0
        const v = c === 'x' ? r : (r & 0x3 | 0x8)
        return v.toString(16)
      })
    },
    async saveOwnerCommitteeInfo() {
      try {
        await this.$refs.form.validate()

        this.addOwnerCommitteeInfo.communityId = getCommunityId()
        const response = await saveOwnerCommittee(this.addOwnerCommitteeInfo)

        if (response.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.goBack()
        } else {
          this.$message.error(response.msg || this.$t('addOwnerCommittee.saveError'))
        }
      } catch (error) {
        console.error('保存失败:', error)
      }
    }
  }
}
</script>

<style scoped>
.add-owner-committee-container {
  padding: 20px;
}

.ibox {
  margin-bottom: 20px;
}

.ibox-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 15px;
  border-bottom: 1px solid #e7eaec;
}

.margin-top {
  margin-top: 20px;
}

.float-right {
  float: right;
}

.clearfix:after {
  content: "";
  display: table;
  clear: both;
}
</style>