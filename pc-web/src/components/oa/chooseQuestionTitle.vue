<template>
  <el-dialog :title="$t('chooseQuestionTitle.title')" :visible.sync="visible" width="70%" @close="handleClose">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-row :gutter="10">
          <el-col :span="18"></el-col>
          <el-col :span="6">
            <el-input v-model="currentQuestionTitleName" :placeholder="$t('chooseQuestionTitle.searchPlaceholder')"
              clearable @keyup.enter.native="queryQuestionTitles">
              <el-button slot="append" icon="el-icon-search" @click="queryQuestionTitles" />
            </el-input>
          </el-col>
        </el-row>

        <el-table :data="questionTitles" border style="width: 100%" class="margin-top">
          <el-table-column prop="qaTitle" :label="$t('chooseQuestionTitle.table.title')" align="center" />
          <el-table-column prop="createTime" :label="$t('chooseQuestionTitle.table.createTime')" align="center" />
          <el-table-column :label="$t('chooseQuestionTitle.table.actions')" align="center">
            <template slot-scope="scope">
              <el-button type="primary" size="mini" @click="handleChoose(scope.row)">
                {{ $t('chooseQuestionTitle.table.choose') }}
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
          :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
          @current-change="handleCurrentChange" class="margin-top" />
      </el-col>
    </el-row>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { listQuestionTitle } from '@/api/oa/editQuestionAnswerApi'

export default {
  name: 'ChooseQuestionTitle',

  data() {
    return {
      visible: false,
      questionTitles: [],
      currentQuestionTitleName: '',
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  methods: {
    open() {
      this.visible = true
      this.resetQuestionTitles()
      this.loadAllQuestionTitleInfo()
    },
    async loadAllQuestionTitleInfo() {
      try {
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          communityId: getCommunityId(),
          qaTitle: this.currentQuestionTitleName
        }
        const { data, total } = await listQuestionTitle(params)
        this.questionTitles = data
        this.pagination.total = total
      } catch (error) {
        console.error('Failed to load question titles:', error)
      }
    },
    handleChoose(questionTitle) {
      this.$emit('chooseQuestionTitle', questionTitle)
      this.visible = false
    },
    queryQuestionTitles() {
      this.pagination.current = 1
      this.loadAllQuestionTitleInfo()
    },
    resetQuestionTitles() {
      this.currentQuestionTitleName = ''
      this.pagination.current = 1
    },
    handleSizeChange(size) {
      this.pagination.size = size
      this.loadAllQuestionTitleInfo()
    },
    handleCurrentChange(current) {
      this.pagination.current = current
      this.loadAllQuestionTitleInfo()
    },
    handleClose() {
      this.resetQuestionTitles()
    }
  }
}
</script>

<style lang="scss" scoped>
.margin-top {
  margin-top: 15px;
}
</style>