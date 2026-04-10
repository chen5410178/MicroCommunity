<template>
  <div class="access-control-inout-container">
    <el-row :gutter="20">
      <el-col :span="4">
        <el-card class="tree-card">
          <ul class="tree-list">
            <li v-for="(item, index) in accessControlInoutInfo.openTypeCds" :key="index" @click="swatchOpenTypeCd(item)"
              :class="{ 'selected': accessControlInoutInfo.conditions.openTypeCd === item.openTypeCd }">
              {{ item.openTypeName }}
            </li>
          </ul>
        </el-card>
      </el-col>
      <el-col :span="20">
        <el-card class="search-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('accessControlInout.search.title') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="6">
              <el-select v-model="accessControlInoutInfo.conditions.machineId"
                :placeholder="$t('accessControlInout.search.machine')" style="width:100%">
                <el-option v-for="item in accessControlInoutInfo.accessControls" :key="item.machineId"
                  :label="item.machineName" :value="item.machineId" />
              </el-select>
            </el-col>
            <el-col :span="6">
              <el-input v-model="accessControlInoutInfo.conditions.name"
                :placeholder="$t('accessControlInout.search.name')" />
            </el-col>
            <el-col :span="6">
              <el-input v-model="accessControlInoutInfo.conditions.tel"
                :placeholder="$t('accessControlInout.search.tel')" />
            </el-col>
            <el-col :span="6">
              <el-select v-model="accessControlInoutInfo.conditions.state"
                :placeholder="$t('accessControlInout.search.state')" style="width:100%">
                <el-option label="全部" value="" />
                <el-option label="开门成功" value="C" />
                <el-option label="开门失败" value="F" />
              </el-select>
            </el-col>
          </el-row>
          <el-row :gutter="20" style="margin-top:15px">
            <el-col :span="6">
              <el-date-picker v-model="accessControlInoutInfo.conditions.queryStartTime" type="datetime"
                :placeholder="$t('accessControlInout.search.startTime')" style="width:100%" />
            </el-col>
            <el-col :span="6">
              <el-date-picker v-model="accessControlInoutInfo.conditions.queryEndTime" type="datetime"
                :placeholder="$t('accessControlInout.search.endTime')" style="width:100%" />
            </el-col>
            <el-col :span="6">
              <el-button type="primary" @click="_queryAccessControlInoutMethod">
                {{ $t('common.search') }}
              </el-button>
            </el-col>
          </el-row>
        </el-card>

        <el-card class="table-card" style="margin-top:20px">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('accessControlInout.table.title') }}</span>
          </div>
          <el-table :data="accessControlInoutInfo.accessControlInouts" border style="width:100%" v-loading="loading">
            <el-table-column prop="facePath" :label="$t('accessControlInout.table.face')" align="center" width="100">
              <template slot-scope="scope">
                <el-image style="width:60px;height:60px;cursor:pointer" :src="scope.row.facePath || '/img/noPhoto.jpg'"
                  :preview-src-list="[scope.row.facePath]" fit="cover" />
              </template>
            </el-table-column>
            <el-table-column prop="inoutId" :label="$t('accessControlInout.table.id')" align="center" />
            <el-table-column prop="machineName" :label="$t('accessControlInout.table.machineName')" align="center" />
            <el-table-column prop="machineCode" :label="$t('accessControlInout.table.machineCode')" align="center" />
            <el-table-column prop="name" :label="$t('accessControlInout.table.userName')" align="center" />
            <el-table-column prop="openTypeCd" :label="$t('accessControlInout.table.openType')" align="center">
              <template slot-scope="scope">
                {{ scope.row.openTypeCd === '1000' ? $t('accessControlInout.openType.face') :
                  $t('accessControlInout.openType.other') }}
              </template>
            </el-table-column>
            <el-table-column prop="tel" :label="$t('accessControlInout.table.tel')" align="center" />
            <el-table-column prop="idCard" :label="$t('accessControlInout.table.idCard')" align="center" />
            <el-table-column prop="similar" :label="$t('accessControlInout.table.similar')" align="center" />
            <el-table-column prop="state" :label="$t('accessControlInout.table.state')" align="center">
              <template slot-scope="scope">
                {{ scope.row.state === 'C' ? $t('accessControlInout.state.success') : $t('accessControlInout.state.fail')
                }}
              </template>
            </el-table-column>
            <el-table-column prop="createTime" :label="$t('accessControlInout.table.createTime')" align="center" />
          </el-table>
          <el-pagination style="margin-top:20px;text-align:right" :current-page.sync="page.current"
            :page-sizes="[10, 20, 30, 50]" :page-size="page.size" :total="page.total"
            layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
            @current-change="handleCurrentChange" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listAccessControlInouts, listAccessControls } from '@/api/machine/accessControlInoutApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AccessControlInoutList',
  data() {
    return {
      loading: false,
      communityId: '',
      accessControlInoutInfo: {
        accessControlInouts: [],
        accessControls: [],
        openTypeCds: [
          { openTypeName: this.$t('accessControlInout.openType.all'), openTypeCd: '' },
          { openTypeName: this.$t('accessControlInout.openType.face'), openTypeCd: '1000' },
          { openTypeName: this.$t('accessControlInout.openType.key'), openTypeCd: '2000' },
          { openTypeName: this.$t('accessControlInout.openType.qrcode'), openTypeCd: '3000' },
          { openTypeName: this.$t('accessControlInout.openType.password'), openTypeCd: '4000' }
        ],
        total: 0,
        records: 1,
        conditions: {
          machineId: '',
          name: '',
          openTypeCd: '',
          tel: '',
          state: '',
          queryStartTime: '',
          queryEndTime: '',
          iotApiCode: 'listAccessControlInoutBmoImpl',
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
    this.communityId = getCommunityId()
    this._listAccessControls()
    this._listAccessControlInouts(this.page.current, this.page.size)
  },
  methods: {
    async _listAccessControlInouts(page, rows) {
      try {
        this.loading = true
        this.accessControlInoutInfo.conditions.page = page
        this.accessControlInoutInfo.conditions.row = rows
        this.accessControlInoutInfo.conditions.communityId = this.communityId

        const { data, total } = await listAccessControlInouts(this.accessControlInoutInfo.conditions)
        this.accessControlInoutInfo.accessControlInouts = data
        this.page.total = total
      } catch (error) {
        console.error('获取门禁进出记录失败:', error)
      } finally {
        this.loading = false
      }
    },
    async _listAccessControls() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.communityId,
          iotApiCode: 'listAccessControlBmoImpl'
        }
        const { data } = await listAccessControls(params)
        this.accessControlInoutInfo.accessControls = data
      } catch (error) {
        console.error('获取门禁设备失败:', error)
      }
    },
    _queryAccessControlInoutMethod() {
      this.page.current = 1
      this._listAccessControlInouts(this.page.current, this.page.size)
    },
    swatchOpenTypeCd(openType) {
      this.accessControlInoutInfo.conditions.openTypeCd = openType.openTypeCd
      this._queryAccessControlInoutMethod()
    },
    handleSizeChange(val) {
      this.page.size = val
      this._listAccessControlInouts(this.page.current, this.page.size)
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._listAccessControlInouts(this.page.current, this.page.size)
    }
  }
}
</script>

<style lang="scss" scoped>
.access-control-inout-container {
  padding: 20px;

  .tree-card {
    height: 100%;

    .tree-list {
      list-style: none;
      padding: 0;
      margin: 0;

      li {
        padding: 10px;
        cursor: pointer;
        text-align: center;
        margin-bottom: 5px;
        border-radius: 4px;

        &:hover {
          background-color: #f5f7fa;
        }

        &.selected {
          background-color: #409eff;
          color: #fff;
        }
      }
    }
  }

  .search-card {
    margin-bottom: 20px;
  }
}
</style>