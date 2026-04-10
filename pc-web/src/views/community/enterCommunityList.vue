<template>
  <div class="enter-community-container">
    <el-card v-if="showPage === 'myCommunity'">
      <div slot="header" class=" flex justify-between">
        <span>{{ $t('enterCommunity.myCommunity') }}</span>
        <el-button type="primary" size="small"  @click="showHcUse">
          <i class="el-icon-plus"></i>
          {{ $t('enterCommunity.commercialProcess') }}
        </el-button>
      </div>

      <el-table :data="communityList" border v-loading="loading">
        <el-table-column prop="provName" :label="$t('enterCommunity.province')" align="center" />
        <el-table-column prop="cityName" :label="$t('enterCommunity.city')" align="center" />
        <el-table-column prop="areaName" :label="$t('enterCommunity.district')" align="center" width="150" />
        <el-table-column prop="name" :label="$t('enterCommunity.communityName')" align="center" width="200" />
        <el-table-column prop="communityId" :label="$t('enterCommunity.communityCode')" align="center" />
        <el-table-column prop="tel" :label="$t('enterCommunity.servicePhone')" align="center" />
        <el-table-column prop="communityArea" :label="$t('enterCommunity.area')" align="center" />
        <el-table-column prop="startTime" :label="$t('enterCommunity.startTime')" align="center" />
        <el-table-column prop="endTime" :label="$t('enterCommunity.endTime')" align="center" />
        <el-table-column :label="$t('enterCommunity.status')" align="center">
          <template slot-scope="{ row }">
            {{ statusMap[row.auditStatusCd] || row.auditStatusCd }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('enterCommunity.operation')" align="center" width="120">
          <template slot-scope="{ row }">
            <el-button v-if="row.auditStatusCd === '1100'" type="text" size="small" @click="openEditDialog(row)">
              {{ $t('enterCommunity.modify') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination class="pagination" :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]"
        :page-size="pagination.size" :total="pagination.total" layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange" @current-change="handlePageChange" />
    </el-card>

    <el-card v-else>
      <div slot="header" class=" flex justify-between">
        <div>{{ $t('enterCommunity.commercialProcess') }}</div>
        <el-button type="primary" size="small"  @click="goBack">
          <i class="el-icon-close"></i>
          {{ $t('enterCommunity.back') }}
        </el-button>
      </div>

      <div class="commercial-content">
        <img src="/img/hc_use.png" alt="Commercial Process">
      </div>
    </el-card>

    <edit-community-area ref="editDialog" @success="fetchCommunityList" />
  </div>
</template>

<script>
import { listMyCommunity } from '@/api/community/enterCommunityApi'
import EditCommunityArea from '@/components/community/editCommunityArea.vue'

export default {
  name: 'EnterCommunityList',
  components: { EditCommunityArea },
  data() {
    return {
      showPage: 'myCommunity',
      loading: false,
      communityList: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      statusMap: {
        '1000': this.$t('enterCommunity.statusOptions.1000'),
        '1100': this.$t('enterCommunity.statusOptions.1100'),
        '1200': this.$t('enterCommunity.statusOptions.1200')
      }
    }
  },
  mounted() {
    this.fetchCommunityList()
  },
  methods: {
    async fetchCommunityList() {
      this.loading = true
      try {
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          communityId: this.getCommunityId()
        }
        const data = await listMyCommunity(params)
        this.communityList = data
        this.pagination.total = data.length
      } catch (error) {
        this.$message.error(error.message || this.$t('enterCommunity.fetchError'))
      } finally {
        this.loading = false
      }
    },
    openEditDialog(community) {
      this.$refs.editDialog.open(community)
    },
    showHcUse() {
      this.showPage = 'hcUse'
    },
    goBack() {
      this.showPage = 'myCommunity'
    },
    handlePageChange(page) {
      this.pagination.current = page
      this.fetchCommunityList()
    },
    handleSizeChange(size) {
      this.pagination.size = size
      this.fetchCommunityList()
    }
  }
}
</script>

<style scoped>
.enter-community-container {
  padding: 20px;
}

.clearfix:after {
  content: "";
  display: table;
  clear: both;
}

.float-right {
  float: right;
}

.commercial-content {
  text-align: center;
  padding: 20px 0;
}

.commercial-content img {
  max-width: 100%;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}
</style>