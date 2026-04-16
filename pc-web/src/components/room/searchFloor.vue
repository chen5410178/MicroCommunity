<template>
  <el-dialog
    :title="$t('room.searchFloor.title')"
    :visible.sync="dialogVisible"
    width="80%"
  >
    <el-row>
      <el-col :span="24">
        <el-row :gutter="10">
          <el-col :sm="8" :md="6">
            <el-input
              v-model="searchParams.floorId"
              :placeholder="$t('room.searchFloor.placeholder.floorId')"
            />
          </el-col>
          <el-col :sm="8" :md="6">
            <el-input
              v-model="searchParams.floorName"
              :placeholder="$t('room.searchFloor.placeholder.floorName')"
            />
          </el-col>
          <el-col :sm="8" :md="6">
            <el-input
              v-model="searchParams.floorNum"
              :placeholder="$t('room.searchFloor.placeholder.floorNum')"
            />
          </el-col>
          <el-col :sm="24" :md="6">
            <el-button type="primary" @click="searchFloors">
              <i class="el-icon-search"></i>
              {{ $t('common.search') }}
            </el-button>
            <el-button @click="resetFloors">
              <i class="el-icon-refresh"></i>
              {{ $t('common.reset') }}
            </el-button>
          </el-col>
        </el-row>
        
        <el-table :data="floorList" style="width: 100%; margin-top: 15px">
          <el-table-column prop="floorId" :label="$t('room.searchFloor.table.floorId')" align="center" />
          <el-table-column prop="floorName" :label="$t('room.searchFloor.table.floorName')" align="center" />
          <el-table-column prop="floorNum" :label="$t('room.searchFloor.table.floorNum')" align="center" />
          <el-table-column prop="userName" :label="$t('room.searchFloor.table.creator')" align="center" />
          <el-table-column :label="$t('room.searchFloor.table.actions')" align="center">
            <template #default="{ row }">
              <el-button size="mini" type="primary" @click="chooseFloor(row)">
                {{ $t('common.select') }}
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        
        <el-pagination
          background
          layout="prev, pager, next"
          :total="total"
          :page-size="pageSize"
          :current-page="currentPage"
          @current-change="handlePageChange"
        />
      </el-col>
    </el-row>
  </el-dialog>
</template>

<script>
import { searchFloors } from '@/api/room/searchFloorApi'

export default {
  name: 'SearchFloor',
  props: {
    emitChooseFloor: String,
    emitLoadData: String
  },
  data() {
    return {
      dialogVisible: false,
      searchParams: {
        floorId: '',
        floorName: '',
        floorNum: ''
      },
      floorList: [],
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  methods: {
    open() {
      this.dialogVisible = true
      this.resetFloors()
      this.loadFloors()
    },
    async loadFloors() {
      try {
        const params = {
          ...this.searchParams,
          page: this.currentPage,
          row: this.pageSize,
          communityId: this.getCommunityId()
        }
        
        const response = await searchFloors(params)
        this.floorList = response.apiFloorDataVoList
        this.total = response.total
      } catch (error) {
        console.error('加载楼栋列表失败', error)
      }
    },
    searchFloors() {
      this.currentPage = 1
      this.loadFloors()
    },
    resetFloors() {
      this.searchParams = {
        floorId: '',
        floorName: '',
        floorNum: ''
      }
      this.currentPage = 1
      this.loadFloors()
    },
    handlePageChange(page) {
      this.currentPage = page
      this.loadFloors()
    },
    chooseFloor(floor) {
      this.$emit('chooseFloor', floor)
      this.$eventBus.$emit(this.emitChooseFloor, 'chooseFloor', floor)
      this.$eventBus.$emit(this.emitLoadData, 'loadData', { floorId: floor.floorId })
      this.dialogVisible = false
    },
  }
}
</script>