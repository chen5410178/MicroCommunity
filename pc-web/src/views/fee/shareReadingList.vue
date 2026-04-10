<template>
  <div class="share-reading-container">
    <el-row :gutter="20">
      <el-col :span="4">
        <el-card class="tree-card">
          <ul class="state-list">
            <li v-for="(item, index) in shareReadingInfo.states" :key="index" @click="swatchShareState(item)"
              :class="{ 'active-state': shareReadingInfo.conditions.state === item.statusCd }">
              {{ item.name }}
            </li>
          </ul>
        </el-card>
      </el-col>
      <el-col :span="20">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('shareReading.search.title') }}</span>
          </div>
          <div class="search-wrapper">
            <el-row :gutter="20">
              <el-col :span="6">
                <el-select v-model="shareReadingInfo.conditions.fsmId"
                  :placeholder="$t('shareReading.search.selectMeter')" style="width:100%">
                  <el-option v-for="(item, index) in shareReadingInfo.floorShares" :key="index"
                    :label="`${item.floorNum}栋-${item.meterTypeName}`" :value="item.fsmId" />
                </el-select>
              </el-col>
              <el-col :span="6">
                <el-date-picker v-model="shareReadingInfo.conditions.startTime" type="datetime"
                  :placeholder="$t('shareReading.search.startTime')" style="width:100%" />
              </el-col>
              <el-col :span="6">
                <el-date-picker v-model="shareReadingInfo.conditions.endTime" type="datetime"
                  :placeholder="$t('shareReading.search.endTime')" style="width:100%" />
              </el-col>
              <el-col :span="2">
                <el-button type="primary" @click="_queryShareReadingMethod">
                  {{ $t('common.search') }}
                </el-button>
              </el-col>
            </el-row>
          </div>
        </el-card>

        <el-card class="mt-20">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('shareReading.list.title') }}</span>
            <el-button type="primary" size="small" style="float:right" @click="_openAddShareReadingModal">
              {{ $t('shareReading.list.addReading') }}
            </el-button>
          </div>
          <el-table :data="shareReadingInfo.shareReadings" border style="width:100%" v-loading="loading">
            <el-table-column prop="readingId" :label="$t('shareReading.table.id')" align="center" />
            <el-table-column prop="title" :label="$t('shareReading.table.name')" align="center" />
            <el-table-column prop="preDegrees" :label="$t('shareReading.table.preDegrees')" align="center" />
            <el-table-column prop="curDegrees" :label="$t('shareReading.table.curDegrees')" align="center" />
            <el-table-column :label="$t('shareReading.table.degrees')" align="center">
              <template slot-scope="scope">
                {{ scope.row.curDegrees - scope.row.preDegrees }}
              </template>
            </el-table-column>
            <el-table-column prop="preReadingTime" :label="$t('shareReading.table.preReadingTime')" align="center" />
            <el-table-column prop="curReadingTime" :label="$t('shareReading.table.curReadingTime')" align="center" />
            <el-table-column prop="createStaffName" :label="$t('shareReading.table.reader')" align="center" />
            <el-table-column :label="$t('shareReading.table.status')" align="center">
              <template slot-scope="scope">
                <span v-if="scope.row.state === 'W'">{{ $t('shareReading.status.waiting') }}</span>
                <span v-else-if="scope.row.state === 'C'">{{ $t('shareReading.status.passed') }}</span>
                <span v-else>{{ $t('shareReading.status.rejected') }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="remark" :label="$t('shareReading.table.remark')" align="center">
              <template slot-scope="scope">
                {{ scope.row.remark || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="auditStaffName" :label="$t('shareReading.table.auditor')" align="center">
              <template slot-scope="scope">
                {{ scope.row.auditStaffName || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="stateMsg" :label="$t('shareReading.table.auditOpinion')" align="center">
              <template slot-scope="scope">
                {{ scope.row.stateMsg || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="shareMsg" :label="$t('shareReading.table.shareProgress')" align="center">
              <template slot-scope="scope">
                {{ scope.row.shareMsg || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="createTime" :label="$t('shareReading.table.readingTime')" align="center" />
            <el-table-column :label="$t('common.operation')" align="center" width="200">
              <template slot-scope="scope">
                <el-button v-if="scope.row.state === 'W' && hasPrivilege('502025032678040006')" size="mini"
                  type="primary" @click="_openAuditShareReadingModel(scope.row)">
                  {{ $t('common.audit') }}
                </el-button>
                <el-button v-if="scope.row.state === 'W'" size="mini" type="danger"
                  @click="_openDeleteShareReadingModel(scope.row)">
                  {{ $t('common.delete') }}
                </el-button>
                <el-button size="mini" type="success" @click="_openShareFee(scope.row)">
                  {{ $t('shareReading.table.shareFee') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination class="mt-20" :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]"
            :page-size="page.size" :total="page.total" layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange" @current-change="handleCurrentChange" />
        </el-card>
      </el-col>
    </el-row>

    <add-share-reading ref="addShareReading" @success="handleSuccess" />
    <audit-share-reading ref="auditShareReading" @success="handleSuccess" />
    <delete-share-reading ref="deleteShareReading" @success="handleSuccess" />
  </div>
</template>

<script>
import { getDict, getCommunityId } from '@/api/community/communityApi'
import { listFloorShareReading, listFloorShareMeter } from '@/api/fee/shareReadingApi'
import AddShareReading from '@/components/fee/addShareReading'
import AuditShareReading from '@/components/fee/auditShareReading'
import DeleteShareReading from '@/components/fee/deleteShareReading'

export default {
  name: 'ShareReadingList',
  components: {
    AddShareReading,
    AuditShareReading,
    DeleteShareReading
  },
  data() {
    return {
      loading: false,
      shareReadingInfo: {
        shareReadings: [],
        states: [],
        floorShares: [],
        conditions: {
          fsmId: '',
          state: '',
          communityId: '',
          startTime: '',
          endTime: '',
          page: 1,
          row: 10
        }
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.initData()
  },
  methods: {
    async initData() {
      try {
        this.loading = true
        this.shareReadingInfo.conditions.communityId = getCommunityId()

        // 获取状态字典
        const states = await getDict('floor_share_reading', 'state')
        this.shareReadingInfo.states = [{ statusCd: '', name: this.$t('common.all') }, ...states]

        await this._listFloorShares()
        await this._listShareReadings()
      } catch (error) {
        console.error('初始化数据失败:', error)
      } finally {
        this.loading = false
      }
    },
    async _listShareReadings() {
      try {
        this.loading = true
        const params = {
          ...this.shareReadingInfo.conditions,
          page: this.page.current,
          row: this.page.size
        }
        const { data, total } = await listFloorShareReading(params)
        this.shareReadingInfo.shareReadings = data
        this.page.total = total
      } catch (error) {
        console.error('获取公摊抄表列表失败:', error)
      } finally {
        this.loading = false
      }
    },
    async _listFloorShares() {
      try {
        const params = {
          page: 1,
          row: 300,
          communityId: getCommunityId()
        }
        const { data } = await listFloorShareMeter(params)
        this.shareReadingInfo.floorShares = data
      } catch (error) {
        console.error('获取公摊表列表失败:', error)
      }
    },
    _openAddShareReadingModal() {
      this.$refs.addShareReading.open()
    },
    _openAuditShareReadingModel(row) {
      this.$refs.auditShareReading.open(row)
    },
    _openDeleteShareReadingModel(row) {
      this.$refs.deleteShareReading.open(row)
    },
    _queryShareReadingMethod() {
      this.page.current = 1
      this._listShareReadings()
    },
    swatchShareState(state) {
      this.shareReadingInfo.conditions.state = state.statusCd
      this._queryShareReadingMethod()
    },
    _openShareFee(reading) {
      this.$router.push(`/views/fee/shareReadingFee?readingId=${reading.readingId}`)
    },
    handleSuccess() {
      this._listShareReadings()
    },
    handleSizeChange(val) {
      this.page.size = val
      this._listShareReadings()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._listShareReadings()
    },

  }
}
</script>

<style lang="scss" scoped>
.share-reading-container {
  padding: 20px;

  .tree-card {
    height: 100%;

    .state-list {
      list-style: none;
      padding: 0;
      margin: 0;

      li {
        padding: 10px;
        text-align: center;
        cursor: pointer;
        border-radius: 4px;
        margin-bottom: 5px;

        &:hover {
          background-color: #f5f7fa;
        }

        &.active-state {
          background-color: #409eff;
          color: #fff;
        }
      }
    }
  }

  .mt-20 {
    margin-top: 20px;
  }

  .search-wrapper {
    margin-bottom: 20px;
  }
}
</style>