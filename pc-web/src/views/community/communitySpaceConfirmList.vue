<template>
  <div class="community-space-confirm-container">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="write-off-card">
          <el-row :gutter="10" class="mb-10">
            <el-col :span="18">
              <el-input 
                v-model="communitySpaceConfirmInfo.timeId"
                :placeholder="$t('communitySpaceConfirm.writeOffPlaceholder')"
                @keyup.enter.native="confirmCommunitySpace"
                clearable
              />
            </el-col>
            <el-col :span="6">
              <el-button 
                type="primary" 
                @click="confirmCommunitySpace"
                class="full-width-btn"
              >
                {{ $t('communitySpaceConfirm.writeOff') }}
              </el-button>
            </el-col>
          </el-row>

          <div class="result-card">
            <div class="result-item">
              <span class="label">{{ $t('communitySpaceConfirm.writeOffResult') }}:</span>
              <span>{{ communitySpaceConfirmInfo.order.remark || '-' }}</span>
            </div>
            <div class="result-item">
              <span class="label">{{ $t('communitySpaceConfirm.writeOffTime') }}:</span>
              <span>{{ communitySpaceConfirmInfo.order.createTime || '-' }}</span>
            </div>
            <div class="result-item">
              <span class="label">{{ $t('communitySpaceConfirm.space') }}:</span>
              <span>{{ communitySpaceConfirmInfo.order.spaceName || '-' }}</span>
            </div>
            <div class="result-item">
              <span class="label">{{ $t('communitySpaceConfirm.appointmentDate') }}:</span>
              <span>{{ communitySpaceConfirmInfo.order.appointmentTime || '-' }}</span>
            </div>
            <div class="result-item">
              <span class="label">{{ $t('communitySpaceConfirm.appointmentHours') }}:</span>
              <span>{{ communitySpaceConfirmInfo.order.hours || '-' }}</span>
            </div>
            <div class="result-item">
              <span class="label">{{ $t('communitySpaceConfirm.appointmentPerson') }}:</span>
              <span>{{ communitySpaceConfirmInfo.order.personName || '-' }}</span>
            </div>
            <div class="result-item">
              <span class="label">{{ $t('communitySpaceConfirm.appointmentPhone') }}:</span>
              <span>{{ communitySpaceConfirmInfo.order.personTel || '-' }}</span>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="18">
        <el-card class="search-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('communitySpaceConfirm.searchCondition') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="6">
              <el-input 
                v-model="communitySpaceConfirmInfo.conditions.personName"
                :placeholder="$t('communitySpaceConfirm.personNamePlaceholder')"
                clearable
              />
            </el-col>
            <el-col :span="6">
              <el-input 
                v-model="communitySpaceConfirmInfo.conditions.personTel"
                :placeholder="$t('communitySpaceConfirm.personTelPlaceholder')"
                clearable
              />
            </el-col>
            <el-col :span="6">
              <el-date-picker
                v-model="communitySpaceConfirmInfo.conditions.appointmentTime"
                type="datetime" value-format="yyyy-MM-dd HH:mm:ss"                :placeholder="$t('communitySpaceConfirm.appointmentTimePlaceholder')"
                
                class="full-width"
              />
            </el-col>
            <el-col :span="6">
              <el-button type="primary" @click="queryCommunitySpaceConfirm">
                <i class="el-icon-search"></i>
                {{ $t('communitySpaceConfirm.search') }}
              </el-button>
              <el-button @click="resetCommunitySpaceConfirm">
                <i class="el-icon-refresh"></i>
                {{ $t('communitySpaceConfirm.reset') }}
              </el-button>
            </el-col>
          </el-row>
        </el-card>

        <el-card class="table-card">
          <el-table 
            :data="communitySpaceConfirmInfo.orders"
            border
            style="width: 100%"
            v-loading="loading"
          >
            <el-table-column 
              prop="orderId" 
              :label="$t('communitySpaceConfirm.writeOffOrder')" 
              align="center"
            />
            <el-table-column 
              prop="venueName" 
              :label="$t('communitySpaceConfirm.venue')" 
              align="center"
            />
            <el-table-column 
              prop="spaceName" 
              :label="$t('communitySpaceConfirm.space')" 
              align="center"
            />
            <el-table-column 
              prop="appointmentTime" 
              :label="$t('communitySpaceConfirm.appointmentDate')" 
              align="center"
            />
            <el-table-column 
              prop="hours" 
              :label="$t('communitySpaceConfirm.appointmentTime')" 
              align="center"
            />
            <el-table-column 
              prop="personName" 
              :label="$t('communitySpaceConfirm.appointmentPerson')" 
              align="center"
            />
            <el-table-column 
              prop="personTel" 
              :label="$t('communitySpaceConfirm.appointmentPhone')" 
              align="center"
            />
            <el-table-column 
              prop="createTime" 
              :label="$t('communitySpaceConfirm.writeOffTime')" 
              align="center"
            />
            <el-table-column 
              prop="remark" 
              :label="$t('communitySpaceConfirm.remark')" 
              align="center"
            />
          </el-table>
          
          <el-pagination
            class="mt-20"
            :current-page="pagination.current"
            :page-sizes="[10, 20, 30, 50]"
            :page-size="pagination.size"
            layout="total, sizes, prev, pager, next, jumper"
            :total="pagination.total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { 
  listCommunitySpaceConfirmOrder, 
  saveCommunitySpaceConfirmOrder 
} from '@/api/community/communitySpaceConfirmApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'CommunitySpaceConfirmList',
  data() {
    return {
      loading: false,
      communitySpaceConfirmInfo: {
        orders: [],
        order: {
          remark: '',
          appointmentTime: '',
          createTime: '',
          hours: '',
          spaceName: '',
          personName: '',
          personTel: '',
        },
        timeId: '',
        conditions: {
          spaceId: '',
          personName: '',
          personTel: '',
          appointmentTime: '',
          communityId: ''
        }
      },
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.communitySpaceConfirmInfo.conditions.communityId = getCommunityId()
    this.listCommunitySpaceConfirms()
  },
  methods: {
    async listCommunitySpaceConfirms() {
      this.loading = true
      try {
        const params = {
          ...this.communitySpaceConfirmInfo.conditions,
          page: this.pagination.current,
          row: this.pagination.size
        }
        
        const { data, total } = await listCommunitySpaceConfirmOrder(params)
        this.communitySpaceConfirmInfo.orders = data || []
        this.pagination.total = total
      } catch (error) {
        console.error('获取核销列表失败:', error)
      } finally {
        this.loading = false
      }
    },
    async confirmCommunitySpace() {
      if (!this.communitySpaceConfirmInfo.timeId) {
        this.$message.warning(this.$t('communitySpaceConfirm.scanTip'))
        return
      }
      
      try {
        const data = {
          timeId: this.communitySpaceConfirmInfo.timeId,
          communityId: this.communitySpaceConfirmInfo.conditions.communityId
        }
        
        const result = await saveCommunitySpaceConfirmOrder(data)
        if (result.code == 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.communitySpaceConfirmInfo.timeId = ''
          
          // 更新核销结果
          if (result.data && result.data.length > 0) {
            this.communitySpaceConfirmInfo.order = { 
              ...result.data[0],
              remark: result.data[0].remark || this.$t('communitySpaceConfirm.writeOffSuccess')
            }
          }
          
          // 刷新列表
          this.pagination.current = 1
          this.listCommunitySpaceConfirms()
        } else {
          this.$message.error(result)
        }
      } catch (error) {
        console.error('核销失败:', error)
        this.$message.error(error)
      }
    },
    queryCommunitySpaceConfirm() {
      this.pagination.current = 1
      this.listCommunitySpaceConfirms()
    },
    resetCommunitySpaceConfirm() {
      this.communitySpaceConfirmInfo.conditions.personName = ''
      this.communitySpaceConfirmInfo.conditions.personTel = ''
      this.communitySpaceConfirmInfo.conditions.appointmentTime = ''
      this.queryCommunitySpaceConfirm()
    },
    handleSizeChange(size) {
      this.pagination.size = size
      this.listCommunitySpaceConfirms()
    },
    handleCurrentChange(current) {
      this.pagination.current = current
      this.listCommunitySpaceConfirms()
    }
  }
}
</script>

<style lang="scss" scoped>
.community-space-confirm-container {
  padding: 20px;
  
  .write-off-card, .search-card, .table-card {
    margin-bottom: 20px;
  }
  
  .result-card {
    margin-top: 15px;
    
    .result-item {
      display: flex;
      justify-content: space-between;
      margin-bottom: 10px;
      font-size: 14px;
      
      .label {
        font-weight: bold;
        color: #606266;
      }
    }
  }
  
  .full-width {
    width: 100%;
  }
  
  .full-width-btn {
    width: 100%;
  }
  
  .mb-10 {
    margin-bottom: 10px;
  }
  
  .mt-20 {
    margin-top: 20px;
  }
}
</style>