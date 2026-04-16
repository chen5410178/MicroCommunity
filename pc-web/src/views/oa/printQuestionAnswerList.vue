<template>
  <div class="print-question-answer-container">

      <div class="text-center">
        <h1>{{ printQuestionAnswerInfo.qaName }}</h1>
      </div>
      <div v-html="printQuestionAnswerInfo.content"></div>

      <el-table :data="printQuestionAnswerInfo.questionTitles" border style="width: 100%" class="margin-top">
        <el-table-column prop="qaTitle" :label="$t('printQuestionAnswer.table.name')" width="300" align="center">
          <template slot-scope="{row}">
            <div style="max-width: 300px;">
              {{ row.qaTitle }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="titleType" :label="$t('printQuestionAnswer.table.type')" width="300" align="center">
          <template slot-scope="{row}">
            {{ _getTitleTypeName(row.titleType) }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('printQuestionAnswer.table.options')" align="center">
          <template slot-scope="{row}">
            <div v-for="(item, index) in row.titleValues" :key="index">
              {{ item.seq }}、{{ item.qaValue }}
            </div>
          </template>
        </el-table-column>
        <el-table-column :label="$t('printQuestionAnswer.table.selectCount')" align="center">
          <template slot-scope="{row}">
            <div v-for="(item, index) in row.titleValues" :key="index">
              {{ item.personCount }} {{ $t('printQuestionAnswer.unit.person') }}
            </div>
          </template>
        </el-table-column>
      </el-table>

      <div class="margin-top">
        <span>{{ $t('printQuestionAnswer.label.total') }}：{{ printQuestionAnswerInfo.voteCount }}</span>；
        <span>{{ $t('printQuestionAnswer.label.submitted') }}：{{ printQuestionAnswerInfo.votedCount }}</span>；
        <span v-for="(item, tIndex) in printQuestionAnswerInfo.titleValues" :key="tIndex">
          {{ item.qaValue }}: {{ item.personCount }}{{ $t('printQuestionAnswer.unit.person') }}；
        </span>
      </div>

      <div class="text-right margin-top margin-right">
        {{ currentCommunity.name }}
      </div>
      <div class="text-right margin-top-sm margin-right">
        {{ dateFormat(new Date()) }}
      </div>

      <div id="print-btn" class="text-right margin-top">
        <el-button type="primary" @click="_printPurchaseApplyDiv">
          <i class="el-icon-printer"></i>&nbsp;{{ $t('common.print') }}
        </el-button>
        <el-button type="warning" style="margin-left: 20px;" @click="_closePage">
          <i class="el-icon-close"></i>
          {{ $t('common.cancel') }}
        </el-button>
      </div>

  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import {
  listQuestionAnswer,
  listQuestionTitle
} from '@/api/oa/printQuestionAnswerApi'
import { dateFormat } from '@/utils/dateUtil'

export default {
  name: 'PrintQuestionAnswerList',
  data() {
    return {
      printQuestionAnswerInfo: {
        qaName: '',
        startTime: '',
        endTime: '',
        communityId: '',
        content: '',
        titleType: '',
        questionTitles: [],
        updateRoomIds: false,
        voteCount: 0,
        qaId: '',
        votedCount: 0
      },
      currentCommunity: {
        name: '',
        communityId: ''
      }
    }
  },
  created() {
    this.printQuestionAnswerInfo.qaId = this.$route.query.qaId
    this.currentCommunity.communityId = getCommunityId()
    this._listQuestionAnswers()
    this._loadQuestionTitles()
  },
  methods: {
    async _listQuestionAnswers() {
      try {
        const params = {
          page: 1,
          row: 1,
          communityId: this.currentCommunity.communityId,
          qaId: this.printQuestionAnswerInfo.qaId
        }
        const { data } = await listQuestionAnswer(params)
        this.printQuestionAnswerInfo = Object.assign(
          this.printQuestionAnswerInfo,
          data[0]
        )
      } catch (error) {
        console.error('Failed to load question answers:', error)
      }
    },
    async _loadQuestionTitles() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.currentCommunity.communityId,
          qaId: this.printQuestionAnswerInfo.qaId
        }
        const { data } = await listQuestionTitle(params)
        this.printQuestionAnswerInfo.questionTitles = data
      } catch (error) {
        console.error('Failed to load question titles:', error)
      }
    },
    _printPurchaseApplyDiv() {
      this.printFlag = '1'
      document.getElementById("print-btn").style.display = "none"
      window.print()
      window.opener = null
      window.close()
    },
    _closePage() {
      window.opener = null
      window.close()
    },
    _getTitleTypeName(_titleType) {
      if (_titleType == '1001') {
        return this.$t('printQuestionAnswer.type.single')
      } else if (_titleType == '2002') {
        return this.$t('printQuestionAnswer.type.multiple')
      } else {
        return this.$t('printQuestionAnswer.type.shortAnswer')
      }
    },
    dateFormat
  }
}
</script>

<style lang="scss" scoped>
.print-question-answer-container {
  padding: 20px;

  .margin-top {
    margin-top: 20px;
  }

  .margin-top-sm {
    margin-top: 10px;
  }

  .margin-right {
    margin-right: 20px;
  }

  .text-center {
    text-align: center;
  }

  .text-right {
    text-align: right;
  }
}
</style>