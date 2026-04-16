<template>
  <el-dialog
    :title="$t('searchFloor.title')"
    :visible.sync="dialogVisible"
    width="70%"
    @close="handleClose"
  >
    <el-row :gutter="20">
      <el-col :span="6">
        <el-input
          v-model.trim="searchFloorInfo.floorId"
          :placeholder="$t('searchFloor.floorId')"
          clearable
        ></el-input>
      </el-col>
      <el-col :span="6">
        <el-input
          v-model.trim="searchFloorInfo.floorName"
          :placeholder="$t('searchFloor.floorName')"
          clearable
        ></el-input>
      </el-col>
      <el-col :span="8">
        <el-input
          v-model.trim="searchFloorInfo.floorNum"
          :placeholder="$t('searchFloor.floorNum')"
          clearable
          style="margin-right: 10px"
        ></el-input>
        <el-button type="primary" @click="searchFloors">
          <i class="el-icon-search"></i>
          {{ $t('common.search') }}
        </el-button>
        <el-button type="info" @click="resetFloors">
          <i class="el-icon-refresh"></i>
          {{ $t('common.reset') }}
        </el-button>
      </el-col>
    </el-row>

    <el-table
      :data="searchFloorInfo.floors"
      border
      style="width: 100%; margin-top: 20px"
      v-loading="loading"
    >
      <el-table-column
        prop="floorId"
        :label="$t('searchFloor.table.floorId')"
        align="center"
      ></el-table-column>
      <el-table-column
        prop="floorName"
        :label="$t('searchFloor.table.floorName')"
        align="center"
      ></el-table-column>
      <el-table-column
        prop="floorNum"
        :label="$t('searchFloor.table.floorNum')"
        align="center"
      ></el-table-column>
      <el-table-column
        prop="userName"
        :label="$t('searchFloor.table.userName')"
        align="center"
      ></el-table-column>
      <el-table-column
        :label="$t('common.operation')"
        align="center"
        width="120"
      >
        <template slot-scope="scope">
          <el-button
            type="primary"
            size="mini"
            @click="chooseFloor(scope.row)"
          >
            {{ $t('common.select') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="page.current"
      :page-sizes="[10, 20, 30, 50]"
      :page-size="page.size"
      layout="total, sizes, prev, pager, next, jumper"
      :total="page.total"
      style="margin-top: 20px;"
    ></el-pagination>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { queryFloors } from '@/api/fee/listOweFeeApi'

export default {
  name: 'SearchFloor',
  data() {
    return {
      dialogVisible: false,
      loading: false,
      searchFloorInfo: {
        floors: [],
        floorId: '',
        floorName: '',
        floorNum: ''
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  methods: {
    open() {
      this.dialogVisible = true
      this.refreshSearchFloorData()
      this.loadAllFloorInfo()
    },
    async loadAllFloorInfo() {
      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
          communityId: getCommunityId(),
          floorId: this.searchFloorInfo.floorId,
          floorName: this.searchFloorInfo.floorName,
          floorNum: this.searchFloorInfo.floorNum
        }
        const { data, total } = await queryFloors(params)
        this.searchFloorInfo.floors = data
        this.page.total = total
      } catch (error) {
        console.error('获取楼栋信息失败:', error)
      } finally {
        this.loading = false
      }
    },
    chooseFloor(floor) {
      this.$emit('chooseFloor', floor)
      this.dialogVisible = false
    },
    searchFloors() {
      this.page.current = 1
      this.loadAllFloorInfo()
    },
    resetFloors() {
      this.searchFloorInfo.floorId = ''
      this.searchFloorInfo.floorName = ''
      this.searchFloorInfo.floorNum = ''
      this.page.current = 1
      this.loadAllFloorInfo()
    },
    refreshSearchFloorData() {
      this.searchFloorInfo.floorId = ''
      this.searchFloorInfo.floorName = ''
      this.searchFloorInfo.floorNum = ''
    },
    handleClose() {
      this.refreshSearchFloorData()
    },
    handleSizeChange(val) {
      this.page.size = val
      this.loadAllFloorInfo()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.loadAllFloorInfo()
    }
  }
}
</script>

<style lang="scss" scoped>
.el-dialog {
  .el-row {
    margin-bottom: 20px;
  }
}
</style>