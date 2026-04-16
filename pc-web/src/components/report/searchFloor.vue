<template>
  <el-dialog
    :title="$t('searchFloor.title')"
    :visible.sync="visible"
    width="70%"
    @close="handleClose">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card>
          <el-row :gutter="20">
            <el-col :span="6">
              <el-input
                v-model="searchForm.floorId"
                :placeholder="$t('searchFloor.floorId')"
                clearable>
              </el-input>
            </el-col>
            <el-col :span="6">
              <el-input
                v-model="searchForm.floorName"
                :placeholder="$t('searchFloor.floorName')"
                clearable>
              </el-input>
            </el-col>
            <el-col :span="6">
              <el-input
                v-model="searchForm.floorNum"
                :placeholder="$t('searchFloor.floorNum')"
                clearable>
              </el-input>
            </el-col>
            <el-col :span="6">
              <el-button type="primary" @click="searchFloors">
                <i class="el-icon-search"></i>
                {{ $t('searchFloor.search') }}
              </el-button>
              <el-button @click="resetFloors" style="margin-left: 10px;">
                <i class="el-icon-refresh"></i>
                {{ $t('searchFloor.reset') }}
              </el-button>
            </el-col>
          </el-row>
          
          <el-table
            :data="floors"
            border
            style="width: 100%; margin-top: 20px;"
            v-loading="loading">
            <el-table-column
              prop="floorId"
              :label="$t('searchFloor.table.floorId')"
              align="center">
            </el-table-column>
            <el-table-column
              prop="floorName"
              :label="$t('searchFloor.table.floorName')"
              align="center">
            </el-table-column>
            <el-table-column
              prop="floorNum"
              :label="$t('searchFloor.table.floorNum')"
              align="center">
            </el-table-column>
            <el-table-column
              prop="userName"
              :label="$t('searchFloor.table.userName')"
              align="center">
            </el-table-column>
            <el-table-column
              :label="$t('searchFloor.table.operation')"
              align="center"
              width="120">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="primary"
                  @click="chooseFloor(scope.row)">
                  {{ $t('searchFloor.table.choose') }}
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
            :total="total"
            style="margin-top: 20px;">
          </el-pagination>
        </el-card>
      </el-col>
    </el-row>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { queryFloors } from '@/api/property/floorApi'

export default {
  name: 'SearchFloor',
  data() {
    return {
      visible: false,
      loading: false,
      communityId: '',
      searchForm: {
        floorId: '',
        floorName: '',
        floorNum: ''
      },
      floors: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open() {
      this.visible = true
      this.refreshSearchFloorData()
      this.loadAllFloorInfo()
    },
    async loadAllFloorInfo() {
      this.loading = true
      try {
        const params = {
          page: this.page.current,
          row: this.page.size,
          communityId: this.communityId,
          floorId: this.searchForm.floorId,
          floorName: this.searchForm.floorName,
          floorNum: this.searchForm.floorNum
        }
        const res = await queryFloors(params)
        this.floors = res.data
        this.page.total = res.total
      } catch (error) {
        console.error('获取楼栋列表失败:', error)
      } finally {
        this.loading = false
      }
    },
    chooseFloor(floor) {
      this.$emit('chooseFloor', floor)
      this.visible = false
    },
    searchFloors() {
      this.page.current = 1
      this.loadAllFloorInfo()
    },
    resetFloors() {
      this.searchForm = {
        floorId: '',
        floorName: '',
        floorNum: ''
      }
      this.loadAllFloorInfo()
    },
    handleSizeChange(val) {
      this.page.size = val
      this.loadAllFloorInfo()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.loadAllFloorInfo()
    },
    refreshSearchFloorData() {
      this.searchForm = {
        floorId: '',
        floorName: '',
        floorNum: ''
      }
    },
    handleClose() {
      this.refreshSearchFloorData()
    }
  }
}
</script>

<style lang="scss" scoped>
.el-dialog__body {
  padding: 20px;
}
</style>