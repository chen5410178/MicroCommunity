<template>
  <div class="add-item-release-view-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('addItemReleaseView.title') }}</span>
        <el-button type="primary" class="float-right" icon="el-icon-close" @click="goBack">
          {{ $t('common.back') }}
        </el-button>
      </div>

      <el-row :gutter="20">
        <el-col :span="24">

            <el-form label-width="120px">
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item :label="$t('addItemReleaseView.releaseType')">
                    <el-select v-model="form.typeId" style="width:100%" @change="changeItemReleaseType">
                      <el-option disabled :value="''" :label="$t('addItemReleaseView.requiredSelectReleaseType')" />
                      <el-option v-for="item in form.itemReleaseTypes" :key="item.typeId" :label="item.typeName"
                        :value="item.typeId" :disabled="item.state !== 'C'" />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item :label="$t('addItemReleaseView.applyCompany')">
                    <el-input v-model="form.applyCompany"
                      :placeholder="$t('addItemReleaseView.requiredApplyCompany')" />
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item :label="$t('addItemReleaseView.applyPerson')">
                    <el-input v-model="form.applyPerson" :placeholder="$t('addItemReleaseView.requiredApplyPerson')" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item :label="$t('addItemReleaseView.idCard')">
                    <el-input v-model="form.idCard" :placeholder="$t('addItemReleaseView.requiredIdCard')" />
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item :label="$t('addItemReleaseView.phone')">
                    <el-input v-model="form.applyTel" :placeholder="$t('addItemReleaseView.requiredPhone')" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item :label="$t('addItemReleaseView.passTime')">
                    <el-date-picker v-model="form.passTime" type="datetime" style="width:100%" value-format="yyyy-MM-dd HH:mm:ss"
                      :placeholder="$t('addItemReleaseView.requiredPassTime')" />
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item :label="$t('addItemReleaseView.carNum')">
                    <el-input v-model="form.carNum" :placeholder="$t('addItemReleaseView.optionalCarNum')" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item :label="$t('addItemReleaseView.remark')">
                    <el-input v-model="form.remark" type="textarea"
                      :placeholder="$t('addItemReleaseView.optionalRemark')" />
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>

        </el-col>
      </el-row>
    </el-card>

      <el-row :gutter="20" class="mt-20">
        <el-col :span="24">
          <el-card>
            <div slot="header" class="flex justify-between">
              <span>{{ $t('addItemReleaseView.releaseItems') }}</span>
              <el-button type="primary" size="small" class="float-right" icon="el-icon-plus" @click="addResName">
                {{ $t('addItemReleaseView.addItem') }}
              </el-button>
            </div>

            <el-table :data="form.resNames" border>
              <el-table-column :label="$t('addItemReleaseView.itemName')" align="center">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.resName" :placeholder="$t('addItemReleaseView.requiredItemName')" />
                </template>
              </el-table-column>
              <el-table-column :label="$t('addItemReleaseView.itemAmount')" align="center">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.amount" type="number"
                    :placeholder="$t('addItemReleaseView.requiredItemAmount')" />
                </template>
              </el-table-column>
              <el-table-column :label="$t('common.operation')" align="center" width="120">
                <template slot-scope="scope">
                  <el-button type="danger" size="small" icon="el-icon-delete"
                    @click="removeResName(scope.row.resName)" />
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
      </el-row>

      <el-row v-if="form.audit.assignee === '-2'" :gutter="20" class="mt-20">
        <el-col :span="24">
          <el-card>
            <div slot="header" class="flex justify-between">
              <span>{{ $t('addItemReleaseView.approverInfo') }}</span>
            </div>
            <el-form label-width="120px">
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item :label="$t('addItemReleaseView.approver')">
                    <el-input v-model="form.audit.staffName" :placeholder="$t('addItemReleaseView.requiredApprover')"
                      disabled />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-button type="primary" @click="chooseStaff">
                    <i class="el-icon-search"></i> {{ $t('common.select') }}
                  </el-button>
                </el-col>
              </el-row>
            </el-form>
          </el-card>
        </el-col>
      </el-row>

      <SelectStaff ref="selectStaff" @selectStaff="handleStaffSelect" />

      <div class="mt-20 text-right">
        <el-button type="warning" class="mr-20" @click="goBack">
          {{ $t('common.cancel') }}
        </el-button>
        <el-button type="primary" @click="saveItemReleaseInfo">
          {{ $t('common.submit') }}
        </el-button>
      </div>

  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import SelectStaff from '@/components/staff/SelectStaff'
import {
  saveItemRelease,
  listItemReleaseType,
  queryFirstAuditStaff
} from '@/api/work/addItemReleaseViewApi'

export default {
  name: 'AddItemReleaseView',
  components: {
    SelectStaff
  },
  data() {
    return {
      form: {
        irId: '',
        typeId: '',
        applyCompany: '',
        applyPerson: '',
        idCard: '',
        applyTel: '',
        passTime: '',
        resNames: [],
        state: '',
        carNum: '',
        remark: '',
        itemReleaseTypes: [],
        audit: {
          assignee: '',
          staffId: '',
          staffName: '',
          taskId: ''
        },
        communityId: ''
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.listItemReleaseTypes()
  },
  methods: {
    goBack() {
      this.$router.go(-1)
    },
    validateForm() {
      if (!this.form.typeId) {
        this.$message.error(this.$t('addItemReleaseView.validate.releaseType'))
        return false
      }
      if (!this.form.applyCompany) {
        this.$message.error(this.$t('addItemReleaseView.validate.applyCompany'))
        return false
      }
      if (!this.form.applyPerson) {
        this.$message.error(this.$t('addItemReleaseView.validate.applyPerson'))
        return false
      }
      if (!this.form.idCard) {
        this.$message.error(this.$t('addItemReleaseView.validate.idCard'))
        return false
      }
      if (!this.form.applyTel) {
        this.$message.error(this.$t('addItemReleaseView.validate.phone'))
        return false
      }
      if (!this.form.passTime) {
        this.$message.error(this.$t('addItemReleaseView.validate.passTime'))
        return false
      }
      if (this.form.resNames.length === 0) {
        this.$message.error(this.$t('addItemReleaseView.validate.itemsRequired'))
        return false
      }
      for (const item of this.form.resNames) {
        if (!item.resName || !item.amount) {
          this.$message.error(this.$t('addItemReleaseView.validate.itemInfoRequired'))
          return false
        }
      }
      if (this.form.audit.assignee === '-2' && !this.form.audit.staffId) {
        this.$message.error(this.$t('addItemReleaseView.validate.approverRequired'))
        return false
      }
      return true
    },
    async saveItemReleaseInfo() {
      if (!this.validateForm()) return

      try {
        this.form.communityId = this.communityId
        const res = await saveItemRelease(this.form)
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.goBack()
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        this.$message.error(this.$t('common.saveFailed'))
      }
    },
    async listItemReleaseTypes() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.communityId
        }
        const res = await listItemReleaseType(params)
        this.form.itemReleaseTypes = res.data
      } catch (error) {
        this.$message.error(this.$t('common.fetchFailed'))
      }
    },
    addResName() {
      this.form.resNames.push({
        resName: '',
        amount: ''
      })
    },
    removeResName(resName) {
      this.form.resNames = this.form.resNames.filter(item => item.resName !== resName)
    },
    async changeItemReleaseType() {
      const selectedType = this.form.itemReleaseTypes.find(
        item => item.typeId === this.form.typeId
      )
      if (selectedType && selectedType.flowId) {
        await this.loadStaffOrg(selectedType.flowId)
      }
    },
    async loadStaffOrg(flowId) {
      try {
        const params = {
          communityId: this.communityId,
          flowId: flowId
        }
        const res = await queryFirstAuditStaff(params)
        if (res.code === 0 && res.data.length > 0) {
          Object.assign(this.form.audit, res.data[0])
          if (!res.data[0].assignee.startsWith('-')) {
            this.form.audit.staffId = res.data[0].assignee
          }
        }
      } catch (error) {
        console.error('Failed to load staff org:', error)
      }
    },
    chooseStaff() {
      this.$refs.selectStaff.open(this.form.audit)
    },
    handleStaffSelect(staff) {
      this.form.audit.staffId = staff.staffId
      this.form.audit.staffName = staff.staffName
    }
  }
}
</script>

<style scoped>
.add-item-release-view-container {
  padding: 20px;
}

.mt-20 {
  margin-top: 20px;
}

.mr-20 {
  margin-right: 20px;
}

.float-right {
  float: right;
}

.text-right {
  text-align: right;
}
</style>