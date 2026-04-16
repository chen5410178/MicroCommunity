<template>
  <div class="edit-question-answer-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('editQuestionAnswer.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form ref="form" :model="editQuestionAnswerInfo" label-width="120px">
            <el-row>
              <el-col :span="12">
                <el-form-item :label="$t('editQuestionAnswer.qaName')" prop="qaName">
                  <el-input v-model="editQuestionAnswerInfo.qaName"
                    :placeholder="$t('editQuestionAnswer.qaNamePlaceholder')" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('editQuestionAnswer.startTime')" prop="startTime">
                  <el-date-picker v-model="editQuestionAnswerInfo.startTime" type="datetime"
                    :placeholder="$t('editQuestionAnswer.startTimePlaceholder')" style="width: 100%" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item :label="$t('editQuestionAnswer.endTime')" prop="endTime">
                  <el-date-picker v-model="editQuestionAnswerInfo.endTime" type="datetime"
                    :placeholder="$t('editQuestionAnswer.endTimePlaceholder')" style="width: 100%" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item :label="$t('editQuestionAnswer.content')">
                  <el-input v-model="editQuestionAnswerInfo.content" type="textarea" :rows="5"
                    :placeholder="$t('editQuestionAnswer.contentPlaceholder')" />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-col>
      </el-row>
    </el-card>

    <el-card class="box-card margin-top">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('editQuestionAnswer.questionTitle') }}</span>
        <div class="float-right">
          <el-button type="primary" @click="chooseTitle">
            <i class="el-icon-search"></i>{{ $t('editQuestionAnswer.chooseTitle') }}
          </el-button>
        </div>
      </div>
      <el-table :data="editQuestionAnswerInfo.questionTitles" border style="width: 100%">
        <el-table-column prop="qaTitle" :label="$t('editQuestionAnswer.table.name')" align="center" />
        <el-table-column prop="titleType" :label="$t('editQuestionAnswer.table.type')" align="center">
          <template slot-scope="scope">
            {{ getTitleTypeName(scope.row.titleType) }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('editQuestionAnswer.table.options')" align="center">
          <template slot-scope="scope">
            <div v-for="(item, index) in scope.row.titleValues" :key="index">
              {{ item.seq }}、{{ item.qaValue }}
            </div>
          </template>
        </el-table-column>
        <el-table-column :label="$t('editQuestionAnswer.table.actions')" align="center">
          <template slot-scope="scope">
            <el-button type="danger" size="mini" @click="openDeleteQuestionTitle(scope.row)">
              {{ $t('editQuestionAnswer.table.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-card class="box-card margin-top">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('editQuestionAnswer.questionRooms') }}</span>
      </div>
      <el-row>
        <el-col :span="24">
          <el-form>
          <el-form-item :label="$t('editQuestionAnswer.questionRooms')">
            <div v-if="editQuestionAnswerInfo.updateRoomIds">
              <select-rooms ref="selectRooms" @notifySelectRooms="handleSelectRooms" />
            </div>
            <div v-else>
              <span>{{ editQuestionAnswerInfo.voteCount }}{{ $t('editQuestionAnswer.household') }}</span>
              <el-button type="text" @click="updateRoomIdsMethod">
                {{ $t('editQuestionAnswer.reselect') }}
              </el-button>
            </div>
          </el-form-item>
        </el-form>
        </el-col>
      </el-row>
    </el-card>

    <div class="action-buttons margin-top">
      <el-button type="primary" @click="updateQuestionAnswer">
        <i class="el-icon-check"></i>{{ $t('editQuestionAnswer.save') }}
      </el-button>
      <el-button type="warning" @click="goBack">
        {{ $t('editQuestionAnswer.cancel') }}
      </el-button>
    </div>

    <choose-question-title ref="chooseQuestionTitle" @chooseQuestionTitle="handleChooseQuestionTitle" />
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import SelectRooms from '@/components/room/selectRooms'
import ChooseQuestionTitle from '@/components/oa/chooseQuestionTitle'
import { updateQuestionAnswer, listQuestionAnswer, listQuestionTitle } from '@/api/oa/editQuestionAnswerApi'

export default {
  name: 'EditQuestionAnswerList',
  components: {
    SelectRooms,
    ChooseQuestionTitle
  },
  data() {
    return {
      editQuestionAnswerInfo: {
        qaName: '',
        startTime: '',
        endTime: '',
        communityId: '',
        content: '',
        titleType: '',
        questionTitles: [],
        roomIds: [],
        updateRoomIds: false,
        voteCount: 0,
        qaId: ''
      }
    }
  },
  created() {
    this.editQuestionAnswerInfo.communityId = getCommunityId()
    this.editQuestionAnswerInfo.qaId = this.$route.query.qaId
    this.loadQuestionAnswerInfo()
    this.loadQuestionTitles()
  },
  methods: {
    async loadQuestionAnswerInfo() {
      try {
        const params = {
          page: 1,
          row: 1,
          communityId: this.editQuestionAnswerInfo.communityId,
          qaId: this.editQuestionAnswerInfo.qaId
        }
        const { data } = await listQuestionAnswer(params)
        this.editQuestionAnswerInfo = { ...this.editQuestionAnswerInfo, ...data[0] }
      } catch (error) {
        console.error('Failed to load question answer info:', error)
      }
    },
    async loadQuestionTitles() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.editQuestionAnswerInfo.communityId,
          qaId: this.editQuestionAnswerInfo.qaId
        }
        const { data } = await listQuestionTitle(params)
        this.editQuestionAnswerInfo.questionTitles = data
      } catch (error) {
        console.error('Failed to load question titles:', error)
      }
    },
    getTitleTypeName(titleType) {
      if (titleType === '1001') {
        return this.$t('editQuestionAnswer.singleChoice')
      } else if (titleType === '2002') {
        return this.$t('editQuestionAnswer.multipleChoice')
      } else {
        return this.$t('editQuestionAnswer.shortAnswer')
      }
    },
    chooseTitle() {
      this.$refs.chooseQuestionTitle.open()
    },
    handleChooseQuestionTitle(questionTitle) {
      const exists = this.editQuestionAnswerInfo.questionTitles.some(
        item => item.titleId === questionTitle.titleId
      )
      if (exists) {
        this.$message.warning(this.$t('editQuestionAnswer.duplicateTitle'))
        return
      }
      this.editQuestionAnswerInfo.questionTitles.push(questionTitle)
    },
    openDeleteQuestionTitle(title) {
      this.editQuestionAnswerInfo.questionTitles = this.editQuestionAnswerInfo.questionTitles.filter(
        item => item.titleId !== title.titleId
      )
    },
    updateRoomIdsMethod() {
      this.editQuestionAnswerInfo.updateRoomIds = true
    },
    handleSelectRooms(selectRooms) {
      this.editQuestionAnswerInfo.roomIds = selectRooms.map(item => item.roomId)
    },
    validateForm() {
      if (!this.editQuestionAnswerInfo.qaName) {
        this.$message.error(this.$t('editQuestionAnswer.validate.qaName'))
        return false
      }
      if (!this.editQuestionAnswerInfo.startTime) {
        this.$message.error(this.$t('editQuestionAnswer.validate.startTime'))
        return false
      }
      if (!this.editQuestionAnswerInfo.endTime) {
        this.$message.error(this.$t('editQuestionAnswer.validate.endTime'))
        return false
      }
      if (new Date(this.editQuestionAnswerInfo.startTime) >= new Date(this.editQuestionAnswerInfo.endTime)) {
        this.$message.error(this.$t('editQuestionAnswer.validate.timeError'))
        return false
      }
      return true
    },
    async updateQuestionAnswer() {
      if (!this.validateForm()) return

      try {
        await updateQuestionAnswer(this.editQuestionAnswerInfo)
        this.$message.success(this.$t('common.operationSuccess'))
        this.goBack()
      } catch (error) {
        console.error('Failed to update question answer:', error)
        this.$message.error(error.message || this.$t('editQuestionAnswer.updateFailed'))
      }
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.edit-question-answer-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .margin-top {
    margin-top: 20px;
  }

  .float-right {
    float: right;
  }

  .action-buttons {
    text-align: right;
    margin-top: 20px;
  }
}
</style>