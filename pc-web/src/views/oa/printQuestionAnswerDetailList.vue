<template>
  <div class="print-question-answer-detail-container">
    <el-row :gutter="20">
      <el-col :span="4" id="print-room">
        <el-card class="tree-card">
          <ul class="room-list">
            <li v-for="(item, index) in printQuestionAnswerDetailInfo.userQuestions" :key="index"
              @click="swatchRoom(item)" :class="{ 'selected-room': printQuestionAnswerDetailInfo.roomId == item.roomId }">
              {{ item.roomName }}
            </li>
          </ul>
        </el-card>
      </el-col>
      <el-col :span="20">
        <div class="text-center">
          <h1>{{ printQuestionAnswerDetailInfo.qaName }}</h1>
        </div>
        <div v-html="printQuestionAnswerDetailInfo.content"></div>

        <el-table :data="printQuestionAnswerDetailInfo.questionTitles" border style="width: 100%" class="margin-top">
          <el-table-column prop="qaTitle" :label="$t('printQuestionAnswerDetail.table.title')" width="300" align="center">
            <template slot-scope="scope">
              <div style="max-width: 300px;">
                {{ scope.row.qaTitle }}
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="titleType" :label="$t('printQuestionAnswerDetail.table.type')" width="300"
            align="center">
            <template slot-scope="scope">
              {{ _getTitleTypeName(scope.row.titleType) }}
            </template>
          </el-table-column>
          <el-table-column :label="$t('printQuestionAnswerDetail.table.options')" align="center">
            <template slot-scope="scope">
              <div v-for="(item, index) in scope.row.titleValues" :key="index">
                {{ item.seq }}、{{ item.qaValue }}
              </div>
            </template>
          </el-table-column>
          <el-table-column :label="$t('printQuestionAnswerDetail.table.choices')" align="center">
            <template slot-scope="scope">
              <div v-for="(item, index) in scope.row.titleValues" :key="index">
                {{ _getChooseValue(item) }}
              </div>
            </template>
          </el-table-column>
        </el-table>

        <div class="margin-top">
          <span>{{ $t('printQuestionAnswerDetail.total') }}：{{ printQuestionAnswerDetailInfo.voteCount }}</span>；
          <span>{{ $t('printQuestionAnswerDetail.submitted') }}：{{ printQuestionAnswerDetailInfo.votedCount }}</span>；
          <span v-for="(item, tIndex) in printQuestionAnswerDetailInfo.titleValues" :key="tIndex">
            {{ item.qaValue }}: {{ item.personCount }}{{ $t('printQuestionAnswerDetail.people') }}；
          </span>
        </div>

        <div class="text-right margin-top">
          {{ currentCommunity.name }}
        </div>
        <div class="text-right margin-top-sm">
          {{ currentDate }}
        </div>
      </el-col>
    </el-row>

    <div id="print-btn" class="margin-top">
      <el-button type="primary" class="float-right" @click="_printPurchaseApplyDiv">
        <i class="el-icon-printer"></i>&nbsp;{{ $t('printQuestionAnswerDetail.print') }}
      </el-button>
      <el-button type="warning" class="float-right margin-right" @click="_closePage">
        <i class="el-icon-close"></i>
        {{ $t('printQuestionAnswerDetail.cancel') }}
      </el-button>
    </div>
  </div>
</template>

<script>
import { getCommunityId ,getCommunity} from '@/api/community/communityApi'
import {
  listQuestionAnswer,
  listQuestionTitle,
  listUserQuestionAnswer
} from '@/api/oa/printQuestionAnswerDetailApi'

export default {
  name: 'PrintQuestionAnswerDetailList',
  data() {
    return {
      printQuestionAnswerDetailInfo: {
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
        votedCount: 0,
        userQuestions: [],
        roomId: '',
        userQuestion: {}
      },
      currentCommunity: {},
      currentDate: new Date().toLocaleDateString()
    }
  },
  created() {
    this.printQuestionAnswerDetailInfo.communityId = getCommunityId()
    this.currentCommunity = getCommunity()
    this.printQuestionAnswerDetailInfo.qaId = this.$route.query.qaId
    this._listValues()
  },
  methods: {
    async _listQuestionAnswers() {
      try {
        const params = {
          page: 1,
          row: 1,
          communityId: this.printQuestionAnswerDetailInfo.communityId,
          qaId: this.printQuestionAnswerDetailInfo.qaId
        }
        const { data } = await listQuestionAnswer(params)
        Object.assign(this.printQuestionAnswerDetailInfo, data[0])
      } catch (error) {
        console.error('Failed to load question answers:', error)
      }
    },
    async _loadQuestionTitles() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.printQuestionAnswerDetailInfo.communityId,
          qaId: this.printQuestionAnswerDetailInfo.qaId
        }
        const { data } = await listQuestionTitle(params)
        this.printQuestionAnswerDetailInfo.questionTitles = data
      } catch (error) {
        console.error('Failed to load question titles:', error)
      }
    },
    _printPurchaseApplyDiv() {
      this.printFlag = '1'
      document.getElementById("print-btn").style.display = "none"
      document.getElementById("print-room").style.display = "none"
      window.print()
      window.opener = null
      window.close()
    },
    _closePage() {
      window.opener = null
      window.close()
    },
    _getTitleTypeName(_titleType) {
      if (_titleType === '1001') {
        return this.$t('printQuestionAnswerDetail.singleChoice')
      } else if (_titleType === '2002') {
        return this.$t('printQuestionAnswerDetail.multipleChoice')
      } else {
        return this.$t('printQuestionAnswerDetail.shortAnswer')
      }
    },
    swatchRoom(_room) {
      this.printQuestionAnswerDetailInfo.roomId = _room.roomId
      this.printQuestionAnswerDetailInfo.userQuestion = _room
    },
    async _listValues() {
      try {
        const params = {
          page: 1,
          row: 500,
          communityId: this.printQuestionAnswerDetailInfo.communityId,
          qaId: this.printQuestionAnswerDetailInfo.qaId
        }
        const { data } = await listUserQuestionAnswer(params)
        this.printQuestionAnswerDetailInfo.userQuestions = data
        await this._listQuestionAnswers()
        await this._loadQuestionTitles()
        if (data && data.length > 0) {
          this.swatchRoom(data[0])
        }
      } catch (error) {
        console.error('Failed to load user question answers:', error)
      }
    },
    _getChooseValue(_title) {
      const _userQuestion = this.printQuestionAnswerDetailInfo.userQuestion
      let _chooseValue = 'X'
      if (_userQuestion['values']) {
        _userQuestion.values.forEach(tmpValue => {
          if (tmpValue.qaValue === _title.qaValue) {
            _chooseValue = 'V'
          }
        })
      }
      return _chooseValue
    }
  }
}
</script>

<style lang="scss" scoped>
.print-question-answer-detail-container {
  padding: 20px;

  .tree-card {
    height: 100%;

    .room-list {
      list-style: none;
      padding: 0;
      margin: 0;

      li {
        padding: 10px;
        margin-bottom: 5px;
        cursor: pointer;
        border-radius: 4px;
        text-align: center;
        transition: all 0.3s;

        &:hover {
          background-color: #f5f7fa;
        }

        &.selected-room {
          background-color: #409eff;
          color: white;
        }
      }
    }
  }

  .margin-top {
    margin-top: 20px;
  }

  .margin-top-sm {
    margin-top: 10px;
  }

  .margin-right {
    margin-right: 20px;
  }

  .float-right {
    float: right;
  }

  .text-center {
    text-align: center;
  }

  .text-right {
    text-align: right;
  }
}

#print-btn,
#print-room {
  @media print {
    display: none !important;
  }
}
</style>