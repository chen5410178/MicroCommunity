<template>
  <div class="remaining-parking-space-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <div>{{ $t('remainingParkingSpace.title') }}</div>
      </div>
      <div class="card-content">
        <el-row>
          <el-col :span="24">
            <el-table
              v-loading="loading"
              :data="tableData"
              style="width: 100%"
              border
              stripe
            >
              <el-table-column
                prop="total"
                :label="$t('remainingParkingSpace.total')"
                align="center"
              >
                <template slot-scope="scope">
                  {{ scope.row.total }} {{ $t('remainingParkingSpace.unit') }}
                </template>
              </el-table-column>
              <el-table-column
                prop="freeCount"
                :label="$t('remainingParkingSpace.freeCount')"
                align="center"
              >
                <template slot-scope="scope">
                  {{ scope.row.freeCount }} {{ $t('remainingParkingSpace.unit') }}
                </template>
              </el-table-column>
              <el-table-column
                prop="createTime"
                :label="$t('remainingParkingSpace.createTime')"
                align="center"
              />
              <el-table-column
                :label="$t('remainingParkingSpace.operation')"
                align="center"
                width="150"
              >
                <template slot-scope="scope">
                  <el-button
                    size="mini"
                    type="primary"
                    @click="handleRefresh(scope.row)"
                  >
                    {{ $t('remainingParkingSpace.refresh') }}
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getRemainingParkingSpace } from '@/api/car/remainingParkingSpaceApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'RemainingParkingSpaceList',
  data() {
    return {
      loading: false,
      tableData: [
        {
          total: 0,
          freeCount: 0,
          createTime: this.formatDateTime(new Date().getTime())
        }
      ],
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.getData()
  },
  methods: {
    formatDateTime(timestamp) {
      const date = new Date(timestamp)
      return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')} ${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}:${date.getSeconds().toString().padStart(2, '0')}`
    },
    async getData() {
      try {
        this.loading = true
        const params = {
          communityId: this.communityId,
          iotApiCode: 'getFreeParkingSpaceBmoImpl'
        }
        
        const res = await getRemainingParkingSpace(params)
        if (res.code === 0) {
          this.tableData = [{
            total: res.data.total,
            freeCount: res.data.freeCount,
            createTime: this.formatDateTime(new Date().getTime())
          }]
        } else {
          this.$message.error(res.msg || this.$t('remainingParkingSpace.fetchError'))
        }
      } catch (error) {
        this.$message.error(this.$t('remainingParkingSpace.fetchError'))
        console.error('Error fetching remaining parking space:', error)
      } finally {
        this.loading = false
      }
    },
    handleRefresh() {
      this.getData()
    }
  }
}
</script>

<style lang="scss" scoped>
.remaining-parking-space-container {
  padding: 20px;
  
  .box-card {
    margin-bottom: 20px;
    
    .clearfix {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
  }
  
  .card-content {
    padding: 20px;
  }
}
</style>