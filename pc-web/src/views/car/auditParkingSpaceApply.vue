<template>
  <div class="audit-container">
    <el-card class="box-card">
      <div slot="header" class=" flex justify-between">
        <span>{{ $t('auditParkingSpaceApply.applyInfo') }}</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="goBack">
          {{ $t('auditParkingSpaceApply.back') }}
        </el-button>
      </div>
      <el-form ref="viewForm" :model="formData" label-width="120px" class="text-left">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item :label="$t('auditParkingSpaceApply.licensePlate') + '：'">
              <span>{{ formData.carNum }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('auditParkingSpaceApply.carBrand') + '：'">
              <span>{{ formData.carBrand }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('auditParkingSpaceApply.carType') + '：'">
              <span>{{ formData.carType }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('auditParkingSpaceApply.color') + '：'">
              <span>{{ formData.carColor }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('auditParkingSpaceApply.startRentTime') + '：'">
              <span>{{ formData.startTime }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('auditParkingSpaceApply.endRentTime') + '：'">
              <span>{{ formData.endTime }}</span>
            </el-form-item>
          </el-col>

          <el-col :span="6">
            <el-form-item :label="$t('auditParkingSpaceApply.applicant') + '：'">
              <span>{{ formData.applyPersonName }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('auditParkingSpaceApply.applicantPhone') + '：'">
              <span>{{ formData.applyPersonLink }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('auditParkingSpaceApply.applicantId') + '：'">
              <span>{{ formData.applyPersonId }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="16">
            <el-form-item :label="$t('auditParkingSpaceApply.remark') + '：'">
              <span>{{ formData.remark }}</span>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <el-card class="box-card" style="margin-top: 20px;">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('auditParkingSpaceApply.auditInfo') }}</span>
      </div>

      <el-form ref="form" :model="formData" label-width="120px">
        <el-form-item :label="$t('auditParkingSpaceApply.startRentTime')" required>
          <el-date-picker v-model="formData.startTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
            :placeholder="$t('auditParkingSpaceApply.requiredField')" style="width: 100%;" />
        </el-form-item>

        <el-form-item :label="$t('auditParkingSpaceApply.endRentTime')" required>
          <el-date-picker v-model="formData.endTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
            :placeholder="$t('auditParkingSpaceApply.requiredField')" style="width: 100%;" />
        </el-form-item>

        <el-form-item :label="$t('auditParkingSpaceApply.auditResult')" required>
          <el-select v-model="formData.state" :placeholder="$t('auditParkingSpaceApply.selectResult')"
            style="width:100%">
            <el-option :label="$t('auditParkingSpaceApply.pass')" value="3003" />
            <el-option :label="$t('auditParkingSpaceApply.reject')" value="4004" />
          </el-select>
        </el-form-item>

        <el-form-item v-if="formData.state === '3003'" :label="$t('auditParkingSpaceApply.selectParkingSpace')">
          <el-input v-model="formData.psName" :placeholder="$t('auditParkingSpaceApply.selectParkingSpace')" readonly
            style="width: 80%;" />
          <el-button type="primary" @click="openChooseParkingSpace" style="margin-left: 10px;">
            <i class="el-icon-search"></i>
            {{ $t('auditParkingSpaceApply.select') }}
          </el-button>
        </el-form-item>

        <el-form-item v-if="formData.state === '3003' && formData.areaNum"
          :label="$t('auditParkingSpaceApply.belongParkingLot')">
          <span>{{ formData.areaNum }}</span>
        </el-form-item>

        <el-form-item :label="$t('auditParkingSpaceApply.auditOpinion')">
          <el-input v-model="formData.remark2" type="textarea" :placeholder="$t('auditParkingSpaceApply.optional')"
            :rows="3" />
        </el-form-item>

        <el-form-item>
          <el-button @click="goBack">
            {{ $t('auditParkingSpaceApply.back') }}
          </el-button>
          <el-button type="primary" @click="doAudit">
            {{ $t('auditParkingSpaceApply.submit') }}
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>


    <!-- 搜索车位组件 -->
    <search-parking-space ref="searchParkingSpace" :ps-flag="formData.parkingSpaceFlag"
      @choose-parking-space="handleChooseParkingSpace" />
  </div>
</template>

<script>
import { getParkingSpaceApplyDetail, auditParkingSpaceApply } from '@/api/car/auditParkingSpaceApplyApi'
import SearchParkingSpace from '@/components/car/SearchParkingSpace'

import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AuditParkingSpaceApply',
  components: {
    SearchParkingSpace
  },
  data() {
    return {
      formData: {
        applyId: '',
        communityId: getCommunityId(),
        psId: '',
        psName: '',
        paId: '',
        areaNum: '',
        carNum: '',
        carBrand: '',
        carType: '',
        carColor: '',
        startTime: '',
        endTime: '',
        applyPersonName: '',
        applyPersonLink: '',
        applyPersonId: '',
        state: '',
        remark: '',
        remark2: '',
        parkingSpaceFlag: 'F'
      }
    }
  },
  created() {
    this.formData.applyId = this.$route.query.applyId
    this.getApplyDetail()
  },
  methods: {
    async getApplyDetail() {
      try {
        const params = {
          page: 1,
          row: 1,
          applyId: this.formData.applyId
        }

        const res = await getParkingSpaceApplyDetail(params)
        if (res.data && res.data.length > 0) {
          Object.assign(this.formData, res.data[0])
          this.formData.state = ''
        }
      } catch (error) {
        this.$message.error(this.$t('common.queryFailed'))
      }
    },
    openChooseParkingSpace() {
      this.$refs.searchParkingSpace.open()
    },
    handleChooseParkingSpace(parkingSpace) {
      console.log('选择车位', parkingSpace)
      this.formData.psId = parkingSpace.psId
      this.formData.psName = parkingSpace.num
      this.formData.paId = parkingSpace.paId || ''
      this.formData.areaNum = parkingSpace.areaNum || ''
    },
    async doAudit() {
      // 验证逻辑
      if (!this.formData.state) {
        this.$message.warning(this.$t('auditParkingSpaceApply.selectResult'))
        return
      }

      if (this.formData.state === '3003' && !this.formData.psId) {
        this.$message.warning(this.$t('auditParkingSpaceApply.selectParkingSpace'))
        return
      }

      if (this.formData.state === '4004' && !this.formData.remark2) {
        this.$message.warning(this.$t('auditParkingSpaceApply.auditOpinionRequired'))
        return
      }

      try {
        // 合并备注信息
        const remark = `${this.formData.remark}\n审核意见：${this.formData.remark2}`

        const data = {
          ...this.formData,
          remark
        }

        await auditParkingSpaceApply(data)
        this.$message.success(this.$t('common.operationSuccess'))
        this.goBack()
      } catch (error) {
        this.$message.error(this.$t('common.submitFailed'))
      }
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style scoped>
.audit-container {
  padding: 20px;
}

.box-card {
  margin-bottom: 20px;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both;
}
</style>