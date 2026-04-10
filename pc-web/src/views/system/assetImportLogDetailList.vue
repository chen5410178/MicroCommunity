<template>
  <div class="animated fadeInRight ecommerce">
    <el-row>
      <el-col :span="4" class="padding-r-0">
        <div class="border-radius">
          <div class="margin-xs-r treeview attendance-staff">
            <ul class="list-group text-center border-radius">
              <li class="list-group-item node-orgTree" v-for="(item, index) in assetImportLogDetailInfo.states"
                :key="index" @click="swatchDetailState(item)"
                :class="{ 'vc-node-selected': assetImportLogDetailInfo.state === item.value }">
                {{ item.name }}
              </li>
            </ul>
          </div>
        </div>
      </el-col>
      <el-col :span="20">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('assetImportLogDetail.title') }}</span>
            <div class="ibox-tools" style="float: right;">
              <el-button type="primary" size="small" @click="queryAssetImportLogDetail()">
                {{ $t('common.refresh') }}
              </el-button>
              <el-button size="small" @click="_goBack()">
                {{ $t('common.back') }}
              </el-button>
            </div>
          </div>
          <el-table :data="assetImportLogDetailInfo.logs" border style="width: 100%" v-loading="loading">
            <el-table-column v-for="(item, index) in assetImportLogDetailInfo.logTypes" :key="index"
              :label="item.logColumn" align="center">
              <template slot-scope="scope">
                <div class="textAuto" style="max-width: 200px;">
                  {{ scope.row[item.logProperty] }}
                </div>
              </template>
            </el-table-column>
            <el-table-column :label="$t('assetImportLogDetail.importTime')" align="center">
              <template slot-scope="scope">
                {{ scope.row.createTime }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('assetImportLogDetail.status')" align="center">
              <template slot-scope="scope">
                <span v-if="scope.row.state === 'W'">{{ $t('assetImportLogDetail.waitingImport') }}</span>
                <span v-else-if="scope.row.state === 'C'">{{ $t('assetImportLogDetail.success') }}</span>
                <span v-else>{{ $t('assetImportLogDetail.failed') }}</span>
              </template>
            </el-table-column>
            <el-table-column :label="$t('assetImportLogDetail.description')" align="center">
              <template slot-scope="scope">
                <el-tooltip :content="scope.row.message" placement="top" :disabled="!scope.row.message || scope.row.message.length <= 20" popper-class="custom-tooltip">
                  <div class="textAuto" style="max-width: 200px;">
                    {{ scope.row.message }}
                  </div>
                </el-tooltip>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
            :current-page="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
            layout="total, sizes, prev, pager, next, jumper" :total="assetImportLogDetailInfo.total"
            class="pagination"></el-pagination>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { queryAssetImportLogDetail, queryAssetImportLogType } from '@/api/system/assetImportLogDetailApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AssetImportLogDetailList',
  data() {
    return {
      loading: false,
      assetImportLogDetailInfo: {
        logs: [],
        logTypes: [],
        states: [
          { name: this.$t('assetImportLogDetail.all'), value: '' },
          { name: this.$t('assetImportLogDetail.waitingImport'), value: 'W' },
          { name: this.$t('assetImportLogDetail.success'), value: 'C' },
          { name: this.$t('assetImportLogDetail.failed'), value: 'F' }
        ],
        total: 0,
        records: 1,
        logId: '',
        logType: '',
        state: ''
      },
      page: {
        current: 1,
        size: 10
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  mounted() {
    this.assetImportLogDetailInfo.logId = this.$route.query.logId
    this.assetImportLogDetailInfo.logType = this.$route.query.logType
    this.queryAssetImportLogType()
  },
  methods: {
    async _listAssetImportLogDetails(page, size) {
      try {
        this.loading = true
        const params = {
          page: page,
          row: size,
          logId: this.assetImportLogDetailInfo.logId,
          communityId: this.communityId,
          state: this.assetImportLogDetailInfo.state
        }
        const { data, total } = await queryAssetImportLogDetail(params)
        this.assetImportLogDetailInfo.logs = data
        this.assetImportLogDetailInfo.total = total
      } catch (error) {
        console.error('请求失败:', error)
      } finally {
        this.loading = false
      }
    },
    async queryAssetImportLogType() {
      try {
        const params = {
          logType: this.assetImportLogDetailInfo.logType
        }
        const { data } = await queryAssetImportLogType(params)
        this.assetImportLogDetailInfo.logTypes = data
        this._listAssetImportLogDetails(this.page.current, this.page.size)
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    _goBack() {
      this.$router.go(-1)
    },
    queryAssetImportLogDetail() {
      this._listAssetImportLogDetails(this.page.current, this.page.size)
    },
    swatchDetailState(item) {
      this.assetImportLogDetailInfo.state = item.value
      this._listAssetImportLogDetails(this.page.current, this.page.size)
    },
    handleSizeChange(val) {
      this.page.size = val
      this._listAssetImportLogDetails(this.page.current, val)
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._listAssetImportLogDetails(val, this.page.size)
    }
  }
}
</script>

<style scoped>
.animated {
  padding: 15px;
}

.padding-r-0 {
  padding-right: 0;
}

.border-radius {
  border-radius: 4px;
}

.margin-xs-r {
  margin-right: 5px;
}

.list-group {
  padding-left: 0;
  margin-bottom: 0;
}

.list-group-item {
  position: relative;
  display: block;
  padding: 10px 15px;
  margin-bottom: -1px;
  background-color: #fff;
  cursor: pointer;
}

.list-group-item:hover {
  background-color: #f5f5f5;
}

.vc-node-selected {
  background-color: #409EFF;
  color: #fff;
}

.textAuto {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.pagination {
  margin-top: 15px;
  text-align: right;
}

/* 自定义 tooltip 样式 */
</style>

<style>
.custom-tooltip {
  max-width: 600px !important;
  word-wrap: break-word !important;
  word-break: break-all !important;
}
</style>