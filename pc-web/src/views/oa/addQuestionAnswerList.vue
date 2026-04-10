<template>
  <div class="add-question-answer-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('addQuestionAnswer.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form ref="form" :model="addQuestionAnswerInfo" label-width="120px">
            <el-row>
              <el-col :span="12">
                <el-form-item :label="$t('addQuestionAnswer.qaName')" prop="qaName">
                  <el-input v-model="addQuestionAnswerInfo.qaName"
                    :placeholder="$t('addQuestionAnswer.qaNamePlaceholder')" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('addQuestionAnswer.startTime')" prop="startTime">
                  <el-date-picker v-model="addQuestionAnswerInfo.startTime" type="datetime"
                    :placeholder="$t('addQuestionAnswer.startTimePlaceholder')" value-format="yyyy-MM-dd HH:mm:ss"
                    style="width: 100%" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item :label="$t('addQuestionAnswer.endTime')" prop="endTime">
                  <el-date-picker v-model="addQuestionAnswerInfo.endTime" type="datetime"
                    :placeholder="$t('addQuestionAnswer.endTimePlaceholder')" value-format="yyyy-MM-dd HH:mm:ss"
                    style="width: 100%" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item :label="$t('addQuestionAnswer.content')">
                  <el-input v-model="addQuestionAnswerInfo.content" type="textarea" :rows="5"
                    :placeholder="$t('addQuestionAnswer.contentPlaceholder')" />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-col>
      </el-row>
    </el-card>

    <el-card class="box-card margin-top">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('addQuestionAnswer.questionTitle') }}</span>
        <el-button type="primary" class="float-right" @click="chooseTitle">
          <i class="el-icon-search"></i>{{ $t('addQuestionAnswer.chooseTitle') }}
        </el-button>
      </div>
      <el-table :data="addQuestionAnswerInfo.questionTitles" border style="width: 100%">
        <el-table-column prop="qaTitle" :label="$t('questionTitle.table.name')" align="center" />
        <el-table-column prop="titleType" :label="$t('questionTitle.table.type')" align="center">
          <template slot-scope="scope">
            {{ getTitleTypeName(scope.row.titleType) }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('questionTitle.table.options')" align="center">
          <template slot-scope="scope">
            <div v-for="(item, index) in scope.row.titleValues" :key="index">
              {{ item.seq }}、{{ item.qaValue }}
            </div>
          </template>
        </el-table-column>
        <el-table-column :label="$t('common.operation')" align="center" width="150">
          <template slot-scope="scope">
            <el-button type="danger" size="mini" @click="openDeleteQuestionTitle(scope.row)">
              {{ $t('common.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-card class="box-card margin-top">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('addQuestionAnswer.questionRoom') }}</span>
      </div>
      <el-row>
        <el-col :span="24">
          <el-form>
            <el-form-item :label="$t('addQuestionAnswer.questionRoom')">
              <select-rooms ref="selectRooms" @notifySelectRooms="handleSelectRooms" />
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </el-card>

    <div class="action-buttons margin-top">
      <el-button type="primary" @click="saveQuestionAnswer">
        <i class="el-icon-check"></i>{{ $t('common.save') }}
      </el-button>
      <el-button type="warning" @click="goBack">
        {{ $t('common.cancel') }}
      </el-button>
    </div>

    <choose-question-title ref="chooseQuestionTitle" @chooseQuestionTitle="handleChooseQuestionTitle" />
  </div>
</template>

<script>
import SelectRooms from '@/components/room/selectRooms'
import ChooseQuestionTitle from '@/components/oa/chooseQuestionTitle'
import { saveQuestionAnswer } from '@/api/oa/addQuestionAnswerApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddQuestionAnswer',
  components: {
    SelectRooms,
    ChooseQuestionTitle
  },
  data() {
    return {
      addQuestionAnswerInfo: {
        qaName: '',
        startTime: '',
        endTime: '',
        communityId: '',
        content: '',
        questionTitles: [],
        roomIds: []
      }
    }
  },
  created() {
    this.addQuestionAnswerInfo.communityId = getCommunityId()
  },
  methods: {
    getTitleTypeName(titleType) {
      if (titleType === '1001') {
        return this.$t('questionTitle.types.singleChoice')
      } else if (titleType === '2002') {
        return this.$t('questionTitle.types.multipleChoice')
      } else {
        return this.$t('questionTitle.types.shortAnswer')
      }
    },
    chooseTitle() {
      this.$refs.chooseQuestionTitle.open()
    },
    handleChooseQuestionTitle(questionTitle) {
      const hasTitle = this.addQuestionAnswerInfo.questionTitles.some(
        item => item.titleId === questionTitle.titleId
      )
      if (hasTitle) {
        this.$message.warning(this.$t('addQuestionAnswer.titleExist'))
        return
      }
      this.addQuestionAnswerInfo.questionTitles.push(questionTitle)
    },
    openDeleteQuestionTitle(title) {
      this.$confirm(
        this.$t('addQuestionAnswer.confirmDeleteTitle'),
        this.$t('common.tip'),
        {
          confirmButtonText: this.$t('common.confirm'),
          cancelButtonText: this.$t('common.cancel'),
          type: 'warning'
        }
      ).then(() => {
        this.addQuestionAnswerInfo.questionTitles = this.addQuestionAnswerInfo.questionTitles.filter(
          item => item.titleId !== title.titleId
        )
      })
    },
    handleSelectRooms(rooms) {
      this.addQuestionAnswerInfo.roomIds = rooms.map(item => item.roomId)
    },
    validateForm() {
      if (!this.addQuestionAnswerInfo.qaName) {
        this.$message.warning(this.$t('addQuestionAnswer.qaNameRequired'))
        return false
      }
      if (!this.addQuestionAnswerInfo.startTime) {
        this.$message.warning(this.$t('addQuestionAnswer.startTimeRequired'))
        return false
      }
      if (!this.addQuestionAnswerInfo.endTime) {
        this.$message.warning(this.$t('addQuestionAnswer.endTimeRequired'))
        return false
      }
      if (this.addQuestionAnswerInfo.startTime >= this.addQuestionAnswerInfo.endTime) {
        this.$message.warning(this.$t('addQuestionAnswer.timeInvalid'))
        return false
      }
      if (!this.addQuestionAnswerInfo.content) {
        this.$message.warning(this.$t('addQuestionAnswer.contentRequired'))
        return false
      }
      if (this.addQuestionAnswerInfo.questionTitles.length === 0) {
        this.$message.warning(this.$t('addQuestionAnswer.titleRequired'))
        return false
      }
      if (this.addQuestionAnswerInfo.roomIds.length === 0) {
        this.$message.warning(this.$t('addQuestionAnswer.roomRequired'))
        return false
      }
      return true
    },
    async saveQuestionAnswer() {
      if (!this.validateForm()) return

      try {
        const res = await saveQuestionAnswer(this.addQuestionAnswerInfo)
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.goBack()
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        this.$message.error(this.$t('addQuestionAnswer.saveError'))
      }
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.add-question-answer-container {
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
    padding: 20px;
    background: #fff;
    border-radius: 4px;
  }
}
</style>