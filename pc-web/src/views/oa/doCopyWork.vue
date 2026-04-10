<template>
  <div class="do-copy-work-container">
    <el-card class="detail-card">
      <div slot="header" class="flex justify-between">
        <span class="">{{ $t('doCopyWork.detail.title') }}</span>
        <el-button size="small" @click="goBack">
          <i class="el-icon-back"></i>
          {{ $t('common.back') }}
        </el-button>
      </div>
      
      <!-- 工单详情信息 -->
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="info-item">
            <label>{{ $t('doCopyWork.detail.workId') }}</label>
            <span>{{ doCopyWorkInfo.workId }}</span>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="info-item">
            <label>{{ $t('doCopyWork.detail.workName') }}</label>
            <span>{{ doCopyWorkInfo.workName }}</span>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="info-item">
            <label>{{ $t('doCopyWork.detail.typeName') }}</label>
            <span>{{ doCopyWorkInfo.typeName }}</span>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="info-item">
            <label>{{ $t('doCopyWork.detail.workCycle') }}</label>
            <span>{{ doCopyWorkInfo.workCycle === '1001' ? $t('doCopyWork.workCycle.once') : $t('doCopyWork.workCycle.periodic') }}</span>
          </div>
        </el-col>
      </el-row>
      
      <el-row :gutter="20" class="margin-top">
        <el-col :span="6">
          <div class="info-item">
            <label>{{ $t('doCopyWork.detail.startTime') }}</label>
            <span>{{ doCopyWorkInfo.startTime }}</span>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="info-item">
            <label>{{ $t('doCopyWork.detail.endTime') }}</label>
            <span>{{ doCopyWorkInfo.endTime }}</span>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="info-item">
            <label>{{ $t('doCopyWork.detail.createUserName') }}</label>
            <span>{{ doCopyWorkInfo.createUserName }}</span>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="info-item">
            <label>{{ $t('doCopyWork.detail.curStaffName') }}</label>
            <span>{{ doCopyWorkInfo.curStaffName || '-' }}</span>
          </div>
        </el-col>
      </el-row>
      
      <el-row :gutter="20" class="margin-top">
        <el-col :span="6">
          <div class="info-item">
            <label>{{ $t('doCopyWork.detail.curCopyName') }}</label>
            <span>{{ doCopyWorkInfo.curCopyName || '-' }}</span>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="info-item">
            <label>{{ $t('doCopyWork.detail.stateName') }}</label>
            <span>{{ doCopyWorkInfo.stateName }}</span>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="info-item">
            <label>{{ $t('doCopyWork.detail.createTime') }}</label>
            <span>{{ doCopyWorkInfo.createTime }}</span>
          </div>
        </el-col>
        <el-col :span="6" v-if="doCopyWorkInfo.pathUrl">
          <div class="info-item">
            <label>{{ $t('doCopyWork.detail.attachment') }}</label>
            <a :href="doCopyWorkInfo.pathUrl" target="_blank">{{ $t('doCopyWork.detail.download') }}</a>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 工单任务明细表格 -->
    <el-card class="table-card margin-top">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('doCopyWork.table.title') }}</span>
      </div>
      <el-table :data="doCopyWorkInfo.items" border style="width: 100%">
        <el-table-column :label="$t('doCopyWork.table.select')" align="center" width="80">
          <template slot-scope="scope">
            <el-radio 
              v-if="scope.row.state === 'C'" 
              v-model="doCopyWorkInfo.audit.itemId" 
              :label="scope.row.itemId"
              @change="selectContent(scope.row)">
              &nbsp;
            </el-radio>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('doCopyWork.table.content')" align="left">
          <template slot-scope="scope">
            <div class="flex justify-start">
              <div>{{ scope.$index + 1 }}、</div>
              <div v-html="scope.row.content"></div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="remark" :label="$t('doCopyWork.table.remark')" align="center" width="300">
        </el-table-column>
        <el-table-column :label="$t('doCopyWork.table.state')" align="center" width="150">
          <template slot-scope="scope">
            {{ scope.row.state === 'W' ? $t('doCopyWork.state.pending') : $t('doCopyWork.state.processed') }}
          </template>
        </el-table-column>
        <el-table-column prop="staffName" :label="$t('doCopyWork.table.staffName')" align="center" width="150">
        </el-table-column>
        <el-table-column prop="finishTime" :label="$t('doCopyWork.table.finishTime')" align="center" width="180">
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 抄送办理 -->
    <el-card class="audit-card margin-top" v-if="doCopyWorkInfo.audit.itemId">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('doCopyWork.audit.title') }}</span>
      </div>
      <el-form :model="doCopyWorkInfo.audit" label-width="120px">
        <el-form-item 
          :label="$t('doCopyWork.audit.deductionMoney')" 
          v-if="doCopyWorkInfo.deduction === 'Y'">
          <el-input-number 
            v-model="doCopyWorkInfo.audit.deductionMoney" 
            :placeholder="$t('doCopyWork.audit.deductionMoneyPlaceholder')"
            :precision="2"
            :min="0"
            style="width: 100%">
          </el-input-number>
        </el-form-item>
        <el-form-item :label="$t('doCopyWork.audit.deductionReason')">
          <el-input 
            type="textarea" 
            v-model.trim="doCopyWorkInfo.audit.deductionReason"
            :placeholder="$t('doCopyWork.audit.deductionReasonPlaceholder')"
            :rows="4">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="auditSubmit">
            <i class="el-icon-check"></i>
            {{ $t('common.submit') }}
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { getWorkInfo, getWorkTaskItems, finishWorkCopy } from '@/api/oa/doWorkApi'

export default {
  name: 'DoCopyWork',
  data() {
    return {
      loading: false,
      doCopyWorkInfo: {
        workId: '',
        wtId: '',
        workName: '',
        typeName: '',
        workCycle: '',
        startTime: '',
        endTime: '',
        createUserName: '',
        curStaffName: '',
        curCopyName: '',
        stateName: '',
        createTime: '',
        items: [],
        pathUrl: '',
        taskId: '',
        deduction: 'N',
        audit: {
          copyId: '',
          deductionMoney: 0.00,
          deductionReason: '',
          itemId: ''
        }
      }
    }
  },
  created() {
    this.initData()
  },
  methods: {
    async initData() {
      this.doCopyWorkInfo.workId = this.$route.query.workId
      this.doCopyWorkInfo.audit.copyId = this.$route.query.copyId
      
      if (!this.doCopyWorkInfo.workId) {
        this.$message.warning(this.$t('doCopyWork.message.noWorkId'))
        return
      }
      
      await this.loadWorkInfo()
      await this.loadWorkTaskItem()
    },
    
    async loadWorkInfo() {
      try {
        this.loading = true
        const params = {
          workId: this.doCopyWorkInfo.workId,
          page: 1,
          row: 1
        }
        const data = await getWorkInfo(params)
        if (data && data.length > 0) {
          Object.assign(this.doCopyWorkInfo, data[0])
        }
      } catch (error) {
        this.$message.error(this.$t('doCopyWork.message.loadWorkInfoError'))
      } finally {
        this.loading = false
      }
    },
    
    async loadWorkTaskItem() {
      try {
        const params = {
          taskId: this.doCopyWorkInfo.taskId,
          workId: this.doCopyWorkInfo.workId,
          page: 1,
          row: 100
        }
        const data = await getWorkTaskItems(params)
        this.doCopyWorkInfo.items = data || []
      } catch (error) {
        this.$message.error(this.$t('doCopyWork.message.loadTaskItemsError'))
      }
    },
    
    selectContent(item) {
      if (item.state !== 'C') {
        return
      }
      this.doCopyWorkInfo.audit.itemId = item.itemId
    },
    
    async auditSubmit() {
      if (!this.doCopyWorkInfo.audit.itemId) {
        this.$message.warning(this.$t('doCopyWork.message.selectItem'))
        return
      }
      
      if (!this.doCopyWorkInfo.audit.deductionReason) {
        this.$message.warning(this.$t('doCopyWork.message.inputReason'))
        return
      }
      
      if (this.doCopyWorkInfo.deduction === 'Y' && !this.doCopyWorkInfo.audit.deductionMoney) {
        this.$message.warning(this.$t('doCopyWork.message.inputMoney'))
        return
      }
      
      try {
        this.loading = true
        await finishWorkCopy(this.doCopyWorkInfo.audit)
        this.$message.success(this.$t('doCopyWork.message.submitSuccess'))
        this.goBack()
      } catch (error) {
        this.$message.error(this.$t('doCopyWork.message.submitError'))
      } finally {
        this.loading = false
      }
    },
    
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.do-copy-work-container {
  padding: 20px;

  .text-title {
    font-size: 16px;
    font-weight: bold;
  }

  .info-item {
    padding: 8px 0;
    
    label {
      color: #606266;
      font-weight: 500;
      margin-right: 10px;
      
      &::after {
        content: ':';
      }
    }
    
    span {
      color: #303133;
    }
    
    a {
      color: #409EFF;
      text-decoration: none;
      
      &:hover {
        text-decoration: underline;
      }
    }
  }

  .margin-top {
    margin-top: 20px;
  }

  .table-card {
    margin-top: 20px;
  }

  .audit-card {
    margin-top: 20px;
  }

  .flex {
    display: flex;
  }

  .justify-between {
    justify-content: space-between;
  }

  .justify-start {
    justify-content: flex-start;
  }
}
</style>

