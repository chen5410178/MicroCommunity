<template>
  <div class="report-question-answer-detail-container animated fadeInRight">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('reportQuestionAnswerDetail.queryCondition') }}</span>
          </div>
          <el-form :inline="true" :model="queryForm" class="demo-form-inline text-left">
            <el-form-item>
              <el-select v-model="queryForm.qaType" :placeholder="$t('reportQuestionAnswerDetail.selectQaType')"
                style="width:100%">
                <el-option :label="$t('reportQuestionAnswerDetail.pleaseSelect')" value="" />
                <el-option :label="$t('reportQuestionAnswerDetail.ownerQuestionnaire')" value="1001" />
                <el-option :label="$t('reportQuestionAnswerDetail.ownerVote')" value="3003" />
                <el-option :label="$t('reportQuestionAnswerDetail.staffVote')" value="4004" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-date-picker v-model="queryForm.startTime" type="date"
                :placeholder="$t('reportQuestionAnswerDetail.selectStartTime')" style="width:100%" />
            </el-form-item>
            <el-form-item>
              <el-date-picker v-model="queryForm.endTime" type="date"
                :placeholder="$t('reportQuestionAnswerDetail.selectEndTime')" style="width:100%"
                @change="handleEndTimeChange" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleQuery" icon="el-icon-search">
                {{ $t('reportQuestionAnswerDetail.query') }}
              </el-button>
              <el-button type="primary" @click="handleReset" icon="el-icon-refresh">
                {{ $t('reportQuestionAnswerDetail.reset') }}
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" >
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header" class="flex justify-between">
            <div>
              <span>{{ $t('reportQuestionAnswerDetail.questionnaireDetail') }}</span>
              <el-tooltip class="item" effect="dark" :content="$t('reportQuestionAnswerDetail.questionnaireAnswerDetail')"
                placement="top">
                <i class="el-icon-info" style="cursor:pointer;margin-left:10px"></i>
              </el-tooltip>
            </div>
            <el-button type="primary" size="small" class="float-right" @click="handleExport" icon="el-icon-download">
              {{ $t('reportQuestionAnswerDetail.export') }}
            </el-button>
          </div>
          <el-table :data="tableData" border style="width: 100%" v-loading="loading">
            <el-table-column prop="index" :label="$t('reportQuestionAnswerDetail.serialNumber')" width="80"
              align="center" />
            <el-table-column prop="userName" :label="$t('reportQuestionAnswerDetail.questionnairePerson')"
              align="center" />
            <el-table-column prop="qaTypeName" :label="$t('reportQuestionAnswerDetail.questionnaireType')"
              align="center" />
            <el-table-column prop="qaName" :label="$t('reportQuestionAnswerDetail.questionnaireName')" align="center" />
            <el-table-column prop="qaTitle" :label="$t('reportQuestionAnswerDetail.questionnaireQuestion')"
              align="center" />
            <el-table-column prop="qaValue" :label="$t('reportQuestionAnswerDetail.answer')" align="center" />
            <el-table-column prop="createTime" :label="$t('reportQuestionAnswerDetail.time')" align="center" />
          </el-table>
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
            :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
            layout="total, sizes, prev, pager, next, jumper" :total="pagination.total" class="pagination-container" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { queryUserQuestionAnswerValue, exportData } from '@/api/report/reportQuestionAnswerDetailApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ReportQuestionAnswerDetailList',
  data() {
    return {
      loading: false,
      queryForm: {
        qaType: '',
        startTime: '',
        endTime: '',
        communityId: ''
      },
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.queryForm.communityId = getCommunityId()
    this.getList()
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          ...this.queryForm
        }
        const { data, total } = await queryUserQuestionAnswerValue(params)
        this.tableData = data.map((item, index) => {
          return {
            ...item,
            index: index + 1
          }
        })
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('reportQuestionAnswerDetail.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleQuery() {
      this.pagination.current = 1
      this.getList()
    },
    handleReset() {
      this.queryForm = {
        qaType: '',
        startTime: '',
        endTime: '',
        communityId: this.queryForm.communityId
      }
      this.handleQuery()
    },
    handleEndTimeChange(val) {
      if (this.queryForm.startTime && val) {
        const start = new Date(this.queryForm.startTime).getTime()
        const end = new Date(val).getTime()
        if (start >= end) {
          this.$message.error(this.$t('reportQuestionAnswerDetail.endTimeMustGreater'))
          this.queryForm.endTime = ''
        }
      }
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.getList()
    },
    async handleExport() {
      try {
        const params = {
          ...this.queryForm,
          pagePath: 'reportQuestionAnswerDetail'
        }
        await exportData(params)
        this.$message.success(this.$t('common.operationSuccess'))
      } catch (error) {
        this.$message.error(this.$t('reportQuestionAnswerDetail.exportError'))
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.report-question-answer-detail-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }

  .float-right {
    float: right;
  }
}
</style>