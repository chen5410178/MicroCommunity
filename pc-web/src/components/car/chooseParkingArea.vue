<template>
  <el-dialog
    :title="$t('chooseParkingArea.title')"
    :visible.sync="visible"
    width="70%"
  >
    <el-card>
      <div slot="header">
        <el-row :gutter="20">
          <el-col :span="18"></el-col>
          <el-col :span="6">
            <el-input-group>
              <el-input
                v-model="searchForm.num"
                :placeholder="$t('chooseParkingArea.parkingLotNumPlaceholder')"
              ></el-input>
              <el-button 
                slot="append" 
                type="primary" 
                @click="queryParkingAreas"
              >
                {{ $t('chooseParkingArea.query') }}
              </el-button>
              <el-button 
                type="primary" 
                @click="resetParkingAreas"
              >
                {{ $t('chooseParkingArea.reset') }}
              </el-button>
            </el-input-group>
          </el-col>
        </el-row>
      </div>
      
      <el-table :data="parkingAreas" border>
        <el-table-column prop="paId" :label="$t('chooseParkingArea.parkingLotId')" align="center"></el-table-column>
        <el-table-column prop="num" :label="$t('chooseParkingArea.parkingLotNum')" align="center"></el-table-column>
        <el-table-column prop="typeCd" :label="$t('chooseParkingArea.parkingLotType')" align="center"></el-table-column>
        <el-table-column :label="$t('chooseParkingArea.operation')" align="center" width="120">
          <template slot-scope="scope">
            <el-button 
              type="primary" 
              size="mini"
              @click="selectParkingArea(scope.row)"
            >
              {{ $t('chooseParkingArea.select') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handlePageChange"
        :current-page="pagination.current"
        :page-sizes="[10, 20, 30, 50]"
        :page-size="pagination.size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total"
        style="margin-top:20px;text-align:right"
      ></el-pagination>
    </el-card>
  </el-dialog>
</template>

<script>
import { listParkingAreas } from '@/api/car/listParkingSpaceApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ChooseParkingArea',
  data() {
    return {
      visible: false,
      searchForm: {
        num: ''
      },
      parkingAreas: [],
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
      this.loadParkingAreas()
    },
    
    async loadParkingAreas() {
      try {
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          communityId: getCommunityId(),
          num: this.searchForm.num
        }
        
        const res = await listParkingAreas(params)
        this.parkingAreas = res.data.parkingAreas
        this.pagination.total = res.data.total
      } catch (error) {
        this.$message.error(this.$t('common.loadError'))
      }
    },
    
    selectParkingArea(row) {
      this.$emit('choose', row)
      this.visible = false
    },
    
    queryParkingAreas() {
      this.pagination.current = 1
      this.loadParkingAreas()
    },
    
    resetParkingAreas() {
      this.searchForm.num = ''
      this.queryParkingAreas()
    },
    
    handlePageChange(page) {
      this.pagination.current = page
      this.loadParkingAreas()
    },
    
    handleSizeChange(size) {
      this.pagination.size = size
      this.loadParkingAreas()
    }
  }
}
</script>