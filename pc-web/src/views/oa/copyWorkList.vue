<template>
  <div class="copy-work-container">
    <el-row :gutter="20">
      <el-col :span="4">
        <div class="border-radius">
          <div class="margin-xs-r treeview">
            <ul class="list-group text-center border-radius">
              <li
                v-for="(item,index) in copyWorkInfo.states"
                :key="index"
                class="list-group-item node-orgTree"
                :class="{'vc-node-selected':copyWorkInfo.conditions.state == item.state}"
                @click="swatchWorkState(item)"
              >
                {{ $t(`copyWork.state.${item.state || 'all'}`) }}
              </li>
            </ul>
          </div>
        </div>
      </el-col>
      <el-col :span="20">
        <el-card class="box-card">
          <div slot="header" class="flex justify-between">
            <h5>{{ $t('copyWork.search.title') }}</h5>
          </div>
          <div class="search-wrapper">
            <el-row :gutter="20">
              <el-col :span="4">
                <el-input
                  v-model="copyWorkInfo.conditions.workName"
                  :placeholder="$t('copyWork.search.workName')"
                  clearable
                />
              </el-col>
              <el-col :span="4">
                <el-input
                  v-model="copyWorkInfo.conditions.createUserNameLike"
                  :placeholder="$t('copyWork.search.createUser')"
                  clearable
                />
              </el-col>
              <el-col :span="4">
                <el-date-picker
                  v-model="copyWorkInfo.conditions.queryStartTime"
                  type="datetime" value-format="yyyy-MM-dd HH:mm:ss"                  :placeholder="$t('copyWork.search.startTime')"
                  style="width: 100%" 
                />
              </el-col>
              <el-col :span="4">
                <el-date-picker
                  v-model="copyWorkInfo.conditions.queryEndTime"
                  type="datetime" value-format="yyyy-MM-dd HH:mm:ss"                  :placeholder="$t('copyWork.search.endTime')"
                  style="width: 100%" 
                />
              </el-col>
              <el-col :span="4">
                <el-button type="primary" @click="_queryCopyWorkMethod">
                  <i class="el-icon-search"></i>
                  {{ $t('common.search') }}
                </el-button>
                <el-button @click="_resetCopyWorkMethod">
                  <i class="el-icon-refresh"></i>
                  {{ $t('common.reset') }}
                </el-button>
              </el-col>
            </el-row>
          </div>
        </el-card>

        <el-card class="box-card">
          <div slot="header" class="flex justify-between">
            <div>{{ $t('copyWork.list.title') }}</div>
          </div>
          <el-table
            :data="copyWorkInfo.works"
            border
            style="width: 100%"
            v-loading="loading"
          >
            <el-table-column
              prop="workId"
              :label="$t('copyWork.table.id')"
              align="center"
            />
            <el-table-column
              prop="workName"
              :label="$t('copyWork.table.workName')"
              align="center"
            />
            <el-table-column
              prop="typeName"
              :label="$t('copyWork.table.typeName')"
              align="center"
            />
            <el-table-column
              :label="$t('copyWork.table.workCycle')"
              align="center"
            >
              <template slot-scope="scope">
                {{ scope.row.workCycle == '1001' ? $t('copyWork.cycle.once') : $t('copyWork.cycle.periodic') }}
              </template>
            </el-table-column>
            <el-table-column
              prop="startTime"
              :label="$t('copyWork.table.startTime')"
              align="center"
            />
            <el-table-column
              prop="endTime"
              :label="$t('copyWork.table.endTime')"
              align="center"
            />
            <el-table-column
              prop="createUserName"
              :label="$t('copyWork.table.createUser')"
              align="center"
            />
            <el-table-column
              prop="curStaffName"
              :label="$t('copyWork.table.handler')"
              align="center"
            >
              <template slot-scope="scope">
                {{ scope.row.curStaffName || '-' }}
              </template>
            </el-table-column>
            <el-table-column
              prop="curCopyName"
              :label="$t('copyWork.table.copyUser')"
              align="center"
            >
              <template slot-scope="scope">
                {{ scope.row.curCopyName || '-' }}
              </template>
            </el-table-column>
            <el-table-column
              prop="stateName"
              :label="$t('copyWork.table.status')"
              align="center"
            />
            <el-table-column
              prop="createTime"
              :label="$t('copyWork.table.createTime')"
              align="center"
            />
            <el-table-column
              :label="$t('common.operation')"
              align="center"
              width="180"
            >
              <template slot-scope="scope">
                <el-button
                  v-if="scope.row.state != 'C'"
                  size="mini"
                  type="primary"
                  @click="_openCopyWorkModel(scope.row)"
                >
                  {{ $t('common.process') }}
                </el-button>
                <el-button
                  size="mini"
                  @click="_toWorkDetailPage(scope.row)"
                >
                  {{ $t('common.detail') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            :current-page.sync="page.current"
            :page-sizes="[10, 20, 30, 50]"
            :page-size="page.size"
            :total="page.total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </el-card>
      </el-col>
    </el-row>

    <el-dialog
      :title="$t('copyWork.audit.title')"
      :visible.sync="auditDialogVisible"
      width="50%"
    >
      <el-form>
        <el-form-item :label="$t('copyWork.audit.message')">
          <el-input
            type="textarea"
            :placeholder="$t('copyWork.audit.messagePlaceholder')"
            v-model="copyWorkInfo.audit.auditMessage"
            :rows="4"
          />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="auditDialogVisible = false">
          {{ $t('common.cancel') }}
        </el-button>
        <el-button type="primary" @click="_auditSubmit">
          {{ $t('common.submit') }}
        </el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { queryCopyWork, finishWorkCopy } from '@/api/oa/copyWorkApi'

export default {
  name: 'CopyWorkList',
  data() {
    return {
      loading: false,
      auditDialogVisible: false,
      page: {
        current: 1,
        size: 10,
        total: 0
      },
      copyWorkInfo: {
        works: [],
        states: [
          {
            name: '全部',
            state: ''
          }, {
            name: '待处理',
            state: 'W'
          }, {
            name: '已处理',
            state: 'C'
          }
        ],
        audit: {
          copyId: '',
          auditMessage: '',
        },
        conditions: {
          workName: '',
          state: '',
          typeName: '',
          timeout: '',
          queryEndTime: '',
          queryStartTime: '',
          createUserNameLike: '',
          page: 1,
          row: 10
        }
      }
    }
  },
  created() {
    this._listCopyWorks(this.page.current, this.page.size)
  },
  methods: {
    async _listCopyWorks(page, size) {
      try {
        this.loading = true
        this.copyWorkInfo.conditions.page = page
        this.copyWorkInfo.conditions.row = size
        const { data, total } = await queryCopyWork(this.copyWorkInfo.conditions)
        this.copyWorkInfo.works = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('copyWork.fetchError'))
      } finally {
        this.loading = false
      }
    },
    _queryCopyWorkMethod() {
      this.page.current = 1
      this._listCopyWorks(this.page.current, this.page.size)
    },
    _resetCopyWorkMethod() {
      this.copyWorkInfo.conditions = {
        workName: '',
        state: '',
        typeName: '',
        timeout: '',
        queryEndTime: '',
        queryStartTime: '',
        createUserNameLike: '',
        page: 1,
        row: 10
      }
      this._listCopyWorks(this.page.current, this.page.size)
    },
    swatchWorkState(state) {
      this.copyWorkInfo.conditions.state = state.state
      this._listCopyWorks(this.page.current, this.page.size)
    },
    _toWorkDetailPage(work) {
      this.$router.push(`/views/oa/workDetail?workId=${work.workId}`)
    },
    _openCopyWorkModel(work) {
      this.$router.push(`/views/oa/doCopyWork?workId=${work.workId}&copyId=${work.copyId}`)
    },
    async _auditSubmit() {
      try {
        await finishWorkCopy(this.copyWorkInfo.audit)
        this.$message.success(this.$t('common.operationSuccess'))
        this.auditDialogVisible = false
        this._listCopyWorks(this.page.current, this.page.size)
      } catch (error) {
        this.$message.error(error.message || this.$t('common.submitFailed'))
      }
    },
    handleSizeChange(val) {
      this.page.size = val
      this._listCopyWorks(this.page.current, this.page.size)
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._listCopyWorks(this.page.current, this.page.size)
    }
  }
}
</script>

<style lang="scss" scoped>
.copy-work-container {
  padding: 20px;

  .border-radius {
    border-radius: 4px;
    overflow: hidden;
  }

  .margin-xs-r {
    margin-right: 10px;
  }

  .list-group {
    list-style: none;
    padding: 0;
    margin: 0;

    .list-group-item {
      padding: 10px 15px;
      border: 1px solid #ddd;
      margin-bottom: -1px;
      cursor: pointer;
      background-color: #fff;

      &:hover {
        background-color: #f5f5f5;
      }

      &.vc-node-selected {
        background-color: #409EFF;
        color: #fff;
      }
    }
  }

  .box-card {
    margin-bottom: 20px;
  }

  .search-wrapper {
    margin-bottom: 20px;
  }
}
</style>